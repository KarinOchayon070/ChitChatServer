import com.google.gson.Gson;

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
            connectionProxy.setChatRoom(connectionProxy.globalChatRoom);

            System.out.println("Received global message from client: " + message.getNickName());
            connectionProxy.globalChatRoom.broadcastMessage(message, connectionProxy);
    }
}
