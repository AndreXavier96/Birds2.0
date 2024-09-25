package controller.brood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import controller.HiperligacoesController;
import controller.bird.ViewSingleBirdController;
import controller.egg.ViewSingleEggController;
import domains.Bird;
import domains.Brood;
import domains.Egg;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	private TableView<Bird> TvAdoptive;
	@FXML
	private TableColumn<Bird,String> colBand,colSpecie;

	private ObservableList<Egg> eggs = FXCollections.observableArrayList();
	private ObservableList<Bird> adoptiveParents = FXCollections.observableArrayList();
	
	private HiperligacoesController hiperligacoes = new HiperligacoesController();
	
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
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/egg/ViewSingleEgg.fxml"));
						Parent root = loader.load();
						ViewSingleEggController viewSingleEggController = loader.getController();
						viewSingleEggController.startValues(selectedEgg);
						
						Stage stage = new Stage();
						stage.setScene(new Scene(root));
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.showAndWait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		TvAdoptive.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Bird selectedBird = TvAdoptive.getSelectionModel().getSelectedItem();
				if (selectedBird != null) {
					try {
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
		
	}

	public void updateAllInfo(Brood b) throws SQLException {
		LbMale.setText(b.getFather().getBand());
		LbFemale.setText(b.getMother().getBand());
		LbCage.setText(b.getCage().getCode());
		TvEggs.setItems(FXCollections.observableList(b.getEggs()));
		TvAdoptive.setItems(FXCollections.observableList(b.getAdoptiveParents()));
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
