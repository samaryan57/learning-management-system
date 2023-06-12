import java.net.Socket;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * Pseudo client class.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class PseudoClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
