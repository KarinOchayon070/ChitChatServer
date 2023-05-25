/* Developers details:
   - Karin Ochayon, 207797002
   - Dor Uzan, 205890510
*/

import java.util.Iterator;
public interface ClientConnectionIterator extends Iterator<ConnectionProxy> {
    ConnectionProxy next();
    boolean hasNext();
}
