/* Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/
public class Message {
    private String nickName;
    private String message;
    private String recipient;

    public Message(String nickName, String message, String recipient) {
        this.nickName = nickName;
        this.message = message;
        this.recipient = recipient;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getNickName() {
        return nickName;
    }
    public String getMessage() {
        return message;
    }
    public String getRecipient() {
        return recipient;
    }
}
