package controller.award;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import repository.AwardRepository;
import repository.BirdsRepository;
import repository.ExibithionRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import constants.MyValues;
import constants.PathsConstants;
import constants.Regex;
import controller.GenericController;
import controller.bird.ViewSingleBirdController;
import domains.Award;
import domains.Bird;
import domains.Exibithion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class AddAwardViewController implements Initializable {
	@FXML
	private Label LabelAlert,LbTitle,LbImagePath,LbImageName;
	@FXML
	private TextField TfPontuation, TfName,TfBand;
	@FXML
	private ListView<Exibithion> LvName ;
	@FXML
	private ListView<Bird> LvBand;
	@FXML
	private Button btnUpload,btnClose;

	private ExibithionRepository exibithionRepository = new ExibithionRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	private AwardRepository awardRepository = new AwardRepository();
	
	@FXML
	private ViewSingleBirdController viewSingleBirdController;
	
	private boolean imageUploaded=false;
	
	public void setViewSingleBirdController(ViewSingleBirdController controller) {
		this.viewSingleBirdController = controller;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		EventHandler<MouseEvent> showAllEvent = event -> {
			String searchTerm = TfName.getText();
			ObservableList<Exibithion> listExibithions = filterExibithions(GenericController.replacePortugueseSpecialCharacters(searchTerm));
			LvName.setItems(listExibithions);
			LvName.setVisible(true); // Show the ListView
		};
		TfName.setOnMouseClicked(showAllEvent);
		TfName.textProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Exibithion> listExibithions = filterExibithions(GenericController.replacePortugueseSpecialCharacters(newValue));
			LvName.setItems(listExibithions);
			LvName.setVisible(true); // Show the ListView
		});
		LvName.setCellFactory(param -> new ListCell<Exibithion>() {
			@Override
			protected void updateItem(Exibithion exibithion, boolean empty) {
				super.updateItem(exibithion, empty);
				if (empty || exibithion == null) {
					setText(null);
				} else {
					setText(exibithion.getName());
				}
			} // Show only name
		});
		LvName.getSelectionModel().selectedItemProperty()
				.addListener((observableSelection, oldValueSelection, newValueSelection) -> {
					if (newValueSelection != null) {
						TfName.setText(newValueSelection.getName());// Select Value
						LvName.setVisible(false); // Hide the ListView after selecting a value
					}
				});

		EventHandler<MouseEvent> showAllEventBand = event -> {
			String searchTerm = TfBand.getText();
			ObservableList<Bird> listBirds = filterBirds(searchTerm);
			LvBand.setItems(listBirds);
			LvBand.setVisible(true); // Show the ListView
		};
		TfBand.setOnMouseClicked(showAllEventBand);
		TfBand.textProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Bird> listBirds = filterBirds(newValue);
			LvBand.setItems(listBirds);
			LvBand.setVisible(true); // Show the ListView
		});
		LvBand.setCellFactory(param -> new ListCell<Bird>() {
			@Override
			protected void updateItem(Bird bird, boolean empty) {
				super.updateItem(bird, empty);
				if (empty || bird == null) {
					setText(null);
				} else {
					setText(bird.getBand());
				}
			} // Show only Band as String
		});
		LvBand.getSelectionModel().selectedItemProperty().addListener((observableSelection, oldValueSelection, newValueSelection) -> {
				if (newValueSelection != null) {
					TfBand.setText(newValueSelection.getBand());// Select Value
					LvBand.setVisible(false); // Hide the ListView after selecting a value
				}
			});

	}
	
	private ObservableList<Exibithion> filterExibithions(String searchTerm) {
		ObservableList<Exibithion> listExibithions = exibithionRepository.getAllExibithions();
		List<Exibithion> filteredExibithions = listExibithions.stream()
				.filter(exibithion -> exibithion.getName().toLowerCase().contains(searchTerm.toLowerCase()))
				.collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredExibithions);
	}
	
	private ObservableList<Bird> filterBirds(String searchTerm) {
		ObservableList<Bird> listBirds = birdsRepository.getAllBirds();
		 List<Bird> filteredBirds = listBirds.stream()
		            .filter(bird -> bird.getBand().toLowerCase().contains(searchTerm.toLowerCase()))
		            .collect(Collectors.toList());
		return FXCollections.observableArrayList(filteredBirds);
	}
	
	public void startValues(String band) {
		TfBand.setText(band);
		TfBand.setDisable(true);
		LvBand.setVisible(false);
	}
	
	@FXML
	public void btnAdd(ActionEvent event) throws NumberFormatException, SQLException {
		if (validator()) {
			LabelAlert.setStyle(null);
			Award a = new Award();
			a.setPontuation(Integer.parseInt(TfPontuation.getText()));
			a.setExibithion(exibithionRepository.getExibithionByName(TfName.getText()));
			a.setBird(birdsRepository.getBirdWhereString("Band", TfBand.getText()));
			if (imageUploaded) {
				File defaultFolder = new File(PathsConstants.DEFAULT_PATH_TO_SAVE_IMAGE_JUDJMENT );
				File selectedFile = new File(LbImagePath.getText());
				try {
					String fileName = TfBand.getText()+"_"+a.getExibithion().getName()+ selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
					Files.copy(selectedFile.toPath(), defaultFolder.toPath().resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
					a.setJudgmentImagePath("file:"+defaultFolder+"\\"+fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			awardRepository.Insert(a);
			btnCancel(event);
			viewSingleBirdController.setSuccess(MyValues.AWARD_SUCCESS, birdsRepository.getBirdWhereString("Band",a.getBird().getBand()));
		}
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void btnUploadImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(MyValues.TITLE_SELECT_JULGAMENTO);
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile!=null) {
			LbImagePath.setText(selectedFile.toPath().toString());
			LbImageName.setText(selectedFile.getName());
			imageUploaded=true;
		}
	}

	public boolean validator() throws NumberFormatException, SQLException {
		boolean validated = false;
		clearAllErrors();
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		LabelAlert.setText("");

		if (TfPontuation.getText().length() == 0) {
			TfPontuation.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Pontuacao tem de ser preenchida.");
			validated = false;
		} else if (!TfPontuation.getText().matches(Regex.INT)) {
			TfPontuation.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Pontuacao nao esta no formato correto.");
			validated = false;
		} else {
			TfPontuation.setStyle(null);
			LabelAlert.setText("");
			validated = true;
		}

		if (validated) {
			if (TfName.getText().length() == 0) {
				TfName.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nome tem de ser preenchido.");
				validated = false;
			} else {
				TfName.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		if (validated) {
			if (TfBand.getText().length() == 0) {
				TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha tem de ser preenchido.");
				validated = false;
			} else if (!TfBand.getText().matches(Regex.FULL_BAND)) {
				TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Anilha nao esta no formato correto.");
				validated = false;
			} else if (birdsRepository.getBirdWhereString("Band", TfBand.getText().toUpperCase()) == null) {
				TfBand.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Passaro nao existe.");
				validated = false;
			} else {
				TfBand.setStyle(null);
				LabelAlert.setText("");
				validated = true;
			}
		}
		
		return validated;
	}
		
	public void clearAllErrors() {
		TfName.setStyle(null);
		TfPontuation.setStyle(null);
		btnUpload.setStyle(null);
		TfBand.setStyle(null);
	}
	
	public void clearAllFields() {
		TfName.setText("");
		TfPontuation.setText("");
		LbImagePath.setText("");
		TfBand.setText("");
		clearAllErrors();
	}

}
