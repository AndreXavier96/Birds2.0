package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllCagesController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Cage> tableID;
	@FXML
	private TableColumn<Cage,String> colCode,colType,colBird;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CageRepository cageRepository = new CageRepository();
		ObservableList<Cage> cages = FXCollections.observableArrayList();
		try {
			cages = cageRepository.getAllCages();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colCode.setCellValueFactory(new PropertyValueFactory<Cage,String>("Code"));
		colType.setCellValueFactory(new PropertyValueFactory<Cage,String>("Type"));
		
		tableID.setItems(cages);
	}
	
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

	
}
