package controller;

import javafx.fxml.FXML;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import constants.MyValues;
import domains.Bird;
import domains.Cage;
import domains.Historic;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CageRepository;
import repository.HistoricRepository;
import javafx.scene.control.DatePicker;

public class ChangeBirdCageController {
	@FXML
	private Label LabelError,LbBand,LbCurrentCage;
	@FXML
	private ComboBox<Cage> CbCage;
	@FXML
	private DatePicker Dtpicker;
	@FXML
	private ImageView IvImage;
	@FXML
	private ViewSingleBirdController viewSingleBirdController;

	CageRepository cageRepository = new CageRepository();
	BirdsRepository birdsRepository = new BirdsRepository();
	HistoricRepository historicRepository = new HistoricRepository();
	
	public void startValues(String band,String img,String currentCageCode) {
		LbBand.setText(band);
		IvImage.setImage(new Image(img));
		LbCurrentCage.setText(currentCageCode);
		ObservableList<Cage> cages = cageRepository.getAllCages();
		for(int i=0;i<cages.size();i++)
			if (cages.get(i).getCode().equals(currentCageCode))
				cages.remove(i);
		CbCage.setItems(cages);
	}
	
	public void setViewSingleBirdController(ViewSingleBirdController controller) {
		this.viewSingleBirdController = controller;
	}
	
	@FXML
	public void btnChange(ActionEvent event) {
		if (validate()) {
			Bird bird = birdsRepository.getBirdWhereString("Band",LbBand.getText());
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(Dtpicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			birdsRepository.partialUpdateIntBird(bird.getId(), "CageId", CbCage.getValue().getId());
			String obs = "Passaro alterado da gaiola '"+LbCurrentCage.getText()+"' para '"+CbCage.getValue().getCode()+"'.";
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_CAGE,date,obs, bird));
		
			// close the window
		    Stage stage = (Stage) CbCage.getScene().getWindow();
		    stage.close();
		  //Set Success msg in viewSingleBird
			viewSingleBirdController.setSuccess(MyValues.CHANGE_STATE_SUCCESS, birdsRepository.getBirdWhereString("Band",LbBand.getText()));
		}
	}
	
	public boolean validate() {
		boolean validate = false;
		if (CbCage.getValue()==null) {
			CbCage.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nova Gaiola tem de ser escolhida");
			validate=false;
		}else {
			CbCage.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		
		if (validate) {
			try {
				Dtpicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				Dtpicker.setStyle(null);
				LabelError.setStyle("");
				validate=true;
			} catch (Exception e) {
				Dtpicker.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Ano nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}
		}
		
		return validate;
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) CbCage.getScene().getWindow();
	    stage.close();
	}
}
