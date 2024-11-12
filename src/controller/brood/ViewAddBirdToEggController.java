package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import constants.MyValues;
import constants.Regex;
import domains.Bird;
import domains.Brood;
import domains.Egg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.BroodRepository;
import repository.EggRepository;

public class ViewAddBirdToEggController implements Initializable {
	@FXML
	private Label LabelError,LbTittle,LbFather,LbMother;
	@FXML
	private TextField TfBird;
	@FXML
	private ListView<Bird> LvBirds;  

	@FXML
	private ViewSingleBroodController viewSingleBroodController;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private EggRepository eggRepository = new EggRepository();
	private BroodRepository broodRepository = new BroodRepository();
	
	private Egg egg;
	private Brood brood;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TfBird.setDisable(false);
		EventHandler<MouseEvent> showAllFathersEvent = event -> {
			String searchTerm = TfBird.getText();
			ObservableList<Bird> listFathers = filterBirds(searchTerm);
			LvBirds.setItems(listFathers);
			LvBirds.setVisible(true); // Show the ListView
		};
		TfBird.setOnMouseClicked(showAllFathersEvent);
		TfBird.textProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Bird> listBirds = filterBirds(newValue);
			LvBirds.setItems(listBirds);
			LvBirds.setVisible(true); // Show the ListView
		});
		LvBirds.setCellFactory(param -> new ListCell<Bird>() {
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
		TfBird.focusedProperty()
		.addListener((observableFocus, oldValueFocus, newValueFocus) -> {
			if (!newValueFocus) {
				LvBirds.setVisible(false); // Hide the ListView when TfFather loses focus
			}
		});
		LvBirds.focusedProperty()
		.addListener((observableFocus, oldValueFocus, newValueFocus) -> {
			if (!newValueFocus) {
				LvBirds.setVisible(false); // Hide the ListView when LvFathers loses focus
			}
		});
		LvBirds.getSelectionModel().selectedItemProperty().addListener(
				(observableSelection, oldValueSelection, newValueSelection) -> {
					if (newValueSelection != null) {
						TfBird.setText(newValueSelection.getBand());
						LvBirds.setVisible(false); // Hide the ListView after selecting a value
					}
				});
	}
	
	private ObservableList<Bird> filterBirds(String searchTerm) {
		ObservableList<Bird> listBirds = birdsRepository.getAllBirds();
		List<Bird> listBirdsFiltered = listBirds.stream()
				.filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase())) //band match
				.filter(bird -> (bird.getEntryDate().compareTo(brood.getStart()))>=0) //passaro inserido depois da start date
				.filter(bird -> brood.getFinish() == null || (bird.getEntryDate().compareTo(brood.getFinish()) <= 0)) // inserido antes da end date ou null
				.filter(bird -> !eggRepository.existEggWithBirdId(bird.getId()))//passaros nao atribuidos a ovos
				.filter(bird -> bird.getSpecies().getId() == brood.getFather().getSpecies().getId())//passaros da mesma especie
				.collect(Collectors.toList());
		return FXCollections.observableArrayList(listBirdsFiltered);
	}
	
	public void setViewSingleBroodController(ViewSingleBroodController controller, Egg egg, Brood brood) {
		this.viewSingleBroodController = controller;
		this.egg = egg;
		this.brood = brood;
	
		LbFather.setText(brood.getFather().getBand());
		LbMother.setText(brood.getMother().getBand());
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws SQLException {
		if (validate()) {
			Stage stage = (Stage) LbTittle.getScene().getWindow();
		    stage.close();
		    Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
		    eggRepository.updateEggBird(b,egg.getId());
		    this.brood = broodRepository.getBroodById(brood.getId());
		    viewSingleBroodController.setSuccess(brood.toString()+ " selecionada com sucesso!" , brood);
		}
	}
	
	private boolean validate() {
		boolean validate = false;
		clearAllErrors();
		LabelError.setStyle(MyValues.ALERT_ERROR);
		LabelError.setText("");
		if (TfBird.getText()==null || TfBird.getText().isEmpty()) {
			TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Anilha tem de ser preenchida");
			validate = false;
		}else {
			TfBird.setStyle(null);
			LabelError.setText("");
			validate=true;
		}
		
		if (validate) {
			if (!TfBird.getText().matches(Regex.FULL_BAND)) {
				TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Anilha nao esta no formato correto");
				validate = false;
			}else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		if (validate) {
			Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
			if (b==null) {
				TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelError.setText("Passaro nao existe");
				validate = false;
			}else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		if (validate) {
			Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
			if (b.getEntryDate().compareTo(brood.getStart()) < 0) {
		        TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
		        LabelError.setText("Passaro foi inserido antes da data de início da ninhada");
		        validate = false;
		    }else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		if (validate) {
			Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
			if (brood.getFinish() != null && b.getEntryDate().compareTo(brood.getFinish()) > 0) {
		        TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
		        LabelError.setText("Passaro foi inserido depois da data de término da ninhada");
		        validate = false;
		    }else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		if (validate) {
			Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
			if (eggRepository.existEggWithBirdId(b.getId())) {
		        TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
		        LabelError.setText("Passaro já foi atribuído a outro ovo");
		        validate = false;
		    }else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		if (validate) {
			Bird b = birdsRepository.getBirdWhereString("Band", TfBird.getText());
			if (b.getSpecies().getId() != brood.getFather().getSpecies().getId()) {
		        TfBird.setStyle(MyValues.ERROR_BOX_STYLE);
		        LabelError.setText("Passaro não pertence à mesma espécie do pais");
		        validate = false;
		    }else {
				TfBird.setStyle(null);
				LabelError.setText("");
				validate = true;
			}
		}
		return validate;
	}
	
	public void clearAllErrors() {
		TfBird.setStyle(null);
		}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LbTittle.getScene().getWindow();
	    stage.close();
	}
	
}
