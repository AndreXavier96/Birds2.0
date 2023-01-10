package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import constants.MyValues;
import domains.Breeder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BreederRepository {

	public void CreateTableBreeder() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table BREEDER ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS BREEDER"
					+" (id INTEGER auto_increment, "
					+"CC INTEGER UNIQUE, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Nif INTEGER UNIQUE, "
					+"Cellphone INTEGER UNIQUE, "
					+"Email VARCHAR(255) UNIQUE, "
					+"Address VARCHAR(255), "
					+"PostalCode VARCHAR(255), "
					+"Locale VARCHAR(255), "
					+"District VARCHAR(255), "
					+"NrCites VARCHAR(255), "
					+"BreederType VARCHAR(255) NOT NULL, "
					+"Club VARCHAR(255), "
					+"STAM INTEGER UNIQUE, "
					+"PRIMARY KEY (id))";
			
			stmt.executeUpdate(sql);
			System.out.println("Table BREEDER Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableBreeder() {
		try {
			System.out.println("Trying to drop BREEDER table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS BREEDER CASCADE";	
			stmt.executeUpdate(sql);
//			System.out.println("Sql: "+sql);
			System.out.println("Table BREEDER Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Breeder> getAllBreeders() {
		try {
		System.out.println("Getting all Breeders...");
		Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
				MyValues.USER, MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM BREEDER";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Breeder> breeders = FXCollections.observableArrayList();
		while(rs.next()) {
			System.out.println("Get Breeder: "+rs.getInt(1));
			Breeder b = new Breeder();
			b.setId(rs.getInt(1));
			b.setCC(rs.getInt(2));
			b.setName(rs.getString(3));
			b.setNif(rs.getInt(4));
			b.setCellphone(rs.getInt(5));
			b.setEmail(rs.getString(6));
//			String address = rs.getString(7);
			b.setAddress(rs.getString(7));
			b.setPostalCode(rs.getString(8));
			b.setLocale(rs.getString(9));
			b.setDistrict(rs.getString(10));
			try {
				b.setNrCites(Integer.parseInt(rs.getString(11)));
			}catch (Exception e) {
				b.setNrCites(0);
			}
			
			b.setType(rs.getString(12));
			b.setClub(rs.getString(13));
			b.setStam(rs.getInt(14));
			
			breeders.add(b);
		}
		return breeders;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void Insert(Breeder b) {
		try {
			System.out.println("Insert Breeder in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "
					+"BREEDER(CC,Name,Nif,Cellphone,Email,Address,PostalCode,Locale,District,NrCites,BreederType,Club,STAM) "
					+"values('"+b.getCC()+"','"+b.getName()+"','"+b.getNif()+"','"+b.getCellphone()
					+"','"+b.getEmail()+"','"+b.getAddress()+"','"+b.getPostalCode()+"','"+b.getLocale()
					+"','"+b.getDistrict()+"','"+b.getNrCites()+"','"+b.getType()+"','"+b.getClub()
					+"','"+b.getStam()+"')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+"Record inserted!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkIfCCExists(int cc) throws SQLException {
//		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT CC FROM BREEDER WHERE CC="+cc+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();	
	}
	
	public boolean checkIfNIFExists(int nif) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT Nif FROM BREEDER WHERE Nif="+nif+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}

	public boolean checkIfPhoneExists(int phone) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT Cellphone FROM BREEDER WHERE Cellphone="+phone+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public boolean checkIfEmailExists(String email) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE Email='"+email+"';";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public boolean checkIfSTAMExists(int stam) throws SQLException {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE STAM="+stam+";";
			ResultSet rs = stmt.executeQuery(sql);
			return rs.next();
	}
	
	public Breeder getBreederbyId(int id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM BREEDER WHERE id="+id+";";
			ResultSet rs = stmt.executeQuery(sql);
			Breeder b = new Breeder();
			while(rs.next()) {
				System.out.println("Get Breeder: "+rs.getInt(1));
				b.setId(rs.getInt(1));
				b.setCC(rs.getInt(2));
				b.setName(rs.getString(3));
				b.setNif(rs.getInt(4));
				b.setCellphone(rs.getInt(5));
				b.setEmail(rs.getString(6));
				b.setAddress(rs.getString(7));
				b.setPostalCode(rs.getString(8));
				b.setLocale(rs.getString(9));
				b.setDistrict(rs.getString(10));
				try {
					b.setNrCites(Integer.parseInt(rs.getString(11)));
				}catch (Exception e) {
					b.setNrCites(0);
				}
				b.setType(rs.getString(12));
				b.setClub(rs.getString(13));
				b.setStam(rs.getInt(14));
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
