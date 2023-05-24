import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionProxy extends Thread {
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
    }

    @Override
    public void run() {
        try {
            String request = inputReader.readLine();

            if(request.isEmpty()) return;

            Message message = gson.fromJson(request, Message.class);

            CommandInterface command;
            if (message.getRecipient().equalsIgnoreCase("global")) {
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
