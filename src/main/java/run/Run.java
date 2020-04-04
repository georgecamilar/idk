package run;

import interfaces.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;
import server.SocketConcurrentServer;

import java.io.IOException;

public class Run extends Application {
    public static void main(String[] args) {
        startServer();
        launch(args);
    }


    private static void startServer() {
        try {
            SocketConcurrentServer server = new SocketConcurrentServer(12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Controller("localhost", 12345));
        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//    public void start(Stage primaryStage) throws Exception {
//        Controller controller = new Controller();
//        primaryStage.setScene(new Scene(controller, 500, 500));
//        primaryStage.setTitle("Application");
//        controller.setStage(primaryStage);
//        primaryStage.show();
//    }
}
