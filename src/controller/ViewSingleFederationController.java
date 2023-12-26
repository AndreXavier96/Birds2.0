package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BreederFederationRepository;
import repository.FederationRepository;

import java.io.IOException;
import java.sql.SQLException;

import constants.MyValues;
import constants.PathsConstants;
import domains.Federation;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleFederationController {
	
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
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	
	
	@FXML
	public void btnDelete(ActionEvent event) throws IOException, SQLException {
		if (validator()&&validatorSearch()) {
			Federation federation = federationRepository.getFederationWhereString("Acronym", TfFederationSearch.getText());
			if (validatorDelete(federation)) {
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
						federationRepository.deleteFederation(federation);
						clearAllFields();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws IOException, SQLException {
		if (validator()&&validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/AddFederationView.fxml"));
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
		Federation updatedFederation = federationRepository.getFederationWhereString("Acronym",TfFederationSearch.getText().toUpperCase());
		updateAllInfo(updatedFederation);
	}
	
	@FXML
	public void btnSearchForAcronym(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Federation f = federationRepository.getFederationWhereString("Acronym", TfFederationSearch.getText().toUpperCase());
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
	
	public boolean validatorDelete(Federation federation) throws SQLException {
		boolean validate=false;
		if (breederFederationRepository.federationHasBreeders(federation.getId())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Federacao nao pode ser apagada porque tem criadores associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LbAcronym.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfFederationSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Federacao tem de ser procurada antes de editar/apagar.");
			validate=false;
		}else if (!TfFederationSearch.getText().equalsIgnoreCase(LbAcronym.getText())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfFederationSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Federacao tem de ser procurada antes de editar/apagar.");
	        validate = false;
	    }else {
	    	TfFederationSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		if (TfFederationSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfFederationSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Sigla tem de ser preenchido");
			validate=false;
		}else if (federationRepository.getFederationWhereString("Acronym",TfFederationSearch.getText().toUpperCase())==null) {
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
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
