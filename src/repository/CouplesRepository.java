package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import constants.MyValues;

public class CouplesRepository {
	
	public void CreateTableCouples() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table COUPLES ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS COUPLES"
					+" (id INTEGER auto_increment, "
					+"Bird1Id INTEGER NOT NULL, "
					+"Bird2Id INTEGER NOT NULL, "
					+"Bird3Id INTEGER, "
					+"State VARCHAR(255) NOT NULL, "
					+"CageId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (Bird1Id) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (Bird2Id) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (Bird3Id) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (CageId) REFERENCES CAGE (id)) ";
			
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
			System.out.println("Table COUPLES Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableCouples() {
		try {
			System.out.println("Trying to drop COUPLES table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS COUPLES CASCADE";	
			stmt.executeUpdate(sql);
//			System.out.println("Sql: "+sql);
			System.out.println("Table COUPLES Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
