package run;

import connection.OneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunClient extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        OneController controller = new OneController();
        Scene scene = new Scene(controller);
        primaryStage.setTitle("Application");
        primaryStage.setScene(scene);
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}
