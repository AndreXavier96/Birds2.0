package controller.couples;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import domains.Couples;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import repository.CouplesRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllCouplesController implements Initializable {
	
	@FXML
	private TableView<Couples> tableID;
	@FXML
	private TableColumn<Couples,String> colMale,colFemale,colCage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CouplesRepository couplesRepository = new CouplesRepository();
		ObservableList<Couples> couples = FXCollections.observableArrayList();
		try {
			couples = couplesRepository.getAllCouples();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colMale.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMale().getBand()));
		colFemale.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFemale().getBand()));
		colCage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCage().getCode()));
		tableID.setItems(couples);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Couples selectedCouples = tableID.getSelectionModel().getSelectedItem();
				if (selectedCouples!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/couples/ViewSingleCouple.fxml"));
						Parent root = loader.load();
						ViewSingleCouplesController viewSingleCouplesController = loader.getController();
						viewSingleCouplesController.search(selectedCouples.getMale().getBand());
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
