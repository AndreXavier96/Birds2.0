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
import constants.Regex;
import domains.Club;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class AddClubViewController implements Initializable {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelAlert,LbTitle;
	@FXML
	private TextField TfAcronym, TfName,TfLocale, TfEmail,TfAddress,TfContact;
	@FXML
	private ComboBox<Federation> CbFederation;
	@FXML
	private Button btnAdd,btnEdit,btnBack,btnClose;
	
	Club club = null;
	
	FederationRepository federationRepository = new FederationRepository();
	ClubRepository clubRepository = new ClubRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Federation> federations=FXCollections.observableArrayList();
		try {
			federations = federationRepository.getAllFederations();
			CbFederation.setItems(federations);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CbFederation.setConverter(new StringConverter<Federation>() {
			public String toString(Federation f) {
				return f.getAcronym();
			}
			
			public Federation fromString(String s) {
				return CbFederation.getItems().stream().filter(b -> b.getAcronym().equals(s)).findFirst().orElse(null);
			}
		});
		if (federations.isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_INFO);
			LabelAlert.setText("Para criar um clube necessita de criar uma federacao antes");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
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
			
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Federacao "+c.getName()+" inserida com sucesso!");
			clearAllFields();
		}	
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws NumberFormatException, SQLException {
		
		if(validatorEdit(club)) {
			club.setName(TfName.getText());
			club.setFederation(CbFederation.getValue());
			club.setAcronym(TfAcronym.getText());
			club.setLocale(TfLocale.getText());
			club.setAddress(TfAddress.getText());
			club.setEmail(TfEmail.getText());
			club.setPhone(TfContact.getText());
			clubRepository.updateClub(club);
			
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText(club.getName()+" alterado com sucesso!");
			clearAllFields();
			stage = (Stage) btnEdit.getScene().getWindow();
			stage.close();
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
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
	
	public boolean validatorEdit(Club c) throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		} else if (!TfName.getText().matches(Regex.NAME)) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		}else if (clubRepository.checkIfExistsString("Name", TfName.getText(),c.getId())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome ja existe.");
			validated = false;
		}else {
			TfName.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (CbFederation.getValue()==null) {
				CbFederation.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Federacao tem de ser escolhida");
				validated=false;
			}else {
				CbFederation.setStyle(null);
				LabelAlert.setText("");
				validated=true;
			}
		}
		
		if (validated) {
			if (TfAcronym.getText().length() == 0) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla tem de ser preenchido");
				validated = false;
			} else if (!TfAcronym.getText().matches(Regex.ACRONYM)) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla nao esta no formato correto.");
				validated = false;
			}else if (clubRepository.checkIfExistsString("Acronym", TfAcronym.getText(),c.getId())) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla ja existe.");
				validated = false;
			}else {
				TfAcronym.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfLocale.getText().length() == 0) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Localidade tem de ser preenchida");
				validated = false;
			} else if (!TfLocale.getText().matches(Regex.NAME)) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Localidade nao esta no formato correto.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfAddress.getText().length()==0) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada tem de ser preenchida");
				validated = false;
			}else if (!TfAddress.getText().matches(Regex.ALL_TEXT)) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddress.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches(Regex.EMAIL)) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			}else if (clubRepository.checkIfExistsString("Email", TfEmail.getText(),c.getId())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe.");
				validated = false;
			}else {
				TfEmail.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfContact.getText().length()==0) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone tem de ser preenchido.");
				validated = false;
			}else if (!TfContact.getText().matches(Regex.PHONE)) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			}else {
				TfContact.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		} else if (!TfName.getText().matches(Regex.NAME)) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		}else if (clubRepository.checkIfExistsString("Name", TfName.getText())) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome ja existe.");
			validated = false;
		} else {
			TfName.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (CbFederation.getValue()==null) {
				CbFederation.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Federacao tem de ser escolhida");
				validated=false;
			}else {
				CbFederation.setStyle(null);
				LabelAlert.setText("");
				validated=true;
			}
		}
		
		if (validated) {
			if (TfAcronym.getText().length() == 0) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla tem de ser preenchido");
				validated = false;
			} else if (!TfAcronym.getText().matches(Regex.ACRONYM)) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla nao esta no formato correto.");
				validated = false;
			}else if (clubRepository.checkIfExistsString("Acronym", TfAcronym.getText())) {
				TfAcronym.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sigla ja existe.");
				validated = false;
			}else {
				TfAcronym.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfLocale.getText().length() == 0) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Localidade tem de ser preenchida");
				validated = false;
			} else if (!TfLocale.getText().matches(Regex.NAME)) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Localidade nao esta no formato correto.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfAddress.getText().length()==0) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada tem de ser preenchida");
				validated = false;
			}else if (!TfAddress.getText().matches(Regex.ALL_TEXT)) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddress.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches(Regex.EMAIL)) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			}else if (clubRepository.checkIfExistsString("Email", TfEmail.getText())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe.");
				validated = false;
			}else {
				TfEmail.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (TfContact.getText().length()==0) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone tem de ser preenchido.");
				validated = false;
			}else if (!TfContact.getText().matches(Regex.PHONE)) {
				TfContact.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			}else {
				TfContact.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		return validated;
	}
	
	public void clearAllErrors() {
		TfName.setStyle(null);
		TfAcronym.setStyle(null);
		CbFederation.setStyle(null);
		TfLocale.setStyle(null);
		TfAddress.setStyle(null);
		TfContact.setStyle(null);
		TfEmail.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText(null);
		TfAcronym.setText(null);
		CbFederation.setValue(null);
		TfLocale.setText(null);
		TfAddress.setText(null);
		TfContact.setText(null);
		TfEmail.setText(null);
		clearAllErrors();
	}
	
	public void startValuesEdit(Club club) throws SQLException {
		LbTitle.setText("Editar "+club.getName());
		TfName.setText(club.getName());
		TfAcronym.setText(club.getAcronym());
		TfLocale.setText(club.getLocale());
		TfEmail.setText(club.getEmail());
		TfAddress.setText(club.getAddress());
		TfContact.setText(club.getPhone());
		CbFederation.setItems(federationRepository.getAllFederations());
		CbFederation.setValue(club.getFederation());
		btnAdd.setVisible(false);
		btnEdit.setVisible(true);
		btnBack.setVisible(false);
		btnClose.setVisible(true);
		this.club=club;
	}
	
}
