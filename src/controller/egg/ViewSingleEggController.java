package controller.egg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.EggRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import constants.PathsConstants;
import controller.brood.ViewSingleBroodController;
import domains.Egg;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;

public class ViewSingleEggController implements Initializable {
	
	@FXML
	private Label LabelAlert,LbTitle,LbDatePosture,LbDateOutBrake,LbVerifiedFertilityDate,LbType,LbStatute;
	@FXML
	private Menu MenuEditar;
	@FXML
	private ViewSingleBroodController viewSingleBroodController;
	
	private EggRepository eggRepository = new EggRepository();
	
	public void setViewSingleBroodController(ViewSingleBroodController controller) {
		this.viewSingleBroodController = controller;
	}
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private Egg egg;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
	public void startValues (Egg e) {
		this.egg = e;
		LbDatePosture.setText(formatter.format(e.getPostureDate()));
		if (e.getOutbreakDate()!=null)
			LbDateOutBrake.setText(formatter.format(e.getOutbreakDate()));
		if (e.getVerifiedFertilityDate()!=null)
			LbVerifiedFertilityDate.setText(formatter.format(e.getVerifiedFertilityDate()));
		LbType.setText(e.getType());
		LbStatute.setText(e.getStatute());
		if (e.getBird()!=null) {
			MenuEditar.setVisible(false);
		}
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LbTitle.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws SQLException, IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/egg/EditEggView.fxml"));
			Parent root = loader.load();
			Egg e =  new Egg();
			e = eggRepository.getEggById(this.egg.getId());
			EditEggViewController editEggViewController  = loader.getController();
			editEggViewController.startValuesEdit(e);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			Egg updateEgg = eggRepository.getEggById(e.getId());
			startValues(updateEgg);
	}
		
}
