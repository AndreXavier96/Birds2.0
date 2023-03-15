package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BreederClubRepository {

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
	
	public void createTableBreederClub(Connection con,	Statement stmt) throws SQLException {
			System.out.println("Creating Table CLUB ...");
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER_CLUB"
					+" (id INTEGER auto_increment, "
					+"BreederId INTEGER NOT NULL, "
					+"ClubId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER(id), "
					+"FOREIGN KEY (ClubId) REFERENCES CLUB(id))";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_CLUB Created.");
	}

	public void dropTableBreederClub(Connection con,	Statement stmt) throws SQLException {
			System.out.println("Trying to drop BREEDER_CLUB table...");
			String sql = "DROP TABLE IF EXISTS BREEDER_CLUB CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER_CLUB Droped.");
	}
	
	public void Insert(Integer breederId, Integer clubId) {
		int id=-1;
		try {
			System.out.println("Insert BREEDER_CLUB in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			
			String sql = "INSERT INTO "
					+"BREEDER_CLUB(BreederId,ClubId) "
					+"values(?,?)";
			PreparedStatement stmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, breederId);
			stmt.setInt(2, clubId);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }
	        CloseConnection(con, stmt, rs);
			System.out.println("["+id+"] BREEDER_CLUB inserted: "+sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer getFromBreederClub(String select,String col, Integer value) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT "+select+" FROM CLUB WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				int result = rs.getInt(0);
				CloseConnection(con, stmt, rs);
				return result;
			}else {
				CloseConnection(con, stmt, rs);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Integer> getClubsFromBreederId(Integer breederId) throws SQLException {
		ObservableList<Integer> clubsIds = FXCollections.observableArrayList();
		try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
				PreparedStatement stmt = con.prepareStatement("SELECT ClubId FROM BREEDER_CLUB WHERE BreederId=?")) {
			stmt.setInt(1, breederId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int clubId = rs.getInt(1);
					clubsIds.add(clubId);
				}
				rs.close();
			}
			CloseConnection(con, stmt, null);
		}
		return clubsIds;
	}
	
	
	
}
