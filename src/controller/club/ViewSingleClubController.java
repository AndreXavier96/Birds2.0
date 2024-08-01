package controller.club;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederClubRepository;
import repository.ClubRepository;

import java.io.IOException;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import domains.Club;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleClubController {
	
	@FXML
	private Label LbTitle;
	@FXML
	private Label LabelAlert;
	@FXML
	private TextField TfSearch;
	@FXML
	private Label LBName;
	@FXML
	private Label LBFederation;
	@FXML
	private Label LBAcronym;
	@FXML
	private Label LBEmail;
	@FXML
	private Label LBPhone;
	@FXML
	private Label LBLocale;
	@FXML
	private Label LbAddress;
	
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository= new BreederClubRepository();

	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
		if (validator()&&validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/AddClubView.fxml"));
			Parent root = loader.load();
			Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText());
			AddClubViewController addClubViewController = loader.getController();
			addClubViewController.startValuesEdit(c);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_EDIT_CLUB + LBAcronym.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Club updateClub = clubRepository.getClubByID(c.getId());
			search(updateClub.getAcronym());
		}
	}
	
	@FXML
	public void btnDelete(ActionEvent event) throws SQLException, IOException {
		if (validator() && validatorSearch()) {
			Club c = clubRepository.getClubWhereString("Acronym", TfSearch.getText());
			if (validatorDelete(c)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
				Parent root = loader.load();
				ConfirmationController confirmationController = loader.getController();
				confirmationController.getLbText()
						.setText("Tem a certeza que quer apagar o Clube: '" + LBAcronym.getText() + "'?");
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.setTitle(MyValues.TITLE_DELETE_CLUB + LBAcronym.getText());
				stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				if (confirmationController.isConfirmed()) {
					try {
						clubRepository.deleteClub(c);
						clearAllFields();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void search(String Acronym) throws SQLException {
		clearAllFields();
		TfSearch.setText(Acronym);
		Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText().toUpperCase());
		updateAllInfo(c);
	}
	
	@FXML
	public void btnSearchForAcronym(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText().toUpperCase());
			updateAllInfo(c);
		}
	}
	
	public void updateAllInfo(Club c) {
		LbTitle.setText(c.getName());
		LBName.setText(c.getName());
		LBFederation.setText(c.getFederation().getName());
		LBAcronym.setText(c.getAcronym());
		LBEmail.setText(c.getEmail());
		LBPhone.setText(c.getPhone());
		LBLocale.setText(c.getLocale());
		LbAddress.setText(c.getAddress());
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Clube XXXX");
		LBName.setText(null);
		LBFederation.setText(null);
		LBAcronym.setText(null);
		LBEmail.setText(null);
		LBPhone.setText(null);
		LBLocale.setText(null);
		LbAddress.setText(null);
	}
	
	public boolean validatorDelete(Club club) throws SQLException {
		boolean validate=false;
		if (breederClubRepository.clubHasBreeders(club.getId())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Clube nao pode ser apagado porque tem criadores associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LBAcronym.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Clube tem de ser procurado antes de editar/apagar.");
			validate=false;
		}else if (!TfSearch.getText().equalsIgnoreCase(LBAcronym.getText().toUpperCase())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Clube tem de ser procurado antes de editar/apagar.");
	        validate = false;
	    }else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla tem de ser preenchido");
			validate=false;
		}else if (clubRepository.getClubWhereString("Acronym",TfSearch.getText().toUpperCase())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla nao existe");
			validate=false;
		}else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
