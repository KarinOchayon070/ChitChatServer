import java.io.*;
import java.net.Socket;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer {

    private StringConsumer consumer;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;


    public ConnectionProxy(Socket socket) throws ChatException{
        try{
            this.socket = socket;
            is = socket.getInputStream();
            os = socket.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
        }catch (IOException e){
            throw new ChatException("Problem creating streams", e);
        }
    }

    public  ConnectionProxy(String computer, int port) throws ChatException{
        try{
            this.socket = new Socket(computer, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);
        }
        catch (IOException e){
            throw new ChatException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void consume(String text) throws ChatException {
        try {
            System.out.println("inside consume invoked on a ConnectionProxy object  thread="+Thread.currentThread().getName());
            dos.writeUTF(text);
            System.out.println("'"+text+"' was sent through dos.writeUTF  thread="+Thread.currentThread().getName());
        } catch (IOException e) {
            throw new ChatException("Problem writing text through the data output stream",e);
        }
    }

    public void run() {
        while(true) {
            System.out.println("inside run of ConnectionProxy  thread="+Thread.currentThread().getName());
            try {
                System.out.println("about to call dis.readUTF  thread="+Thread.currentThread().getName());
                String text = dis.readUTF();
                System.out.println("dis.readUTF returned "+text+"     thread="+Thread.currentThread().getName());
                this.consumer.consume(text);
                System.out.println("calling consumer.consume() passing over "+text+ "    thread="+Thread.currentThread().getName());
            } catch(IOException | ChatException e) {
                try {
                    this.consumer.consume("error");
                } catch(ChatException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public void addConsumer(StringConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void removeConsumer(StringConsumer consumer) {
        if(this.consumer == consumer){
            this.consumer = null;
        }
    }
}
