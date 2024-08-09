package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constants.MyValues;
import domains.Bird;
import domains.BirdTreatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BirdTreatmentRepository {
	
	BirdsRepository birdsRepository = new BirdsRepository();
	TreatmentRepository treatmentRepository = new TreatmentRepository();

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
	
	public void createTableBirdTreatment(Connection con, Statement stmt) throws SQLException {
			System.out.println("Creating Table BIRD_TREATMENT ...");
			String sql = "CREATE TABLE IF NOT EXISTS BIRD_TREATMENT"
					+" (id INTEGER auto_increment, "
					+"BirdId INTEGER NOT NULL, "
					+"TreatmentId INTEGER NOT NULL, "
					+"Start DATE NOT NULL, "
					+"Finish DATE NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (BirdId) REFERENCES BIRDS(id) ON DELETE CASCADE, "
					+"FOREIGN KEY (TreatmentId) REFERENCES TREATMENT(id) ON DELETE CASCADE)";
			stmt.executeUpdate(sql);
			System.out.println("Table BIRD_TREATMENT Created.");
	}
	
	public void dropTableBirdTreatment(Connection con, Statement stmt) throws SQLException {
			String sql = "DROP TABLE IF EXISTS BIRD_TREATMENT";
			stmt.executeUpdate(sql);
			System.out.println("Table BIRD_TREATMENT dropped.");
	}
	
	public void insert(BirdTreatment bt) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO BIRD_TREATMENT (BirdId, TreatmentId, Start, Finish) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, bt.getBird().getId());
			pstmt.setInt(2, bt.getTreatment().getId());
			pstmt.setDate(3, new java.sql.Date(bt.getStart().getTime()));
			pstmt.setDate(4, new java.sql.Date(bt.getFinish().getTime()));
			pstmt.executeUpdate();
			CloseConnection(con, null, pstmt, null);
			System.out.println("Passaro["+bt.getBird().getBand()+"] tratamento: "+bt.getTreatment().getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<BirdTreatment> getBirdTreatmentByFinishDate(Date start,Date finish) {
		List<BirdTreatment> birdTreatments = new ArrayList<>();
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
					MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM BIRD_TREATMENT WHERE ? >= Start AND ? <= Finish");
			pstmt.setDate(1, new java.sql.Date(finish.getTime())); // Set the date parameter
			pstmt.setDate(2, new java.sql.Date(start.getTime())); // Set the date parameter again for the second placeholder

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				BirdTreatment bt = new BirdTreatment();
				bt.setBird(birdsRepository.getBirdWhereInt("id", rs.getInt("BirdId")));
				bt.setTreatment(treatmentRepository.getTreatmentById(rs.getInt("TreatmentId")));
				bt.setStart(dateFormat.parse(rs.getString("Start")));
				bt.setFinish(dateFormat.parse(rs.getString("Finish")));
				birdTreatments.add(bt);
			}
			CloseConnection(con, null, pstmt, rs);
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		return birdTreatments;
	}
	
	public ObservableList<BirdTreatment> getBirdTreatmentByBird(Bird b) {
		ObservableList<BirdTreatment> birdTreatments = FXCollections.observableArrayList();
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
					MyValues.PASSWORD);
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM BIRD_TREATMENT WHERE BirdId=?");
			pstmt.setInt(1, b.getId()); // Set the date parameter
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				BirdTreatment bt = new BirdTreatment();
				bt.setBird(birdsRepository.getBirdWhereInt("id", rs.getInt("BirdId")));
				bt.setTreatment(treatmentRepository.getTreatmentById(rs.getInt("TreatmentId")));
				bt.setStart(dateFormat.parse(rs.getString("Start")));
				bt.setFinish(dateFormat.parse(rs.getString("Finish")));
				birdTreatments.add(bt);
			}
			CloseConnection(con, null, pstmt, rs);
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		return birdTreatments;
	}
	
	public void getAllBirdTreatments() {
        String sql = "SELECT * FROM BIRD_TREATMENT";
        try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
                MyValues.PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Treatment: "+treatmentRepository.getTreatmentById(rs.getInt("TreatmentId")).getName()
                		+" Bird: "+birdsRepository.getBirdWhereInt("id", rs.getInt("BirdId")).getBand()
                		+" Start: "+rs.getString("Start")+" Finish: "+rs.getString("Finish"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
