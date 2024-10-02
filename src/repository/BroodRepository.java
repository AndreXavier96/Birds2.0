package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import constants.MyValues;
import domains.Brood;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BroodRepository {
	
	EggRepository eggRepository = new EggRepository();
	AdoptiveParentsRepository adoptiveParentsRepository = new AdoptiveParentsRepository();
	BirdsRepository birdsRepository = new BirdsRepository();
	CageRepository cageRepository = new CageRepository();
	
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
	
	public void createTableBrood(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Creating Table BROOD ...");
	        String sql = "CREATE TABLE IF NOT EXISTS BROOD"
	                +" (id INTEGER auto_increment, "
	                +"Start DATE NOT NULL, "
	                +"Finish DATE, "
	                +"FatherId INTEGER NOT NULL, "
	                +"MotherId INTEGER NOT NULL, "
	                +"CageId INTEGER, "
	                +"FOREIGN KEY (FatherId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
	                +"FOREIGN KEY (MotherId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
	                +"FOREIGN KEY (CageId) REFERENCES CAGE(id) ON DELETE CASCADE, "
	                +"PRIMARY KEY (id))";
	        stmt.executeUpdate(sql);
	        System.out.println("Table BROOD Created.");
	}
	
	public void dropTableBrood(Connection con, Statement stmt) throws SQLException {
	        System.out.println("Trying to drop BROOD table...");
	        String sql = "DROP TABLE IF EXISTS BROOD CASCADE"; 
	        stmt.executeUpdate(sql);
	        System.out.println("Table BROOD dropped.");
	}

	public void insert(Brood b) throws SQLException {
		int generatedId = -1;
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		System.out.println("Inserting Ninhanda in database...");
		String sql = "INSERT INTO BROOD (Start, Finish, FatherId, MotherId,CageId) VALUES (?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstmt.setDate(1, new java.sql.Date(b.getStart().getTime()));
		 if (b.getFinish() == null) {
		        pstmt.setNull(2, java.sql.Types.DATE);
		    } else {
		        pstmt.setDate(2, new java.sql.Date(b.getFinish().getTime()));
		    }
		pstmt.setInt(3, b.getFather().getId());
		pstmt.setInt(4, b.getMother().getId());
		pstmt.setInt(5, b.getCage().getId());
		int i = pstmt.executeUpdate();
		if (i == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getInt(1);
				eggRepository.insert(generatedId,b.getEggs());
				if (!b.getAdoptiveParents().isEmpty())
					adoptiveParentsRepository.insert(generatedId,b.getAdoptiveParents());
				System.out.println("Ninhanda "+generatedId+" inserted with success!");
			}
		}
		CloseConnection(con, null, pstmt, null);
	}
	
	public ObservableList<Brood> getAllBroods() {
	    try {
	        System.out.println("Getting all Broods...");
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
	                MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM BROOD";
	        ResultSet rs = stmt.executeQuery(sql);
	        ObservableList<Brood> broods = FXCollections.observableArrayList();
	        while (rs.next()) {
	            System.out.println("Get Brood: " + rs.getInt(1));
	            Brood b = new Brood();
	            b.setId(rs.getInt(1));
	            b.setStart(rs.getDate(2));
	            b.setFinish(rs.getDate(3));
	            b.setFather(birdsRepository.getBirdWhereInt("id", rs.getInt(4)));
	            b.setMother(birdsRepository.getBirdWhereInt("id", rs.getInt(5)));
	            b.setCage(cageRepository.getCage(rs.getInt(6)));
	            b.setEggs(eggRepository.getEggsForBrood(rs.getInt(1)));
	            b.setAdoptiveParents(adoptiveParentsRepository.getAdoptiveParentsForBrood(rs.getInt(1)));
	            broods.add(b);
	        }
	        CloseConnection(con, stmt, null, rs);
	        return broods;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	public ObservableList<Brood> getAllBroodsFromCouple(int fatherId, int motherId,Date selectedDate) {
	    try {
	        System.out.println("Getting all Broods from couple father["+fatherId+"] mother["+motherId+"]");
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM BROOD WHERE FatherId=? AND MotherId=? AND Start<=? AND (Finish IS NULL OR ? <= Finish)");
	        pstmt.setInt(fatherId, 1);
	        pstmt.setInt(motherId, 2);
	        java.sql.Date sqlSelectedDate = new java.sql.Date(selectedDate.getTime());
	        pstmt.setDate(3, sqlSelectedDate);
	        pstmt.setDate(4, sqlSelectedDate);
	        ResultSet rs = pstmt.executeQuery();
	        ObservableList<Brood> broods = FXCollections.observableArrayList();
	        while (rs.next()) {
	            System.out.println("Get Brood: " + rs.getInt(1));
	            Brood b = new Brood();
	            b.setId(rs.getInt(1));
	            b.setStart(rs.getDate(2));
	            b.setFinish(rs.getDate(3));
	            b.setFather(birdsRepository.getBirdWhereInt("id", rs.getInt(4)));
	            b.setMother(birdsRepository.getBirdWhereInt("id", rs.getInt(5)));
	            b.setCage(cageRepository.getCage(rs.getInt(6)));
	            b.setEggs(eggRepository.getEggsForBrood(rs.getInt(1)));
	            b.setAdoptiveParents(adoptiveParentsRepository.getAdoptiveParentsForBrood(rs.getInt(1)));
	            broods.add(b);
	        }
	        CloseConnection(con, null, pstmt, rs);
	        return broods;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public void updateBroodCage(int broodId, int newCageId) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    String sql = "UPDATE BROOD SET CageId = ? WHERE id = ?";
	    PreparedStatement pstmt = con.prepareStatement(sql);

	    pstmt.setInt(1, newCageId);
	    pstmt.setInt(2, broodId);

	    int rowsAffected = pstmt.executeUpdate();
	    System.out.println(rowsAffected + " row(s) updated.");

	    CloseConnection(con, null, pstmt, null);
	}
	
	public Brood getBroodById(int broodId) throws SQLException {
	    Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    String sql = "SELECT * FROM BROOD WHERE id = ?";
	    PreparedStatement pstmt = con.prepareStatement(sql);
	    pstmt.setInt(1, broodId);
	    ResultSet rs = pstmt.executeQuery();
	    Brood brood = null;
	    if (rs.next()) {
	        brood = new Brood();
	        brood.setId(rs.getInt("id"));
	        brood.setStart(rs.getDate("Start"));
	        brood.setFinish(rs.getDate("Finish"));
	        brood.setFather(birdsRepository.getBirdWhereInt("id", rs.getInt("FatherId")));
	        brood.setMother(birdsRepository.getBirdWhereInt("id", rs.getInt("MotherId")));
	        brood.setCage(cageRepository.getCage(rs.getInt("CageId")));
	        brood.setEggs(eggRepository.getEggsForBrood(rs.getInt("id")));
	        brood.setAdoptiveParents(adoptiveParentsRepository.getAdoptiveParentsForBrood(rs.getInt("id")));
	    }
	    CloseConnection(con, null, pstmt, rs);

	    return brood;
	}

	public void deleteBroodById(int broodId) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	    PreparedStatement pstmt = con.prepareStatement("DELETE FROM BROOD WHERE id = ?");
        pstmt.setInt(1, broodId);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Brood with id " + broodId + " deleted successfully.");
        } else {
            System.out.println("No brood found with id " + broodId + ".");
        }
	}

	public void updateFinishDateBrood(Integer id, Date date) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement pstmt = con.prepareStatement("UPDATE BROOD SET Finish = ? WHERE id=?");
	        pstmt.setDate(1, new java.sql.Date(date.getTime()));
	        pstmt.setInt(2, id);
	        pstmt.executeUpdate();
	        CloseConnection(con, null, pstmt, null);
	        System.out.println("Brood with id " + id + " updated,data fim para valor: "+date+" .");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
}
