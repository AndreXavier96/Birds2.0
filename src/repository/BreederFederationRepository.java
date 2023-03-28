package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import constants.MyValues;

public class BreederFederationRepository {

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
	
	public void createTableBreederFederation(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table CLUB ...");
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER_FEDERATION"
					+" (id INTEGER auto_increment, "
					+"Stam VARCHAR(255) NOT NULL UNIQUE, "
					+"BreederId INTEGER NOT NULL, "
					+"FederationId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER(id) ON DELETE CASCADE, "
					+"FOREIGN KEY (FederationId) REFERENCES FEDERATION(id) ON DELETE CASCADE)";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_FEDERATION Created.");
	}
	
	public void dropTableBreederFederation(Connection con, Statement stmt) throws SQLException {
			String sql = "DROP TABLE IF EXISTS BREEDER_FEDERATION";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_FEDERATION dropped.");
	}
	
	public void insert(int breederId, int federationId,String stam) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO BREEDER_FEDERATION (Stam, BreederId, FederationId) VALUES (?, ?, ?)");
			pstmt.setString(1, stam);
			pstmt.setInt(2, breederId);
			pstmt.setInt(3, federationId);
			pstmt.executeUpdate();
			CloseConnection(con, null, pstmt, null);
			System.out.println("["+stam+"] inserted: "+pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, String> getAllByBreederId(int breederId) {
	    HashMap<Integer, String> stams = new HashMap<>();
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement pstmt = con.prepareStatement("SELECT FederationId, Stam FROM BREEDER_FEDERATION WHERE BreederId = ?");
	        pstmt.setInt(1, breederId);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            int federationId = rs.getInt("FederationId");
	            String stam = rs.getString("Stam");
	            stams.put(federationId, stam);
	        }
	        CloseConnection(con, null, pstmt, rs);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return stams;
	}
	
	
	public String getStamByBreederAndFederationId(int breederId, int federationId) throws SQLException {
	    String stam = null;
	    String sql = "SELECT Stam FROM BREEDER_FEDERATION WHERE BreederId = ? AND FederationId = ?";
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {
	        pstmt.setInt(1, breederId);
	        pstmt.setInt(2, federationId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                stam = rs.getString("stam");
	            }
	            CloseConnection(con, null, pstmt, rs);
	        }
	    }
	    return stam;
	}

	public boolean checkIfStamExists(String stam) throws SQLException {
		boolean exists = false;
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
		PreparedStatement pstmt = con.prepareStatement("SELECT COUNT(*) FROM BREEDER_FEDERATION WHERE Stam = ?");
		pstmt.setString(1, stam);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		exists = rs.getInt(1) > 0;
		CloseConnection(con, null, pstmt, rs);
		return exists;
	}
	
}
