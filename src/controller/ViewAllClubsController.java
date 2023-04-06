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
import repository.BreederClubRepository;
import repository.ClubRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	
	@FXML
	private Label LabelAlert;
	
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	
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
			                        try {
										deleteButtonAction(club);
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
//		editButton.setCellFactory(new Callback<TableColumn<Club, String>, TableCell<Club, String>>() {
//			@Override
//			public TableCell<Club, String> call(TableColumn<Club, String> column) {
//				return new TableCell<Club, String>() {
//					final Button editButton = new Button();
//					 {
//						 ImageView editImage = new ImageView(PathsConstants.EDIT_ICO);
//						 editImage.setFitHeight(16);
//						 editImage.setFitWidth(16);
//						 editButton.setGraphic(editImage);
//						 editButton.setOnAction(new EventHandler<ActionEvent>() {
//			                    @Override
//			                    public void handle(ActionEvent event) {
//			                    	Club club = getTableView().getItems().get(getIndex());
//			                    	editButtonAction(club);
//			                    }
//			                });
//			             }
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
		tableID.setItems(clubs);
		tableID.setOnMouseClicked(event -> {
			if(event.getClickCount()==2) {
				Club selectedClub = tableID.getSelectionModel().getSelectedItem();
				if (selectedClub!=null) {
					try {
						FXMLLoader loader =  new FXMLLoader(getClass().getResource("/views/clubs/ViewSingleClub.fxml"));
						Parent root = loader.load();
						ViewSingleClubController viewSingleClubController = loader.getController();
						viewSingleClubController.search(selectedClub.getAcronym());
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
	
	private void deleteButtonAction(Club club) throws SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (validatorDelete(club)) {
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer apagar o clube: '" + club.getName() + "'?");
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
	
	public boolean validatorDelete(Club club) throws SQLException {
		boolean validate=false;
		if (breederClubRepository.clubHasBreeders(club.getId())) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Clube nao pode ser apagado porque tem criadores associados.");
			validate=false;
		}else {
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
//	private void editButtonAction(Club club) {
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/addClubView.fxml"));
//		Parent root = null;
//		try {
//			root = loader.load();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		AddClubViewController addClubViewController = loader.getController();
//		try {
//			addClubViewController.startValuesEdit(club);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		// Show the view using a new window
//		Scene scene = new Scene(root);
//		Stage stage = new Stage();
//		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
//		stage.setScene(scene);
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.showAndWait();
//		ObservableList<Club> clubs;
//		try {
//			clubs = clubRepository.getAllClubs();
//			tableID.setItems(clubs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
