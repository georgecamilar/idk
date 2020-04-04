package interfaces;

import com.Request;
import com.RequestType;
import com.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controller extends AnchorPane {

    Stage stage;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private volatile boolean done;
    Thread thread;

    public Controller(String ipAddress, Integer port) {
        try {
            done = false;
            socket = new Socket("localhost", 12345);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

            thread = new Thread(new ReaderThread());
            thread.setDaemon(true);
            thread.start();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    public void clickLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Request request = new Request.Builder().type(RequestType.LOGIN).object(username + ";" + password).build();
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void responseLogin(Response response) {
        if ((Integer) response.content() > -1) {
            this.stage.close();
            try {
                input.close();
                output.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            MainController mainController = new MainController((Integer) response.content(), usernameField.getText(), socket, input, output);
//            MainController mainController = new MainController((Integer) response.content(), usernameField.getText());
            Stage stage = new Stage();
            stage.setScene(new Scene(mainController));
            mainController.setStage(stage);
            stage.show();
            done = true;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public Stage getStage() {
        return stage;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    class ReaderThread implements Runnable {

        @Override
        public void run() {
            while (!done) {
                try {
                    Response in = (Response) input.readObject();
                    Platform.runLater(() -> responseLogin(in));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
