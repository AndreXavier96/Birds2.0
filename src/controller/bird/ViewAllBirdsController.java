package controller.bird;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import constants.MyValues;
import domains.Bird;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colAno;
	@FXML
	private TableColumn<Bird, String> colAnilha, colState, colSex, colSpecie, colCage, deleteButton;
	@FXML
	private Label LabelAlert;

	@FXML
	private TextField TfFilterAnilha, TfFilterAno, TfFilterEspecie, TfFilterGaiola;
	@FXML
	private ComboBox<String> CbFilterSexo, CbFilterEstado;

	private ObservableList<Bird> birds;

	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) tableID.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CbFilterSexo.setItems(MyValues.SEXLIST);
		CbFilterEstado.setItems(MyValues.STATELIST);
		BirdsRepository birdsRepository = new BirdsRepository();
		birds = birdsRepository.getAllBirds();
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Year"));
		colState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		colSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
		colSpecie.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		colCage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCage().getCode()));
		tableID.setItems(birds);

		// Listeners
		TfFilterAnilha.textProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		TfFilterAno.textProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		TfFilterEspecie.textProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		TfFilterGaiola.textProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		CbFilterSexo.valueProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		CbFilterEstado.valueProperty().addListener((observable, oldValue, newValue) -> filterBirds());
		tableID.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Bird selectedBird = tableID.getSelectionModel().getSelectedItem();
				if (selectedBird != null) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ViewSingleBird.fxml"));
						Parent root = loader.load();
						ViewSingleBirdController viewSingleBirdController = loader.getController();
						viewSingleBirdController.search(selectedBird.getBand());
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

	private void filterBirds() {
		String anilha = TfFilterAnilha.getText().toLowerCase();
		String ano = TfFilterAno.getText();
		String especie = TfFilterEspecie.getText().toLowerCase();
		String gaiola = TfFilterGaiola.getText().toLowerCase();
		String sexo = CbFilterSexo.getValue() != null ? CbFilterSexo.getValue().toLowerCase() : "";
		String estado = CbFilterEstado.getValue() != null ? CbFilterEstado.getValue().toLowerCase() : "";
		Predicate<Bird> predicate = bird -> {
			boolean matchesAnilha = bird.getBand().toLowerCase().contains(anilha);
			boolean matchesAno = ano.isEmpty() || Integer.toString(bird.getYear()).contains(ano);
			boolean matchesEspecie = bird.getSpecies().getCommonName().toLowerCase().contains(especie);
			boolean matchesGaiola = bird.getCage().getCode().toLowerCase().contains(gaiola);
			boolean matchesSexo = sexo.isEmpty() || bird.getSex().toLowerCase().equals(sexo);
			boolean matchesEstado = estado.isEmpty() || bird.getState().getType().toLowerCase().equals(estado);
			return matchesAnilha && matchesAno && matchesEspecie && matchesGaiola && matchesSexo && matchesEstado;
		};

		ObservableList<Bird> filteredBirds = birds.stream().filter(predicate)
				.collect(Collectors.toCollection(FXCollections::observableArrayList));
		tableID.setItems(filteredBirds);
	}

	@FXML
	private void clearFilter() {
		// Reset all filter fields to default values
		TfFilterAnilha.clear();
		TfFilterAno.clear();
		TfFilterEspecie.clear();
		TfFilterGaiola.clear();
		CbFilterSexo.setValue(null);
		CbFilterEstado.setValue(null);

		// Restore the original list of birds
		tableID.setItems(birds);
	}
}
