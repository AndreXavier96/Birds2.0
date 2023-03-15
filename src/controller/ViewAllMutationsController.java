package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Mutation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
	private TableColumn<Mutation,String> colName,colVar1,colVar2,colVar3,colObs,colObservation;
	@FXML
	private TableColumn<Mutation,String> colSpecie;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MutationsRepository mutationsRepository = new MutationsRepository();
		ObservableList<Mutation> mutations=FXCollections.observableArrayList();
		try {
			mutations = mutationsRepository.getAllMutations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Name"));
		colVar1.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var1"));
		colVar2.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var3"));
		colVar3.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var2"));
		colObs.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Obs"));
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
