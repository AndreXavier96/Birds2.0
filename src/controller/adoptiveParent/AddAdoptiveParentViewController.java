package controller.adoptiveParent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.BirdsRepository;
import java.io.IOException;
import java.sql.SQLException;
import constants.MyValues;
import constants.Regex;
import controller.brood.AddBroodViewController;
import domains.Bird;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class AddAdoptiveParentViewController {
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfBand;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private AddBroodViewController addBroodViewController;
	
	public void setAddBroodViewController(AddBroodViewController addBroodViewController) {
		this.addBroodViewController = addBroodViewController;
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException, IOException  {
		if (validator()) {
			LabelAlert.setStyle(null);
			Bird bird = birdsRepository.getBirdWhereString("Band", TfBand.getText().toUpperCase());
			addBroodViewController.addAdoptiveParentsToListView(bird);
			btnClose(event);
		}
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) TfBand.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {//TODO pais adoptivos nao podem ser eles proprios
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		Bird bird = birdsRepository.getBirdWhereString("Band", TfBand.getText().toUpperCase());
		if (TfBand.getText().length() == 0) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha tem de ser preenchido.");
			validated = false;
		} else if (!TfBand.getText().matches(Regex.FULL_BAND)) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha nao esta no formato correto.");
			validated = false;
		} else if (bird==null) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Passaro nao existe.");
			validated = false;
		} else {
			TfBand.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		return validated;
	}
		
	public void clearAllErrors() {
		TfBand.setStyle(null);
	}
	
	public void clearAllFields() {
		TfBand.setText("");
		clearAllErrors();
	}
}
