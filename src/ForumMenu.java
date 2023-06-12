import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * ForumMenu class that implements the course menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/23/2022
 */

public class ForumMenu {

    JFrame frame;
    Course course;
    int forumNum;
    Person user;
    Forum selectedForum;
    ArrayList<Course> courses;
    int courseNum;

    JButton deleteButton;
    JButton addReplyButton;
    JButton addCommentButton;
    JButton editButton;
    JButton viewScoresButton;
    JButton backButton;
    JButton voteButton;
    JButton manualUploadButton;
    JButton fileUpload;
    JButton refreshButton;


    JPanel userInfo;

    public ForumMenu(JFrame frame, Course course, int forumNum, ArrayList<Course> courses,
                     int courseNum, Person user) {
        this.selectedForum = course.getForum(forumNum - 1);
        this.course = course;
        this.forumNum = forumNum;
        this.frame = frame;
        this.user = user;
        this.courses = courses;
        this.courseNum = courseNum;

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));


        addReplyButton = new JButton("Add Reply");
        addReplyButton.addActionListener(actionListener);

        addCommentButton = new JButton("Add Comment to Reply");
        addCommentButton.addActionListener(actionListener);

        editButton = new JButton("Edit reply");
        editButton.addActionListener(actionListener);

        deleteButton = new JButton("Delete reply");
        deleteButton.addActionListener(actionListener);

        viewScoresButton = new JButton("View Scores");
        viewScoresButton.addActionListener(actionListener);

        voteButton = new JButton("Vote on Reply");
        voteButton.addActionListener(actionListener);

        backButton = new JButton("Back");
        backButton.addActionListener(actionListener);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(actionListener);

        content.removeAll();
        content.repaint();

        userInfo = new JPanel();
        JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
        userInfo.add(userLabel);

        content.add(userInfo);
        content.add(new JLabel("<html>" + selectedForum.toString().replaceAll("\n", "<br>")));
        content.add(addReplyButton);
        if (user instanceof Student) {
            content.add(addCommentButton);
            content.add(voteButton);
            content.add(viewScoresButton);
        } else {
            content.add(editButton);
            content.add(deleteButton);
        }
        content.add(backButton);
        content.add(refreshButton);
        frame.setVisible(true);
    }    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Container content = frame.getContentPane();

            if (e.getSource() == addReplyButton) {
                JButton backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                    }
                });

                manualUploadButton = new JButton("Manual Upload");
                manualUploadButton.addActionListener(actionListener);

                fileUpload = new JButton("File Upload");
                fileUpload.addActionListener(actionListener);


                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("Do you want to enter the reply manually or upload a file with the " +
                        "reply?"));
                content.add(new JLabel("[Note that the file text still has to comply " +
                        "with the naming standards]"));
                content.add(manualUploadButton);
                content.add(fileUpload);
                content.add(backButton);
                frame.setVisible(true);
            }
            if (e.getSource() == voteButton) {

                if (selectedForum.getReplies().size() == 0) {
                    JOptionPane.showMessageDialog(null, "No replies to vote on!",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JButton backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                        }
                    });

                    JTextField replyNumText = new JTextField(3);
                    JButton enterBTN = new JButton("Enter");
                    enterBTN.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (isInteger(replyNumText.getText())) {
                                int replyNum = Integer.parseInt(replyNumText.getText());
                                if (replyNum <= selectedForum.getReplies().size() && replyNum > 0) {
                                    replyNum = replyNum - 1;
                                    Reply editRep = selectedForum.getReply(replyNum);
                                    editRep.incrementVote(user, selectedForum);
                                    ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses,
                                            courseNum, user);

                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Input",
                                            "Forum Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                    });

                    content.removeAll();
                    content.repaint();

                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(new JLabel("<html>" + selectedForum.toString().replaceAll("\n",
                            "<br>")));
                    content.add(new JLabel("Enter the reply number to vote on:"));
                    content.add(replyNumText);
                    content.add(enterBTN);

                    content.add(backButton);
                    frame.setVisible(true);
                }
            }

            if (e.getSource() == viewScoresButton) {
                String score = selectedForum.viewScore(user);
                if (score.equals("Error, No Score available")) {
                    JOptionPane.showMessageDialog(null, "There are no scores available for" +
                                    " you currently.", "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else if (score.equals("Error, Person is not a Student")) {
                    JOptionPane.showMessageDialog(null, "Error! Person is not a Student",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JButton backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                        }
                    });

                    content.removeAll();
                    content.repaint();

                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(new JLabel("Your score: " + score + "/100"));
                    content.add(backButton);
                    frame.setVisible(true);
                }
            }

            if (e.getSource() == addCommentButton) {
                if (selectedForum.getReplies().size() == 0) {
                    JOptionPane.showMessageDialog(null, "No replies to comment on!",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {

                    JButton backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                        }
                    });

                    JTextField replyNumText = new JTextField(3);
                    JTextField replyMessage = new JTextField(15);
                    JButton enterBTN = new JButton("Enter");
                    enterBTN.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (isInteger(replyNumText.getText())) {
                                int replyNum = Integer.parseInt(replyNumText.getText());
                                if (replyNum <= selectedForum.getReplies().size() && replyNum > 0) {
                                    replyNum = replyNum - 1;
                                    Reply editRep = selectedForum.getReply(replyNum);
                                    if (isValidMessage(replyMessage.getText())) {
                                        selectedForum.addComment(replyNum, replyMessage.getText(), user);
                                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses,
                                                courseNum, user);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Input",
                                            "Forum Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                    });


                    content.removeAll();
                    content.repaint();

                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(new JLabel("<html>" + selectedForum.toString().replaceAll("\n",
                            "<br>")));
                    content.add(new JLabel("Enter the reply number to comment on:"));
                    content.add(replyNumText);
                    content.add(new JLabel("Enter comment message"));
                    content.add(replyMessage);
                    content.add(enterBTN);

                    content.add(backButton);
                    frame.setVisible(true);
                }
            }

            if (e.getSource() == editButton) {
                JButton backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                    }
                });

                JTextField replyNumText = new JTextField(3);
                JTextField replyMessage = new JTextField(15);
                JButton enterBTN = new JButton("Enter");
                enterBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isInteger(replyNumText.getText())) {
                            int replyNum = Integer.parseInt(replyNumText.getText());
                            if (replyNum <= selectedForum.getReplies().size() && replyNum > 0) {
                                replyNum = replyNum - 1;
                                Reply editRep = selectedForum.getReply(replyNum);
                                if (isValidMessage(replyMessage.getText())) {
                                    editRep.setContent(replyMessage.getText());
                                    ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum,
                                            user);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Input",
                                        "Forum Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }
                });

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("<html>" + selectedForum.toString().replaceAll("\n",
                        "<br>")));
                content.add(new JLabel("Enter reply number to edit"));
                content.add(replyNumText);
                content.add(new JLabel("Enter new reply message"));
                content.add(replyMessage);
                content.add(enterBTN);

                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == deleteButton) {
                JButton backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                    }
                });

                JTextField replyNumText = new JTextField(3);
                JButton enterBTN = new JButton("Enter");
                enterBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isInteger(replyNumText.getText())) {
                            int replyNum = Integer.parseInt(replyNumText.getText());
                            if (replyNum <= selectedForum.getReplies().size() && replyNum > 0) {
                                replyNum = replyNum - 1;
                                Reply editRep = selectedForum.getReply(replyNum);
                                selectedForum.removeReply(editRep);
                                JOptionPane.showMessageDialog(null, "Success",
                                        "Forum Menu",
                                        JOptionPane.INFORMATION_MESSAGE);
                                ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);

                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Input",
                                        "Forum Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }
                });


                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("<html>" + selectedForum.toString().replaceAll("\n",
                        "<br>")));
                content.add(new JLabel("Enter reply number to delete"));
                content.add(replyNumText);
                content.add(enterBTN);

                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == backButton) {
                CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
            }

            if (e.getSource() == manualUploadButton) {
                JTextField replyMessage = new JTextField(10);
                JButton enterBTN = new JButton("Enter");
                enterBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isValidMessage(replyMessage.getText())) {
                            selectedForum.addReply(replyMessage.getText(), user);
                            ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                        }
                    }
                });

                JButton backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                    }
                });

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("Enter the reply message:"));
                content.add(replyMessage);
                content.add(enterBTN);
                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == fileUpload) {
                JTextField replyMessage = new JTextField(10);
                JButton enterBTN = new JButton("Enter");
                enterBTN.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File f = new File(replyMessage.getText());
                        if (!f.exists() || f.isDirectory()) {
                            JOptionPane.showMessageDialog(null, "The file does not exist! " +
                                            "Please try again", "Forum Menu",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            String newReply = "";
                            boolean gotReply = false;
                            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                                String temp = "";
                                while ((temp = br.readLine()) != null) {
                                    newReply += temp;
                                }
                                gotReply = true;
                                if (!isValidMessage(newReply)) {
                                    JOptionPane.showMessageDialog(null, "Edit the file and " +
                                                    "try again", "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                    gotReply = false;
                                    newReply = "";
                                }
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                            if (gotReply) {
                                selectedForum.addReply(newReply, user);
                                newReply = "";
                                ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                            }
                        }
                    }
                });

                JButton backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ForumMenu forumMenu = new ForumMenu(frame, course, forumNum, courses, courseNum, user);
                    }
                });

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("Please enter the file path which you would like to upload:"));
                content.add(replyMessage);
                content.add(enterBTN);
                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == refreshButton) {
                Menu.refreshButton.doClick();
            }
        }
    };

    public boolean isValidTitle(String input) {
        if (input == null || input.equals("")) {
            return false;
        } else if (input.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Cannot contain spaces", "Forum Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (input.contains("~")) {
            JOptionPane.showMessageDialog(null, "Cannot contain ~", "Forum Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isValidMessage(String input) {
        if (input == null || input.equals("")) {
            JOptionPane.showMessageDialog(null, "Cannot be empty", "Forum Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (input.contains("~")) {
            JOptionPane.showMessageDialog(null, "Cannot contain ~", "Forum Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Input", "Forum Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


}
