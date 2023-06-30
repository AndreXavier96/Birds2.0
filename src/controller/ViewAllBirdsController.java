package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import domains.Bird;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colAno;
	@FXML
	private TableColumn<Bird, String> colAnilha,colState,colSex,colSpecie,colCage,deleteButton;
	@FXML
	private Label LabelAlert;

	private BirdsRepository birdsRepository = new BirdsRepository();
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllBirds();
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Year"));
		colState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		colSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		colCage.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getCage().getCode()));
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
			                        try {
										deleteButtonAction(bird);
									} catch (SQLException e) {
										e.printStackTrace();
									}
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
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
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
	
	private void deleteButtonAction(Bird bird) throws SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConfirmationController confirmationController = loader.getController();
		int sons = birdsRepository.birdCountByMotherIdOrFatherId(bird.getId());
		if (sons>0)
			confirmationController.getLbText().setText("O passaro: '" + bird.getBand() + "' tem "+sons+" filhos, tem a certeza que pretende apagar e perder a informacao relativa a linhagem deste passaro?");
		else 
			confirmationController.getLbText().setText("Tem a certeza que quer apagar o passaro: '" + bird.getBand() + "'?");
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_BIRD+bird.getBand());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				birdsRepository.deleteBird(bird);
				ObservableList<Bird> birds = birdsRepository.getAllBirds();
				tableID.setItems(birds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
