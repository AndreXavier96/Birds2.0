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
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.BreederFederationRepository;
import repository.FederationRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllFederationsController implements Initializable {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TableView<Federation> tableID;
	@FXML
	private TableColumn<Federation, String> colName, colAcronym, colCountry, colEmail, deleteButton;
	@FXML
	private Label LabelAlert;
	
	private FederationRepository federationRepository = new FederationRepository();
	private BreederFederationRepository breederFederationRepository= new BreederFederationRepository();
	
	@FXML
	public void btnBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainScene.fxml"));
			root = loader.load();
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
			                        try {
										deleteButtonAction(federation);
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
//		editButton.setCellFactory(new Callback<TableColumn<Federation, String>, TableCell<Federation, String>>() {
//			@Override
//			public TableCell<Federation, String> call(TableColumn<Federation, String> column) {
//				return new TableCell<Federation, String>() {
//
//					final Button editButton = new Button();
//					 {
//						 ImageView editImage = new ImageView(PathsConstants.EDIT_ICO);
//						 editImage.setFitHeight(16);
//						 editImage.setFitWidth(16);
//						 editButton.setGraphic(editImage);
//						 editButton.setOnAction(new EventHandler<ActionEvent>() {
//			                    @Override
//			                    public void handle(ActionEvent event) {
//			                    	Federation federation = getTableView().getItems().get(getIndex());
//			                    	editButtonAction(federation);
//			                    }
//			                });
//			             }
//					 
//					@Override
//					protected void updateItem(String item, boolean empty) {
//						super.updateItem(item, empty);
//						if (!empty) {
//							setGraphic(editButton);
//						} else {
//							setGraphic(null);
//						}
//					}
//				};
//			}
//		});
		tableID.setItems(federations);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Federation selectedFederation = tableID.getSelectionModel().getSelectedItem();
				if (selectedFederation!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/federation/ViewSingleFederation.fxml"));
						Parent root = loader.load();
						ViewSingleFederationController viewSingleFederationController = loader.getController();
						viewSingleFederationController.search(selectedFederation.getAcronym());
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

	private void deleteButtonAction(Federation federation) throws SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (validatorDelete(federation)) {
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText()
					.setText("Tem a certeza que quer apagar a federacao: '" + federation.getName() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_FEDERATION + federation.getAcronym());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
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
		
	public boolean validatorDelete(Federation federation) throws SQLException {
		boolean validate=false;
		if (breederFederationRepository.federationHasBreeders(federation.getId())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Federacao nao pode ser apagada porque tem criadores associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
		
//		private void editButtonAction(Federation federation) {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/addFederationView.fxml"));
//			Parent root = null;
//			try {
//				root = loader.load();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			AddFederationViewController addFederationViewController = loader.getController();
//			addFederationViewController.startValues(federation);
//			// Show the view using a new window
//			Scene scene = new Scene(root);
//			Stage stage = new Stage();
//			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
//			stage.setScene(scene);
//			stage.initModality(Modality.APPLICATION_MODAL);
//			stage.showAndWait();
//			ObservableList<Federation> federations;
//			try {
//				federations = federationRepository.getAllFederations();
//				tableID.setItems(federations);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
}
