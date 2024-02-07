package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Bird;
import domains.Couples;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CouplesRepository {

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
	
	public void CreateTableCouples(Connection con,Statement stmt) throws SQLException {
			System.out.println("Creating Table COUPLES ...");
			String sql = "CREATE TABLE IF NOT EXISTS COUPLES"
					+" (id INTEGER auto_increment, "
					+"MaleId INTEGER NOT NULL, "
					+"FemaleId INTEGER NOT NULL, "
					+"CageId INTEGER, "
					+"State VARCHAR(255), "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (MaleId) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (FemaleId) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (CageId) REFERENCES CAGE (id)) ";
			stmt.executeUpdate(sql);
			System.out.println("Table COUPLES Created.");
	}
	
	
	public void Insert(Couples couple) throws SQLException {
		System.out.println("Insert Couple in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "INSERT INTO "
				+"COUPLES(MaleId,FemaleId,CageId,State) "
				+"values('"+couple.getMale().getId()+"','"+couple.getFemale().getId()+"','"
				+couple.getCage().getId()+"','"+couple.getState()+"')";
		int i = stmt.executeUpdate(sql);
		CloseConnection(con, stmt, null);
		System.out.println(i+" Couple Record inserted");
	}
	
	public ObservableList<Bird> getAllBirdsWhere(String select, String col,String value) {
		try {
			System.out.println("Getting all Birds...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT "+select+" FROM COUPLES WHERE "+col+"='"+value+"'";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Bird> birds = FXCollections.observableArrayList();
			BirdsRepository birdsRepository = new BirdsRepository();
			while (rs.next()) {
				System.out.println("Get Bird: " + rs.getInt(1));
				Bird b = birdsRepository.getBirdWhereInt("id", rs.getInt(1));
				birds.add(b);
			}
			CloseConnection(con, stmt, null, rs);
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Couples> getAllCouples() throws SQLException {
		System.out.println("Getting all Couples...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM COUPLES";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Couples> couples = FXCollections.observableArrayList();
		BirdsRepository birdsRepository = new BirdsRepository();
		CageRepository cageRepository = new CageRepository();
		while (rs.next()) {
			System.out.println("Get COUPLES: " + rs.getString(3));
			Couples c = new Couples();
			c.setId(rs.getInt(1));
			c.setMale(birdsRepository.getBirdWhereInt("id", rs.getInt(2)));
			c.setFemale(birdsRepository.getBirdWhereInt("id", rs.getInt(3)));
			c.setCage(cageRepository.getCage(rs.getInt(4)));
			c.setState(rs.getString(5));
			couples.add(c);
		}
		CloseConnection(con, stmt,null, rs);
		return couples;
	}
	
	
	public boolean checkIfCouplesExist(Bird bird) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM COUPLES WHERE MaleId='"+bird.getId()+"' OR FemaleId='"+bird.getId()+"' ;";
		ResultSet rs = stmt.executeQuery(sql);
		boolean result = rs.next();
		CloseConnection(con, stmt,null, rs);
		return result;	
}
	
	
	public Couples getCouplesWhereBird(Bird bird) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM COUPLES WHERE MaleId='"+bird.getId()+"' OR FemaleId='"+bird.getId()+"' ;";
		ResultSet rs = stmt.executeQuery(sql);
		Couples c = new Couples();
		BirdsRepository birdsRepository = new BirdsRepository();
		CageRepository cageRepository = new CageRepository();
		if (rs.next()) {
			c.setId(rs.getInt(1));
			c.setMale(birdsRepository.getBirdWhereInt("id", rs.getInt(2)));
			c.setFemale(birdsRepository.getBirdWhereInt("id", rs.getInt(3)));
			c.setCage(cageRepository.getCage(rs.getInt(4)));
			c.setState(rs.getString(5));
			CloseConnection(con, stmt,null, rs);
			return c;
		} else {
			CloseConnection(con, stmt,null, rs);
			return null;
		}
	}
	
	public void deleteCouple(Couples couple) throws SQLException {
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	         PreparedStatement pstmt = con.prepareStatement("DELETE FROM COUPLES WHERE id=?")) {
	        pstmt.setInt(1, couple.getId());
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Failed to delete couple, no rows affected.");
	        } else {
	            System.out.println("Couple with id " + couple.getId() + " deleted!");
	        }
	    } catch (SQLException e) {
	        throw e;
	    }
	}

	
	public void DropTableCouples(Connection con,Statement stmt) throws SQLException  {
			System.out.println("Trying to drop COUPLES table...");
			String sql = "DROP TABLE IF EXISTS COUPLES CASCADE";	
			stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println("Table COUPLES Droped.");
	}
	
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
}
