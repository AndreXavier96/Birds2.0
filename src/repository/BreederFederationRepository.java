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

	public void createTableBreederFederation() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table CLUB ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER_FEDERATION"
					+" (id INTEGER auto_increment, "
					+"Stam VARCHAR(255) NOT NULL, "
					+"BreederId INTEGER NOT NULL, "
					+"FederationId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER(id), "
					+"FOREIGN KEY (FederationId) REFERENCES FEDERATION(id))";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_FEDERATION Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dropTableBreederFederation() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS BREEDER_FEDERATION";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_FEDERATION dropped.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insert(int breederId, int federationId,String stam) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO BREEDER_FEDERATION (Stam, BreederId, FederationId) VALUES (?, ?, ?)");
			pstmt.setString(1, stam);
			pstmt.setInt(2, breederId);
			pstmt.setInt(3, federationId);
			pstmt.executeUpdate();
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
	        rs.close();
	        pstmt.close();
	        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return stams;
	}
	
	
	public String getStamByBreederAndFederationId(int breederId, int federationId) throws SQLException {
	    String stam = null;
	    String sql = "SELECT Stam FROM BREEDER_FEDERATION WHERE BreederId = ? AND FederationId = ?";
	    try (Connection conn = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, breederId);
	        pstmt.setInt(2, federationId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                stam = rs.getString("stam");
	            }
	        }
	    }
	    return stam;
	}


}
