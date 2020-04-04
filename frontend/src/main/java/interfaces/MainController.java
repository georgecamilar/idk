package interfaces;

import com.Request;
import com.RequestType;
import com.Response;
import com.ResponseType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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


public class MainController extends AnchorPane {


    @FXML
    Label arbiterLabel;

    private String name;
    Stage stage;

    @FXML
    private TextField idField;
    @FXML
    private TextField gradeField;

    @FXML
    TableView<Participant> scoreTable;

    @FXML
    TableColumn<Participant, String> nameColumn;

    @FXML
    TableColumn<Participant, Double> scoreColumn;

    private Integer idUser;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean done;

    public final ObservableList<Participant> dataSource = FXCollections.observableArrayList();

    public MainController(Integer id, String name, Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.idUser = id;
        this.name = name;
        stage = new Stage();
        done = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

            this.socket = socket;
            this.input = inputStream;
            this.output = outputStream;
        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    public MainController(Integer id, String name) {
        this.idUser = id;
        this.name = name;
        stage = new Stage();
        done = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

            this.socket = new Socket("localhost", 12345);
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        this.arbiterLabel.textProperty().set(name);
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.scoreColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        scoreTable.setItems(dataSource);
        requestTotalScores();
    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getArbiterLabel() {
        return arbiterLabel.textProperty().get();
    }

    public void setArbiterLabel(String text) {
        this.arbiterLabel.textProperty().set(text);
    }


    //functions for request and responses

    public void requestTotalScores() {
        Request request = new Request.Builder().type(RequestType.GETSCORES).object(null).build();
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void responseTotalScores(Response response) {
        try {
            this.dataSource.clear();
            this.dataSource.addAll((List<Participant>) response.content());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void addButton(ActionEvent event) {

    }

    public void addResponse(Response response) {

    }

    public void getRaport(ActionEvent event) {

    }

    public void getRaportResponse(Response response) {

    }

    class ReadThread implements Runnable {

        @Override
        public void run() {
            while (!done) {
                try {
                    Response response = (Response) input.readObject();

                    if (isUpdate(response)) {
                        //add
                    } else {
                        Platform.runLater(() -> responseTotalScores(response));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isUpdate(Response response) {
        return ResponseType.ADD == response.type();
    }
}
