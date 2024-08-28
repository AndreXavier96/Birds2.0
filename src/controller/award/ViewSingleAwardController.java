package controller.award;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import repository.AwardRepository;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import constants.PathsConstants;
import controller.HiperligacoesController;
import domains.Award;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

public class ViewSingleAwardController implements Initializable {
	@FXML
	private Label LbTitle,LbBand,LbPontuation,LbName;
	@FXML
	private ImageView judgmentImage;

	private AwardRepository awardRepository = new AwardRepository();
	
	private HiperligacoesController hiperligacoes = new HiperligacoesController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LbBand.setOnMouseClicked(event -> {
		    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
		    	hiperligacoes.openViewSingleBird(LbTitle.getScene(), LbBand.getText());
		});
	}
	
	public void search(Integer id) {
		clearAllFields();
		Award a = awardRepository.getAwardById(id);
		LbTitle.setText("Ver Premio Passaro "+a.getBird().getBand());
		LbBand.setText(a.getBird().getBand());
		LbName.setText(a.getExibithion().getName());
		LbPontuation.setText(a.getPontuation().toString());
		if (a.getJudgmentImagePath()!=null && !a.getJudgmentImagePath().isEmpty()) {
			String s = a.getJudgmentImagePath();
			if (Files.exists(Paths.get(s.substring(s.indexOf(':')+1))))
				judgmentImage.setImage(new Image(a.getJudgmentImagePath()));
			else
				judgmentImage.setImage(PathsConstants.DEFAULT_IMAGE);
		}
	}
	
	public void clearAllFields() {
		LbTitle.setText("Ver Premio Passaro XXX");
		LbBand.setText(null);
		LbPontuation.setText(null);
		LbName.setText(null);
		judgmentImage.setImage(PathsConstants.DEFAULT_IMAGE);
	}
	
	@FXML
	public void btnCancel(ActionEvent event) {
		Stage stage = (Stage) LbTitle.getScene().getWindow();
		stage.close();
	}
}
