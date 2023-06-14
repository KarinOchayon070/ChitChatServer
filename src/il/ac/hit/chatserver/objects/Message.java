/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (Message.java) defines a Message class that represents a chat message.
    It has three instance variables: nickName, message, and recipient, which store the nickname of the sender,
    the content of the message, and the recipient of the message, respectively.
    The class provides a constructor to initialize the Message object with the specified nickname, message, and recipient.
    It also includes setter methods (setNickName(), setMessage(), setRecipient()) to modify the values of the instance variables,
    and getter methods (getNickName(), getMessage(), getRecipient()) to retrieve the values of the instance variables.
 */


package il.ac.hit.chatserver.objects;

/**
 * Represents a chat message
 */
public class Message {

    // The nickname of the message sender
    private String nickName;

    // The content of the message
    private String message;

    // The recipient of the message
    private String recipient;

    /**
     * Constructs a Message object with the specified nickname, message, and recipient
     *
     * @param nickName  The nickname of the sender
     * @param message   The content of the message
     * @param recipient The recipient of the message
     */
    public Message(String nickName, String message, String recipient) {
        this.nickName = nickName;
        this.message = message;
        this.recipient = recipient;
    }

    /**
     * Sets the nickname of the sender
     *
     * @param nickName The nickname to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Sets the content of the message
     *
     * @param message The message content to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the recipient of the message
     *
     * @param recipient The recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Returns the nickname of the sender
     *
     * @return The nickname of the sender
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Returns the content of the message
     *
     * @return The content of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the recipient of the message
     *
     * @return The recipient of the message
     */
    public String getRecipient() {
        return recipient;
    }
}
