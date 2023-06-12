import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerThread extends Thread {
    Socket socket;

    public synchronized static ArrayList<String> readFile(String filePath) {
        ArrayList<String> list = new ArrayList<String>();

        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader bfr = new BufferedReader(new FileReader(filePath))) {
            String line = bfr.readLine();

            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }

        } catch (IOException e) {
            return list;
        }
        return list;
    }

    public synchronized static void writeFile(ArrayList<String> content, String fileName) {
        try {
            File f = new File(fileName);
            PrintWriter pw = new PrintWriter(new FileOutputStream(f));
            for (String s : content) {
                pw.write(s);
                pw.println();
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Error in writing file - Server side");
        }
    }

    public synchronized static String loginWriteFile(ArrayList<Person> loginData) {
        File file = new File("login.txt");
        if (file.isDirectory()) {
            return "f";
        }
        if (file.exists() && !file.canWrite()) {
            return "f";
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("login.txt")))) {
            for (Person user : loginData) {
                writer.write(user.toString() + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            return "f";
        } catch (IOException e) {
            return "f";
        }

        return "t";
    }

    public synchronized static String coursesWriteFile(ArrayList<Course> courses) {
        try {
            File f = new File("Courses.txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(f));
            for (int i = 0; i < courses.size(); i++) {
                pw.println(courses.get(i).getCourseName());
            }
            pw.close();
            return "t";
        } catch (Exception e) {
            return "f";
        }
    }

    public synchronized static String forumsWriteFile(String courseName, ArrayList<Forum> forums) {
        String forumFile = courseName + "_forums.txt";

        try {
            File f = new File(forumFile);
            PrintWriter pw = new PrintWriter(new FileOutputStream(f));
            for (Forum forum : forums) {
                pw.write(forum.getForumName() + "~" + forum.getTime() + "\n");
            }
            pw.close();
            return "t";
        } catch (Exception e) {
            return "f";
        }
    }

    public synchronized static String repliesWriteFile(String courseName, String forumName, ArrayList<Reply> replies) {
        String forumFile = courseName + "_" + forumName + "_r.txt";

        try {
            File f = new File(forumFile);
            PrintWriter pw = new PrintWriter(new FileOutputStream(f));
            for (Reply reply : replies) {
                String output = "";
                output += reply.getPoster() + "~" + reply.getTime() + "~" + reply.getContent() + "~" +
                        reply.getVotes();
                pw.write(output + "\n");
                if (reply.getVotedStudents().size() > 0) {
                    output = String.join("~", reply.getVotedStudents());
                } else {
                    output = "No votes yet";
                }
                pw.write(output + "\n");
                if (reply.getComments().size() > 0) {
                    output = String.join("~", reply.getComments());
                } else {
                    output = "No comments yet";
                }
                pw.write(output + "\n");
            }
            pw.close();
            return "t";
        } catch (Exception e) {
            return "f";
        }
    }

    public synchronized static String scoresWriteFile(String courseName, String forumName, ArrayList<String> scores) {
        String forumScoreFile = courseName + "_" + forumName + "_scores.txt";
        File f = new File(forumScoreFile);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(f))) {
            for (String s : scores) {
                pw.write(s + "\n");
            }
            return "t";
        } catch (Exception e) {
            return "f";
        }
    }

    public static ArrayList<Reply> replyReadLines(ArrayList<String> inStr) {
        ArrayList<Reply> logs = new ArrayList<>();
        for (int i = 0; i < inStr.size(); i++) {
            String[] lineSplit = inStr.get(i).split("~");
            Reply reply = new Reply(lineSplit[2], lineSplit[0]);
            reply.setTime(lineSplit[1]);
            reply.setVotes(Integer.parseInt(lineSplit[3]));

            i++;

            if (!inStr.get(i).equals("No votes yet")) {
                ArrayList<String> votedStudents = new ArrayList<>(Arrays.asList(inStr.get(i).split("~")));
                if (votedStudents.size() > 0) {
                    reply.setVotedStudents(votedStudents);
                }
            }

            i++;

            if (!inStr.get(i).equals("No comments yet")) {
                ArrayList<String> comments = new ArrayList<>(Arrays.asList(inStr.get(i).split("~")));
                if (comments.size() > 0) {
                    reply.setComments(comments);
                }

            }
            logs.add(reply);
        }
        return logs;
    }

    public static ArrayList<Forum> forumReadLines(ArrayList<String> inStr, String courseName) {
        System.out.println("Here!!!");
        ArrayList<Forum> logs = new ArrayList<>();
        for (String s : inStr) {
            String[] lineSplit = s.split("~");
            Forum forum = new Forum(lineSplit[0], courseName);
            forum.setTime(lineSplit[1]);
            logs.add(forum);
        }
        return logs;
    }

    public static ArrayList<Person> logReadLines(ArrayList<String> inStr) {
        ArrayList<Person> logs = new ArrayList<>();
        for (String s : inStr) {
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
        return logs;
    }

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //Functional area of Server thread.
            //Currently default w/ simple reading/writing

            boolean running = true;
            while (running) {
                ArrayList<String> dataArr = (ArrayList<String>) ois.readObject();

                //System.out.printf("Command from client: %s, File: %s\n", dataArr.get(0), dataArr.get(1));

                //Debugging, prints all incoming string
                /*
                for(String s: dataArr) {
                    System.out.println(s);
                }

                 */

                switch (dataArr.get(0)) {
                    case "R": //No input, returns arraylist<String>
                        ArrayList<String> retStr = readFile(dataArr.get(1));
                        oos.writeObject(retStr);
                        break;

                    case "W": //Requires ArrayList<person> of all login people. Returns boolean
                        String fileName = dataArr.get(1);
                        dataArr.remove(0);
                        dataArr.remove(0);
                        writeFile(dataArr, fileName);
                        break;

                    case "Quit":
                        running = false;
                        break;
                    default:
                        System.out.println("Error: Invalid server read command");
                }
                //String response = message.replaceAll(" ", ",");
                //writer.write(response);
                oos.flush();
                // Ensure data is sent to the client.
                //System.out.printf("Sent to client:\n%s\n", response);
            }

            //End of Functional area

            oos.close();
            ois.close();
            System.out.println("Thread Closed");
        } catch (SocketException se) {
            System.out.println("Thread Closed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in server code");
        }
    }
    //Course and Scores dont need lines, already in sendable form.


}