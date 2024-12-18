package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TreatmentRepository {
	
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
	
	  public void createTableTreatment(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Creating Table TREATMENT ...");
	        String sql = "CREATE TABLE IF NOT EXISTS TREATMENT"
	                   + " (id INTEGER auto_increment, "
	                   + "Name VARCHAR(255) NOT NULL, "
	                   + "Description VARCHAR(255) NOT NULL, "
	                   + "Frequency INTEGER NOT NULL, "
	                   + "FrequencyType  VARCHAR(255) NOT NULL, "
	                   + "DurationDays INTEGER NOT NULL, "
	                   + "TimesAplied INTEGER NOT NULL, "
	                   + "BirdsTreated INTEGER NOT NULL, "
	                   + "PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table TREATMENT Created.");
	    }
	
	  public void dropTableTreatment(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Trying to drop TREATMENT table...");
	        String sql = "DROP TABLE IF EXISTS TREATMENT CASCADE";
	        stmt.executeUpdate(sql);
	        CloseConnection(con, stmt,null, null);
	        System.out.println("Table TREATMENT Dropped.");
	    }
	
	
	public Treatment getTreatmentById(Integer id) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM TREATMENT  WHERE id='"+id+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			Treatment t = new Treatment();
			while (rs.next()) {
				t.setId(rs.getInt(1));
				t.setName(rs.getString(2));
				t.setDescription(rs.getString(3));
				t.setFrequency(rs.getInt(4));
				t.setFrequencyType(rs.getString(5));
				t.setDurationDays(rs.getInt(6));
				t.setTimesAplied(rs.getInt(7));
				t.setBirdsTreated(rs.getInt(8));
			}
			CloseConnection(con, stmt,null, rs);
			return t;
	}
	
	public void Insert(Treatment treatment) throws SQLException {
		System.out.println("Insert Treatment in Database...");
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO TREATMENT(Name, Description, Frequency,FrequencyType,DurationDays,TimesAplied,BirdsTreated) VALUES ('"
                + treatment.getName().replace("'", "''") + "','" + treatment.getDescription().replace("'", "''") + "','"
                + treatment.getFrequency()+ "','" + treatment.getFrequencyType().replace("'", "''")+ "','" +treatment.getDurationDays()+ "','"+ 0 + "','"+ 0 + "')";
        int i = stmt.executeUpdate(sql);
		CloseConnection(con, stmt,null, null);
		System.out.println(i+" Record inserted: "+sql);
	}
	
	public void updateTreatment(Treatment treatment) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE TREATMENT SET Name = ?, Description = ?, Frequency = ?, FrequencyType = ?, DurationDays = ? WHERE id = ?";
			stmt = con.prepareStatement(sql);
			stmt = con.prepareStatement(sql);
			stmt.setString(1, treatment.getName());
			stmt.setString(2, treatment.getDescription());
			stmt.setInt(3, treatment.getFrequency());
			stmt.setString(4, treatment.getFrequencyType());
			stmt.setInt(5, treatment.getDurationDays());
			stmt.setInt(6, treatment.getId());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Treatment updated successfully.");
			} else {
				System.out.println("Treatment not found.");
			}
		} finally {
			CloseConnection(con, stmt, null, null);
		}
	}
	
	public void updateTreatmentStats(Treatment treatment,Integer timesAplied,Integer birdsTreated) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE TREATMENT SET TimesAplied = ?, BirdsTreated = ? WHERE id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, timesAplied);
			stmt.setInt(2, birdsTreated);
			stmt.setInt(3, treatment.getId());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Treatment stats updated successfully.");
			} else {
				System.out.println("Treatment not found.");
			}
		} finally {
			CloseConnection(con, stmt, null, null);
		}
	}
	
	public ObservableList<Treatment> getAllTreatments() throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM TREATMENT";
			ResultSet rs  = stmt.executeQuery(sql);
			ObservableList<Treatment> treatments = FXCollections.observableArrayList();
			while (rs.next()) {
				Treatment t = new Treatment();
				t.setId(rs.getInt(1));
				t.setName(rs.getString(2));
				t.setDescription(rs.getString(3));
				t.setFrequency(rs.getInt(4));
				t.setFrequencyType(rs.getString(5));
				t.setDurationDays(rs.getInt(6));
				t.setTimesAplied(rs.getInt(7));
				t.setBirdsTreated(rs.getInt(8));
				treatments.add(t);
			}
			CloseConnection(con, stmt,null, rs);
			return treatments;
	}
	
	
	public void deleteTreatment(Treatment t) throws SQLException {
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	         PreparedStatement pstmt = con.prepareStatement("DELETE FROM TREATMENT WHERE id=?")) {
	        pstmt.setInt(1, t.getId());
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Failed to delete treatment, no rows affected.");
	        } else {
	            System.out.println("TREATMENT with id " + t.getId() + " deleted!");
	        }
	    } catch (SQLException e) {
	        throw e;
	    }
	}

}
