/* Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

import com.google.gson.Gson;
import java.net.Socket;
import java.util.*;

public class ChatRoom {
    private String name;
    private  Map<Socket, ConnectionProxy> connectionProxies;
    private static Map<String, ChatRoom> oneOnOneChatRooms = new HashMap<>();
    private Gson gson =  new Gson();


    public ChatRoom(String name) {
        this.name = name;
        this.connectionProxies = new HashMap<>();
    }

    public void addClient(ConnectionProxy connectionProxy) {
        connectionProxies.put(connectionProxy.getClientSocket(), connectionProxy);
        ConnectionProxy.clientConnections.put(connectionProxy.getClientSocket(), connectionProxy);
    }

    public void removeClient(ConnectionProxy connectionProxy) {
        connectionProxies.remove(connectionProxy.getClientSocket());
        ConnectionProxy.clientConnections.remove(connectionProxy.getClientSocket());

        // If the chat room is empty, remove it
        if (connectionProxies.isEmpty() && !name.equals("global")) {
            oneOnOneChatRooms.remove(name);
        }
    }

    public void broadcastMessage(String message,  ConnectionProxy sender) {
        Iterator<ConnectionProxy> iterator = connectionProxies.values().iterator();
        while (iterator.hasNext()) {
            ConnectionProxy connectionProxy = iterator.next();
            if (connectionProxy.getClientSocket() != sender.getClientSocket()) {
                connectionProxy.getOutputWriter().println(message);
            }
        }
    }

    public void broadcastMessage(Message message,  ConnectionProxy sender) {
        String messageJson = gson.toJson(message);
        this.broadcastMessage(messageJson, sender);
    }
}