package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

import domains.Bird;
import domains.State;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.BirdsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

public class ViewAllBirdsController implements Initializable {
	@FXML
	private TableView<Bird> tableID;
	@FXML
	private TableColumn<Bird, Integer> colAno;
	@FXML
	private TableColumn<Bird, String> colAnilha,colEntryDate,colEntryType,
		colBuyPrice,colSellPrice,colState,colSex,colFather,colMother,colSpecie,colMutation,
		colCage,colBreeder;
	
	private Parent root;
	private Stage stage;
	private Scene scene;

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


	//dont forget to implements Initializable
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllBirds();
		colAnilha.setCellValueFactory(new PropertyValueFactory<>("Band"));
		colAno.setCellValueFactory(new PropertyValueFactory<>("Year"));
		colEntryDate.setCellValueFactory(new PropertyValueFactory<>("EntryDate"));
		colEntryType.setCellValueFactory(new PropertyValueFactory<>("EntryType"));
		colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("BuyPrice"));
		colSellPrice.setCellValueFactory(cellData ->  new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getState())
	    		.map(State::getValor)
	    		.map(String::valueOf)
	    		.orElse("0")));
		colState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		colSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
	    colFather.setCellValueFactory(cellData -> new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getFather())
                .map(Bird::getBand)
                .orElse("")));
	    colMother.setCellValueFactory(cellData ->  new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getMother())
	    		.map(Bird::getBand)
	    		.orElse("")));
		colSpecie.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getSpecies().getCommonName()));
		colMutation.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getMutations().getName()));
		colCage.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getCage().getCode()));
		colBreeder.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getBreeder().getName()));
		tableID.setItems(birds);
		
	}
}
