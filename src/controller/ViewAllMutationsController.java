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
import domains.Mutation;
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
import repository.MutationsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllMutationsController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Mutation> tableID;
	@FXML
	private TableColumn<Mutation,String> colName,colVar1,colVar2,colVar3,colObs,colObservation;
	@FXML
	private TableColumn<Mutation,String> colSpecie,deleteButton;

	private MutationsRepository mutationsRepository = new MutationsRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MutationsRepository mutationsRepository = new MutationsRepository();
		ObservableList<Mutation> mutations=FXCollections.observableArrayList();
		try {
			mutations = mutationsRepository.getAllMutations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Name"));
		colVar1.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var1"));
		colVar2.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var3"));
		colVar3.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Var2"));
		colObs.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Obs"));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecie().getCommonName()));
		deleteButton.setCellFactory(new Callback<TableColumn<Mutation, String>, TableCell<Mutation, String>>() {
			@Override
			public TableCell<Mutation, String> call(TableColumn<Mutation, String> column) {
				return new TableCell<Mutation, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Mutation mutation = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(mutation);
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
		tableID.setItems(mutations);
	}
	
	private void deleteButtonAction(Mutation mutation) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar a federacao: '" + mutation.getName() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_MUTATION+mutation.getName());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				mutationsRepository.deleteMutation(mutation);
				tableID.getItems().remove(mutation);
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
