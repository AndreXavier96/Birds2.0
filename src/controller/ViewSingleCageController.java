package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CageRepository;

import java.io.IOException;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import domains.Cage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleCageController {
	@FXML
	private Label LbTitle, LabelAlert,LbCode, LbType,LbBirdNr;
	@FXML
	private TextField TfSearch;
	
	private CageRepository cageRepository = new CageRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();

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
		Cage c = cageRepository.getCageByCode(TfSearch.getText());
		updateAllInfo(c);
	}
	
	@FXML
	public void btnSearchForCode(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Cage c = cageRepository.getCageByCode(TfSearch.getText());
			updateAllInfo(c);
		}
	}

	public void updateAllInfo(Cage c) throws SQLException {
		LbTitle.setText("Gaiola: "+c.getCode());
		LbCode.setText(c.getCode());
		LbType.setText(c.getType());
		LbBirdNr.setText(String.valueOf(birdsRepository.getBirdCountByCageId(c.getId())));
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
		}else if (!cageRepository.checkIfCodeExist(TfSearch.getText())) {
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
