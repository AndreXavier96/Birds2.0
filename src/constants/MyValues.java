package constants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyValues {
	public static final String USER = "admin";
	public static final String PASSWORD = "admin";
	public static final String DBNAME = "BirdDataBase2";
	
	public static final String ERROR_BOX_STYLE = "-fx-border-color: red; -fx-border-width:2px;";
	public static final String ALERT_ERROR = "-fx-background-color: red; -fx-border-width: 0; -fx-background-radius: 5;";
	public static final String ALERT_SUCESS = "-fx-background-color: green; -fx-border-width: 0; -fx-background-radius: 5;";
	public static final String ALERT_INFO = "-fx-background-color: blue; -fx-border-width: 0; -fx-background-radius: 5;";

	public static final String COMPRA = "Compra";
	public static final String NASCIMENTO = "Nascimento";
	public static final ObservableList<String> ENTRYTYPELIST = FXCollections.observableArrayList(COMPRA,NASCIMENTO);

	public static final String JUNTOS = "Juntos";
	public static final String SEPARADOS = "Separados";
	public static final ObservableList<String> COUPLESTATELIST = FXCollections.observableArrayList(JUNTOS,SEPARADOS);

	public static final String FEMEA = "Femea";
	public static final String MACHO = "Macho";
	public static final String DESCONHECIDO = "Desconhecido";
	public static final ObservableList<String> SEXLIST = FXCollections.observableArrayList(FEMEA,MACHO,DESCONHECIDO);

	public static final String MORTO = "Morto";
	public static final String VENDIDO = "Vendido";
	public static final String REPRODUCAO = "Em periodo de reproducao";
	public static final String REPOUSO = "Em periodo de repouso";
	public static final String VENDA = "Para venda";
	public static final String OTHER = "Outro mas disponivel";
	public static final ObservableList<String> STARTING_STATE_LIST = FXCollections.observableArrayList(REPRODUCAO,REPOUSO,VENDA,OTHER);
	public static final ObservableList<String> STATELIST = FXCollections.observableArrayList(MORTO,VENDIDO,REPRODUCAO,REPOUSO,VENDA,OTHER);

	public static final String MUTACAO_DEFAULT = "Sem Mutacao";
	public static final String MUTACAO_TYPE = "";

	public static final String SEM_PAI = "Sem Pai";
	public static final String SEM_MAE = "Sem Mae";

	public static final String VOADEIRA = "Voadeira";
	public static final String EXPOSICAO = "Exposicao";
	public static final String CRIADEIRA = "Criadeira";
	public static final ObservableList<String> CAGE_TIPE = FXCollections.observableArrayList(VOADEIRA,EXPOSICAO,CRIADEIRA);	
	
	public static final String HORA = "HORA";
	public static final String DIA = "DIA";
	public static final String SEMANA = "SEMANA";
	public static final ObservableList<String> FREQUENCIA = FXCollections.observableArrayList(HORA,DIA,SEMANA);	

	public static final String UNICO = "UNICO";
	public static final String MULTIPLOS = "MULTIPLOS";
	public static final String TODOS = "TODOS";
	public static final ObservableList<String> TREATMENT_TYPE = FXCollections.observableArrayList(UNICO,MULTIPLOS,TODOS);	

	
	public static final String TITLE_BIRD_APP = "Aviário Virtual";
	public static final String TITLE_SELECT_IMAGE = "Selecionar Imagem Passaro";
	public static final String TITLE_CHANGE_STATE = "Alterar Estado";
	public static final String TITLE_CHANGE_CAGE = "Alterar Gaiola";
	public static final String TITLE_CHANGE_IMAGE = "Alterar Foto";
	
	public static final String TITLE_DELETE_CLUB = "Apagar Clube ";
	public static final String TITLE_DELETE_FEDERATION = "Apagar Federacao ";
	public static final String TITLE_DELETE_CAGE = "Apagar Gaiola ";
	public static final String TITLE_DELETE_BREEDER = "Apagar Criador ";
	public static final String TITLE_DELETE_SPECIE = "Apagar Especie ";
	public static final String TITLE_DELETE_MUTATION = "Apagar criador ";
	public static final String TITLE_DELETE_BIRD = "Apagar passaro ";
	public static final String TITLE_DELETE_TREATMENT = "Apagar Tratamento ";
	
	public static final String TITLE_EDIT_CLUB = "Editar Clube ";
	public static final String TITLE_EDIT_FEDERATION = "Editar Federacao ";
	public static final String TITLE_EDIT_CAGE = "Editar Gaiola ";
	public static final String TITLE_EDIT_BREEDER = "Editar Criador ";
	public static final String TITLE_EDIT_SPECIE = "Editar Especie ";
	public static final String TITLE_EDIT_MUTATION = "Editar criador ";
	public static final String TITLE_EDIT_BIRD = "Editar passaro ";
	public static final String TITLE_EDIT_TREATMENT = "Editar tratamento ";
	
	public static final String TITLE_SEPARATE_COUPLE = "Separar Casal ";
	
	public static final String BIRD_INSERTED = "PASSARO INSERIDO";
	public static final String CHANGE_STATE = "ESTADO ALTERADO";
	public static final String CHANGE_CAGE = "GAIOLA ALTERADA";
	public static final String CHANGE_SEX = "SEXAGEM ALTERADA";
	public static final String CHANGE_IMAGE = "IMAGEM ALTERADA";
	
	public static final String CHANGE_STATE_SUCCESS = "Estado alterado com sucesso!";
	public static final String CHANGE_SEX_SUCCESS = "Genero alterado com sucesso!";
	public static final String ADD_COUPLE = "Passaro acasalado";
	public static final String NO_COUPLE = "Passaro nao acasalado";
	
	public static final String DATE_FORMATE = "dd-MM-yyyy";
	
	public static final String aSUPERSCRIPT = "\u00AA";
	public static final String oSUPERSCRIPT = "\u00BA";
	
	public static final String DESCONHECIDO2 = "Desconhecido";
	public static final String PARTIDO = "Partido";
	public static final String ESVAZIAR = "Esvaziar";
	public static final String EM_DESENVOLVIMENTO = "Em Desenvolvimento";
	public static final String CHOCADO = "Chocado";
	public static final String MORTE_NO_OVO = "Morte No Ovo";
	public static final String AUSENCIA_DE = "Ausencia De Embriao";
	public static final ObservableList<String> OVO_LIST = FXCollections.observableArrayList(PARTIDO,DESCONHECIDO2,ESVAZIAR,EM_DESENVOLVIMENTO,CHOCADO,MORTE_NO_OVO,AUSENCIA_DE);

	public static final String FECUNDADO = "Fecundado";
}
