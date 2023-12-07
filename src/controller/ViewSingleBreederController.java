package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederClubRepository;
import repository.BreederFederationRepository;
import repository.BreederRepository;
import repository.ClubRepository;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class ViewSingleBreederController{
	
	@FXML
	private Label LbTitle;
	@FXML
	private Label LabelAlert;
	@FXML
	private AnchorPane ApSearchStam,ApSearchName;
	@FXML
	private TextField TfSearchName, TfSearchStam;
	@FXML
	private Label LbName, LbPhone, LbEmail, LbDistrict, LbLocale, LbAddress;
	@FXML
	private TableView<ClubFederation> TableID;
	@FXML
	private TableColumn<Breeder,String> TcFederation, TcClub,TcStam;

	private BreederRepository breederRepository = new BreederRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private ClubRepository clubRepository = new  ClubRepository();
	
	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
			if (validatorSearch()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/AddBreederView.fxml"));
				Parent root = loader.load();
				Breeder b =  new Breeder();
				if (!TfSearchStam.getText().isEmpty() && TfSearchName.getText().isEmpty())
					b = breederRepository.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
				else
					b = breederRepository.getBreederbyString("Name", TfSearchName.getText());
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
				searchName(updateBreeder.getName());
			}
	}
	
	public void startValues(Breeder b) throws SQLException {
			TfSearchName.setText(b.getName());
			TfSearchStam.setText(null);
			LabelAlert.setStyle(null);
			clearAllFields();
			searchName(b.getName());
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
		updateInfo(breeder);
	}
	
	public void searchName(String name) throws SQLException {
		clearAllFields();
		TfSearchName.setText(name);
		Breeder breeder = breederRepository.getBreederbyString("Name", TfSearchName.getText());
		updateInfo(breeder);
	}

	@FXML
	public void btnDelete(ActionEvent event) throws SQLException, IOException {
		if (validatorSearch()) {
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
				Breeder b = new Breeder();
				if (!TfSearchStam.getText().isEmpty() && TfSearchName.getText().isEmpty())
					b = breederRepository.getBreederbyId(breederFederationRepository.getBreederIdByStam(TfSearchStam.getText()));
				else
					b = breederRepository.getBreederbyString("Name", TfSearchName.getText());
				breederRepository.deleteBreeder(b);
				clearAllFields();
			}
		}
	}

	@FXML
	public void btnSearchForStam(ActionEvent event) throws SQLException {
		TfSearchName.setText("");
		TfSearchName.setStyle(null);
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
			updateInfo(breeder);
		}
	}

	@FXML
	public void btnSearchForName(ActionEvent event) throws SQLException {
		TfSearchStam.setText("");
		TfSearchStam.setStyle(null);
		clearAllFields();
		if (validatorName()) {
			LabelAlert.setStyle(null);
			Breeder breeder = breederRepository.getBreederbyString("Name", TfSearchName.getText());
			updateInfo(breeder);
		}
	}

	public boolean validatorSearch() throws SQLException{
		boolean validate = false;
		if (LbName.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Criador tem de ser procurado antes de editar/apagar.");
			validate=false;
		}else {
			TfSearchStam.setStyle(null);
			TfSearchName.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorStam() throws SQLException {
		boolean validate = false;
		if (TfSearchStam.getText().isEmpty()) {
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
	
	public boolean validatorName() throws SQLException {
		boolean validate = false;
		if (TfSearchName.getText().isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido");
			validate=false;
		}else if (breederRepository.getBreederbyString("Name", TfSearchName.getText())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearchName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome nao existe");
			validate=false;
		}else {
			TfSearchStam.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public void updateInfo(Breeder b) {
		TfSearchStam.setStyle(null);
		TfSearchName.setStyle(null);
		LbTitle.setText(b.getName());
		LbName.setText(b.getName());
		LbPhone.setText(b.getCellphone().toString());
		LbEmail.setText(b.getEmail());
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
        if (clubs!=null)	
	        for (Club club : clubs) {
	            Federation federation = club.getFederation();
	            String federationName = federation.getName();
	            String breederStam = breeder.getStam().get(federation.getId());
	            ClubFederation clubFederation = new ClubFederation(club.getAcronym(), federationName, breederStam);
	            clubFederations.add(clubFederation);
	        }
        return clubFederations;
    }
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Criador XXXX");
		LbName.setText(null);
		LbPhone.setText(null);
		LbEmail.setText(null);
		LbDistrict.setText(null);
		LbLocale.setText(null);
		LbAddress.setText(null);
		TableID.setItems(null);
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
	
}
