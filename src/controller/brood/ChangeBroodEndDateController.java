package controller.brood;

import javafx.fxml.FXML;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import constants.MyValues;
import domains.Brood;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import repository.BroodRepository;
import repository.CageRepository;

public class ChangeBroodEndDateController {
	@FXML
	private Label LabelError;
	@FXML
	private DatePicker DtDate;
	
	@FXML
	private ViewSingleBroodController viewSingleBroodController;
	
	private Brood brood;

	CageRepository cageRepository = new CageRepository();
	BroodRepository broodRepository = new BroodRepository();
	
	public void setViewSingleBroodController(ViewSingleBroodController controller,Brood brood) {
		this.viewSingleBroodController = controller;
		this.brood = brood;
		if (brood.getFinish()!=null) {
			 java.util.Date utilDate = new java.util.Date(brood.getFinish().getTime());
			 DtDate.setValue(utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
	}
	
	
	@FXML
	public void btnChange(ActionEvent event) throws SQLException {
		if (validate()) {
			broodRepository.updateFinishDateBrood(brood.getId(), Date.from(DtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		    Stage stage = (Stage) DtDate.getScene().getWindow();
		    stage.close();
		    viewSingleBroodController.setSuccess(MyValues.CHANGE_END_DATE_SUCCESS, broodRepository.getBroodById(brood.getId()));
		}
	}
	
	public boolean validate() {
		boolean validate = false;
		try {
			DtDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			DtDate.setStyle(null);
			validate=true;
		} catch (Exception e) {
			DtDate.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Data nao esta no formato correto ou tem de ser preenchido");
			validate=false;
		}
		
		if (validate) {
	    	Date choosenDate = Date.from(DtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	if (choosenDate.before(brood.getStart())) {
	    		DtDate.setStyle(MyValues.ERROR_BOX_STYLE);
	    		LabelError.setText("Data postura nao pode ser antes de data inicio postura de ninhada!");
	            validate = false;
	        } else {
	        	DtDate.setStyle(null);
	        	LabelError.setText("");
	            validate = true;
	        }
	    }
		
		return validate;
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) LabelError.getScene().getWindow();
	    stage.close();
	}
}
