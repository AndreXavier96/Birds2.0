package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import domains.Mutation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.MutationsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllMutationsController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Mutation> tableID;
	@FXML
	private TableColumn<Mutation,String> colName,colType,colSymbol,colObservation;
	@FXML
	private TableColumn<Mutation,String> colSpecie;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MutationsRepository mutationsRepository = new MutationsRepository();
		ObservableList<Mutation> mutations = mutationsRepository.getAllMutations();
		colName.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Name"));
		colType.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Type"));
		colSymbol.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Symbol"));
		colObservation.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Observation"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecie().getCommonName()));

		tableID.setItems(mutations);
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
