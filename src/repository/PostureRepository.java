package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import constants.MyValues;

public class PostureRepository {
	public void CreateTableBrooding() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table POSTURE ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS POSTURE"
					+" (id INTEGER auto_increment, "
					+"StartDate Date NOT NULL, "
					+"BirthDate Date NOT NULL, "
					+"BroodingDate Date NOT NULL, "
					+"TotalEggs INTEGER, "
					+"HatchedEggs INTEGER, "
					+"BornEggs INTEGER, "
					+"CoupleId INTEGER NOT NULL, "
					+"PRIMARY KEY (id))";
			
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
			System.out.println("Table POSTURE Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void AddFK() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table POSTURE ...");
			Statement stmt = con.createStatement();
			String sql = "ALTER TABLE POSTURE "
					+"ADD FOREIGN KEY (CoupleId) REFERENCES COUPLES (id)";
			
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
			System.out.println("Table POSTURE Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableBrooding() {
		try {
			System.out.println("Trying to drop POSTURE table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS POSTURE CASCADE";	
			stmt.executeUpdate(sql);
//			System.out.println("Sql: "+sql);
			System.out.println("Table POSTURE Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
