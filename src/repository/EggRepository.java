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
	
	private BirdsRepository birdsRepository = new BirdsRepository();
	
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
	                e.setBird(birdsRepository.getBirdWhereInt("id",rs.getInt("BirdId") ));
	                eggs.add(e);
	            }
	            CloseConnection(con, null, pstmt, rs);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return eggs;
	    }

	public Egg getEggById(int id) throws SQLException {
		Egg e = new Egg();
		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM EGG WHERE ID = " + id);
		if (rs.next()) {
			e.setId(rs.getInt("id"));
			e.setPostureDate(new java.util.Date(rs.getDate("PostureDate").getTime()));
			java.sql.Date verifiedFertilityDate = rs.getDate("VerifiedFertilityDate");
	        if (verifiedFertilityDate != null) {
	            e.setVerifiedFertilityDate(new java.util.Date(verifiedFertilityDate.getTime()));
	        }
	        java.sql.Date outbreakDate = rs.getDate("OutbreakDate");
	        if (outbreakDate != null) {
	            e.setOutbreakDate(new java.util.Date(outbreakDate.getTime()));
	        }
	        e.setType(rs.getString("Type"));
            e.setStatute(rs.getString("Statute"));
            e.setBird(null);//TODO
		}
		CloseConnection(con, stmt, null, rs);
		return e;
	}

	public void deleteEgg(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
	    PreparedStatement pstmt = con.prepareStatement("DELETE FROM EGG WHERE id = ?");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        CloseConnection(con, null, pstmt, null);
        System.out.println("egg deleted");
	}

	public void updateEgg(Egg egg) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement pstmt = con.prepareStatement("UPDATE EGG SET PostureDate=?, OutbreakDate=?, Type=?, Statute=? WHERE id=?");
		pstmt.setDate(1, new java.sql.Date(egg.getPostureDate().getTime()));
		if (egg.getOutbreakDate() != null)
			pstmt.setDate(2, new java.sql.Date(egg.getOutbreakDate().getTime()));
		else
			pstmt.setNull(2, java.sql.Types.DATE);
		pstmt.setString(3, egg.getType());
		pstmt.setString(4, egg.getStatute());
		pstmt.setInt(5, egg.getId());
		int rowsAffected = pstmt.executeUpdate();
		CloseConnection(con, null, pstmt, null);
		System.out.println(rowsAffected + " egg row updated.");
	}
	
	public void updateEggBird(int birdId,int eggId) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement pstmt = con.prepareStatement("UPDATE EGG SET BirdId=? WHERE id=?");
		pstmt.setInt(1, birdId);
		pstmt.setInt(2, eggId);
		int rowsAffected = pstmt.executeUpdate();
		CloseConnection(con, null, pstmt, null);
		System.out.println(rowsAffected + " egg row updated.");
	}

}
