package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Breeder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BreederRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBreedersController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Breeder> tableID;
	@FXML
	private TableColumn<Breeder,String> colName, colEmail, colPostalCode, colLocale, colDistrict, colAddress, colType, colClube;
	@FXML 
	private TableColumn<Breeder, Integer> colCC,colNIF,colCellphone,colCites,colStam;

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
	
	//dont forget to implements Initializable
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BreederRepository breederRepository = new BreederRepository();
		ObservableList<Breeder> breeders = breederRepository.getAllBreeders();
		colCC.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("CC"));
		colName.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Name"));
		colNIF.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("Nif"));
		colCellphone.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("Cellphone"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Email"));
		colPostalCode.setCellValueFactory(new PropertyValueFactory<Breeder,String>("PostalCode"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Locale"));
		colDistrict.setCellValueFactory(new PropertyValueFactory<Breeder,String>("District"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Address"));
		colCites.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("NrCites"));
		colType.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Type"));
		colClube.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Club"));
		colStam.setCellValueFactory(new PropertyValueFactory<Breeder,Integer>("Stam"));
		
		tableID.setItems(breeders);
	}
	
}