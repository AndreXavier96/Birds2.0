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
import repository.BirdsRepository;
import repository.MutationsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	@FXML
	private Label LabelAlert;
	
	private MutationsRepository mutationsRepository = new MutationsRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MutationsRepository mutationsRepository = new MutationsRepository();
		ObservableList<Mutation> mutations=FXCollections.observableArrayList();
		try {
			mutations = mutationsRepository.getAllMutations();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Mutation,String>("Name"));
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
			                        try {
										deleteButtonAction(mutation);
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
		tableID.setItems(mutations);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Mutation selectedMutation = tableID.getSelectionModel().getSelectedItem();
				if (selectedMutation!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/mutations/ViewSingleMutation.fxml"));
						Parent root = loader.load();
						ViewSingleMutationController viewSingleMutationController = loader.getController();
						viewSingleMutationController.search(selectedMutation.getName());
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
	
	private void deleteButtonAction(Mutation mutation) throws SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (validatorDelete(mutation)) {
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText()
					.setText("Tem a certeza que quer apagar a mutacao: '" + mutation.getName() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_MUTATION + mutation.getName());
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
	}
	
	public boolean validatorDelete(Mutation mutation) throws SQLException {
		boolean validate=false;
		if (birdsRepository.getBirdWhereInt("MutationsId", mutation.getId()) != null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Mutacao nao pode ser apagada porque tem passaros associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
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
