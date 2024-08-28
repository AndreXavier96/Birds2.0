package controller.exibithion;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.ExibithionRepository;
import java.sql.SQLException;
import constants.MyValues;
import controller.GenericController;
import domains.Exibithion;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AddExibithionViewController {
	
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfName, TfLocale;
	@FXML
	private Button btnClose;
	
	private ExibithionRepository exibithionRepository = new ExibithionRepository();
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			LabelAlert.setStyle(null);
			Exibithion e = new Exibithion();
			e.setName(GenericController.replacePortugueseSpecialCharacters(TfName.getText().toLowerCase()));
			e.setLocale(GenericController.replacePortugueseSpecialCharacters(TfLocale.getText()));
			exibithionRepository.Insert(e);
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Exposicao " + e.getName() + " inserida com sucesso!");
			clearAllFields();
		}
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		if (TfName.getText().length() == 0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido.");
			validated = false;
		} else {
			TfName.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (TfLocale.getText().length() == 0) {
				TfLocale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Localidade tem de ser preenchida.");
				validated = false;
			} else {
				TfLocale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		return validated;
	}
		
	public void clearAllErrors() {
		TfName.setStyle(null);
		TfLocale.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText("");
		TfLocale.setText("");
		clearAllErrors();
	}
}
