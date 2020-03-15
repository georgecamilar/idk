package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ConcurrentServer server;

    public Worker(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream,ConcurrentServer server) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.server = server;
    }

    @Override
    public void run() {
        //make the changes for everyone but this instance of worker
        try {
            while(true){
                Object clientInput = inputStream.readObject();
                switch(clientInput.toString()){
                    case "SAVE":
                        O
                        server.getController().saveArbiter();
                        break;
                    case "DELETE":break;
                    case "LOGIN":break;
                        
                }
                server.notifyClients(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    
}
