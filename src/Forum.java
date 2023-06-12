import javax.swing.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Forum class to handle the forums in courses.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Forum {
    private static final SimpleDateFormat SF1 = new SimpleDateFormat("[MM/dd/YY HH:mm]");
    public static ClientConnection s;
    private String forumName;
    private String courseName;
    private ArrayList<Reply> replies;
    private String time;
    private ArrayList<String> scores = new ArrayList<>();
    private int rID = 0;

    public Forum(String forumName, String courseName) {
        this.forumName = forumName;
        this.replies = new ArrayList<Reply>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = SF1.format(timestamp);
        this.courseName = courseName;
        this.replies = replyReadLines(s.oos, s.ois);
        this.scores = scoreReadLines(s.oos, s.ois);
    }

    public void writeScores() {
        String forumScoreFile = this.courseName + "_" + this.forumName + "_scores.txt";
        File f = new File(forumScoreFile);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(f))) {
            for (String s : scores) {
                pw.write(s + "\n");
            }
        } catch (Exception e) {
            return;
        }
    }


    public ArrayList<String> getScores() {
        this.scores = scoreReadLines(s.oos, s.ois);
        return scores;
    }

    public void addComment(int replyNum, String message, Person user) {
        getReply(replyNum).addComment(message, user);
        replyWriteLines(s.oos, s.ois);
    }


    public ArrayList<Reply> getReplies() {
        this.replies = replyReadLines(s.oos, s.ois);
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public ArrayList<Reply> getStudentReplies(String username) {
        ArrayList<Reply> temp = new ArrayList<>();
        for (Reply r : replies) {
            if (username.equals(r.getPoster())) {
                temp.add(r);
            }
        }
        return temp;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Reply getReply(int index) {
        return replies.get(index);
    }

    public void removeReply(Reply reply) {
        replies.remove(reply);
    }

    public boolean addScore(String username, String score) {
        boolean scoreExists = false;

        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).substring(0, scores.get(i).indexOf(":")).equals(username)) {
                scores.set(i, username + ":" + score);
                scoreExists = true;
                break;
            }
        }
        if (!scoreExists) {
            scores.add(username + ":" + score);
        }
        writeScores();
        return scoreExists;
    }

    public String viewScore(Person user) {
        if (user instanceof Student) {
            String[] thisSplit;
            for (String s : scores) {
                thisSplit = s.split(":");
                if (thisSplit[0].equals(user.getUsername())) {
                    return thisSplit[1];
                }
            }
        } else {
            return "Error, Person is not a Student";
        }
        return "Error, No Score available";
    }

    public String getForumName() {
        return this.forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addReply(String reply, Person poster) {
        Reply newReply = new Reply(reply, poster);
        replies.add(0, newReply);
        replyWriteLines(s.oos, s.ois);
        JOptionPane.showMessageDialog(null, "Reply successfully added!", "Forum",
                JOptionPane.PLAIN_MESSAGE);
    }

    public String toString() {
        String temp = "";
        String tempWords = "";
        String tempDashes = "";

        tempWords = tempWords + "\n" + forumName + "'s Replies " + time + "\n";
        for (int i = 0; i < (tempWords.length() - 2); i++) {
            tempDashes = tempDashes + "-";
        }
        temp = temp + tempDashes + tempWords + tempDashes + "\n";

        if ((replies == null) || replies.isEmpty()) {
            temp = temp + "NO REPLIES";
        } else {
            int i = 1;
            for (Reply r : replies) {
                temp += "Reply " + i + ") " + r.toString();
                i++;
            }
        }
        temp += "\n";

        return temp;
    }

    public ArrayList<Reply> replyReadLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String fileName = this.courseName + "_" + this.forumName + "_r.txt";
        ArrayList<String> requestStr = new ArrayList<>();
        requestStr.add("R");
        requestStr.add(fileName);

        ArrayList<Reply> reps = new ArrayList<>();
        try {
            oos.writeObject(requestStr);
            oos.flush();
            ArrayList<String> dataStr = (ArrayList<String>) ois.readObject();


            for (int i = 0; i < dataStr.size(); i++) {
                String[] lineSplit = dataStr.get(i).split("~");
                Reply reply = new Reply(lineSplit[2], lineSplit[0]);
                reply.setTime(lineSplit[1]);
                reply.setVotes(Integer.parseInt(lineSplit[3]));

                i++;

                if (!dataStr.get(i).equals("No votes yet")) {
                    ArrayList<String> votedStudents = new ArrayList<>(Arrays.asList(dataStr.get(i).split("~")));
                    if (votedStudents.size() > 0) {
                        reply.setVotedStudents(votedStudents);
                    }
                }

                i++;

                if (!dataStr.get(i).equals("No comments yet")) {
                    ArrayList<String> comments = new ArrayList<>(Arrays.asList(dataStr.get(i).split("~")));
                    if (comments.size() > 0) {
                        reply.setComments(comments);
                    }

                }
                reps.add(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reps;
    }

    public void replyWriteLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String forumFile = this.courseName + "_" + this.forumName + "_r.txt";
        ArrayList<String> dataStr = new ArrayList<>();
        dataStr.add("W");
        dataStr.add(forumFile);
        for (Reply reply : replies) {
            String output = "";
            output += reply.getPoster() + "~" + reply.getTime() + "~" + reply.getContent() + "~" +
                    reply.getVotes();
            dataStr.add(output);
            if (reply.getVotedStudents().size() > 0) {
                output = String.join("~", reply.getVotedStudents());
            } else {
                output = "No votes yet";
            }
            dataStr.add(output);
            if (reply.getComments().size() > 0) {
                output = String.join("~", reply.getComments());
            } else {
                output = "No comments yet";
            }
            dataStr.add(output);
        }
        try {
            oos.writeObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> scoreReadLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String forumScoreFile = this.courseName + "_" + this.forumName + "_scores.txt";
        ArrayList<String> requestStr = new ArrayList<>();
        requestStr.add("R");
        requestStr.add(forumScoreFile);
        ArrayList<String> dataStr = new ArrayList<>();
        try {
            oos.writeObject(requestStr);
            oos.flush();
            dataStr = (ArrayList<String>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataStr;
    }

    public void scoreWriteLines(ObjectOutputStream oos, ObjectInputStream ois) {
        String forumScoreFile = this.courseName + "_" + this.forumName + "_scores.txt";
        ArrayList<String> dataStr = new ArrayList<>();
        dataStr.add("W");
        dataStr.add(forumScoreFile);
        for (String s : scores) {
            dataStr.add(s);
        }
        try {
            oos.writeObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}