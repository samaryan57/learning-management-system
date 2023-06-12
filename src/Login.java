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
 * Login class, for login functionality.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class Login {

    public static ClientConnection s;
    private static ArrayList<Person> loginData;
    JFrame frame;
    JButton loginButton;
    JButton createAccountButton;
    JTextField loginUsernameText;
    JTextField loginPasswordText;
    JTextField createAccUsernameText;
    JTextField createAccPasswordText;
    JComboBox personList;
    String[] personStrings = {"Student", "Teacher"};
    Person user = null;
    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                user = getUser(loginUsernameText.getText(), loginPasswordText.getText());
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    Container content = frame.getContentPane();
                    content.removeAll();
                    content.repaint();

                    Menu menu = new Menu(frame, user);
                }
            }
            if (e.getSource() == createAccountButton) {

                String username = createAccUsernameText.getText();
                String password = createAccPasswordText.getText();

                if (!doesUsernameExist(username) && isValidUsernameOrPassword(username) &&
                        isValidUsernameOrPassword(password)) {

                    if (personList.getSelectedIndex() == 0) {
                        Container content = frame.getContentPane();
                        content.removeAll();
                        content.repaint();
                        user = new Student(username, password);
                        Menu.s = s;
                        Menu menu = new Menu(frame, user);
                    } else {
                        Container content = frame.getContentPane();
                        content.removeAll();
                        content.repaint();
                        user = new Teacher(username, password);

                        Menu menu = new Menu(frame, user);
                        System.out.println("2");
                    }
                    if (!addUser(user)) {
                        JOptionPane.showMessageDialog(null, "There was a error creating " +
                                        "account please try again",
                                "Error!", JOptionPane.ERROR_MESSAGE);
                        user = null;
                    }
                }
            }

        }
    };

    public Login(JFrame frame, Person currentUser) {
        this.frame = frame;
        loginData = logReadLines("login.txt", s.oos, s.ois);
        //loginData = readFile("login.txt");
        for (Person login : loginData) {
            if (login.getUsername().equals(currentUser.getUsername()) &&
                    (login.getPassword().equals(currentUser.getPassword()))) {
                user = login;
            }
        }
    }

    public Login(JFrame frame) {
        //Will create an arraylist of all the usernames and passwords for teachers and students
        loginData = logReadLines("login.txt", s.oos, s.ois);
        //loginData = readFile("login.txt");
        this.frame = frame;

        Container content = frame.getContentPane();
        content.setLayout(new GridLayout(3, 2));

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));

        JPanel createAccPanel = new JPanel();
        createAccPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));

        JPanel loginUsernamePanel = new JPanel();
        JPanel loginPasswordPanel = new JPanel();

        JPanel createAccUsernamePanel = new JPanel();
        JPanel createAccPasswordPanel = new JPanel();

        JLabel loginLabel = new JLabel("Login");
        JLabel createAccLabel = new JLabel("Create Account");

        loginUsernameText = new JTextField(10);
        loginUsernameText.setText("");
        loginUsernamePanel.add(new JLabel("Username: "));
        loginUsernamePanel.add(loginUsernameText);

        loginPasswordText = new JTextField(10);
        loginPasswordText.setText("");
        loginPasswordPanel.add(new JLabel("Password: "));
        loginPasswordPanel.add(loginPasswordText);


        createAccUsernameText = new JTextField(10);
        createAccUsernameText.setText("");
        createAccUsernamePanel.add(new JLabel("Username: "));
        createAccUsernamePanel.add(createAccUsernameText);

        createAccPasswordText = new JTextField(10);
        createAccPasswordText.setText("");
        createAccPasswordPanel.add(new JLabel("Password: "));
        createAccPasswordPanel.add(createAccPasswordText);


        personList = new JComboBox(personStrings);
        personList.setSelectedIndex(0);


        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);

        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(actionListener);


        loginPanel.add(loginLabel);
        loginPanel.add(loginUsernamePanel);
        loginPanel.add(loginPasswordPanel);
        loginPanel.add(loginButton);

        createAccPanel.add(createAccLabel);
        createAccPanel.add(createAccUsernamePanel);
        createAccPanel.add(createAccPasswordPanel);
        createAccPanel.add(personList);
        createAccPanel.add(createAccountButton);

        content.removeAll();
        content.repaint();
        content.add(new JPanel());
        content.add(new JPanel());
        content.add(loginPanel);
        content.add(createAccPanel);

        frame.setVisible(true);
    }

    public static ArrayList<Person> getLoginData() {
        loginData = logReadLines("login.txt", s.oos, s.ois);
        return loginData;
    }

    /**
     * reads file.
     *
     * @param fileName file path of the file
     * @return an array list of persons in the file
     */

    public static ArrayList<Person> logReadLines(String fileName, ObjectOutputStream oos, ObjectInputStream ois) {
        ArrayList<String> requestStr = new ArrayList<>();
        requestStr.add("R");
        requestStr.add(fileName);
        ArrayList<Person> logs = new ArrayList<>();
        try {
            oos.writeObject(requestStr);
            oos.flush();
            ArrayList<String> dataStr = (ArrayList<String>) ois.readObject();


            for (String s : dataStr) {
                String[] lineSplit = s.split(":");
                if (lineSplit.length == 3) {
                    if (lineSplit[2].equals("S")) {
                        Student student = new Student(lineSplit[0], lineSplit[1]);
                        logs.add(student);
                    } else if (lineSplit[2].equals("T")) {
                        Teacher teacher = new Teacher(lineSplit[0], lineSplit[1]);
                        logs.add(teacher);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Deletes user.
     *
     * @param user person user
     */
    public void deleteUser(Person user) {
        loginData.remove(user);
        logWriteLines(s.oos, s.ois);
    }

    /**
     * Gets user.
     *
     * @param username String username
     * @param password String password
     * @return person
     */
    public Person getUser(String username, String password) {

        for (Person person : loginData) {
            if (person.getUsername().equals(username) && person.getPassword().equals(password)) {
                return person;
            }
        }

        return null;
    }

    /**
     * Adds user.
     *
     * @param user person user
     * @return boolean added or not
     */
    public boolean addUser(Person user) {
        loginData.add(user);
        return logWriteLines(s.oos, s.ois);
    }

    /**
     * Checks for username and password validity.
     *
     * @param usernameOrPassword String username or password
     * @return person
     */
    public boolean isValidUsernameOrPassword(String usernameOrPassword) {
        if (usernameOrPassword.equals("")) {
            JOptionPane.showMessageDialog(null, "Field cannot be empty!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usernameOrPassword.contains(":")) {
            JOptionPane.showMessageDialog(null, "Field cannot contain a colon!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usernameOrPassword.contains("~")) {
            JOptionPane.showMessageDialog(null, "Field cannot contain a ~!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Checks for user existence.
     *
     * @param username String username
     * @return if username exists (boolean)
     */
    public boolean doesUsernameExist(String username) {
        for (Person person : loginData) {
            if (person.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(null, "Username already exists!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }

        return false;
    }

    public boolean logWriteLines(ObjectOutputStream oos, ObjectInputStream ois) {
        ArrayList<String> dataStr = new ArrayList<>();
        dataStr.add("W");
        dataStr.add("login.txt");
        for (Person user : loginData) {
            dataStr.add(user.toString());
        }

        try {
            oos.writeObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}