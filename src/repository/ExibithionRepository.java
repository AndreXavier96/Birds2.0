package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Exibithion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExibithionRepository {

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
	
	public void createTableExibithion(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table EXIBITHION ...");
			String sql = "CREATE TABLE IF NOT EXISTS EXIBITHION"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Locale VARCHAR(255), "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table EXIBITHION Created.");
	}
	
	public void dropTableExibithion(Connection con, Statement stmt) throws SQLException {
			System.out.println("Trying to drop EXIBITHION table...");
			String sql = "DROP TABLE IF EXISTS EXIBITHION CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table EXIBITHION Droped.");
	}
	
	public ObservableList<Exibithion> getAllExibithions() {
		System.out.println("Getting all Exibithions...");
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM EXIBITHION";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Exibithion> exibithions = FXCollections.observableArrayList();
			while(rs.next()) {
				System.out.println("Get exibithions: "+rs.getInt(1));
				Exibithion e = new Exibithion();
				e.setId(rs.getInt(1));
				e.setName(rs.getString(2));
				e.setLocale(rs.getString(3));
				exibithions.add(e);
			}
			CloseConnection(con, stmt, null, rs);
			return exibithions;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void Insert(Exibithion e) throws SQLException {
		int exibithionId = -1;
		System.out.println("Insert Exibithion in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
				MyValues.PASSWORD);
		String sql = "INSERT INTO "
				+ "EXIBITHION(Name,Locale) "
				+ "values(?,?)";
		PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, e.getName());
		stmt.setString(2, e.getLocale());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next())
			exibithionId = rs.getInt(1);
		CloseConnection(con, null, stmt, rs);
		System.out.println("[" + exibithionId + "] inserted: " + sql);
	}
	
	 public Exibithion getExibithionByName(String name) throws SQLException {
	        Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM EXIBITHION WHERE Name='" + name + "';";
	        ResultSet rs = stmt.executeQuery(sql);
	        Exibithion exibithion = null;
	        
	        if (rs.next()) {
	            System.out.println("Get Exibithion: " + rs.getInt("id"));
	            exibithion = new Exibithion();
	            exibithion.setId(rs.getInt("id"));
	            exibithion.setName(rs.getString("Name"));
	            exibithion.setLocale(rs.getString("Locale"));
	        }

	        CloseConnection(con, stmt, null, rs);
	        return exibithion;
	    }
	 
	 public Exibithion getExibithionById(Integer id) throws SQLException {
	        Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM EXIBITHION WHERE id='" + id + "';";
	        ResultSet rs = stmt.executeQuery(sql);
	        Exibithion exibithion = null;
	        
	        if (rs.next()) {
	            System.out.println("Get Exibithion: " + rs.getInt("id"));
	            exibithion = new Exibithion();
	            exibithion.setId(rs.getInt("id"));
	            exibithion.setName(rs.getString("Name"));
	            exibithion.setLocale(rs.getString("Locale"));
	        }

	        CloseConnection(con, stmt, null, rs);
	        return exibithion;
	    }
	
}
