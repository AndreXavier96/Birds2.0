package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import constants.MyValues;
import domains.Breeder;
import domains.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BreederRepository {
	
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	private ClubRepository clubRepository = new ClubRepository();
	private BreederFederationRepository breederFederationRepository = new BreederFederationRepository();
	

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
	
	public void CreateTableBreeder(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table BREEDER ...");
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Cellphone INTEGER, "
					+"Email VARCHAR(255), "
					+"Address VARCHAR(255), "
					+"Locale VARCHAR(255), "
					+"District VARCHAR(255), "
					+"PRIMARY KEY (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER Created.");
	}
	
	public void dropTableBreeder(Connection con, Statement stmt) throws SQLException {
			System.out.println("Trying to drop BREEDER table...");
			String sql = "DROP TABLE IF EXISTS BREEDER CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER Droped.");
	}
	
	public ObservableList<Breeder> getAllBreeders() throws SQLException {
		System.out.println("Getting all Breeders...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM BREEDER";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Breeder> breeders = FXCollections.observableArrayList();
		while(rs.next()) {
			System.out.println("Get Breeder: "+rs.getInt(1));
			Breeder b = new Breeder();
			b.setId(rs.getInt(1));
			b.setName(rs.getString(2));
			b.setCellphone(rs.getInt(3));
			b.setEmail(rs.getString(4));
			b.setAddress(rs.getString(5));
			b.setLocale(rs.getString(6));
			b.setDistrict(rs.getString(7));
			b.setClub(clubRepository.getAllClubsByClubIds(breederClubRepository.getClubsFromBreederId(b.getId())));
			breeders.add(b);
		}
		CloseConnection(con, stmt, null, rs);
		return breeders;
	}
	
	public void Insert(Breeder b) throws SQLException {
		int breederId = -1;
		System.out.println("Insert Breeder in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
				MyValues.PASSWORD);
		String sql = "INSERT INTO "
				+ "BREEDER(Name,Cellphone,Email,Address,Locale,District) "
				+ "values(?,?,?,?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, b.getName());
		stmt.setInt(2, b.getCellphone());
		if (b.getEmail() == null)
			stmt.setString(3, null);
		else
			stmt.setString(3, b.getEmail());
		if (b.getAddress() == null)
			stmt.setString(4, null);
		else
			stmt.setString(4, b.getAddress());
		if (b.getLocale() == null)
			stmt.setString(5, null);
		else
			stmt.setString(5, b.getLocale());
		if (b.getDistrict() == null)
			stmt.setString(6, null);
		else
			stmt.setString(6, b.getDistrict());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next())
			breederId = rs.getInt(1);
		CloseConnection(con, null, stmt, rs);
		System.out.println("[" + breederId + "] inserted: " + sql);
		for (Club c : b.getClub())
			breederClubRepository.Insert(breederId, c.getId());
		for (Map.Entry<Integer, String> entry : b.getStam().entrySet())
			breederFederationRepository.insert(breederId, entry.getKey(), entry.getValue());
	}

	public boolean checkIfPhoneExists(int phone) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT Cellphone FROM BREEDER WHERE Cellphone="+phone+";";
			ResultSet rs = stmt.executeQuery(sql);
			boolean result = rs.next();
			CloseConnection(con, stmt,null, rs);
			return result;
	}
	
	public boolean checkIfEmailExists(String email) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE Email='"+email+"';";
			ResultSet rs = stmt.executeQuery(sql);
			boolean result = rs.next();
			CloseConnection(con, stmt,null, rs);
			return result;
	}
	
	
	public boolean checkIfExistsString(String col,String value, int idToExclude) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM BREEDER WHERE " + col + "='" + value + "' AND ID <> " + idToExclude;
		ResultSet rs = stmt.executeQuery(sql);
		boolean result = rs.next();
		CloseConnection(con, stmt,null, rs);
		return result;	
}
	
	public Breeder getBreederbyId(int id) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE id="+id+";";
			ResultSet rs = stmt.executeQuery(sql);
			Breeder b = new Breeder();
			while(rs.next()) {
				System.out.println("Get Breeder: "+rs.getInt(1));
				b.setId(rs.getInt(1));
				b.setName(rs.getString(2));
				b.setCellphone(rs.getInt(3));
				b.setEmail(rs.getString(4));
				b.setAddress(rs.getString(5));
				b.setLocale(rs.getString(6));
				b.setDistrict(rs.getString(7));
			}
			CloseConnection(con, stmt, null, rs);
			return b;
	}
	
	public void deleteBreeder(Breeder breeder) throws SQLException {
		try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
		         PreparedStatement pstmt = con.prepareStatement("DELETE FROM BREEDER WHERE id=?")){
			    pstmt.setInt(1, breeder.getId());
			    int rowsDeleted = pstmt.executeUpdate();
			    if (rowsDeleted == 0) {
		            throw new SQLException("Failed to delete breeder, no rows affected.");
			    }else {
			    	 System.out.println("Breeder with id " + breeder.getId() + " deleted!");
			    }
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public Breeder getBreederbyString(String col, String value) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
	    Statement stmt = con.createStatement();
	    String sql = "SELECT * FROM BREEDER WHERE "+col+"='"+value+"';";
	    ResultSet rs = stmt.executeQuery(sql);
	    Breeder b = new Breeder();
	    if (rs.next()) {
	        b.setId(rs.getInt(1));
	        b.setName(rs.getString(2));
	        b.setCellphone(rs.getInt(3));
	        b.setEmail(rs.getString(4));
	        b.setAddress(rs.getString(5));
	        b.setLocale(rs.getString(6));
	        b.setDistrict(rs.getString(7));
	        CloseConnection(con, stmt,null, rs);
	        return b;
	    } else {
	        CloseConnection(con, stmt,null, rs);
	        return null;
	    }
	}

	public void updateBreeder(Breeder breeder) {
	    try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	            PreparedStatement pstmt = con.prepareStatement("UPDATE BREEDER SET Name = ?, Cellphone = ?, Email = ?, Address = ?, Locale = ?, District = ? WHERE id = ?");) {
	        pstmt.setString(1, breeder.getName());
	        pstmt.setInt(2, breeder.getCellphone());
	        pstmt.setString(3, breeder.getEmail());
	        pstmt.setString(4, breeder.getAddress());
	        pstmt.setString(5, breeder.getLocale());
	        pstmt.setString(6, breeder.getDistrict());
	        pstmt.setInt(7, breeder.getId());
	        int rowsUpdated = pstmt.executeUpdate();
	        if (rowsUpdated > 0) {
	            breederClubRepository.deleteAllByBreederId(breeder.getId());
	            breederFederationRepository.deleteAllByBreederId(breeder.getId());
	            for (Club c : breeder.getClub())
	    			breederClubRepository.Insert(breeder.getId(), c.getId());
	    		for (Map.Entry<Integer, String> entry : breeder.getStam().entrySet())
	    			breederFederationRepository.insert(breeder.getId(), entry.getKey(), entry.getValue());
	    		System.out.println("Breeder updated successfully.");
	        } else {
	            System.out.println("Breeder not found.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
