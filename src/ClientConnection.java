import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Project 5 - Discussion forum, Learning Management System
 * <p>
 * Main class to run the menu.
 *
 * @author Noah Mundy, Paul Kelson, Raj Lunia, Sarah Nicholson, Aryan Samantaray L11
 * @version 04/06/2022
 */

public class ClientConnection {

    public Socket socket;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;

    public ClientConnection(String Server, int port) {
        try {
            socket = new Socket(Server, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);

        } catch (ConnectException e) {
            System.out.println("Connection Error: Ensure ServerMain is running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        ArrayList<String> endReq = new ArrayList<>();
        endReq.add("Quit");
        try {
            oos.writeObject(endReq);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}