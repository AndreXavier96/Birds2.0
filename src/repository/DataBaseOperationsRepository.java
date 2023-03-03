package repository;

import constants.MyValues;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseOperationsRepository {
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private MutationsRepository mutationsRepository = new MutationsRepository();
	private CageRepository cageRepository = new CageRepository();
	private BreederRepository breederRepository = new BreederRepository();
	private CouplesRepository couplesRepository = new CouplesRepository();
	private PostureRepository broodingRepository = new PostureRepository();
	private StateRepository stateRepository = new StateRepository();
	private FederationRepository federationRepository = new FederationRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	
	public Connection GetConnection(String DbName,String user, String pass) {
		try {
			System.out.println("Connecting to DataBase "+DbName+"...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+DbName,user,pass);
			System.out.println("Connected!");
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void CloseConnection(Connection con, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
	}
	
	public void CreateDataBase(String DbName) {
		try {
			System.out.println("Creatin DataBase "+DbName+"...");
			Class.forName("org.h2.Driver").getDeclaredConstructor().newInstance();
			@SuppressWarnings("unused")
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+DbName,MyValues.USER,MyValues.PASSWORD);
			System.out.println("Database "+DbName+" created!");
			CreateAllTables();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void CreateAllTables() {
		System.out.println("Trying to create all tables...");
		federationRepository.createTableFederation();
		clubRepository.createTableClub();
		breederRepository.CreateTableBreeder();
		cageRepository.CreateTableCage();
		speciesRepository.CreateTableSpecies();
		mutationsRepository.CreateTableMutations();
		stateRepository.CreateTableState();
		broodingRepository.CreateTableBrooding();
		birdsRepository.CreateTableBird();
		couplesRepository.CreateTableCouples();
		broodingRepository.AddFK();
		breederClubRepository.createTableBreederClub();
		breederFederationRepository.createTableBreederFederation();
		historicRepository.createTableHistoric();
		System.out.println("All tables created!");
	}
	
	public void DropAllTables() {
		System.out.println("Trying to drop all tables...");
		historicRepository.DropTableHistoric();
		breederFederationRepository.dropTableBreederFederation();
		breederClubRepository.dropTableBreederClub();
		birdsRepository.DropTableBird();
		speciesRepository.DropTableSpecies();
		mutationsRepository.DropTableMutations();
		cageRepository.DropTableCage();
		breederRepository.DropTableBreeder();
		couplesRepository.DropTableCouples();
		broodingRepository.DropTableBrooding();
		stateRepository.dropTableState();
		clubRepository.dropTableClub();
		federationRepository.dropTableFederation();
		System.out.println("All tables dropped!");
	}

	public void alterTableDropColumn(String tableName,String columnName) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table Birds ...");
			Statement stmt = con.createStatement();
			String sql = "ALTER TABLE "+tableName+" DROP COLUMN "+columnName;
			stmt.executeUpdate(sql);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
