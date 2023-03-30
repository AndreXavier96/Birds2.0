package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.ClubRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import domains.Club;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleClubController {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
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

	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
		if (validator()) {
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
		if (validator()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = loader.load();
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer apagar o Clube: '" + LBAcronym.getText() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_CLUB+LBAcronym.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText());
					clubRepository.deleteClub(c);
					clearAllFields();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void search(String Acronym) throws SQLException {
		clearAllFields();
		TfSearch.setText(Acronym);
		Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText());
		updateAllInfo(c);
	}
	
	@FXML
	public void btnSearchForAcronym(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Club c = clubRepository.getClubWhereString("Acronym",TfSearch.getText());
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
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla tem de ser preenchido");
			validate=false;
		}else if (clubRepository.getClubWhereString("Acronym",TfSearch.getText())==null) {
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
}
