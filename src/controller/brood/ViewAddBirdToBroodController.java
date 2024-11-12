package controller.brood;

import javafx.fxml.FXML;
import java.sql.SQLException;
import java.util.Date;

import constants.MyValues;
import controller.bird.AddBirdViewController;
import domains.Bird;
import domains.Brood;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.BroodRepository;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAddBirdToBroodController {
	
	@FXML
	private Label LbFather, LbMother,LabelAlert;
	
	@FXML
	private TableView<Brood> tableID;
	
	@FXML
	private TableColumn<Brood,String> colEggsFree,colEggsTotal;
	
	@FXML
	private TableColumn<Brood,Date> colStart,colFinish;

	@FXML
	private AddBirdViewController addBirdViewController;
	
	private BroodRepository broodRepository = new BroodRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	
	private ObservableList<Brood> allBroodsListFromCouple;
	private Bird father;
	private Bird mother;
	
	public void setViewAddBirdToBroodController(AddBirdViewController controller, String fatherBand,String motherBand, Date selectedDate) {
		this.addBirdViewController=controller;
		this.father = birdsRepository.getBirdWhereString("Band", fatherBand);
		this.mother = birdsRepository.getBirdWhereString("Band", motherBand);
		this.allBroodsListFromCouple = broodRepository.getAllBroodsFromCouple(this.father.getId(), this.mother.getId(),selectedDate);
		startValues();
	}

	

	public void startValues() {
		LbFather.setText(father.getBand());
		LbMother.setText(mother.getBand());
		tableID.setItems(allBroodsListFromCouple);
		colEggsTotal.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTotalEggs())));
		colEggsFree.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getChocadoEggsCount())));
		colStart.setCellValueFactory(new PropertyValueFactory<Brood, Date>("start"));
		colFinish.setCellValueFactory(new PropertyValueFactory<Brood, Date>("finish"));

		tableID.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Brood selectedBrood = tableID.getSelectionModel().getSelectedItem();
				if (selectedBrood != null) {
					int freeEggs = selectedBrood.getChocadoEggsCount();
					if (freeEggs <= 0) {
						showAlert("Sem Ovos Disponiveis", "A ninhada selecionada nao tem ovos disponiveis!");
					} else {
						Stage currentStage = (Stage) LbFather.getScene().getWindow();
						currentStage.close();
						try {
							addBirdViewController.setEggChose(selectedBrood.toString() + " selecionada com sucesso!",selectedBrood);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}); 
		if (allBroodsListFromCouple==null || allBroodsListFromCouple.isEmpty()) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			LabelAlert.setText("Data Entrada Passaro nao coincide com nenhuma ninhada para o casal selecionado");
		}
	}

	private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}
}
