package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Bird;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BirdsRepository {
	
	private CageRepository cageRepository =  new CageRepository();
	private BreederRepository breederRepository = new BreederRepository();
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	private MutationsRepository mutationsRepository = new MutationsRepository();
	private StateRepository stateRepository = new StateRepository();
	
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
	
	public void CreateTableBird(Connection con,Statement stmt) throws SQLException {
			System.out.println("Creating Table Birds ...");
			String sql = "CREATE TABLE IF NOT EXISTS BIRDS"
					+" (id INTEGER auto_increment, "
					+"Band VARCHAR(255) NOT NULL, "
					+"BirthYear SMALLINT NOT NULL, "
					+"EntryDate DATE, "
					+"EntryType VARCHAR(255) NOT NULL, "
					+"BuyPrice DOUBLE, "
					+"StateId INTEGER NOT NULL, "
					+"Sex VARCHAR(255) NOT NULL, "
					+"Father INTEGER, "
					+"Mother INTEGER, "
					+"SpeciesId INTEGER NOT NULL, "
					+"MutationsId INTEGER, "
					+"CageId INTEGER,"
					+"BreederId INTEGER,"
					+"PostureId INTEGER,"
					+"ImagePath VARCHAR(500),"
					+"Obs VARCHAR(500),"
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (Father) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (Mother) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (SpeciesId) REFERENCES SPECIES (id), "
					+"FOREIGN KEY (MutationsId) REFERENCES MUTATIONS (id), "
					+"FOREIGN KEY (CageId) REFERENCES CAGE (id), "
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER (id), "
					+"FOREIGN KEY (PostureId) REFERENCES POSTURE (id), "
		            +"FOREIGN KEY (StateId) REFERENCES STATE (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table BIRDS Created.");
	}
	
	public void DropTableBird(Connection con,Statement stmt) throws SQLException {
			System.out.println("Trying to drop BIRDS table...");
			String sql = "DROP TABLE IF EXISTS BIRDS CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table BIRDS Droped.");
	}
	
	public ObservableList<Bird> getAllBirds() {
			try {
				System.out.println("Getting all Birds...");
				Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
						MyValues.USER, MyValues.PASSWORD);
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM BIRDS";
				ResultSet rs = stmt.executeQuery(sql);
				ObservableList<Bird> birds = FXCollections.observableArrayList();
				while (rs.next()) {
					System.out.println("Get Bird: " + rs.getInt(1));
					Bird b = new Bird();
					b.setId(rs.getInt(1));
					b.setBand(rs.getString(2));
					b.setYear(rs.getInt(3));
					b.setEntryDate(rs.getDate(4));
					b.setEntryType(rs.getString(5));
					b.setBuyPrice(rs.getDouble(6));
					b.setState(stateRepository.getStateById(rs.getInt(7)));
					b.setSex(rs.getString(8));
					if (rs.getInt(9)!=0)
						b.setFather(getBirdWhereInt("id",rs.getInt(9)));
					else
						b.setFather(null);
					if (rs.getInt(10)!=0)
						b.setMother(getBirdWhereInt("id",rs.getInt(10)));
					else
						b.setMother(null);
					b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
					b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
					b.setCage(cageRepository.getCage(rs.getInt(13)));
					b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
					b.setPosture(rs.getInt(15));
					b.setImage(rs.getString(16));
					b.setObs(rs.getString(17));
					birds.add(b);
				}
				CloseConnection(con, stmt, rs);
				return birds;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	
	public ObservableList<Bird> getAllWhere(String col,String value) {
		try {
			System.out.println("Getting all Birds...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE "+col+"='"+value+"'";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Bird> birds = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Bird: " + rs.getInt(1));
				Bird b = new Bird();
				b.setId(rs.getInt(1));
				b.setBand(rs.getString(2));
				b.setYear(rs.getInt(3));
				b.setEntryDate(rs.getDate(4));
				b.setEntryType(rs.getString(5));
				b.setBuyPrice(rs.getDouble(6));
				b.setState(stateRepository.getStateById(rs.getInt(7)));
				b.setSex(rs.getString(8));
				if (rs.getInt(9)!=0)
					b.setFather(getBirdWhereInt("id",rs.getInt(9)));
				else
					b.setFather(null);
				if (rs.getInt(10)!=0)
					b.setMother(getBirdWhereInt("id",rs.getInt(10)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
				b.setCage(cageRepository.getCage(rs.getInt(13)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
				b.setPosture(rs.getInt(15));
				b.setImage(rs.getString(16));
				b.setObs(rs.getString(17));
				birds.add(b);
			}
			CloseConnection(con, stmt, rs);
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Bird getBirdWhereInt(String col,Integer value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM BIRDS WHERE "+col+"=" + value + ";";
	        ResultSet rs = stmt.executeQuery(sql);
	        Bird b = new Bird();
	        if (rs.next()) {
	            b.setId(rs.getInt(1));
	            b.setBand(rs.getString(2));
	            b.setYear(rs.getInt(3));
	            b.setEntryDate(rs.getDate(4));
	            b.setEntryType(rs.getString(5));
	            b.setBuyPrice(rs.getDouble(6));
	            b.setState(stateRepository.getStateById(rs.getInt(7)));
	            b.setSex(rs.getString(8));
	            int fatherId = rs.getInt(9);
	            int motherId = rs.getInt(10);
	            if (fatherId != 0) {
	                b.setFather(getBirdWhereInt("id",fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBirdWhereInt("id",motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
	            b.setCage(cageRepository.getCage(rs.getInt(13)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
	            b.setPosture(rs.getInt(15));
	            b.setImage(rs.getString(16));
	            b.setObs(rs.getString(17));
	            CloseConnection(con, stmt, rs);
	            return b;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public Bird getBirdWhereString(String col, String value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM BIRDS WHERE "+col+"='" + value + "';";
	        ResultSet rs = stmt.executeQuery(sql);
	        Bird b = new Bird();
	        if (rs.next()) {
	            b.setId(rs.getInt(1));
	            b.setBand(rs.getString(2));
	            b.setYear(rs.getInt(3));
	            b.setEntryDate(rs.getDate(4));
	            b.setEntryType(rs.getString(5));
	            b.setBuyPrice(rs.getDouble(6));
	            b.setState(stateRepository.getStateById(rs.getInt(7)));
	            b.setSex(rs.getString(8));
	            int fatherId = rs.getInt(9);
	            int motherId = rs.getInt(10);
	            if (fatherId != 0) {
	                b.setFather(getBirdWhereInt("id",fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBirdWhereInt("id",motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
	            b.setCage(cageRepository.getCage(rs.getInt(13)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
	            b.setPosture(rs.getInt(15));
	            b.setImage(rs.getString(16));
	            b.setObs(rs.getString(17));
	            CloseConnection(con, stmt, rs);
	            return b;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public int Insert(Bird bird) {
		int id = -1;
		try {
			System.out.println("Insert Bird in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate(
					"INSERT INTO BIRDS (Band,BirthYear,EntryDate,EntryType,BuyPrice,StateId,Sex,Father,Mother,MutationsId,SpeciesId,CageId,BreederId,ImagePath,Obs) "
							+ "VALUES ('" + bird.getBand() + "','" + bird.getYear() + "','"
							+ new java.sql.Date(bird.getEntryDate().getTime()) + "','" + bird.getEntryType() + "','"
							+ bird.getBuyPrice() + "','" + bird.getState().getId() + "','"
							+ bird.getSex() + "',"
							+ (bird.getFather() == null ? "NULL" : "'" + bird.getFather().getId() + "'") + ","
							+ (bird.getMother() == null ? "NULL" : "'" + bird.getMother().getId() + "'") + ","
							+ (bird.getMutations() == null ? "NULL" : "'" + bird.getMutations().getId() + "'") + ",'"
							+ bird.getSpecies().getId() + "','" + bird.getCage().getId() + "','"
							+ bird.getBreeder().getId() + "','" + bird.getImage() +"','"+bird.getObs()+ "')",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			CloseConnection(con, stmt, rs);
			System.out.println("Bird Record inserted with id: " + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public void partialUpdateStringsBird(Integer id, String col, String value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement stmt = con.prepareStatement("UPDATE BIRDS SET "+col+" = ? WHERE id=?");
	        stmt.setString(1, value);
	        stmt.setInt(2, id);
	        stmt.executeUpdate();
	        CloseConnection(con, stmt, null);
	        System.out.println("Bird with id " + id + " updated, coluna "+col+" para valor: "+value+" .");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void partialUpdateIntBird(Integer id, String col, Integer value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement stmt = con.prepareStatement("UPDATE BIRDS SET "+col+" = ? WHERE id=?");
	        if (value==null)
	        	stmt.setString(1, null);
	        else
	        	stmt.setInt(1, value);
	        stmt.setInt(2, id);
	        stmt.executeUpdate();
	        CloseConnection(con, stmt, null);
	        System.out.println("Bird with id " + id + " updated, coluna "+col+" para valor: "+value+" .");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public Bird fullUpdateBird(Bird bird) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement stmt = con.prepareStatement("UPDATE BIRDS SET Band=?, BirthYear=?, EntryDate=?, EntryType=?, BuyPrice=?, SellPrice=?, StateId=?, Sex=?, Father=?, Mother=?, SpeciesId=?, MutationsId=?, CageId=?, BreederId=?, PostureId=?, ImagePath=?,Obs=? WHERE id=?");
	        stmt.setString(1, bird.getBand());
	        stmt.setInt(2, bird.getYear());
	        stmt.setDate(3, new java.sql.Date(bird.getEntryDate().getTime()));
	        stmt.setString(4, bird.getEntryType());
	        stmt.setDouble(5, bird.getBuyPrice());
	        stmt.setInt(6, bird.getState().getId());
	        stmt.setString(7, bird.getSex());
	        stmt.setInt(8, bird.getFather().getId());
	        stmt.setInt(9, bird.getMother().getId());
	        stmt.setInt(10, bird.getSpecies().getId());
	        stmt.setInt(11, bird.getMutations().getId());
	        stmt.setInt(12, bird.getCage().getId());
	        stmt.setInt(13, bird.getBreeder().getId());
	        stmt.setInt(14, bird.getPosture());
	        stmt.setString(15, bird.getImage());
	        stmt.setString(16, bird.getObs());
	        stmt.setInt(17, bird.getId());
	        stmt.executeUpdate();
	        CloseConnection(con, stmt, null);
	        System.out.println("Bird with id " + bird.getId() + " updated.");
	        return bird;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public ObservableList<Bird> getAllWhereIntOrWhereInt(String col1,Integer value1,String col2,Integer value2) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,
					MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE "+col1+"='" + value1 + "' OR "+col2+"='" + value2 + "';";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Bird> birds = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Bird: " + rs.getInt(1));
				Bird b = new Bird();
				b.setId(rs.getInt(1));
				b.setBand(rs.getString(2));
				b.setYear(rs.getInt(3));
				b.setEntryDate(rs.getDate(4));
				b.setEntryType(rs.getString(5));
				b.setBuyPrice(rs.getDouble(6));;
				b.setState(stateRepository.getStateById(rs.getInt(7)));
				b.setSex(rs.getString(8));
				if (rs.getInt(9) != 0)
					b.setFather(getBirdWhereInt("id",rs.getInt(9)));
				else
					b.setFather(null);
				if (rs.getInt(10) != 0)
					b.setMother(getBirdWhereInt("id",rs.getInt(10)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
				b.setCage(cageRepository.getCage(rs.getInt(13)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
				b.setPosture(rs.getInt(15));
				b.setImage(rs.getString(16));
				b.setObs(rs.getString(17));
				birds.add(b);
			}
			CloseConnection(con, stmt, rs);
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ObservableList<Bird> getAllWhereStringAndInteger(String col,String value,String col2,Integer value2) {
		try {
			System.out.println("Getting all Birds...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE "+col+"='"+value+"' AND "+col2+"="+value2+";";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Bird> birds = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Bird: " + rs.getInt(1));
				Bird b = new Bird();
				b.setId(rs.getInt(1));
				b.setBand(rs.getString(2));
				b.setYear(rs.getInt(3));
				b.setEntryDate(rs.getDate(4));
				b.setEntryType(rs.getString(5));
				b.setBuyPrice(rs.getDouble(6));
				b.setState(stateRepository.getStateById(rs.getInt(7)));
				b.setSex(rs.getString(8));
				if (rs.getInt(9)!=0)
					b.setFather(getBirdWhereInt("id",rs.getInt(9)));
				else
					b.setFather(null);
				if (rs.getInt(10)!=0)
					b.setMother(getBirdWhereInt("id",rs.getInt(10)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(11)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(12)));
				b.setCage(cageRepository.getCage(rs.getInt(13)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(14)));
				b.setPosture(rs.getInt(15));
				b.setImage(rs.getString(16));
				b.setObs(rs.getString(17));
				birds.add(b);
			}
			CloseConnection(con, stmt, rs);
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
