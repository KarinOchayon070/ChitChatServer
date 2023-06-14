/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (ChatException.java) represents a custom exception class called ChatException.
    This class extends the built-in Exception class, which allows you to create and throw exceptions specific to the chat application.
 */

/**
 * The ChatException class represents an exception specific to the chat application
 * It is a subclass of the Exception class and provides constructors for creating a ChatException object
 * with a custom error message and an optional cause
 */

package il.ac.hit.chatserver.objects;
public class ChatException extends Exception{

    /**
     * Constructs ChatException object with the specified error message
     *
     * @param message the error message describing the exception
     */

    public ChatException(String message) {
        super(message);
    }

    /**
     * Constructs a ChatException object with the specified error message and cause
     *
     * @param message the error message describing the exception
     * @param cause   the cause of the exception
     */

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
