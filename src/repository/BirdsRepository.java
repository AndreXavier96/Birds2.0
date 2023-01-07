package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.MyValues;
import domains.Bird;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BirdsRepository {
	
	private CageRepository cageRepository =  new CageRepository();
	private BreederRepository breederRepository = new BreederRepository();
	
	public void CreateTableBird() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table Birds ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS BIRDS"
					+" (id INTEGER auto_increment, "
					+"Breeder VARCHAR(255) NOT NULL, "
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
					+"FOREIGN KEY (BreederId) REFERENCES BREEDER (CC), "
					+"FOREIGN KEY (PostureId) REFERENCES POSTURE (id))";
		
			stmt.executeUpdate(sql);
//			System.out.println("Sql: " + sql);
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
//			System.out.println("Sql: "+sql);
			System.out.println("Table BIRDS Droped.");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	//TODO
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
//					b.setNrBreeder(rs.getString(2));
					b.setNrBreeder(breederRepository.getBreederbyId(2));
					b.setBand(rs.getString(3));
					b.setYear(rs.getInt(4));
					b.setEntryDate(rs.getDate(5));
					b.setEntryType(rs.getString(6));
					b.setBuyPrice(rs.getDouble(7));
					b.setSellPrice(rs.getDouble(8));
					b.setStatel(rs.getString(9));
					b.setSex(rs.getString(10));
//					b.setFather(rs.getInt(11));
					b.setFather(getBird(rs.getInt(11)));
//					b.setMother(rs.getInt(12));
					b.setMother(getBird(rs.getInt(12)));
					b.setSpecies(rs.getInt(13));
					b.setMutations(rs.getInt(14));
//					b.setCage(rs.getInt(15));
					b.setCage(cageRepository.getCage(rs.getInt(15)));
					b.setBreeder(rs.getInt(16));
					b.setPosture(rs.getInt(17));
					birds.add(b);
				}
				return birds;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	
		
	//TODO
	public void Insert(Bird bird) {
		try {
			System.out.println("Insert Bird in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
			Date entryDate = sdfDate.parse(sdf.format(bird.getEntryDate()));
			
			String sql = "INSERT INTO "
					+ "BIRDS(Breeder,Band,BirthYear,EntryDate,EntryType,BuyPrice,SellPrice,State,Sex,Father,Mother,SpeciesId,MutationsId,CageId,BreederId,PostureId) "
					+ "values('" + bird.getNrBreeder().getId() + "','" + bird.getBand() + "','" + bird.getYear() + "','"
					+ entryDate + "','" + bird.getEntryType() + "','" + bird.getBuyPrice() + "','"
					+ bird.getSellPrice() + "','" + bird.getStatel() + "','" + bird.getSex() + "','" + bird.getFather()
					+ "','" + bird.getMother() + "','" + bird.getSpecies() + "','" + bird.getMutations() + "','"
					+ bird.getCage() + "','" + bird.getBreeder() + "','" + bird.getPosture() + "')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+" Record inserted");
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public Bird getBird(Integer id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BIRDS WHERE BIRDS.id='"+id+"'";
			ResultSet rs  = stmt.executeQuery(sql);
			Bird b = new Bird();
			while (rs.next()) {
				b.setId(rs.getInt(0));
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
