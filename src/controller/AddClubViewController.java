package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.ClubRepository;
import repository.FederationRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Club;
import domains.Federation;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

public class AddClubViewController implements Initializable {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelError;
	@FXML
	private Label labelAlert;
	@FXML
	private TextField TfAcronym, TfName,TfLocale, TfEmail,TfAddress,TfContact;
	@FXML
	private ComboBox<Federation> CbFederation;
	
	FederationRepository federationRepository = new FederationRepository();
	ClubRepository clubRepository = new ClubRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbFederation.setItems(federationRepository.getAllFederations());
		CbFederation.setConverter(new StringConverter<Federation>() {
			public String toString(Federation f) {
				return f.getAcronym();
			}
			
			public Federation fromString(String s) {
				return CbFederation.getItems().stream().filter(b -> b.getAcronym().equals(s)).findFirst().orElse(null);
			}
		});
		
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		labelAlert.setText(null);
		labelAlert.setStyle(null);
		if(validator()) {
			Club c = new Club();
			c.setName(TfName.getText());
			c.setFederation(CbFederation.getValue());
			c.setAcronym(TfAcronym.getText());
			c.setLocale(TfLocale.getText());
			c.setAddress(TfAddress.getText());
			c.setEmail(TfEmail.getText());
			c.setPhone(TfContact.getText());
			clubRepository.Insert(c);
			
			labelAlert.setStyle(MyValues.SUCCESS_BOX_STYLE);
			labelAlert.setText("Federacao "+c.getName()+" inserida com sucesso!");
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
		}else if (clubRepository.checkIfExistsString("Name", TfName.getText())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome ja existe.");
			validated = false;
		} else {
			TfName.setStyle(null);
			LabelError.setText("");
			validated = true;
		}
		
		if (validated) {
			if (CbFederation.getValue()==null) {
				CbFederation.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Federacao tem de ser escolhida");
				validated=false;
			}else {
				CbFederation.setStyle(null);
				LabelError.setText("");
				validated=true;
			}
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
			}else if (clubRepository.checkIfExistsString("Acronym", TfAcronym.getText())) {
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
			if (TfLocale.getText().length() == 0) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Localidade tem de ser preenchida");
				validated = false;
			} else if (!TfLocale.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Localidade nao esta no formato correto.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfAddress.getText().length()==0) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Morada tem de ser preenchida");
				validated = false;
			}else if (!TfAddress.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]|[0-9]| )+$")) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddress.setStyle(null);
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
			}else if (clubRepository.checkIfExistsString("Email", TfEmail.getText())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email ja existe.");
				validated = false;
			}else {
				TfEmail.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfContact.getText().length()==0) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Telefone tem de ser preenchido.");
				validated = false;
			}else if (!TfContact.getText().matches("^\\d+$")) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Telefone nao esta no formato correto.");
				validated = false;
			}else {
				TfContact.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	public void clearAllFields() {
		TfName.setText(null);
		TfAcronym.setText(null);
		CbFederation.setValue(null);
		TfLocale.setText(null);
		TfAddress.setText(null);
		TfContact.setText(null);
		TfEmail.setText(null);
		
		LabelError.setText("");
		TfName.setStyle(null);
		TfAcronym.setStyle(null);
		CbFederation.setStyle(null);
		TfLocale.setStyle(null);
		TfAddress.setStyle(null);
		TfContact.setStyle(null);
		TfEmail.setStyle(null);
	}
	
}
