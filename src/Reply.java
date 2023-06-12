import javax.swing.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Reply class, to create and manage replies.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Reply {
    private static final SimpleDateFormat SF1 = new SimpleDateFormat("[MM/dd/YY HH:mm]");
    public static ClientConnection s;
    private String content;
    private String poster;
    private int votes;
    private ArrayList<String> comments;
    private String time;
    private ArrayList<String> votedStudents = new ArrayList<>();
    private String forumAndCourseName;

    public Reply(String content, Person poster) {
        this.content = content;
        this.votes = 0;
        this.comments = new ArrayList<String>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = SF1.format(timestamp);
        this.poster = poster.getUsername();
        this.forumAndCourseName = "";
    }

    public Reply(String content, String poster) {
        this.content = content;
        this.votes = 0;
        this.comments = new ArrayList<String>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = SF1.format(timestamp);
        this.poster = poster;
        this.forumAndCourseName = "";

    }


    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getVotedStudents() {
        return votedStudents;
    }

    public void setVotedStudents(ArrayList<String> votedStudents) {
        this.votedStudents = votedStudents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getForumAndCourseName() {
        return forumAndCourseName;
    }

    public void setForumAndCourseName(String forumAndCourseName) {
        this.forumAndCourseName = forumAndCourseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean incrementVote(Person user, Forum forum) {
        if (user instanceof Teacher) {
            JOptionPane.showMessageDialog(null, "Error: Teachers cannot vote", "Reply",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (String s : votedStudents) {
            if (user.getUsername().equals(s)) {
                JOptionPane.showMessageDialog(null, "Error: Student has already voted",
                        "Reply",
                        JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        votedStudents.add(user.getUsername());
        setVotes(++this.votes);
        forum.replyWriteLines(s.oos, s.ois);
        JOptionPane.showMessageDialog(null, "Reply successfully upvoted!", "Reply",
                JOptionPane.PLAIN_MESSAGE);
        return true;
    }

    public void addComment(String commentContent, Person commenter) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String commentTime = SF1.format(timestamp);
        String temp = String.format("@%s %s: %s", commenter.getUsername(), commentTime, commentContent);
        this.comments.add(0, temp);
        JOptionPane.showMessageDialog(null, "Comment successfully added!", "Reply",
                JOptionPane.PLAIN_MESSAGE);
    }

    public String toString() {
        String temp = String.format("@%s %s: %s\nVotes: %d\n", poster, time, content, votes);
        int i = 1;
        for (String s : comments) {
            temp = temp + "    Comment " + i + ") " + s + "\n";
            i++;
        }
        return temp;
    }
}