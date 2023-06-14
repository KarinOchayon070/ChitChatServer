/*
    Developers details:
    - Karin Ochayon, 207797002
    - Dor Uzan, 205890510
*/

/*
    This file (ClientConnectionIterator.java) defines an interface called ClientConnectionIterator that extends the Iterator interface.
    It represents an iterator over a collection of ConnectionProxy objects, which are used to encapsulate client connections in a chat application.
    The interface declares two methods: next() to retrieve the next ConnectionProxy object in the iteration, and hasNext() to check if there are more
    ConnectionProxy objects available in the iteration.
 */

package il.ac.hit.chatserver.interfaces;
import il.ac.hit.chatserver.network.ConnectionProxy;
import java.util.Iterator;

/**
 The ClientConnectionIterator interface represents an iterator over a collection of ConnectionProxy objects
 which encapsulate client connections in a chat application
 */
public interface ClientConnectionIterator extends Iterator<ConnectionProxy> {

    /**
     Returns the next ConnectionProxy object in the iteration
     @return the next ConnectionProxy object
     */
    ConnectionProxy next();

    /**
     Checks if there are more ConnectionProxy objects in the iteration
     @return true if there are more ConnectionProxy objects, false otherwise
     */
    boolean hasNext();
}