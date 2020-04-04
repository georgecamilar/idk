package connection;

import com.Request;
import com.RequestType;
import com.Response;
import com.ResponseType;
import interfaces.Controller;
import interfaces.MainController;
import interfaces.RaportController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Nota;
import model.Participant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class OneController extends AnchorPane {
    @FXML
    private TextField idField;
    @FXML
    private TextField gradeField;
    @FXML
    TextField usernameField;
    private String name;

    @FXML
    Label arbiterLabel;
    @FXML
    TextField passwordField;


    @FXML
    TableView<Participant> scoreTable;
    public final ObservableList<Participant> dataSource = FXCollections.observableArrayList();
    @FXML
    TableColumn<Participant, String> nameColumn;
    @FXML
    TableColumn<Participant, Double> scoreColumn;

    private Integer id;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Thread thread;
    private Controller controller;
    private Stage stage;
    private volatile boolean finished;

    public OneController() {
        try {
            finished = false;

            socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Start.fxml"));
                loader.setRoot(this);
                loader.setController(this);
                loader.load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            thread = new Thread(new ReadThread());
            thread.start();


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        this.arbiterLabel.textProperty().set(name);
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.scoreColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        scoreTable.setItems(dataSource);
        this.requestGetAll();
    }

    /**
     * Login Controller part is here
     */


    public void clickLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Request request = new Request.Builder().type(RequestType.LOGIN).object(username + ";" + password).build();
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void responseLogin(Response response) {
        this.id = (Integer) response.content();

        if (id > -1) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            try {
                loader.load();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
            stage.show();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Main Controller part is here
     */
    public void requestGetAll() {
        Request request = new Request.Builder().type(RequestType.GETSCORES).build();
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void responseGetAll(Response response) {
        dataSource.clear();
        dataSource.addAll((List<Participant>) response.content());
    }

    public void requestAdd(ActionEvent event) {
        Nota nota = new Nota(Integer.parseInt(idField.getText()), id, Double.parseDouble(gradeField.getText()));
        Request request = new Request.Builder().type(RequestType.ADD).object(nota).build();

        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void requestRaport(ActionEvent event) {
        Request request = new Request.Builder().type(RequestType.REPORT).build();
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void responseRaport(Response response) {
        RaportController raportController = new RaportController((List<Nota>) response.content());

        Stage stage = new Stage();
        stage.setScene(new Scene(raportController));
        stage.setTitle("Reports");
        stage.show();
    }

    /** 
     * ReadThread implementation is here
     */


    class ReadThread implements Runnable {
        @Override
        public void run() {
            try {
                while (!finished) {
                    try {
                        Response returned = (Response) input.readObject();
                        Platform.runLater(() -> handleGiven(returned));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleGiven(Response response) {
        if (ResponseType.GETSCORES == response.type()) {
            this.responseGetAll(response);
        } else if (ResponseType.LOGIN == response.type()) {
            this.responseLogin(response);
        }
    }


    /**
     * Utils part
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
