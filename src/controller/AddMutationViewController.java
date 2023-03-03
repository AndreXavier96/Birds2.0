package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.MutationsRepository;
import repository.SpeciesRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Mutation;
import domains.Specie;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

public class AddMutationViewController implements Initializable {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label LabelError;
	@FXML
	private TextField TfName, TfType, TfSymbol;
	@FXML
	private ComboBox<Specie> CbSpecie;

	MutationsRepository mutationsRepository = new MutationsRepository();
	SpeciesRepository speciesRepository = new SpeciesRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbSpecie.setItems(speciesRepository.getAllSpecies());
		CbSpecie.setConverter(new StringConverter<Specie>() {
			 public String toString(Specie s) {
				 return s.getCommonName();
			 }
			 
			 public Specie fromString(String s) {
				 return CbSpecie.getItems().stream().filter(b -> b.getCommonName().equals(s)).findFirst().orElse(null);
			 }
			 
		});
		
	}
	

	@FXML
	public void btnAdd(ActionEvent event) {
		boolean validated = validator();
		if (validated) {
			Mutation mutation = new Mutation();
			mutation.setName(TfName.getText());
			mutation.setType(TfType.getText());
			mutation.setSymbol(TfSymbol.getText());
			mutation.setSpecie(CbSpecie.getValue());
			mutationsRepository.Insert(mutation);
		}
	}
	
	
	
	
	public boolean validator() {
		boolean validate = false;
		if (TfName.getText().length()==0) {
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome tem de ser preenchido");
			validate=false;
		}else if(!TfName.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")){
			TfName.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Nome nao esta no formato correto");
			validate=false;
		}else {
			TfName.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfType.getText().length()!=0)
				if(!TfType.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]| )+$")){
					TfType.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Tipo nao esta no formato correto");
					validate=false;
				}else {
					TfType.setStyle(null);
					LabelError.setText("");
					validate=true;
				}
		}
		
		if (validate) {
			if (TfSymbol.getText().length()!=0)
				if(!TfSymbol.getText().matches("^([a-zA-Z]|[à-ü]|[À-Ü]|[0-9]| )+$")){
					TfSymbol.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Simbolo nao esta no formato correto");
					validate=false;
				}else {
					TfSymbol.setStyle(null);
					LabelError.setText("");
					validate=true;
				}
		}
		
		if (validate) {
			 if(CbSpecie.getValue()==null){
				 CbSpecie.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Especie tem de ser escolhida");
				validate=false;
			}else {
				CbSpecie.setStyle(null);
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
