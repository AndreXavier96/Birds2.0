package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdTreatmentRepository;
import repository.DataBaseOperationsRepository;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import constants.MyValues;
import domains.BirdTreatment;
import javafx.event.ActionEvent;



public class MainSceneController implements Initializable {
	@FXML
	private GridPane calendarGrid;
	
	private LocalDate calendarDate=LocalDate.now();
	
	private DataBaseOperationsRepository dataBaseOperationsRepository = new DataBaseOperationsRepository();
	private BirdTreatmentRepository birdTreatmentRepository = new BirdTreatmentRepository();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		styleGrid();
		updateCalendar(calendarDate);
	}
	
	private void updateCalendar(LocalDate date) {
		LocalDate startDate = date.minusDays(2);
		LocalDate today = LocalDate.now();
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            LocalDate rowDate = startDate.plusDays(dayIndex);
            Label dateLabel = new Label();
            if (today.equals(rowDate))
            	dateLabel.setText("Hoje");
			else
				dateLabel.setText(rowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            dateLabel.setStyle("-fx-font-weight: bold;"); // Apply styling Label
            dateLabel.setAlignment(Pos.CENTER); // align label to center
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(dateLabel);	// Add the label to the AnchorPane
            calendarGrid.add(anchorPane, dayIndex, 0); // Add the AnchorPane to the GridPane
            AnchorPane.setLeftAnchor(dateLabel, 0.0);	// AnchorPane fill all the cell
            AnchorPane.setRightAnchor(dateLabel, 0.0);
            AnchorPane.setTopAnchor(dateLabel, 0.0);
            AnchorPane.setBottomAnchor(dateLabel, 0.0);
        }
        Date dateStart = Date.from(date.minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFinish = Date.from(date.plusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<BirdTreatment> birdTreatments =birdTreatmentRepository.getBirdTreatmentByFinishDate(dateStart,dateFinish);
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) { 
        	LocalDate rowDate = startDate.plusDays(dayIndex);
        	AnchorPane anchorPane = new AnchorPane();
        	VBox vbox = new VBox();
        	vbox.setAlignment(Pos.CENTER);
        	vbox.setMaxHeight(Double.MAX_VALUE);
        	vbox.setSpacing(5);
        	AnchorPane.setLeftAnchor(vbox, 0.0);
 	        AnchorPane.setRightAnchor(vbox, 0.0);
 	        AnchorPane.setTopAnchor(vbox, 0.0);
 	        AnchorPane.setBottomAnchor(vbox, 0.0);
 	        vbox.setStyle("-fx-padding: 5px;");
        	for (BirdTreatment bt : birdTreatments) {
        	    if (isTreatmentScheduled(bt, rowDate)) {
        	        Label birdTreatmentLabel = new Label(bt.getTreatment().getName() + " : " + bt.getBird().getBand());
        	        birdTreatmentLabel.setStyle("-fx-background-radius: 5px;");
        	        if (today.equals(rowDate))
        	        	birdTreatmentLabel.setStyle("-fx-background-color: #29cf71;");
        			else
        				birdTreatmentLabel.setStyle("-fx-background-color: #d0f5d4;");
        	        vbox.getChildren().add(birdTreatmentLabel);
        	    }
        	}
 	        anchorPane.getChildren().add(vbox);
        	calendarGrid.add(anchorPane, dayIndex, 1);
        	RowConstraints rowConstraints = new RowConstraints();
        	rowConstraints.setVgrow(Priority.ALWAYS);
        	calendarGrid.getRowConstraints().set(1, rowConstraints);
        }
	}
	
	private void styleGrid() {
		for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
			Pane paneRow0 = new Pane();
			paneRow0.setStyle("-fx-background-color: #f3defc;");
			paneRow0.setPrefSize(100, 30); // Set preferred size for the cell
			Pane paneRow1 = new Pane();
			paneRow1.setStyle("-fx-background-color: white;");
			paneRow1.setPrefSize(100, 30); // Set preferred size for the cell

			if (dayIndex == 0) {
				paneRow0.setStyle(paneRow0.getStyle() + "-fx-border-color: black; -fx-border-width: 2 1 0 2;");// top right bottom left
				paneRow1.setStyle(paneRow1.getStyle() + "-fx-border-color: black; -fx-border-width: 1 1 2 2;");
			} else if (dayIndex == 6) {
				paneRow0.setStyle(paneRow0.getStyle() + "-fx-border-color: black; -fx-border-width: 2 2 0 0;");
				paneRow1.setStyle(paneRow1.getStyle() + "-fx-border-color: black; -fx-border-width: 1 2 2 0;");
			} else {
				paneRow0.setStyle(paneRow0.getStyle() + "-fx-border-color: black; -fx-border-width: 2 1 0 0;");
				paneRow1.setStyle(paneRow1.getStyle() + "-fx-border-color: black; -fx-border-width: 1 1 2 0;");
			}

			calendarGrid.add(paneRow0, dayIndex, 0);
			calendarGrid.add(paneRow1, dayIndex, 1);
		}
	}
	 
	private boolean isTreatmentScheduled(BirdTreatment bt, LocalDate currentDate) {
		LocalDate treatmentStartDate = bt.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		switch (bt.getTreatment().getFrequencyType()) {
		case MyValues.HORA:
			for (int d = 0; d < bt.getTreatment().getDurationDays(); d++) {
				LocalDate treatmentDay = treatmentStartDate.plusDays(d);
				if (currentDate.isEqual(treatmentDay))
					return true;
			}
			break;
		case MyValues.DIA:
			for (int d = 0; d < bt.getTreatment().getDurationDays(); d += bt.getTreatment().getFrequency()) {
				LocalDate treatmentDay = treatmentStartDate.plusDays(d);
				if (currentDate.isEqual(treatmentDay))
					return true;
			}
			break;
		case MyValues.SEMANA:
			for (int d = 0; d < bt.getTreatment().getDurationDays() / 7; d++) {
				LocalDate treatmentDay = treatmentStartDate.plusWeeks(d * bt.getTreatment().getFrequency());
				if (currentDate.isEqual(treatmentDay))
					return true;
			}
			break;
		}
		return false;
	}
	 
	private void clearLabels(GridPane gridPane) {
		gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 0);
	}
	
	@FXML
	public void btnBack(ActionEvent event) {
		clearLabels(calendarGrid);
		styleGrid();
		calendarDate = calendarDate.minusDays(1);
		updateCalendar(calendarDate);
	}
	 
	@FXML
	public void btnNext(ActionEvent event) {
		clearLabels(calendarGrid);
		styleGrid();
		calendarDate = calendarDate.plusDays(1);
		updateCalendar(calendarDate); 
	}
	 
	@FXML
	public void btnToday(ActionEvent event) {
		clearLabels(calendarGrid);
		styleGrid();
		calendarDate = LocalDate.now();
		updateCalendar(calendarDate); 
	}
	 
	@FXML
	public void btnAddFederation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/AddFederationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllFederations(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/ViewAllFederations.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchFederation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/federation/ViewSingleFederation.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddClub(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/AddClubView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllClubs(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/ViewAllClubs.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchClub(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clubs/ViewSingleClub.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBreeder(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/AddBreederView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBreeders(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/ViewAllBreeders.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchBreeder(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/breeder/ViewSingleBreeder.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/AddBirdView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBirds(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewAllBirds.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddSpecies(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/AddSpeciesView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnViewAllSpecies(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/ViewAllSpecies.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchSpecie(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/species/ViewSingleSpecie.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddCage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/AddCageView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void BtnViewCages(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewAllCages.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnSearchCage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cages/ViewSingleCage.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddMutation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/AddMutationView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllMutations(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/ViewAllMutations.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnSearchMutation(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mutations/ViewSingleMutation.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/AddCoupleView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ViewAllCouple.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnSearchCouple(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/couples/ViewSingleCouple.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddTreatment(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/AddTreatmentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllTreatment(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/ViewAllTreatments.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnTreatBird(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/BirdTreatmentView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddBrood(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/AddBroodView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnViewAllBrood(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/brood/ViewAllBrood.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void btnAddAward(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/awards/AddAwardView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void btnAddExposithion(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/exibithion/AddExibithionView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	@FXML
	private void btnCreateDB(ActionEvent event) {
		dataBaseOperationsRepository.CreateDataBase(MyValues.DBNAME);
	}
	
	@FXML
	private void btnDropDB(ActionEvent event) {
		System.out.println(System.getProperty("user.dir"));
		try {
			System.out.println("Trying to delete DB...");
			File f =new File("./Database");
			deleteDirectory(f);
			System.out.println("DB deleted!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
