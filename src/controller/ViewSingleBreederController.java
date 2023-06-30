package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import domains.Breeder;
import domains.Club;
import domains.ClubFederation;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederClubRepository;
import repository.BreederFederationRepository;
import repository.BreederRepository;
import repository.ClubRepository;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class ViewSingleBreederController implements Initializable{
	
	@FXML
	private Label LbTitle;
	@FXML
	private Label LabelAlert;
	@FXML
	private ComboBox<String> CbType;
	@FXML
	private AnchorPane ApSearchStam,ApSearchName,ApProfissional,ApOtherType;
	@FXML
	private TextField TfSearchName, TfSearchStam;
	@FXML
	private Label LbCC,LbNif, LbName, LbPhone, LbEmail, LbPostal, LbDistrict, LbLocale, LbAddress;
	@FXML
	private TableView<ClubFederation> TableID;
	@FXML
	private TableColumn<Breeder,String> TcFederation, TcClub,TcStam;
	@FXML
	private Label LbName2, LbPhone2, LbEmail2, LbLocale2, LbAddress2;

	private BreederRepository breederRepository = new BreederRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private ClubRepository clubRepository = new  ClubRepository();
	
	Boolean profissional;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.BREEDERTYPELIST);
		CbType.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.equals(MyValues.LOJA) || newValue.equals(MyValues.CRIADOR_AMADOR)) {
					TfSearchName.setText(null);
					TfSearchStam.setText(null);
					LabelAlert.setStyle(null);
					clearAllFields();
					ApOtherType.setVisible(true);
					ApProfissional.setVisible(false);
					ApSearchStam.setVisible(false);
					ApSearchName.setVisible(true);
					profissional = false;
				} else if (newValue.equals(MyValues.CRIADOR_PROFISSIONAL)) {
					TfSearchName.setText(null);
					TfSearchStam.setText(null);
					LabelAlert.setStyle(null);
					clearAllFields();
					ApOtherType.setVisible(false);
					ApProfissional.setVisible(true);
					ApSearchStam.setVisible(true);
					ApSearchName.setVisible(false);
					profissional = true;
				}
			}
		});
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
		if (validatorType())
			if (CbType.getValue().equals(MyValues.CRIADOR_PROFISSIONAL) && validatorStam() && validatorStamSearch()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/AddBreederView.fxml"));
				Parent root = loader.load();
				Breeder b = breederRepository
						.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
				AddBreederViewController addBreederViewController  = loader.getController();
				addBreederViewController.startValuesEdit(b);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_EDIT_BREEDER + LbName.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				Breeder updateBreeder = breederRepository.getBreederbyId(b.getId());
				updateBreeder.setStam(breederFederationRepository.getAllByBreederId(updateBreeder.getId()));
				searchStam(updateBreeder.getStam().values().toArray()[0].toString());
			}else if ((CbType.getValue().equals(MyValues.CRIADOR_AMADOR) || CbType.getValue().equals(MyValues.LOJA))
					&& validatorName() && validatorNameSearch()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/AddBreederView.fxml"));
				Parent root = loader.load();
				Breeder b = breederRepository.getBreederbyString("Name", TfSearchName.getText());
				AddBreederViewController addBreederViewController  = loader.getController();
				addBreederViewController.startValuesEdit(b);
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_EDIT_BREEDER + LbName.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				Breeder updateBreeder = breederRepository.getBreederbyId(b.getId());
				searchName(updateBreeder.getName());
			}
	}
	
	public void searchStam(String stam) throws SQLException {
		clearAllFields();
		TfSearchStam.setText(stam);
		Breeder breeder = breederRepository.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
		ObservableList<Integer> clubsId= breederClubRepository.getClubsFromBreederId(breeder.getId());
		ObservableList<Club> clubs = FXCollections.observableArrayList();
		for (Integer i : clubsId) 
			clubs.add(clubRepository.getClubByID(i));
		breeder.setClub(clubs);
		breeder.setStam(breederFederationRepository.getAllByBreederId(breeder.getId()));
		updateInfoProfessional(breeder);
	}
	
	public void searchName(String name) throws SQLException {
		clearAllFields();
		TfSearchName.setText(name);
		Breeder breeder = breederRepository.getBreederbyString("Name", TfSearchName.getText());
		updateInfoOther(breeder);
	}

	@FXML
	public void btnDelete(ActionEvent event) throws SQLException, IOException {
		if (validatorType())
			if (CbType.getValue().equals(MyValues.CRIADOR_PROFISSIONAL) && validatorStam() && validatorStamSearch()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
				Parent root = loader.load();
				ConfirmationController confirmationController = loader.getController();
				confirmationController.getLbText()
						.setText("Tem a certeza que quer apagar o Criador: '" + LbName.getText() + "'?");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_DELETE_BREEDER + LbName.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				if (confirmationController.isConfirmed()) {
					Breeder b = breederRepository
							.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
					breederRepository.deleteBreeder(b);
					clearAllFields();
				}
			} else if ((CbType.getValue().equals(MyValues.CRIADOR_AMADOR) || CbType.getValue().equals(MyValues.LOJA))
					&& validatorName() && validatorNameSearch()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
				Parent root = loader.load();
				ConfirmationController confirmationController = loader.getController();
				if (CbType.getValue().equals(MyValues.LOJA))
					confirmationController.getLbText()
							.setText("Tem a certeza que quer apagar a Loja: '" + LbName2.getText() + "'?");
				else
					confirmationController.getLbText()
							.setText("Tem a certeza que quer apagar o Criador: '" + LbName2.getText() + "'?");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_DELETE_BREEDER + LbName2.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				if (confirmationController.isConfirmed()) {
					Breeder b = breederRepository.getBreederbyString("Name", TfSearchName.getText());
					breederRepository.deleteBreeder(b);
					clearAllFields();
				}
			}
	}

	@FXML
	public void btnSearchForStam(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validatorStam()) {
			LabelAlert.setStyle(null);
			Breeder breeder = breederRepository.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
			ObservableList<Integer> clubsId= breederClubRepository.getClubsFromBreederId(breeder.getId());
			ObservableList<Club> clubs = FXCollections.observableArrayList();
			for (Integer i : clubsId) 
				clubs.add(clubRepository.getClubByID(i));
			breeder.setClub(clubs);
			breeder.setStam(breederFederationRepository.getAllByBreederId(breeder.getId()));
			updateInfoProfessional(breeder);
		}
	}

	@FXML
	public void btnSearchForName(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validatorName()) {
			LabelAlert.setStyle(null);
			Breeder breeder = breederRepository.getBreederbyString("Name", TfSearchName.getText());
			updateInfoOther(breeder);
		}
	}

	public boolean validatorStamSearch(){
		boolean validate = false;
		if (LbCC.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchStam.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Criador tem de ser procurado antes de editar/apagar.");
			validate=false;
		}else {
			TfSearchStam.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorNameSearch(){
		boolean validate = false;
		if (LbName2.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Criador tem de ser procurado antes de editar/apagar.");
			validate=false;
		}else {
			TfSearchName.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorStam() throws SQLException {
		boolean validate = false;
		if (TfSearchStam.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchStam.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("STAM tem de ser preenchido");
			validate=false;
		}else if (!breederFederationRepository.stamExists(TfSearchStam.getText())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchStam.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("STAM nao existe");
			validate=false;
		}else {
			TfSearchStam.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorType() {
		boolean validate = false;
		if (CbType.getValue() == null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			CbType.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Tipo tem de ser escolhido");
			validate=false;
		}else {
			CbType.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorName() throws SQLException {
		boolean validate = false;
		if (TfSearchName.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido");
			validate=false;
		}else if (breederRepository.getBreederbyString("Name", TfSearchName.getText())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao existe");
			validate=false;
		}else if (MyValues.CRIADOR_PROFISSIONAL.equals(breederRepository.getBreederbyString("Name", TfSearchName.getText()).getType())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			if (CbType.getValue().equals(MyValues.CRIADOR_AMADOR)) 
				LabelAlert.setText("Criador amador nao existe!");
			else
				LabelAlert.setText("Loja nao existe!");
			validate=false;
		}else {
			TfSearchStam.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public void updateInfoProfessional(Breeder b) {
		TfSearchStam.setStyle(null);
		LbTitle.setText(b.getName());
		LbCC.setText(b.getCC().toString());
		LbNif.setText(b.getNif().toString());
		LbName.setText(b.getName());
		LbPhone.setText(b.getCellphone().toString());
		LbEmail.setText(b.getEmail());
		LbPostal.setText(b.getPostalCode());
		LbDistrict.setText(b.getDistrict());
		LbLocale.setText(b.getLocale());
		LbAddress.setText(b.getAddress());
		ObservableList<ClubFederation> clubFederations = getClubFederationsForBreeder(b);
		TcClub.setCellValueFactory(new PropertyValueFactory<>("clubAcronym"));
		TcFederation.setCellValueFactory(new PropertyValueFactory<>("federationName"));
		TcStam.setCellValueFactory(new PropertyValueFactory<>("breederStam"));
		TableID.setItems(clubFederations);
	}
	
    private ObservableList<ClubFederation> getClubFederationsForBreeder(Breeder breeder) {
        List<Club> clubs = breeder.getClub();
        ObservableList<ClubFederation> clubFederations = FXCollections.observableArrayList();
        for (Club club : clubs) {
            Federation federation = club.getFederation();
            String federationName = federation.getName();
            String breederStam = breeder.getStam().get(federation.getId());
            ClubFederation clubFederation = new ClubFederation(club.getAcronym(), federationName, breederStam);
            clubFederations.add(clubFederation);
        }
        return clubFederations;
    }
	
	public void updateInfoOther(Breeder b) {
		TfSearchName.setStyle(null);
		LbTitle.setText(b.getName());
		LbName2.setText(b.getName());
		LbPhone2.setText(b.getCellphone().toString());
		LbEmail2.setText(b.getEmail());
		LbLocale2.setText(b.getLocale());
		LbAddress2.setText(b.getAddress());
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Clube XXXX");
		LbCC.setText(null);
		LbNif.setText(null);
		LbName.setText(null);
		LbPhone.setText(null);
		LbEmail.setText(null);
		LbPostal.setText(null);
		LbDistrict.setText(null);
		LbLocale.setText(null);
		LbAddress.setText(null);
		LbName2.setText(null);
		LbPhone2.setText(null);
		LbEmail2.setText(null);
		LbLocale2.setText(null);
		LbAddress2.setText(null);
		TableID.setItems(null);
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
	
	public void startValues(Breeder b) throws SQLException {
		if (b.getType().equals(MyValues.CRIADOR_PROFISSIONAL)) {
			CbType.setValue(b.getType());
			TfSearchName.setText(null);
			TfSearchStam.setText(null);
			LabelAlert.setStyle(null);
			clearAllFields();
			ApOtherType.setVisible(false);
			ApProfissional.setVisible(true);
			ApSearchStam.setVisible(true);
			ApSearchName.setVisible(false);
			profissional = true;
			searchStam(b.getStam().values().toArray()[0].toString());
		}else {
			CbType.setValue(b.getType());
			TfSearchName.setText(null);
			TfSearchStam.setText(null);
			LabelAlert.setStyle(null);
			clearAllFields();
			ApOtherType.setVisible(true);
			ApProfissional.setVisible(false);
			ApSearchStam.setVisible(false);
			ApSearchName.setVisible(true);
			profissional = false;
			searchName(b.getName());
		}
	}
}
