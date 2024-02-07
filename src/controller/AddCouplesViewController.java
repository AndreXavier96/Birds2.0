package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CageRepository;
import repository.CouplesRepository;
import repository.HistoricRepository;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import constants.MyValues;
import domains.Bird;
import domains.Cage;
import domains.Couples;
import domains.Historic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.Node;

public class AddCouplesViewController implements Initializable {
	
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfMale, TfFemale, TfCage;
	@FXML
	private ListView<Bird> LvMale, LvFemale;
	@FXML
	private ListView<Cage> LvCages;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private CageRepository cageRepository = new CageRepository();
	private CouplesRepository couplesRepository = new CouplesRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 EventHandler<MouseEvent> showAllMalesEvent = event -> {
	            String searchTerm = TfMale.getText();
				ObservableList<Bird> listFathers = filterBirdsMale(searchTerm);
				LvMale.setItems(listFathers);
				LvMale.setVisible(true); // Show the ListView
	        };
	        TfMale.setOnMouseClicked(showAllMalesEvent);
	        TfMale.textProperty().addListener((observableFather, oldValueFather, newValueFather) -> {
	            ObservableList<Bird> listFathers = filterBirdsMale(newValueFather);
	            LvMale.setItems(listFathers);
	            LvMale.setVisible(true); // Show the ListView
	        });
	        EventHandler<MouseEvent> showAllFemalesEvent = event -> {
	            String searchTerm = TfFemale.getText();
				ObservableList<Bird> listMothers = filterBirdsFemale(searchTerm);
				LvFemale.setItems(listMothers);
				LvFemale.setVisible(true); // Show the ListView
	        };
	        TfFemale.setOnMouseClicked(showAllFemalesEvent);
	        TfFemale.textProperty().addListener((observableMother, oldValueMother, newValueMother) -> {
	            ObservableList<Bird> listMothers = filterBirdsFemale(newValueMother);
	            LvFemale.setItems(listMothers);
	            LvFemale.setVisible(true); // Show the ListView
	        });
	        EventHandler<MouseEvent> showAllCagesEvent = event -> {
	            String searchTerm = TfCage.getText();
				ObservableList<Cage> listCages = filterCages(searchTerm);
				LvCages.setItems(listCages);
				LvCages.setVisible(true); // Show the ListView
	        };
	        TfCage.setOnMouseClicked(showAllCagesEvent);
	        TfCage.textProperty().addListener((observableFather, oldValueFather, newValueFather) -> {
	            ObservableList<Cage> listCages = filterCages(newValueFather);
	            LvCages.setItems(listCages);
	            LvCages.setVisible(true); // Show the ListView
	        });
	        
	        
	        LvMale.setCellFactory(param -> new ListCell<Bird>() {
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
	        LvFemale.setCellFactory(param -> new ListCell<Bird>() {
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
	        LvCages.setCellFactory(param -> new ListCell<Cage>() {
	            @Override
	            protected void updateItem(Cage cage, boolean empty) {
	                super.updateItem(cage, empty);
	                if (empty || cage == null) {
	                    setText(null);
	                } else {
	                    setText(cage.getCode());
	                }
	            }
	        });
	        
	       
	        TfMale.focusedProperty().addListener((observableFocusFather, oldValueFocusFather, newValueFocusFather) -> {
	            if (!newValueFocusFather) {
	                LvMale.setVisible(false); // Hide the ListView when TfFather loses focus
	            }
	        });		        
	        LvMale.focusedProperty().addListener((observableFocusLvFathers, oldValueFocusLvFathers, newValueFocusLvFathers) -> {
	            if (!newValueFocusLvFathers) {
	            	LvMale.setVisible(false); // Hide the ListView when LvFathers loses focus
	            }
	        });
	        
	        TfFemale.focusedProperty().addListener((observableFocusMother, oldValueFocusMother, newValueFocusMother) -> {
	            if (!newValueFocusMother) {
	            	LvFemale.setVisible(false); // Hide the ListView when TfFather loses focus
	            }
	        });		        
	        LvFemale.focusedProperty().addListener((observableFocusLvMothers, oldValueFocusLvMothers, newValueFocusLvMothers) -> {
	            if (!newValueFocusLvMothers) {
	            	LvFemale.setVisible(false); // Hide the ListView when LvFathers loses focus
	            }
	        });
	        TfCage.focusedProperty().addListener((observableFocusCage, oldValueFocusCage, newValueFocusCage) -> {
	            if (!newValueFocusCage) {
	                LvCages.setVisible(false); // Hide the ListView when TfFather loses focus
	            }
	        });		        
	        LvCages.focusedProperty().addListener((observableFocusLvCage, oldValueFocusLvCage, newValueFocusLvCage) -> {
	            if (!newValueFocusLvCage) {
	            	LvCages.setVisible(false); // Hide the ListView when LvFathers loses focus
	            }
	        });
	        
	        
	        LvMale.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvFathers, oldValueSelectionLvFathers, newValueSelectionLvFathers) -> {
	            if (newValueSelectionLvFathers != null) {
	                TfMale.setText(newValueSelectionLvFathers.getBand());
	                LvMale.setVisible(false); // Hide the ListView after selecting a value
	            }
	        });
	        LvFemale.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvMothers, oldValueSelectionLvMothers, newValueSelectionLvMothers) -> {
	            if (newValueSelectionLvMothers != null) {
	                TfFemale.setText(newValueSelectionLvMothers.getBand());
	                LvFemale.setVisible(false); // Hide the ListView after selecting a value
	            }
	        });
	        LvCages.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvCage, oldValueSelectionLvCage, newValueSelectionLvCage) -> {
	            if (newValueSelectionLvCage != null) {
	                TfCage.setText(newValueSelectionLvCage.getCode());
	                LvCages.setVisible(false); // Hide the ListView after selecting a value
	            }
	        });
	}
	

	private ObservableList<Bird> filterBirdsMale(String searchTerm) {
		ObservableList<Bird> listMale = birdsRepository.getAllWhere("Sex", MyValues.MACHO);
		ObservableList<Bird> ListMaleCouples = couplesRepository.getAllBirdsWhere("MaleId", "State", MyValues.JUNTOS);
		Set<Integer> maleCoupleIds = ListMaleCouples.stream().map(Bird::getId).collect(Collectors.toSet());
		List<Bird> filteredListMale = listMale.stream()
	            .filter(bird -> !maleCoupleIds.contains(bird.getId()))
	            .collect(Collectors.toList());
		 List<Bird> filteredBirds = filteredListMale.stream()
		            .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		            .collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}

	private ObservableList<Bird> filterBirdsFemale(String searchTerm) {
		ObservableList<Bird> listFemale = birdsRepository.getAllWhere("Sex", MyValues.FEMEA);
		ObservableList<Bird> ListFemaleCouples = couplesRepository.getAllBirdsWhere("FemaleId", "State", MyValues.JUNTOS);
		Set<Integer> femaleCoupleIds = ListFemaleCouples.stream().map(Bird::getId).collect(Collectors.toSet());
		List<Bird> filteredListFemale = listFemale.stream()
	            .filter(bird -> !femaleCoupleIds.contains(bird.getId()))
	            .collect(Collectors.toList());
		 List<Bird> filteredBirds = filteredListFemale.stream()
		            .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		            .collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	private ObservableList<Cage> filterCages(String searchTerm) {
		ObservableList<Cage> listCages;
		try {
			listCages = cageRepository.getAllCages();
		
		List<Cage> filteredCages = listCages.stream()
				.filter(cage -> cage.getCode().toLowerCase().contains(searchTerm.toLowerCase()))
				.collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredCages);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			Couples couple = new Couples();
			Bird male = birdsRepository.getBirdWhereString("Band", TfMale.getText());
			Bird female = birdsRepository.getBirdWhereString("Band", TfFemale.getText());
			Cage cage = cageRepository.getCageByCode(TfCage.getText());
			couple.setMale(male);
			couple.setFemale(female);
			couple.setCage(cage);
			couple.setState(MyValues.JUNTOS);
			couplesRepository.Insert(couple);
			String dateMale = new SimpleDateFormat(MyValues.DATE_FORMATE).format(male.getEntryDate());
			String dateFemale = new SimpleDateFormat(MyValues.DATE_FORMATE).format(female.getEntryDate());
			String obsMale = "Acasalado com "+female.getBand();
			String obsFemale = "Acasalado com "+male.getBand();
			String obs="Gaiola Alterada por acasalar, gaiola "+cage.getCode();
			historicRepository.insertHistoric(new Historic(null,MyValues.ADD_COUPLE,dateMale,obsMale, male));
			historicRepository.insertHistoric(new Historic(null,MyValues.ADD_COUPLE,dateFemale,obsFemale, female));
			birdsRepository.partialUpdateIntBird(male.getId(), "CageId", cage.getId());
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_CAGE,dateMale,obs, male));
			birdsRepository.partialUpdateIntBird(female.getId(), "CageId", cage.getId());
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_CAGE,dateFemale,obs, female));
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Casal "+couple.getMale().getBand()+"+"+couple.getFemale().getBand()+" inserido com sucesso!");
			clearAllFields();
		}
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
		 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	     stage.close(); 
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		
		if (TfMale.getText().length() == 0) {
			TfMale.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Macho tem de ser escolhido.");
			validated = false;
		} else {
			TfMale.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		
		if (validated) 
			if (TfFemale.getText().length() == 0) {
				TfFemale.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Femea tem de ser escolhida.");
				validated = false;
			} else {
				TfFemale.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		
		if (validated) 
			if (TfCage.getText().length() == 0) {
				TfCage.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Gaiola tem de ser escolhida.");
				validated = false;
			} else {
				TfCage.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		
		return validated;
	}
		
	public void clearAllErrors() {
		TfMale.setStyle(null);
		TfFemale.setStyle(null);
		TfCage.setStyle(null);
		LvMale.setVisible(false);
		LvFemale.setVisible(false);
		LvCages.setVisible(false);
	}
	
	public void clearAllFields() {
		TfMale.setText("");
		TfFemale.setText("");
		TfCage.setText("");
		clearAllErrors();
	}
}
