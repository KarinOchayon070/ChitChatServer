/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (ConnectionProxy.java) defines a ConnectionProxy class that extends Thread and implements the ClientConnectionIterator interface.
    It represents a client connection to a chat server.
    Here's a summary of what the code does:
    - It imports required classes and libraries for the implementation.
    - The class ConnectionProxy is defined and extends Thread, which means it can be executed concurrently.
    - The class implements the ClientConnectionIterator interface, which provides methods to iterate over client connections.
    - Various member variables are declared, including an iterator, nickname, client socket, chat room, input reader, output writer,
      and static variables for the global chat room and client connections map.
    - Getter and setter methods are provided for accessing and modifying the member variables.
    - The constructor initializes the ConnectionProxy object by setting the client socket, creating input and output streams, and initializing the iterator.
    - The run() method is overridden to handle client communication. It reads messages from the client, parses them using Gson into Message objects, sets the nickname,
      adds the client to the global chat room, broadcasts a join message, reads further messages, and executes the corresponding commands based on the recipient.
    - When a client leaves the chat, a left chat message is broadcast, the client's socket is removed from the connections map, and the client socket is closed.
    - The next() and hasNext() methods are implemented from the ClientConnectionIterator interface to iterate over client connections.
 */

package il.ac.hit.chatserver.network;
import com.google.gson.Gson;
import il.ac.hit.chatserver.interfaces.ClientConnectionIterator;
import il.ac.hit.chatserver.interfaces.CommandInterface;
import il.ac.hit.chatserver.objects.ChatException;
import il.ac.hit.chatserver.objects.Message;
import il.ac.hit.chatserver.rooms.ChatRoom;
import il.ac.hit.chatserver.rooms.GlobalChatCommand;
import il.ac.hit.chatserver.rooms.OneOnOneChatCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConnectionProxy extends Thread implements ClientConnectionIterator {

    // Iterator to iterate over client connections
    private Iterator<ConnectionProxy> iterator;

    // Nickname of the client
    private String nickName;

    // Socket associated with the client connection
    private Socket clientSocket;

    // Chat room the client belongs to
    private ChatRoom chatRoom;

    // Reader to read input from the client
    private BufferedReader inputReader;

    // Writer to send output to the client
    private PrintWriter outputWriter;

    // Global chat room shared by all clients
    public static ChatRoom globalChatRoom = new ChatRoom("global");

    // Map to track client connections
    public static Map<Socket, ConnectionProxy> clientConnections = new HashMap<>();

    // Gson object for JSON serialization/deserialization
    private static Gson gson = new Gson();

    // Getter for the client socket
    public Socket getClientSocket() {
        return clientSocket;
    }

    // Getter for the input reader
    public BufferedReader getInputReader() {
        return inputReader;
    }

    // Getter for the output writer
    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    // Getter for the client's nickname
    public String getNickName() {
        return nickName;
    }

    // Setter for the client's nickname
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    // Setter for the chat room
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    // Constructor that initializes the connection proxy with the client socket and sets up input and output streams
    public ConnectionProxy(Socket clientSocket) throws IOException, ChatException {
        // Set the client socket for this connection proxy
        this.clientSocket = clientSocket;

        // Create a reader to read input from the client socket
        try {
            this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ChatException("Failed to create input reader.", e);
        }

        // Create a writer to send output to the client socket
        try {
            this.outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ChatException("Failed to create output writer.", e);
        }

        // Initialize the iterator to iterate over the client connections
        iterator = ConnectionProxy.clientConnections.values().iterator();
    }

    // Override the run() method to handle client communication
    @Override
    public void run() {
        try {
            String request = inputReader.readLine();

            if (request.isEmpty()) return;

            // Parse the request as a Message object using Gson
            Message message = gson.fromJson(request, Message.class);
            this.setNickName(message.getNickName());

            // Add the client to the global chat room and broadcast a joined message
            this.globalChatRoom.addClient(this);
            Message joinedMessage = new Message(message.getNickName(), "*** " + message.getNickName() + " has joined the chat ***", message.getRecipient());
            this.globalChatRoom.broadcastMessage(joinedMessage, this);

            try {
                // Continue reading messages from the client and execute corresponding commands
                while ((request = this.getInputReader().readLine()) != null) {
                    message = gson.fromJson(request, Message.class);

                    CommandInterface command;
                    if (message.getRecipient().equalsIgnoreCase("global")) {
                        command = new GlobalChatCommand(message, this);
                    } else {
                        command = new OneOnOneChatCommand(message, this);
                    }

                    command.execute();
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    throw new ChatException("Error occurred while reading/handling messages.", e);
                } catch (ChatException ex) {
                    throw new RuntimeException(ex);
                }
            }

            // Broadcast a left chat message when the client leaves
            Message leftMessage = new Message(message.getNickName(), "*** " + message.getNickName() + " has left the chat ***", message.getRecipient());
            this.globalChatRoom.broadcastMessage(leftMessage, this);

            // Remove the client's socket from the connections map
            clientConnections.remove(this.getClientSocket());

            // Close the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                throw new ChatException("Error occurred while reading client input.", e);
            } catch (ChatException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // Implement the next() method from the ClientConnectionIterator interface
    @Override
    public ConnectionProxy next() {
        return iterator.next();
    }

    // Implement the hasNext() method from the ClientConnectionIterator interface
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
