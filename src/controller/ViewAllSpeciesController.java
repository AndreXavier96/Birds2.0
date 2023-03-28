package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

import constants.MyValues;
import constants.PathsConstants;
import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.SpeciesRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllSpeciesController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Specie> tableID;
	@FXML
	private TableColumn<Specie,Integer> colId,colIncubationDays,colBandingDays,colOutOfCageDays,colMaturityDays,colBandSize;
	@FXML
	private TableColumn<Specie, String> colCommonName,colScientificName,deleteButton;

	private SpeciesRepository speciesRepository = new SpeciesRepository();

	@FXML
	public void btnBack(ActionEvent event) {
		try {
			root = FXMLLoader.load(Paths.get("resources/views/MainScene.fxml").toUri().toURL());
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SpeciesRepository speciesRepository = new SpeciesRepository();
		ObservableList<Specie> species=FXCollections.observableArrayList();
		try {
			species = speciesRepository.getAllSpecies();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colCommonName.setCellValueFactory(new PropertyValueFactory<Specie,String>("CommonName"));
		colScientificName.setCellValueFactory(new PropertyValueFactory<Specie,String>("ScientificName"));
		colIncubationDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("IncubationDays"));
		colBandingDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("DaysToBand"));
		colOutOfCageDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("OutofCageAfterDays"));
		colMaturityDays.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("MaturityAfterDays"));
		colBandSize.setCellValueFactory(new PropertyValueFactory<Specie,Integer>("BandSize"));
		deleteButton.setCellFactory(new Callback<TableColumn<Specie, String>, TableCell<Specie, String>>() {
			@Override
			public TableCell<Specie, String> call(TableColumn<Specie, String> column) {
				return new TableCell<Specie, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Specie specie = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(specie);
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
		tableID.setItems(species);
	}
	
	private void deleteButtonAction(Specie specie) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar a especie: '" + specie.getCommonName() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_SPECIE+specie.getCommonName());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				speciesRepository.deleteSpecie(specie);
				tableID.getItems().remove(specie);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
