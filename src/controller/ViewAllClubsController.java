package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Club;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.ClubRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllClubsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Club> tableID;
	@FXML
	private TableColumn<Club,String> colFederation, colName, colAcronym, colLocale, colAddress;
	@FXML
	private TableColumn<Club,String> colContact, colEmail;

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
		ClubRepository clubRepository = new ClubRepository();
		ObservableList<Club> clubs = clubRepository.getAllClubs();
		colFederation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFederation().getAcronym()));
		colName.setCellValueFactory(new PropertyValueFactory<Club,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Club,String>("Acronym"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Club,String>("Locale"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Club,String>("Address"));
		colContact.setCellValueFactory(new PropertyValueFactory<Club,String>("Phone"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Club,String>("Email"));
		tableID.setItems(clubs);
	}
}
