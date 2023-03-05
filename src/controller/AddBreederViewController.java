package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederRepository;
import repository.ClubRepository;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import constants.MyValues;
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
import javafx.scene.control.ComboBox;

public class AddBreederViewController implements Initializable {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelAlert, labelAlert;
	@FXML
	private TextField TfCC, TfName, TfPhone, TfNif, TfEmail;
	@FXML
	private TextField TfDistrict, TfPostalCode, TfLocale, TfAddress;
	@FXML
	private ComboBox<String> CbType;
	@FXML
	private ListView<Club> clubListViewAvailable,clubListViewAssigned ;
	@FXML
	private Button btnAssign, btnDeAssign;

	private BreederRepository breederRepository = new BreederRepository();
	private ClubRepository clubRepository = new ClubRepository();
	
	private ObservableList<Club> availableClubs;
	private ObservableList<Club> assignedClubs;
	private HashMap<Integer, String> stamMap = new HashMap<>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.BREEDERTYPELIST);
		availableClubs = FXCollections.observableArrayList(clubRepository.getAllClubs());
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
			LabelAlert.setText("");
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
			LabelAlert.setText("");
		}else {
			btnDeAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Um clube escolhido tem de ser selecionado.");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			Breeder b = new Breeder();
			if (TfCC.getText().isBlank())
				b.setCC(null);
			else
				b.setCC(Integer.parseInt(TfCC.getText()));
			b.setName(TfName.getText());
			if (TfNif.getText().isBlank())
				b.setNif(null);
			else
				b.setNif(Integer.parseInt(TfNif.getText()));
			b.setCellphone(Integer.parseInt(TfPhone.getText()));
			if (TfEmail.getText().isBlank())
				b.setEmail("");
			else
				b.setEmail(TfEmail.getText());
			if (TfPostalCode.getText().isBlank())
				b.setPostalCode("");
			else
				b.setPostalCode(TfPostalCode.getText());
			
			if (TfLocale.getText().isBlank())
				b.setLocale("");
			else
				b.setLocale(TfLocale.getText());
			
			if (TfDistrict.getText().isBlank())
				b.setDistrict("");
			else
				b.setDistrict(TfDistrict.getText());
			b.setType(CbType.getValue());
			b.setClub(assignedClubs);
			for (Club club :assignedClubs) {
				Federation federation = club.getFederation();
				if (!stamMap.containsKey(federation.getId())) {
					String stam = openStamPromptDialogController(federation.getName());
					stamMap.put(federation.getId(), stam);
				}
			}
			b.setStam(stamMap);;
			breederRepository.Insert(b);
			labelAlert.setStyle(MyValues.ALERT_SUCESS);
			labelAlert.setText("Criador"+b.getName()+" inserido com sucesso!");
			clearAllFields();
		}	
	}
	
	
	private String openStamPromptDialogController(String federationName) {
		String promptResult = null;
		do {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/stamPromptDialog.fxml"));
				Parent root = loader.load();
				StamPromptDialogController stamPromptDialogController = loader.getController();
				stamPromptDialogController.startValues(federationName);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_BIRD_APP);
				stage.getIcons().add(new Image(MyValues.ICON_PATH));
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
	
	
	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
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
		//VALIDATE CC
		if (validated) {
			if (CbType.getValue() == MyValues.CRIADOR_PROFISSIONAL)
				if (TfCC.getText().length() == 0) {
					TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Cartao Cidadao tem de ser preenchido.");
					validated = false;
				} else if (!TfCC.getText().matches("^[\\d]{8}$")) {
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
			else if(TfCC.getText().length()>0) {
				if (!TfCC.getText().matches("^[\\d]{8}$")) {
					TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Cartao Cidadao nao esta no formato correto.");
					validated = false;
				}else if (breederRepository.checkIfCCExists(Integer.parseInt(TfCC.getText()))) {
					TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Cartao Cidadao ja existe no sistema.");
					validated = false;

				} else {
					TfCC.setStyle(null);
					LabelAlert.setText("");
					validated = true;
				}
			}
		}
		
		// VALIDATE NOME
		if (validated)
			if (TfName.getText().length() == 0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome tem de ser preenchido.");
				validated = false;
			} else if (!TfName.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome nao esta no formato correto.");
				validated = false;
			} else {
				TfName.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		
		//VALIDATE TELEFONE
		if (validated)
			if (TfPhone.getText().length() == 0) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Telefone tem de ser preenchido");
				validated = false;
			} else if (!TfPhone.getText().matches("^[\\d]{9}$")) {
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
		
		// VALIDATE NIF
		if (validated) {
			if (CbType.getValue() == MyValues.CRIADOR_PROFISSIONAL) {
				if (TfNif.getText().length() == 0) {
					TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("NIF tem de ser preenchido.");
					validated = false;
				} else if (!TfNif.getText().matches("^[\\d]{9}$")) {
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
			} else if (TfNif.getText().length() > 0) {
				if (!TfNif.getText().matches("^[\\d]{9}$")) {
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
			}
		}

		// VALIDATE Email
		if (validated) {
			if (CbType.getValue() == MyValues.CRIADOR_PROFISSIONAL) {
				if (TfEmail.getText().length() == 0) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Email tem de ser preenchido.");
					validated = false;
				} else if (!TfEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
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
			} else if(TfEmail.getText().length()>0){
				if (!TfEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
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
			}
		}

		// VALIDATE Localidade
		if (validated) {
			if (TfLocale.getText().length() > 0) {
				if (!TfLocale.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
					TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Distrito nao esta no formato correto.");
					validated = false;
				} else {
					TfLocale.setStyle(null);
					LabelAlert.setText("");
					validated = true;
				}
			}
		}

		// VALIDATE Distrito
		if (validated) {
			if (TfDistrict.getText().length() > 0) {
				if (!TfDistrict.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
					TfDistrict.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Distrito nao esta no formato correto.");
					validated = false;
				} else {
					TfDistrict.setStyle(null);
					LabelAlert.setText("");
					validated = true;
				}
			}
		}

		// VALIDATE PostalCode
		if (validated) {
			if (TfPostalCode.getText().length() > 0) {
				if (!TfPostalCode.getText().matches("^\\d{4}(-\\d{3})?$")) {
					TfPostalCode.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Codigo Postal nao esta no formato correto.");
					validated = false;
				} else {
					TfPostalCode.setStyle(null);
					LabelAlert.setText("");
					validated = true;
				}
			}
		}

		// VALIDATE Morada
		if (validated) {
			if (TfAddress.getText().length() > 0) {
				if (!TfAddress.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
					TfAddress.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Distrito nao esta no formato correto.");
					validated = false;
				} else {
					TfAddress.setStyle(null);
					LabelAlert.setText("");
					validated = true;
				}
			}
		}
		//VALIDATE CLUBS2
		if (validated) {
			if (CbType.getValue()==MyValues.CRIADOR_PROFISSIONAL && assignedClubs.isEmpty()) {
				clubListViewAssigned.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Criador Profissional tem de escolher pelo menos um clube");
				validated = false;
			}else {
				clubListViewAssigned.setStyle(null);
				LabelAlert.setText("");
				validated=true;
			}
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
		CbType.setStyle(null);
		clubListViewAssigned.setStyle(null);
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
	}
	
	public void clearAllFields() {
		TfCC.setText(null);
		TfName.setText(null);
		TfNif.setText(null);
		TfPhone.setText(null);
		TfEmail.setText(null);
		TfPostalCode.setText(null);
		TfLocale.setText(null);
		TfDistrict.setText(null);
		CbType.setValue(null);
		assignedClubs = FXCollections.observableArrayList();
		clearAllErrors();
	}

	
}
