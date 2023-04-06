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
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.Regex;
import domains.Mutation;
import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class AddMutationViewController implements Initializable {
	
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	@FXML
	private Label labelAlert,LbTitle;
	@FXML
	private TextField TfName, TfVar1,TfVar2,TfVar3;
	@FXML
	private TextArea TfObs;
	@FXML
	private ComboBox<Specie> CbSpecie;
	@FXML
	private Button btnAdd,btnEdit,btnBack,btnClose;
	
	Mutation mutation=null;

	MutationsRepository mutationsRepository = new MutationsRepository();
	SpeciesRepository speciesRepository = new SpeciesRepository();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Specie> species = FXCollections.observableArrayList();
		try {
			species=speciesRepository.getAllSpecies();
			CbSpecie.setItems(species);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CbSpecie.setConverter(new StringConverter<Specie>() {
			 public String toString(Specie s) {
				 return s.getCommonName();
			 }
			 
			 public Specie fromString(String s) {
				 return CbSpecie.getItems().stream().filter(b -> b.getCommonName().equals(s)).findFirst().orElse(null);
			 }
		});
		if (species.isEmpty()) {
			labelAlert.setStyle(MyValues.ALERT_INFO);
			labelAlert.setText("Para criar uma mutacao necessita de criar uma especie antes");
		}
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
			Mutation mutation = new Mutation();
			mutation.setName(TfName.getText());
			mutation.setVar1(TfVar1.getText());
			mutation.setVar2(TfVar2.getText());
			mutation.setVar3(TfVar3.getText());
			mutation.setObs(TfObs.getText());
			mutation.setSpecie(CbSpecie.getValue());
			mutationsRepository.Insert(mutation);
			labelAlert.setStyle(MyValues.ALERT_SUCESS);
			labelAlert.setText("Mutacao " + mutation.getName() + " inserida com sucesso!");
			clearAllFields();
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws SQLException {
		if (validatorEdit(mutation)) {
			mutation.setName(TfName.getText());
			mutation.setVar1(TfVar1.getText());
			mutation.setVar2(TfVar2.getText());
			mutation.setVar3(TfVar3.getText());
			mutation.setObs(TfObs.getText());
			mutation.setSpecie(CbSpecie.getValue());
			mutationsRepository.updateMutation(mutation);
			labelAlert.setStyle(MyValues.ALERT_SUCESS);
			labelAlert.setText(mutation.getName()+" alterada com sucesso!");
			clearAllFields();
			stage = (Stage) btnEdit.getScene().getWindow();
			stage.close();
		}
	}
	
	public boolean validatorEdit(Mutation mutation) throws SQLException {
		boolean validate = false;
		clearAllErrors();
		labelAlert.setStyle(MyValues.ALERT_ERROR);
		labelAlert.setText("");
		
		 if(CbSpecie.getValue()==null){
			 CbSpecie.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Especie tem de ser escolhida");
			validate=false;
		}else {
			CbSpecie.setStyle(null);
			labelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfName.getText().length()==0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome tem de ser preenchido");
				validate=false;
			}else if(!TfName.getText().matches(Regex.NAME)){
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome nao esta no formato correto");
				validate=false;
			}else if(mutationsRepository.mutationExistsForSpecie(TfName.getText(),CbSpecie.getValue().getId().toString(),mutation.getId())){
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome ja existe para esta especie");
				validate=false;
			}else {
				TfName.setStyle(null);
				labelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (TfVar1.getText().length()!=0)
				if(!TfVar1.getText().matches(Regex.ALL_TEXT)){
					TfVar1.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao1 nao esta no formato correto");
					validate=false;
				}else {
					TfVar1.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		if (validate) {
			if (TfVar2.getText().length()!=0)
				if(!TfVar2.getText().matches(Regex.ALL_TEXT)){
					TfVar2.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao2 nao esta no formato correto");
					validate=false;
				}else {
					TfVar2.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		if (validate) {
			if (TfVar3.getText().length()!=0)
				if(!TfVar3.getText().matches(Regex.ALL_TEXT)){
					TfVar3.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao3 nao esta no formato correto");
					validate=false;
				}else {
					TfVar3.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		
		if (validate)
			if (TfObs.getText().length()>500) {
				TfObs.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Observacoes so pode ter no maximo 500 caracteres.");
				validate=false;
			}else {
				TfObs.setStyle(null);
				labelAlert.setText("");
				validate=true;
			}
		
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		clearAllErrors();
		labelAlert.setStyle(MyValues.ALERT_ERROR);
		labelAlert.setText("");
		
		 if(CbSpecie.getValue()==null){
			 CbSpecie.setStyle(MyValues.ERROR_BOX_STYLE);
			labelAlert.setText("Especie tem de ser escolhida");
			validate=false;
		}else {
			CbSpecie.setStyle(null);
			labelAlert.setText("");
			validate=true;
		}
		
		if (validate) {
			if (TfName.getText().length()==0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome tem de ser preenchido");
				validate=false;
			}else if(!TfName.getText().matches(Regex.NAME)){
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome nao esta no formato correto");
				validate=false;
			}else if(mutationsRepository.mutationExistsForSpecie(TfName.getText(),CbSpecie.getValue().getId().toString())){
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Nome ja existe para esta especie");
				validate=false;
			}else {
				TfName.setStyle(null);
				labelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (TfVar1.getText().length()!=0)
				if(!TfVar1.getText().matches(Regex.ALL_TEXT)){
					TfVar1.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao1 nao esta no formato correto");
					validate=false;
				}else {
					TfVar1.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		if (validate) {
			if (TfVar2.getText().length()!=0)
				if(!TfVar2.getText().matches(Regex.ALL_TEXT)){
					TfVar2.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao2 nao esta no formato correto");
					validate=false;
				}else {
					TfVar2.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		if (validate) {
			if (TfVar3.getText().length()!=0)
				if(!TfVar3.getText().matches(Regex.ALL_TEXT)){
					TfVar3.setStyle(MyValues.ERROR_BOX_STYLE);
					labelAlert.setText("Variacao3 nao esta no formato correto");
					validate=false;
				}else {
					TfVar3.setStyle(null);
					labelAlert.setText("");
					validate=true;
				}
		}
		
		if (validate)
			if (TfObs.getText().length()>500) {
				TfObs.setStyle(MyValues.ERROR_BOX_STYLE);
				labelAlert.setText("Observacoes so pode ter no maximo 500 caracteres.");
				validate=false;
			}else {
				TfObs.setStyle(null);
				labelAlert.setText("");
				validate=true;
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
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
	
	public void startValuesEdit(Mutation mutation) throws SQLException {
		LbTitle.setText("Editar "+mutation.getName());
		TfName.setText(mutation.getName());
		TfVar1.setText(mutation.getVar1());
		TfVar2.setText(mutation.getVar2());
		TfVar3.setText(mutation.getVar3());
		TfObs.setText(mutation.getObs());
		btnAdd.setVisible(false);
		btnAdd.setDefaultButton(false);
		btnEdit.setDefaultButton(true);
		btnEdit.setVisible(true);
		btnBack.setVisible(false);
		btnClose.setVisible(true);
		CbSpecie.setValue(mutation.getSpecie());
		CbSpecie.setDisable(true);
		this.mutation=mutation;
	}
	
	public void clearAllErrors() {
		TfName.setStyle(null);
		TfVar1.setStyle(null);
		TfVar2.setStyle(null);
		TfVar3.setStyle(null);
		TfObs.setStyle(null);
		CbSpecie.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText("");
		TfVar1.setText("");
		TfVar2.setText("");
		TfVar3.setText("");
		TfObs.setText("");
		CbSpecie.setValue(null);
		clearAllErrors();
	}

}
