package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Cage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CageRepository {
	
	public void CreateTableCage() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table CAGE ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS CAGE"
					+" (id INTEGER auto_increment, "
					+"Code VARCHAR(255) NOT NULL UNIQUE, "
					+"Type VARCHAR(255) NOT NULL, "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table CAGE Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableCage() {
		try {
			System.out.println("Trying to drop MUTATIONS table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS CAGE CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table CAGE Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Cage getCage(Integer id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CAGE WHERE CAGE.id='"+id+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			Cage c = new Cage();
			while (rs.next()) {
				c.setId(rs.getInt(1));
				c.setCode(rs.getString(2));
				c.setType(rs.getString(3));
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void Insert(Cage cage) {
		try {
		System.out.println("Insert Cage in Database...");
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO CAGE(Code,Type) VALUES('"
				+cage.getCode().replace("'","''")+"','"+cage.getType()+"')";
		int i = stmt.executeUpdate(sql);
		System.out.println(i+" Record inserted");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkIfCodeExist(String code)  throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT Code FROM CAGE WHERE Code='"+code.replace("'", "''")+"';";
		ResultSet rs = stmt.executeQuery(sql);
		return rs.next();
	}
	
	public ObservableList<Cage> getAllCages() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CAGE";
			ResultSet rs  = stmt.executeQuery(sql);
			ObservableList<Cage> cages = FXCollections.observableArrayList();
			while (rs.next()) {
				Cage c = new Cage();
				c.setId(rs.getInt(1));
				c.setCode(rs.getString(2));
				c.setType(rs.getString(3));
				cages.add(c);
			}
			return cages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
