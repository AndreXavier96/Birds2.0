package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllMutationsController implements Initializable {
	
	@FXML
	private TableView<Mutation> tableID;
	@FXML
	private TableColumn<Mutation,String> colName,colVar1,colVar2,colVar3,colObs,colObservation;
	@FXML
	private TableColumn<Mutation,String> colSpecie,deleteButton;
	@FXML
	private Label LabelAlert;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MutationsRepository mutationsRepository = new MutationsRepository();
		ObservableList<Mutation> mutations=FXCollections.observableArrayList();
		try {
			mutations = mutationsRepository.getAllMutations();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Name"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecie().getCommonName()));
		tableID.setItems(mutations);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Mutation selectedMutation = tableID.getSelectionModel().getSelectedItem();
				if (selectedMutation!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/mutations/ViewSingleMutation.fxml"));
						Parent root = loader.load();
						ViewSingleMutationController viewSingleMutationController = loader.getController();
						viewSingleMutationController.search(selectedMutation.getName());
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
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}
}
