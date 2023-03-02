package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import constants.MyValues;
import domains.Historic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HistoricRepository {
	
	BirdsRepository birdsRepository = new BirdsRepository();
	
	public void createTableHistoric() {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	        System.out.println("Creating Table HISTORIC ...");
	        Statement stmt = con.createStatement();
	        String sql = "CREATE TABLE IF NOT EXISTS HISTORIC"
	                + " (id INTEGER auto_increment, "
	                + "Title VARCHAR(255) NOT NULL, "
	                + "Date VARCHAR(255) NOT NULL, "
	                + "Obs VARCHAR(255) NOT NULL, "
	                + "BirdId INTEGER NOT NULL, "
	                + "PRIMARY KEY (id), "
	                + "FOREIGN KEY (BirdId) REFERENCES BIRDS(id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table HISTORIC Created.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void DropTableHistoric() {
		try {
			System.out.println("Trying to drop HISTORIC table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS HISTORIC CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table HISTORIC Droped.");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public ObservableList<Historic> getAllByBirdId(int birdId) {
	    ObservableList<Historic> historics = FXCollections.observableArrayList();
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	        String sql = "SELECT * FROM HISTORIC WHERE BirdId = ?";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setInt(1, birdId);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            Historic historic = new Historic();
	            historic.setId(rs.getInt("id"));
	            historic.setTitle(rs.getString("Title"));
	            historic.setDate(rs.getString("Date"));
	            historic.setObs(rs.getString("Obs"));
	            historic.setBird(birdsRepository.getBirdWhereInt("id", rs.getInt("BirdId")));
	            historics.add(historic);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return historics;
	}
	
	public void insertHistoric(Historic historic) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	        String sql = "INSERT INTO HISTORIC (Title, Date, Obs, BirdId) VALUES (?, ?, ?, ?)";
	        PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        stmt.setString(1, historic.getTitle());
	        stmt.setString(2, historic.getDate());
	        stmt.setString(3, historic.getObs());
	        stmt.setInt(4, historic.getBird().getId());
	        stmt.executeUpdate();
	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            int id = rs.getInt(1);
	            historic.setId(id);
	        }
	        System.out.println("New record created in HISTORIC table.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
