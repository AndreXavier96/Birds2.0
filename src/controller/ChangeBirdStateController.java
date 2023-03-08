package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Bird;
import domains.Historic;
import domains.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.HistoricRepository;
import repository.StateRepository;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChangeBirdStateController implements Initializable{
	@FXML
	private Label LabelError;
	@FXML
	private Label LbBand,LbState;
	@FXML
	private AnchorPane ApValor;
	@FXML
	private TextField TfPriceSell;
	@FXML
	private AnchorPane ApMotivo;
	@FXML
	private TextField TfMotivo;
	@FXML
	private ComboBox<String> CbState;
	@FXML
	private ImageView IvImage;
	@FXML
	private DatePicker Dtpicker;
	
	@FXML
	private ViewSingleBirdController viewSingleBirdController;
	
	private StateRepository stateRepositor = new StateRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	public void startValues(String band,String actualState,String img) {
		LbBand.setText(band);
		LbState.setText(actualState);
		IvImage.setImage(new Image(img));
		ObservableList<String> states = FXCollections.observableArrayList(MyValues.STATELIST);
		for(int i=0;i<MyValues.STATELIST.size();i++) {
			if (MyValues.STATELIST.get(i).equals(actualState)) {
				states.remove(i);
			}
		}
		CbState.setItems(states);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbState.valueProperty().addListener((observable, oldValue, newValue) -> {
		      if (MyValues.MORTO.equals(newValue)) {
		    	  ApMotivo.setVisible(true);
		    	  ApValor.setVisible(false);
		      } else if(MyValues.VENDIDO.equals(newValue)) {
		    	  ApMotivo.setVisible(false);
		    	  ApValor.setVisible(true);
		      }else {
		    	  ApMotivo.setVisible(false);
		    	  ApValor.setVisible(false);
		      }
		    });
	}
	
	public void setViewSingleBirdController(ViewSingleBirdController controller) {
		this.viewSingleBirdController = controller;
	}
	
	@FXML
	public void btnChange(ActionEvent event) {
		if (validate()) {
			State state = new State();
			state.setType(CbState.getValue());
			if (state.getType().equals(MyValues.MORTO))
				state.setMotivo(TfMotivo.getText());
			else if (state.getType().equals(MyValues.VENDIDO))
				state.setValor(Double.parseDouble(TfPriceSell.getText()));
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(Dtpicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			state.setDate(date);
			Bird bird = birdsRepository.getBirdWhereString("Band",LbBand.getText());
			state.setId(bird.getState().getId());
			stateRepositor.updateState(state);
			String obs = "Estado do passaro alterado de '"+LbState.getText()+"' para '"+state.getType()+"'";	
			if (state.getType().equals(MyValues.MORTO)) {
				obs+=", motivo '"+state.getMotivo()+"'.";
				birdsRepository.partialUpdateIntBird(bird.getId(), "CageId", null);
			}else if (state.getType().equals(MyValues.VENDIDO)) {
				obs += ", preco '"+state.getValor()+"'.";
			}else
				obs+=".";
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_STATE,date,obs, bird));
			// close the window
		    Stage stage = (Stage) CbState.getScene().getWindow();
		    stage.close();
			//Set Success msg in viewSingleBird
			viewSingleBirdController.setSuccess(MyValues.CHANGE_STATE_SUCCESS, birdsRepository.getBirdWhereString("Band",LbBand.getText()));			
		}
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) CbState.getScene().getWindow();
	    stage.close();
	}
	
 	public boolean validate() {
		boolean validate= false;
		if (CbState.getValue()==null) {
			CbState.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Novo Estado tem de ser escolhido");
			validate=false;
		}else {
			CbState.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		
		if (validate) {
			if (CbState.getValue()==MyValues.MORTO && TfMotivo.getText().isBlank()) {
				TfMotivo.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Motivo tem de ser preenchido");
				validate=false;
			}else {
				TfMotivo.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		}
		
		
		if (validate) {
			if (CbState.getValue()==MyValues.VENDIDO && TfPriceSell.getText().isBlank()) {
				TfPriceSell.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Valor tem de ser preenchido");
				validate=false;
			}else if (CbState.getValue()==MyValues.VENDIDO && !TfPriceSell.getText().matches("^[+]?[0-9]*[.]?[0-9]+$")) {
				TfPriceSell.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Valor no formato incorreto");
				validate=false;
			}else {
				TfPriceSell.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
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
}
