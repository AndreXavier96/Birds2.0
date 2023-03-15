package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Historic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HistoricRepository {
	
	BirdsRepository birdsRepository = new BirdsRepository();

	private void CloseConnection(Connection con, Statement stmt,PreparedStatement pstmt, ResultSet rs) throws SQLException {
		if (rs != null) {
            rs.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
	}
	
	public void createTableHistoric(Connection con, Statement stmt) throws SQLException {
		System.out.println("Creating Table HISTORIC ...");
		String sql = "CREATE TABLE IF NOT EXISTS HISTORIC" + " (id INTEGER auto_increment, "
				+ "Title VARCHAR(255) NOT NULL, " + "Date VARCHAR(255) NOT NULL, " + "Obs VARCHAR(255) NOT NULL, "
				+ "BirdId INTEGER NOT NULL, " + "PRIMARY KEY (id), " + "FOREIGN KEY (BirdId) REFERENCES BIRDS(id))";
		stmt.executeUpdate(sql);
		System.out.println("Table HISTORIC Created.");
	}
	
	public void DropTableHistoric(Connection con, Statement stmt) throws SQLException {
		System.out.println("Trying to drop HISTORIC table...");
		String sql = "DROP TABLE IF EXISTS HISTORIC CASCADE";
		stmt.executeUpdate(sql);
		System.out.println("Table HISTORIC Droped.");
	}

	public ObservableList<Historic> getAllByBirdId(int birdId) throws SQLException {
		ObservableList<Historic> historics = FXCollections.observableArrayList();
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		String sql = "SELECT * FROM HISTORIC WHERE BirdId = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, birdId);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Historic historic = new Historic();
			historic.setId(rs.getInt("id"));
			historic.setTitle(rs.getString("Title"));
			historic.setDate(rs.getString("Date"));
			historic.setObs(rs.getString("Obs"));
			historic.setBird(birdsRepository.getBirdWhereInt("id", rs.getInt("BirdId")));
			historics.add(historic);
		}
		CloseConnection(con, null, pstmt, rs);
		return historics;
	}
	
	public void insertHistoric(Historic historic) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		String sql = "INSERT INTO HISTORIC (Title, Date, Obs, BirdId) VALUES (?, ?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, historic.getTitle());
		pstmt.setString(2, historic.getDate());
		pstmt.setString(3, historic.getObs());
		pstmt.setInt(4, historic.getBird().getId());
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
			int id = rs.getInt(1);
			historic.setId(id);
		}
		CloseConnection(con, null, pstmt, rs);
		System.out.println("New record created in HISTORIC table.");
	}
}
