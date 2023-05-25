import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

public class ConnectionProxy extends Thread implements ClientConnectionIterator {

    private Iterator<ConnectionProxy> iterator;
    private String nickName;
    private Socket clientSocket;
    private ChatRoom chatRoom;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    public static ChatRoom globalChatRoom = new ChatRoom("global");
    public static Map<Socket, ConnectionProxy> clientConnections = new HashMap<>();
    private static Gson gson = new Gson();
    public Socket getClientSocket() {
        return clientSocket;
    }
    public BufferedReader getInputReader() {
        return inputReader;
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ConnectionProxy(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        iterator = ConnectionProxy.clientConnections.values().iterator();
    }


    @Override
    public void run() {
        try {
            String request = inputReader.readLine();

            if(request.isEmpty()) return;

            Message message = gson.fromJson(request, Message.class);
            this.setNickName(message.getNickName());

            this.globalChatRoom.addClient(this);
            Message joinedMessage = new Message(message.getNickName(), "*** " + message.getNickName() + " has joined the chat ***", message.getRecipient() );
            this.globalChatRoom.broadcastMessage(joinedMessage, this);


            try {
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


            Message leftChatMessage = new Message(message.getNickName(), "*** " + message.getNickName() + " has left the chat ***", message.getRecipient() );
            this.globalChatRoom.broadcastMessage(leftChatMessage, this);

            // If the client disconnected, remove its socket from the list
            clientConnections.remove(clientSocket);
            globalChatRoom.removeClient(this);
            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConnectionProxy next() {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
