package connection;

import interfaces.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionController {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Thread thread;
    private Controller controller;
    public ConnectionController() {
        try {
            socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            thread = new Thread(new ReadThread());
            thread.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    


    class ReadThread implements Runnable {


        @Override
        public void run() {

        }
    }
}