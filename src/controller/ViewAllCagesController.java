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
import domains.Cage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.CageRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllCagesController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Cage> tableID;
	@FXML
	private TableColumn<Cage,String> colCode,colType,colBird,deleteButton;

	private CageRepository cageRepository = new CageRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CageRepository cageRepository = new CageRepository();
		ObservableList<Cage> cages = FXCollections.observableArrayList();
		try {
			cages = cageRepository.getAllCages();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colCode.setCellValueFactory(new PropertyValueFactory<Cage,String>("Code"));
		colType.setCellValueFactory(new PropertyValueFactory<Cage,String>("Type"));
		
		deleteButton.setCellFactory(new Callback<TableColumn<Cage, String>, TableCell<Cage, String>>() {
			@Override
			public TableCell<Cage, String> call(TableColumn<Cage, String> column) {
				return new TableCell<Cage, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Cage cage = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(cage);
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
		
		tableID.setItems(cages);
	}
	
	private void deleteButtonAction(Cage cage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar a federacao: '" + cage.getCode() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_CAGE+cage.getCode());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				cageRepository.deleteCage(cage);
				tableID.getItems().remove(cage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
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

	
}
