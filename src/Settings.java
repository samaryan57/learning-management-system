import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Settings class to run settings related to accounts.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class Settings {

    public static ClientConnection s;
    JFrame frame;
    Login login;
    Person user;
    JButton usernameButton;
    JButton passwordButton;
    JButton deleteButton;
    JTextField usernameText;
    JTextField passwordText;
    JButton backButton;
    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == usernameButton) {

                String username = usernameText.getText();
                if (login.doesUsernameExist(username) || !login.isValidUsernameOrPassword(username)) {

                } else {
                    JOptionPane.showMessageDialog(null, "Username Changed!");
                    user.setUsername(username);
                    login.logWriteLines(s.oos, s.ois);
                }
            }

            if (e.getSource() == passwordButton) {

                String password = passwordText.getText();
                if (!login.isValidUsernameOrPassword(password)) {

                }
                JOptionPane.showMessageDialog(null, "Password Changed!");
                user.setPassword(password);
                login.logWriteLines(s.oos, s.ois);
            }

            if (e.getSource() == deleteButton) {

                login.deleteUser(user);
                JOptionPane.showMessageDialog(null, "Account Deleted!");
                Login login = new Login(frame);
            }

            if (e.getSource() == backButton) {
                Menu menu = new Menu(frame, user);
            }

        }
    };

    public Settings(JFrame frame, Person user) {
        this.login = new Login(frame, user);
        this.user = login.user;
        this.frame = frame;

        Container content = frame.getContentPane();
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));


        usernameText = new JTextField(10);
        usernameText.setText("");

        passwordText = new JTextField(10);
        passwordText.setText("");

        usernameButton = new JButton("Change Username");
        usernameButton.addActionListener(actionListener);

        passwordButton = new JButton("Change Password");
        passwordButton.addActionListener(actionListener);

        deleteButton = new JButton("Delete Account");
        deleteButton.addActionListener(actionListener);

        backButton = new JButton("Back");
        backButton.addActionListener(actionListener);

        content.removeAll();
        content.repaint();
        content.add(new JLabel("Change Username"));
        content.add(usernameText);
        content.add(usernameButton);

        content.add(new JLabel("Change Password"));
        content.add(passwordText);
        content.add(passwordButton);

        content.add(new JLabel("Delete Account"));
        content.add(deleteButton);

        content.add(new JLabel("Back"));
        content.add(backButton);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}