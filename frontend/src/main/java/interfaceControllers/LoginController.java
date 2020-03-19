package interfaceControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML
    public void initialize(){
        
    }
    @FXML
    TextField usernameField;
    
    @FXML
    TextField passwordField;
    
    public void clickLogin(){
        //socket client connection or rest client request

        System.out.println("[INFO] Connected");
    }
}
