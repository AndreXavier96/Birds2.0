package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.State;

public class StateRepository {
	
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
	
	public void getStatesConsole() {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM STATE");
			if (rs.next()) {
				System.out.println("Id:" + rs.getInt(1) + " Type:" + rs.getString(2));
			}
			CloseConnection(con, stmt, null, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CreateTableState(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Creating Table STATE ...");
	        String sql = "CREATE TABLE IF NOT EXISTS STATE"
	                +" (id INTEGER auto_increment, "
	                +"Type VARCHAR(255) NOT NULL, "
	                +"Date VARCHAR(255), "
	                +"Valor DOUBLE, "
	                +"Motivo VARCHAR(255), "
	                +"PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table STATE Created.");
	}
	
	public void dropTableState(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Trying to drop STATE table...");
	        String sql = "DROP TABLE IF EXISTS STATE CASCADE"; 
	        stmt.executeUpdate(sql);
	        System.out.println("Table STATE dropped.");
	}

	public State insertVivo() throws SQLException {
		State state = new State();
		int generatedId = -1;
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		System.out.println("Inserting VIVO state in database...");
		String sql = "INSERT INTO STATE (Type) VALUES (?)";
		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, MyValues.OTHER);
		int i = pstmt.executeUpdate();
		if (i == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getInt(1);
				state = getStateById(generatedId);
			}
		}
		CloseConnection(con, null, pstmt, null);
		return state;
	}
	
	public State insertState(State state) throws SQLException {
		State returnState = new State();
		int generatedId = -1;
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		System.out.println("Inserting " + state.getType() + " state in database...");
		String sql = "INSERT INTO STATE (Type,Date,Valor,Motivo) VALUES (?, ?, ?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, state.getType());
		pstmt.setString(2, state.getDate());
		Double valor = (double) 0;
		if (state.getValor() != null)
			valor = state.getValor();
		pstmt.setDouble(3, valor);
		String motivo = "";
		if (state.getMotivo() != null) {
			motivo = state.getMotivo();
		}
		pstmt.setString(4, motivo);
		int i = pstmt.executeUpdate();
		if (i == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getInt(1);
				returnState = getStateById(generatedId);
			}
		}
		CloseConnection(con, null, pstmt, null);
		return returnState;
	}

	

	public State getStateById(int id) throws SQLException {
		State state = new State();
		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM STATE WHERE ID = " + id);
		if (rs.next()) {
			state.setId(rs.getInt(1));
			state.setType(rs.getString(2));
			state.setDate(rs.getString(3));
			state.setValor(rs.getDouble(4));
			state.setMotivo(rs.getString(5));
		}
		CloseConnection(con, stmt, null, rs);
		return state;
	}

	public void deleteState(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement stmt = con.prepareStatement("DELETE FROM STATE WHERE ID = ?");
		stmt.setInt(1, id);
		stmt.executeUpdate();
		CloseConnection(con, stmt, null, null);
	}
	
	
	public void updateState(State state) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement pstmt = con.prepareStatement("UPDATE STATE SET Type=?, Date=?, Valor=?, Motivo=? WHERE id=?");
		pstmt.setString(1, state.getType());
		pstmt.setString(2, state.getDate());
		if (state.getValor() == null)
			state.setValor((double) 0);
		pstmt.setDouble(3, state.getValor());
		if (state.getMotivo() == null)
			state.setMotivo("");
		pstmt.setString(4, state.getMotivo());
		pstmt.setInt(5, state.getId());
		int rowsAffected = pstmt.executeUpdate();
		CloseConnection(con, null, pstmt, null);
		System.out.println(rowsAffected + " row(s) updated.");
	}

}
