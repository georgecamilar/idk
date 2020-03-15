package server;

import controller.Controller;
import repositories.ArbiterRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConcurrentServer {
    private ServerSocket serverSocket;
    private List<Worker> clientPool;
    private Controller controller ;

    public Controller getController() {
        return controller;
    }

    public ConcurrentServer(int port, Properties properties) {
        try {
            controller = new Controller(new ArbiterRepository(properties, "arbiter"));
            serverSocket = new ServerSocket(port);
            clientPool = new ArrayList<>();
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        try {
            Socket socket = null;

            while (true) {
                socket = serverSocket.accept();
                
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                
                clientPool.add(new Worker(socket, inputStream, outputStream,this));
                clientPool.get(clientPool.size()-1).run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void notifyClients(Worker worker){
        //apart from the parameter worker, we make the changes for everyone
        
    }
}
