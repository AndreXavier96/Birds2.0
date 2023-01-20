package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
					+"State VARCHAR(255) NOT NULL, "
					+"Sex VARCHAR(255) NOT NULL, "
					+"Father INTEGER, "
					+"Mother INTEGER, "
					+"SpeciesId INTEGER NOT NULL, "
					+"MutationsId INTEGER, "
					+"CageId INTEGER NOT NULL,"
					+"BreederId INTEGER,"
					+"PostureId INTEGER,"
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (Father) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (Mother) REFERENCES BIRDS (id), "
					+"FOREIGN KEY (SpeciesId) REFERENCES SPECIES (id), "
					+"FOREIGN KEY (MutationsId) REFERENCES MUTATIONS (id), "
					+"FOREIGN KEY (CageId) REFERENCES CAGE (id), "
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER (id), "
					+"FOREIGN KEY (PostureId) REFERENCES POSTURE (id))";
		
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
					b.setState(rs.getString(8));
					b.setSex(rs.getString(9));
					if (rs.getInt(10)!=0)
						b.setFather(getBird(rs.getInt(10)));
					else
						b.setFather(null);
					if (rs.getInt(11)!=0)
						b.setMother(getBird(rs.getInt(11)));
					else
						b.setMother(null);
					b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
					b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
					b.setCage(cageRepository.getCage(rs.getInt(14)));
					b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
					b.setPosture(rs.getInt(16));
					birds.add(b);
				}
				return birds;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	
	
	public ObservableList<Bird> getAllMales() {
		try {
			System.out.println("Getting all Birds...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE Sex='"+MyValues.MACHO+"'";
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
				b.setState(rs.getString(8));
				b.setSex(rs.getString(9));
				if (rs.getInt(10)!=0)
					b.setFather(getBird(rs.getInt(10)));
				else
					b.setFather(null);
				if (rs.getInt(11)!=0)
					b.setMother(getBird(rs.getInt(11)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
				b.setCage(cageRepository.getCage(rs.getInt(14)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
				b.setPosture(rs.getInt(16));
				birds.add(b);
			}
			return birds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Bird> getAllFemales() {
		try {
			System.out.println("Getting all Birds...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE Sex='"+MyValues.FEMEA+"'";
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
				b.setState(rs.getString(8));
				b.setSex(rs.getString(9));
				if (rs.getInt(10)!=0)
					b.setFather(getBird(rs.getInt(10)));
				else
					b.setFather(null);
				if (rs.getInt(11)!=0)
					b.setMother(getBird(rs.getInt(11)));
				else
					b.setMother(null);
				b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
				b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
				b.setCage(cageRepository.getCage(rs.getInt(14)));
				b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
				b.setPosture(rs.getInt(16));
				birds.add(b);
			}
			return birds;
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
			String into = "(Band,BirthYear,EntryDate,EntryType,BuyPrice,SellPrice,State,Sex,Father,Mother,MutationsId,SpeciesId,CageId,BreederId)";
			String values="values('" + bird.getBand() + "','" + bird.getYear() + "','"
					+ dateString + "','" + bird.getEntryType() + "','" + bird.getBuyPrice() + "','"
					+ bird.getSellPrice() + "','" + bird.getState() + "','" + bird.getSex() + "'";
			String valuesFinal=",'"+ bird.getSpecies().getId() +"','"+ bird.getCage().getId() +"','"+bird.getBreeder().getId()+"')";
			

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


	public Bird getBird(Integer id) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM BIRDS WHERE id=" + id + ";";
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
	            b.setState(rs.getString(8));
	            b.setSex(rs.getString(9));
	            int fatherId = rs.getInt(10);
	            int motherId = rs.getInt(11);
	            if (fatherId != 0) {
	                b.setFather(getBird(fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBird(motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
	            b.setCage(cageRepository.getCage(rs.getInt(14)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
	            b.setPosture(rs.getInt(16));
	            return b;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	public Bird getBirdByBand(String band) {
	    try {
	        Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER, MyValues.PASSWORD);
	        Statement stmt = con.createStatement();
	        String sql = "SELECT * FROM BIRDS WHERE Band='" + band + "';";
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
	            b.setState(rs.getString(8));
	            b.setSex(rs.getString(9));
	            int fatherId = rs.getInt(10);
	            int motherId = rs.getInt(11);
	            if (fatherId != 0) {
	                b.setFather(getBird(fatherId));
	            }
	            if (motherId != 0) {
	                b.setMother(getBird(motherId));
	            }
	            b.setSpecies(speciesRepository.getSpecieById(rs.getInt(12)));
	            b.setMutations(mutationsRepository.getMutationsById(rs.getInt(13)));
	            b.setCage(cageRepository.getCage(rs.getInt(14)));
	            b.setBreeder(breederRepository.getBreederbyId(rs.getInt(15)));
	            b.setPosture(rs.getInt(16));
	            return b;
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
