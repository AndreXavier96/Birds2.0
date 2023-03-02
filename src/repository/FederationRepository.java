package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FederationRepository {

	public void createTableFederation() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table FEDERATION ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS FEDERATION"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL UNIQUE, "
					+"Acronym VARCHAR(255) NOT NULL UNIQUE, "
					+"Email VARCHAR(255) UNIQUE, "
					+"Country VARCHAR(255), "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table FEDERATION Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dropTableFederation() {
		try {
			System.out.println("Trying to drop FEDERATION table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS FEDERATION CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table FEDERATION Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Insert(Federation f) {
		try {
			System.out.println("Insert Federation in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"FEDERATION(Name,Acronym,Email,Country) "
					+"values('"+f.getName()+"','"+f.getAcronym()+"','"+f.getEmail()+"','"+f.getCountry()+"')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+"Record inserted: "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ObservableList<Federation> getAllFederations() {
		try {
			System.out.println("Getting all Federations...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM FEDERATION";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Federation> federations = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Federations: " + rs.getString(3));
				Federation f = new Federation();
				f.setId(rs.getInt(1));
				f.setName(rs.getString(2));
				f.setAcronym(rs.getString(3));
				f.setEmail(rs.getString(4));;
				f.setCountry(rs.getString(5));
				federations.add(f);
				System.out.println(f);
			}
			return federations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Federation getFederationWhereString(String col, String value) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM FEDERATION WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			Federation f = new Federation();
			if (rs.next()) {
				f.setId(rs.getInt(1));
				f.setName(rs.getString(2));
				f.setAcronym(rs.getString(3));
				f.setEmail(rs.getString(4));;
				f.setCountry(rs.getString(5));
				return f;
			}else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Federation getFederationWhereInt(String col, Integer value) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM FEDERATION WHERE "+col+"="+value+";";
			ResultSet rs = stmt.executeQuery(sql);
			Federation f = new Federation();
			if (rs.next()) {
				f.setId(rs.getInt(1));
				f.setName(rs.getString(2));
				f.setAcronym(rs.getString(3));
				f.setEmail(rs.getString(4));;
				f.setCountry(rs.getString(5));
				return f;
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
			String sql = "SELECT * FROM FEDERATION WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();	
	}
	
}
