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
import domains.Breeder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.BreederRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAllBreedersController implements Initializable {
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Breeder> tableID;
	@FXML
	private TableColumn<Breeder,String> colName, colEmail, colPostalCode, colLocale, colDistrict, colAddress, colType, colClube,colStam,deleteButton;
	@FXML 
	private TableColumn<Breeder, Integer> colCC,colNIF,colCellphone,colCites;

//	private FederationRepository federationRepository = new FederationRepository();	
	private BreederRepository breederRepository = new BreederRepository();
	
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
		BreederRepository breederRepository = new BreederRepository();
		ObservableList<Breeder> breeders = FXCollections.observableArrayList();;
		try {
			breeders = breederRepository.getAllBreeders();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		colName.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Name"));
		colLocale.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Locale"));
		colType.setCellValueFactory(new PropertyValueFactory<Breeder,String>("Type"));
//		colClube.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Breeder, String>, ObservableValue<String>>() {
//		        @Override
//		        public ObservableValue<String> call(TableColumn.CellDataFeatures<Breeder, String> param) {
//		            StringBuilder sb = new StringBuilder();
//		            for (Club c : param.getValue().getClub()) {
//		            	sb.append("[");
//		            	sb.append(c.getFederation().getAcronym());
//		            	sb.append("]");
//		                sb.append(c.getAcronym());
//		                sb.append(", ");
//		            }
//		            if (sb.length() > 2) {
//		                sb.delete(sb.length() - 2, sb.length());
//		            }
//		            return new SimpleStringProperty(sb.toString());
//		        }
//		    });
//		colStam.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Breeder, String>, ObservableValue<String>>() {
//		    @Override
//		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Breeder, String> param) {
//		        Breeder breeder = param.getValue();
//		        Map<Integer, String> stam = breeder.getStam();
//		        StringBuilder sb = new StringBuilder();
//		        for (Map.Entry<Integer, String> entry : stam.entrySet()) {
//		            Integer federationId = entry.getKey();
//		            String federationStam = entry.getValue();
//		            String federationName = "";
//					try {
//						federationName = federationRepository.getFederationWhereInt("id", federationId).getAcronym();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//		            sb.append("[").append(federationName).append("]").append(federationStam).append(", ");
//		        }
//		        if (sb.length() > 2) {
//		            sb.delete(sb.length() - 2, sb.length());
//		        }
//		        return new SimpleStringProperty(sb.toString());
//		    }
//		});

		deleteButton.setCellFactory(new Callback<TableColumn<Breeder, String>, TableCell<Breeder, String>>() {
			@Override
			public TableCell<Breeder, String> call(TableColumn<Breeder, String> column) {
				return new TableCell<Breeder, String>() {
					final Button deleteButton = new Button("X");
					 {
			                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			                    @Override
			                    public void handle(ActionEvent event) {
			                    	Breeder breeder = getTableView().getItems().get(getIndex());
			                        deleteButtonAction(breeder);
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
		tableID.setItems(breeders);
		tableID.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Breeder selectedBreeder = tableID.getSelectionModel().getSelectedItem();
				if (selectedBreeder != null) {
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/views/breeder/ViewSingleBreeder.fxml"));
						Parent root = loader.load();
						ViewSingleBreederController viewSingleBreederController = loader.getController();
						viewSingleBreederController.startValues(selectedBreeder);
						Scene currentScene = tableID.getScene();
						Stage currentStage = (Stage) currentScene.getWindow();
						currentScene.setRoot(root);
						currentStage.sizeToScene();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	private void deleteButtonAction(Breeder breeder) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar a federacao: '" + breeder.getName() + "'?");

		// Show the view using a new window
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_BREEDER+breeder.getName());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				breederRepository.deleteBreeder(breeder);
				tableID.getItems().remove(breeder);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
