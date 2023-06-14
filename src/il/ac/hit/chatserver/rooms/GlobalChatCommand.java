package il.ac.hit.chatserver.rooms;/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (il.ac.hit.chatserver.rooms.GlobalChatCommand.java) defines a class called il.ac.hit.chatserver.rooms.GlobalChatCommand that represents a command to execute a global chat message.
    When the execute() method of this command is called, it sets the chat room of the associated il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy object to the global chat room,
    prints a log message indicating the reception of a global message from a client, and broadcasts the message to all clients in the global chat room.
 */

import il.ac.hit.chatserver.interfaces.CommandInterface;
import il.ac.hit.chatserver.network.ConnectionProxy;
import il.ac.hit.chatserver.objects.Message;

public class GlobalChatCommand implements CommandInterface {

    // The message to be sent
    private Message message;

    // The connection proxy representing the client
    private ConnectionProxy connectionProxy;

    /**
     * Constructs a il.ac.hit.chatserver.rooms.GlobalChatCommand object with the provided message and connection proxy
     *
     * @param message         The message to be sent
     * @param connectionProxy The connection proxy associated with the client
     */
    public GlobalChatCommand(Message message, ConnectionProxy connectionProxy) {
        this.message = message;
        this.connectionProxy = connectionProxy;
    }

    @Override
    public void execute() {
        // Set the chat room of the connection proxy to the global chat room
        connectionProxy.setChatRoom(connectionProxy.globalChatRoom);

        // Print a log message indicating the reception of a global message from the client
        System.out.println("Received global message from client: " + message.getNickName());

        // Broadcast the message to all clients in the global chat room
        connectionProxy.globalChatRoom.broadcastMessage(message, connectionProxy);
    }
}
