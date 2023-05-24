import java.util.Iterator;

public interface ClientConnectionIterator extends Iterator<ConnectionProxy> {
    ConnectionProxy next();
    boolean hasNext();
}
