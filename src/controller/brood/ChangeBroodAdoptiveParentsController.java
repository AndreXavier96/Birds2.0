package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import constants.MyValues;
import domains.Bird;
import domains.Brood;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.AdoptiveParentsRepository;
import repository.BirdsRepository;
import repository.BroodRepository;

public class ChangeBroodAdoptiveParentsController implements Initializable {
	@FXML
	private Label LabelError,LbFather,LbMother;
	@FXML
	private ListView<Bird> adoptiveParentsAssigned,adoptiveParentsAvailable;
	@FXML
	private Button btnAssign, btnDeAssign;	
	@FXML
	private ViewSingleBroodController viewSingleBroodController;
	
	private BroodRepository broodRepository = new BroodRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	private AdoptiveParentsRepository adoptiveParentsRepository = new AdoptiveParentsRepository();
	
	private ObservableList<Bird> availableAdoptive;
	private ObservableList<Bird> assignedAdoptive;
	private Brood brood;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		adoptiveParentsAssigned.setCellFactory(new Callback<ListView<Bird>, ListCell<Bird>>() {
            @Override
            public ListCell<Bird> call(ListView<Bird> listView) {
                return new ListCell<Bird>() {
                    @Override
                    protected void updateItem(Bird bird, boolean empty) {
                        super.updateItem(bird, empty);
                        if (empty || bird == null) {
                            setText(null);
                        } else {
                            setText(bird.getBand() + " [" + bird.getSpecies().getCommonName() + "]");
                        }
                    }
                };
            }
        });
		adoptiveParentsAvailable.setCellFactory(new Callback<ListView<Bird>, ListCell<Bird>>() {
            @Override
            public ListCell<Bird> call(ListView<Bird> listView) {
                return new ListCell<Bird>() {
                    @Override
                    protected void updateItem(Bird bird, boolean empty) {
                        super.updateItem(bird, empty);
                        if (empty || bird == null) {
                            setText(null);
                        } else {
                            setText(bird.getBand() + " [" + bird.getSpecies().getCommonName() + "]");
                        }
                    }
                };
            }
        });
	
	}
	
	public void startValues(Brood brood) throws SQLException {
		this.brood = brood;
		LbFather.setText(brood.getFather().getBand());
		LbMother.setText(brood.getMother().getBand());
		ObservableList<Bird> listBirds = birdsRepository.getAllBirds();
		List<Bird> availableBirds = listBirds.stream()
				.filter(bird -> !bird.getBand().toLowerCase().equals(brood.getFather().getBand().toLowerCase()))
				.filter(bird -> !bird.getBand().toLowerCase().equals(brood.getMother().getBand().toLowerCase()))
				.filter(bird -> brood.getAdoptiveParents().stream().noneMatch(assigned -> assigned.getBand().equalsIgnoreCase(bird.getBand())))
				.collect(Collectors.toList());
		assignedAdoptive = FXCollections.observableList(brood.getAdoptiveParents());
		availableAdoptive = FXCollections.observableArrayList(availableBirds);
		adoptiveParentsAssigned.setItems(assignedAdoptive);
		adoptiveParentsAvailable.setItems(availableAdoptive);
	}
	
	public void setViewSingleBroodController(ViewSingleBroodController controller) {
		this.viewSingleBroodController = controller;
	}
	
	@FXML
	public void btnChange(ActionEvent event) throws SQLException {
		adoptiveParentsRepository.updateAdoptiveParents(brood.getId(), assignedAdoptive);
	    Stage stage = (Stage) btnAssign.getScene().getWindow();
	    stage.close();
	    viewSingleBroodController.setSuccess(MyValues.CHANGE_ADOPTIVE_SUCCESS, broodRepository.getBroodById(brood.getId()));
	}
	
	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) btnAssign.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	public void btnAssign(ActionEvent event) {
		Bird selectedBird = adoptiveParentsAvailable.getSelectionModel().getSelectedItem();
		clearAllErrors();
		if (selectedBird!=null) {
			assignedAdoptive.add(selectedBird);
			availableAdoptive.remove(selectedBird);
			btnAssign.setStyle(null);
		}else {
			btnAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Um passaro disponivel tem de ser selecionado.");
		}
	}
	
	@FXML
	public void btnDeAssign(ActionEvent event) {
		Bird selectedBird = adoptiveParentsAssigned.getSelectionModel().getSelectedItem();
		clearAllErrors();
		if (selectedBird!=null) {
			assignedAdoptive.remove(selectedBird);
			availableAdoptive.add(selectedBird);
			btnDeAssign.setStyle(null);
		}else {
			btnDeAssign.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelError.setText("Um passaro escolhido tem de ser selecionado.");
		}
	}
	
	public void clearAllErrors() {
		btnAssign.setStyle(null);
		btnDeAssign.setStyle(null);
	}
}
