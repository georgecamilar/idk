package interfaces.rmi;

import connection.ConnectionController;
import connection.RmiConnectionController;
import interfaces.FrontendController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RmiStartController extends AnchorPane implements FrontendController {

    private RmiConnectionController connection;

    Stage stage;

    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    public RmiStartController(String domain, int port) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickLogin(ActionEvent event) {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        Integer id = this.connection.login(username, password);
        if (id > -1) {
            RmiMainController controller = new RmiMainController(id, this.usernameField.getText(), this.connection);
            this.stage.close();
            Stage newStage = new Stage();
            newStage.setTitle("Application");
            newStage.setScene(new Scene(controller));
            controller.setStage(newStage);
            newStage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Login");
            alert.show();
        }
    }

    public RmiConnectionController getConnection() {
        return connection;
    }

    public void setConnection(RmiConnectionController connection) {
        this.connection = connection;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
