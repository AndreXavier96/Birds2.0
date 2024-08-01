package controller.breeder;

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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import constants.Regex;
import controller.StamPromptDialogController;
import domains.Breeder;
import domains.Club;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class AddBreederViewController implements Initializable {
	private Stage stage;
	
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfName, TfPhone, TfEmail, TfDistrict, TfLocale, TfAddress;
	@FXML
	private ListView<Club> clubListViewAvailable,clubListViewAssigned ;
	@FXML
	private Button btnAssign, btnDeAssign,btnAdd,btnEdit,btnClose;

	private BreederRepository breederRepository = new BreederRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new  BreederClubRepository();
	
	private ObservableList<Club> availableClubs;
	private ObservableList<Club> assignedClubs;
	private HashMap<Integer, String> stamMap = new HashMap<>();
	Breeder breeder = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
		if (validator()) {
			LabelAlert.setStyle(null);
			stamMap.clear();
			Breeder b = new Breeder();
			b.setName(replaceSpecialCharacters(TfName.getText().toLowerCase()));		
			if (TfPhone.getText().isBlank())
				b.setCellphone(0);
			else
				b.setCellphone(Integer.parseInt(TfPhone.getText()));
			if (TfEmail.getText().isBlank())
				b.setEmail("");
			else
				b.setEmail(TfEmail.getText());
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
			LabelAlert.setText("Criador " + b.getName() + " inserido com sucesso!");
			clearAllFields();
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws NumberFormatException, SQLException {
		if (validatorEdit(breeder)) {
			stamMap.clear();
			breeder.setName(replaceSpecialCharacters(TfName.getText().toLowerCase()));
			if (TfPhone.getText().isBlank())
				breeder.setCellphone(0);
			else
				breeder.setCellphone(Integer.parseInt(TfPhone.getText()));
			if (TfEmail.getText().isBlank())
				breeder.setEmail("");
			else
				breeder.setEmail(TfEmail.getText());
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

			breeder.setClub(assignedClubs);
			for (Club club : assignedClubs) {
				Federation federation = club.getFederation();
				if (!stamMap.containsKey(federation.getId())) {
					String stam = openStamEditPromptDialogController(federation, breeder.getId());
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
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		// VALIDATE NOME
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
		return validated;
	}
	
	private static String replaceSpecialCharacters(String input) {
        // Replace specific special characters
        String replacedString = input
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ü", "u");
        return replacedString;
    }
	
	public boolean validatorEdit(Breeder b) throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		// VALIDATE NOME
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
		return validated;
	}
		
	public void clearAllErrors() {
		
		TfName.setStyle(null);
		TfPhone.setStyle(null);
		TfEmail.setStyle(null);
		TfLocale.setStyle(null);
		TfDistrict.setStyle(null);
		TfAddress.setStyle(null);
		clubListViewAssigned.setStyle(null);
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText("");
		TfPhone.setText("");
		TfEmail.setText("");
		TfLocale.setText("");
		TfDistrict.setText("");
		TfAddress.setText("");
		Integer assignedClubsSize = assignedClubs.size();
		for (int i =0;i<assignedClubsSize;i++) {
			availableClubs.add(assignedClubs.get(0));
			assignedClubs.remove(0);
		}
		clearAllErrors();
	}

	public void startValuesEdit(Breeder breeder) throws SQLException {
		btnAdd.setVisible(false);
		btnEdit.setVisible(true);
		btnClose.setVisible(true);
		LBTitle.setText("Editar " + breeder.getName());
		TfName.setText(breeder.getName());
		TfPhone.setText(breeder.getCellphone().toString());
		TfEmail.setText(breeder.getEmail());
		TfDistrict.setText(breeder.getDistrict());
		TfLocale.setText(breeder.getLocale());
		TfAddress.setText(breeder.getAddress());
		ObservableList<Integer> clubsId = breederClubRepository.getClubsFromBreederId(breeder.getId());
		ObservableList<Club> clubs = FXCollections.observableArrayList();
		for (Integer i : clubsId)
			clubs.add(clubRepository.getClubByID(i));
		Iterator<Club> iterator = availableClubs.iterator();
		while (iterator.hasNext()) {
			Club ac = iterator.next();
			for (Club c : clubs) {
				if (c.getId() == ac.getId()) {
					iterator.remove();
					assignedClubs.add(ac);
					availableClubs.remove(ac);
					break;
				}
			}
		}
		this.breeder = breeder;
	}

}
