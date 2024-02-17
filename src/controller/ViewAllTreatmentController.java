package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import domains.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.TreatmentRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllTreatmentController implements Initializable {
	
	@FXML
	private TableView<Treatment> tableID;
	@FXML
	private TableColumn<Treatment,String> colName,colDesc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TreatmentRepository treatmentRepository = new TreatmentRepository();
		ObservableList<Treatment> treatments = FXCollections.observableArrayList();
		try {
			treatments = treatmentRepository.getAllTreatments();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Treatment,String>("Name"));
		colDesc.setCellValueFactory(new PropertyValueFactory<Treatment,String>("Description"));
		tableID.setItems(treatments);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Treatment selectedTreatment = tableID.getSelectionModel().getSelectedItem();
				if (selectedTreatment!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/treatments/ViewSingleTreatment.fxml"));
						Parent root = loader.load();
						ViewSingleTreatmentController viewSingleTreatmentController = loader.getController();
						viewSingleTreatmentController.search(selectedTreatment.getId());
						Scene currentScene = tableID.getScene();
						Stage currentStage =(Stage) currentScene.getWindow();
						currentScene.setRoot(root);
						currentStage.sizeToScene(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}
}
