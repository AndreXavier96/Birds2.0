package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.CageRepository;

import java.net.URL;
import java.nio.file.Paths;
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
	private Label LabelError;
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
		if (validate()) {
			Cage cage = new Cage();
			cage.setCode(TfCode.getText());
			cage.setType(CbType.getValue());
			cageRepository.Insert(cage);
		}
		
	}	
	
	public boolean validate() throws SQLException {
		boolean validate= false;
		if (TfCode.getText().isEmpty()) {
			TfCode.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Codigo de gaiolas tem de ser preenchido");
			validate=false;
		}else if (cageRepository.checkIfCodeExist(TfCode.getText())) {
			TfCode.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Codigo de gaiola ja existe");
			validate=false;
		}else {
			TfCode.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		if (validate) {
			if(CbType.getValue().isEmpty()) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Tipo tem de ser escolhido");
				validate=false;
			}else {
				CbType.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
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
