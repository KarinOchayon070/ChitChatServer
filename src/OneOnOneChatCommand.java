import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;

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
            String recipient = this.message.getRecipient();

            ConnectionProxy targetProxy = findConnectionProxyByUsername(recipient);


            if(targetProxy == null){
                this.message.setNickName("*** SERVER ERROR ***");
                this.message.setMessage("*** User not found or offline ***");
                this.sendMessageToSelf();
                return;
            }

            if(targetProxy.getClientSocket() == connectionProxy.getClientSocket()){
                this.message.setNickName("*** SERVER ERROR ***");
                this.message.setMessage("*** Don't talk to yourself darling ***");
                this.sendMessageToSelf();
                return;
        }


            System.out.println("Received oneOnOne message from client: " + this.message.getNickName());
            this.sendMessageToRecipient(targetProxy);
    }

    private ConnectionProxy findConnectionProxyByUsername(String recipient) {
        Iterator<ConnectionProxy> iterator = ConnectionProxy.clientConnections.values().iterator();
        while (iterator.hasNext()) {
            ConnectionProxy proxy = iterator.next();
            if (recipient.equalsIgnoreCase(proxy.getNickName())) {
                return proxy;
            }
        }
        return null;
    }

    private void sendMessageToRecipient(ConnectionProxy targetProxy){
        String json = gson.toJson(this.message);
        targetProxy.getOutputWriter().println(json);
    }
    private void sendMessageToSelf(){
        String json = gson.toJson(this.message);
        connectionProxy.getOutputWriter().println(json);
    }
}