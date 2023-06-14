package il.ac.hit.chatserver.interfaces;/*
    Developers details:
    - Karin Ochayon, 207797002
    - Dor Uzan, 205890510
*/

/*
    This file (il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator.java) defines an interface called il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator that extends the Iterator interface.
    It represents an iterator over a collection of il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy objects, which are used to encapsulate client il.ac.hit.chatserver.connections in a chat application.
    The interface declares two methods: next() to retrieve the next il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy object in the iteration, and hasNext() to check if there are more
    il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy objects available in the iteration.
 */

import il.ac.hit.chatserver.network.ConnectionProxy;

import java.util.Iterator;

/**
 The il.ac.hit.chatserver.il.ac.hit.chatserver.connections.ClientConnectionIterator interface represents an iterator over a collection of il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy objects
 which encapsulate client il.ac.hit.chatserver.connections in a chat application
 */
public interface ClientConnectionIterator extends Iterator<ConnectionProxy> {

    /**
     Returns the next il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy object in the iteration
     @return the next il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy object
     */
    ConnectionProxy next();

    /**
     Checks if there are more il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy objects in the iteration
     @return true if there are more il.ac.hit.chatserver.il.ac.hit.chatserver.network.ConnectionProxy objects, false otherwise
     */
    boolean hasNext();
}