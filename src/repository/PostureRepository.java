package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostureRepository {
	
	private void CloseConnection(Connection con, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
	}
	
	public void CreateTableBrooding(Connection con, Statement stmt) throws SQLException {
		System.out.println("Creating Table POSTURE ...");
		String sql = "CREATE TABLE IF NOT EXISTS POSTURE" 
					+ " (id INTEGER auto_increment, "
					+ "StartDate Date NOT NULL, " 
					+ "BirthDate Date NOT NULL, " 
					+ "BroodingDate Date NOT NULL, "
					+ "TotalEggs INTEGER, " 
					+ "HatchedEggs INTEGER, " 
					+ "BornEggs INTEGER, "
					+ "CoupleId INTEGER NOT NULL, "
					+ "PRIMARY KEY (id))";
		stmt.executeUpdate(sql);
		System.out.println("Table POSTURE Created.");
	}
	
	public void AddFK(Connection con, Statement stmt) throws SQLException {
			System.out.println("Add FK from couple to Table POSTURE ...");
			String sql = "ALTER TABLE POSTURE "
					+"ADD FOREIGN KEY (CoupleId) REFERENCES COUPLES (id)";
			stmt.executeUpdate(sql);
			System.out.println("FK from couple added to POSTURE.");
	}
	
	public void dropTableBrooding(Connection con, Statement stmt) throws SQLException {
			System.out.println("Trying to drop POSTURE table...");
			String sql = "DROP TABLE IF EXISTS POSTURE CASCADE";	
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println("Table POSTURE Droped.");
	}
}
