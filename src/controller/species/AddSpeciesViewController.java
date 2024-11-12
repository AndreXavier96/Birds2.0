package controller.species;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.SpeciesRepository;

import java.sql.SQLException;

import constants.MyValues;
import constants.Regex;
import domains.Specie;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AddSpeciesViewController {
	
	private Stage stage;
	
	@FXML
	private Label LabelAlert,LbTitle;
	@FXML
	private TextField TfCommonName;
	@FXML
	private TextField TfScientificName;
	@FXML
	private TextField TfIncubationTime;
	@FXML
	private TextField TfTimeToBand;
	@FXML
	private TextField TfTimeOutOfCage;
	@FXML
	private TextField TfMaturityAfterDays,TfBandSize;
	@FXML
	private Button btnAdd,btnEdit,btnClose;
	
	Specie specie=null;
	
	private SpeciesRepository speciesRepository = new SpeciesRepository();

	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
			Specie specie = new Specie();
			specie.setCommonName(TfCommonName.getText().toUpperCase());
			specie.setScientificName(TfScientificName.getText());
			specie.setIncubationDays(Integer.parseInt(TfIncubationTime.getText()));
			specie.setDaysToBand(Integer.parseInt(TfTimeToBand.getText()));
			specie.setOutofCageAfterDays(Integer.parseInt(TfTimeOutOfCage.getText()));
			specie.setMaturityAfterDays(Integer.parseInt(TfMaturityAfterDays.getText()));
			specie.setBandSize(Integer.parseInt(TfBandSize.getText()));
			speciesRepository.Insert(specie);
			
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Especie "+specie.getCommonName()+" inserida com sucesso!");
			clearAllFields();
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws SQLException {
		if (validatorEdit(specie)) {
			specie.setCommonName(TfCommonName.getText().toUpperCase());
			specie.setScientificName(TfScientificName.getText());
			specie.setIncubationDays(Integer.parseInt(TfIncubationTime.getText()));
			specie.setDaysToBand(Integer.parseInt(TfTimeToBand.getText()));
			specie.setOutofCageAfterDays(Integer.parseInt(TfTimeOutOfCage.getText()));
			specie.setMaturityAfterDays(Integer.parseInt(TfMaturityAfterDays.getText()));
			specie.setBandSize(Integer.parseInt(TfBandSize.getText()));
			speciesRepository.updateSpecie(specie);
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText(specie.getCommonName()+" alterado com sucesso!");
			clearAllFields();
			stage = (Stage) btnEdit.getScene().getWindow();
			stage.close();
		}
	}
	
	public boolean validatorEdit(Specie s) throws SQLException {
		boolean validate = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfCommonName.getText().length()==0) {
			TfCommonName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome comum tem de ser preenchido");
			validate=false;
		}else if(!TfCommonName.getText().matches(Regex.NAME)){
			TfCommonName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome comum nao esta no formato correto");
			validate=false;
		}else {
			TfCommonName.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfScientificName.getText().length()==0 || TfScientificName.getText().isBlank()) {
				TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome cientifico tem de ser preenchido");
				validate=false;
			}else if(!TfScientificName.getText().matches(Regex.NAME)){
					TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Nome cientifico nao esta no formato correto");
					validate=false;
			}else if (speciesRepository.checkIfExistsString("ScientificName",TfScientificName.getText(),s.getId())) {
				TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome cientifico ja existe");
				validate=false;
			}else {
				TfScientificName.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfIncubationTime.getText().matches(Regex.INT)) {
				TfIncubationTime.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias de incubacao nao esta no formato correto");
				validate=false;
			}else {
				TfIncubationTime.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfTimeToBand.getText().matches(Regex.INT)) {
				TfTimeToBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para anilhar nao esta no formato correto");
				validate=false;
			}else {
				TfTimeToBand.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfTimeOutOfCage.getText().matches(Regex.INT)) {
				TfTimeOutOfCage.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para sair da gaiola nao esta no formato correto");
				validate=false;
			}else {
				TfTimeOutOfCage.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		if (validate) {
			if (!TfMaturityAfterDays.getText().matches(Regex.INT)) {
				TfMaturityAfterDays.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para maturidade nao esta no formato correto");
				validate=false;
			}else {
				TfMaturityAfterDays.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		if (validate) {
			if (!TfBandSize.getText().matches(Regex.DOUBLES)) {
				TfBandSize.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tamanho de anilha nao esta no formato correto");
				validate=false;
			}else {
				TfBandSize.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfCommonName.getText().length()==0) {
			TfCommonName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome comum tem de ser preenchido");
			validate=false;
		}else if(!TfCommonName.getText().matches(Regex.NAME)){
			TfCommonName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome comum nao esta no formato correto");
			validate=false;
		}else {
			TfCommonName.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfScientificName.getText().length()==0 || TfScientificName.getText().isBlank()) {
				TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome cientifico tem de ser preenchido");
				validate=false;
			}else if(!TfScientificName.getText().matches(Regex.NAME)){
					TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Nome cientifico nao esta no formato correto");
					validate=false;
			}else if (speciesRepository.checkSpecieByScientificName(TfScientificName.getText())) {
				TfScientificName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome cientifico ja existe");
				validate=false;
			}else {
				TfScientificName.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfIncubationTime.getText().matches(Regex.INT)) {
				TfIncubationTime.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias de incubacao nao esta no formato correto");
				validate=false;
			}else {
				TfIncubationTime.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfTimeToBand.getText().matches(Regex.INT)) {
				TfTimeToBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para anilhar nao esta no formato correto");
				validate=false;
			}else {
				TfTimeToBand.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (!TfTimeOutOfCage.getText().matches(Regex.INT)) {
				TfTimeOutOfCage.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para sair da gaiola nao esta no formato correto");
				validate=false;
			}else {
				TfTimeOutOfCage.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		if (validate) {
			if (!TfMaturityAfterDays.getText().matches(Regex.INT)) {
				TfMaturityAfterDays.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Dias para maturidade nao esta no formato correto");
				validate=false;
			}else {
				TfMaturityAfterDays.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		if (validate) {
			if (!TfBandSize.getText().matches(Regex.DOUBLES)) {
				TfBandSize.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tamanho de anilha nao esta no formato correto");
				validate=false;
			}else {
				TfBandSize.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		return validate;
	}
	
	public void clearAllErrors() {
		TfCommonName.setStyle(null);
		TfScientificName.setStyle(null);
		TfIncubationTime.setStyle(null);
		TfTimeToBand.setStyle(null);
		TfTimeOutOfCage.setStyle(null);
		TfMaturityAfterDays.setStyle(null);
		TfBandSize.setStyle(null);
	}
	
	public void clearAllFields() {
		TfCommonName.setText("");
		TfScientificName.setText("");
		TfIncubationTime.setText("");
		TfTimeToBand.setText("");
		TfTimeOutOfCage.setText("");
		TfMaturityAfterDays.setText("");
		TfBandSize.setText("");
		clearAllErrors();
	}
	
	public void startValuesEdit(Specie specie) throws SQLException {
		LbTitle.setText("Editar "+specie.getCommonName());
		TfCommonName.setText(specie.getCommonName());
		TfScientificName.setText(specie.getScientificName());
		TfIncubationTime.setText(specie.getIncubationDays().toString());
		TfTimeToBand.setText(specie.getDaysToBand().toString());
		TfTimeOutOfCage.setText(specie.getOutofCageAfterDays().toString());
		TfMaturityAfterDays.setText(specie.getMaturityAfterDays().toString());
		TfBandSize.setText(specie.getBandSize().toString());
		btnAdd.setVisible(false);
		btnEdit.setVisible(true);
		this.specie=specie;
	}

	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
}
