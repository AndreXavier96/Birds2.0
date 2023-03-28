package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Federation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FederationRepository {

	BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	
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
	
	public void createTableFederation(Connection con,Statement stmt) throws SQLException {
			System.out.println("Creating Table FEDERATION ...");
			String sql = "CREATE TABLE IF NOT EXISTS FEDERATION"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL UNIQUE, "
					+"Acronym VARCHAR(255) NOT NULL UNIQUE, "
					+"Email VARCHAR(255) UNIQUE, "
					+"Country VARCHAR(255), "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table FEDERATION Created.");
	}
	
	public void dropTableFederation(Connection con, Statement stmt) throws SQLException {
		System.out.println("Trying to drop FEDERATION table...");
		String sql = "DROP TABLE IF EXISTS FEDERATION CASCADE";
		stmt.executeUpdate(sql);
		System.out.println("Table FEDERATION Droped.");
	}

	public void Insert(Federation f) throws SQLException {
		System.out.println("Insert Federation in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO " + "FEDERATION(Name,Acronym,Email,Country) " 
					+ "values('" + f.getName() + "','"
					+ f.getAcronym() + "','" + f.getEmail() + "','" + f.getCountry() + "')";
		int i = stmt.executeUpdate(sql);
		CloseConnection(con, stmt,null, null);
		System.out.println(i + "Record inserted: " + sql);
	}
	
	
	public ObservableList<Federation> getAllFederations() throws SQLException {
		System.out.println("Getting all Federations...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM FEDERATION";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Federation> federations = FXCollections.observableArrayList();
		while (rs.next()) {
			System.out.println("Get Federations: " + rs.getString(3));
			Federation f = new Federation();
			f.setId(rs.getInt(1));
			f.setName(rs.getString(2));
			f.setAcronym(rs.getString(3));
			f.setEmail(rs.getString(4));
			;
			f.setCountry(rs.getString(5));
			federations.add(f);
			System.out.println(f);
		}
		CloseConnection(con, stmt,null, rs);
		return federations;

	}
	
	public Federation getFederationWhereString(String col, String value) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM FEDERATION WHERE " + col + "='" + value + "';";
		ResultSet rs = stmt.executeQuery(sql);
		Federation f = new Federation();
		if (rs.next()) {
			f.setId(rs.getInt(1));
			f.setName(rs.getString(2));
			f.setAcronym(rs.getString(3));
			f.setEmail(rs.getString(4));
			;
			f.setCountry(rs.getString(5));
			CloseConnection(con, stmt,null, rs);
			return f;
		} else {
			CloseConnection(con, stmt,null, rs);
			return null;
		}
	}

	public Federation getFederationWhereInt(String col, Integer value) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM FEDERATION WHERE " + col + "=" + value + ";";
		ResultSet rs = stmt.executeQuery(sql);
		Federation f = new Federation();
		if (rs.next()) {
			f.setId(rs.getInt(1));
			f.setName(rs.getString(2));
			f.setAcronym(rs.getString(3));
			f.setEmail(rs.getString(4));
			f.setCountry(rs.getString(5));
			CloseConnection(con, stmt,null, rs);
			return f;
		} else {
			CloseConnection(con, stmt,null, rs);
			return null;
		}
	}
	
	public boolean checkIfExistsString(String col,String value) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM FEDERATION WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			boolean result = rs.next();
			CloseConnection(con, stmt,null, rs);
			return result;	
	}
	
	public void deleteFederation(Federation federation) throws SQLException {
		try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
				PreparedStatement pstmt = con.prepareStatement("DELETE FROM FEDERATION WHERE id=?");) {
			con.setAutoCommit(false);
			pstmt.setInt(1, federation.getId());
			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted == 0) {
				throw new SQLException("Failed to delete federation, no rows affected.");
			} else {
				System.out.println("Federation " + federation.getName() + " deleted!");
			}
			con.commit();
		} catch (SQLException e) {
			throw e;
		}
	}

}
