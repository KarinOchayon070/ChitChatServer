/* Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/
public class ChatException extends Exception{

    public ChatException(String message) {
        super(message);
    }

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
