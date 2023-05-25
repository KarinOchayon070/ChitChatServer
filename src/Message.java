public class Message {
    private String nickName;
    private String message;
    private String recipient;
    private  boolean isPrivate;
    public Message(String nickName, String message, String recipient) {
        this.nickName = nickName;
        this.message = message;
        this.recipient = recipient;
        this.isPrivate = false;
    }

    public Message(String nickName, String message, String recipient, boolean isPrivate) {
        this.nickName = nickName;
        this.message = message;
        this.recipient = recipient;
        this.isPrivate = isPrivate;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
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
    public boolean getIsPrivate() {
        return isPrivate;
    }
}
