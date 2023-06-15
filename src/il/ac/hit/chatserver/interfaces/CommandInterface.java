/*
 Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

/*
    This file (CommandInterface.java) defines an interface called CommandInterface.
    This interface represents a command that can be executed.
    It declares a single method execute() which is responsible for executing the command.
 */

package il.ac.hit.chatserver.interfaces;

/**
 * The CommandInterface represents an interface for executing commands
 * Implementing classes should provide the implementation for the execute() method
 */
public interface CommandInterface {

    /**
     * Executes the command
     */
    void execute();
}