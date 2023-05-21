import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTCPIPServer {
    private static ChatRoom globalChatRoom = new ChatRoom("global");
    private static Map<String, ChatRoom> oneOnOneChatRooms = new HashMap<>();
    private static Map<Socket, ConnectionProxy> clientConnections = new HashMap<>();
    private static Gson gson = new Gson();

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

    private static class ConnectionProxy extends Thread {
        private Socket clientSocket;
        private BufferedReader inputReader;
        private PrintWriter outputWriter;
        private String username;
        private ChatRoom chatRoom;

        public ConnectionProxy(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                Message message = gson.fromJson(inputReader.readLine(), Message.class);

                Command command;
                if (message.getRoomName().equalsIgnoreCase("global")) {
                    command = new GlobalChatCommand(message, this);
                } else {
                    command = new OneOnOneChatCommand(message, this);
                }
                command.execute();

                // If the client disconnected, remove its socket from the list
                clientConnections.remove(clientSocket);
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private interface Command {
        void execute();
    }

    private static class GlobalChatCommand implements Command {
        private Message message;
        private ConnectionProxy connectionProxy;

        public GlobalChatCommand(Message message, ConnectionProxy connectionProxy) {
            this.message = message;
            this.connectionProxy = connectionProxy;
        }

        @Override
        public void execute() {
            try {
                connectionProxy.username = message.getNickName();
                connectionProxy.chatRoom = globalChatRoom;
                globalChatRoom.addClient(connectionProxy);

                String clientMessage;
                while ((clientMessage = connectionProxy.inputReader.readLine()) != null) {
                    System.out.println("Received message from client: " + clientMessage);
                    globalChatRoom.broadcastMessage(clientMessage);
                }

                globalChatRoom.removeClient(connectionProxy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class OneOnOneChatCommand implements Command {
        private Message message;
        private ConnectionProxy connectionProxy;
        private PrintStream outputWriter;

        public OneOnOneChatCommand(Message message, ConnectionProxy connectionProxy) {
            this.message = message;
            this.connectionProxy = connectionProxy;
        }

        @Override
        public void execute() {
            try {
                connectionProxy.username = message.getNickName();
                String targetUsername = message.getRoomName();

                ConnectionProxy targetProxy = findConnectionProxyByUsername(targetUsername);
                if (targetProxy != null) {
                    outputWriter.println("Connected to " + targetUsername + " for one-on-one chat.");
                    targetProxy.outputWriter.println("You are connected for one-on-one chat with " + connectionProxy.username);

                    String clientMessage;
                    while ((clientMessage = connectionProxy.inputReader.readLine()) != null) {
                        System.out.println("Received message from client: " + clientMessage);
                        targetProxy.outputWriter.println(connectionProxy.username + ": " + clientMessage);
                    }
                } else {
                    connectionProxy.outputWriter.println("User not found or offline.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ConnectionProxy findConnectionProxyByUsername(String username) {
            for (ConnectionProxy proxy : clientConnections.values()) {
                if (username.equalsIgnoreCase(proxy.username)) {
                    return proxy;
                }
            }
            return null;
        }
    }

    private static class ChatRoom {
        private String name;
        private List<ConnectionProxy> clients;

        public ChatRoom(String name) {
            this.name = name;
            this.clients = new ArrayList<>();
        }

        public void addClient(ConnectionProxy client) {
            clients.add(client);
            clientConnections.put(client.clientSocket, client);
        }

        public void removeClient(ConnectionProxy client) {
            clients.remove(client);
            clientConnections.remove(client.clientSocket);

            // If the chat room is empty, remove it
            if (clients.isEmpty() && !name.equals("global")) {
                oneOnOneChatRooms.remove(name);
            }
        }

        public void broadcastMessage(String message) {
            for (ConnectionProxy client : clients) {
                client.outputWriter.println(message);
            }
        }
    }
}
