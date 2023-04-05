package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Specie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SpeciesRepository {
	
	private void CloseConnection(Connection con, Statement stmt, ResultSet rs) throws SQLException {
		if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
	}
	
	public void CreateTableSpecies(Connection con, Statement stmt) throws SQLException {
		System.out.println("Creating Table SPECIES ...");
		String sql = "CREATE TABLE IF NOT EXISTS SPECIES" 
					+ " (id INTEGER auto_increment, "
					+ "CommonName VARCHAR(255) NOT NULL, " 
					+ "ScientificName VARCHAR(255) NOT NULL UNIQUE, "
					+ "IncubationDays INTEGER NOT NULL, " 
					+ "BandingDays INTEGER NOT NULL, "
					+ "OutOfCageDays INTEGER NOT NULL, " 
					+ "MaturityDays INTEGER NOT NULL, " 
					+ "BandSize INTEGER NOT NULL, "
					+ "PRIMARY KEY (id))";
		stmt.executeUpdate(sql);
		System.out.println("Table SPECIES Created.");
	}
	
	public void DropTableSpecies(Connection con, Statement stmt) throws SQLException {
		System.out.println("Trying to drop BIRDS table...");
		String sql = "DROP TABLE IF EXISTS SPECIES CASCADE";
		stmt.executeUpdate(sql);
		System.out.println("Table SPECIES Droped.");
	}
	
	public void Insert(Specie specie) throws SQLException {
			System.out.println("Insert Specie in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"SPECIES(CommonName,ScientificName,IncubationDays,BandingDays,OutOfCageDays,MaturityDays,BandSize) "
					+"values('"+specie.getCommonName()+"','"+specie.getScientificName()+"','"
					+specie.getIncubationDays()+"','"+specie.getDaysToBand()+"','"+specie.getOutofCageAfterDays()
					+"','"+specie.getMaturityAfterDays()+"','"+specie.getBandSize()+"')";
			int i = stmt.executeUpdate(sql);
			CloseConnection(con, stmt, null);
			System.out.println(i+" Specie Record inserted");
	}
	
	public Specie getSpecieById(Integer id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM SPECIES WHERE SPECIES.id='" + id + "'";
		ResultSet rs = stmt.executeQuery(sql);
		Specie s = new Specie();
		while (rs.next()) {
			s.setId(rs.getInt(1));
			s.setCommonName(rs.getString(2));
			s.setScientificName(rs.getString(3));
			s.setIncubationDays(rs.getInt(4));
			s.setDaysToBand(rs.getInt(5));
			s.setOutofCageAfterDays(rs.getInt(6));
			s.setMaturityAfterDays(rs.getInt(7));
			s.setBandSize(rs.getInt(8));
		}
		CloseConnection(con, stmt, rs);
		return s;
	}
	
	public boolean checkSpecieByScientificName(String scientificName) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM SPECIES WHERE SPECIES.ScientificName='"+scientificName+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			boolean result = rs.next();
			CloseConnection(con, stmt, rs);
			return result;
		}
	
	public boolean checkIfExistsString(String col,String value, int idToExclude) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM SPECIES WHERE "+col+"='"+value+"' AND ID <> "+idToExclude;
		ResultSet rs  = stmt.executeQuery(sql);
		boolean result = rs.next();
		CloseConnection(con, stmt, rs);
		return result;
	}
	
		public ObservableList<Specie> getAllSpecies() throws SQLException {
			System.out.println("Getting all Species...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Species";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Specie> species = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Specie: " + rs.getInt(1));
				Specie s = new Specie();
				s.setId(rs.getInt(1));
				s.setCommonName(rs.getString(2));
				s.setScientificName(rs.getString(3));
				s.setIncubationDays(rs.getInt(4));
				s.setDaysToBand(rs.getInt(5));
				s.setOutofCageAfterDays(rs.getInt(6));
				s.setMaturityAfterDays(rs.getInt(7));
				s.setBandSize(rs.getInt(8));
				species.add(s);
			}
			CloseConnection(con, stmt, rs);
			return species;
		}
		
		public void deleteSpecie(Specie specie) throws SQLException {
			try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
			         PreparedStatement pstmt = con.prepareStatement("DELETE FROM SPECIES WHERE id=?")){
				    pstmt.setInt(1, specie.getId());
				    int rowsDeleted = pstmt.executeUpdate();
				    if (rowsDeleted == 0) {
			            throw new SQLException("Failed to delete specie, no rows affected.");
				    }else {
				    	 System.out.println("Specie with id " + specie.getId() + " deleted!");
				    }
			} catch (SQLException e) {
				throw e;
			}
		}
		
		public Specie getSpecieFromString(String str) throws SQLException {
			try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
					PreparedStatement pstmt = con
							.prepareStatement("SELECT * FROM SPECIES WHERE CommonName = ? OR ScientificName = ?")) {
				pstmt.setString(1, str);
				pstmt.setString(2, str);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					Specie specie = new Specie(rs.getInt("id"), rs.getString("CommonName"),
							rs.getString("ScientificName"), rs.getInt("IncubationDays"), rs.getInt("BandingDays"),
							rs.getInt("OutOfCageDays"), rs.getInt("MaturityDays"), rs.getInt("BandSize"));
					return specie;
				} else {
					return null;
				}
			} catch (Exception e) {
				throw e;
			}
		}

		public void updateSpecie(Specie specie) throws SQLException {
			try (Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
					PreparedStatement stmt = con.prepareStatement(
							"UPDATE SPECIES SET CommonName = ?, ScientificName = ?, IncubationDays = ?, BandingDays = ?, OutOfCageDays = ?, MaturityDays = ?, BandSize = ? WHERE id = ?");) {
				stmt.setString(1, specie.getCommonName());
				stmt.setString(2, specie.getScientificName());
				stmt.setInt(3, specie.getIncubationDays());
				stmt.setInt(4, specie.getDaysToBand());
				stmt.setInt(5, specie.getOutofCageAfterDays());
				stmt.setInt(6, specie.getMaturityAfterDays());
				stmt.setInt(7, specie.getBandSize());
				stmt.setInt(8, specie.getId());
				stmt.executeUpdate();
				System.out.println("Specie with id " + specie.getId() + " updated successfully!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
