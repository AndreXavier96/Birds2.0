package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClubRepository {
	
	private FederationRepository federationRepository;
	private BreederClubRepository breederClubRepository = new BreederClubRepository();
	
	public ClubRepository() {}
	public ClubRepository(FederationRepository federationRepository) {
	       this.federationRepository = federationRepository;
	   }
	
	private void CloseConnection(Connection con, Statement stmt, PreparedStatement pstmt, ResultSet rs) throws SQLException {
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

	public void createTableClub(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table CLUB ...");
			String sql = "CREATE TABLE IF NOT EXISTS CLUB"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL UNIQUE, "
					+"Acronym VARCHAR(255) NOT NULL UNIQUE, "
					+"Locale VARCHAR(255) NOT NULL, "
					+"Address VARCHAR(255), "
					+"Phone VARCHAR(255), "
					+"Email VARCHAR(255) NOT NULL UNIQUE, "
					+"FederationId Integer NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (FederationId) REFERENCES FEDERATION(id))";
			stmt.executeUpdate(sql);
			System.out.println("Table CLUB Created.");
	}
	
	public void dropTableClub(Connection con, Statement stmt) throws SQLException {
			System.out.println("Trying to drop CLUB table...");
			String sql = "DROP TABLE IF EXISTS CLUB CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table CLUB Droped.");
	}

	public void Insert(Club c) throws SQLException {
			System.out.println("Insert Club in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"CLUB(Name,Acronym,Locale,Address,Phone,Email,FederationId) "
					+"values('"+c.getName()+"','"+c.getAcronym()+"','"+c.getLocale()+"','"
					+c.getAddress()+"','"+c.getPhone()+"','"+c.getEmail()+"','"+c.getFederation().getId()+"')";
			int i = stmt.executeUpdate(sql);
			CloseConnection(con, stmt,null, null);
			System.out.println(i+"Club inserted: "+sql);
	}
	
	
	public ObservableList<Club> getAllClubs() throws SQLException {
		System.out.println("Getting all Clubs...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM CLUB";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Club> clubs = FXCollections.observableArrayList();
		while (rs.next()) {
			System.out.println("Get Clubs: " + rs.getString(3));
			Club c = new Club();
			c.setId(rs.getInt(1));
			c.setName(rs.getString(2));
			c.setAcronym(rs.getString(3));
			c.setLocale(rs.getString(4));
			c.setAddress(rs.getString(5));
			;
			c.setPhone(rs.getString(6));
			c.setEmail(rs.getString(7));
			c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
			clubs.add(c);
		}
		CloseConnection(con, stmt,null, rs);
		return clubs;
	}
	
	public Club getClubWhereString(String col, String value) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			Club c = new Club();
			if (rs.next()) {
				c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
				CloseConnection(con, stmt,null, rs);
				return c;
			}else {
				CloseConnection(con, stmt,null, rs);
				return null;
			}
	}
	
	public Club getClubByID(Integer id) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB WHERE id="+id+";";
			ResultSet rs = stmt.executeQuery(sql);
			Club c = new Club();
			if (rs.next()) {
				c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
				CloseConnection(con, stmt,null, rs);
				return c;
			}else {
				CloseConnection(con, stmt,null, rs);
				return null;
			}
				
	}
	
	public boolean checkIfExistsString(String col,String value) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM CLUB WHERE "+col+"='"+value+"';";
			ResultSet rs = stmt.executeQuery(sql);
			boolean result = rs.next();
			CloseConnection(con, stmt,null, rs);
			return result;	
	}
	
	public ObservableList<Club> getAvailableClubs(Integer breederId) throws SQLException {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM CLUB WHERE id NOT IN (SELECT ClubId FROM BREEDER_CLUB WHERE BreederId = " + breederId + ")";
	        ResultSet rs = stmt.executeQuery(sql);
	        ObservableList<Club> availableClubs = FXCollections.observableArrayList();
	        while (rs.next()) {
	            Club c = new Club();
	            c.setId(rs.getInt(1));
				c.setName(rs.getString(2));
				c.setAcronym(rs.getString(3));
				c.setLocale(rs.getString(4));
				c.setAddress(rs.getString(5));;
				c.setPhone(rs.getString(6));
				c.setEmail(rs.getString(7));
				c.setFederation(federationRepository.getFederationWhereInt("id", rs.getInt(8)));
	            availableClubs.add(c);
	        }
	        CloseConnection(con, stmt,null, rs);
	        return availableClubs;
	}
	
	public ObservableList<Club> getAllClubsByClubIds(ObservableList<Integer> clubIds) throws SQLException{
		 ObservableList<Club> allClubs = FXCollections.observableArrayList();
		 for (Integer clubId : clubIds) {
		        Club club = getClubWhereString("id",Integer.toString(clubId));
		        allClubs.add(club);
		    }
		return allClubs;
	}
	
	public void deleteClub(Club club) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    try {
	        con.setAutoCommit(false);

	        // Delete all rows in the BREEDER_CLUB table that reference the club being deleted
	        breederClubRepository.deleteBreederClubByClubId(con,club.getId());

	        // Delete the club
	        PreparedStatement pstmt = con.prepareStatement("DELETE FROM CLUB WHERE id=?");
	        pstmt.setInt(1, club.getId());
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted == 0) {
	            throw new SQLException("Failed to delete club, no rows affected.");
	        } else {
	            System.out.println("Club " + club.getAcronym() + " deleted!");
	        }

	        con.commit();
	    } catch (SQLException e) {
	        con.rollback();
	        throw e;
	    } finally {
	        CloseConnection(con, null, null, null);
	    }
	}
	
	public void deleteClubsByFederationId(Connection con,int federationId) throws SQLException {
        String query = "SELECT COUNT(*) FROM CLUB WHERE FederationId = ?";
        PreparedStatement countStatement = con.prepareStatement(query);
        countStatement.setInt(1, federationId);
        boolean hasRows = countStatement.executeQuery().next();
        CloseConnection(null, null, countStatement, null);
        if (hasRows) {
            String deleteQuery = "DELETE FROM CLUB WHERE FederationId = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, federationId);
            deleteStatement.executeUpdate();
            CloseConnection(null, null, deleteStatement, null);
        }
    }
	
}
