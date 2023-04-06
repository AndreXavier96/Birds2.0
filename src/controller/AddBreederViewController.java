package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederClubRepository;
import repository.BreederRepository;
import repository.ClubRepository;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import constants.Regex;
import domains.Breeder;
import domains.Club;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;

public class AddBreederViewController implements Initializable {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private ComboBox<String> CbType;
	@FXML
	private AnchorPane ApProfissional,ApOtherType;
	@FXML
	private TextField TfNameOther, TfPhoneOther, TfEmailOther, TfDistrictOther,TfLocaleOther,TfAddressOther;
	
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfCC, TfName, TfPhone, TfNif, TfEmail, TfDistrict, TfPostalCode, TfLocale, TfAddress;
	@FXML
	private ListView<Club> clubListViewAvailable,clubListViewAssigned ;
	@FXML
	private Button btnAssign, btnDeAssign,btnAdd,btnEdit,btnBack,btnClose;

	private BreederRepository breederRepository = new BreederRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new  BreederClubRepository();
	
	private ObservableList<Club> availableClubs;
	private ObservableList<Club> assignedClubs;
	private HashMap<Integer, String> stamMap = new HashMap<>();
	private boolean profissional;
	Breeder breeder = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.BREEDERTYPELIST);
		CbType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.equals(MyValues.LOJA) || newValue.equals(MyValues.CRIADOR_AMADOR)) {
					ApOtherType.setVisible(true);
					ApProfissional.setVisible(false);
					profissional = false;
				} else if (newValue.equals(MyValues.CRIADOR_PROFISSIONAL)) {
					ApOtherType.setVisible(false);
					ApProfissional.setVisible(true);
					profissional = true;
					if (availableClubs.isEmpty() && assignedClubs.isEmpty()) {
						LabelAlert.setStyle(MyValues.ALERT_INFO);
						LabelAlert.setText("Para criar um criador necessita de criar um clube antes");
					}
				}
			}
		});
		try {
			availableClubs = FXCollections.observableArrayList(clubRepository.getAllClubs());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assignedClubs = FXCollections.observableArrayList();
		clubListViewAvailable.setItems(availableClubs);
		clubListViewAssigned.setItems(assignedClubs);
	}
	
	@FXML
	public void btnAssign(ActionEvent event) {
		Club selectedClub = clubListViewAvailable.getSelectionModel().getSelectedItem();
		btnDeAssign.setStyle(null);
		if (selectedClub!= null) {
			assignedClubs.add(selectedClub);
			availableClubs.remove(selectedClub);
			btnAssign.setStyle(null);
		}else {
			btnAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Um clube disponivel tem de ser selecionado.");
		}
	}
	
	@FXML
	public void btnDeAssign(ActionEvent event) {
		Club selectedClub = clubListViewAssigned.getSelectionModel().getSelectedItem();
		btnAssign.setStyle(null);
		if (selectedClub!= null) {
			assignedClubs.remove(selectedClub);
			availableClubs.add(selectedClub);
			btnDeAssign.setStyle(null);
		}else {
			btnDeAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Um clube escolhido tem de ser selecionado.");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validatorCbType()) {
			if (profissional && validatorProfissional()) {
				LabelAlert.setStyle(null);
				stamMap.clear();
				Breeder b = new Breeder();
				b.setCC(Integer.parseInt(TfCC.getText()));
				b.setName(TfName.getText());
				b.setNif(Integer.parseInt(TfNif.getText()));
				b.setCellphone(Integer.parseInt(TfPhone.getText()));
				b.setEmail(TfEmail.getText());
				if (TfPostalCode.getText().isBlank())
					b.setPostalCode("");
				else
					b.setPostalCode(TfPostalCode.getText());
				if (TfLocale.getText().isBlank())
					b.setLocale("");
				else
					b.setLocale(TfLocale.getText());
				if (TfAddress.getText().isBlank())
					b.setAddress("");
				else
					b.setAddress(TfAddress.getText());
				if (TfDistrict.getText().isBlank())
					b.setDistrict("");
				else
					b.setDistrict(TfDistrict.getText());
				b.setType(CbType.getValue());
				b.setClub(assignedClubs);
				for (Club club : assignedClubs) {
					Federation federation = club.getFederation();
					if (!stamMap.containsKey(federation.getId())) {
						String stam = openStamPromptDialogController(federation.getName());
						stamMap.put(federation.getId(), stam);
					}
				}
				b.setStam(stamMap);
				breederRepository.Insert(b);
				LabelAlert.setStyle(MyValues.ALERT_SUCESS);
				LabelAlert.setText("Criador" + b.getName() + " inserido com sucesso!");
				clearAllFields();
			} else if (!profissional && validatorOther()) {
				// insert
				Breeder b = new Breeder();
				b.setName(TfNameOther.getText());
				if (TfPhoneOther.getText().isBlank())
					b.setCellphone(null);
				else
					b.setCellphone(Integer.parseInt(TfPhoneOther.getText()));
				if (TfEmailOther.getText().isBlank())
					b.setEmail("");
				else
					b.setEmail(TfEmailOther.getText());
				if (TfDistrictOther.getText().isBlank())
					b.setDistrict("");
				else
					b.setDistrict(TfDistrictOther.getText());
				if (TfLocaleOther.getText().isBlank())
					b.setLocale("");
				else
					b.setLocale(TfLocaleOther.getText());
				if (TfAddressOther.getText().isBlank())
					b.setAddress("");
				else
					b.setAddress(TfAddressOther.getText());
				b.setType(CbType.getValue());
				breederRepository.InsertOther(b);
				LabelAlert.setStyle(MyValues.ALERT_SUCESS);
				LabelAlert.setText("Criador "+b.getType()+" "+b.getName()+" inserido com sucesso!");
				clearAllFields();
			}
		} else {
			ApOtherType.setVisible(false);
			ApProfissional.setVisible(false);
			profissional = false;
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws NumberFormatException, SQLException {
		if (validatorCbType()) {
			if (profissional && validatorProfissionalEdit(breeder)) {
				stamMap.clear();
				breeder.setCC(Integer.parseInt(TfCC.getText()));
				breeder.setName(TfName.getText());
				breeder.setNif(Integer.parseInt(TfNif.getText()));
				breeder.setCellphone(Integer.parseInt(TfPhone.getText()));
				breeder.setEmail(TfEmail.getText());
				if (TfPostalCode.getText().isBlank())
					breeder.setPostalCode("");
				else
					breeder.setPostalCode(TfPostalCode.getText());
				if (TfLocale.getText().isBlank())
					breeder.setLocale("");
				else
					breeder.setLocale(TfLocale.getText());
				if (TfAddress.getText().isBlank())
					breeder.setAddress("");
				else
					breeder.setAddress(TfAddress.getText());
				if (TfDistrict.getText().isBlank())
					breeder.setDistrict("");
				else
					breeder.setDistrict(TfDistrict.getText());
				breeder.setType(CbType.getValue());
				breeder.setClub(assignedClubs);
				for (Club club : assignedClubs) {
					Federation federation = club.getFederation();
					if (!stamMap.containsKey(federation.getId())) {
						String stam = openStamEditPromptDialogController(federation,breeder.getId());
						stamMap.put(federation.getId(), stam);
					}
				}
				breeder.setStam(stamMap);
				breederRepository.updateBreeder(breeder);
				LabelAlert.setStyle(MyValues.ALERT_SUCESS);
				LabelAlert.setText(breeder.getName() + " alterado com sucesso!");
				clearAllFields();
				stage = (Stage) btnEdit.getScene().getWindow();
				stage.close();
			}else if(!profissional && validatorOtherEdit(breeder)) {
				breeder.setName(TfNameOther.getText());
				if (TfPhoneOther.getText().isBlank())
					breeder.setCellphone(null);
				else
					breeder.setCellphone(Integer.parseInt(TfPhoneOther.getText()));
				if (TfEmailOther.getText().isBlank())
					breeder.setEmail("");
				else
					breeder.setEmail(TfEmailOther.getText());
				if (TfDistrictOther.getText().isBlank())
					breeder.setDistrict("");
				else
					breeder.setDistrict(TfDistrictOther.getText());
				if (TfLocaleOther.getText().isBlank())
					breeder.setLocale("");
				else
					breeder.setLocale(TfLocaleOther.getText());
				if (TfAddressOther.getText().isBlank())
					breeder.setAddress("");
				else
					breeder.setAddress(TfAddressOther.getText());
				breeder.setType(CbType.getValue());
				breederRepository.updateBreederOther(breeder);
				LabelAlert.setStyle(MyValues.ALERT_SUCESS);
				LabelAlert.setText(breeder.getName() + " alterado com sucesso!");
				clearAllFields();
				stage = (Stage) btnEdit.getScene().getWindow();
				stage.close();
			}
		}
	}
	
	private String openStamEditPromptDialogController(Federation federation,int breederId) {
		String promptResult = null;
		do {
			try {
				LabelAlert.setStyle(null);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stamPromptDialog.fxml"));
				Parent root = loader.load();
				StamPromptDialogController stamPromptDialogController = loader.getController();
				stamPromptDialogController.startValuesEdit(federation,stamMap,breederId);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_BIRD_APP);
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				promptResult = stamPromptDialogController.getPromptResult();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (promptResult == null);
		return promptResult;
	}
	
	private String openStamPromptDialogController(String federationName) {
		String promptResult = null;
		do {
			try {
				LabelAlert.setStyle(null);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stamPromptDialog.fxml"));
				Parent root = loader.load();
				StamPromptDialogController stamPromptDialogController = loader.getController();
				stamPromptDialogController.startValues(federationName,stamMap);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_BIRD_APP);
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				promptResult = stamPromptDialogController.getPromptResult();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (promptResult == null);
		return promptResult;
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
	
	public boolean validatorCbType() {
		boolean validated=false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		// VALIDATE Tipo Criador
		if (CbType.getValue() == null) {
			CbType.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Tipo criador tem de ser escolhido.");
			validated = false;
		} else {
			CbType.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		return validated;
	}
	
	public boolean validatorOther() throws NumberFormatException, SQLException {
		boolean validated=false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		if (TfNameOther.getText().length() == 0) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		}else if (TfNameOther.getText().isBlank()) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		}else if (!TfNameOther.getText().matches(Regex.NAME)) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		} else {
			TfNameOther.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		// VALIDATE TELEFONE
		if (validated && TfPhoneOther.getText().length()>0)
			if (!TfPhoneOther.getText().matches(Regex.PHONE)) {
				TfPhoneOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfPhoneExists(Integer.parseInt(TfPhoneOther.getText()))) {
				TfPhoneOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone ja existe no sistema.");
				validated = false;
			} else {
				TfPhoneOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		
		// VALIDATE Email
		if (validated && TfEmailOther.getText().length() > 0)
			if (!TfEmailOther.getText().matches(Regex.EMAIL)) {
				TfEmailOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfEmailExists(TfEmailOther.getText())) {
				TfEmailOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe no sistema.");
				validated = false;
			} else {
				TfEmailOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Distrito
		if (validated && TfDistrictOther.getText().length() > 0)
			if (!TfDistrictOther.getText().matches(Regex.NAME)) {
				TfDistrictOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfDistrictOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Localidade
		if (validated && TfLocaleOther.getText().length() > 0)
			if (!TfLocaleOther.getText().matches(Regex.NAME)) {
				TfLocaleOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfLocaleOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Morada
		if (validated && TfAddressOther.getText().length() > 0)
			if (!TfAddressOther.getText().matches(Regex.ALL_TEXT)) {
				TfAddressOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddressOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		return validated;
	}
	
	public boolean validatorOtherEdit(Breeder b) throws NumberFormatException, SQLException {
		boolean validated=false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		if (TfNameOther.getText().length() == 0) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		}else if (TfNameOther.getText().isBlank()) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		}else if (breederRepository.checkIfExistsString("Name",TfNameOther.getText(),b.getId())) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome ja existe no sistema");
			validated = false;
		}else if (!TfNameOther.getText().matches(Regex.NAME)) {
			TfNameOther.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao esta no formato correto.");
			validated = false;
		} else {
			TfNameOther.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		// VALIDATE TELEFONE
		if (validated && TfPhoneOther.getText().length()>0)
			if (!TfPhoneOther.getText().matches(Regex.PHONE)) {
				TfPhoneOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfExistsString("Cellphone",TfPhoneOther.getText(),b.getId())) {
				TfPhoneOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone ja existe no sistema.");
				validated = false;
			} else {
				TfPhoneOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		
		// VALIDATE Email
		if (validated && TfEmailOther.getText().length() > 0)
			if (!TfEmailOther.getText().matches(Regex.EMAIL)) {
				TfEmailOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfExistsString("Email",TfEmailOther.getText(),b.getId())) {
				TfEmailOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe no sistema.");
				validated = false;
			} else {
				TfEmailOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Distrito
		if (validated && TfDistrictOther.getText().length() > 0)
			if (!TfDistrictOther.getText().matches(Regex.NAME)) {
				TfDistrictOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfDistrictOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Localidade
		if (validated && TfLocaleOther.getText().length() > 0)
			if (!TfLocaleOther.getText().matches(Regex.NAME)) {
				TfLocaleOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfLocaleOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Morada
		if (validated && TfAddressOther.getText().length() > 0)
			if (!TfAddressOther.getText().matches(Regex.ALL_TEXT)) {
				TfAddressOther.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddressOther.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		return validated;
	}

	public boolean validatorProfissional() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		// VALIDATE CC
		if (TfCC.getText().length() == 0) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao tem de ser preenchido.");
			validated = false;
		} else if (!TfCC.getText().matches(Regex.CC)) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao nao esta no formato correto.");
			validated = false;
		} else if (breederRepository.checkIfCCExists(Integer.parseInt(TfCC.getText()))) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao ja existe no sistema.");
			validated = false;

		} else {
			TfCC.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		// VALIDATE NIF
		if (validated)
			if (TfNif.getText().length() == 0) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("NIF tem de ser preenchido.");
				validated = false;
			} else if (!TfNif.getText().matches(Regex.PHONE)) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nif nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfNIFExists(Integer.parseInt(TfNif.getText()))) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("NIF ja existe no sistema.");
				validated = false;
			} else {
				TfNif.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE NOME
		if (validated)
			if (TfName.getText().length() == 0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome tem de ser preenchido.");
				validated = false;
			} else if (!TfName.getText().matches(Regex.NAME)) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome nao esta no formato correto.");
				validated = false;
			} else {
				TfName.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE TELEFONE
		if (validated)
			if (TfPhone.getText().length() == 0) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone tem de ser preenchido");
				validated = false;
			} else if (!TfPhone.getText().matches(Regex.PHONE)) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfPhoneExists(Integer.parseInt(TfPhone.getText()))) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone ja existe no sistema.");
				validated = false;
			} else {
				TfPhone.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Email
		if (validated)
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches(Regex.EMAIL)) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfEmailExists(TfEmail.getText())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe no sistema.");
				validated = false;
			} else {
				TfEmail.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE PostalCode
		if (validated && TfPostalCode.getText().length() > 0)
			if (!TfPostalCode.getText().matches(Regex.POSTALCODE)) {
				TfPostalCode.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Codigo Postal nao esta no formato correto.");
				validated = false;
			} else {
				TfPostalCode.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Distrito
		if (validated && TfDistrict.getText().length() > 0)

			if (!TfDistrict.getText().matches(Regex.NAME)) {
				TfDistrict.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfDistrict.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Localidade
		if (validated && TfLocale.getText().length() > 0)
			if (!TfLocale.getText().matches(Regex.NAME)) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Morada
		if (validated && TfAddress.getText().length() > 0)
			if (!TfAddress.getText().matches(Regex.ALL_TEXT)) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddress.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE CLUBS
		if (validated)
			if (CbType.getValue() == MyValues.CRIADOR_PROFISSIONAL && assignedClubs.isEmpty()) {
				clubListViewAssigned.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Criador Profissional tem de escolher pelo menos um clube");
				validated = false;
			} else {
				clubListViewAssigned.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		return validated;
	}
	
	public boolean validatorProfissionalEdit(Breeder b) throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		// VALIDATE CC
		if (TfCC.getText().length() == 0) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao tem de ser preenchido.");
			validated = false;
		} else if (!TfCC.getText().matches(Regex.CC)) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao nao esta no formato correto.");
			validated = false;
		} else if (breederRepository.checkIfExistsString("CC",TfCC.getText(),b.getId())) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Cartao Cidadao ja existe no sistema.");
			validated = false;
		} else {
			TfCC.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		// VALIDATE NIF
		if (validated)
			if (TfNif.getText().length() == 0) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("NIF tem de ser preenchido.");
				validated = false;
			} else if (!TfNif.getText().matches(Regex.PHONE)) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nif nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfExistsString("Nif",TfNif.getText(),b.getId())) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("NIF ja existe no sistema.");
				validated = false;
			} else {
				TfNif.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE NOME
		if (validated)
			if (TfName.getText().length() == 0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome tem de ser preenchido.");
				validated = false;
			} else if (!TfName.getText().matches(Regex.NAME)) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome nao esta no formato correto.");
				validated = false;
			} else {
				TfName.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE TELEFONE
		if (validated)
			if (TfPhone.getText().length() == 0) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone tem de ser preenchido");
				validated = false;
			} else if (!TfPhone.getText().matches(Regex.PHONE)) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfExistsString("Cellphone",TfPhone.getText(),b.getId())) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone ja existe no sistema.");
				validated = false;
			} else {
				TfPhone.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Email
		if (validated)
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches(Regex.EMAIL)) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfExistsString("Email",TfEmail.getText(),b.getId())) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Email ja existe no sistema.");
				validated = false;
			} else {
				TfEmail.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE PostalCode
		if (validated && TfPostalCode.getText().length() > 0)
			if (!TfPostalCode.getText().matches(Regex.POSTALCODE)) {
				TfPostalCode.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Codigo Postal nao esta no formato correto.");
				validated = false;
			} else {
				TfPostalCode.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Distrito
		if (validated && TfDistrict.getText().length() > 0)

			if (!TfDistrict.getText().matches(Regex.NAME)) {
				TfDistrict.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfDistrict.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Localidade
		if (validated && TfLocale.getText().length() > 0)
			if (!TfLocale.getText().matches(Regex.NAME)) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Distrito nao esta no formato correto.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE Morada
		if (validated && TfAddress.getText().length() > 0)
			if (!TfAddress.getText().matches(Regex.ALL_TEXT)) {
				TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Morada nao esta no formato correto.");
				validated = false;
			} else {
				TfAddress.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		// VALIDATE CLUBS2
		if (validated)
			if (CbType.getValue() == MyValues.CRIADOR_PROFISSIONAL && assignedClubs.isEmpty()) {
				clubListViewAssigned.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Criador Profissional tem de escolher pelo menos um clube");
				validated = false;
			} else {
				clubListViewAssigned.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}

		return validated;
	}
		
	public void clearAllErrors() {
		TfCC.setStyle(null);
		TfName.setStyle(null);
		TfNif.setStyle(null);
		TfPhone.setStyle(null);
		TfEmail.setStyle(null);
		TfPostalCode.setStyle(null);
		TfLocale.setStyle(null);
		TfDistrict.setStyle(null);
		TfAddress.setStyle(null);
		CbType.setStyle(null);
		clubListViewAssigned.setStyle(null);
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
		
		TfNameOther.setStyle(null);
		TfPhoneOther.setStyle(null);
		TfEmailOther.setStyle(null);
		TfDistrictOther.setStyle(null);
		TfLocaleOther.setStyle(null);
		TfAddressOther.setStyle(null);
	}
	
	public void clearAllFields() {
		TfCC.setText("");
		TfName.setText("");
		TfNif.setText("");
		TfPhone.setText("");
		TfEmail.setText("");
		TfPostalCode.setText("");
		TfLocale.setText("");
		TfDistrict.setText("");
		TfAddress.setText("");
		CbType.setValue(null);
		Integer assignedClubsSize = assignedClubs.size();
		for (int i =0;i<assignedClubsSize;i++) {
			availableClubs.add(assignedClubs.get(0));
			assignedClubs.remove(0);
		}
		
		TfNameOther.setText("");
		TfPhoneOther.setText("");
		TfEmailOther.setText("");
		TfDistrictOther.setText("");
		TfLocaleOther.setText("");
		TfAddressOther.setText("");
		
		ApOtherType.setVisible(false);
		ApProfissional.setVisible(false);
		clearAllErrors();
	}

	public void startValuesEdit(Breeder breeder) throws SQLException {
		btnAdd.setVisible(false);
		btnEdit.setVisible(true);
		btnBack.setVisible(false);
		btnClose.setVisible(true);
		LBTitle.setText("Editar " + breeder.getName());
		CbType.setValue(breeder.getType());
		CbType.setDisable(true);
		if (CbType.getValue().equals(MyValues.CRIADOR_PROFISSIONAL)) {
			profissional = true;
			ApProfissional.setVisible(true);
			TfCC.setText(breeder.getCC().toString());
			TfNif.setText(breeder.getNif().toString());
			TfName.setText(breeder.getName());
			TfPhone.setText(breeder.getCellphone().toString());
			TfEmail.setText(breeder.getEmail());
			TfPostalCode.setText(breeder.getPostalCode());
			TfDistrict.setText(breeder.getDistrict());
			TfLocale.setText(breeder.getLocale());
			TfAddress.setText(breeder.getAddress());
			ObservableList<Integer> clubsId = breederClubRepository.getClubsFromBreederId(breeder.getId());
			ObservableList<Club> clubs = FXCollections.observableArrayList();
			for (Integer i : clubsId)
				clubs.add(clubRepository.getClubByID(i));
			Iterator<Club> iterator=availableClubs.iterator();
			while (iterator.hasNext()) {
				Club ac = iterator.next();
				for (Club c : clubs) {
					if (c.getId()==ac.getId()) {
						iterator.remove();
						assignedClubs.add(ac);
						availableClubs.remove(ac);
						break;
					}
				}
			}
		} else {
			profissional = false;
			ApOtherType.setVisible(true);
			TfNameOther.setText(breeder.getName());
			TfPhoneOther.setText(breeder.getCellphone().toString());
			TfEmailOther.setText(breeder.getEmail());
			TfDistrictOther.setText(breeder.getDistrict());
			TfLocaleOther.setText(breeder.getLocale());
			TfAddressOther.setText(breeder.getAddress());
		}
		this.breeder=breeder;
	}

}
