package controller;

import java.util.HashMap;

import constants.MyValues;
import constants.Regex;
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
	
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	
	public void startValues(String federationName,HashMap<Integer, String> map) {
		federationLabel.setText(federationName);
		stamMap=map;
	}
	
	@FXML
	public void submitButton(ActionEvent event) {
		if (validate()) {
			 Stage stage = (Stage) stamTextField.getScene().getWindow();
			 promptResult = stamTextField.getText();
			 stage.close();
		}
	}
	
	  public String getPromptResult() {
	        return promptResult;
	    }
	
	public boolean validate() {
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
}
