package interfaces;

import controller.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    
    private Service service;
    
    public Controller() throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/bd.config"));
        service = new Service(props);
    }
    
    @FXML
    public void initialize(){
    }
    
    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    public void clickLogin(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if(service.login(username, password)){
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setTitle("Application");
                primaryStage.setScene(scene);

                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void loadTable(){
        
    }
    
    
    
}
