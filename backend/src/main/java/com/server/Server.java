package com.server;

import com.controller.ServiceImplementation;
import com.model.Nota;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private ServerSocket serverSocket;
    private LinkedBlockingQueue<ClientItem> messages;
    private ArrayList<ConnectionToClient> clientList;

    private ServiceImplementation serviceImplementation;

    public Server(int port) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        serviceImplementation = new ServiceImplementation(props);
        clientList = new ArrayList<>();
        serverSocket = new ServerSocket(port);
        messages = new LinkedBlockingQueue<>();

        Thread acceptThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Waiting for clients");
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
                    ClientItem message = messages.take();
                    

                    String recived = (String) message.recived;
                    String[] fields = recived.split(";");
                    switch (fields[0]) {
                        case "add":
                            if (fields.length - 1 < 3)
                                message.client.write(serviceImplementation.getAllTotalScores());
                            int idParticipant = Integer.parseInt(fields[1]);
                            int idProba = Integer.parseInt(fields[2]);
                            double grade = Double.parseDouble(fields[3]);
                            serviceImplementation.saveNota(new Nota(idParticipant, idProba, grade));
                            message.client.write(serviceImplementation.getAllTotalScores());
                            break;
                        case "login":
                            String username = fields[1];
                            String password = fields[2];
                            message.client.write(serviceImplementation.login(username, password));
                            break;
                        case "report":
                            Integer idUser = Integer.parseInt(fields[1]);
                            message.client.write(this.serviceImplementation.getRaportList(idUser));
                            break;
                        default:

                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Starting thread...");
//        acceptThread.setDaemon(true);
        acceptThread.start();
        messageThread.start();
    }

    public void sendToOne(int index, Object object) throws IndexOutOfBoundsException {
        clientList.get(index).write(object);
    }

    public void sendToAll(Object object) {
        clientList.forEach(client -> {
            client.write(object);
        });
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
                        messages.put(new ClientItem(this, object));
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

    private class ClientItem {
        ConnectionToClient client;
        Object recived;

        ClientItem(ConnectionToClient client, Object recived) {
            this.client = client;
            this.recived = recived;
        }
    }


}





