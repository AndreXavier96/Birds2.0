package controller.adoptiveParent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import constants.MyValues;
import constants.Regex;
import controller.brood.AddBroodViewController;
import domains.Bird;
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

public class AddAdoptiveParentViewController implements Initializable {
	@FXML
	private Label LabelAlert,LBTitle;
	@FXML
	private TextField TfBand;
	@FXML
	private ListView<Bird> LvBand;
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private AddBroodViewController addBroodViewController;
	
	private String fatherBand;
	private String motherBand;
	
	public void setAdoptiveParentsViewController(AddBroodViewController addBroodViewController,String fatherBand, String motherBand) {
		this.addBroodViewController = addBroodViewController;
		this.fatherBand = fatherBand;
		this.motherBand = motherBand;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		EventHandler<MouseEvent> showAllEvent = event -> {
            String searchTerm = TfBand.getText();
			ObservableList<Bird> listBirds = filterBirds(searchTerm);
			LvBand.setItems(listBirds);
			LvBand.setVisible(true); // Show the ListView
        };
        TfBand.setOnMouseClicked(showAllEvent);
        TfBand.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Bird> listBirds = filterBirds(newValue);
            LvBand.setItems(listBirds);
            LvBand.setVisible(true); // Show the ListView
        });
        LvBand.setCellFactory(param -> new ListCell<Bird>() {
            @Override
            protected void updateItem(Bird bird, boolean empty) {
                super.updateItem(bird, empty);
                if (empty || bird == null) {
                    setText(null);
                } else {
                    setText(bird.getBand());
                }
            } //Show only Band as String
        });
        LvBand.getSelectionModel().selectedItemProperty().addListener((observableSelection, oldValueSelection, newValueSelection) -> {
            if (newValueSelection != null) {
            	TfBand.setText(newValueSelection.getBand());//Select Value
            	LvBand.setVisible(false); // Hide the ListView after selecting a value
            }
        });
	}
	
	private ObservableList<Bird> filterBirds(String searchTerm) {
		ObservableList<Bird> listBirds = birdsRepository.getAllBirds();
		 List<Bird> filteredBirds = listBirds.stream()
		            .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		            .filter(bird -> !bird.getBand().toLowerCase().equals(fatherBand.toLowerCase()))
		            .filter(bird -> !bird.getBand().toLowerCase().equals(motherBand.toLowerCase()))
		            .collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException, IOException  {
		if (validator()) {
			LabelAlert.setStyle(null);
			Bird bird = birdsRepository.getBirdWhereString("Band", TfBand.getText().toUpperCase());
			addBroodViewController.addAdoptiveParentsToListView(bird);
			btnClose(event);
		}
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) TfBand.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		Bird bird = birdsRepository.getBirdWhereString("Band", TfBand.getText().toUpperCase());
		if (TfBand.getText().length() == 0) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha tem de ser preenchido.");
			validated = false;
		} else if (!TfBand.getText().matches(Regex.FULL_BAND)) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha nao esta no formato correto.");
			validated = false;
		} else if (bird==null) {
			TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Passaro nao existe.");
			validated = false;
		} else {
			TfBand.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}
		return validated;
	}
		
	public void clearAllErrors() {
		TfBand.setStyle(null);
	}
	
	public void clearAllFields() {
		TfBand.setText("");
		clearAllErrors();
	}
}
