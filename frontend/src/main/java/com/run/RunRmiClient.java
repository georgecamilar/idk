package com.run;

import com.model.Participant;
import connection.ConnectionController;
import connection.RmiConnectionController;
import connection.Service;
import interfaces.classic.StartController;
import interfaces.rmi.RmiMainController;
import interfaces.rmi.RmiStartController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunRmiClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        RmiConnectionController connectionCtrl = new RmiConnectionController();
        RmiStartController controller = new RmiStartController("localhost", 12345);
        controller.setConnection(connectionCtrl);

        Scene scene = new Scene(controller);
        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}
