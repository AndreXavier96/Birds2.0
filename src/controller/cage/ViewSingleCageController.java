package controller.cage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CageRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import controller.bird.ViewSingleBirdController;
import domains.Bird;
import domains.Cage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ViewSingleCageController implements Initializable {
	@FXML
	private Label LbTitle, LabelAlert,LbCode, LbType,LbBirdNr;
	@FXML
	private TextField TfSearch;
	@FXML
	private TableView<Bird> TbBird;
	@FXML
	private TableColumn<Bird,String> TcBand,TcSpecie,TcMutation;
	
	private CageRepository cageRepository = new CageRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TbBird.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Bird selectedBird = TbBird.getSelectionModel().getSelectedItem();
				if (selectedBird!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
						Parent root = loader.load();
						ViewSingleBirdController viewSingleBirdController = loader.getController();
						viewSingleBirdController.search(selectedBird.getBand());
						Scene currentScene = TbBird.getScene();
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
	public void btnDelete(ActionEvent event) throws IOException, SQLException {
		if (validator()&&validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = loader.load();
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer apagar a Gaiola: '" + LbCode.getText() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_CAGE+LbCode.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					Cage c = cageRepository.getCageByCode(TfSearch.getText());
					cageRepository.deleteCage(c);
					clearAllFields();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void search(String code) throws SQLException {
		clearAllFields();
		TfSearch.setText(code);
		Cage c = cageRepository.getCageByCode(TfSearch.getText().toUpperCase());
		updateAllInfo(c);
	}
	
	@FXML
	public void btnSearchForCode(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Cage c = cageRepository.getCageByCode(TfSearch.getText().toUpperCase());
			updateAllInfo(c);
		}
	}

	public void updateAllInfo(Cage c) throws SQLException {
		LbTitle.setText("Gaiola: "+c.getCode());
		LbCode.setText(c.getCode());
		LbType.setText(c.getType());
		LbBirdNr.setText(String.valueOf(birdsRepository.getBirdCountByCageId(c.getId())));
		ObservableList<Bird> birdList = birdsRepository.getAllWhereInt("CageId", c.getId());
		TcBand.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getBand()));
		TcSpecie.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		TcMutation.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getMutations().getName()));
		TbBird.setItems(birdList);
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Gaiola XXXX");
		LbCode.setText(null);
		LbType.setText(null);
		LbBirdNr.setText(null);
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LbCode.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Gaiola tem de ser procurada antes de editar/apagar.");
			validate=false;
		}else if (!TfSearch.getText().equalsIgnoreCase(LbCode.getText())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Gaiola tem de ser procurada antes de editar/apagar.");
	        validate = false;
	    }else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Codigo tem de ser preenchido");
			validate=false;
		}else if (!cageRepository.checkIfCodeExist(TfSearch.getText().toUpperCase())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Codigo nao existe");
			validate=false;
		}else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
