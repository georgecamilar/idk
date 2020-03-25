package run;

import interfaces.Controller;
import interfaces.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Run extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        primaryStage.setScene(new Scene(controller, 500, 500));
        primaryStage.setTitle("Application");
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}
