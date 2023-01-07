package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import constants.MyValues;
import domains.Cage;

public class CageRepository {
	
	public void CreateTableCage() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table CAGE ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS CAGE"
					+" (id INTEGER NOT NULL, "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
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
//			System.out.println("Sql: "+sql);
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
				c.setId(rs.getInt(0));
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
		String sql = "INSER INTO CAGE(id) VALUES('"+cage.getId()+"')";
		int i = stmt.executeUpdate(sql);
		System.out.println(i+" Record inserted");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
