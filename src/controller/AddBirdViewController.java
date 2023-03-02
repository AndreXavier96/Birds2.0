package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.BirdsRepository;
import repository.BreederClubRepository;
import repository.BreederFederationRepository;
import repository.BreederRepository;
import repository.CageRepository;
import repository.ClubRepository;
import repository.HistoricRepository;
import repository.MutationsRepository;
import repository.SpeciesRepository;
import repository.StateRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import constants.MyValues;
import domains.Bird;
import domains.Breeder;
import domains.Cage;
import domains.Club;
import domains.Federation;
import domains.Historic;
import domains.Mutation;
import domains.Specie;
import domains.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private ComboBox<Cage> CbCage;
	@FXML
	private ComboBox<String> CbEntryType, CbSex, CbState;
	@FXML
	private ComboBox<Bird> CbFather,CbMother;
	@FXML
	private TextField TfAno, TfNumero, TfBuyPrice;
	@FXML
	private DatePicker DfDataEntrada;
	@FXML
	private Label LabelAnilha, LabelError, labelTfBuyPrice,LbImagePath;
	@FXML
	private AnchorPane ApStam;
	@FXML
	private ComboBox<Club> CbClub;
	@FXML
	private ImageView ImImage;
	@FXML
	private Button btnUpload;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private BreederRepository breederRepository = new BreederRepository();
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private MutationsRepository mutationsRepository = new MutationsRepository();
	private CageRepository cageRepository = new CageRepository();
	private StateRepository stateRepository=new StateRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	private boolean imageUploaded=false;
	
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
		CbCriador.valueProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		        try {
		        	ObservableList<Club> clubList = FXCollections.observableArrayList();
		        	for (Integer i : breederClubRepository.getClubsFromBreederId(newValue.getId())) {
						clubList.add(clubRepository.getClubByID(i));
					}
		            CbClub.setItems(clubList);
		            ApStam.setVisible(true);
		            CbClub.setConverter(new StringConverter<Club>() {
		                public String toString(Club c) {
		                    return c.getAcronym();
		                }
		                public Club fromString(String s) {
		                    return CbClub.getItems().stream().filter(c -> c.getAcronym().equals(s)).findFirst().orElse(null);
		                }
		            });
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});
		
		CbState.setItems(MyValues.STARTING_STATE_LIST);
		
		CbEntryType.setItems(MyValues.ENTRYTYPELIST);
		CbEntryType.valueProperty().addListener((observable, oldValue, newValue) -> {
		      if (MyValues.ENTRYTYPELIST.get(0).equals(newValue)) {
		    	  TfBuyPrice.setVisible(true);
		    	  labelTfBuyPrice.setVisible(true);
		      } else {
		    	  TfBuyPrice.setVisible(false);
		    	  labelTfBuyPrice.setVisible(false);
		      }
		    });
		CbSex.setItems(MyValues.SEXLIST);
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
		        	ObservableList<Mutation> listMutations = mutationsRepository.getMutationsBySpecie(newValue.getId());
		        	Mutation normal = new Mutation(null, "Sem Mutacao","","", "", newValue);
		        	listMutations.add(0,normal);
		        	CbMutation.setItems(listMutations);
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
		CbCage.setItems(cageRepository.getAllCages());
		CbCage.setConverter(new StringConverter<Cage>() {
			@Override
			public String toString(Cage s) {
				return s.getCode();
			}
			@Override
			public Cage fromString(String s) {
				return CbCage.getItems().stream().filter(b -> b.getCode().equals(s)).findFirst().orElse(null);
			}
		});
		ObservableList<Bird> listFathers = birdsRepository.getAllWhere("Sex",MyValues.FEMEA);
		Bird defaultFather = new Bird(null, null, MyValues.SEM_PAI, null, null, null, null, null, null, null, null, null, null, null, null, null,null);
		listFathers.add(0,defaultFather);
		CbFather.setItems(listFathers);
		CbFather.setConverter(new StringConverter<Bird>() {
			@Override
			public String toString(Bird s) {
				return s.getBand();
			}
			@Override
			public Bird fromString(String s) {
				return CbFather.getItems().stream().filter(b -> b.getBand().equals(s)).findFirst().orElse(null);
			}
		});
		ObservableList<Bird> listMothers = birdsRepository.getAllWhere("Sex",MyValues.MACHO);
		Bird defaultMother = new Bird(null, null, MyValues.SEM_MAE, null, null, null, null, null, null, null, null, null, null, null, null, null,null);
		listMothers.add(0,defaultMother);
		CbMother.setItems(listMothers);
		CbMother.setConverter(new StringConverter<Bird>() {
			@Override
			public String toString(Bird s) {
				return s.getBand();
			}
			@Override
			public Bird fromString(String s) {
				return CbMother.getItems().stream().filter(b -> b.getId().equals(s)).findFirst().orElse(null);
			}
		});
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if(validate()) {
			Bird bird = new Bird();
			bird.setBreeder(breederRepository.getBreederbyId(CbCriador.getValue().getId()));
			bird.setYear(Integer.parseInt(TfAno.getText()));
			bird.setEntryDate(Date.from(DfDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			bird.setEntryType(CbEntryType.getValue().toString());
			if (bird.getEntryType().equals(MyValues.COMPRA)) {
				bird.setBuyPrice(Double.parseDouble(TfBuyPrice.getText()));
			}else if (bird.getEntryType().equals(MyValues.NASCIMENTO)) {
				bird.setBuyPrice(0.0);
			}
			bird.setSellPrice(0.0);
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(DfDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			bird.setState(stateRepository.insertState(new State(null, CbState.getValue(),date , null, null)));
			bird.setSex(CbSex.getValue());
			if (CbFather.getValue().getId()!=null)
				bird.setFather(birdsRepository.getBirdWhereInt("id",CbFather.getValue().getId()));
			if (CbMother.getValue().getId()!=null) 
				bird.setMother(birdsRepository.getBirdWhereInt("id",CbMother.getValue().getId()));
			bird.setSpecies(speciesRepository.getSpecieById(CbSpecies.getValue().getId()));
			if (CbMutation.getValue().getId()!=null)
				bird.setMutations(mutationsRepository.getMutationsById(CbMutation.getValue().getId()));
			else
				bird.setMutations(null);
			bird.setCage(cageRepository.getCage(CbCage.getValue().getId()));
			
			Integer breederID =bird.getBreeder().getId();
			Club club = CbClub.getValue();
			Federation federation = club.getFederation();
			String stam = breederFederationRepository.getStamByBreederAndFederationId(breederID,federation.getId());
			String anilha = club.getAcronym()+" "+stam+" "+TfNumero.getText()+" "+federation.getCountry()+" "+federation.getAcronym()+TfAno.getText();
			bird.setBand(anilha);
			
			if (imageUploaded) {
				File defaultFolder = new File("resources/images/birds");
				File selectedFile = new File (LbImagePath.getText());
				try {
					String fileName = anilha+selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
					Files.copy(selectedFile.toPath(), defaultFolder.toPath().resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
					bird.setImage("file:"+defaultFolder+"\\"+fileName);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
			Integer BirdId = birdsRepository.Insert(bird);
			bird.setId(BirdId);
			String obs="";
			if (bird.getEntryType().equals(MyValues.COMPRA)) {
				obs="Passaro comprado por "+bird.getBuyPrice()+"\\u20ac a "+bird.getEntryDate().toString();
			}else if(CbEntryType.getValue().equals(MyValues.NASCIMENTO)){
				obs="Passaro nascido a "+bird.getEntryDate().toString();
			}
			String formatedDate =new SimpleDateFormat(MyValues.DATE_FORMATE).format(bird.getEntryDate());
			historicRepository.insertHistoric(new Historic(null,MyValues.BIRD_INSERTED,formatedDate,obs, bird));
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
			if (!TfAno.getText().matches("^\\d{4}|\\d{2}$")) {
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Ano nao esta no formato correto ou tem de ser preenchido. ex:2022 ou 22");
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
			if (CbClub.getValue()==null) {
				CbClub.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Clube tem de ser escolhido");
				validate=false;
			}else {
				CbClub.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		}
		
		if (validate) {
			if (CbState.getValue()==null) {
				CbState.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Estado tem de ser escolhido");
				validate=false;
			}else {
				CbState.setStyle(null);
				LabelError.setText("");
				validate=true;
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
		
		if (validate) {
			try {
				DfDataEntrada.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				DfDataEntrada.setStyle(null);
				LabelError.setStyle("");
				validate=true;
			} catch (Exception e) {
				DfDataEntrada.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Data entrada nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}
		}
		
		if (validate) 
			if (CbEntryType.getValue().equals(MyValues.ENTRYTYPELIST.get(0))) 
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
			if (CbSex.getValue()==null) {
				CbSex.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Sexo tem de ser escolhido");
				validate=false;
			}else {
				CbSex.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		if (validate) 
			if (CbSpecies.getValue()==null) {
				CbSpecies.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Especie tem de ser escolhido");
				validate=false;
			}else {
				CbSpecies.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		if (validate) 
			if (CbSpecies.getValue()!=null && CbMutation.getValue()==null) {
				CbMutation.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Mutacao tem de ser escolhido");
				validate=false;
			}else {
				CbMutation.setStyle(null);
				LabelError.setText("");
				validate=true;
			}
		if (validate) 
			if (CbCage.getValue()==null) {
				CbCage.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Gaiola tem de ser escolhido");
				validate=false;
			}else {
				CbCage.setStyle(null);
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
	
	@FXML
	public void btnUploadImage(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(MyValues.TITLE_SELECT_IMAGE);
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files","*.png","*.jpg"));
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile!=null) {
				LbImagePath.setText(selectedFile.toPath().toString());
				ImImage.setImage(new Image(selectedFile.toURI().toString()));
				imageUploaded=true;
			}
	}
	
}
