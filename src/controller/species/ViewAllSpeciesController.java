package controller.species;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.SpeciesRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllSpeciesController implements Initializable {
	
	@FXML
	private TableView<Specie> tableID;
	@FXML
	private TableColumn<Specie,Integer> colId,colIncubationDays,colBandingDays,colOutOfCageDays,colMaturityDays,colBandSize;
	@FXML
	private TableColumn<Specie, String> colCommonName,colScientificName,deleteButton;
	@FXML
	private Label LabelAlert;

	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpeciesRepository speciesRepository = new SpeciesRepository();
		ObservableList<Specie> species=FXCollections.observableArrayList();
		try {
			species = speciesRepository.getAllSpecies();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colCommonName.setCellValueFactory(new PropertyValueFactory<Specie,String>("CommonName"));
		colScientificName.setCellValueFactory(new PropertyValueFactory<Specie,String>("ScientificName"));
		tableID.setItems(species);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Specie selectedSpecie = tableID.getSelectionModel().getSelectedItem();
				if (selectedSpecie!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/species/ViewSingleSpecie.fxml"));
						Parent root = loader.load();
						ViewSingleSpecieController viewSingleSpecieController = loader.getController();
						viewSingleSpecieController.search(selectedSpecie.getCommonName());
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
	
//	public boolean validatorDelete(Specie specie) throws SQLException {
//		boolean validate=false;
//		if (birdsRepository.getBirdWhereInt("SpeciesId", specie.getId()) != null) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			LabelAlert.setText("Especie nao pode ser apagada porque tem passaros associados.");
//			validate=false;
//		}else {
//			LabelAlert.setText("");
//			validate=true;
//		}
//		return validate;
//	}
}
