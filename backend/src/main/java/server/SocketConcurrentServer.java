package server;

import com.Request;
import com.Response;
import com.ResponseType;
import controller.Service;
import model.Nota;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;


public class SocketConcurrentServer {
    private ServerSocket server;
    private LinkedBlockingQueue<Object> tasks;
    protected List<ClientWorker> workers; //client workers are observers, the SocketConcurrentClass is the Observable
    protected List<String> loggedIn;

    public SocketConcurrentServer(int port) {
        try {
            this.server = new ServerSocket(port);
            loggedIn = new ArrayList<>();
            this.workers = new ArrayList<>();
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
                Thread thread = new Thread(worker);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}

class ClientWorker implements Runnable, IObserver<ClientWorker> {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Service service;
    private SocketConcurrentServer serverInstance;

    ClientWorker(Socket client, SocketConcurrentServer server) throws IOException {
        this.serverInstance = server;
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        service = new Service(props);

        this.client = client;

        this.input = new ObjectInputStream(client.getInputStream());
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.output.flush();

    }

    ClientWorker(Socket client, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        service = new Service(props);

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
                        service.saveNota((Nota) request.content());
                        response = new Response.Builder().type(ResponseType.SUCCESS).build();
                        break;
                    case REPORT:
                        List<Nota> list = service.getRaportList((Integer) request.content());
                        response = new Response.Builder().type(ResponseType.REPORT).object(list).build();
                        output.writeObject(response);
                        output.flush();
                        break;
                    case LOGIN:
                        String content = (String) request.content();
                        String[] fields = content.split(";");

                        Integer result = service.login(fields[0], fields[1]);
                        if (result > -1) {
                        }

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
                        response = new Response.Builder().type(ResponseType.GETSCORES).object(service.getAllTotalScores()).build();
                        output.writeObject(response);
                        output.flush();
                        break;
                    default:
                        break;
                }
            }catch(EOFException ex){
                ex.printStackTrace();
                try {
                    this.output.close();
                    this.input.close();
                    client.close();
                } catch (Exception exception) {
                    System.err.println(exception.getMessage());
                }
                break;
            }
            catch (SocketException ex) {
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
                System.err.println(ex.getMessage());
            }

        }
    }


    @Override
    public void notifyClients(List<ClientWorker> clientList) {
        for (ClientWorker client : clientList) {
            try {
                output.writeObject(this.service.getAllTotalScores());
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



