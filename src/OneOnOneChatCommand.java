import com.google.gson.Gson;

import java.io.IOException;

public class OneOnOneChatCommand implements CommandInterface {
    private Message message;
    private ConnectionProxy connectionProxy;
    private Gson gson = new Gson();
    public OneOnOneChatCommand(Message message, ConnectionProxy connectionProxy) {
        this.message = message;
        this.connectionProxy = connectionProxy;
    }

    @Override
    public void execute() {
        try {
            connectionProxy.setNickName(message.getNickName());
            String recipient = message.getRecipient();

            ConnectionProxy targetProxy = findConnectionProxyByUsername(recipient);

            if(targetProxy == null){
                this.message.setMessage("User not found or offline.");
                this.sendMessageToSelf();
                return;
            }

            this.message.setMessage("Sent private message to "+ recipient);
            this.sendMessageToSelf();


            String clientMessage;
            while ((clientMessage = connectionProxy.getInputReader().readLine()) != null) {
                System.out.println("Received message from client: " + clientMessage);
                targetProxy.getOutputWriter().println(clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ConnectionProxy findConnectionProxyByUsername(String recipient) {
        for (ConnectionProxy proxy : ConnectionProxy.clientConnections.values()) {
            if (recipient.equalsIgnoreCase(proxy.getNickName())) {
                return proxy;
            }
        }
        return null;
    }

    private void sendMessageToSelf(){
        String json = gson.toJson(this.message);
        connectionProxy.getOutputWriter().println(json);
    }
}
