package controller.bird;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import controller.HiperligacoesController;
import controller.award.AddAwardViewController;
import controller.award.ViewSingleAwardController;
import domains.Award;
import domains.Bird;
import domains.BirdTreatment;
import domains.Couples;
import domains.Historic;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.AwardRepository;
import repository.BirdTreatmentRepository;
import repository.BirdsRepository;
import repository.CouplesRepository;
import repository.HistoricRepository;
import repository.TreatmentRepository;

public class ViewSingleBirdController implements Initializable{
	
	@FXML
	private TabPane TabPane;
	@FXML
	private Tab TabPersonalInfo;
	
	@FXML
	private Label LabelAlert;
	@FXML
	private Label LbTitle,LbBand,LbYear,LbEntryDate,LbEntryType,LbCouple;
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
	private AnchorPane ApBuyPrice, ApSellPrice,ApCouple;
	
	@FXML
	private TableView<Bird> TbDescendants;
	@FXML
	private TableColumn<Bird, String> TcBand,TcFather,TcMother,TcState,
		TcCage,TcBrooding,TcMutation,TcBreeder;
	
	@FXML
	private TableView<BirdTreatment> TbTreatments;
	@FXML
	private TableColumn<BirdTreatment, String> TcTreatmentName,TcTreatmentDesc,TcTreatmentStart,TcTreatmentEnd;
	
	@FXML
	private TableView<Historic> TableHistoric;
	@FXML
	private TableColumn<Historic, String> colTitle,colDate,colObs;
	
	@FXML
	private TableView<Award> TbAwards;
	@FXML
	private TableColumn<Award, String> TcExibithion,TcPontuation;
	
	BirdsRepository birdsRepository=new BirdsRepository();
	HistoricRepository historicRepository = new HistoricRepository();
	CouplesRepository couplesRepository = new CouplesRepository();
	TreatmentRepository treatmentRepository = new TreatmentRepository();
	BirdTreatmentRepository birdTreatmentRepository = new BirdTreatmentRepository();
	AwardRepository awardRepository = new AwardRepository();
	
	private HiperligacoesController hiperligacoes = new HiperligacoesController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LbSpecie.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSpecie(LbTitle.getScene(),LbSpecie.getText());
		});
		LbBreeder.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewBreeder(LbTitle.getScene(),LbBreeder.getText());
		});
		LbMutation.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewMutation(LbTitle.getScene(),LbMutation.getText());
		});
		LbCage.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewCage(LbTitle.getScene(),LbCage.getText());
		});
		LbCouple.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSingleCouple(LbTitle.getScene(),LbCouple.getText());
		});
		
		TbAwards.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Award selectedAward = TbAwards.getSelectionModel().getSelectedItem();
				if (selectedAward != null) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/awards/ViewSingleAward.fxml"));
						Parent root = loader.load();
						ViewSingleAwardController viewSingleAwardController = loader.getController();
						viewSingleAwardController.search(selectedAward.getId());
						Scene currentScene = TbAwards.getScene();
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
	
	
	@FXML
	private void btnAward() throws IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/awards/AddAwardView.fxml"));
			Parent root = loader.load();
			AddAwardViewController addAwardViewController = loader.getController();
			addAwardViewController.startValues(LbBand.getText());
			addAwardViewController.setViewSingleBirdController(this);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
	}
	
	
	@FXML
	private void btnChangeBirdState() throws IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ChangeBirdState.fxml"));
			Parent root = loader.load();
			ChangeBirdStateController birdStateController = loader.getController();
			birdStateController.startValues(LbBand.getText(), LbState.getText(), ImBird.getImage().getUrl());
			birdStateController.setViewSingleBirdController(this);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_CHANGE_STATE);
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
	}
	
	@FXML
	private void btnChangeBirdCage() throws IOException, SQLException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ChangeBirdCage.fxml"));
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
	}
	
	@FXML
	private void btnChangeBirdSex() throws IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ChangeBirdSex.fxml"));
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
	}
	
	@FXML
	private void btnChangeBirdPhoto() throws IOException {
		if (validator() && validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/birds/ChangeBirdPhoto.fxml"));
			Parent root = loader.load();
			ChangeBirdPhotoController birdPhotoController = loader.getController();
			birdPhotoController.startValues(LbBand.getText(), ImBird.getImage().getUrl());
			birdPhotoController.setViewSingleBirdController(this);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_CHANGE_IMAGE);
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		}
	}
	
	@FXML
	private void btnDeleteBird() throws IOException {
		if (validator()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = loader.load();
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText()
					.setText("Tem a certeza que quer apagar este passaro: '" + LbBand.getText() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_DELETE_BIRD + LbBand.getText());
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					Bird bird = birdsRepository.getBirdWhereString("Band", LbBand.getText());
					birdsRepository.deleteBird(bird);
					clearAllFields();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void btnSearchForBand(ActionEvent event) {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Bird b = birdsRepository.getBirdWhereString("Band",TfBandSearch.getText().toUpperCase());
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
		treatments(b);
		awards(b);
	}
	
	public void personalInfo(Bird b) {
		LbTitle.setText("Bird "+b.getBand());
		LbBand.setText(b.getBand());
		try {
			Couples c = couplesRepository.getCouplesWhereBird(b);
			if (c != null) {
				ApCouple.setVisible(true);
				LbCouple.setStyle("-fx-underline:true;");
				LbCouple.setCursor(Cursor.HAND);
				if (c.getMale() == b)
					LbCouple.setText(c.getFemale().getBand());
				else
					LbCouple.setText(c.getMale().getBand());
			}else {
				LbCouple.setStyle("-fx-underline:false;");
				LbCouple.setCursor(Cursor.DEFAULT);
				ApCouple.setVisible(false);
				LbCouple.setText(MyValues.NO_COUPLE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			LbBandFather.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					search(LbBandFather.getText());
					TabPane.getSelectionModel().select(TabPersonalInfo);
				}
			});
	
			if (b.getFather().getFather() != null) {
				LbBandGrandFatherFather.setText(b.getFather().getFather().getBand());
				if (!b.getFather().getFather().getImage().isEmpty()) {
					String s = b.getFather().getFather().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandFatherFather.setImage(new Image(b.getFather().getFather().getImage()));
					else
						ImGrandFatherFather.setImage(PathsConstants.DEFAULT_IMAGE);
				}
				LbBandGrandFatherFather.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2) {
						search(LbBandGrandFatherFather.getText());
						TabPane.getSelectionModel().select(TabPersonalInfo);
					}
				});
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
				LbBandGrandFatherMother.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2) {
						search(LbBandGrandFatherMother.getText());
						TabPane.getSelectionModel().select(TabPersonalInfo);
					}
				});
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
			LbBandMother.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2) {
					search(LbBandMother.getText());
					TabPane.getSelectionModel().select(TabPersonalInfo);
				}
			});
	
			if (b.getMother().getFather() != null) {
				LbBandGrandMotherFather.setText(b.getMother().getFather().getBand());
				if (!b.getMother().getFather().getImage().isEmpty()) {
					String s = b.getMother().getFather().getImage();
					if (Files.exists(Paths.get(s.substring(s.indexOf(':') + 1))))
						ImGrandMotherFather.setImage(new Image(b.getMother().getFather().getImage()));
					else
						ImGrandMotherFather.setImage(PathsConstants.DEFAULT_IMAGE);
				}
				LbBandGrandMotherFather.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2) {
						search(LbBandGrandMotherFather.getText());
						TabPane.getSelectionModel().select(TabPersonalInfo);
					}
				});
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
				LbBandGrandMotherMother.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2) {
						search(LbBandGrandMotherMother.getText());
						TabPane.getSelectionModel().select(TabPersonalInfo);
					}
				});
			}
		}
	}
	
	public void descendants(Bird b) {
		BirdsRepository birdsRepository = new BirdsRepository();
		ObservableList<Bird> birds = birdsRepository.getAllWhereIntOrWhereInt("Father", b.getId(), "Mother", b.getId());
		TcBand.setCellValueFactory(new PropertyValueFactory<>("Band"));
		TcFather.setCellValueFactory(cellData -> new SimpleStringProperty(
				Optional.ofNullable(cellData.getValue().getFather()).map(Bird::getBand).orElse("")));
		TcMother.setCellValueFactory(cellData -> new SimpleStringProperty(
				Optional.ofNullable(cellData.getValue().getMother()).map(Bird::getBand).orElse("")));
		TcState.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getType()));
		TcCage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCage().getCode()));
//		TcBrooding
		TcMutation.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getMutations().getName()));
		TcBreeder.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBreeder().getName()));
		TbDescendants.setItems(birds);
		TbDescendants.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				Bird selectedBird = TbDescendants.getSelectionModel().getSelectedItem();
				if (selectedBird != null) {
					search(selectedBird.getBand());
					TabPane.getSelectionModel().select(TabPersonalInfo);
				}
			}
		});
	}
	
	public void treatments(Bird b) {
		ObservableList<BirdTreatment> birdTreatments = birdTreatmentRepository.getBirdTreatmentByBird(b);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		TcTreatmentName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTreatment().getName()));
		TcTreatmentDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTreatment().getDescription()));
		TcTreatmentStart.setCellValueFactory(cellData -> {
		    Date startDate = cellData.getValue().getStart();
		    String formattedStartDate = dateFormat.format(startDate);
		    return new SimpleStringProperty(formattedStartDate);
		});
		TcTreatmentEnd.setCellValueFactory(cellData -> {
		    Date finishDate = cellData.getValue().getFinish();
		    String displayText = (finishDate != null) ? dateFormat.format(finishDate) : MyValues.EM_TRATAMENTO;
		    return new SimpleStringProperty(displayText);
		});
		TbTreatments.setItems(birdTreatments);
	}
	
	public void awards(Bird b) {
		ObservableList<Award> birdAwards = awardRepository.getAwardsByBird(b);
		TcExibithion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExibithion().getName()));
		TcPontuation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPontuation().toString()));
		TbAwards.setItems(birdAwards);
	}
	
	public void historic(Bird b) {
		colTitle.setCellValueFactory(new PropertyValueFactory<Historic, String>("title"));
	    colDate.setCellValueFactory(new PropertyValueFactory<Historic, String>("date"));
	    colObs.setCellValueFactory(new PropertyValueFactory<Historic, String>("obs"));
	    ObservableList<Historic> historics=FXCollections.observableArrayList();
		try {
			historics = historicRepository.getAllByBirdId(b.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    TableHistoric.setItems(historics);
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LbBand.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Passaro tem de ser procurado antes de editar/apagar.");
			validate=false;
		}else if (!TfBandSearch.getText().equalsIgnoreCase(LbBand.getText())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Passaro tem de ser procurado antes de editar/apagar.");
	        validate = false;
	    }else {
	    	TfBandSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() {
		boolean validate = false;
		if (TfBandSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha tem de ser preenchido");
			validate=false;
		}else if (birdsRepository.getBirdWhereString("Band",TfBandSearch.getText().toUpperCase())==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfBandSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Anilha nao existe");
			validate=false;
		}else {
//			LabelAlert.setStyle(MyValues.ALERT_SUCESS);
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
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
