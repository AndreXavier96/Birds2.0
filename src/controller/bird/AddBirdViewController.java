package controller.bird;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import constants.MyValues;
import constants.PathsConstants;
import constants.Regex;
import domains.Bird;
import domains.Breeder;
import domains.Cage;
import domains.Club;
import domains.Historic;
import domains.Mutation;
import domains.Specie;
import domains.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class AddBirdViewController implements Initializable {
	
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
	private TextField TfFather,TfMother;
	@FXML
	private ListView<Bird> LvFathers,LvMothers;
	
	@FXML
	private TextField TfAnilha, TfAno, TfBuyPrice;
	@FXML
	private TextArea TfObs;
	@FXML
	private DatePicker DfDataEntrada;
	@FXML
	private Label LabelAlert,LbImagePath;
	@FXML
	private AnchorPane ApBuyPrice;
	@FXML
	private ImageView ImImage;
	@FXML
	private Button btnUpload;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private BreederRepository breederRepository = new BreederRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private MutationsRepository mutationsRepository = new MutationsRepository();
	private CageRepository cageRepository = new CageRepository();
	private StateRepository stateRepository=new StateRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	private Club clubBand=null;
	private boolean imageUploaded=false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	ObservableList<Breeder> breeders = FXCollections.observableArrayList();
    	ObservableList<Specie> species = FXCollections.observableArrayList();
    	ObservableList<Cage> cages = FXCollections.observableArrayList();
		try {
			breeders=breederRepository.getAllBreeders();
			CbCriador.setItems(breeders);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		CbCriador.setConverter(new StringConverter<Breeder>() {
			 public String toString(Breeder b) {
				 return b.getName();
			 }
			 
			 public Breeder fromString(String s) {
				 return CbCriador.getItems().stream().filter(b -> b.getName().equals(s)).findFirst().orElse(null);
			 }
			 
		});
		CbState.setItems(MyValues.STARTING_STATE_LIST);
		CbEntryType.setItems(MyValues.ENTRYTYPELIST);
		CbEntryType.valueProperty().addListener((observable, oldValue, newValue) -> {
		      if (MyValues.ENTRYTYPELIST.get(0).equals(newValue)) {
		    	  TfBuyPrice.setVisible(true);
		    	  ApBuyPrice.setVisible(true);
		      } else {
		    	  TfBuyPrice.setVisible(false);
		    	  ApBuyPrice.setVisible(false);
		      }
		    });
		CbSex.setItems(MyValues.SEXLIST);
		try {
			species=speciesRepository.getAllSpecies();
			CbSpecies.setItems(species);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		        	Mutation normal = new Mutation(null, "Sem Mutacao","", "", null, null, newValue);
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
		        
		        TfFather.setDisable(false);
		        EventHandler<MouseEvent> showAllFathersEvent = event -> {
		            String searchTerm = TfFather.getText();
					ObservableList<Bird> listFathers = filterBirdsFather(searchTerm);
					LvFathers.setItems(listFathers);
					LvFathers.setVisible(true); // Show the ListView
		        };
		        TfFather.setOnMouseClicked(showAllFathersEvent);
		        TfFather.textProperty().addListener((observableFather, oldValueFather, newValueFather) -> {
		            ObservableList<Bird> listFathers = filterBirdsFather(newValueFather);
					LvFathers.setItems(listFathers);
					LvFathers.setVisible(true); // Show the ListView
		        });
		        
		        LvFathers.setCellFactory(param -> new ListCell<Bird>() {
		            @Override
		            protected void updateItem(Bird bird, boolean empty) {
		                super.updateItem(bird, empty);
		                if (empty || bird == null) {
		                    setText(null);
		                } else {
		                    setText(bird.getBand());
		                }
		            }
		        });
		        LvMothers.setCellFactory(param -> new ListCell<Bird>() {
		            @Override
		            protected void updateItem(Bird bird, boolean empty) {
		                super.updateItem(bird, empty);
		                if (empty || bird == null) {
		                    setText(null);
		                } else {
		                    setText(bird.getBand());
		                }
		            }
		        });
		    	
		        TfMother.setDisable(false);
		        EventHandler<MouseEvent> showAllMothersEvent = event -> {
		            String searchTerm = TfMother.getText();
					ObservableList<Bird> listMothers = filterBirdsMother(searchTerm);
					LvMothers.setItems(listMothers);
					LvMothers.setVisible(true); // Show the ListView
		        };
		        TfMother.setOnMouseClicked(showAllMothersEvent);
		        TfMother.textProperty().addListener((observableMother, oldValueMother, newValueMother) -> {
		            ObservableList<Bird> listMothers = filterBirdsMother(newValueMother);
		            LvMothers.setItems(listMothers);
		            LvMothers.setVisible(true); // Show the ListView
		        });
		        
		        TfFather.focusedProperty().addListener((observableFocusFather, oldValueFocusFather, newValueFocusFather) -> {
		            if (!newValueFocusFather) {
		                LvFathers.setVisible(false); // Hide the ListView when TfFather loses focus
		            }
		        });		        
		        LvFathers.focusedProperty().addListener((observableFocusLvFathers, oldValueFocusLvFathers, newValueFocusLvFathers) -> {
		            if (!newValueFocusLvFathers) {
		                LvFathers.setVisible(false); // Hide the ListView when LvFathers loses focus
		            }
		        });
		        
		        TfMother.focusedProperty().addListener((observableFocusMother, oldValueFocusMother, newValueFocusMother) -> {
		            if (!newValueFocusMother) {
		            	LvMothers.setVisible(false); // Hide the ListView when TfFather loses focus
		            }
		        });		        
		        LvMothers.focusedProperty().addListener((observableFocusLvMothers, oldValueFocusLvMothers, newValueFocusLvMothers) -> {
		            if (!newValueFocusLvMothers) {
		            	LvMothers.setVisible(false); // Hide the ListView when LvFathers loses focus
		            }
		        });
		        
		        LvFathers.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvFathers, oldValueSelectionLvFathers, newValueSelectionLvFathers) -> {
		            if (newValueSelectionLvFathers != null) {
		                TfFather.setText(newValueSelectionLvFathers.getBand());
		                LvFathers.setVisible(false); // Hide the ListView after selecting a value
		            }
		        });
		        LvMothers.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvMothers, oldValueSelectionLvMothers, newValueSelectionLvMothers) -> {
		            if (newValueSelectionLvMothers != null) {
		                TfMother.setText(newValueSelectionLvMothers.getBand());
		                LvMothers.setVisible(false); // Hide the ListView after selecting a value
		            }
		        });
		        
		    }
		});
		try {
			cages=cageRepository.getAllCages();
			CbCage.setItems(cages);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		
		if (breeders.isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_INFO);
			LabelAlert.setText("Para criar um passaro necessita de criar uma criador antes");
		}else if (species.isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_INFO);
			LabelAlert.setText("Para criar um passaro necessita de criar uma especie antes");
		}else if (cages.isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_INFO);
			LabelAlert.setText("Para criar um passaro necessita de criar uma gaiola antes");
		}
	}

	private ObservableList<Bird> filterBirdsFather(String searchTerm) {
	    try {
	        ObservableList<Bird> listFathers = birdsRepository.getAllWhereStringAndInteger("Sex", MyValues.MACHO, "SpeciesId", CbSpecies.getValue().getId());
	        Bird defaultFather = new Bird(null, null, MyValues.SEM_PAI, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	        listFathers.add(0, defaultFather);
	        List<Bird> filteredBirds = listFathers.stream()
	                .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
	                .collect(Collectors.toList());
	        return FXCollections.observableArrayList(filteredBirds);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return FXCollections.observableArrayList(); // Return an empty list in case of an exception
	}
	  
	  private ObservableList<Bird> filterBirdsMother(String searchTerm) {
			try {
				ObservableList<Bird> listMothers = birdsRepository.getAllWhereStringAndInteger("Sex",MyValues.FEMEA,"SpeciesId",CbSpecies.getValue().getId());
		  		Bird defaultMother = new Bird(null, null, MyValues.SEM_MAE, null, null, null, null, null,null, null, null, null, null, null, null, null,null);
		  		listMothers.add(0,defaultMother);
		        List<Bird> filteredBirds = listMothers.stream()
		                .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		                .collect(Collectors.toList());
		        return FXCollections.observableArrayList(filteredBirds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return FXCollections.observableArrayList();
	    }
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {//TODO validator TfFather and TfMother
			Bird bird = new Bird();
			bird.setBreeder(breederRepository.getBreederbyId(CbCriador.getValue().getId()));
			bird.setYear(Integer.parseInt(TfAno.getText()));
			bird.setEntryDate(Date.from(DfDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			bird.setEntryType(CbEntryType.getValue().toString());
			if (bird.getEntryType().equals(MyValues.COMPRA))
				bird.setBuyPrice(Double.parseDouble(TfBuyPrice.getText()));
			else if (bird.getEntryType().equals(MyValues.NASCIMENTO)) 
				bird.setBuyPrice(0.0);
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(DfDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			bird.setState(stateRepository.insertState(new State(null, CbState.getValue(), date, null, null)));
			bird.setSex(CbSex.getValue());
			if (!TfFather.getText().equals(MyValues.SEM_PAI))
				bird.setFather(birdsRepository.getBirdWhereString("Band", TfFather.getText()));
			if (!TfMother.getText().equals(MyValues.SEM_MAE))
				bird.setMother(birdsRepository.getBirdWhereString("Band", TfMother.getText()));
			bird.setSpecies(speciesRepository.getSpecieById(CbSpecies.getValue().getId()));
			if (CbMutation.getValue().getId() != null)
				bird.setMutations(mutationsRepository.getMutationsById(CbMutation.getValue().getId()));
			else
				bird.setMutations(null);
			bird.setCage(cageRepository.getCage(CbCage.getValue().getId()));
			
			String anilha = TfAnilha.getText().toUpperCase();
			if (validateBand(anilha)) {
				bird.setBand(anilha);
				if (imageUploaded) {
					File defaultFolder = new File(PathsConstants.DEFAULT_PATH_TO_SAVE_IMAGE);
					File selectedFile = new File(LbImagePath.getText());
					try {
						String fileName = anilha+ selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
						Files.copy(selectedFile.toPath(), defaultFolder.toPath().resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
						bird.setImage("file:" + defaultFolder + "\\" + fileName);
					} catch (IOException e) {
						System.out.println(e);
					}
				}
				bird.setObs(TfObs.getText());
				Integer BirdId = birdsRepository.Insert(bird);
				bird.setId(BirdId);
				String obs = "";
				String formatedDate = new SimpleDateFormat(MyValues.DATE_FORMATE).format(bird.getEntryDate());
				if (bird.getEntryType().equals(MyValues.COMPRA))
					obs = "Passaro comprado por " + bird.getBuyPrice() + " euros .";
				else if (CbEntryType.getValue().equals(MyValues.NASCIMENTO))
					obs = "Passaro nascido a " + formatedDate;
				System.out.println(formatedDate);
				historicRepository.insertHistoric(new Historic(MyValues.BIRD_INSERTED, formatedDate, obs, bird));
				LabelAlert.setStyle(MyValues.ALERT_SUCESS);
				LabelAlert.setText("Passaro " + bird.getBand() + " inserido com sucesso!");
				clearAllFields();
			}
		}
	}
	
	public boolean validateBand(String band) throws SQLException {
		boolean validate = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (birdsRepository.checkIfExistsWithBand(band)) {
			TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha " + band + " ja existe!");
			validate = false;
		} else {
			TfAnilha.setStyle(null);
			LabelAlert.setText("");
			validate = true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate= false;
		clubBand=null;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");		//0		1	2	3	4  5
		String[] infoAnilha = null; //CLUB STAM 123 PT FED 23
		if (CbCriador.getValue()==null) {
			CbCriador.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Criador tem de ser escolhido");
			validate=false;
		}else {
			CbCriador.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate)
			if (!TfAnilha.getText().matches(Regex.FULL_BAND)) {
				TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}else {
				TfAnilha.setStyle(null);
				LabelAlert.setText("");
				infoAnilha=TfAnilha.getText().split(" ");
				validate=true;
			}
		
		if (validate && infoAnilha.length>0) {
			String ano;
			if (TfAno.getText().length()>2) 
				ano =TfAno.getText().substring(2);
			else 
				ano = TfAno.getText();
			
			if (!TfAno.getText().matches(Regex.ANO)) {
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Ano nao esta no formato correto ou tem de ser preenchido. ex:2023");
				validate = false;
			} else if (!ano.equals(infoAnilha[5])) {
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Ano nao coincide com ano da anilha");
				validate = false;
			} else {
				TfAno.setStyle(null);
				LabelAlert.setText("");
				validate = true;
			}
		}
		
		if (validate) {// validate clubs in band
			List<Integer> clubs = breederClubRepository.getClubsFromBreederId(CbCriador.getValue().getId());
			validate = false;
			for (Integer c : clubs) {
				Club club = clubRepository.getClubByID(c);
				if (club.getAcronym().equals(infoAnilha[0])) {
					validate = true;
					clubBand = club;
				}
			}
			if (!validate) {
				TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Clube da anilha nao existe");
			}
		}
		
		if (validate) {//validate stam in band
			String stam = breederFederationRepository.getStamByBreederAndFederationId(CbCriador.getValue().getId(), clubBand.getFederation().getId());
			validate=false;
			if (stam.equals(infoAnilha[1])) {
				validate=true;
			}
			if (!validate) {
				TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("STAM da anilha nao pertence ao criador selecionado");
			}
		}
		
		if (validate) {//validate fed in band
			validate=false;
			if (clubBand.getFederation().getAcronym().equals(infoAnilha[4])) {
				validate=true;
			}
			if (!validate) {
				TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Federacao da anilha nao pertence ao criador selecionado");
			}
		}
		
		if (validate) {//validate country in band
			validate=false;
			if (clubBand.getFederation().getCountry().equals(infoAnilha[3])) {
				validate=true;
			}
			if (!validate) {
				TfAnilha.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Pais da anilha nao pertence ao criador selecionado");
			}
		}
		
		if (validate) 
			if (CbSex.getValue()==null) {
				CbSex.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Sexo tem de ser escolhido");
				validate=false;
			}else {
				CbSex.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate) 
			if (CbSpecies.getValue()==null) {
				CbSpecies.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Especie tem de ser escolhido");
				validate=false;
			}else {
				CbSpecies.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate) 
			if (CbSpecies.getValue()!=null && CbMutation.getValue()==null) {
				CbMutation.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Mutacao tem de ser escolhido");
				validate=false;
			}else {
				CbMutation.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate) 
			if (CbCage.getValue()==null) {
				CbCage.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Gaiola tem de ser escolhido");
				validate=false;
			}else {
				CbCage.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate)
			if (TfFather.getText()==null) {
				TfFather.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Pai tem de ser escolhido.(Caso nao tenha Pai escolher 'Sem Pai')");
				validate=false;
			}else if (birdsRepository.getBirdWhereString("Band", TfFather.getText())==null && !TfFather.getText().equals(MyValues.SEM_PAI)) {
				TfFather.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha de Pai nao existe");
				validate=false;
			}else {
				TfFather.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate)
			if (TfMother.getText()==null) {
				TfMother.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Mae tem de ser escolhida.(Caso nao tenha Mae escolher 'Sem Mae')");
				validate=false;
			}else if (birdsRepository.getBirdWhereString("Band", TfMother.getText())==null && !TfMother.getText().equals(MyValues.SEM_MAE)) {
				TfMother.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha de Mae nao existe");
				validate=false;
			}else {
				TfMother.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate)
			try {
				DfDataEntrada.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				DfDataEntrada.setStyle(null);
				validate=true;
			} catch (Exception e) {
				DfDataEntrada.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Data entrada nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}
		
		if (validate) {
			if (CbEntryType.getValue()==null) {
				CbEntryType.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Tipo de entrada tem de ser escolhido");
				validate=false;
			}else {
				CbEntryType.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		}
		
		if (validate)
			if (CbState.getValue()==null) {
				CbState.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Estado tem de ser escolhido");
				validate=false;
			}else {
				CbState.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate) 
			if (CbEntryType.getValue().equals(MyValues.ENTRYTYPELIST.get(0))) 
				if (!TfBuyPrice.getText().matches(Regex.DOUBLES)) {
					TfBuyPrice.setStyle(MyValues.ERROR_BOX_STYLE);
					LabelAlert.setText("Preco compra tem de ser escolhido");
					validate=false;
				}else {
					TfBuyPrice.setStyle(null);
					LabelAlert.setText("");
					validate=true;
				}
		
		if (validate)
			if (TfObs.getText().length()>500) {
				TfObs.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Observacoes so pode ter no maximo 500 caracteres.");
				validate=false;
			}else {
				TfObs.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		if(validate && !LbImagePath.getText().isBlank()) {
			String sufix = LbImagePath.getText().substring(LbImagePath.getText().lastIndexOf(".")+1);
			if (sufix.toLowerCase().equals("png") || sufix.toLowerCase().equals("jpg")) {
				btnUpload.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}else {
				btnUpload.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Imagem tem de ser .jpg ou .png");
				validate=false;
			}
		}
		return validate;
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.close(); 
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
	
	public void clearAllErrors() {
		CbCriador.setStyle(null);
		CbEntryType.setStyle(null);
		DfDataEntrada.setStyle(null);
		CbSex.setStyle(null);
		CbSpecies.setStyle(null);
		CbMutation.setStyle(null);
		CbCage.setStyle(null);
		TfFather.setStyle(null);
		TfMother.setStyle(null);
		TfAno.setStyle(null);
		TfAnilha.setStyle(null);
		TfBuyPrice.setStyle(null);
		CbState.setStyle(null);
		TfObs.setStyle(null);
		btnUpload.setStyle(null);
	}
	
	public void clearAllFields() {
		clubBand=null;
		CbCriador.setValue(null);
		CbEntryType.setValue(null);
		DfDataEntrada.setValue(null);
		CbSex.setValue(null);
		CbSpecies.setValue(null);
		CbMutation.setValue(null);
		CbCage.setValue(null);
		TfFather.setText("");
		TfMother.setText("");
		TfAno.setText("");
		TfAnilha.setText("");
		TfBuyPrice.setText("");
		CbState.setValue(null);
		TfObs.setText("");
    	ImImage.setImage(PathsConstants.DEFAULT_IMAGE);
		clearAllErrors();
	}
	
}
