package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.FederationRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import domains.Federation;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleFederationController {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private Label LbTitle;
	@FXML
	private Label LabelAlert;
	@FXML
	private TextField TfFederationSearch;
	@FXML
	private Label LbName;
	@FXML
	private Label LbAcronym;
	@FXML
	private Label LbEmail;
	@FXML
	private Label LbCountry;

	private FederationRepository federationRepository = new FederationRepository();
	
	
	@FXML
	public void btnDelete(ActionEvent event) throws IOException, SQLException {
		if (validator()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = loader.load();
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer apagar a federacao: '" + LbAcronym.getText() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_FEDERATION+LbAcronym.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					Federation federation = federationRepository.getFederationWhereString("Acronym", TfFederationSearch.getText());
					federationRepository.deleteFederation(federation);
					clearAllFields();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws IOException, SQLException {
		if (validator()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/addFederationView.fxml"));
			Parent root = loader.load();
			Federation federation = federationRepository.getFederationWhereString("Acronym",
					TfFederationSearch.getText());
			AddFederationViewController addFederationViewController = loader.getController();
			addFederationViewController.startValues(federation);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_EDIT_FEDERATION + LbAcronym.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Federation updatedFederation = federationRepository.getFederationWhereInt("id",federation.getId());
			search(updatedFederation.getAcronym());

		}
	}
	
	public void search(String Acronym) throws SQLException {
		clearAllFields();
		TfFederationSearch.setText(Acronym);
		Federation updatedFederation = federationRepository.getFederationWhereString("Acronym",TfFederationSearch.getText());
		updateAllInfo(updatedFederation);
	}
	
	@FXML
	public void btnSearchForAcronym(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Federation f = federationRepository.getFederationWhereString("Acronym", TfFederationSearch.getText());
			updateAllInfo(f);
		}
	}
	
	public void updateAllInfo(Federation f) {
		LbTitle.setText("Federacao "+f.getName());
		LbName.setText(f.getName());
		LbAcronym.setText(f.getAcronym());
		LbEmail.setText(f.getEmail());
		LbCountry.setText(f.getCountry());
	}
	
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Federacao XXXX");
		LbName.setText(null);
		LbAcronym.setText(null);
		LbEmail.setText(null);
		LbCountry.setText(null);
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfFederationSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfFederationSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla tem de ser preenchido");
			validate=false;
		}else if (federationRepository.getFederationWhereString("Acronym",TfFederationSearch.getText())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfFederationSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla nao existe");
			validate=false;
		}else {
			TfFederationSearch.setStyle(null);
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
