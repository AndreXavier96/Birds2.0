package controller.bird;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import constants.MyValues;
import constants.PathsConstants;
import domains.Bird;
import domains.Historic;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import repository.BirdsRepository;
import repository.HistoricRepository;
import javafx.scene.control.DatePicker;

public class ChangeBirdPhotoController {
	@FXML
	private Label LbTitle,LabelAlert, LbBand,LbImagePath;
	@FXML
	private DatePicker Dtpicker;
	@FXML
	private ImageView ImOld,ImNew;
	@FXML
	private Button btnUpload;
	@FXML
	private ViewSingleBirdController viewSingleBirdController;
	

	private BirdsRepository birdsRepository = new BirdsRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	private boolean imageUploaded=false;
	
	public void startValues(String band,String img) {
		LbTitle.setText("Alterar imagem de "+band);
		LbBand.setText(band);
		ImOld.setImage(new Image(img));
	}

	public void setViewSingleBirdController(ViewSingleBirdController controller) {
		this.viewSingleBirdController = controller;
	}
	
	@FXML
	public void btnUploadImage(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(MyValues.TITLE_SELECT_IMAGE);
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files","*.png","*.jpg"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile!=null) {
			LbImagePath.setText(selectedFile.toPath().toString());
			ImNew.setImage(new Image(selectedFile.toURI().toString()));
			imageUploaded=true;
		}
	}

	@FXML
	public void btnChange(ActionEvent event) throws SQLException {
		if (validate()) {
			Bird bird= birdsRepository.getBirdWhereString("Band", LbBand.getText());
			String date = new SimpleDateFormat(MyValues.DATE_FORMATE).format(Date.from(Dtpicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			File defaultFolder = new File(PathsConstants.DEFAULT_PATH_TO_SAVE_IMAGE);
			File selectedFile = new File(LbImagePath.getText());
			try {
				String fileName = LbBand.getText()+ selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
				Files.copy(selectedFile.toPath(), defaultFolder.toPath().resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
				birdsRepository.partialUpdateStringsBird(bird.getId(), "ImagePath","file:" + defaultFolder + "\\" + fileName);
			} catch (IOException e) {
				System.out.println(e);
			}
			historicRepository.insertHistoric(new Historic(null,MyValues.CHANGE_IMAGE,date,"Nova imagem", bird));
			Stage stage = (Stage) LbBand.getScene().getWindow();
		    stage.close();
			viewSingleBirdController.setSuccess(MyValues.CHANGE_STATE_SUCCESS, birdsRepository.getBirdWhereString("Band",LbBand.getText()));
		}
	}
	
	public boolean validate() {
		LabelAlert.setStyle(MyValues.ALERT_ERROR);
		boolean validate = false;

		try {
			Dtpicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			Dtpicker.setStyle(null);
			LabelAlert.setStyle("");
			validate = true;
		} catch (Exception e) {
			Dtpicker.setStyle(MyValues.ERROR_BOX_STYLE);
			LabelAlert.setText("Ano nao esta no formato correto ou tem de ser preenchido");
			validate = false;
		}

		if (validate) {
			if (!imageUploaded) {
				btnUpload.setStyle(MyValues.ERROR_BOX_STYLE);
				LabelAlert.setText("Nova imagem tem de ser escolhida");
				validate = false;
			} else {
				btnUpload.setStyle(null);
				LabelAlert.setText("");
				validate = true;
			}
		}
		return validate;
	}

	@FXML
	public void BtnCancel(ActionEvent event) {
		Stage stage = (Stage) LbBand.getScene().getWindow();
	    stage.close();
	}
}
