package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.FederationRepository;

import java.nio.file.Paths;
import java.sql.SQLException;

import constants.MyValues;
import constants.Regex;
import domains.Federation;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AddFederationViewController {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label labelAlert,LbTitle;
	@FXML
	private TextField TfAcronym, TfName, TfEmail,TfAddress,TfCountry;
	@FXML
	private Button btnAdd,btnEdit;
	
	FederationRepository federationRepository = new FederationRepository();
	
	Federation federation=null;
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if(validator()) {
			Federation f = new Federation();
			f.setName(TfName.getText());
			f.setAcronym(TfAcronym.getText());
			f.setCountry(TfCountry.getText());
			f.setEmail(TfEmail.getText());
			federationRepository.Insert(f);
			
			labelAlert.setStyle(MyValues.ALERT_SUCESS);
			labelAlert.setText("Federacao "+f.getName()+" inserida com sucesso!");
			clearAllFields();
		}	
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
	
	@FXML
	public void btnEdit(ActionEvent event) throws NumberFormatException, SQLException {
		
		if(validatorEdit(federation)) {
//			Federation f = new Federation();
			federation.setName(TfName.getText());
			federation.setAcronym(TfAcronym.getText());
			federation.setCountry(TfCountry.getText());
			federation.setEmail(TfEmail.getText());
			federationRepository.updateFederation(federation);
			
			labelAlert.setStyle(MyValues.ALERT_SUCESS);
			labelAlert.setText("Federacao "+federation.getName()+" alterada com sucesso!");
			clearAllFields();
			stage = (Stage) btnEdit.getScene().getWindow();
			stage.close();
		}	
	}
	
	
	public boolean validatorEdit(Federation f) throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		labelAlert.setStyle(MyValues.ALERT_ERROR);
		labelAlert.setText("");
		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		} else if (!TfName.getText().matches(Regex.NAME)) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		}else if (federationRepository.checkIfExistsString("Name", TfName.getText(),f.getId())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome ja existe.");
			validated = false;
		}else {
			TfName.setStyle(null);
			labelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (TfAcronym.getText().length() == 0) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla tem de ser preenchido");
				validated = false;
			} else if (!TfAcronym.getText().matches(Regex.ACRONYM)) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla nao esta no formato correto.");
				validated = false;
			}else if (federationRepository.checkIfExistsString("Acronym", TfAcronym.getText(),f.getId())) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla ja existe.");
				validated = false;
			}else {
				TfAcronym.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfCountry.getText().length() == 0) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Pais tem de ser preenchido");
				validated = false;
			} else if (!TfCountry.getText().matches(Regex.NAME)) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Pais nao esta no formato correto.");
				validated = false;
			} else {
				TfCountry.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfEmail.getText().length() > 0) {
				if (!TfEmail.getText().matches(Regex.EMAIL)) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Email nao esta no formato correto.");
					validated = false;
				} else if (federationRepository.checkIfExistsString("Email", TfEmail.getText(),f.getId())) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Email ja existe.");
					validated = false;
				} else {
					TfEmail.setStyle(null);
					labelAlert.setText("");
					validated = true;
				}
			}else {
				TfEmail.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	
	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		labelAlert.setStyle(MyValues.ALERT_ERROR);
		labelAlert.setText("");
		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		} else if (!TfName.getText().matches(Regex.NAME)) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		}else if (federationRepository.checkIfExistsString("Name", TfName.getText())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Nome ja existe.");
			validated = false;
		}else {
			TfName.setStyle(null);
			labelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (TfAcronym.getText().length() == 0) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla tem de ser preenchido");
				validated = false;
			} else if (!TfAcronym.getText().matches(Regex.ACRONYM)) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla nao esta no formato correto.");
				validated = false;
			}else if (federationRepository.checkIfExistsString("Acronym", TfAcronym.getText())) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Sigla ja existe.");
				validated = false;
			}else {
				TfAcronym.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfCountry.getText().length() == 0) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Pais tem de ser preenchido");
				validated = false;
			} else if (!TfCountry.getText().matches(Regex.NAME)) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Pais nao esta no formato correto.");
				validated = false;
			} else {
				TfCountry.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfEmail.getText().length() > 0) {
				if (!TfEmail.getText().matches(Regex.EMAIL)) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Email nao esta no formato correto.");
					validated = false;
				} else if (federationRepository.checkIfExistsString("Email", TfEmail.getText())) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Email ja existe.");
					validated = false;
				} else {
					TfEmail.setStyle(null);
					labelAlert.setText("");
					validated = true;
				}
			}else {
				TfEmail.setStyle(null);
				labelAlert.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	public void clearAllErrors() {
		TfName.setStyle(null);
		TfAcronym.setStyle(null);
		TfCountry.setStyle(null);
		TfEmail.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText(null);
		TfAcronym.setText(null);
		TfCountry.setText(null);
		TfEmail.setText(null);
		clearAllErrors();
	}

	
	public void startValues(Federation federation) {
		LbTitle.setText("Editar Federacao "+federation.getName());
		TfName.setText(federation.getName());
		TfAcronym.setText(federation.getAcronym());
		TfCountry.setText(federation.getCountry());
		TfEmail.setText(federation.getEmail());
		btnAdd.setVisible(false);
		btnEdit.setVisible(true);
		this.federation=federation;
	}
	
}
