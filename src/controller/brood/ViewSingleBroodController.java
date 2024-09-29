package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.EggRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import controller.HiperligacoesController;
import controller.bird.ViewSingleBirdController;
import controller.egg.AddEggViewController;
import domains.Bird;
import domains.Brood;
import domains.Egg;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private TableColumn<Egg,Date>colDate, colDateEclo;
	@FXML
	private TableColumn<Egg,String> colDelete;
	
	@FXML
	private TableView<Bird> TvAdoptive;
	@FXML
	private TableColumn<Bird,String> colBand,colSpecie;

	private ObservableList<Egg> eggs = FXCollections.observableArrayList();
	private ObservableList<Bird> adoptiveParents = FXCollections.observableArrayList();
	
	private Brood brood;
	
	private HiperligacoesController hiperligacoes = new HiperligacoesController();
	private EggRepository eggRepository = new EggRepository();
	
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
		
		colDateEclo.setCellValueFactory(new PropertyValueFactory<>("outbreakDate"));
		colDateEclo.setCellFactory(column -> {
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
		
		LbMale.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSingleBird(LbTitle.getScene(),LbMale.getText());
		});
		LbFemale.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSingleBird(LbTitle.getScene(),LbFemale.getText());
		});
		LbCage.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewCage(LbTitle.getScene(),LbCage.getText());
		});
		TvEggs.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Egg selectedEgg = TvEggs.getSelectionModel().getSelectedItem();
				if (selectedEgg != null) {
					hiperligacoes.openViewEggFromSingleBrood(LbTitle.getScene(),selectedEgg);
					eggs = FXCollections.observableArrayList(eggRepository.getEggsForBrood(brood.getId()));
					TvEggs.setItems(eggs);
				}
			}
		});
		
		TvAdoptive.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Bird selectedBird = TvAdoptive.getSelectionModel().getSelectedItem();
				if (selectedBird != null) {
					try {//TODO use hiperligacoes
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
						Parent root = loader.load();
						ViewSingleBirdController viewSingleBirdController = loader.getController();
						viewSingleBirdController.search(selectedBird.getBand());
						Scene currentScene = TvAdoptive.getScene();
						Stage currentStage = (Stage) currentScene.getWindow();
						currentScene.setRoot(root);
						currentStage.sizeToScene();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		// Set up the delete column with buttons
	    colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
	    colDelete.setCellFactory(column -> new TableCell<Egg, String>() {
	        private final Button deleteButton = new Button("X");
	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                setGraphic(deleteButton);
	                deleteButton.setOnAction(event -> {
	                    Egg selectedEgg = getTableView().getItems().get(getIndex());
	                    try {
	                        deleteEgg(selectedEgg);
	                    } catch (SQLException | IOException e) {
	                        e.printStackTrace();
	                    }
	                });
	            }
	        }
	    });

	}   
	
	private void deleteEgg(Egg egg) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = loader.load();
		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar este ovo?");
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_EGG);
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
			    eggRepository.deleteEgg(egg.getId());
			    eggs.remove(egg);
			    TvEggs.setItems(eggs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    TvEggs.refresh();
	}

	public void updateAllInfo(Brood b) throws SQLException {
		this.brood = b;
		LbMale.setText(b.getFather().getBand());
		LbFemale.setText(b.getMother().getBand());
		LbCage.setText(b.getCage().getCode());
		ObservableList<Egg> eggsObservableList = FXCollections.observableList(b.getEggs());
		TvEggs.setItems(eggsObservableList);
		this.eggs = eggsObservableList;
		TvAdoptive.setItems(FXCollections.observableList(b.getAdoptiveParents()));
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
	        addEggViewController.setAddBroodViewController2(this);
	        stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEggToListView(List<Egg> eggs) throws SQLException {
		this.eggs.addAll(eggs);
		this.brood.setEggs(eggs);
		TvEggs.setItems(this.eggs);
		eggRepository.insert(brood.getId(), eggs);
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void btnChangeCage(ActionEvent event) throws IOException, SQLException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/ChangeBroodCage.fxml"));
			Parent root = loader.load();
			ChangeBroodCageController broodCageController = loader.getController();
			broodCageController.startValues(brood);
			broodCageController.setViewSingleBroodController(this);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_CHANGE_CAGE);
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
	}
	
	@FXML
	public void btnChangeAdoptiveParents(ActionEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/ChangeBroodAdoptiveParents.fxml"));
		Parent root = loader.load();
		ChangeBroodAdoptiveParentsController controller = loader.getController();
		controller.startValues(brood);
		controller.setViewSingleBroodController(this);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_CHANGE_ADOPTIVES);
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
	
	@FXML
	public void btnDeleteBrood(ActionEvent event) throws IOException, SQLException {//TODO delete Brood
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/ChangeBroodAdoptiveParents.fxml"));
//		Parent root = loader.load();
//		ChangeBroodAdoptiveParentsController controller = loader.getController();
//		controller.startValues(brood);
//		controller.setViewSingleBroodController(this);
//		Scene scene = new Scene(root);
//		Stage stage = new Stage();
//		stage.setTitle(MyValues.TITLE_CHANGE_ADOPTIVES);
//		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
//		stage.setScene(scene);
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.showAndWait();
	}
	
	public void setSuccess(String msg, Brood brood) throws SQLException {
		LabelAlert.setStyle(MyValues.ALERT_SUCESS);
		LabelAlert.setText(msg);
		updateAllInfo(brood);
	}
}
