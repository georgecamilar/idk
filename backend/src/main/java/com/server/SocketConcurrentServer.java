package com.server;

import com.Request;
import com.Response;
import com.ResponseType;
import com.controller.ServiceImplementation;
import com.model.Nota;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SocketConcurrentServer {
    private ServerSocket server;
    protected List<ClientWorker> workers = new ArrayList<>(); //client workers are observers, the SocketConcurrentClass is the Observable
    protected List<String> loggedIn;

    public SocketConcurrentServer(int port) {
        try {
//            this.workers = new ArrayList<>();
            this.server = new ServerSocket(port);
            loggedIn = new ArrayList<>();
            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runServer() {
        while (true) {
            Socket socket = null;
            try {
                System.out.println("Waiting for clients...");
                socket = server.accept();

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                System.out.println("Client connected");
                ClientWorker worker = new ClientWorker(socket, input, output);
                this.workers.add(worker);
                worker.setServerInstance(this);

                Thread thread = new Thread(worker);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void notifyAllClients(ClientWorker worker) {
        for (ClientWorker element : this.workers) {
            if (!element.equals(worker)) {
                element.sendToClient();
            }
        }
    }

}

class ClientWorker implements Runnable, IObserver<ClientWorker> {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ServiceImplementation serviceImplementation;
    private SocketConcurrentServer serverInstance;

    ClientWorker(Socket client, SocketConcurrentServer server) throws IOException {
        this.serverInstance = server;
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        serviceImplementation = new ServiceImplementation(props);

        this.client = client;

        this.input = new ObjectInputStream(client.getInputStream());
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.output.flush();

    }

    ClientWorker(Socket client, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        serviceImplementation = new ServiceImplementation(props);

        this.client = client;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while (client.isConnected()) {
            try {
                Request request = (Request) input.readObject();
                Response response = null;
                switch (request.type()) {
                    case ADD:
                        String recived = (String) request.content();
                        String[] addFields = recived.split(";");
                        Nota nota = new Nota(Integer.parseInt(addFields[0]), Integer.parseInt(addFields[1]), Double.parseDouble(addFields[2]));
                        serviceImplementation.saveNota(nota);

                        response = new Response.Builder().type(ResponseType.GETSCORES).object(serviceImplementation.getAllTotalScores()).build();
                        serverInstance.notifyAllClients(this);
                        output.writeObject(response);
                        output.flush();
                        break;
                    case REPORT:
                        List<Nota> list = serviceImplementation.getRaportList((Integer) request.content());
                        response = new Response.Builder().type(ResponseType.REPORT).object(list).build();
                        output.writeObject(response);
                        output.flush();
                        break;
                    case LOGIN:
                        String content = (String) request.content();
                        String[] fields = content.split(";");

                        Integer result = serviceImplementation.login(fields[0], fields[1]);
//                        if (result > -1) {
//                            serverInstance.workers.add(this);
//                        }

                        response = new Response.Builder().type(ResponseType.LOGIN).object(result).build();
                        output.writeObject(response);
                        output.flush();
                        break;
                    case LOGOUT:
                        String name = (String) request.content();
                        for (String username : serverInstance.loggedIn) {
                            if (username.equals(name)) {
                                response = new Response.Builder().type(ResponseType.LOGOUT).object(username).build();
                                break;
                            }
                        }
                        if (response == null) {
                            response = new Response.Builder().type(ResponseType.FAILURE).object("Player not logged in").build();
                        }

                        output.writeObject(response);
                        output.flush();
                        break;


                    case GETSCORES:
                        response = new Response.Builder().type(ResponseType.GETSCORES).object(serviceImplementation.getAllTotalScores()).build();
                        output.writeObject(response);
                        output.flush();
                        break;
                    default:
                        break;
                }
            } catch (EOFException ex) {
                ex.printStackTrace();
                try {
                    this.output.close();
                    this.input.close();
                    client.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            } catch (SocketException ex) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public void sendToClient() {
        Response response = new Response.Builder().type(ResponseType.GETSCORES).object(serviceImplementation.getAllTotalScores()).build();
        try {
            output.writeObject(response);
            output.flush();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }


    @Override
    public void notifyClients(List<ClientWorker> clientList) {
        for (ClientWorker client : clientList) {
            try {
                output.writeObject(this.serviceImplementation.getAllTotalScores());
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SocketConcurrentServer getServerInstance() {
        return serverInstance;
    }

    public void setServerInstance(SocketConcurrentServer serverInstance) {
        this.serverInstance = serverInstance;
    }
}



