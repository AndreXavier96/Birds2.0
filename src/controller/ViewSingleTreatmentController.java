package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.TreatmentRepository;
import java.io.IOException;
import java.sql.SQLException;
import constants.MyValues;
import constants.PathsConstants;
import domains.Treatment;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ViewSingleTreatmentController {
	@FXML
	private Label LbTitle, LabelAlert,LbName, LbDesc, LbDuracao, LbFreq,LbId;
	@FXML
	private Label LbTimesAplied, LbBirdsTreated; //TODO
	
	private TreatmentRepository treatmentRepository = new TreatmentRepository();
	
//	Treatment treatment = null;

	@FXML
	public void btnDelete(ActionEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Confirmation.fxml"));
		Parent root = loader.load();
		ConfirmationController confirmationController = loader.getController();
		confirmationController.getLbText().setText("Tem a certeza que quer apagar o Tratamento: '" + LbName.getText() + "'?");
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_DELETE_TREATMENT+LbName.getText());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		if (confirmationController.isConfirmed()) {
			try {
				Treatment t = treatmentRepository.getTreatmentById(Integer.parseInt(LbId.getText()));
				treatmentRepository.deleteTreatment(t);
				Stage stageClose = (Stage) LabelAlert.getScene().getWindow();
				stageClose.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void btnEdit(ActionEvent event) throws IOException, NumberFormatException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/treatments/AddTreatmentView.fxml"));
		Parent root = loader.load();
		Treatment t = treatmentRepository.getTreatmentById(Integer.parseInt(LbId.getText()));
		AddTreatmentViewController addTreatmentViewController =  loader.getController();
		addTreatmentViewController.startValuesEdit(t);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle(MyValues.TITLE_EDIT_TREATMENT + LbName.getText());
		stage.getIcons().add(new Image(PathsConstants.ICON_PATH));
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		Treatment updatedTreatment = treatmentRepository.getTreatmentById(t.getId());
		search(updatedTreatment.getId());
	}
	

	public void search(int id) throws SQLException {
		Treatment t = treatmentRepository.getTreatmentById(id);
		updateAllInfo(t);	
	}
	
	public void updateAllInfo(Treatment t) throws SQLException {
		LbId.setText(t.getId().toString());
		LbTitle.setText("Tratamento: "+t.getName());
		LbName.setText(t.getName());
		LbDesc.setText(t.getDescription());
		LbDuracao.setText(t.getDurationDays()+" Dias");
		if (t.getFrequencyType().equals(MyValues.HORA)) {
			LbFreq.setText("De "+t.getFrequency()+" em "+t.getFrequency()+" Horas");
		}else if (t.getFrequencyType().equals(MyValues.DIA)) {
			LbFreq.setText("De "+t.getFrequency()+" em "+t.getFrequency()+" Dias");
		}else if (t.getFrequencyType().equals(MyValues.SEMANA)) {
			LbFreq.setText("De "+t.getFrequency()+" em "+t.getFrequency()+" Semanas");
		}
		LbTimesAplied.setText(null);//TODO
		LbBirdsTreated.setText(null);//TODO
//		treatment=t;
	}
	
	@FXML
	public void btnClose(ActionEvent event) {
		Stage stage = (Stage) LabelAlert.getScene().getWindow();
		stage.close();
	}
}
