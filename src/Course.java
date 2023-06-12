import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * Course class to handle the courses.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Course {
    public static ClientConnection s;
    private String courseName;
    private ArrayList<Forum> forums;

    public Course(String courseName) {
        this.courseName = courseName;
        this.forums = forumReadLines(s.oos, s.ois);
    }


    public boolean isValidForum(String title) {
        for (Forum forum : forums) {
            if (forum.getForumName().equals(title)) {
                JOptionPane.showMessageDialog(null, "Forum already exists", "Course",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void addForum(Forum newForum) {
        forums.add(newForum);
        forumWriteLines(s.oos, s.ois);
    }

    public void removeForum(int index) {
        forums.remove(index);
        forumWriteLines(s.oos, s.ois);
    }

    public void editForum(int index, String name) {
        this.getForum(index).setForumName(name);
        forumWriteLines(s.oos, s.ois);
    }

    public ArrayList<Forum> getForums() {
        this.forums = forumReadLines(s.oos, s.ois);
        return forums;
    }

    public void setForums(ArrayList<Forum> forums) {
        this.forums = forums;
    }

    public Forum getForum(int index) {
        return forums.get(index);
    }

    public String toString() {
        String temp = "";
        String tempWords = "";
        String tempDashes = "";

        tempWords = tempWords + "\n" + courseName + "'s Forums\n";
        for (int i = 0; i < (tempWords.length() - 2); i++) {
            tempDashes = tempDashes + "-";
        }
        temp = temp + tempDashes + tempWords + tempDashes + "\n";

        if ((forums == null) || forums.isEmpty()) {
            temp = temp + "NO FORUMS\n";
        } else {
            int i = 1;
            for (Forum f : forums) {
                temp += "Forum " + i + ") " + f.getForumName() + "\n";
                i++;
            }
        }
        temp += "\n";

        return temp;
    }

    public ArrayList<Forum> forumReadLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String fileName = this.getCourseName() + "_forums.txt";
        ArrayList<String> requestStr = new ArrayList<>();
        requestStr.add("R");
        requestStr.add(fileName);
        ArrayList<Forum> list = new ArrayList<>();
        try {
            oos.writeObject(requestStr);
            oos.flush();
            ArrayList<String> dataStr = (ArrayList<String>) ois.readObject();

            for (String s : dataStr) {
                String[] lineSplit = s.split("~");
                Forum forum = new Forum(lineSplit[0], courseName);
                forum.setTime(lineSplit[1]);
                list.add(forum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void forumWriteLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String forumFile = courseName + "_forums.txt";
        ArrayList<String> dataStr = new ArrayList<>();
        dataStr.add("W");
        dataStr.add(forumFile);
        for (Forum forum : forums) {
            dataStr.add(forum.getForumName() + "~" + forum.getTime());
        }
        try {
            oos.writeObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}