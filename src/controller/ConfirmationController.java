package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationController {

	@FXML
	public Label LbText;

	private boolean confirmed;

	@FXML
	public void BtnSim(ActionEvent event) {
		confirmed = true;
		Stage stage = (Stage) LbText.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void BtnNao(ActionEvent event) {
		confirmed = false;
		Stage stage = (Stage) LbText.getScene().getWindow();
		stage.close();
	}

	public Label getLbText() {
		return LbText;
	}

	public void setLbText(Label LbText) {
		this.LbText = LbText;
	}

	public boolean isConfirmed() {
		return confirmed;
	}
}
