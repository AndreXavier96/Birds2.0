package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import constants.MyValues;

public class SpeciesRepository {
	
	public void CreateTableSpecies() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table SPECIES ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS SPECIES"
					+" (id INTEGER auto_increment, "
					+"ScientificName VARCHAR(255), "
					+"CommonName VARCHAR(255) NOT NULL, "
					+"IncubationDays INTEGER NOT NULL, "
					+"BandingDays INTEGER NOT NULL, "
					+"OutOfCageDays INTEGER NOT NULL, "
					+"MaturityDays INTEGER NOT NULL, "
					+"PRIMARY KEY (id))";
		
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
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
//			System.out.println("Sql: "+sql);
			System.out.println("Table SPECIES Droped.");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

}
