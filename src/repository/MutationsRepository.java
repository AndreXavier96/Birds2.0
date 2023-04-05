package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Mutation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MutationsRepository {
	
	private SpeciesRepository speciesRepository = new SpeciesRepository();

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
	
	public void CreateTableMutations(Connection con, Statement stmt) throws SQLException {
		System.out.println("Creating Table MUTATIONS ...");
		String sql = "CREATE TABLE IF NOT EXISTS MUTATIONS" 
				+ " (id INTEGER auto_increment, "
				+ "Name VARCHAR(255) NOT NULL, " 
				+ "Var1 VARCHAR(255), " 
				+ "Var2 VARCHAR(255), " 
				+ "Var3 VARCHAR(255), "
				+ "Obs VARCHAR(500), " 
				+ "SpeciesId INTEGER NOT NULL, " 
				+ "PRIMARY KEY (id), "
				+ "FOREIGN KEY (SpeciesId) REFERENCES SPECIES (id) ON DELETE CASCADE)";
		stmt.executeUpdate(sql);
		System.out.println("Table MUTATIONS Created.");
	}
	
	public void DropTableMutations(Connection con, Statement stmt) throws SQLException {
		System.out.println("Trying to drop MUTATIONS table...");
		String sql = "DROP TABLE IF EXISTS MUTATIONS CASCADE";
		stmt.executeUpdate(sql);
		System.out.println("Table MUTATIONS Droped.");
	}
	
	public void Insert(Mutation mutation) throws SQLException {
			System.out.println("Insert Mutation in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"MUTATIONS(Name,Var1,Var2,Var3,Obs,SpeciesId) "
					+"values('"+mutation.getName()+"','"+mutation.getVar1()+"','"
					+mutation.getVar2()+"','"+mutation.getVar3()
					+"','"+mutation.getObs()
					+"','"+mutation.getSpecie().getId()+"')";
			int i = stmt.executeUpdate(sql);
			CloseConnection(con, stmt,null, null);
			System.out.println(i+" Mutation Record inserted");
	}
	
	
	public ObservableList<Mutation> getAllMutations() throws SQLException {
		System.out.println("Getting all Mutations...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Mutation> mutations = FXCollections.observableArrayList();
		while (rs.next()) {
			System.out.println("Get Mutations: " + rs.getInt(1));
			Mutation m = new Mutation();
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
			mutations.add(m);
		}
		CloseConnection(con, stmt,null, rs);
		return mutations;
	}
	
	
	public ObservableList<Mutation> getMutationsBySpecie(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS WHERE SpeciesId="+id+";";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Mutation> mutations = FXCollections.observableArrayList();
		while (rs.next()) {
			Mutation m = new Mutation();
			System.out.println("Get Mutation: "+rs.getInt(1));
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
			mutations.add(m);
		}
		CloseConnection(con, stmt,null, rs);
		return mutations;
	}
	
	public Mutation getMutationsById(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS WHERE id="+id+";";
		ResultSet rs = stmt.executeQuery(sql);
		Mutation m = new Mutation();
		while (rs.next()) {
			System.out.println("Get Mutation: "+rs.getInt(1));
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
		}
		CloseConnection(con, stmt,null, rs);
		return m;
	}
	
	public void deleteMutation(Mutation mutation) throws SQLException {
		try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
		         PreparedStatement pstmt = con.prepareStatement("DELETE FROM MUTATIONS WHERE id=?")){
			    pstmt.setInt(1, mutation.getId());
			    int rowsDeleted = pstmt.executeUpdate();
			    if (rowsDeleted == 0) {
		            throw new SQLException("Failed to delete mutation, no rows affected.");
			    }else {
			    	 System.out.println("Mutation with id " + mutation.getId() + " deleted!");
			    }
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public Mutation getMutationWhereString(String col, String value) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS WHERE " + col + "='" + value + "';";
		ResultSet rs = stmt.executeQuery(sql);
		Mutation m = new Mutation();
		if (rs.next()) {
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
			CloseConnection(con, stmt,null, rs);
			return m;
		} else {
			CloseConnection(con, stmt,null, rs);
			return null;
		}
	}
	
	
	public boolean mutationExistsForSpecie(String mutationName, String specieId) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    Statement stmt = con.createStatement();
	    String sql = "SELECT COUNT(*) FROM MUTATIONS WHERE Name='" + mutationName + "' AND SpeciesId=" + specieId;
	    ResultSet rs = stmt.executeQuery(sql);
	    rs.next();
	    int count = rs.getInt(1);
	    CloseConnection(con, stmt, null, rs);
	    return count > 0;
	}
	
	public boolean mutationExistsForSpecie(String mutationName, String specieId, int excludeId) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    Statement stmt = con.createStatement();
	    String sql = "SELECT COUNT(*) FROM MUTATIONS WHERE Name='" + mutationName + "' AND SpeciesId=" + specieId + " AND id<>" + excludeId;
	    ResultSet rs = stmt.executeQuery(sql);
	    rs.next();
	    int count = rs.getInt(1);
	    CloseConnection(con, stmt, null, rs);
	    return count > 0;
	}

	
	public void updateMutation(Mutation m) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    PreparedStatement pstmt = null;
	    try {
	        String sql = "UPDATE MUTATIONS SET Name=?, Var1=?, Var2=?, Var3=?, Obs=?, SpeciesId=? WHERE id=?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, m.getName());
	        pstmt.setString(2, m.getVar1());
	        pstmt.setString(3, m.getVar2());
	        pstmt.setString(4, m.getVar3());
	        pstmt.setString(5, m.getObs());
	        pstmt.setInt(6, m.getSpecie().getId());
	        pstmt.setInt(7, m.getId());
	        int rowsUpdated = pstmt.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("Mutation updated successfully.");
	        } else {
	            System.out.println("Mutation not found.");
	        }
	    } finally {
	        CloseConnection(con, pstmt, null, null);
	    }
	}


}
