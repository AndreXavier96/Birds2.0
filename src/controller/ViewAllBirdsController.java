package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Bird;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colId;
	@FXML
	private TableColumn<Bird, String> colNrCriador, colAno, colAnilha;
	
	private Parent root;
	private Stage stage;
	private Scene scene;

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
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllBirds();
		colId.setCellValueFactory(new PropertyValueFactory<Bird,Integer>("id"));
		colNrCriador.setCellValueFactory(new PropertyValueFactory<>("NrCriador"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Ano"));
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Anilha"));
		tableID.setItems(birds);
		
	}
}
