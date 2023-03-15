package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;
import constants.MyValues;
import constants.PathsConstants;
import domains.Bird;
import domains.Historic;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.HistoricRepository;

public class ViewSingleBirdController{
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuFile;
	
	@FXML
	private Label LabelAlert;
	@FXML
	private Label LbTitle,LbBand,LbYear,LbEntryDate,LbEntryType;
	@FXML
	private Label LbSex,LbBuyPrice,LbSpecie,LbMutation,LbClassification;
	@FXML
	private Label LbSellPrice,LbState,LbCage, LbBreeder;
	@FXML
	private TextArea LbObs;
	@FXML
	private ImageView ImBird;
	@FXML
	private TextField TfBandSearch;
	
	@FXML
	private Label LbBandFather,LbBandMother;
	@FXML
	private ImageView ImFather,ImMother;
	@FXML
	private Label LbBandGrandFatherFather,LbBandGrandMotherFather;
	@FXML
	private ImageView ImGrandFatherFather,ImGrandMotherFather;
	@FXML
	private Label LbBandGrandFatherMother,LbBandGrandMotherMother;
	@FXML
	private ImageView ImGrandFatherMother,ImGrandMotherMother;
	
	@FXML
	private AnchorPane ApBuyPrice, ApSellPrice;
	
	@FXML
	private TableView<Bird> TbDescendants;
	
	@FXML
	private TableColumn<Bird, String> TcBand,TcFather,TcMother,TcState,
		TcCage,TcBrooding,TcMutation,TcBreeder;
	
	@FXML
	private TableView<Historic> TableHistoric;
	@FXML
	private TableColumn<Historic, String> colTitle,colDate,colObs;
	
	BirdsRepository birdsRepository=new BirdsRepository();
	HistoricRepository historicRepository = new HistoricRepository();
	
	@FXML
	private void btnChangeBirdState() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChangeBirdState.fxml"));
		Parent root = loader.load();
	    ChangeBirdStateController birdStateController = loader.getController();
	    birdStateController.startValues(LbBand.getText(),LbState.getText(),ImBird.getImage().getUrl());
	    birdStateController.setViewSingleBirdController(this);
	    Scene scene = new Scene(root);
	    Stage stage = new Stage();
	    stage.setTitle(MyValues.TITLE_CHANGE_STATE);
	    stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
	    stage.setScene(scene);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.showAndWait();
	}
	
	@FXML
	private void btnChangeBirdCage() throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChangeBirdCage.fxml"));
		Parent root = loader.load();
		ChangeBirdCageController birdCageController = loader.getController();
		birdCageController.startValues(LbBand.getText(), ImBird.getImage().getUrl(), LbCage.getText());
		birdCageController.setViewSingleBirdController(this);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_CHANGE_CAGE);
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
	
	@FXML
	private void btnChangeBirdSex() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChangeBirdSex.fxml"));
		Parent root = loader.load();
		ChangeBirdSexController birdSexController = loader.getController();
		birdSexController.startValues(LbBand.getText(), ImBird.getImage().getUrl(), LbSex.getText());
		birdSexController.setViewSingleBirdController(this);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_CHANGE_CAGE);
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
	
	@FXML
	public void btnSearchForBand(ActionEvent event) {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Bird b = birdsRepository.getBirdWhereString("Band",TfBandSearch.getText());
			updateAllInfo(b);
		}
	}
	
	public void search(String band) {
		clearAllFields();
		TfBandSearch.setText(band);
		Bird b = birdsRepository.getBirdWhereString("Band",band);
		updateAllInfo(b);
	}
	
	public void setSuccess(String msg, Bird bird) {
		LabelAlert.setStyle(MyValues.ALERT_SUCESS);
		LabelAlert.setText(msg);
		updateAllInfo(bird);
	}
	
	public void updateAllInfo(Bird b) {
		personalInfo(b);
		affiliation(b);
		descendants(b);
		historic(b);
	}
	
	public void historic(Bird b) {
		colTitle.setCellValueFactory(new PropertyValueFactory<Historic, String>("title"));
	    colDate.setCellValueFactory(new PropertyValueFactory<Historic, String>("date"));
	    colObs.setCellValueFactory(new PropertyValueFactory<Historic, String>("obs"));
	    ObservableList<Historic> historics=FXCollections.observableArrayList();
		try {
			historics = historicRepository.getAllByBirdId(b.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    TableHistoric.setItems(historics);
	}
	
	public void affiliation(Bird b) {
		if (b.getFather() != null) {
			LbBandFather.setText(b.getFather().getBand());
			if (!b.getFather().getImage().isEmpty()) {
				String s = b.getFather().getImage();
				if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
					ImFather.setImage(new Image(b.getFather().getImage()));
				else
					ImFather.setImage(PathsConstants.DEFAULT_IMAGE);
			}
			if (b.getFather().getFather() != null) {
				LbBandGrandFatherFather.setText(b.getFather().getFather().getBand());
				if (!b.getFather().getFather().getImage().isEmpty()) {
					String s = b.getFather().getFather().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandFatherFather.setImage(new Image(b.getFather().getFather().getImage()));
					else
						ImGrandFatherFather.setImage(PathsConstants.DEFAULT_IMAGE);
				}
			}
			if (b.getFather().getMother() != null) {
				LbBandGrandFatherMother.setText(b.getFather().getMother().getBand());
				if (!b.getFather().getMother().getImage().isEmpty()) {
					String s = b.getFather().getMother().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandFatherMother.setImage(new Image(b.getFather().getMother().getImage()));
					else
						ImGrandFatherMother.setImage(PathsConstants.DEFAULT_IMAGE);
				}
			}
		}
		
		if (b.getMother() != null) {
			LbBandMother.setText(b.getMother().getBand());
			if (!b.getMother().getImage().isEmpty()) {
				String s = b.getMother().getImage();
				if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
					ImMother.setImage(new Image(b.getMother().getImage()));
				else
					ImMother.setImage(PathsConstants.DEFAULT_IMAGE);
			}
			if (b.getMother().getFather() != null) {
				LbBandGrandMotherFather.setText(b.getMother().getFather().getBand());
				if (!b.getMother().getFather().getImage().isEmpty()) {
					String s = b.getMother().getFather().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandMotherFather.setImage(new Image(b.getMother().getFather().getImage()));
					else
						ImGrandMotherFather.setImage(PathsConstants.DEFAULT_IMAGE);
				}
			}
			if (b.getMother().getMother() != null) {
				LbBandGrandMotherMother.setText(b.getMother().getMother().getBand());
				if (!b.getMother().getMother().getImage().isEmpty()) {
					String s = b.getMother().getMother().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandMotherMother.setImage(new Image(b.getMother().getMother().getImage()));
					else
						ImGrandMotherMother.setImage(PathsConstants.DEFAULT_IMAGE);
				}
			}
		}
	}

	public void personalInfo(Bird b) {
			LbTitle.setText("Bird "+b.getBand());
			LbBand.setText(b.getBand());
			LbSpecie.setText(b.getSpecies().getCommonName());
			LbMutation.setText(b.getMutations().getName());
			LbYear.setText(b.getYear().toString());
			LbSex.setText(b.getSex());
			LbClassification.setText("TODO");
			LbEntryDate.setText(b.getEntryDate().toString());
			LbCage.setText(b.getCage().getCode());
			LbEntryType.setText(b.getEntryType());
			LbState.setText(b.getState().getType());
			if (LbEntryType.getText().equals(MyValues.COMPRA)) {
				LbBuyPrice.setText(b.getBuyPrice().toString()+"\u20AC");
				ApBuyPrice.setVisible(true);
			}else 
				ApBuyPrice.setVisible(false);
			if (LbState.getText().equals(MyValues.VENDIDO)) {
				LbSellPrice.setText(b.getState().getValor().toString()+"\u20AC");
				ApSellPrice.setVisible(true);
			}else
				ApSellPrice.setVisible(false);
			LbObs.setText(b.getObs());
			LbBreeder.setText(b.getBreeder().getName());
			if (!b.getImage().isEmpty()) {
				String s = b.getImage();
				if (Files.exists(Paths.get(s.substring(s.indexOf(':')+1))))
					ImBird.setImage(new Image(b.getImage()));
				else
					ImBird.setImage(PathsConstants.DEFAULT_IMAGE);
			}
			
	}
	
	public void descendants(Bird b) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllWhereIntOrWhereInt("Father",b.getId(),"Mother",b.getId());
		TcBand.setCellValueFactory(new PropertyValueFactory<>("Band"));
		TcFather.setCellValueFactory(cellData -> new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getFather())
                .map(Bird::getBand)
                .orElse("")));
		TcMother.setCellValueFactory(cellData ->  new SimpleStringProperty(Optional.ofNullable(cellData.getValue().getMother())
	    		.map(Bird::getBand)
	    		.orElse("")));
		TcState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		TcCage.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getCage().getCode()));
//		TcBrooding
		TcMutation.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getMutations().getName()));
		TcBreeder.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getBreeder().getName()));
		TbDescendants.setItems(birds);
	}
	
	public boolean validator() {
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		boolean validate = false;
		if (TfBandSearch.getText().length()==0) {
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha tem de ser preenchido");
			validate=false;
		}else if (birdsRepository.getBirdWhereString("Band",TfBandSearch.getText())==null) {
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha nao existe");
			validate=false;
		}else {
			TfBandSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Bird STAM yyyy 0000");
		LbBand.setText(null);
		LbYear.setText(null);
		LbEntryDate.setText(null);
		LbEntryType.setText(null);
		LbSex.setText(null);
		LbBuyPrice.setText(null);
		ApBuyPrice.setVisible(false);
		LbSellPrice.setText(null);
		ApSellPrice.setVisible(false);
		LbSpecie.setText(null);
		LbMutation.setText(null);
		LbClassification.setText(null);
		LbState.setText(null);
		LbObs.setText(null);
		LbCage.setText(null);
		LbBreeder.setText(null);
		ImBird.setImage(PathsConstants.DEFAULT_IMAGE);

		LbBandFather.setText(null);
		LbBandMother.setText(null);
		LbBandGrandFatherFather.setText(null);
		LbBandGrandMotherFather.setText(null);
		LbBandGrandFatherMother.setText(null);
		LbBandGrandMotherMother.setText(null);
		ImFather.setImage(PathsConstants.DEFAULT_IMAGE);
		ImMother.setImage(PathsConstants.DEFAULT_IMAGE);
		ImGrandFatherFather.setImage(PathsConstants.DEFAULT_IMAGE);
		ImGrandMotherFather.setImage(PathsConstants.DEFAULT_IMAGE);
		ImGrandFatherMother.setImage(PathsConstants.DEFAULT_IMAGE);
		ImGrandMotherMother.setImage(PathsConstants.DEFAULT_IMAGE);

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
