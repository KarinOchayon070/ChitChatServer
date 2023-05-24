import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTCPIPServer {
    public static void main(String[] args) {
        int port = 1300; // Port number to listen on

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new ConnectionProxy instance for each client
                ConnectionProxy proxy = new ConnectionProxy(clientSocket);
                proxy.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
