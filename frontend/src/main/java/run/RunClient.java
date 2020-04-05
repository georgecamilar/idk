package run;

import connection.ConnectionController;
import interfaces.StartController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnectionController connectionCtrl = new ConnectionController();
        StartController controller = new StartController("localhost", 12345);
        controller.setConnectionController(connectionCtrl);
        connectionCtrl.setController(controller);
        
        Scene scene = new Scene(controller);
        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        controller.setStage(primaryStage);
        primaryStage.show();


    }
}
