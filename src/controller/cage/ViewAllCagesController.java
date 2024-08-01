package controller.cage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Cage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.CageRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllCagesController implements Initializable {
	
	@FXML
	private TableView<Cage> tableID;
	@FXML
	private TableColumn<Cage,String> colCode,colType,colBird,deleteButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CageRepository cageRepository = new CageRepository();
		ObservableList<Cage> cages = FXCollections.observableArrayList();
		try {
			cages = cageRepository.getAllCages();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colCode.setCellValueFactory(new PropertyValueFactory<Cage,String>("Code"));
		colType.setCellValueFactory(new PropertyValueFactory<Cage,String>("Type"));
		tableID.setItems(cages);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Cage selectedCage = tableID.getSelectionModel().getSelectedItem();
				if (selectedCage!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/cages/ViewSingleCage.fxml"));
						Parent root = loader.load();
						ViewSingleCageController viewSingleCageController = loader.getController();
						viewSingleCageController.search(selectedCage.getCode());
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
