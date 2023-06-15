/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (SimpleTCPServer.java) represents a simple TCP server that listens on a specified port and handles incoming client connections.
    It creates a server socket, accepts client connections, and creates a separate thread (represented by the ConnectionProxy class) to handle each client's communication.
    The server runs indefinitely, continuously accepting new client connections and handling them concurrently.
 */

import il.ac.hit.chatserver.network.ConnectionProxy;
import il.ac.hit.chatserver.objects.ChatException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple TCP server that listens on a specified port and handles client connections
 */
public class SimpleTCPServer {
    /**
     * The main entry point of the server application
     *
     * @param args The command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Port number to listen on
        int port = 1300;

        try {
            // Create a server socket and bind it to the specified port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new ConnectionProxy instance for each client and start it as a separate thread
                ConnectionProxy proxy = new ConnectionProxy(clientSocket);
                proxy.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ChatException e) {
            throw new RuntimeException(e);
        }
    }
}

