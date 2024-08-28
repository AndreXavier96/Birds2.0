package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Cage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CageRepository {
	
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
	
	public void CreateTableCage(Connection con,Statement stmt) throws SQLException {
			System.out.println("Creating Table CAGE ...");
			String sql = "CREATE TABLE IF NOT EXISTS CAGE"
					+" (id INTEGER auto_increment, "
					+"Code VARCHAR(255) NOT NULL UNIQUE, "
					+"Type VARCHAR(255) NOT NULL, "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table CAGE Created.");
	}
	
	public void dropTableCage(Connection con,Statement stmt) throws SQLException {
			System.out.println("Trying to drop MUTATIONS table...");
			String sql = "DROP TABLE IF EXISTS CAGE CASCADE";	
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt,null, null);
			System.out.println("Table CAGE Droped.");
	}
	
	
	public Cage getCage(Integer id) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CAGE WHERE CAGE.id='"+id+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			Cage c = new Cage();
			while (rs.next()) {
				c.setId(rs.getInt(1));
				c.setCode(rs.getString(2));
				c.setType(rs.getString(3));
			}
			CloseConnection(con, stmt,null, rs);
			return c;
	}
	
	public void Insert(Cage cage) throws SQLException {
		System.out.println("Insert Cage in Database...");
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO CAGE(Code,Type) VALUES('"
				+cage.getCode().replace("'","''")+"','"+cage.getType()+"')";
		int i = stmt.executeUpdate(sql);
		CloseConnection(con, stmt,null, null);
		System.out.println(i+" Record inserted");
	}
	
	public boolean checkIfCodeExist(String code)  throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT Code FROM CAGE WHERE Code='"+code.replace("'", "''")+"';";
		ResultSet rs = stmt.executeQuery(sql);
		boolean result = rs.next();
		CloseConnection(con, stmt,null, rs);
		return result;
	}
	
	public ObservableList<Cage> getAllCages() throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CAGE";
			ResultSet rs  = stmt.executeQuery(sql);
			ObservableList<Cage> cages = FXCollections.observableArrayList();
			while (rs.next()) {
				Cage c = new Cage();
				c.setId(rs.getInt(1));
				c.setCode(rs.getString(2));
				c.setType(rs.getString(3));
				cages.add(c);
			}
			CloseConnection(con, stmt,null, rs);
			return cages;
	}
	
	
	public void deleteCage(Cage cage) throws SQLException {
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	         PreparedStatement pstmt = con.prepareStatement("DELETE FROM CAGE WHERE id=?")) {
	        pstmt.setInt(1, cage.getId());
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Failed to delete cage, no rows affected.");
	        } else {
	            System.out.println("Cage with id " + cage.getId() + " deleted!");
	        }
	    } catch (SQLException e) {
	        throw e;
	    }
	}
	
	public Cage getCageByCode(String code) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    Statement stmt = con.createStatement();
	    String sql = "SELECT * FROM CAGE WHERE Code='" + code.replace("'", "''") + "'";
	    ResultSet rs = stmt.executeQuery(sql);
	    Cage cage = null;
	    if (rs.next()) {
	        cage = new Cage();
	        cage.setId(rs.getInt("id"));
	        cage.setCode(rs.getString("Code"));
	        cage.setType(rs.getString("Type"));
	    }
	    CloseConnection(con, stmt, null, rs);
	    return cage;
	}


}
