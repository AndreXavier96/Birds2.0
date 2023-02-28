package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.FederationRepository;

import java.nio.file.Paths;
import java.sql.SQLException;
import constants.MyValues;
import domains.Federation;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AddFederationViewController {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelError;
	@FXML
	private Label labelAlert;
	@FXML
	private TextField TfAcronym, TfName, TfEmail,TfAddress,TfCountry;
	
	FederationRepository federationRepository = new FederationRepository();
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		labelAlert.setText(null);
		labelAlert.setStyle(null);
		if(validator()) {
			Federation f = new Federation();
			f.setName(TfName.getText());
			f.setAcronym(TfAcronym.getText());
			f.setCountry(TfCountry.getText());
			f.setEmail(TfEmail.getText());
			federationRepository.Insert(f);
			
			labelAlert.setStyle(MyValues.SUCCESS_BOX_STYLE);
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
	
	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		
		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome tem de ser preenchido.");
			validated = false;
		} else if (!TfName.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome nao esta no formato correto.");
			validated = false;
		}else if (federationRepository.checkIfExistsString("Name", TfName.getText())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome ja existe.");
			validated = false;
		}else {
			TfName.setStyle(null);
			LabelError.setText("");
			validated = true;
		}
		
		if (validated) {
			if (TfAcronym.getText().length() == 0) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Sigla tem de ser preenchido");
				validated = false;
			} else if (!TfAcronym.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Sigla nao esta no formato correto.");
				validated = false;
			}else if (federationRepository.checkIfExistsString("Acronym", TfAcronym.getText())) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Sigla ja existe.");
				validated = false;
			}else {
				TfAcronym.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfCountry.getText().length() == 0) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Pais tem de ser preenchido");
				validated = false;
			} else if (!TfCountry.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
				TfCountry.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Pais nao esta no formato correto.");
				validated = false;
			} else {
				TfCountry.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email nao esta no formato correto.");
				validated = false;
			}else if (federationRepository.checkIfExistsString("Email", TfEmail.getText())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email ja existe.");
				validated = false;
			}else {
				TfEmail.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	public void clearAllFields() {
		TfName.setText(null);
		TfAcronym.setText(null);
		TfCountry.setText(null);
		TfEmail.setText(null);
		
		LabelError.setText("");
		TfName.setStyle(null);
		TfAcronym.setStyle(null);
		TfCountry.setStyle(null);
		TfEmail.setStyle(null);
	}

	
}
