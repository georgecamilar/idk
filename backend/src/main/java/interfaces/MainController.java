package interfaces;

import controller.Service;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import model.Participant;

import java.io.IOException;

public class MainController extends AnchorPane {

    Service service;

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
    
    public final ObservableList<Participant> dataSource = FXCollections.observableArrayList();

    public MainController(Integer id, String name, Service service) {
        this.idUser = id;
        this.service = service;
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
        
        dataSource.addListener(new ListChangeListener<Participant>() {
            @Override
            public void onChanged(Change<? extends Participant> c) {
               //notify all 
            }
        });

    }
    
    
    public void getRaport(ActionEvent event){
        
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        this.arbiterLabel.textProperty().set(name);


        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.scoreColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        scoreTable.setItems(dataSource);
        loadTable();
    }
    
    private void loadTable(){
        dataSource.addAll(service.getAllTotalScores());
    }


    public void addButton(ActionEvent event) {
        try{
            Double grade = Double.parseDouble(gradeField.getText());
            
            
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
    
}
