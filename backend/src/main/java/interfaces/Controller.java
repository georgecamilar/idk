package interfaces;

import controller.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Controller extends AnchorPane {

    private Service service;
    Stage stage;

    public Controller() {
        try {
            Properties props = new Properties();
            props.load(new FileReader("src/main/resources/bd.config"));
            service = new Service(props);

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
        int id;
        if ((id = service.login(username, password)) >= 0) {
            MainController controller = new MainController(id, username, service);
            stage.setScene(new Scene(controller));
            stage.setTitle("Application");
            controller.setStage(stage);
            stage.show();
        }
    }

    public void loadTable() {

    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
