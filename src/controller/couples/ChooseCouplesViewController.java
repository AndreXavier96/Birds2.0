package controller.couples;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import constants.MyValues;
import controller.brood.AddBroodViewController;
import domains.Bird;
import domains.Couples;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CouplesRepository;

public class ChooseCouplesViewController implements Initializable{

	@FXML
	private Label LabelAlert, LBTitle;
	
	@FXML
	private TextField TfMale, TfFemale;
	
	@FXML
	private ListView<Bird> LvMale;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private CouplesRepository couplesRepository = new CouplesRepository();
	
	private AddBroodViewController addBroodViewController;
	
	public void setAddBroodViewControllerCouples(AddBroodViewController addBroodViewController) {
		this.addBroodViewController = addBroodViewController;
	}
	
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
            TfFemale.clear();
            TfFemale.setText(getFemaleByMale(newValueFather));
        });
		TfMale.focusedProperty().addListener((observableFocusFather, oldValueFocusFather, newValueFocusFather) -> {
            if (!newValueFocusFather) {
                LvMale.setVisible(false); // Hide the ListView when TfFather loses focus
            }
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
        LvMale.focusedProperty().addListener((observableFocusLvFathers, oldValueFocusLvFathers, newValueFocusLvFathers) -> {
            if (!newValueFocusLvFathers) {
            	LvMale.setVisible(false); // Hide the ListView when LvFathers loses focus
            }
        });
        LvMale.getSelectionModel().selectedItemProperty().addListener((observableSelectionLvFathers, oldValueSelectionLvFathers, newValueSelectionLvFathers) -> {
            if (newValueSelectionLvFathers != null) {
                TfMale.setText(newValueSelectionLvFathers.getBand());
                LvMale.setVisible(false); // Hide the ListView after selecting a value
                TfFemale.setText(getFemaleByMale(newValueSelectionLvFathers.getBand()));
            }
        });
		
          
	}
	
	private String getFemaleByMale(String maleBand) {
		Bird male = birdsRepository.getBirdWhereString("Band", maleBand);
		try {
			return couplesRepository.getCouplesWhereBird(male).getFemale().getBand();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private ObservableList<Bird> filterBirdsMale(String searchTerm) {
		ObservableList<Bird> listMale = birdsRepository.getAllWhere("Sex", MyValues.MACHO);
		ObservableList<Bird> ListMaleCouples = couplesRepository.getAllBirdsWhere("MaleId", "State", MyValues.JUNTOS);
		Set<Integer> maleCoupleIds = ListMaleCouples.stream().map(Bird::getId).collect(Collectors.toSet());
		List<Bird> filteredListMale = listMale.stream()
	            .filter(bird -> maleCoupleIds.contains(bird.getId()))
	            .collect(Collectors.toList());
		 List<Bird> filteredBirds = filteredListMale.stream()
		            .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		            .collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	
	@FXML
	public void btnAdd(ActionEvent actionEvent) {
		if (validator()) {
			Bird male = birdsRepository.getBirdWhereString("Band", TfMale.getText());
			Couples couple = new Couples();
			try {
				couple = couplesRepository.getCouplesWhereBirdJunto(male);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Casal escolhido com sucesso!");
			clearAllFields();
			addBroodViewController.addCouples(couple);
			btnCancel(actionEvent);
		}
	}
	
	public boolean validator() {
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
		return validated;
	}
	
	public void clearAllErrors() {
		TfMale.setStyle(null);
		TfFemale.setStyle(null);
	}
	
	public void clearAllFields() {
		TfMale.setText("");
		TfFemale.setText("");;
		clearAllErrors();
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
		Stage stage = (Stage) LBTitle.getScene().getWindow();
		stage.close();
	}
}
