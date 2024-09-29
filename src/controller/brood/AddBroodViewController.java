package controller.brood;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import constants.MyValues;
import controller.adoptiveParent.AddAdoptiveParentViewController;
import controller.couples.ChooseCouplesViewController;
import controller.egg.AddEggViewController;
import domains.Bird;
import domains.Brood;
import domains.Couples;
import domains.Egg;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BroodRepository;
import repository.CageRepository;

public class AddBroodViewController implements Initializable {
	@FXML
	private Label LabelAlert, LBTitle;
	@FXML
	private TextField TfMale,TfFemale, TfCage;
	@FXML
	private DatePicker DtStart, DtFinish;
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
	
	@FXML
	private AnchorPane ApAdoptiveParents;
	
	private BroodRepository broodRepository = new BroodRepository();
	private CageRepository cageRepository = new CageRepository();
	
	private ObservableList<Egg> eggs = FXCollections.observableArrayList();
	private ObservableList<Bird> adoptiveParents = FXCollections.observableArrayList();
	private Couples couples = new Couples();
	
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
		TfMale.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null || newValue.isEmpty())
				ApAdoptiveParents.setDisable(true);
			else
				ApAdoptiveParents.setDisable(false);
		});
	}
	
	@FXML
	public void btnChooseCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ChooseCoupleView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        ChooseCouplesViewController chooseCouplesViewController = loader.getController();
	        chooseCouplesViewController.setAddBroodViewControllerCouples(this);
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddEggs(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/egg/AddEggView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        AddEggViewController addEggViewController = loader.getController();
	        addEggViewController.setAddBroodViewController(this,DtStart.getValue(),DtFinish.getValue());
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddAdoptives(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/adoptiveParent/AddAdoptiveParentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.initModality(Modality.APPLICATION_MODAL);
	        AddAdoptiveParentViewController addAdoptiveParentViewController = loader.getController();
	        addAdoptiveParentViewController.setAdoptiveParentsViewController(this,TfMale.getText(),TfFemale.getText());
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			LabelAlert.setStyle(null);
			Brood b = new Brood();
			b.setEggs(TvEggs.getItems());
			b.setStart(Date.from(DtStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			if(DtFinish.getValue()!=null)
				b.setFinish(Date.from(DtFinish.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			b.setFather(this.couples.getMale());
			b.setMother(this.couples.getFemale());
			b.setAdoptiveParents(TvAdoptive.getItems());
			b.setCage(cageRepository.getCageByCode(TfCage.getText()));
			broodRepository.insert(b);
			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
			LabelAlert.setText("Ninhada inserida com sucesso!");
			clearAllFields();
		}
	}

	public void addEggToListView(List<Egg> eggs) {
		this.eggs.addAll(eggs);
		TvEggs.setItems(this.eggs);
	}
	
	public void addCouples(Couples c) {
		this.couples=c;
		TfMale.setText(this.couples.getMale().getBand());
		TfFemale.setText(this.couples.getFemale().getBand());
		TfCage.setText(this.couples.getCage().getCode());
	}
	
	public void addAdoptiveParentsToListView(Bird adoptiveParent) {
		this.adoptiveParents.add(adoptiveParent);
		TvAdoptive.setItems(this.adoptiveParents);
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LBTitle.getScene().getWindow();
		stage.close();
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");
		if (TfMale.getText().length() == 0) {
			TfMale.setStyle(MyValues.ERROR_BOX_STYLE);
			TfFemale.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Casal tem de ser preenchido.");
			validated = false;
//		} else if (!TfCouple.getText().matches(Regex.FULL_BAND)) {
//			TfCouple.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Anilha nao esta no formato correto.");
//			validated = false;
//		} else if (bird==null) {
//			TfCouple.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Passaro do casal nao existe");
//			validated = false;
//		} else if (!couplesRepository.checkIfCouplesExist(bird)) {
//			TfCouple.setStyle(MyValues.ERROR_BOX_STYLE);
//			LabelAlert.setText("Passaro nao acasalado");
//			validated = false;
		} else {
			TfMale.setStyle(null);
			TfFemale.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		if (validated)
			try {
				DtStart.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				DtStart.setStyle(null);
				validated = true;
			} catch (Exception e) {
				DtStart.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Data inicio postura nao esta no formato correto ou tem de ser preenchido");
				validated = false;
			}

		if (validated)
			try {
				if (DtFinish.getValue()==null) 
					validated = true;
				else {
					DtFinish.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					DtFinish.setStyle(null);
					validated = true;
				}
			} catch (Exception e) {
				DtFinish.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Data inicio postura nao esta no formato correto");
				validated = false;
			}
		return validated;
	}

	public void clearAllErrors() {
		TfMale.setStyle(null);
		TfFemale.setStyle(null);
		TfCage.setStyle(null);
		DtStart.setStyle(null);
		DtFinish.setStyle(null);
		TvEggs.setStyle(null);
		TvAdoptive.setStyle(null);
	}

	public void clearAllFields() {
		TfMale.setText("");
		TfFemale.setText("");
		TfCage.setText("");
		DtStart.setValue(null);
		DtFinish.setValue(null);
		eggs.clear();
		adoptiveParents.clear();
		TvEggs.setItems(eggs);
		TvAdoptive.setItems(adoptiveParents);
		clearAllErrors();
	}

	
}
