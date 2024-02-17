package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.BirdTreatment;

public class BirdTreatmentRepository {
	
	BirdsRepository birdsRepository = new BirdsRepository();
	TreatmentRepository treatmentRepository = new TreatmentRepository();

	private void CloseConnection(Connection con, Statement stmt,PreparedStatement pstmt, ResultSet rs) throws SQLException {
		if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
        if (con != null) {
            con.close();
        }
	}
	
	public void createTableBirdTreatment(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table BIRD_TREATMENT ...");
			String sql = "CREATE TABLE IF NOT EXISTS BIRD_TREATMENT"
					+" (id INTEGER auto_increment, "
					+"BirdId INTEGER NOT NULL, "
					+"TreatmentId INTEGER NOT NULL, "
					+"Start VARCHAR(255) NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BirdId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
					+"FOREIGN KEY (TreatmentId) REFERENCES TREATMENT(id) ON DELETE CASCADE)";
			stmt.executeUpdate(sql);
			System.out.println("Table BIRD_TREATMENT Created.");
	}
	
	public void dropTableBirdTreatment(Connection con, Statement stmt) throws SQLException {
			String sql = "DROP TABLE IF EXISTS BIRD_TREATMENT";
			stmt.executeUpdate(sql);
			System.out.println("Table BIRD_TREATMENT dropped.");
	}
	
	public void insert(BirdTreatment bt) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO BIRD_TREATMENT (BirdId, TreatmentId, Start) VALUES (" + bt.getBird().getId() + ", " + bt.getTreatment().getId() + ", '" + bt.getStart() + "')";
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null, null);
			System.out.println("Passaro["+bt.getBird().getBand()+"] tratamento: "+bt.getTreatment().getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
