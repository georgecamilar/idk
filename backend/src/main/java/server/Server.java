package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private ServerSocket serverSocket;
    private LinkedBlockingQueue<Object> messages;
    private ArrayList<ConnectionToClient> clientList;


    public Server(int port) throws IOException {
        clientList = new ArrayList<>();
        serverSocket = new ServerSocket(port);
        messages = new LinkedBlockingQueue<>();

        Thread acceptThread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    clientList.add(new ConnectionToClient(socket));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread messageThread = new Thread(() -> {
            while (true) {
                try {
                    Object message = messages.take();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    private class ConnectionToClient {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private Socket socket;

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread(() -> {
                while (true) {
                    try {
                        Object object = inputStream.read();
                        messages.put(object);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            read.setDaemon(true);
            read.start();
        }

        public void write(Object object) {
            try {
                outputStream.writeObject(object);
                outputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendToOne(int index, Object object) throws IndexOutOfBoundsException {
        clientList.get(index).write(object);
    }

    public void sendToAll(Object object) {
        clientList.forEach(client -> {
            client.write(object);
        });
    }
}


