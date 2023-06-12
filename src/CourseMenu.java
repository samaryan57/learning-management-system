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
 * CourseMenu class that implements the course menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/23/2022
 */

public class CourseMenu {
    Course selectedCourse;
    ArrayList<Course> courses;
    ArrayList<Forum> forums;
    Person user;
    int courseNum;

    JFrame frame;

    JButton createForumButton;
    JButton deleteForumButton;
    JButton editForumButton;
    JButton viewRepComButton;
    JButton viewPopRepButton;
    JButton gradeButton;
    JButton backButton;
    JButton manualUploadButton;
    JButton fileUploadButton;
    JButton popRepsYes;
    JButton popRepsNo;
    JButton enterButton;
    JButton refreshButton;

    JTextField forumNameField;
    JTextField filePath;
    JTextField forumNumField;
    JTextField studentNumField;
    JTextField scoreField;

    JLabel forumNamePrompt;
    JLabel forumsList;
    JLabel popRepsDashboard;

    JPanel userInfo;

    String forumName;

    int replyNum;
    String score;
    int numScore;

    public CourseMenu(JFrame frame, int courseNum, ArrayList<Course> courses, Person user) {
        this.frame = frame;
        this.user = user;
        this.courses = courses;
        this.courseNum = courseNum;

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));

        selectedCourse = courses.get(courseNum);

        forumsList = new JLabel("<html>" + selectedCourse.toString().replaceAll("\n", "<br>"));

        createForumButton = new JButton("Create Forum");
        createForumButton.addActionListener(actionListener);

        deleteForumButton = new JButton("Delete Forum");
        deleteForumButton.addActionListener(actionListener);

        editForumButton = new JButton("Edit Forum");
        editForumButton.addActionListener(actionListener);

        viewRepComButton = new JButton("View Replies/Comments");
        viewRepComButton.addActionListener(actionListener);

        viewPopRepButton = new JButton("View Popular Replies Dashboard");
        viewPopRepButton.addActionListener(actionListener);

        gradeButton = new JButton("Grade");
        gradeButton.addActionListener(actionListener);

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
        content.add(forumsList);
        if (user instanceof Teacher) {
            content.add(createForumButton);
            content.add(deleteForumButton);
            content.add(editForumButton);
            content.add(viewPopRepButton);
            content.add(gradeButton);
        }
        content.add(viewRepComButton);
        content.add(backButton);
        content.add(refreshButton);
        frame.setVisible(true);

    }    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Container content = frame.getContentPane();
            forumsList = new JLabel("<html>" + courses.get(courseNum).toString().replaceAll("\n",
                    "<br>"));

            if (e.getSource() == createForumButton) {
                forumNamePrompt = new JLabel("Do you want to enter the forum name manually or upload a " +
                        "file with " +
                        "the forum name?");

                manualUploadButton = new JButton("Manual Upload");
                manualUploadButton.addActionListener(actionListener);

                fileUploadButton = new JButton("File Upload");
                fileUploadButton.addActionListener(actionListener);

                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                    }
                });

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(forumNamePrompt);
                content.add(new JLabel("[Note that the file text still has to comply with " +
                        "the naming standards]"));
                content.add(new JLabel("[.txt only]"));
                content.add(manualUploadButton);
                content.add(fileUploadButton);
                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == deleteForumButton) {
                if (selectedCourse.getForums().size() == 0) {
                    JOptionPane.showMessageDialog(null, "NO FORUMS TO DELETE",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);

                } else {
                    forumNumField = new JTextField(3);
                    backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                        }
                    });
                    enterButton = new JButton("Enter");
                    enterButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String input = forumNumField.getText();
                            int forumNum = 0;
                            if (isInteger(input)) {
                                int inputInt = Integer.parseInt(input);
                                if ((inputInt > 0) && (inputInt <= selectedCourse.getForums().size())) {
                                    forumNum = Integer.parseInt(input);
                                    selectedCourse.removeForum(forumNum - 1);
                                    JOptionPane.showMessageDialog(null, "Forum deleted!",
                                            "Course Menu",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                            "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                        "Course Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    content.removeAll();
                    content.repaint();
                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(forumsList);
                    content.add(new JLabel("Enter the forum number to delete:"));
                    content.add(forumNumField);
                    content.add(enterButton);
                    content.add(backButton);
                    frame.setVisible(true);


                }
            }

            if (e.getSource() == editForumButton) {
                if (selectedCourse.getForums().size() == 0) {
                    JOptionPane.showMessageDialog(null, "NO FORUMS TO EDIT",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    forumNumField = new JTextField(3);

                    JTextField newNameText = new JTextField(9);

                    backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                        }
                    });
                    enterButton = new JButton("Enter");
                    enterButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String newName = newNameText.getText();

                            int forumNum = 0;
                            String input = forumNumField.getText();
                            if (isInteger(input)) {
                                int inputInt = Integer.parseInt(input);
                                if ((inputInt > 0) && (inputInt <= selectedCourse.getForums().size())) {
                                    forumNum = Integer.parseInt(input) - 1;
                                    if (selectedCourse.isValidForum(newName) && isValidTitle(newName)) {
                                        selectedCourse.editForum(forumNum, newName);
                                        JOptionPane.showMessageDialog(null, "Forum changed!",
                                                "Course Menu",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);

                                    }

                                } else {
                                    JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                            "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                        "Course Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });


                    content.removeAll();
                    content.repaint();
                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(forumsList);
                    content.add(new JLabel("Enter the forum number to edit:"));
                    content.add(forumNumField);
                    content.add(new JLabel("Enter new forum name:"));
                    content.add(newNameText);
                    content.add(enterButton);
                    content.add(backButton);
                    frame.setVisible(true);

                }
            }

            if (e.getSource() == viewRepComButton) {
                if ((selectedCourse != null) && !selectedCourse.getForums().isEmpty()) {
                    forumNumField = new JTextField(3);
                    backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                        }
                    });
                    enterButton = new JButton("Enter");
                    enterButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int forumNum = 0;
                            String input = forumNumField.getText();
                            if (isInteger(input)) {
                                int inputInt = Integer.parseInt(input);
                                if ((inputInt > 0) && (inputInt <= selectedCourse.getForums().size())) {
                                    forumNum = Integer.parseInt(input);
                                    ForumMenu forumMenu = new ForumMenu(frame, selectedCourse, forumNum, courses,
                                            courseNum, user);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                            "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Enter Valid Input",
                                        "Course Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    content.removeAll();
                    content.repaint();
                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(forumsList);
                    content.add(new JLabel("Enter the forum number to view/edit its replies:"));
                    content.add(forumNumField);
                    content.add(enterButton);
                    content.add(backButton);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "NO FORUMS TO SHOW!",
                            "Course Menu", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == viewPopRepButton) {
                if (selectedCourse.getForums() == null || selectedCourse.getForums().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "NO FORUMS TO SHOW",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    forums = selectedCourse.getForums();
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < forums.size(); i++) {
                        ArrayList<Reply> replies = forums.get(i).getReplies();
                        if (replies == null || replies.isEmpty()) {
                            continue;
                        }
                        int maxVotes = 0;
                        String student = "";
                        for (Reply r : replies) {
                            if (r.getVotes() > maxVotes) {
                                maxVotes = r.getVotes();
                                student = r.getPoster();
                            }
                        }
                        result.append("Forum " + (i + 1) + ") " + forums.get(i).getForumName() + "\n" +
                                "Student with maximum votes: " + student + "  No. of upvotes: " + maxVotes + "\n");
                    }
                    popRepsDashboard = new JLabel("<html>" + String.valueOf(result).replaceAll("\n",
                            "<br>"));

                    popRepsYes = new JButton("Yes");
                    popRepsYes.addActionListener(actionListener);

                    popRepsNo = new JButton("No");
                    popRepsNo.addActionListener(actionListener);

                    content.removeAll();
                    content.repaint();
                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(popRepsDashboard);
                    content.add(new JLabel("Would you like to see a sorted dashboard by the order of votes?"));
                    content.add(popRepsYes);
                    content.add(popRepsNo);
                    frame.setVisible(true);

                }
            }

            if (e.getSource() == gradeButton) {
                if (selectedCourse.getForums().size() == 0) {
                    JOptionPane.showMessageDialog(null, "NO FORUMS TO GRADE",
                            "Course Menu",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    forumNumField = new JTextField(3);
                    backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                        }
                    });
                    enterButton = new JButton("Enter");
                    enterButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String input = forumNumField.getText();
                            int forumNum = 0;

                            if (isInteger(input)) {
                                int inputInt = Integer.parseInt(input);
                                if ((inputInt > 0) && (inputInt <= selectedCourse.getForums().size())) {
                                    forumNum = Integer.parseInt(input);
                                    Forum forum = selectedCourse.getForum(forumNum - 1);

                                    if (forum.getReplies().size() == 0) {
                                        JOptionPane.showMessageDialog(null,
                                                "NO REPLIES TO GRADE", "Course Menu",
                                                JOptionPane.ERROR_MESSAGE);
                                    } else {

                                        StringBuilder students = new StringBuilder();
                                        for (int i = 0; i < forum.getReplies().size(); i++) {
                                            String thisPoster = forum.getReplies().get(i).getPoster();
                                            students.append((i + 1) + ") " + thisPoster + "\n");
                                        }

                                        JLabel studentList = new JLabel("<html>" +
                                                students.toString().replaceAll("\n", "<br>"));

                                        studentNumField = new JTextField(10);
                                        enterButton = new JButton("Enter");
                                        enterButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String newInput = studentNumField.getText();
                                                if (isInteger(newInput)) {
                                                    int inputInt = Integer.parseInt(newInput);
                                                    if ((inputInt > 0) && (inputInt <= forum.getReplies().size())) {
                                                        replyNum = Integer.parseInt(newInput);
                                                        ArrayList<Reply> studentReplies =
                                                                forum.getStudentReplies(forum.getReplies().get(replyNum
                                                                        - 1).getPoster());
                                                        content.removeAll();
                                                        content.repaint();
                                                        userInfo = new JPanel();
                                                        JLabel userLabel = new JLabel("Logged in as: " +
                                                                user.getUsername());
                                                        userInfo.add(userLabel);

                                                        content.add(userInfo);
                                                        content.add(new JLabel("All replies by" +
                                                                " the selected student:"));
                                                        for (int i = 0; i < studentReplies.size(); i++) {
                                                            String temp = String.format("@%s %s: %s\nVotes: %d\n",
                                                                    studentReplies.get(i).getPoster(),
                                                                    studentReplies.get(i).getTime(),
                                                                    studentReplies.get(i).getContent(),
                                                                    studentReplies.get(i).getVotes());
                                                            content.add(new JLabel(temp));
                                                        }


                                                        scoreField = new JTextField(3);
                                                        enterButton = new JButton("Enter");
                                                        enterButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                score = scoreField.getText();
                                                                if (isInteger(score)) {
                                                                    numScore = Integer.parseInt(score);
                                                                    Reply reply = forum.getReplies().get(replyNum - 1);
                                                                    boolean scoreExisted =
                                                                            forum.addScore(reply.getPoster(),
                                                                                    String.valueOf(numScore));
                                                                    if (scoreExisted) {
                                                                        JOptionPane.showMessageDialog(null,
                                                                                "Old score updated!",
                                                                                "Course Menu",
                                                                                JOptionPane.INFORMATION_MESSAGE);
                                                                    } else {
                                                                        JOptionPane.showMessageDialog(null,
                                                                                "Score successfully added!",
                                                                                "Course Menu",
                                                                                JOptionPane.INFORMATION_MESSAGE);
                                                                    }
                                                                    CourseMenu courseMenu = new CourseMenu(frame,
                                                                            courseNum, courses, user);
                                                                }
                                                            }
                                                        });

                                                        content.add(new JLabel("Based on the above replies," +
                                                                " assign a score to " +
                                                                "the student out of 100:"));
                                                        content.add(scoreField);
                                                        content.add(enterButton);
                                                        content.add(backButton);
                                                        frame.setVisible(true);

                                                    } else {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Enter Valid Input", "Course Menu",
                                                                JOptionPane.ERROR_MESSAGE);
                                                    }
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Enter Valid Input", "Course Menu",
                                                            JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                        });

                                        content.removeAll();
                                        content.repaint();
                                        userInfo = new JPanel();
                                        JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                                        userInfo.add(userLabel);

                                        content.add(userInfo);
                                        content.add(studentList);
                                        content.add(new JLabel("Enter the student number whose" +
                                                " replies need to be displayed:"));
                                        content.add(studentNumField);
                                        content.add(enterButton);
                                        content.add(backButton);
                                        frame.setVisible(true);

                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Enter Valid Input", "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Enter Valid Input", "Course Menu",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    content.removeAll();
                    content.repaint();
                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    content.add(forumsList);
                    content.add(new JLabel("Enter the forum number to grade:"));
                    content.add(forumNumField);
                    content.add(enterButton);
                    content.add(backButton);
                    frame.setVisible(true);
                }

            }

            if (e.getSource() == backButton) {
                Menu menu = new Menu(frame, user);
            }

            if (e.getSource() == manualUploadButton) {
                forumNameField = new JTextField(15);
                enterButton = new JButton("Enter");
                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                    }
                });
                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (selectedCourse.isValidForum(forumNameField.getText())
                                && isValidTitle(forumNameField.getText())) {
                            Forum newForum = new Forum(forumNameField.getText(), selectedCourse.getCourseName());
                            selectedCourse.addForum(newForum);
                            JOptionPane.showMessageDialog(null,
                                    "Forum created!", "Course Menu",
                                    JOptionPane.INFORMATION_MESSAGE);
                            CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                        }
                    }
                });
                content.removeAll();
                content.repaint();
                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("Enter the forum name:"));
                content.add(forumNameField);
                content.add(enterButton);
                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == fileUploadButton) {
                filePath = new JTextField(15);

                content.removeAll();
                content.repaint();
                enterButton = new JButton("Enter");
                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                    }
                });
                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File f = new File(filePath.getText());
                        if (!f.exists() || f.isDirectory()) {
                            JOptionPane.showMessageDialog(null, "The file does not exist! " +
                                            "Please try again", "Course Menu",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            boolean gotForum = false;
                            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                                String temp = "";
                                while ((temp = br.readLine()) != null) {
                                    forumName += temp;
                                }
                                gotForum = true;
                                if (!selectedCourse.isValidForum(forumName) || !isValidTitle(forumName)) {
                                    JOptionPane.showMessageDialog(null, "Edit the file" +
                                                    " and try again", "Course Menu",
                                            JOptionPane.ERROR_MESSAGE);
                                    gotForum = false;
                                    forumName = "";
                                }
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                            if (gotForum) {
                                Forum newForum = new Forum(forumName, selectedCourse.getCourseName());
                                selectedCourse.addForum(newForum);
                                JOptionPane.showMessageDialog(null, "Forum created!",
                                        "Course Menu",
                                        JOptionPane.INFORMATION_MESSAGE);
                                forumName = "";
                                CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                            }
                        }
                    }
                });

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(new JLabel("Please enter the file path which you would like to upload:"));
                content.add(filePath);
                content.add(enterButton);
                content.add(backButton);
                frame.setVisible(true);

            }

            if (e.getSource() == popRepsYes) {
                StringBuilder newResult = new StringBuilder();
                ArrayList<String> sortedForums = new ArrayList<>();
                for (int i = 0; i < forums.size(); i++) {
                    ArrayList<Reply> replies = forums.get(i).getReplies();
                    if (replies == null || replies.isEmpty()) {
                        continue;
                    }
                    int maxVotes = 0;
                    String student = "";
                    for (Reply r : replies) {
                        if (r.getVotes() > maxVotes) {
                            maxVotes = r.getVotes();
                            student = r.getPoster();
                        }
                    }
                    String element = String.valueOf(maxVotes) + ":" + forums.get(i).getForumName()
                            + "-" + student;
                    sortedForums.add(element);
                }
                //Insertion sort
                for (int i = 1; i < sortedForums.size(); i++) {
                    String key = new String(sortedForums.get(i));
                    int j = i - 1;
                    while (j >= 0 && Integer.parseInt(String.valueOf(sortedForums.get(j).charAt(0)))
                            < Integer.parseInt(String.valueOf(key.charAt(0)))) {
                        sortedForums.set(j + 1, sortedForums.get(j));
                        j -= 1;
                    }
                    sortedForums.set(j + 1, key);
                }
                for (int i = 0; i < sortedForums.size(); i++) {
                    String votes = sortedForums.get(i).substring(0, sortedForums.get(i).indexOf(":"));
                    String forumName = sortedForums.get(i).substring(sortedForums.get(i).indexOf(":")
                            + 1, sortedForums.get(i).indexOf("-"));
                    String student = sortedForums.get(i).substring(sortedForums.get(i).indexOf("-") + 1);
                    newResult.append("Forum " + (i + 1) + ") " + forumName + "\n" + "Student" +
                            " with maximum votes: " + student + "  No. of upvotes: " + votes + "\n");
                }

                popRepsDashboard = new JLabel("<html>" + newResult.toString().replaceAll("\n",
                        "<br>"));
                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
                    }
                });

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(popRepsDashboard);
                content.add(backButton);
                frame.setVisible(true);
            }

            if (e.getSource() == popRepsNo) {
                CourseMenu courseMenu = new CourseMenu(frame, courseNum, courses, user);
            }

            if (e.getSource() == refreshButton) {
                Menu.refreshButton.doClick();
            }
        }
    };

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidTitle(String input) {
        if (input == null || input.equals("")) {
            JOptionPane.showMessageDialog(null, "Cannot be empty", "Course Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (input.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Cannot contain spaces", "Course Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (input.contains("~")) {
            JOptionPane.showMessageDialog(null, "Cannot contain ~", "Course Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


}
