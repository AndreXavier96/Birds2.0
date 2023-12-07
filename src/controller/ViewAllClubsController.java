package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Club;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.ClubRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllClubsController implements Initializable {
	@FXML
	private TableView<Club> tableID;
	@FXML
	private TableColumn<Club,String> colFederation, colName, colAcronym, colLocale, colAddress;
	@FXML
	private TableColumn<Club,String> colContact, colEmail,deleteButton;
	
	@FXML
	private Label LabelAlert;
	
	private ClubRepository clubRepository = new ClubRepository();
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ObservableList<Club> clubs = FXCollections.observableArrayList();
		try {
			clubs = clubRepository.getAllClubs();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colFederation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFederation().getAcronym()));
		colName.setCellValueFactory(new PropertyValueFactory<Club,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Club,String>("Acronym"));
		tableID.setItems(clubs);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Club selectedClub = tableID.getSelectionModel().getSelectedItem();
				if (selectedClub!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/clubs/ViewSingleClub.fxml"));
						Parent root = loader.load();
						ViewSingleClubController viewSingleClubController = loader.getController();
						viewSingleClubController.search(selectedClub.getAcronym());
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
}
