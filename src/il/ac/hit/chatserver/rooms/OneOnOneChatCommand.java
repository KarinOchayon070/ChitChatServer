/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (OneOnOneChatCommand.java) represents the OneOnOneChatCommand class, which implements the CommandInterface.
    It handles the execution of a command to send a one-on-one chat message between clients. Here's a summary of what the code does:
    - The class has a constructor that initializes the command with the given message and connection proxy.
    - The execute() method is implemented to carry out the execution of the command.
    - It retrieves the recipient from the message.
    - It searches for the connection proxy associated with the recipient using the findConnectionProxyByUsername() method.
    - If the recipient is not found or offline, it sets an error message and sends it back to the sender.
    - It checks if the sender is trying to send a message to themselves and handles the case by setting an error message and sending it to the sender.
    - If everything is valid, it sends the one-on-one message to the recipient using the sendMessageToRecipient() method.
    - The findConnectionProxyByUsername() method iterates over the client connections and returns the connection proxy associated with the given recipient username.
    - The sendMessageToRecipient() method converts the message to JSON using Gson and sends it to the recipient's output writer.
    - The sendMessageToSelf() method converts the message to JSON using Gson and sends it back to the sender using their own output writer.
 */

package il.ac.hit.chatserver.rooms;
import com.google.gson.Gson;
import il.ac.hit.chatserver.interfaces.CommandInterface;
import il.ac.hit.chatserver.network.ConnectionProxy;
import il.ac.hit.chatserver.objects.Message;
import java.util.Iterator;

public class OneOnOneChatCommand implements CommandInterface {

    // The message to be sent
    private Message message;

    // The connection proxy representing the client connection
    private ConnectionProxy connectionProxy;

    // Gson object for JSON serialization/deserialization
    private Gson gson = new Gson();

    /**
     * Constructor for the OneOnOneChatCommand class
     * Initializes the command with the given message and connection proxy
     *
     * @param message          The message to be sent
     * @param connectionProxy  The connection proxy associated with the sender
     */
    public OneOnOneChatCommand(Message message, ConnectionProxy connectionProxy) {
        this.message = message;
        this.connectionProxy = connectionProxy;
    }


    /**
     * Executes the one-on-one chat command by retrieving the recipient from the message
     * searching for the connection proxy associated with the recipient, and sending the message
     * to the recipient or handling error cases
     */
    @Override
    public void execute() {
        String recipient = this.message.getRecipient();

        // Find the connection proxy associated with the recipient
        ConnectionProxy targetProxy = findConnectionProxyByUsername(recipient);

        // If the recipient is not found or offline, send an error message to the sender
        if (targetProxy == null) {
            this.message.setNickName("*** SERVER ERROR ***");
            this.message.setMessage("*** User not found or offline ***");
            this.sendMessageToSelf();
            return;
        }

        // Check if the sender is trying to talk to themselves
        if (targetProxy.getClientSocket() == connectionProxy.getClientSocket()) {
            this.message.setNickName("*** SERVER ERROR ***");
            this.message.setMessage("*** Don't talk to yourself darling ***");
            this.sendMessageToSelf();
            return;
        }

        System.out.println("Received oneOnOne message from client: " + this.message.getNickName());
        this.sendMessageToRecipient(targetProxy);
    }

    /**
     * Finds the ConnectionProxy associated with the given recipient username
     *
     * @param recipient The recipient username
     * @return The ConnectionProxy associated with the recipient, or null if not found
     */
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

    /**
     * Sends the message to the recipient
     *
     * @param targetProxy The ConnectionProxy associated with the recipient
     */
    private void sendMessageToRecipient(ConnectionProxy targetProxy) {
        String json = gson.toJson(this.message);
        targetProxy.getOutputWriter().println(json);
    }

    /**
     * Sends the message to the sender itself
     */
    private void sendMessageToSelf() {
        String json = gson.toJson(this.message);
        connectionProxy.getOutputWriter().println(json);
    }
}
