package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.CageRepository;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import domains.Bird;
import domains.Brood;
import domains.Egg;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ViewSingleBroodController implements Initializable {
	@FXML
	private Label LbTitle, LabelAlert,LbMale, LbFemale,LbCage;
	
	@FXML
	private TableView<Egg> TvEggs;
	@FXML
	private TableColumn<Egg,String>colType,colStatute;
	@FXML
	private TableColumn<Egg,Date>colDate;
	
	@FXML
	private TableView<Bird> TvAdoptive;
	@FXML
	private TableColumn<Bird,String> colBand,colSpecie;

	private ObservableList<Egg> eggs = FXCollections.observableArrayList();
	private ObservableList<Bird> adoptiveParents = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colDate.setCellValueFactory(new PropertyValueFactory<>("postureDate"));
		colDate.setCellFactory(column -> {
			return new TableCell<Egg, Date>() {
				private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item==null || empty)
						setText(null);
					else
						setText(sdf.format(item));
				}
			};
		});
		colType.setCellValueFactory(new PropertyValueFactory<>("type"));
		colStatute.setCellValueFactory(new PropertyValueFactory<>("statute"));
		TvEggs.setItems(eggs);
		
		colBand.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		TvAdoptive.setItems(adoptiveParents);
	}

	public void updateAllInfo(Brood b) throws SQLException {
		LbMale.setText(b.getFather().getBand());
		LbFemale.setText(b.getMother().getBand());
		LbCage.setText(b.getCage().getCode());
		TvEggs.setItems(FXCollections.observableList(b.getEggs()));//TODO empty
		TvAdoptive.setItems(FXCollections.observableList(b.getAdoptiveParents()));//TODO empty
	}
	
//	private void openViewSingleBird(String band) {
//	    try {
//	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
//	    	Parent root = loader.load();
//	    	ViewSingleBirdController viewSingleBirdController = loader.getController();
//	    	viewSingleBirdController.search(band);
//	    	Scene currentScene = LbTitle.getScene();
//	    	Stage currentStage =(Stage) currentScene.getWindow();
//	    	currentScene.setRoot(root);
//	    	currentStage.sizeToScene();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	}
	
//	private void openViewCage(String code) {
//	    try {
//	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewSingleCage.fxml"));
//	    	Parent root = loader.load();
//	    	ViewSingleCageController viewSingleCageController = loader.getController();
//	    	viewSingleCageController.search(code);
//	    	Scene currentScene = LbTitle.getScene();
//	    	Stage currentStage =(Stage) currentScene.getWindow();
//	    	currentScene.setRoot(root);
//	    	currentStage.sizeToScene();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    } catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	public void clearAllFields() {
//		LabelAlert.setText(null);
//		LbTitle.setText("Gaiola XXXX");
//		LbMale.setText(null);
//		LbFemale.setText(null);
//		LbCage.setText(null);
//		eggs.clear();
//		adoptiveParents.clear();
//		TvEggs.setItems(eggs);
//		TvAdoptive.setItems(adoptiveParents);
//	}
	
//	public boolean validatorSearch(){
//		boolean validate = false;
//		if (LbMale.getText()==null) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Casal tem de ser procurado.");
//			validate=false;
//		}else if (!TfSearch.getText().equalsIgnoreCase(LbMale.getText()) && !TfSearch.getText().equalsIgnoreCase(LbFemale.getText())) {
//	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
//	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
//	        LabelAlert.setText("Casal tem de ser procurado.");
//	        validate = false;
//	    }else {
//			TfSearch.setStyle(null);
//			LabelAlert.setText("");
//			validate=true;
//		}
//		return validate;
//	}
	
//	public boolean validator() throws SQLException {
//		boolean validate = false;
//		Bird bird = birdsRepository.getBirdWhereString("Band", TfSearch.getText().toUpperCase());
//		if (TfSearch.getText().length()==0) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Pesquisa tem de ser preenchida");
//			validate=false;
//		}else if (bird==null) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Passaro nao existe");
//			validate=false;
//		}else if (!broodRepository.checkIfBroodExists(bird.getId())) {
//			LabelAlert.setStyle(MyValues.ALERT_ERROR);
//			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Passaro nao tem ninhada");
//			validate=false;
//		}else {
//			TfSearch.setStyle(null);
//			LabelAlert.setText("");
//			validate=true;
//		}
//		return validate;
//	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
