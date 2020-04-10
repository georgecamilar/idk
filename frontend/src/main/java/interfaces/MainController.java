package interfaces;

import com.Response;
import connection.ConnectionController;
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


public class MainController extends AnchorPane implements FrontendController {

    private ConnectionController network;

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


    public MainController(Integer id, String name, ConnectionController connectionController) {
        this.network = connectionController;
        this.idUser = id;
        this.name = name;
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
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
        network.requestGetAll();
    }

    public void responseTotalScores(Response response) {
        try {
            this.dataSource.clear();
            this.dataSource.addAll((List<Participant>) response.content());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void requestAdd(ActionEvent event) {
        Integer id = Integer.parseInt(gradeField.getText());
        Double grade = Double.parseDouble(idField.getText());

        network.requestAdd(id, this.idUser, grade);
    }

    public void addResponse(Response response) {
        List<Participant> list = (List<Participant>) response.content();
        this.dataSource.clear();
        this.dataSource.addAll(list);
    }

    public void requestReport(ActionEvent event) {
        Integer id = this.idUser;
        network.requestReport(id);
    }

    public void responseReport(Response response) {
        List<Nota> list = (List<Nota>) response.content();
        RaportController reportCtrl = new RaportController(list);
        Stage st = new Stage();
        st.setScene(new Scene(reportCtrl));
        st.show();
    }

    public void getRaportResponse(Response response) {
        System.out.println("Not implemented yet");
    }

    public ConnectionController getNetwork() {
        return network;
    }

    public void setNetwork(ConnectionController network) {
        this.network = network;
    }
}
