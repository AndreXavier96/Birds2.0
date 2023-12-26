package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Breeder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BreederRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBreedersController implements Initializable {
	
	@FXML
	private TableView<Breeder> tableID;
	@FXML
	private TableColumn<Breeder,String> colName, colEmail, colLocale, colDistrict, colAddress, colClube,colStam,deleteButton;
	@FXML 
	private TableColumn<Breeder, Integer> colCellphone;

	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BreederRepository breederRepository = new BreederRepository();
		ObservableList<Breeder> breeders = FXCollections.observableArrayList();;
		try {
			breeders = breederRepository.getAllBreeders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Name"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Locale"));
		tableID.setItems(breeders);
		tableID.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Breeder selectedBreeder = tableID.getSelectionModel().getSelectedItem();
				if (selectedBreeder != null) {
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/views/breeder/ViewSingleBreeder.fxml"));
						Parent root = loader.load();
						ViewSingleBreederController viewSingleBreederController = loader.getController();
						viewSingleBreederController.startValues(selectedBreeder);
						Scene currentScene = tableID.getScene();
						Stage currentStage = (Stage) currentScene.getWindow();
						currentScene.setRoot(root);
						currentStage.sizeToScene();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
}
