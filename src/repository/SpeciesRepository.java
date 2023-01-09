package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Bird;
import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SpeciesRepository {
	
	public void CreateTableSpecies() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table SPECIES ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS SPECIES"
					+" (id INTEGER auto_increment, "
					+"CommonName VARCHAR(255) NOT NULL, "
					+"ScientificName VARCHAR(255) NOT NULL UNIQUE, "
					+"IncubationDays INTEGER NOT NULL, "
					+"BandingDays INTEGER NOT NULL, "
					+"OutOfCageDays INTEGER NOT NULL, "
					+"MaturityDays INTEGER NOT NULL, "
					+"PRIMARY KEY (id))";
		
			stmt.executeUpdate(sql);
			System.out.println("Table SPECIES Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableSpecies() {
		try {
			System.out.println("Trying to drop BIRDS table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS SPECIES CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table SPECIES Droped.");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void Insert(Specie specie) {
		try {
			System.out.println("Insert Specie in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			
			String sql = "INSERT INTO "
					+"SPECIES(CommonName,ScientificName,IncubationDays,BandingDays,OutOfCageDays,MaturityDays) "
					+"values('"+specie.getCommonName()+"','"+specie.getScientificName()+"','"
					+specie.getIncubationDays()+"','"+specie.getDaysToBand()+"','"+specie.getOutofCageAfterDays()
					+"','"+specie.getMaturityAfterDays()+"')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+" Specie Record inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Specie getSpecieById(Integer id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM SPECIES WHERE SPECIES.id='"+id+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			Specie s = new Specie();
			while (rs.next()) {
				s.setId(rs.getInt(1));
				s.setCommonName(rs.getString(2));
				s.setScientificName(rs.getString(3));
				s.setIncubationDays(rs.getInt(4));
				s.setDaysToBand(rs.getInt(5));
				s.setOutofCageAfterDays(rs.getInt(6));
				s.setMaturityAfterDays(rs.getInt(7));
			}
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;}
		}
	
	
	public boolean checkSpecieByScientificName(String scientificName) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM SPECIES WHERE SPECIES.ScientificName='"+scientificName+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			return rs.next();
		}
	
	public ObservableList<Specie> getAllSpecies() {
		try {
			System.out.println("Getting all Species...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Species";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Specie> species = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Specie: " + rs.getInt(1));
				Specie s = new Specie();
				s.setId(rs.getInt(1));
				s.setCommonName(rs.getString(2));
				s.setScientificName(rs.getString(3));
				s.setIncubationDays(rs.getInt(4));
				s.setDaysToBand(rs.getInt(5));
				s.setOutofCageAfterDays(rs.getInt(6));
				s.setMaturityAfterDays(rs.getInt(7));
				species.add(s);
			}
			return species;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
