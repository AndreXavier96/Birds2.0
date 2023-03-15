package controller;

import javafx.fxml.FXML;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import constants.MyValues;
import domains.Bird;
import domains.Historic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.HistoricRepository;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChangeBirdSexController{
	@FXML
	private Label LabelAlert;
	@FXML
	private Label LbBand,LbCurrentSex;
	@FXML
	private ComboBox<String> CbSex;
	@FXML
	private ImageView IvImage;
	@FXML
	private DatePicker Dtpicker;
	@FXML
	private ViewSingleBirdController viewSingleBirdController;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	public void startValues(String band,String img,String actualSex) {
		LbBand.setText(band);
		LbCurrentSex.setText(actualSex);
		IvImage.setImage(new Image(img));
		ObservableList<String> list = FXCollections.observableArrayList(MyValues.SEXLIST);
		for(int i=0;i<MyValues.SEXLIST.size();i++) {
			if (MyValues.SEXLIST.get(i).equals(actualSex)) {
				list.remove(i);
			}
		}
		CbSex.setItems(list);
	}

	public void setViewSingleBirdController(ViewSingleBirdController controller) {
		this.viewSingleBirdController = controller;
	}
	
	@FXML
	public void btnChange(ActionEvent event) throws SQLException {
		if (validate()) {
			String newSex = CbSex.getValue();
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(Dtpicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			Bird bird = birdsRepository.getBirdWhereString("Band",LbBand.getText());
			bird.setSex(newSex);
			birdsRepository.partialUpdateStringsBird(bird.getId(), "Sex", bird.getSex());
			String obs = "Sexo do passaro alterado de '"+LbCurrentSex.getText()+"' para '"+bird.getSex()+"'.";	
			
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_SEX,date,obs, bird));
			// close the window
		    Stage stage = (Stage) CbSex.getScene().getWindow();
		    stage.close();
			//Set Success msg in viewSingleBird
			viewSingleBirdController.setSuccess(MyValues.CHANGE_SEX, birdsRepository.getBirdWhereString("Band",LbBand.getText()));			
		}
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) CbSex.getScene().getWindow();
	    stage.close();
	}
	
 	public boolean validate() {
 		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		boolean validate= false;
		if (CbSex.getValue()==null) {
			CbSex.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Novo genero tem de ser escolhido");
			validate=false;
		}else {
			CbSex.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			try {
				Dtpicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				Dtpicker.setStyle(null);
				LabelAlert.setStyle("");
				validate=true;
			} catch (Exception e) {
				Dtpicker.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Ano nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}
		}
		return validate;
	}
}
