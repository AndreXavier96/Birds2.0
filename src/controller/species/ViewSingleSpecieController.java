package controller.species;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.SpeciesRepository;

import java.io.IOException;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import domains.Specie;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleSpecieController {
	@FXML
	private Label LabelAlert;
	@FXML
	private TextField TfSearch;
	@FXML
	private Label LbTitle,LbCommonName, LbScientificName, LbIncubationDays, LbBandDays;
	@FXML
	private Label LbOutOfCageDays, LbMaturityAfterDays, LbBandSize;

	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	
	@FXML
 	public void btnEdit(ActionEvent event) throws SQLException, IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/AddSpeciesView.fxml"));
			Parent root = loader.load();
			Specie s = speciesRepository.getSpecieFromString(TfSearch.getText());
			AddSpeciesViewController addSpeciesViewController = loader.getController();
			addSpeciesViewController.startValuesEdit(s);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_EDIT_SPECIE + LbCommonName.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Specie updatedSpecie = speciesRepository.getSpecieById(s.getId());
			search(updatedSpecie.getCommonName());
		}
	}

	@FXML
	public void btnDelete(ActionEvent event) throws SQLException, IOException {
		if (validator() && validatorSearch()) {
			Specie s = speciesRepository.getSpecieFromString(TfSearch.getText());
			if (validatorDelete(s)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
				Parent root = loader.load();
				ConfirmationController confirmationController = loader.getController();
				confirmationController.getLbText()
						.setText("Tem a certeza que quer apagar a Especie: '" + LbCommonName.getText() + "'?");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_DELETE_SPECIE + LbCommonName.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				if (confirmationController.isConfirmed()) {
					speciesRepository.deleteSpecie(s);
					clearAllFields();
				}
			}
		}
	}
	
	public void search(String name) throws SQLException {
		clearAllFields();
		TfSearch.setText(name);
		Specie s = speciesRepository.getSpecieFromString(TfSearch.getText().toUpperCase());
		updateAllInfo(s);
	}
	
	@FXML
	public void btnSearchForName(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Specie s = speciesRepository.getSpecieFromString(TfSearch.getText().toUpperCase());
			updateAllInfo(s);
		}
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
	
	public boolean validatorDelete(Specie specie) throws SQLException {
		boolean validate=false;
		if (birdsRepository.getBirdWhereInt("SpeciesId", specie.getId()) != null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Especie nao pode ser apagada porque tem passaros associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LbCommonName.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Especie tem de ser procurado antes de editar/apagar.");
			validate=false;
		} else if (!TfSearch.getText().equalsIgnoreCase(LbCommonName.getText().toUpperCase())
	            && !TfSearch.getText().equalsIgnoreCase(LbScientificName.getText().toUpperCase())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("A pesquisa deve corresponder ao nome comum ou nome científico.");
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
			LabelAlert.setText("Nome tem de ser preenchido");
			validate=false;
		}else if (speciesRepository.getSpecieFromString(TfSearch.getText().toUpperCase())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao existe");
			validate=false;
		}else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public void updateAllInfo(Specie s) {
		LbTitle.setText(s.getCommonName());
		LbCommonName.setText(s.getCommonName());
		LbScientificName.setText(s.getScientificName());
		LbIncubationDays.setText(s.getIncubationDays().toString());
		LbBandDays.setText(s.getDaysToBand().toString());
		LbOutOfCageDays.setText(s.getOutofCageAfterDays().toString());
		LbMaturityAfterDays.setText(s.getMaturityAfterDays().toString());
		LbBandSize.setText(s.getBandSize().toString());
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Especie XXXX");
		LbCommonName.setText(null);
		LbScientificName.setText(null);
		LbIncubationDays.setText(null);
		LbBandDays.setText(null);
		LbOutOfCageDays.setText(null);
		LbMaturityAfterDays.setText(null);
		LbBandSize.setText(null);
	}
}
