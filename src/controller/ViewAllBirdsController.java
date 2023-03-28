package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import domains.Bird;
import domains.State;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.BirdsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colAno;
	@FXML
	private TableColumn<Bird, String> colAnilha,colEntryDate,colEntryType,
		colBuyPrice,colSellPrice,colState,colSex,colFather,colMother,colSpecie,colMutation,
		colCage,colBreeder,deleteButton;

	private BirdsRepository birdsRepository = new BirdsRepository();
	
	@FXML
	public void btnBack(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(Paths.get("resources/views/MainScene.fxml").toUri().toURL());
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllBirds();
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Year"));
		colEntryDate.setCellValueFactory(new PropertyValueFactory<>("EntryDate"));
		colEntryType.setCellValueFactory(new PropertyValueFactory<>("EntryType"));
		colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("BuyPrice"));
		colSellPrice.setCellValueFactory(cellData ->  new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getState())
	    		.map(State::getValor)
	    		.map(String::valueOf)
	    		.orElse("0")));
		colState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		colSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
	    colFather.setCellValueFactory(cellData -> new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getFather())
                .map(Bird::getBand)
                .orElse("")));
	    colMother.setCellValueFactory(cellData ->  new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getMother())
	    		.map(Bird::getBand)
	    		.orElse("")));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		colMutation.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getMutations().getName()));
		colCage.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getCage().getCode()));
		colBreeder.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getBreeder().getName()));
		deleteButton.setCellFactory(new Callback<TableColumn<Bird, String>, TableCell<Bird, String>>() {
			@Override
			public TableCell<Bird, String> call(TableColumn<Bird, String> column) {
				return new TableCell<Bird, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Bird bird = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(bird);
			                    }
			                });
			             }
					 
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty) {
							setGraphic(deleteButton);
						} else {
							setGraphic(null);
						}
					}
				};
			}
		});
		
		tableID.setItems(birds);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Bird selectedBird = tableID.getSelectionModel().getSelectedItem();
				if (selectedBird!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/ViewSingleBird.fxml"));
						Parent root = loader.load();
						ViewSingleBirdController viewSingleBirdController = loader.getController();
						viewSingleBirdController.search(selectedBird.getBand());
						Scene currentScene = tableID.getScene();
						Stage currentStage =(Stage) currentScene.getWindow();
						currentScene.setRoot(root);
						currentStage.sizeToScene(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	
	private void deleteButtonAction(Bird bird) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar o passaro: '" + bird.getBand() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_BIRD+bird.getBand());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				birdsRepository.deleteBird(bird);
//				tableID.getItems().remove(bird);
				ObservableList<Bird> birds = birdsRepository.getAllBirds();
				tableID.setItems(birds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
