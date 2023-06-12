import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Menu class, that implements the menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Menu {

    public static ClientConnection s;
    static JButton refreshButton;
    //ArrayList<Course> courses = readCourses("Courses.txt");
    ArrayList<Course> courses = courseReadLines("Courses.txt", s.oos, s.ois);
    JFrame frame;
    Person user;
    JButton enterButton;
    JButton backButton;
    JButton viewCourseButton;
    JButton createCourseButton;
    JButton settingsButton;
    JButton logoutButton;
    JPanel userInfo;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewCourseButton) {
                //View Course
                if ((courses == null) || courses.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "NO COURSES",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    Container content = frame.getContentPane();
                    content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));
                    content.removeAll();
                    content.repaint();

                    userInfo = new JPanel();
                    JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                    userInfo.add(userLabel);

                    content.add(userInfo);
                    for (int i = 0; i < courses.size(); i++) {
                        content.add(new JLabel((i + 1) + ") " + courses.get(i).getCourseName()));
                    }

                    JPanel courseNumPanel = new JPanel();
                    JLabel viewForums = new JLabel("Enter the course number to view forums:");
                    JTextField input = new JTextField(3);

                    courseNumPanel.add(viewForums);
                    courseNumPanel.add(input);

                    content.add(courseNumPanel);

                    backButton = new JButton("Back");
                    backButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Menu menu = new Menu(frame, user);
                        }
                    });

                    enterButton = new JButton("Enter");
                    enterButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String inputString = input.getText();
                            if (isInteger(inputString)) {
                                int inputNum = Integer.parseInt(inputString);
                                if ((inputNum > 0) && (inputNum <= courses.size())) {
                                    inputNum = inputNum - 1;
                                    CourseMenu courseMenu = new CourseMenu(frame, inputNum, courses, user);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid Input!",
                                            "Error!", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Input!",
                                        "Error!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    content.add(enterButton);
                    content.add(backButton);
                    frame.setVisible(true);
                }

            }

            if (e.getSource() == createCourseButton) {
                Container content = frame.getContentPane();
                content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));
                content.removeAll();
                content.repaint();

                //Create Course
                JPanel panel = new JPanel();
                JLabel courseName = new JLabel("Please enter the course name:");
                JTextField input = new JTextField(15);


                panel.add(courseName);
                panel.add(input);

                content.removeAll();
                content.repaint();

                userInfo = new JPanel();
                JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
                userInfo.add(userLabel);

                content.add(userInfo);
                content.add(panel);

                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Menu menu = new Menu(frame, user);
                    }
                });

                enterButton = new JButton("Enter");
                enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String courseTitle = input.getText();
                        if (courseTitle.contains("~")) {
                            JOptionPane.showMessageDialog(null,
                                    "Course title cannot contian ~",
                                    "Error!", JOptionPane.ERROR_MESSAGE);
                        } else if (courseTitle.equals("")) {
                            JOptionPane.showMessageDialog(null,
                                    "Course title cannot be empty",
                                    "Error!", JOptionPane.ERROR_MESSAGE);
                        } else if (!isValidCourse(courseTitle, courses)) {
                            //DO not remove this
                        } else {
                            JOptionPane.showMessageDialog(null, "Course added");

                            Course newCourse = new Course(courseTitle);
                            courses.add(newCourse);
                            courseWriteLines("Courses.txt", courses, s.oos, s.ois);
                            Menu menu = new Menu(frame, user);
                        }
                    }
                });

                content.add(enterButton);
                content.add(backButton);


                frame.setVisible(true);
            }

            if (e.getSource() == settingsButton) {
                //Settings
                Settings settings = new Settings(frame, user);
            }

            if (e.getSource() == logoutButton) {
                Login login = new Login(frame);
            }

            if (e.getSource() == refreshButton) {
                ServerThread.coursesWriteFile(getCourses());
                for (Course course : getCourses()) {
                    ServerThread.forumsWriteFile(course.getCourseName(), course.getForums());
                    for (Forum forum : course.getForums()) {
                        ServerThread.repliesWriteFile(course.getCourseName(), forum.getForumName(),
                                forum.getReplies());
                        ServerThread.scoresWriteFile(course.getCourseName(), forum.getForumName(),
                                forum.getScores());
                    }
                }
                ServerThread.loginWriteFile(Login.getLoginData());
                JOptionPane.showMessageDialog(null, "Refreshed!",
                        "LMS Discussion Board", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    public Menu(JFrame frame, Person user) {
        this.frame = frame;
        this.user = user;

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));

        viewCourseButton = new JButton("View Course");
        viewCourseButton.addActionListener(actionListener);

        createCourseButton = new JButton("Create Course");
        createCourseButton.addActionListener(actionListener);

        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(actionListener);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionListener);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(actionListener);

        userInfo = new JPanel();
        JLabel userLabel = new JLabel("Logged in as: " + user.getUsername());
        userInfo.add(userLabel);

        content.removeAll();
        content.repaint();
        content.add(userInfo);
        content.add(viewCourseButton);
        if (user instanceof Teacher) {
            content.add(createCourseButton);
        }
        content.add(settingsButton);
        content.add(logoutButton);
        content.add(refreshButton);
        frame.setVisible(true);
    }

    public static ArrayList<Course> courseReadLines(String courseFile, ObjectOutputStream oos,
                                                    ObjectInputStream ois) {
        ArrayList<String> requestStr = new ArrayList<>();
        requestStr.add("R");
        requestStr.add(courseFile);
        ArrayList<Course> coursesList = new ArrayList<>();
        try {
            oos.writeObject(requestStr);
            oos.flush();
            ArrayList<String> dataStr = (ArrayList<String>) ois.readObject();

            try {
                for (String s : dataStr) {
                    Course course = new Course(s);
                    coursesList.add(course);
                }
                return coursesList;
            } catch (Exception e) {
                return coursesList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coursesList;
    }

    public static void courseWriteLines(String courseFile, ArrayList<Course> courses,
                                        ObjectOutputStream oos, ObjectInputStream ois) {
        ArrayList<String> dataStr = new ArrayList<>();
        try {
            dataStr.add("W");
            dataStr.add(courseFile);
            for (Course c : courses) {
                dataStr.add(c.getCourseName());
            }
        } catch (Exception e) {
            return;
        }
        try {
            oos.writeObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidTitle(String input) {
        if (input.isEmpty() || input.isBlank()) {
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

    public boolean isValidMessage(String input) {
        if (input.isEmpty() || input.isBlank()) {
            return false;
        } else if (input.contains("~")) {
            JOptionPane.showMessageDialog(null, "Cannot contain ~", "Course Menu",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isValidCourse(String title, ArrayList<Course> courses) {
        for (Course corse : courses) {
            if (corse.getCourseName().equals(title)) {
                JOptionPane.showMessageDialog(null, "Course already exists",
                        "Course Menu",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public ArrayList<Course> getCourses() {
        courses = courseReadLines("Courses.txt", s.oos, s.ois);
        return courses;
    }
}
