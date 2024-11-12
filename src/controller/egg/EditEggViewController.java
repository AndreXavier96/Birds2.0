package controller.egg;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import repository.EggRepository;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import constants.MyValues;
import domains.Egg;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class EditEggViewController implements Initializable {
	
	@FXML
	private Label LabelAlert,LbTitle;
	@FXML
	private DatePicker DtPosture,DtOutbrake;
	@FXML
	private ComboBox<String> CbType,CbStatute;
	
	
	private EggRepository eggRepository = new EggRepository();
	private Egg egg;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.OVO_TYPE);
//		CbStatute.setItems(MyValues.OVO_STATUTE);
		CbType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.equals(MyValues.DESCONHECIDO2)) {
					CbStatute.setItems(MyValues.OVO_STATUTE_DESCONHECIDO2);
				} else if (newValue.equals(MyValues.FECUNDADO)) {
					CbStatute.setItems(MyValues.OVO_STATUTE_FECUNDADO);
				} else if (newValue.equals(MyValues.VAZIO)) {
					CbStatute.setItems(MyValues.OVO_STATUTE_VAZIO);
				}
			}
		});
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			LabelAlert.setStyle(null);
			egg.setPostureDate(Date.from(DtPosture.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			if (DtOutbrake.getValue()!=null) {
				egg.setOutbreakDate(Date.from(DtOutbrake.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			}
			egg.setType(CbType.getValue());
			egg.setStatute(CbStatute.getValue());
			
			eggRepository.updateEgg(egg);
			
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Ovo alterado com sucesso!");
			btnClose(event);
		}
	}
	
	public void startValuesEdit(Egg egg) throws SQLException {
		this.egg=egg;
		DtPosture.setValue(egg.getPostureDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		if (egg.getOutbreakDate()!=null) 
			DtOutbrake.setValue(egg.getOutbreakDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		CbType.setValue(egg.getType());
		CbStatute.setValue(egg.getStatute());
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LbTitle.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		
		try {
			DtPosture.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			DtPosture.setStyle(null);
			validated=true;
		} catch (Exception e) {
			DtPosture.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Data postura nao esta no formato correto ou tem de ser preenchido");
			validated=false;
		}
		
		if (validated) {
			if (DtOutbrake.getValue()!= null) {
				try {
					DtOutbrake.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					DtOutbrake.setStyle(null);
					validated=true;
				} catch (Exception e) {
					DtOutbrake.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Data Eclosao nao esta no formato correto ou tem de ser preenchido");
					validated=false;
				}
			}
		}
		
		if (validated) {
			if (CbType.getValue() == null) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tipo tem de ser escolhido.");
				validated = false;
			}else if (DtOutbrake.getValue()!=null && CbType.getValue()!=MyValues.FECUNDADO) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Se existiu eclosao, ovo tem de estar fecundado");
				validated = false;
			}else {
				CbType.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			if (CbStatute.getValue() == null) {
				CbStatute.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Estado tem de ser escolhido.");
				validated = false;
			}else if (DtOutbrake.getValue()!=null && CbStatute.getValue()!=MyValues.CHOCADO) {
				CbStatute.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Se existiu eclosao, ovo tem de estar chocado");
				validated = false;
			} else {
				CbStatute.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		return validated;
	}
		
	public void clearAllErrors() {
		DtOutbrake.setStyle(null);
		DtPosture.setStyle(null);
		CbType.setStyle(null);
		CbStatute.setStyle(null);
	}
	
	public void clearAllFields() {
		DtOutbrake.setValue(null);
		DtPosture.setValue(null);
		CbType.setValue(null);
		CbStatute.setValue(null);
		clearAllErrors();
	}
}
