package controller.couples;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.BirdsRepository;
import repository.CouplesRepository;
import repository.HistoricRepository;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import constants.MyValues;
import constants.PathsConstants;
import controller.ConfirmationController;
import controller.HiperligacoesController;
import domains.Bird;
import domains.Couples;
import domains.Historic;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleCouplesController {
	@FXML
	private Label LbTitle, LabelAlert,LbMale, LbFemale, LbEstado,LbCage;
	@FXML
	private ImageView IvMale,IvFemale;
	@FXML
	private TextField TfSearch;
	
	private CouplesRepository couplesRepository = new CouplesRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	private HistoricRepository historicRepository = new HistoricRepository();

	private HiperligacoesController hiperligacoes = new HiperligacoesController();
	
	@FXML
	public void btnSeparar(ActionEvent event) throws IOException, SQLException {
		if (validator()&&validatorSearch()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
			Parent root = loader.load();
			ConfirmationController confirmationController = loader.getController();
			confirmationController.getLbText().setText("Tem a certeza que quer separar o casal: '" + LbMale.getText()+" e "+LbFemale.getText() + "'?");
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setTitle(MyValues.TITLE_SEPARATE_COUPLE);
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			if (confirmationController.isConfirmed()) {
				try {
					Couples c = couplesRepository.getCouplesWhereBird(birdsRepository.getBirdWhereString("Band", TfSearch.getText()));
					couplesRepository.deleteCouple(c);
					String obsMale = "Passara separado de '"+c.getFemale().getBand()+"'.";
					String obsFemale = "Passara separado de '"+c.getMale().getBand()+"'.";
					String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					historicRepository.insertHistoric(new Historic(null,MyValues.SEPARADOS,date,obsMale,c.getMale()));
					historicRepository.insertHistoric(new Historic(null,MyValues.SEPARADOS,date,obsFemale,c.getFemale()));
					clearAllFields();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void search(String couples) throws SQLException {
		clearAllFields();
		TfSearch.setText(couples);
		Bird bird = birdsRepository.getBirdWhereString("Band", TfSearch.getText().toUpperCase());
		Couples c = couplesRepository.getCouplesWhereBird(bird);
		updateAllInfo(c);
	}
	
	@FXML
	public void btnSearchForCouple(ActionEvent event) throws SQLException {
		clearAllFields();
		if (validator()) {
			LabelAlert.setStyle(null);
			Bird bird = birdsRepository.getBirdWhereString("Band", TfSearch.getText().toUpperCase());
			Couples c = couplesRepository.getCouplesWhereBird(bird);
			updateAllInfo(c);
		}
	}

	public void updateAllInfo(Couples c) throws SQLException {
		LbTitle.setText("Casal: "+c.getMale().getBand()+" e "+c.getFemale().getBand());
		LbMale.setText(c.getMale().getBand());
		LbMale.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
		    	hiperligacoes.openViewSingleBird(LbTitle.getScene(),LbMale.getText());
		    }
		});
		LbFemale.setText(c.getFemale().getBand());
		LbFemale.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSingleBird(LbTitle.getScene(),LbFemale.getText());
		});
		LbEstado.setText(c.getState());
		LbCage.setText(c.getCage().getCode());
		LbCage.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewCage(LbTitle.getScene(),LbCage.getText());
		});
		IvMale.setImage(new Image(c.getMale().getImage()));
		IvFemale.setImage(new Image(c.getFemale().getImage()));
	}

	public void clearAllFields() {
		LabelAlert.setText(null);
		LbTitle.setText("Gaiola XXXX");
		LbMale.setText(null);
		LbFemale.setText(null);
		LbEstado.setText(null);
		IvMale.setImage(PathsConstants.DEFAULT_IMAGE);
		IvFemale.setImage(PathsConstants.DEFAULT_IMAGE);
	}
	
	public boolean validatorSearch(){
		boolean validate = false;
		if (LbMale.getText()==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Casal tem de ser procurado.");
			validate=false;
		}else if (!TfSearch.getText().equalsIgnoreCase(LbMale.getText()) && !TfSearch.getText().equalsIgnoreCase(LbFemale.getText())) {
	        LabelAlert.setStyle(MyValues.ALERT_ERROR);
	        TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
	        LabelAlert.setText("Casal tem de ser procurado.");
	        validate = false;
	    }else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	public boolean validator() throws SQLException {
		boolean validate = false;
		Bird bird = birdsRepository.getBirdWhereString("Band", TfSearch.getText().toUpperCase());
		if (TfSearch.getText().length()==0) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Pesquisa tem de ser preenchida");
			validate=false;
		}else if (bird==null) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Passaro nao existe");
			validate=false;
		}else if (!couplesRepository.checkIfCouplesExist(bird)) {
			LabelAlert.setStyle(MyValues.ALERT_ERROR);
			TfSearch.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Passaro nao acasalado");
			validate=false;
		}else {
			TfSearch.setStyle(null);
			LabelAlert.setText("");
			validate=true;
		}
		return validate;
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
