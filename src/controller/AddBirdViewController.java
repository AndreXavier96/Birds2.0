package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.BirdsRepository;
import repository.BreederRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Bird;
import domains.Breeder;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class AddBirdViewController implements Initializable {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private ComboBox<Breeder> CbCriador;
	
	@FXML
	private TextField TfAno, TfNumero, TfEntryType,TfBuyPrice;
	
	@FXML
	private TextField TfSellPrice,TfState,TfSex,TfFather,TfMother,TfSpecie;
	
	@FXML
	private TextField TfMutation,TfCage,TfBreeder,TfPosture;
	
	@FXML
	private DatePicker DfDataEntrada;
	
	@FXML
	private Label LabelAnilha, LabelError;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private BreederRepository breederRepository = new BreederRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbCriador.setItems(null);
		CbCriador.setItems(breederRepository.getAllBreeders());
		CbCriador.setConverter(new StringConverter<Breeder>() {
			 public String toString(Breeder b) {
				 return b.getName();
			 }
			 
			 public Breeder fromString(String s) {
				 return CbCriador.getItems().stream().filter(b -> b.getName().equals(s)).findFirst().orElse(null);
			 }
			 
		});
	}
	
	
	
	@FXML
	public void btnAdd(ActionEvent event) {
		boolean validate=validate();
		if(validate) {
			String anilha =CbCriador.getValue().getStam()+"-"+TfAno.getText()+"-"+TfNumero.getText() ;
			Bird bird = new Bird();
			bird.setNrBreeder(CbCriador.getValue());
			bird.setBand(anilha);
			bird.setYear(Integer.parseInt(TfAno.getText()));
			bird.setEntryDate(Date.from(DfDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	//		bird.setEntryType(TfEntryType.getText());
	//		bird.setBuyPrice(Double.parseDouble(TfBuyPrice.getText()));
	//		bird.setSellPrice(Double.parseDouble(TfSellPrice.getText()));
	//		bird.setStatel(TfState.getText());
	//		bird.setSex(TfSex.getText());
	//		bird.setFather(birdsRepository.getBird(Integer.parseInt(TfFather.getText())));
	//		bird.setMother(birdsRepository.getBird(Integer.parseInt(TfMother.getText())));
	//		bird.setSpecies(Integer.parseInt(TfSpecie.getText()));
	//		bird.setMutations(Integer.parseInt(TfMutation.getText()));
	//		bird.setCage(cageRepository.getCage(Integer.parseInt(TfCage.getText())));
	//		bird.setBreeder(Integer.parseInt(TfBreeder.getText()));
	//		bird.setPosture(Integer.parseInt(TfPosture.getText()));
			birdsRepository.Insert(bird);
		}
	}
	
	public boolean validate() {
		boolean validate= false;
		if (CbCriador.getValue()==null) {
			CbCriador.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Criador tem de ser escolhido");
			validate=false;
		}else {
			CbCriador.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		
		if (validate) {
			if (!TfAno.getText().matches("^\\d{4}$")) {
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Ano nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}else {
				TfAno.setStyle(null);
				LabelError.setStyle("");
				validate=true;
			}
		}
		
		
		if (validate) {
			if (!TfNumero.getText().matches("^\\d+$")) {
				TfNumero.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Numero nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}else {
				TfNumero.setStyle(null);
				LabelError.setStyle("");
				validate=true;
			}
		}
		
		if (validate) {
			try {
				DfDataEntrada.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				DfDataEntrada.setStyle(null);
				LabelError.setStyle("");
				validate=true;
			} catch (Exception e) {
				DfDataEntrada.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Ano nao esta no formato correto ou tem de ser preenchido");
				validate=false;
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
