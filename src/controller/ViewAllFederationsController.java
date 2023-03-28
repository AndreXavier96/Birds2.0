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
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.FederationRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllFederationsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Federation> tableID;
	@FXML
	private TableColumn<Federation,String> colName, colAcronym, colCountry, colEmail,deleteButton;
	
	private FederationRepository federationRepository = new FederationRepository();
	
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
		FederationRepository federationRepository = new  FederationRepository();
		ObservableList<Federation> federations = FXCollections.observableArrayList();
		try {
			federations = federationRepository.getAllFederations();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Federation,String>("Name"));
		colAcronym.setCellValueFactory(new PropertyValueFactory<Federation,String>("Acronym"));
		colCountry.setCellValueFactory(new PropertyValueFactory<Federation,String>("Country"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Federation,String>("Email"));
		
		// Set up the delete button column
		deleteButton.setCellFactory(new Callback<TableColumn<Federation, String>, TableCell<Federation, String>>() {
			@Override
			public TableCell<Federation, String> call(TableColumn<Federation, String> column) {
				return new TableCell<Federation, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Federation federation = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(federation);
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
		
		tableID.setItems(federations);
	}
	
	
	// Create a method to handle the delete button action
		private void deleteButtonAction(Federation federation) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = null;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}

			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer apagar a federacao: '" + federation.getName() + "'?");

			// Show the view using a new window
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_CLUB+federation.getAcronym());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					federationRepository.deleteFederation(federation);
					tableID.getItems().remove(federation);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
}
