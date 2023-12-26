package controller;

import java.sql.SQLException;
import java.util.HashMap;

import constants.MyValues;
import constants.Regex;
import domains.Federation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.BreederFederationRepository;

public class StamPromptDialogController {

	@FXML
	private Label federationLabel,LabelError;
	@FXML
	private TextField stamTextField;
	
	private String promptResult;
	private HashMap<Integer, String> stamMap;
	boolean edit=false;
	int breederId;
	int federationId;
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	
	public void startValues(String federationName,HashMap<Integer, String> map) {
		federationLabel.setText(federationName);
		stamMap=map;
	}
	
	public void startValuesEdit(Federation federation,HashMap<Integer, String> map,int breederId) {
		federationLabel.setText(federation.getName());
		stamMap=map;
		edit=true;
		this.breederId=breederId;
		this.federationId=federation.getId();
	}
	
	@FXML
	public void submitButton(ActionEvent event) throws SQLException {
		if (!edit) {
			if (validate()) {
				Stage stage = (Stage) stamTextField.getScene().getWindow();
				promptResult = stamTextField.getText();
				stage.close();
			}
		} else{
			if (validateEdit()) {
				Stage stage = (Stage) stamTextField.getScene().getWindow();
				promptResult = stamTextField.getText();
				stage.close();
			}
		}

	}
	
	  public String getPromptResult() {
	        return promptResult;
	    }
	
	public boolean validate() throws SQLException {
		boolean validate = false;
		if (stamTextField.getText().length() == 0 || stamTextField.getText().isEmpty()) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam tem de ser preenchido!");
			validate = false;
		} else if (!stamTextField.getText().matches(Regex.STAM)) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam nao esta no formato correto!");
			validate = false;
		}else if (breederFederationRepository.checkIfStamExists(stamTextField.getText()) || stamMap.containsValue(stamTextField.getText())) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam ja existe!");
			validate = false;
		} else {
			stamTextField.setStyle(null);
			LabelError.setText("");
			validate = true;
		}
		return validate;
	}

	public boolean validateEdit() throws SQLException {
		boolean validate = false;
		Integer breederFederationId = breederFederationRepository.getIdByBreederAndFederationId(breederId,federationId);
		if (breederFederationId==null)
			breederFederationId=-1;
		if (stamTextField.getText().length() == 0 || stamTextField.getText().isEmpty()) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam tem de ser preenchido!");
			validate = false;
		} else if (!stamTextField.getText().matches(Regex.STAM)) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam nao esta no formato correto!");
			validate = false;
		} else if (breederFederationRepository.checkIfExistsString("Stam", stamTextField.getText(), breederFederationId) || stamMap.containsValue(stamTextField.getText())) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam ja existe!");
			validate = false;
		} else {
			stamTextField.setStyle(null);
			LabelError.setText("");
			validate = true;
		}
		return validate;
	}
}
