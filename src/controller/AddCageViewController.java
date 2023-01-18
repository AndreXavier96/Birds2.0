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
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class AddCageViewController implements Initializable {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelError;
	@FXML
	private TextField TfCages, TfCode, TfType;
	
	@FXML
	private TextField TfExistingCages;

	CageRepository cageRepository = new CageRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			TfExistingCages.setText(Integer.toString(cageRepository.getCageNumber()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validate()) {
			int i = Integer.parseInt(TfCages.getText());
			cageRepository.InsertMultipleCages(i,TfCode.getText(),TfType.getText());
			try {
				TfExistingCages.setText(Integer.toString(cageRepository.getCageNumber()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@FXML
	public void btnDelete(ActionEvent event) throws SQLException {
		if (validate()) {
			int i = Integer.parseInt(TfCages.getText());
			if (cageRepository.getCageNumber()-i<0) {
				TfCages.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Numero de gaiolas que pertende apagar grande de mais");
			}else
				cageRepository.deleteXCages(i);
			try {
				TfExistingCages.setText(Integer.toString(cageRepository.getCageNumber()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean validate() {
		boolean validate= false;
		if (!TfCages.getText().matches("^[0-9]\\d*$")) {
			TfCages.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Numero de gaiolas invalido");
			validate=false;
		}else {
			TfCages.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		if (validate) {
			if(TfType.getText().isBlank()) {
				TfType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Tipo tem de ser preenchido");
				validate=false;
			}else {
				TfType.setStyle(null);
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
