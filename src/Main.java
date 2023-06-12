import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Project 4 - Discussion forum, Learning Management System
 * <p>
 * Main class to run the menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class Main extends JComponent {

    static JButton enterButton;
    static JButton quitButton;
    static JFrame frame;

    static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterButton) {
                Login login = new Login(frame);
            }

            if (e.getSource() == quitButton) {
                frame.dispose();

            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JComboBox enterQuitList;
                JTextField welcomeText;

                String[] enterQuitStrings = {"Enter Program", "Quit"};

                frame = new JFrame("Learning Management System Discussion Board");
                frame.setSize(600, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);

                Container content = frame.getContentPane();
                content.setLayout(new BorderLayout());

                ArrayList<Course> courses = readCourses("Courses.txt");

                AtomicReference<String> choice = null;

                JPanel welcomePanel = new JPanel();
                welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 500, 10));

                welcomePanel.add(new JLabel("Welcome to Learning Management System Discussion Board"));

                enterButton = new JButton("Enter Program");
                enterButton.addActionListener(actionListener);

                quitButton = new JButton("Quit");
                quitButton.addActionListener(actionListener);

                welcomePanel.add(enterButton);
                welcomePanel.add(quitButton);
                content.add(welcomePanel);

                frame.setVisible(true);
            }
        });
    }

    public static ArrayList<Course> readCourses(String courseFile) {
        ArrayList<Course> coursesList = new ArrayList<Course>();
        try {
            File f = new File(courseFile);
            if (!f.exists()) {
                return coursesList;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));

            String line;
            while ((line = br.readLine()) != null) {

                Course course = new Course(line);
                coursesList.add(course);
            }
            br.close();
            return coursesList;
        } catch (Exception e) {
            return coursesList;
        }
    }
}