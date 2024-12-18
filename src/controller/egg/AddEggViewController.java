package controller.egg;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import constants.MyValues;
import constants.Regex;
import controller.brood.AddBroodViewController;
import controller.brood.ViewSingleBroodController;
import domains.Egg;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class AddEggViewController implements Initializable {
	
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfQuantidade;
	@FXML
	private DatePicker DtPosture;
	@FXML
	private ComboBox<String> CbType;

	private AddBroodViewController addBroodViewController;
	private ViewSingleBroodController viewSingleBroodController;
	private Date startDate;
	private Date finishDate;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbType.setItems(MyValues.OVO_STATUTE);
	}
	
	public void setAddBroodViewController(AddBroodViewController addBroodViewController, LocalDate startDate, LocalDate finishDate) {
		this.addBroodViewController = addBroodViewController;
		this.startDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		if(finishDate!=null)
			this.finishDate = Date.from(finishDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public void setAddBroodViewController2(ViewSingleBroodController viewSingleBroodController, Date startDate, Date finishDate) {
		this.viewSingleBroodController = viewSingleBroodController;
		this.startDate = startDate;
		if(finishDate!=null)
			this.finishDate = finishDate;
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException, IOException  {
		if (validator()) {
			LabelAlert.setStyle(null);
			String [] result = calculateStatus(CbType.getValue());
			int quantidade = Integer.parseInt(TfQuantidade.getText());
			List<Egg> eggsInserted=new ArrayList<Egg>();
			for (int i=0;i<quantidade;i++) {
				Egg egg = new Egg();
				egg.setType(result[0]);
				egg.setStatute(result[1]);
				egg.setPostureDate(Date.from(DtPosture.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				eggsInserted.add(egg);
			}
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Ovos inseridos com sucesso!");
			clearAllFields();
			if (addBroodViewController !=null)
				addBroodViewController.addEggToListView(eggsInserted);
			else
				viewSingleBroodController.addEggToListView(eggsInserted);
			btnClose(event);
		}
	}
	
	public String[] calculateStatus(String type) {
		switch (type) {
		case MyValues.DESCONHECIDO2:
			return new String[] { MyValues.DESCONHECIDO2, MyValues.DESCONHECIDO2 };
		case MyValues.PARTIDO:
			return new String[] { MyValues.DESCONHECIDO2, MyValues.PARTIDO };
		case MyValues.EM_DESENVOLVIMENTO:
			return new String[] {MyValues.FECUNDADO, MyValues.EM_DESENVOLVIMENTO};
		case MyValues.CHOCADO:
			return new String[] {MyValues.FECUNDADO, MyValues.CHOCADO};
		case MyValues.MORTE_NO_OVO:
			return new String[] {MyValues.FECUNDADO, MyValues.MORTE_NO_OVO};
		case MyValues.AUSENCIA_DE:
			return new String[] {MyValues.DESCONHECIDO2, MyValues.AUSENCIA_DE};
		}
		return null;
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) TfQuantidade.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		if (TfQuantidade.getText().length() == 0) {
			TfQuantidade.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Quantidade tem de ser preenchido.");
			validated = false;
		} else if (!TfQuantidade.getText().matches(Regex.INT)) {
			TfQuantidade.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Quantidade nao esta no formato correto.");
			validated = false;
		} else {
			TfQuantidade.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		
		if (validated) {
			if (CbType.getValue()==null) {
				CbType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tipo tem de ser escolhido.");
				validated = false;
			} else {
				CbType.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		if (validated) {
			try {
				DtPosture.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				DtPosture.setStyle(null);
//				LabelAlert.setStyle("");
				validated=true;
			} catch (Exception e) {
				DtPosture.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Data nao esta no formato correto ou tem de ser preenchido");
				validated=false;
			}
		}
		
	    if (validated) {
	    	Date choosenDate = Date.from(DtPosture.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    	if (choosenDate.before(startDate)) {
	            DtPosture.setStyle(MyValues.ERROR_BOX_STYLE);
	            LabelAlert.setText("Data postura nao pode ser antes de data inicio postura de ninhada!");
	            validated = false;
	        } else {
	            DtPosture.setStyle(null);
	            LabelAlert.setText("");
	            validated = true;
	        }
	    }
	    
	    if (validated && finishDate != null) {
	    	Date choosenDate = Date.from(DtPosture.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	        if (choosenDate.after(finishDate)) {
	            DtPosture.setStyle(MyValues.ERROR_BOX_STYLE);
	            LabelAlert.setText("Data postura nao pode ser depois de data fim postura de ninhada!");
	            validated = false;
	        } else {
	            DtPosture.setStyle(null);
	            LabelAlert.setText("");
	            validated = true;
	        }
	    }
		return validated;
	}
		
	public void clearAllErrors() {
		TfQuantidade.setStyle(null);
		DtPosture.setStyle(null);
		CbType.setStyle(null);
	}
	
	public void clearAllFields() {
		TfQuantidade.setText("");
		DtPosture.setValue(null);;
		CbType.setValue(null);;
		clearAllErrors();
	}
}
