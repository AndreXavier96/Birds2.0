package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import constants.MyValues;
import domains.Bird;
import domains.Brood;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.BroodRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBroodController implements Initializable {
	
	@FXML
	private TextField TfFemale,TfMale;
	
	@FXML
	private TableView<Brood> tableID;
	
	@FXML
	private TableColumn<Brood,String> colMale,colFemale;
	
	@FXML
	private TableColumn<Brood,Date> colStart,colFinish;

	private BroodRepository broodRepository = new BroodRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	
	private ObservableList<Brood> allBroodsList = broodRepository.getAllBroods();
	ObservableList<Bird> listFathers=FXCollections.observableArrayList();;
	ObservableList<Bird> listMothers=FXCollections.observableArrayList();;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableID.setItems(allBroodsList);
		colMale.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFather().getBand()));
		colFemale.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMother().getBand()));
		colStart.setCellValueFactory(new PropertyValueFactory<Brood, Date>("start"));
		colFinish.setCellValueFactory(new PropertyValueFactory<Brood, Date>("finish"));

		TfMale.textProperty().addListener((observableFather, oldValueFather, newValueFather) -> {
			listFathers = filterBirdsMale(newValueFather);
			updateTable();
		});
		
		TfFemale.textProperty().addListener((observableMother, oldValueMother, newValueMother) -> {
			listMothers = filterBirdsFemale(newValueMother);
			updateTable();
		});
		
		tableID.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Brood selectedBrood = tableID.getSelectionModel().getSelectedItem();
				if (selectedBrood != null) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/ViewSingleBrood.fxml"));
						Parent root = loader.load();
						ViewSingleBroodController viewSingleBroodController = loader.getController();
						viewSingleBroodController.updateAllInfo(selectedBrood);
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
	

	private void updateTable() {
		ObservableList<Brood> filteredBroods;
		if (TfFemale.getText().isBlank()) {
			filteredBroods = allBroodsList.filtered(brood -> listFathers.stream()
					.anyMatch(bird -> bird.getBand().toLowerCase().contains(TfMale.getText().toLowerCase())));
		} else if (TfMale.getText().isBlank()) {
			filteredBroods = allBroodsList.filtered(brood -> listMothers.stream()
					.anyMatch(bird -> bird.getBand().toLowerCase().contains(TfFemale.getText().toLowerCase())));
		} else {
			filteredBroods = allBroodsList.filtered(brood -> listFathers.stream()
					.anyMatch(bird -> bird.getBand().toLowerCase().contains(TfMale.getText().toLowerCase()))
					&& listMothers.stream()
							.anyMatch(bird -> bird.getBand().toLowerCase().contains(TfFemale.getText().toLowerCase())));
		}
		tableID.setItems(filteredBroods);
	}

	
	private ObservableList<Bird> filterBirdsMale(String searchTerm) {
		ObservableList<Bird> listMale = birdsRepository.getAllWhere("Sex", MyValues.MACHO);
		List<Bird> filteredBirds = listMale.stream()
				.filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
				.collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	private ObservableList<Bird> filterBirdsFemale(String searchTerm) {
		ObservableList<Bird> listFemale = birdsRepository.getAllWhere("Sex", MyValues.FEMEA);
		List<Bird> filteredBirds = listFemale.stream()
				.filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
				.collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}
}
