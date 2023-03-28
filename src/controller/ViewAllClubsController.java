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
import domains.Club;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.ClubRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllClubsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Club> tableID;
	@FXML
	private TableColumn<Club,String> colFederation, colName, colAcronym, colLocale, colAddress;
	@FXML
	private TableColumn<Club,String> colContact, colEmail,deleteButton;
	
	private ClubRepository clubRepository = new ClubRepository();
	
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
		
		ObservableList<Club> clubs = FXCollections.observableArrayList();
		try {
			clubs = clubRepository.getAllClubs();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colFederation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFederation().getAcronym()));
		colName.setCellValueFactory(new PropertyValueFactory<Club,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Club,String>("Acronym"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Club,String>("Locale"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Club,String>("Address"));
		colContact.setCellValueFactory(new PropertyValueFactory<Club,String>("Phone"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Club,String>("Email"));
		
		// Set up the delete button column
		deleteButton.setCellFactory(new Callback<TableColumn<Club, String>, TableCell<Club, String>>() {
			@Override
			public TableCell<Club, String> call(TableColumn<Club, String> column) {
				return new TableCell<Club, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                        Club club = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(club);
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

		tableID.setItems(clubs);
	}
	
	// Create a method to handle the delete button action
	private void deleteButtonAction(Club club) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar o clube: '" + club.getName() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_CLUB+club.getAcronym());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				clubRepository.deleteClub(club);
				tableID.getItems().remove(club);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
