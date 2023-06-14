package il.ac.hit.chatserver.objects;/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (il.ac.hit.chatserver.exception.ChatException.java) represents a custom exception class called il.ac.hit.chatserver.exception.ChatException.
    This class extends the built-in Exception class, which allows you to create and throw exceptions specific to the chat application.
 */

/**
 * The il.ac.hit.chatserver.exception.ChatException class represents an exception specific to the chat application
 * It is a subclass of the Exception class and provides constructors for creating a il.ac.hit.chatserver.exception.ChatException object
 * with a custom error message and an optional cause
 */
public class ChatException extends Exception{

    /**
     * Constructs il.ac.hit.chatserver.exception.ChatException object with the specified error message
     *
     * @param message the error message describing the exception
     */

    public ChatException(String message) {
        super(message);
    }

    /**
     * Constructs a il.ac.hit.chatserver.exception.ChatException object with the specified error message and cause
     *
     * @param message the error message describing the exception
     * @param cause   the cause of the exception
     */

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
