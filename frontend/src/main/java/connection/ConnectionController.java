package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionController {

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> messages;
    private Socket socket;


    public ConnectionController(String IpAddress, int port) throws IOException {
        socket = new Socket(IpAddress, port);
        messages = new LinkedBlockingQueue<>();
        server = new ConnectionToServer(socket);

        Thread messageHandler = new Thread(() -> {
            while (true) {
                try {
                    Object message = messages.take();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        messageHandler.setDaemon(true);
        messageHandler.start();

    }

    private class ConnectionToServer {
        ObjectInputStream input;
        ObjectOutputStream output;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {
            this.socket = socket;
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread(() -> {
                while (true) {
                    try {
                        Object object = input.readObject();
                        messages.put(object);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            read.setDaemon(true);
            read.start();
        }

        private void write(Object object) {
            try {
                output.writeObject(object);
                output.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void send(Object object) {
        server.write(object);
    }
}
