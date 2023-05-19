public interface StringProducer {

    public void addConsumer(StringConsumer consumer);

    public void removeConsumer(StringConsumer consumer);
}
