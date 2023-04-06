package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.CageRepository;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Cage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AddCageViewController implements Initializable {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelAlert;
	@FXML
	private TextField TfCode;
	@FXML
	private ComboBox<String> CbType;

	CageRepository cageRepository = new CageRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.CAGE_TIPE);
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
			Cage cage = new Cage();
			cage.setCode(TfCode.getText());
			cage.setType(CbType.getValue());
			cageRepository.Insert(cage);
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Gaiola "+cage.getCode()+" inserida com sucesso!");
			clearAllFields();
		}
	}	
	
	public boolean validator() throws SQLException {
		boolean validate= false;
		clearAllErros();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfCode.getText().isEmpty()) {
			TfCode.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Codigo de gaiolas tem de ser preenchido");
			validate=false;
		}else if (cageRepository.checkIfCodeExist(TfCode.getText())) {
			TfCode.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Codigo de gaiola ja existe");
			validate=false;
		}else {
			TfCode.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		if (validate) {
			if(CbType.getValue()==null) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tipo tem de ser escolhido");
				validate=false;
			}else {
				CbType.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		return validate;
	}
	
	@FXML
	public void btnBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainScene.fxml"));
			root = loader.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearAllErros() {
		TfCode.setStyle(null);
		CbType.setStyle(null);
	}
	
	public void clearAllFields() {
		TfCode.setText("");
		CbType.setValue(null);
		clearAllErros();
	}
}
