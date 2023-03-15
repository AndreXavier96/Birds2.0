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
import constants.Regex;
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
import javafx.scene.control.TextArea;

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
	private TextField TfAno, TfNumero, TfBuyPrice,TfBand;
	@FXML
	private TextArea TfObs;
	@FXML
	private DatePicker DfDataEntrada;
	@FXML
	private Label LabelAnilha, LabelAlert,LbImagePath;
	@FXML
	private ComboBox<Club> CbClub;
	@FXML
	private AnchorPane ApBuyPrice, ApBand,ApNumero,ApClub;
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
		ApClub.setVisible(false);
    	ApNumero.setVisible(false);
    	ApBand.setVisible(false);
    	
		try {
			CbCriador.setItems(breederRepository.getAllBreeders());
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
		CbCriador.valueProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null && newValue.getType().equals(MyValues.CRIADOR_PROFISSIONAL)) {
		    	ApClub.setVisible(true);
		    	ApNumero.setVisible(true);
		    	ApBand.setVisible(false);
		    	TfBand.setText("");
		        try {
		        	ObservableList<Club> clubList = FXCollections.observableArrayList();
		        	for (Integer i : breederClubRepository.getClubsFromBreederId(newValue.getId())) {
						clubList.add(clubRepository.getClubByID(i));
					}
		            CbClub.setItems(clubList);
		            CbClub.setDisable(false);
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
		    }else if (newValue !=null && !newValue.getType().equals(MyValues.CRIADOR_PROFISSIONAL)) {
		    	ApClub.setVisible(false);
		    	ApNumero.setVisible(false);
		    	ApBand.setVisible(true);
		    	CbClub.setValue(null);
		    	TfNumero.setText("");
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
			CbSpecies.setItems(speciesRepository.getAllSpecies());
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
		        	ObservableList<Bird> listFathers = birdsRepository.getAllWhereStringAndInteger("Sex",MyValues.MACHO,"SpeciesId",CbSpecies.getValue().getId());
		        	Bird defaultFather = new Bird(null, null, MyValues.SEM_PAI, null, null, null, null, null, null,null, null, null, null, null, null, null,null);
		    		listFathers.add(0,defaultFather);
		    		CbFather.setItems(listFathers);
		    		ObservableList<Bird> listMothers = birdsRepository.getAllWhereStringAndInteger("Sex",MyValues.FEMEA,"SpeciesId",CbSpecies.getValue().getId());
		    		Bird defaultMother = new Bird(null, null, MyValues.SEM_MAE, null, null, null, null, null,null, null, null, null, null, null, null, null,null);
		    		listMothers.add(0,defaultMother);
		    		CbMother.setItems(listMothers);
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
		        CbFather.setDisable(false);
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
		    	CbMother.setDisable(false);
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
		});
		try {
			CbCage.setItems(cageRepository.getAllCages());
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
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validator()) {
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
			if (CbFather.getValue().getId() != null)
				bird.setFather(birdsRepository.getBirdWhereInt("id", CbFather.getValue().getId()));
			if (CbMother.getValue().getId() != null)
				bird.setMother(birdsRepository.getBirdWhereInt("id", CbMother.getValue().getId()));
			bird.setSpecies(speciesRepository.getSpecieById(CbSpecies.getValue().getId()));
			if (CbMutation.getValue().getId() != null)
				bird.setMutations(mutationsRepository.getMutationsById(CbMutation.getValue().getId()));
			else
				bird.setMutations(null);
			bird.setCage(cageRepository.getCage(CbCage.getValue().getId()));
			Integer breederID = bird.getBreeder().getId();
			String anilha = "";
			String breederType = bird.getBreeder().getType();
			if (breederType.equals(MyValues.CRIADOR_PROFISSIONAL)) {
				Club club = CbClub.getValue();
				Federation federation = club.getFederation();
				String stam = breederFederationRepository.getStamByBreederAndFederationId(breederID,federation.getId());
				anilha = club.getAcronym() + " " + stam + " " + TfNumero.getText() + " " + federation.getCountry() + " "+ federation.getAcronym() + TfAno.getText();
			} else
				anilha = TfAno.getText() + " " + TfBand.getText();
			if (validateBand(anilha, breederType)) {
				bird.setBand(anilha);
				if (imageUploaded) {
					File defaultFolder = new File("resources/images/birds");
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
					obs = "Passaro comprado por " + bird.getBuyPrice() + " \\u20ac .";
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
	
	public boolean validateBand(String band, String breederType) throws SQLException {
		boolean validate = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (birdsRepository.checkIfExistsWithBand(band)) {
			if (breederType.equals(MyValues.CRIADOR_PROFISSIONAL)) {
//				anilha = club.getAcronym()+" "+stam+" "+TfNumero.getText()+" "+federation.getCountry()+" "+federation.getAcronym()+TfAno.getText(); 
				CbClub.setStyle(MyValues.ERROR_BOX_STYLE);
				TfNumero.setStyle(MyValues.ERROR_BOX_STYLE);
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha " + band + " ja existe!");
				validate = false;
			} else {
//				anilha = TfAno.getText()+" "+TfBand.getText();
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha " + band + " ja existe!");
				validate = false;
			}
		} else {
			CbClub.setStyle(null);
			TfNumero.setStyle(null);
			TfAno.setStyle(null);
			TfBand.setStyle(null);
			LabelAlert.setText("");
			validate = true;
		}
		return validate;
	}
	
	public boolean validator() {
		boolean validate= false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (CbCriador.getValue()==null) {
			CbCriador.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Criador tem de ser escolhido");
			validate=false;
		}else {
			CbCriador.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		
		if (validate && CbCriador.getValue().getType().equals(MyValues.CRIADOR_PROFISSIONAL))
			if (CbClub.getValue()==null) {
				CbClub.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Clube tem de ser escolhido");
				validate=false;
			}else {
				CbClub.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate && CbCriador.getValue().getType().equals(MyValues.CRIADOR_PROFISSIONAL))
			if (!TfNumero.getText().matches(Regex.INT)) {
				TfNumero.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Numero nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}else {
				TfNumero.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate && !CbCriador.getValue().getType().equals(MyValues.CRIADOR_PROFISSIONAL))
			if (!TfBand.getText().matches(Regex.CLASSIC_BAND)) {
				TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha Personalizada nao esta no formato correto ou tem de ser preenchido");
				validate=false;
			}else {
				TfBand.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate)
			if (!TfAno.getText().matches(Regex.ANO)) {
				TfAno.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Ano nao esta no formato correto ou tem de ser preenchido. ex:2022 ou 22");
				validate=false;
			}else {
				TfAno.setStyle(null);
				LabelAlert.setText("");
				validate=true;
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
			if (CbFather.getValue()==null) {
				CbFather.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Pai tem de ser escolhido.(Caso nao tenha Pai escolher 'Sem Pai')");
				validate=false;
			}else {
				CbFather.setStyle(null);
				LabelAlert.setText("");
				validate=true;
			}
		
		if (validate)
			if (CbMother.getValue()==null) {
				CbMother.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Mae tem de ser escolhida.(Caso nao tenha Mae escolher 'Sem Mae')");
				validate=false;
			}else {
				CbMother.setStyle(null);
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
			String sufix = LbImagePath.getText().substring(LbImagePath.getText().lastIndexOf("."));
			if (sufix!="png" || sufix!="jpg") {
				btnUpload.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Imagem tem de ser .jpg ou .png");
				validate=false;
			}else {
				btnUpload.setStyle(null);
				LabelAlert.setText("");
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
		CbClub.setStyle(null);
		CbEntryType.setStyle(null);
		DfDataEntrada.setStyle(null);
		CbSex.setStyle(null);
		CbSpecies.setStyle(null);
		CbMutation.setStyle(null);
		CbCage.setStyle(null);
		CbFather.setStyle(null);
		CbMother.setStyle(null);
		TfAno.setStyle(null);
		TfNumero.setStyle(null);
		TfBuyPrice.setStyle(null);
		CbState.setStyle(null);
		TfObs.setStyle(null);
		btnUpload.setStyle(null);
		TfBand.setStyle(null);
	}
	
	public void clearAllFields() {
		CbCriador.setValue(null);
		CbClub.setValue(null);
		CbEntryType.setValue(null);
		DfDataEntrada.setValue(null);
		CbSex.setValue(null);
		CbSpecies.setValue(null);
		CbMutation.setValue(null);
		CbCage.setValue(null);
		CbFather.setValue(null);
		CbMother.setValue(null);
		TfAno.setText("");
		TfNumero.setText("");
		TfBuyPrice.setText("");
		CbState.setValue(null);
		TfObs.setText("");
		TfBand.setText("");
    	ApNumero.setVisible(false);
    	ApBand.setVisible(false);
    	ApClub.setVisible(false);
		clearAllErrors();
	}
	
}
