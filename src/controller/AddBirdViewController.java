package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.BirdsRepository;
import repository.BreederRepository;
import repository.MutationsRepository;
import repository.SpeciesRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Bird;
import domains.Breeder;
import domains.Mutation;
import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private ComboBox<Specie> CbSpecies;
	
	@FXML
	private ComboBox<Mutation> CbMutation;
	
	@FXML
	private ComboBox<String> CbEntryType, CbState, CbSex, CbFather, CbMother, CbCage;

	@FXML
	private TextField TfAno, TfNumero, TfBuyPrice, TfSellPrice;
	
	@FXML
	private DatePicker DfDataEntrada;
	
	@FXML
	private TextField TfBreeder,TfPosture;
	
	
	@FXML
	private Label LabelAnilha, LabelError, labelTfBuyPrice;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private BreederRepository breederRepository = new BreederRepository();
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private MutationsRepository mutationsRepository = new MutationsRepository();
	
	private ObservableList<String> entryTypeList = FXCollections.observableArrayList("Compra","Nascimento");
	private ObservableList<String> stateList = FXCollections.observableArrayList("Vivo","Morto","Vendido");
	private ObservableList<String> sexList = FXCollections.observableArrayList("Femea","Macho");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbCriador.setItems(breederRepository.getAllBreeders());
		CbCriador.setConverter(new StringConverter<Breeder>() {
			 public String toString(Breeder b) {
				 return b.getName();
			 }
			 
			 public Breeder fromString(String s) {
				 return CbCriador.getItems().stream().filter(b -> b.getName().equals(s)).findFirst().orElse(null);
			 }
			 
		});
		CbEntryType.setItems(entryTypeList);
		CbEntryType.valueProperty().addListener((observable, oldValue, newValue) -> {
		      if (entryTypeList.get(0).equals(newValue)) {
		    	  TfBuyPrice.setVisible(true);
		    	  labelTfBuyPrice.setVisible(true);
		      } else {
		    	  TfBuyPrice.setVisible(false);
		    	  labelTfBuyPrice.setVisible(false);
		      }
		    });
		CbState.setItems(stateList);
		CbSex.setItems(sexList);
		CbSpecies.setItems(speciesRepository.getAllSpecies());
		CbSpecies.setConverter(new StringConverter<Specie>() {
			public String toString(Specie s) {
				return s.getCommonName();
			}
			public Specie fromString(String s) {
				return CbSpecies.getItems().stream().filter(b -> b.getCommonName().equals(s)).findFirst().orElse(null);
			}
		});
		CbSpecies.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
		        try {
					CbMutation.setItems(mutationsRepository.getMutationsBySpecie(newValue.getId()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        CbMutation.setDisable(false);
		        CbMutation.setConverter(new StringConverter<Mutation>() {
		            public String toString(Mutation m) {
		                return m.getName();
		            }
		            public Mutation fromString(String s) {
		                return CbMutation.getItems().stream().filter(b -> b.getName().equals(s)).findFirst().orElse(null);
		            }
		        });
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
			bird.setEntryType(CbEntryType.getValue().toString());
			bird.setBuyPrice(Double.parseDouble(TfBuyPrice.getText()));
			bird.setSellPrice(Double.parseDouble(TfSellPrice.getText()));
			bird.setState(CbState.getValue());
			bird.setSex(CbSex.getValue());
	//		bird.setFather(birdsRepository.getBird(Integer.parseInt(TfFather.getText())));
	//		bird.setMother(birdsRepository.getBird(Integer.parseInt(TfMother.getText())));
			bird.setSpecies(CbSpecies.getValue());
			bird.setMutations(CbMutation.getValue());
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
		
		if (validate) {
			if (CbEntryType.getValue()==null) {
				CbEntryType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Tipo de entrada tem de ser escolhido");
				validate=false;
			}else {
				CbEntryType.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		}
		
		if (validate) 
			if (CbEntryType.getValue().equals(entryTypeList.get(0))) 
				if (!TfBuyPrice.getText().matches("^[+]?[0-9]*[.]?[0-9]+$")) {
					TfBuyPrice.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Preco compra tem de ser escolhido");
					validate=false;
				}else {
					TfBuyPrice.setStyle(null);
					LabelError.setText("");
					validate=true;
				}
		
		if (validate)
				if (!TfSellPrice.getText().matches("^[+]?[0-9]*[.]?[0-9]+$|^$")) {
					TfSellPrice.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelError.setText("Preco venda tem formato incorreto");
					validate=false;
				}else {
					TfSellPrice.setStyle(null);
					LabelError.setText("");
					validate=true;
				}
		
		if (validate) 
			if (CbState.getValue()==null) {
				CbState.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Estado tem de ser escolhido");
				validate=false;
			}else {
				CbState.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		
		if (validate) 
			if (CbSex.getValue()==null) {
				CbSex.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Estado tem de ser escolhido");
				validate=false;
			}else {
				CbSex.setStyle(null);
				LabelError.setText("");
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
}
