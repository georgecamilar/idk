package interfaces.rmi;

import com.model.Nota;
import com.model.Participant;
import connection.RmiConnectionController;
import interfaces.FrontendController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class RmiMainController extends AnchorPane implements FrontendController {
    private RmiConnectionController connection;
    Stage stage;
    @FXML
    Label arbiterLabel;

    private String name;

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

    public final ObservableList<Participant> dataSource = FXCollections.observableArrayList();


    public RmiMainController(Integer id, String name, RmiConnectionController controller) {

        this.connection = controller;
        this.idUser = id;
        this.name = name;

        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public void requestTotalScores() {
        setTableItems(connection.getAllScores());
    }

    private void setTableItems(List<Participant> list) {
        dataSource.clear();
        dataSource.addAll(list);
    }

    public void requestAdd(ActionEvent event) {
        Integer id = Integer.parseInt(gradeField.getText());
        Double grade = Double.parseDouble(idField.getText());

        Nota nota = new Nota(id, this.idUser, grade);
        connection.addNota(nota);

        requestTotalScores();
    }


    public void requestReport(ActionEvent event) {
        Integer user = this.idUser;
        List<Nota> list = connection.getReport(user);

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
