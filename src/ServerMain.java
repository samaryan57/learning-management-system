import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {
        //ArrayList<FrameWorkServer> serverThreads = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected!");

                    ServerThread temp = new ServerThread();
                    temp.setSocket(socket);
                    temp.start();

                    // OPTIONAL CODE FOR RETAINING AN ARRAY LIST OF ALL THREADS
                    // UNSURE IF NECESSARY, AND CURRENTLY THE REPLACEMENT
                    // CODE IS NON-FUNCTIONAL

                /*
                boolean replaced = false;
                for(int i = 0; i < serverThreads.size(); i ++) {
                    if(!serverThreads.get(i).isAlive()) {
                        serverThreads.set(i,temp);
                        replaced = true;
                        System.out.println("Thread replaced");
                        break;
                    }
                }
                if(!replaced) {
                    serverThreads.add(temp);
                }
                serverThreads.add(temp);
                System.out.printf("Threads running: %d\n", serverThreads.size());
                 */

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}