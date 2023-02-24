package repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import constants.MyValues;
import domains.State;

public class StateRepository {
	public void CreateTableState() {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	        System.out.println("Creating Table STATE ...");
	        Statement stmt = con.createStatement();
	        String sql = "CREATE TABLE IF NOT EXISTS STATE"
	                +" (id INTEGER auto_increment, "
	                +"Type VARCHAR(255) NOT NULL, "
	                +"Date DATE, "
	                +"Valor DOUBLE, "
	                +"Motivo VARCHAR(255), "
	                +"PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table STATE Created.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void dropTableState() {
	    try {
	        System.out.println("Trying to drop STATE table...");
	        Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "DROP TABLE IF EXISTS STATE CASCADE"; 
	        stmt.executeUpdate(sql);
	        System.out.println("Table STATE dropped.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public State insertVivo() {
		State state = new State();
		int generatedId = -1;
		try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        System.out.println("Inserting VIVO state in database...");
	        PreparedStatement ps = null;
            String sql = "INSERT INTO STATE (Type) VALUES (?)";
            ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, MyValues.VIVO);
	        int i = ps.executeUpdate();
	        if (i == 1) {
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                generatedId = rs.getInt(1);
	                state = getStateById(generatedId);
	            }
	        }
	        ps.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return state;
	}
	
	public State insertState(State state) {
		State returnState = new State();
	 int generatedId = -1;
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        System.out.println("Inserting "+state.getType()+" state in database...");
	        PreparedStatement ps = null;
            String sql = "INSERT INTO STATE (Type,Date,Valor,Motivo) VALUES (?, ?, ?,?)";
            ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, MyValues.VIVO);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = formatter.format(state.getDate());
	        ps.setString(2, formattedDate);
            Double valor= (double) 0;
            if (state.getValor()!=null)
				valor = state.getValor();
            ps.setDouble(3, valor);
            
            String motivo="";
            if (state.getMotivo()!=null) {
				motivo=state.getMotivo();
			}
            ps.setString(4, motivo);
	        int i = ps.executeUpdate();
	        if (i == 1) {
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                generatedId = rs.getInt(1);
	                returnState = getStateById(generatedId);
	            }
	        }
	        ps.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return returnState;
	}

	

	public State getStateById(int id) {
	    State state = new State();
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM STATE WHERE ID = " + id);
	        if (rs.next()) {
	        	state.setId(rs.getInt(1));
	        	state.setType(rs.getString(2));
	        	state.setDate(rs.getDate(3));
	        	state.setValor(rs.getDouble(4));
	        	state.setMotivo(rs.getString(5));
	        }
	        rs.close();
	        stmt.close();
	        con.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return state;
	}


	public void deleteState(int id) {
		 try {
		        Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
		        PreparedStatement stmt = con.prepareStatement("DELETE FROM STATE WHERE ID = ?");
		        stmt.setInt(1, id);
		        stmt.executeUpdate();
		        stmt.close();
		        con.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	
	public void updateState(State state) {
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	         PreparedStatement stmt = con.prepareStatement("UPDATE STATE SET Type=?, Date=?, Valor=?, Motivo=? WHERE id=?")) {
	        stmt.setString(1, state.getType());
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        String formattedDate = formatter.format(state.getDate());
	        stmt.setString(2, formattedDate);
	        if (state.getValor()==null)
				state.setValor((double) 0);
	        stmt.setDouble(3, state.getValor());
	        if (state.getMotivo()==null) 
	        	state.setMotivo("");
	        stmt.setString(4, state.getMotivo());
	        stmt.setInt(5, state.getId());
	        int rowsAffected = stmt.executeUpdate();
	        System.out.println(rowsAffected + " row(s) updated.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
}
