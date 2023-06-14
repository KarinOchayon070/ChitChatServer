package il.ac.hit.chatserver.network;

/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy.java) defines a il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy class that extends Thread and implements the il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface.
    It represents a client connection to a chat server.
    Here's a summary of what the code does:
    - It imports required classes and libraries for the implementation.
    - The class il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy is defined and extends Thread, which means it can be executed concurrently.
    - The class implements the il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface, which provides methods to iterate over client il.ac.hit.chatserver.connections.
    - Various member variables are declared, including an iterator, nickname, client socket, chat room, input reader, output writer,
      and static variables for the global chat room and client il.ac.hit.chatserver.connections map.
    - Getter and setter methods are provided for accessing and modifying the member variables.
    - The constructor initializes the il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy object by setting the client socket, creating input and output streams, and initializing the iterator.
    - The run() method is overridden to handle client communication. It reads messages from the client, parses them using Gson into il.ac.hit.chatserver.objects.Message objects, sets the nickname,
      adds the client to the global chat room, broadcasts a join message, reads further messages, and executes the corresponding commands based on the recipient.
    - When a client leaves the chat, a left chat message is broadcasted, the client's socket is removed from the il.ac.hit.chatserver.connections map, and the client socket is closed.
    - The next() and hasNext() methods are implemented from the il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface to iterate over client il.ac.hit.chatserver.connections.
 */

import com.google.gson.Gson;
import il.ac.hit.chatserver.interfaces.ClientConnectionIterator;
import il.ac.hit.chatserver.interfaces.CommandInterface;
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

    // Iterator to iterate over client il.ac.hit.chatserver.connections
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

    // Map to track client il.ac.hit.chatserver.connections
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
    public ConnectionProxy(Socket clientSocket) throws IOException {

        // Set the client socket for this connection proxy
        this.clientSocket = clientSocket;

        // Create a reader to read input from the client socket
        this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Create a writer to send output to the client socket
        this.outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);

        // Initialize the iterator to iterate over the client il.ac.hit.chatserver.connections
        iterator = ConnectionProxy.clientConnections.values().iterator();
    }

    // Override the run() method to handle client communication
    @Override
    public void run() {
        try {
            String request = inputReader.readLine();

            if (request.isEmpty()) return;

            // Parse the request as a il.ac.hit.chatserver.objects.Message object using Gson
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
            }

            // Broadcast a left chat message when the client leaves
            Message leftChatMessage = new Message(message.getNickName(), "*** " + message.getNickName() + " has left the chat ***", message.getRecipient());
            this.globalChatRoom.broadcastMessage(leftChatMessage, this);

            // If the client disconnected, remove its socket from the list and close the socket
            clientConnections.remove(clientSocket);
            globalChatRoom.removeClient(this);
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implement the next() method from the il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface
    @Override
    public ConnectionProxy next() {
        return iterator.next();
    }

    // Implement the hasNext() method from the il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
