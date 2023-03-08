package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.SpeciesRepository;

import java.nio.file.Paths;
import java.sql.SQLException;

import constants.MyValues;
import constants.Regex;
import domains.Specie;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class AddSpeciesViewController {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelAlert;
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
	private TextField TfMaturityAfterDays;
	
	
	private SpeciesRepository speciesRepository = new SpeciesRepository();

	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
			Specie specie = new Specie();
			specie.setCommonName(TfCommonName.getText());
			specie.setScientificName(TfScientificName.getText());
			specie.setIncubationDays(Integer.parseInt(TfIncubationTime.getText()));
			specie.setDaysToBand(Integer.parseInt(TfTimeToBand.getText()));
			specie.setOutofCageAfterDays(Integer.parseInt(TfTimeOutOfCage.getText()));
			specie.setMaturityAfterDays(Integer.parseInt(TfMaturityAfterDays.getText()));
			speciesRepository.Insert(specie);
			
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Especie "+specie.getCommonName()+" inserida com sucesso!");
			clearAllFields();
		}
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
		
		return validate;
	}
	
	public void clearAllErrors() {
		TfCommonName.setStyle(null);
		TfScientificName.setStyle(null);
		TfIncubationTime.setStyle(null);
		TfTimeToBand.setStyle(null);
		TfTimeOutOfCage.setStyle(null);
		TfMaturityAfterDays.setStyle(null);
	}
	
	public void clearAllFields() {
		TfCommonName.setText("");
		TfScientificName.setText("");
		TfIncubationTime.setText("");
		TfTimeToBand.setText("");
		TfTimeOutOfCage.setText("");
		TfMaturityAfterDays.setText("");
		clearAllErrors();
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
