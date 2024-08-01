package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import constants.MyValues;
import domains.Egg;

public class EggRepository {
	
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
	
	public void createTableEgg(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Creating Table EGG ...");
	        String sql = "CREATE TABLE IF NOT EXISTS EGG"
	                +" (id INTEGER auto_increment, "
	                +"PostureDate DATE NOT NULL, "
	                +"VerifiedFertilityDate DATE, "
	                +"OutbreakDate DATE, "
	                +"Type VARCHAR(255) NOT NULL, "
	                +"Statute VARCHAR(255) NOT NULL, "
	                +"BroodId INTEGER NOT NULL, "
	                +"BirdId INTEGER, "
	                +"FOREIGN KEY (BroodId) REFERENCES BROOD(id) ON DELETE CASCADE, "
	                +"FOREIGN KEY (BirdId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
	                +"PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table EGG Created.");
	}
	
	public void dropTableEgg(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Trying to drop EGG table...");
	        String sql = "DROP TABLE IF EXISTS EGG CASCADE"; 
	        stmt.executeUpdate(sql);
	        System.out.println("Table EGG dropped.");
	}

	public void insert(Integer broodId,List<Egg> eggs) throws SQLException {
			System.out.println("Insert EGG in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO EGG (PostureDate, Type, Statute,BroodId) VALUES (?, ?, ?, ?)");
			for (Egg egg : eggs) {
				 pstmt.setDate(1, new java.sql.Date(egg.getPostureDate().getTime()));
				 pstmt.setString(2, egg.getType());
				 pstmt.setString(3, egg.getStatute());
				 pstmt.setInt(4, broodId);
				 pstmt.executeUpdate();
				 System.out.println("Eggs inserted with success!");
			}
			CloseConnection(con, null, pstmt, null);
	}
	
	
	 public List<Egg> getEggsForBrood(int broodId) {
	       	List<Egg> eggs = new ArrayList<>();
	        try {
	            Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,MyValues.USER, MyValues.PASSWORD);
	            String sql = "SELECT * FROM EGG WHERE BroodId = ?";
	            PreparedStatement  pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, broodId);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	Egg e = new Egg();
	                e.setId(rs.getInt("id"));
	                e.setPostureDate(rs.getDate("PostureDate"));
	                e.setVerifiedFertilityDate(rs.getDate("VerifiedFertilityDate"));
	                e.setOutbreakDate(rs.getDate("OutbreakDate"));
	                e.setType(rs.getString("Type"));
	                e.setStatute(rs.getString("Statute"));
	                e.setBird(null);//TODO
	                eggs.add(e);
	            }
	            CloseConnection(con, null, pstmt, rs);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return eggs;
	    }

//	public State getStateById(int id) throws SQLException {
//		State state = new State();
//		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
//		Statement stmt = con.createStatement();
//		ResultSet rs = stmt.executeQuery("SELECT * FROM STATE WHERE ID = " + id);
//		if (rs.next()) {
//			state.setId(rs.getInt(1));
//			state.setType(rs.getString(2));
//			state.setDate(rs.getString(3));
//			state.setValor(rs.getDouble(4));
//			state.setMotivo(rs.getString(5));
//		}
//		CloseConnection(con, stmt, null, rs);
//		return state;
//	}
//
//	public void deleteState(int id) throws SQLException {
//		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
//		PreparedStatement stmt = con.prepareStatement("DELETE FROM STATE WHERE ID = ?");
//		stmt.setInt(1, id);
//		stmt.executeUpdate();
//		CloseConnection(con, stmt, null, null);
//	}

//	public void updateState(State state) throws SQLException {
//		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
//		PreparedStatement pstmt = con.prepareStatement("UPDATE STATE SET Type=?, Date=?, Valor=?, Motivo=? WHERE id=?");
//		pstmt.setString(1, state.getType());
//		pstmt.setString(2, state.getDate());
//		if (state.getValor() == null)
//			state.setValor((double) 0);
//		pstmt.setDouble(3, state.getValor());
//		if (state.getMotivo() == null)
//			state.setMotivo("");
//		pstmt.setString(4, state.getMotivo());
//		pstmt.setInt(5, state.getId());
//		int rowsAffected = pstmt.executeUpdate();
//		CloseConnection(con, null, pstmt, null);
//		System.out.println(rowsAffected + " row(s) updated.");
//	}

}
