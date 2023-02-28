package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.BreederRepository;
import repository.ClubRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Breeder;
import domains.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class AddBreederViewController implements Initializable {
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelError, labelAlert;
	@FXML
	private TextField TfCC, TfName, TfPhone, TfNif, TfEmail;
	@FXML
	private TextField TfDistrict, TfPostalCode, TfLocale, TfAddress;
	@FXML
	private TextField TfCites, TfStam;
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
			LabelError.setText("");
		}else {
			btnAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Um clube disponivel tem de ser selecionado.");
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
			LabelError.setText("");
		}else {
			btnDeAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Um clube escolhido tem de ser selecionado.");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		labelAlert.setText(null);
		labelAlert.setStyle(null);
		//ALL VALIDATED
		if(validator()) {
			Breeder b = new Breeder();
			
			b.setCC(Integer.parseInt(TfCC.getText()));
			b.setName(TfName.getText());
			b.setNif(Integer.parseInt(TfNif.getText()));
			b.setCellphone(Integer.parseInt(TfPhone.getText()));
			b.setEmail(TfEmail.getText());
			b.setPostalCode(TfPostalCode.getText());
			b.setLocale(TfLocale.getText());
			b.setDistrict(TfDistrict.getText());
			if (!TfCites.getText().isEmpty())
				b.setNrCites(Integer.valueOf(TfCites.getText()));
			else
				b.setNrCites(0);
			b.setType(CbType.getValue());
			b.setClub(assignedClubs);
			b.setStam(TfStam.getText());
			breederRepository.Insert(b);
			
			labelAlert.setStyle(MyValues.SUCCESS_BOX_STYLE);
			labelAlert.setText("Criador"+b.getName()+" inserido com sucesso!");
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
		//VALIDATE CC
		if (TfCC.getText().length() == 0) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Cartao Cidadao tem de ser preenchido.");
			validated = false;
		} else if (!TfCC.getText().matches("^[\\d]{8}$")) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Cartao Cidadao nao esta no formato correto.");
			validated = false;
		} else if (breederRepository.checkIfCCExists(Integer.parseInt(TfCC.getText()))) {
			TfCC.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Cartao Cidadao ja existe no sistema.");
			validated = false;
			
		} else {
			TfCC.setStyle(null);
			LabelError.setText("");
			validated = true;
		}
		
		//VALIDATE NOME
		if (validated) {
			if (TfName.getText().length() == 0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Nome tem de ser preenchido.");
				validated = false;
			} else if (!TfName.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Nome nao esta no formato correto.");
				validated = false;
			} else {
				TfName.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		//VALIDATE TELEFONE
		if (validated) {
			if (TfPhone.getText().length() == 0) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Telefone tem de ser preenchido");
				validated = false;
			} else if (!TfPhone.getText().matches("^[\\d]{9}$")) {
				TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Telefone nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfPhoneExists(Integer.parseInt(TfPhone.getText()))) {
					TfPhone.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Telefone ja existe no sistema.");
					validated = false;
			} else {
				TfPhone.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		//VALIDATE NIF
		if (validated) {
			if (TfNif.getText().length() == 0) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("NIF tem de ser preenchido.");
				validated = false;
			} else if (!TfNif.getText().matches("^[\\d]{9}$")) {
				TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Nif nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfNIFExists(Integer.parseInt(TfNif.getText()))) {
					TfNif.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("NIF ja existe no sistema.");
					validated = false;
			} else {
				TfNif.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		
		//VALIDATE Email
		if (validated) {
			if (TfEmail.getText().length() == 0) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email tem de ser preenchido.");
				validated = false;
			} else if (!TfEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Email nao esta no formato correto.");
				validated = false;
			} else if (breederRepository.checkIfEmailExists(TfEmail.getText())) {
					TfEmail.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Email ja existe no sistema.");
					validated = false;
			} else {
				TfEmail.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		//TODO regex
		//VALIDATE STAM
		if (validated) {
			if (TfStam.getText().length() == 0) {
				TfStam.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("STAM tem de ser preenchido.");
				validated = false;
			} else if (breederRepository.checkIfSTAMExists(TfStam.getText())) {
					TfStam.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("STAM ja existe no sistema.");
					validated = false;
			} else {
				TfStam.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		//TODO regex
		//VALIDATE CITES
		if (validated) {
			if (!TfCites.getText().matches("^[\\d]+$") && !(TfCites.getText().length()==0)) {
				TfCites.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Nr CITES nao esta no formato correto.");
				validated = false;
			} else {
				TfCites.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}

		//VALIDATE PostalCode
		if (validated) {
			if (!TfPostalCode.getText().matches("^\\d{4}(-\\d{3})?$")) {
				TfPostalCode.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Codigo Postal nao esta no formato correto.");
				validated = false;
			} else {
				TfPostalCode.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		//VALIDATE Tipo Criador
		if (validated) {
			if (CbType.getValue()==null) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Tipo criador tem de ser escolhido.");
				validated = false;
			} else {
				CbType.setStyle(null);
				LabelError.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (CbType.getValue()==MyValues.CRIADOR_PROFISSIONAL && assignedClubs.isEmpty()) {
				clubListViewAssigned.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Criador Profissional tem de escolher pelo menos um clube");
				validated = false;
			}else {
				clubListViewAssigned.setStyle(null);
				LabelError.setText("");
				validated=true;
			}
		}
		
		return validated;
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
		TfCites.setText(null);
		CbType.setValue(null);
		TfStam.setText(null);
		assignedClubs = FXCollections.observableArrayList();
		
		LabelError.setText("");
		TfCC.setStyle(null);
		TfName.setStyle(null);
		TfNif.setStyle(null);
		TfPhone.setStyle(null);
		TfEmail.setStyle(null);
		TfPostalCode.setStyle(null);
		TfLocale.setStyle(null);
		TfDistrict.setStyle(null);
		TfCites.setStyle(null);
		CbType.setStyle(null);
		TfStam.setStyle(null);
		clubListViewAssigned.setStyle(null);
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
	
	}

	
}
