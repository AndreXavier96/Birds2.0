package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import constants.MyValues;

public class MutationsRepository {
	
	public void CreateTableMutations() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table MUTATIONS ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS MUTATIONS"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Type VARCHAR(255) NOT NULL, "
					+"Symbol INTEGER NOT NULL, "
					+"SpeciesId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (SpeciesId) REFERENCES SPECIES (id))";
			
		
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
			System.out.println("Table MUTATIONS Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableMutations() {
		try {
			System.out.println("Trying to drop MUTATIONS table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS MUTATIONS CASCADE";	
			stmt.executeUpdate(sql);
//			System.out.println("Sql: "+sql);
			System.out.println("Table MUTATIONS Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
