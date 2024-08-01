package controller.federation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.FederationRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllFederationsController implements Initializable {
	@FXML
	private TableView<Federation> tableID;
	@FXML
	private TableColumn<Federation, String> colName, colAcronym, colCountry, colEmail, deleteButton;
	@FXML
	private Label LabelAlert;
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		FederationRepository federationRepository = new  FederationRepository();
		ObservableList<Federation> federations = FXCollections.observableArrayList();
		try {
			federations = federationRepository.getAllFederations();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Federation,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Federation,String>("Acronym"));
		tableID.setItems(federations);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Federation selectedFederation = tableID.getSelectionModel().getSelectedItem();
				if (selectedFederation!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/federation/ViewSingleFederation.fxml"));
						Parent root = loader.load();
						ViewSingleFederationController viewSingleFederationController = loader.getController();
						viewSingleFederationController.search(selectedFederation.getAcronym());
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
		
//	public boolean validatorDelete(Federation federation) throws SQLException {
//		boolean validate=false;
//		if (breederFederationRepository.federationHasBreeders(federation.getId())) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			LabelAlert.setText("Federacao nao pode ser apagada porque tem criadores associados.");
//			validate=false;
//		}else {
//			LabelAlert.setText("");
//			validate=true;
//		}
//		return validate;
//	}
}
