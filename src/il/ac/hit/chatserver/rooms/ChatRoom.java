package il.ac.hit.chatserver.rooms;/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
 This file (il.ac.hit.chatserver.rooms.ChatRoom.java) represents a il.ac.hit.chatserver.rooms.ChatRoom class that facilitates communication between clients in a chat application.
 It allows clients to join or leave the chat room, broadcast text or message objects to all clients except the sender.
 Here are the key functionalities of the code:
  - The il.ac.hit.chatserver.rooms.ChatRoom class manages a chat room by storing the name of the room, maintaining a map of client sockets and their corresponding connection proxies,
    and using Gson for JSON serialization.
  - The addClient method adds a client to the chat room by associating the client's socket with its connection proxy in the connectionProxies map.
  - The removeClient method removes a client from the chat room by removing the client's socket and connection proxy from the connectionProxies map.
    If the chat room becomes empty (no clients) and it's not the "global" room, it is removed from the oneOnOneChatRooms map.
  - The broadcastMessage method sends a text message or message object to all clients in the chat room, except the sender.
    It iterates over the connectionProxies map and sends the message to each client's output writer.
 */

import com.google.gson.Gson;
import il.ac.hit.chatserver.network.ConnectionProxy;
import il.ac.hit.chatserver.objects.Message;

import java.net.Socket;
import java.util.*;

public class ChatRoom {

    // Represents the name of the chat room
    private String name;

    // Stores the connection proxies for each client socket in the chat room
    private Map<Socket, ConnectionProxy> connectionProxies;

    // Stores the mapping of chat room names to il.ac.hit.chatserver.rooms.ChatRoom objects for one-on-one chat rooms
    private static Map<String, ChatRoom> oneOnOneChatRooms = new HashMap<>();

    // Gson object for JSON serialization and deserialization
    private Gson gson =  new Gson();

    /**
     * Constructs a il.ac.hit.chatserver.rooms.ChatRoom object with the specified name.
     *
     * @param name the name of the chat room
     */
    public ChatRoom(String name) {
        this.name = name;
        this.connectionProxies = new HashMap<>();
    }

    // Adds a client to the chat room
    public void addClient(ConnectionProxy connectionProxy) {
        connectionProxies.put(connectionProxy.getClientSocket(), connectionProxy);
        ConnectionProxy.clientConnections.put(connectionProxy.getClientSocket(), connectionProxy);
    }

    // Removes a client from the chat room
    public void removeClient(ConnectionProxy connectionProxy) {
        connectionProxies.remove(connectionProxy.getClientSocket());
        ConnectionProxy.clientConnections.remove(connectionProxy.getClientSocket());

        // If the chat room is empty, remove it
        if (connectionProxies.isEmpty() && !name.equals("global")) {
            oneOnOneChatRooms.remove(name);
        }
    }

    // Broadcasts a text message to all clients in the chat room, except the sender
    public void broadcastMessage(String message,  ConnectionProxy sender) {
        Iterator<ConnectionProxy> iterator = connectionProxies.values().iterator();
        while (iterator.hasNext()) {
            ConnectionProxy connectionProxy = iterator.next();
            if (connectionProxy.getClientSocket() != sender.getClientSocket()) {
                connectionProxy.getOutputWriter().println(message);
            }
        }
    }

    // Broadcasts a message object to all clients in the chat room, except the sender
    public void broadcastMessage(Message message, ConnectionProxy sender) {
        String messageJson = gson.toJson(message);
        this.broadcastMessage(messageJson, sender);
    }
}