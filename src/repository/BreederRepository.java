package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import constants.MyValues;
import domains.Breeder;
import domains.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BreederRepository {
	
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();

	public void CreateTableBreeder() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table BREEDER ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER"
					+" (id INTEGER auto_increment, "
					+"CC INTEGER UNIQUE, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Nif INTEGER UNIQUE, "
					+"Cellphone INTEGER UNIQUE, "
					+"Email VARCHAR(255) UNIQUE, "
					+"Address VARCHAR(255), "
					+"PostalCode VARCHAR(255), "
					+"Locale VARCHAR(255), "
					+"District VARCHAR(255), "
					+"BreederType VARCHAR(255) NOT NULL, "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableBreeder() {
		try {
			System.out.println("Trying to drop BREEDER table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS BREEDER CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Breeder> getAllBreeders() {
		try {
		System.out.println("Getting all Breeders...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
				MyValues.USER, MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM BREEDER";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Breeder> breeders = FXCollections.observableArrayList();
		while(rs.next()) {
			System.out.println("Get Breeder: "+rs.getInt(1));
			Breeder b = new Breeder();
			b.setId(rs.getInt(1));
			b.setCC(rs.getInt(2));
			b.setName(rs.getString(3));
			b.setNif(rs.getInt(4));
			b.setCellphone(rs.getInt(5));
			b.setEmail(rs.getString(6));
			b.setAddress(rs.getString(7));
			b.setPostalCode(rs.getString(8));
			b.setLocale(rs.getString(9));
			b.setDistrict(rs.getString(10));
			b.setType(rs.getString(11));
			b.setClub(clubRepository.getAllClubsByClubIds(breederClubRepository.getClubsFromBreederId(b.getId())));
			b.setStam(breederFederationRepository.getAllByBreederId(b.getId()));
			
			breeders.add(b);
		}
		return breeders;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void Insert(Breeder b) {
		int breederId = -1;
		try {
			System.out.println("Insert Breeder in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			String sql = "INSERT INTO "
	                +"BREEDER(CC,Name,Nif,Cellphone,Email,Address,PostalCode,Locale,District,BreederType) "
	                +"values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (b.getCC()==null)
				stmt.setString(1, null);
			else
				stmt.setInt(1, b.getCC());
	        stmt.setString(2, b.getName());
	        if (b.getNif()==null)
				stmt.setString(3, null);
			else
				stmt.setInt(3, b.getNif());
	        stmt.setInt(4, b.getCellphone());
	        if (b.getEmail()==null)
				stmt.setString(5, null);
			else
				stmt.setString(5, b.getEmail());
	        if (b.getAddress()==null)
				stmt.setString(6, null);
			else
				stmt.setString(6, b.getAddress());
	        if (b.getPostalCode()==null)
				stmt.setString(7, null);
			else
				stmt.setString(7, b.getPostalCode());
	        if (b.getLocale()==null)
				stmt.setString(8, null);
			else
				stmt.setString(8, b.getLocale());
	        if (b.getDistrict()==null)
				stmt.setString(9, null);
			else
				stmt.setString(9, b.getDistrict());
	        stmt.setString(10, b.getType());
	        stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            breederId = rs.getInt(1);
	        }
			System.out.println("["+breederId+"] inserted: "+sql);
			for (Club c : b.getClub())
				breederClubRepository.Insert(breederId, c.getId());
			for (Map.Entry<Integer, String> entry : b.getStam().entrySet())
				breederFederationRepository.insert(breederId, entry.getKey(), entry.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkIfCCExists(int cc) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT CC FROM BREEDER WHERE CC="+cc+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();	
	}
	
	public boolean checkIfNIFExists(int nif) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT Nif FROM BREEDER WHERE Nif="+nif+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}

	public boolean checkIfPhoneExists(int phone) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT Cellphone FROM BREEDER WHERE Cellphone="+phone+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public boolean checkIfEmailExists(String email) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE Email='"+email+"';";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public boolean checkIfSTAMExists(String stam) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE STAM='"+stam+"';";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public Breeder getBreederbyId(int id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE id="+id+";";
			ResultSet rs = stmt.executeQuery(sql);
			Breeder b = new Breeder();
			while(rs.next()) {
				System.out.println("Get Breeder: "+rs.getInt(1));
				b.setId(rs.getInt(1));
				b.setCC(rs.getInt(2));
				b.setName(rs.getString(3));
				b.setNif(rs.getInt(4));
				b.setCellphone(rs.getInt(5));
				b.setEmail(rs.getString(6));
				b.setAddress(rs.getString(7));
				b.setPostalCode(rs.getString(8));
				b.setLocale(rs.getString(9));
				b.setDistrict(rs.getString(10));
				b.setType(rs.getString(11));
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Breeder getBreederbyCC(int cc) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE id="+cc+";";
			ResultSet rs = stmt.executeQuery(sql);
			Breeder b = new Breeder();
			while(rs.next()) {
				System.out.println("Get Breeder: "+rs.getInt(1));
				b.setId(rs.getInt(1));
				b.setCC(rs.getInt(2));
				b.setName(rs.getString(3));
				b.setNif(rs.getInt(4));
				b.setCellphone(rs.getInt(5));
				b.setEmail(rs.getString(6));
				b.setAddress(rs.getString(7));
				b.setPostalCode(rs.getString(8));
				b.setLocale(rs.getString(9));
				b.setDistrict(rs.getString(10));
				b.setType(rs.getString(11));
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
