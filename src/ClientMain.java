import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * Main class to run the menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */
public class ClientMain extends JComponent {

    static final int PORT = 4242;
    static final String SERVER = "localhost";
    public static ClientConnection s;
    static JButton enterButton;
    static JButton quitButton;
    static JFrame frame;
    static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterButton) {
                Login.s = s;
                Login login = new Login(frame);
            }

            if (e.getSource() == quitButton) {
                frame.dispose();
                s.close();
            }
        }
    };

    public static void main(String[] args) {
        try {
            s = new ClientConnection(SERVER, PORT);
            Course.s = s;
            Reply.s = s;
            Forum.s = s;
            Settings.s = s;
            Login.s = s;
            Menu.s = s;


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

            ArrayList<Course> courses = coursesReadLines("Courses.txt", s.oos, s.ois);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Course> coursesReadLines(String courseFile, ObjectOutputStream oos,
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
        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coursesList;
    }
}