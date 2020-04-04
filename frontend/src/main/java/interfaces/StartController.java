package interfaces;

import com.Request;
import com.RequestType;
import com.Response;
import connection.ConnectionController;
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

public class StartController extends AnchorPane implements FrontendController {
    private ConnectionController connectionController;
    Stage stage;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    Thread thread;

    public StartController(String ipAddress, Integer port) {
        try {
            socket = new Socket(ipAddress, port);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

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
            MainController mainController = new MainController((Integer) response.content(), this.usernameField.getText(), connectionController);
            connectionController.setController(mainController);

            this.stage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(mainController));
            mainController.setStage(stage);
            stage.show();

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

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }
}
