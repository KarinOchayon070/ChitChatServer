import java.io.IOException;

public class GlobalChatCommand implements CommandInterface {
    private Message message;
    private ConnectionProxy connectionProxy;

    public GlobalChatCommand(Message message, ConnectionProxy connectionProxy) {
        this.message = message;
        this.connectionProxy = connectionProxy;
    }

    @Override
    public void execute() {
        try {
            connectionProxy.setNickName(message.getNickName());
            connectionProxy.setChatRoom(connectionProxy.globalChatRoom);
            connectionProxy.globalChatRoom.addClient(connectionProxy);

            String clientMessage;
            while ((clientMessage = connectionProxy.getInputReader().readLine()) != null) {
                System.out.println("Received message from client: " + clientMessage);
                connectionProxy.globalChatRoom.broadcastMessage(clientMessage);
            }

            connectionProxy.globalChatRoom.removeClient(connectionProxy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
