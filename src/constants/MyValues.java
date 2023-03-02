package constants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyValues {
	public static final String USER = "admin";
	public static final String PASSWORD = "admin";
	public static final String DBNAME = "BirdDataBase";
	public static final String ERROR_BOX_STYLE = "-fx-border-color: red; -fx-border-width:2px;";
	public static final String SUCCESS_BOX_STYLE = "-fx-border-color: green; -fx-border-width:2px;";
	
	public static final String COMPRA = "Compra";
	public static final String NASCIMENTO = "Nascimento";
	public static final ObservableList<String> ENTRYTYPELIST = FXCollections.observableArrayList(MyValues.COMPRA,MyValues.NASCIMENTO);
	
	public static final String FEMEA = "Femea";
	public static final String MACHO = "Macho";
	public static final ObservableList<String> SEXLIST = FXCollections.observableArrayList(MyValues.FEMEA,MyValues.MACHO);
			
	public static final String VIVO ="Vivo";
	public static final String MORTO = "Morto";
	public static final String VENDIDO = "Vendido";
	public static final ObservableList<String> STATELIST = FXCollections.observableArrayList(MyValues.VIVO,MyValues.MORTO,MyValues.VENDIDO);

	public static final String MUTACAO_DEFAULT ="Sem Mutacao";
	public static final String MUTACAO_TYPE ="";
	
	public static final String SEM_PAI ="Sem Pai";
	public static final String SEM_MAE ="Sem Mae";
	
	public static final String CRIADOR_AMADOR ="Amador";
	public static final String CRIADOR_PROFISSIONAL ="Profissional";
	public static final String CRIADOR_LOJA ="Loja";
	public static final ObservableList<String> BREEDERTYPELIST = FXCollections.observableArrayList(CRIADOR_AMADOR,CRIADOR_PROFISSIONAL,CRIADOR_LOJA);
	
	public static final String CHANGE_STATE_SUCCESS ="Estado alterado com sucesso!";
	
	public static final String ICON_PATH="file:resources/images/img/icon.png";
	public static final String TITLE_BIRD_APP = "Bird Application";
	public static final String TITLE_SELECT_IMAGE = "Selecionar Imagem Passaro";
	public static final String TITLE_CHANGE_STATE = "Alterar Estado";
	
	public static final String BIRD_INSERTED = "PASSARO INSERIDO";
	public static final String CHANGE_STATE = "ESTADO ALTERADO";
	
	public static final String DATE_FORMATE = "dd-MM-yyyy HH:mm";
}
