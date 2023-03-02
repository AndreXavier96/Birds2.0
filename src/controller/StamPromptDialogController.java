package controller;

import constants.MyValues;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StamPromptDialogController {

	@FXML
	private Label federationLabel,LabelError;
	@FXML
	private TextField stamTextField;
	
	private String promptResult;
	
	
	public void startValues(String federationName) {
		federationLabel.setText(federationName);
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
		} else if (!stamTextField.getText().matches("^([A-Z]|[0-9])+$")) {
			stamTextField.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Stam nao esta no formato correto!");
			validate = false;
		} else {
			stamTextField.setStyle(null);
			LabelError.setText("");
			validate = true;
		}
		return validate;
	}
}
