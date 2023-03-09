package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Specie;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.SpeciesRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllSpeciesController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Specie> tableID;
	@FXML
	private TableColumn<Specie,Integer> colId,colIncubationDays,colBandingDays,colOutOfCageDays,colMaturityDays,colBandSize;
	@FXML
	private TableColumn<Specie, String> colCommonName,colScientificName;


	@FXML
	public void btnBack(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/MainScene.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpeciesRepository speciesRepository = new SpeciesRepository();
		ObservableList<Specie> species = speciesRepository.getAllSpecies();
		colCommonName.setCellValueFactory(new PropertyValueFactory<Specie,String>("CommonName"));
		colScientificName.setCellValueFactory(new PropertyValueFactory<Specie,String>("ScientificName"));
		colIncubationDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("IncubationDays"));
		colBandingDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("DaysToBand"));
		colOutOfCageDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("OutofCageAfterDays"));
		colMaturityDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("MaturityAfterDays"));
		colBandSize.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("BandSize"));
		tableID.setItems(species);
	}
}
