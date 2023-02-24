package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
	
	public void CreateTableBird() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table Birds ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS BIRDS"
					+" (id INTEGER auto_increment, "
					+"Band VARCHAR(255) NOT NULL, "
					+"BirthYear SMALLINT NOT NULL, "
					+"EntryDate DATE, "
					+"EntryType VARCHAR(255) NOT NULL, "
					+"BuyPrice DOUBLE, "
					+"SellPrice DOUBLE, "
					+"StateId INTEGER NOT NULL, "
					+"Sex VARCHAR(255) NOT NULL, "
					+"Father INTEGER, "
					+"Mother INTEGER, "
					+"SpeciesId INTEGER NOT NULL, "
					+"MutationsId INTEGER, "
					+"CageId INTEGER NOT NULL,"
					+"BreederId INTEGER,"
					+"PostureId INTEGER,"
					+"ImagePath VARCHAR(500),"
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableBird() {
		try {
			System.out.println("Trying to drop BIRDS table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS BIRDS CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table BIRDS Droped.");
			}catch (Exception e) {
				e.printStackTrace();
			}
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
					b.setSellPrice(rs.getDouble(7));
					b.setState(stateRepository.getStateById(rs.getInt(8)));
					b.setSex(rs.getString(9));
					if (rs.getInt(10)!=0)
						b.setFather(getBirdWhereInt("id",rs.getInt(10)));
					else
						b.setFather(null);
					if (rs.getInt(11)!=0)
						b.setMother(getBirdWhereInt("id",rs.getInt(11)));
					else
						b.setMother(null);
					b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
					b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
					b.setCage(cageRepository.getCage(rs.getInt(14)));
					b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
					b.setPosture(rs.getInt(16));
					b.setImage(rs.getString(17));
					birds.add(b);
				}
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
				b.setSellPrice(rs.getDouble(7));
				b.setState(stateRepository.getStateById(rs.getInt(8)));
				b.setSex(rs.getString(9));
				if (rs.getInt(10)!=0)
					b.setFather(getBirdWhereInt("id",rs.getInt(10)));
				else
					b.setFather(null);
				if (rs.getInt(11)!=0)
					b.setMother(getBirdWhereInt("id",rs.getInt(11)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
				b.setCage(cageRepository.getCage(rs.getInt(14)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
				b.setPosture(rs.getInt(16));
				b.setImage(rs.getString(17));
				birds.add(b);
			}
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
	            b.setSellPrice(rs.getDouble(7));
	            b.setState(stateRepository.getStateById(rs.getInt(8)));
	            b.setSex(rs.getString(9));
	            int fatherId = rs.getInt(10);
	            int motherId = rs.getInt(11);
	            if (fatherId != 0) {
	                b.setFather(getBirdWhereInt("id",fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBirdWhereInt("id",motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
	            b.setCage(cageRepository.getCage(rs.getInt(14)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
	            b.setPosture(rs.getInt(16));
	            b.setImage(rs.getString(17));
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
	            b.setSellPrice(rs.getDouble(7));
	            b.setState(stateRepository.getStateById(rs.getInt(8)));
	            b.setSex(rs.getString(9));
	            int fatherId = rs.getInt(10);
	            int motherId = rs.getInt(11);
	            if (fatherId != 0) {
	                b.setFather(getBirdWhereInt("id",fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBirdWhereInt("id",motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
	            b.setCage(cageRepository.getCage(rs.getInt(14)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
	            b.setPosture(rs.getInt(16));
	            b.setImage(rs.getString(17));
	            return b;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public void Insert(Bird bird) {
		try {
			System.out.println("Insert Bird in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = dateFormat.format(bird.getEntryDate());
			
			String insert = "INSERT INTO BIRDS";
			String into = "(Band,BirthYear,EntryDate,EntryType,BuyPrice,SellPrice,StateId,Sex,Father,Mother,MutationsId,SpeciesId,CageId,BreederId,ImagePath)";
			String values="values('" + bird.getBand() + "','" + bird.getYear() + "','"
					+ dateString + "','" + bird.getEntryType() + "','" + bird.getBuyPrice() + "','"
					+ bird.getSellPrice() + "','" + bird.getState().getId() + "','" + bird.getSex() + "'";
			String imagePath = bird.getImage();
			if (bird.getImage()==null) {
				imagePath="";
			}
			String valuesFinal=",'"+ bird.getSpecies().getId() +"','"+ bird.getCage().getId() +"','"+bird.getBreeder().getId()+"','"+imagePath+"')";
			

			if (bird.getFather()==null) {
				into = into.replace(",Father", "");
			}else {
				values+=",'"+bird.getFather().getId()+ "'";
			}
			
			if (bird.getMother()==null) {
				into = into.replace(",Mother", "");
			}else {
				values+=",'"+bird.getMother().getId()+ "'";
			}
			
			if (bird.getMutations()==null) {
				into = into.replace(",MutationsId", "");
			}else {
				values+=",'"+bird.getMutations().getId()+ "'";
			}
			int i = stmt.executeUpdate(insert+into+values+valuesFinal);
			System.out.println(i+" Bird Record inserted");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void partialUpdateStringsBird(Integer id, String col, String value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement stmt = con.prepareStatement("UPDATE BIRDS SET ? = ? WHERE id=?");
	        stmt.setString(1, col);
	        stmt.setString(2, value);
	        stmt.setInt(3, id);
	        stmt.executeUpdate();
	        System.out.println("Bird with id " + id + " updated, coluna "+col+" para valor: "+value+" .");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void partialUpdateIntBird(Integer id, String col, int value) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        PreparedStatement stmt = con.prepareStatement("UPDATE BIRDS SET ? = ? WHERE id=?");
	        stmt.setString(1, col);
	        stmt.setInt(2, value);
	        stmt.setInt(3, id);
	        stmt.executeUpdate();
	        System.out.println("Bird with id " + id + " updated, coluna "+col+" para valor: "+value+" .");
	    } catch (SQLException e) {
	        e.printStackTrace();
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
				b.setBuyPrice(rs.getDouble(6));
				b.setSellPrice(rs.getDouble(7));
				b.setState(stateRepository.getStateById(rs.getInt(8)));
				b.setSex(rs.getString(9));
				if (rs.getInt(10) != 0)
					b.setFather(getBirdWhereInt("id",rs.getInt(10)));
				else
					b.setFather(null);
				if (rs.getInt(11) != 0)
					b.setMother(getBirdWhereInt("id",rs.getInt(11)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
				b.setCage(cageRepository.getCage(rs.getInt(14)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
				b.setPosture(rs.getInt(16));
				b.setImage(rs.getString(17));
				birds.add(b);
			}
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
