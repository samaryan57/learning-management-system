import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * FrameWorkServer class.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/23/2022
 */
public class FrameWorkServer extends Thread {
    Socket socket;

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            System.out.println("New Server thread started");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //Functional area of Server thread
            //Currently default w/ simple reading/writing

            String message = reader.readLine();
            System.out.printf("Received from client:\n%s\n", message);
            String response = message.replaceAll(" ", ",");
            writer.write(response);
            writer.println();
            writer.flush(); // Ensure data is sent to the client.
            System.out.printf("Sent to client:\n%s\n", response);

            //End of functional area

            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in server code");
        }
    }
}