package repository;

import constants.MyValues;

import java.io.File;
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
	private PostureRepository postureRepository = new PostureRepository();
	private StateRepository stateRepository = new StateRepository();
	private FederationRepository federationRepository = new FederationRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	private HistoricRepository historicRepository = new HistoricRepository();
	private TreatmentRepository treatmentRepository = new TreatmentRepository();
	private BirdTreatmentRepository birdTreatmentRepository = new BirdTreatmentRepository();
	private EggRepository eggRepository = new EggRepository();
	private BroodRepository broodRepository = new BroodRepository();
	private AdoptiveParentsRepository adoptiveParentsRepository = new AdoptiveParentsRepository();
	
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
	
	private void CloseConnection(Connection con, Statement stmt, ResultSet rs) throws SQLException {
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
			con.close();
			System.out.println("Database "+DbName+" created!");
			CreateAllTables();
			File folder = new File("Database/bird_images");
			folder.mkdir();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void CreateAllTables() throws SQLException {
		System.out.println("Trying to create all tables...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		federationRepository.createTableFederation(con,stmt);
		clubRepository.createTableClub(con,stmt);
		breederRepository.CreateTableBreeder(con,stmt);
		cageRepository.CreateTableCage(con,stmt);
		speciesRepository.CreateTableSpecies(con,stmt);
		mutationsRepository.CreateTableMutations(con,stmt);
		stateRepository.CreateTableState(con,stmt);
		postureRepository.CreateTableBrooding(con,stmt);
		birdsRepository.CreateTableBird(con,stmt);
		couplesRepository.CreateTableCouples(con,stmt);
		postureRepository.AddFK(con,stmt);
		breederClubRepository.createTableBreederClub(con,stmt);
		breederFederationRepository.createTableBreederFederation(con,stmt);
		historicRepository.createTableHistoric(con,stmt);
		treatmentRepository.createTableTreatment(con, stmt);
		birdTreatmentRepository.createTableBirdTreatment(con, stmt);
		broodRepository.createTableBrood(con, stmt);
		eggRepository.createTableEgg(con, stmt);
		adoptiveParentsRepository.createTableAdoptiveParents(con, stmt);
		CloseConnection(con, stmt, null);
		System.out.println("All tables created!");
	}
	
	public void DropAllTables() throws SQLException {
		System.out.println("Trying to drop all tables...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		historicRepository.DropTableHistoric(con,stmt);
		breederFederationRepository.dropTableBreederFederation(con,stmt);
		breederClubRepository.dropTableBreederClub(con,stmt);
		birdsRepository.DropTableBird(con,stmt);
		speciesRepository.DropTableSpecies(con,stmt);
		mutationsRepository.DropTableMutations(con,stmt);
		cageRepository.DropTableCage(con,stmt);
		breederRepository.DropTableBreeder(con,stmt);
		couplesRepository.DropTableCouples(con,stmt);
		postureRepository.DropTableBrooding(con,stmt);
		stateRepository.dropTableState(con,stmt);
		clubRepository.dropTableClub(con,stmt);
		federationRepository.dropTableFederation(con,stmt);
		treatmentRepository.dropTableTreatment(con, stmt);
		birdTreatmentRepository.dropTableBirdTreatment(con, stmt);
		broodRepository.dropTableBrood(con, stmt);
		eggRepository.dropTableEgg(con, stmt);
		adoptiveParentsRepository.createTableAdoptiveParents(con, stmt);
		CloseConnection(con, stmt, null);
		System.out.println("All tables dropped!");
	}

	public void alterTableDropColumn(String tableName,String columnName) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table Birds ...");
			Statement stmt = con.createStatement();
			String sql = "ALTER TABLE "+tableName+" DROP COLUMN "+columnName;
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void alterTableAddColumn(String tableName,String column) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table Birds ...");
			Statement stmt = con.createStatement();
			String sql = "ALTER TABLE "+tableName+" ADD COLUMN "+column;
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
