package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.TreatmentRepository;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.Regex;
import domains.Treatment;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AddTreatmentViewController implements Initializable {
	
	@FXML
	private Label LabelAlert;
	@FXML
	private TextField TfName,TfDesc,TfFreq,TfDuration;
	@FXML
	private ComboBox<String> CbFreqType;

	TreatmentRepository treatmentRepository = new TreatmentRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbFreqType.setItems(MyValues.FREQUENCIA);
		CbFreqType.valueProperty().addListener((observable, oldValue, newValue) -> {
		      if (MyValues.FREQUENCIA.contains(newValue)) {
		    	  TfDuration.setDisable(false);
		    	  TfFreq.setText("");
		    	  TfFreq.setPromptText(newValue);
		    	  TfFreq.setDisable(false);
		      }
		    });
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
			Treatment t = new Treatment();
			t.setName(TfName.getText());
			t.setDescription(TfDesc.getText());
			t.setFrequency(Integer.parseInt(TfFreq.getText()));
			t.setFrequencyType(CbFreqType.getValue());
			t.setDurationDays(Integer.parseInt(TfDuration.getText()));
			treatmentRepository.Insert(t);
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Tratamento "+t.getName()+" inserido com sucesso!");
			clearAllFields();
		}
	}	
	
	public boolean validator() throws SQLException {
		boolean validate= false;
		clearAllErros();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfName.getText().isEmpty()) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Nome tem de ser preenchido");
			validate=false;
		}else {
			TfName.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfDesc.getText().isEmpty()) {
				TfDesc.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Descricao tem de ser preenchida");
				validate=false;
			}else {
				TfDesc.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (TfFreq.getText().isEmpty()) {
				TfFreq.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Frequencia tem de ser preenchida");
				validate=false;
			}else if (!TfFreq.getText().matches(Regex.INT)) {
				TfFreq.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Frequencia tem de ser um numero inteiro");
				validate=false;
			}else {
				TfFreq.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (CbFreqType.getValue()==null) {
				CbFreqType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tipo frequencia tem de ser preenchida");
				validate=false;
			}else {
				CbFreqType.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (TfDuration.getText().isEmpty()) {
				TfDuration.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Duracao tem de ser preenchida");
				validate=false;
			}else if (!TfDuration.getText().matches(Regex.INT)) {
				TfDuration.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Duracao tem de ser um numero inteiro");
				validate=false;
			}else {
				TfDuration.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		return validate;
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.close(); 
	}

	public void clearAllErros() {
		TfName.setStyle(null);
		TfDesc.setStyle(null);
		TfFreq.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText("");
		TfDesc.setText("");
		TfFreq.setText("");
		clearAllErros();
	}

	
}
