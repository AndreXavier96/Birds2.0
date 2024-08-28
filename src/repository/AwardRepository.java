package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Award;
import domains.Bird;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AwardRepository {

	private ExibithionRepository exibithionRepository = new ExibithionRepository();
	private BirdsRepository birdsRepository = new BirdsRepository();
	
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
	
	public void createTableAward(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table AWARD ...");
			String sql = "CREATE TABLE IF NOT EXISTS AWARD"
					+" (id INTEGER auto_increment, "
					+"Pontuation INTEGER NOT NULL, "
					+"ExibithionId INTEGER NOT NULL, "
					+"ImagePath VARCHAR(500), "
					+"BirdId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BirdId) REFERENCES BIRDS (id) ON DELETE SET NULL, "
					+"FOREIGN KEY (ExibithionId) REFERENCES EXIBITHION (id) ON DELETE SET NULL)";
			stmt.executeUpdate(sql);
			System.out.println("Table AWARD Created.");
	}
	
	public void dropTableAward(Connection con, Statement stmt) throws SQLException {
			System.out.println("Trying to drop AWARD table...");
			String sql = "DROP TABLE IF EXISTS AWARD CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table AWARD Droped.");
	}
	
	public void Insert(Award a) throws SQLException {
		int awardId = -1;
		System.out.println("Insert AWARD in DataBase...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
				MyValues.PASSWORD);
		String sql = "INSERT INTO "
				+ "AWARD(Pontuation,ExibithionId,ImagePath,BirdId) "
				+ "values(?,?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setInt(1, a.getPontuation());
		stmt.setInt(2, a.getExibithion().getId());
		stmt.setString(3, a.getJudgmentImagePath());
		stmt.setInt(4, a.getBird().getId());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next())
			awardId = rs.getInt(1);
		CloseConnection(con, null, stmt, rs);
		System.out.println("[" + awardId + "] inserted: " + sql);
	}
	
	public ObservableList<Award> getAwardsByBird(Bird b) {
	    ObservableList<Award> awards = FXCollections.observableArrayList();
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM AWARD WHERE BirdId = ?");
	        pstmt.setInt(1, b.getId()); // Set the BirdId parameter
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Award award = new Award();
	            award.setId(rs.getInt("id"));
	            award.setPontuation(rs.getInt("Pontuation"));
	            award.setExibithion(exibithionRepository.getExibithionById(rs.getInt("ExibithionId")));
	            award.setJudgmentImagePath(rs.getString("ImagePath"));
	            award.setBird(b);
	            awards.add(award);
	        }
	        CloseConnection(con, null, pstmt, rs);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return awards;
	}
	
	public Award getAwardById(Integer id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
					MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM AWARD WHERE id = ?");
			pstmt.setInt(1, id); // Set the BirdId parameter
			ResultSet rs = pstmt.executeQuery();
			Award award = new Award();
			if (rs.next()) {
				award.setId(rs.getInt("id"));
				award.setPontuation(rs.getInt("Pontuation"));
				award.setExibithion(exibithionRepository.getExibithionById(rs.getInt("ExibithionId")));
				award.setJudgmentImagePath(rs.getString("ImagePath"));
				award.setBird(birdsRepository.getBirdWhereInt("id", rs.getInt("ExibithionId")));
			}
			CloseConnection(con, null, pstmt, rs);
			return award;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
//	 public Exibithion getExibithionByName(String name) throws SQLException {
//	        Connection con = DriverManager.getConnection("jdbc:h2:./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
//	        Statement stmt = con.createStatement();
//	        String sql = "SELECT * FROM EXIBITHION WHERE Name='" + name + "';";
//	        ResultSet rs = stmt.executeQuery(sql);
//	        Exibithion exibithion = null;
//	        
//	        if (rs.next()) {
//	            System.out.println("Get Exibithion: " + rs.getInt("id"));
//	            exibithion = new Exibithion();
//	            exibithion.setId(rs.getInt("id"));
//	            exibithion.setName(rs.getString("Name"));
//	            exibithion.setLocale(rs.getString("Locale"));
//	        }
//
//	        CloseConnection(con, stmt, null, rs);
//	        return exibithion;
//	    }
	
}
