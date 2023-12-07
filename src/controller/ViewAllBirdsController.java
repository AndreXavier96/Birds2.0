package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import domains.Bird;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colAno;
	@FXML
	private TableColumn<Bird, String> colAnilha,colState,colSex,colSpecie,colCage,deleteButton;
	@FXML
	private Label LabelAlert;
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllBirds();
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Year"));
		colState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		colSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		colCage.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getCage().getCode()));
		tableID.setItems(birds);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Bird selectedBird = tableID.getSelectionModel().getSelectedItem();
				if (selectedBird!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
						Parent root = loader.load();
						ViewSingleBirdController viewSingleBirdController = loader.getController();
						viewSingleBirdController.search(selectedBird.getBand());
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
}
