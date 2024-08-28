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
import domains.Bird;

public class AdoptiveParentsRepository {
	
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
	
	public void createTableAdoptiveParents(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Creating Table ADOPTIVE_PARENTS ...");
	        String sql = "CREATE TABLE IF NOT EXISTS ADOPTIVE_PARENTS"
	                +" (id INTEGER auto_increment, "
	                +"AdoptiveParentId INTEGER NOT NULL, "
	                +"BroodId INTEGER NOT NULL, "
	                +"FOREIGN KEY (AdoptiveParentId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
	                +"FOREIGN KEY (BroodId) REFERENCES BROOD(id) ON DELETE CASCADE, "
	                +"PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table ADOPTIVE_PARENTS Created.");
	}
	
	public void dropTableAdoptiveParents(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Trying to drop ADOPTIVE_PARENTS table...");
	        String sql = "DROP TABLE IF EXISTS ADOPTIVE_PARENTS CASCADE"; 
	        stmt.executeUpdate(sql);
	        System.out.println("Table ADOPTIVE_PARENTS dropped.");
	}

	public void insert(Integer broodId,List<Bird> adoptiveParents) throws SQLException {
		System.out.println("Insert ADOPTIVE_PARENTS in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO ADOPTIVE_PARENTS (AdoptiveParentId, BroodId) VALUES (?, ?)");
		for (Bird b : adoptiveParents) {
			 pstmt.setInt(1, b.getId());
			 pstmt.setInt(2, broodId);
			 pstmt.executeUpdate();
			 System.out.println("ADOPTIVE_PARENTS inserted with success!");
		}
		CloseConnection(con, null, pstmt, null);
	}

	 public List<Bird> getAdoptiveParentsForBrood(int broodId) {
	       	List<Bird> birds = new ArrayList<>();
	        try {
	            Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,MyValues.USER, MyValues.PASSWORD);
	            String sql = "SELECT * FROM ADOPTIVE_PARENTS WHERE BroodId = ?";
	            PreparedStatement  pstmt = con.prepareStatement(sql);
	            pstmt.setInt(1, broodId);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	Bird b = birdsRepository.getBirdWhereInt("id", rs.getInt("id"));
	                birds.add(b);
	            }
	            CloseConnection(con, null, pstmt, rs);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return birds;
	    }
}
