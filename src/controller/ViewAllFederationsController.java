package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Federation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.FederationRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllFederationsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Federation> tableID;
	@FXML
	private TableColumn<Federation,String> colName, colAcronym, colCountry, colEmail;
	
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
		FederationRepository federationRepository = new  FederationRepository();
		ObservableList<Federation> federations = federationRepository.getAllFederations();
		colName.setCellValueFactory(new PropertyValueFactory<Federation,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Federation,String>("Acronym"));
		colCountry.setCellValueFactory(new PropertyValueFactory<Federation,String>("Country"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Federation,String>("Email"));
		tableID.setItems(federations);
	}
}
