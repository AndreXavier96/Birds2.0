package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CouplesRepository {

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
	
	public void CreateTableCouples(Connection con,Statement stmt) throws SQLException {
			System.out.println("Creating Table COUPLES ...");
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
			System.out.println("Table COUPLES Created.");
	}
	
	public void DropTableCouples(Connection con,Statement stmt) throws SQLException  {
			System.out.println("Trying to drop COUPLES table...");
			String sql = "DROP TABLE IF EXISTS COUPLES CASCADE";	
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println("Table COUPLES Droped.");
	}
}
