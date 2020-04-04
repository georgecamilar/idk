package interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Nota;

import java.io.IOException;
import java.util.List;

public class RaportController extends AnchorPane {
    @FXML
    private TableView<Nota> tableView;
    private ObservableList<Nota> dataSource = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Nota, Integer> nameColumn;
    @FXML
    private TableColumn<Nota, Double> scoreColumn;
    private List<Nota> nota;

    public RaportController(List<Nota> nota) {
        this.nota = nota;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/report.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("participantId"));
        this.scoreColumn.setCellValueFactory(new PropertyValueFactory<>("nota"));

        tableView.setItems(dataSource);
        loadTable();
    }

    private void loadTable() {
        dataSource.addAll(this.nota);
    }
}
