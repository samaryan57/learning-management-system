import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * FrameWorkClient class.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/23/2022
 */
public class FrameWorkClient {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 4242);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //Functional area of client
            //Currently default

            System.out.println("Send to server: ");
            String response = scan.nextLine();
            writer.write(response);
            writer.println();
            writer.flush();
            System.out.printf("Sent to server:\n%s\n", response);
            String s1 = reader.readLine();
            System.out.printf("Received from server:\n%s\n", s1);

            //End of functional area

            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in client code");
        }
    }
}
