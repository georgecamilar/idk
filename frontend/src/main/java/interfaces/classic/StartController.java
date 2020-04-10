package interfaces.classic;

import com.Response;
import connection.ConnectionController;
import interfaces.FrontendController;
import interfaces.classic.MainController;
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
    
    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    public StartController(String ipAddress, Integer port) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    public void clickLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        connectionController.requestLogin(username, password);
    }

    public void responseLogin(Response response) {
        if ((Integer) response.content() > -1) {
            MainController mainController = new MainController((Integer) response.content(), this.usernameField.getText(), connectionController);
            mainController.setNetwork(connectionController);
            connectionController.setController(mainController);

            this.stage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(mainController));
            mainController.setStage(stage);
            stage.show();
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
