package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClubRepository {
	
	FederationRepository federationRepository = new FederationRepository();

	public void createTableClub() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table CLUB ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS CLUB"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL UNIQUE, "
					+"Acronym VARCHAR(255) NOT NULL UNIQUE, "
					+"Locale VARCHAR(255) NOT NULL, "
					+"Address VARCHAR(255), "
					+"Phone VARCHAR(255), "
					+"Email VARCHAR(255) NOT NULL UNIQUE, "
					+"FederationId Integer NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (FederationId) REFERENCES FEDERATION(id))";
			stmt.executeUpdate(sql);
			System.out.println("Table CLUB Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dropTableClub() {
		try {
			System.out.println("Trying to drop CLUB table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS CLUB CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table CLUB Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Insert(Club c) {
		try {
			System.out.println("Insert Club in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"CLUB(Name,Acronym,Locale,Address,Phone,Email,FederationId) "
					+"values('"+c.getName()+"','"+c.getAcronym()+"','"+c.getLocale()+"','"
					+c.getAddress()+"','"+c.getPhone()+"','"+c.getEmail()+"','"+c.getFederation().getId()+"')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+"Club inserted: "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ObservableList<Club> getAllClubs() {
		try {
			System.out.println("Getting all Clubs...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Club> clubs = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Clubs: " + rs.getString(3));
				Club c = new Club();
				c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
				clubs.add(c);
			}
			return clubs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Club getClubsWhereString(String col, String value) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			Club c = new Club();
			if (rs.next()) {
				c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
				return c;
			}else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkIfExistsString(String col,String value) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();	
	}
	
	
	public ObservableList<Club> getAvailableClubs(Integer breederId) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM CLUB WHERE id NOT IN (SELECT ClubId FROM BREEDER_CLUB WHERE BreederId = " + breederId + ")";
	        ResultSet rs = stmt.executeQuery(sql);
	        ObservableList<Club> availableClubs = FXCollections.observableArrayList();
	        while (rs.next()) {
	            Club c = new Club();
	            c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
	            availableClubs.add(c);
	        }
	        con.close();
	        return availableClubs;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	public ObservableList<Club> getAllClubsByClubIds(ObservableList<Integer> clubIds){
		 ObservableList<Club> allClubs = FXCollections.observableArrayList();
		 for (Integer clubId : clubIds) {
		        Club club = getClubsWhereString("id",Integer.toString(clubId));
		        allClubs.add(club);
		    }
		return allClubs;
	}
	
}
