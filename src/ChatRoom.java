import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom {
    private String name;
    private List<ConnectionProxy> connectionProxies;
    private static Map<String, ChatRoom> oneOnOneChatRooms = new HashMap<>();

    public ChatRoom(String name) {
        this.name = name;
        this.connectionProxies = new ArrayList<>();
    }

    public void addClient(ConnectionProxy connectionProxy) {
        connectionProxies.add(connectionProxy);
        ConnectionProxy.clientConnections.put(connectionProxy.getClientSocket(), connectionProxy);
    }

    public void removeClient(ConnectionProxy connectionProxy) {
        connectionProxies.remove(connectionProxy);
        ConnectionProxy.clientConnections.remove(connectionProxy.getClientSocket());

        // If the chat room is empty, remove it
        if (connectionProxies.isEmpty() && !name.equals("global")) {
            oneOnOneChatRooms.remove(name);
        }
    }

    public void broadcastMessage(String message) {
        for (ConnectionProxy connectionProxy : connectionProxies) {
            connectionProxy.getOutputWriter().println(message);
        }
    }
}
