package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.MyValues;
import domains.Mutation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MutationsRepository {
	
	private SpeciesRepository speciesRepository = new SpeciesRepository();
	
	public void CreateTableMutations() {
		try {
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME, MyValues.USER,MyValues.PASSWORD);
			System.out.println("Creating Table MUTATIONS ...");
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS MUTATIONS"
					+" (id INTEGER auto_increment, "
					+"Name VARCHAR(255) NOT NULL, "
					+"Var1 VARCHAR(255), "
					+"Var2 VARCHAR(255), "
					+"Var3 VARCHAR(255), "
					+"Obs VARCHAR(500), "
					+"SpeciesId INTEGER NOT NULL, "
					+"PRIMARY KEY (id), "
					+"FOREIGN KEY (SpeciesId) REFERENCES SPECIES (id))";
			stmt.executeUpdate(sql);
			System.out.println("Table MUTATIONS Created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableMutations() {
		try {
			System.out.println("Trying to drop MUTATIONS table...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "DROP TABLE IF EXISTS MUTATIONS CASCADE";	
			stmt.executeUpdate(sql);
			System.out.println("Table MUTATIONS Droped.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Insert(Mutation mutation) {
		try {
			System.out.println("Insert Mutation in DataBase...");
			Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			
			String sql = "INSERT INTO "
					+"MUTATIONS(Name,Var1,Var2,Var3,Obs,SpeciesId) "
					+"values('"+mutation.getName()+"','"+mutation.getVar1()+"','"
					+mutation.getVar2()+"','"+mutation.getVar3()
					+"','"+mutation.getObs()
					+"','"+mutation.getSpecie().getId()+"')";
			int i = stmt.executeUpdate(sql);
			System.out.println(i+" Mutation Record inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ObservableList<Mutation> getAllMutations() {
		try {
			System.out.println("Getting all Mutations...");
			Connection con = DriverManager.getConnection("jdbc:h2:" + "./Database/" + MyValues.DBNAME,
					MyValues.USER, MyValues.PASSWORD);
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM MUTATIONS";
			ResultSet rs = stmt.executeQuery(sql);
			ObservableList<Mutation> mutations = FXCollections.observableArrayList();
			while (rs.next()) {
				System.out.println("Get Mutations: " + rs.getInt(1));
				Mutation m = new Mutation();
				m.setId(rs.getInt(1));
				m.setName(rs.getString(2));
				m.setVar1(rs.getString(3));
				m.setVar2(rs.getString(4));
				m.setVar3(rs.getString(5));
				m.setObs(rs.getString(6));
				m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
				mutations.add(m);
			}
			return mutations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public ObservableList<Mutation> getMutationsBySpecie(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS WHERE SpeciesId="+id+";";
		ResultSet rs = stmt.executeQuery(sql);
		ObservableList<Mutation> mutations = FXCollections.observableArrayList();
		while (rs.next()) {
			Mutation m = new Mutation();
			System.out.println("Get Mutation: "+rs.getInt(1));
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
			mutations.add(m);
		}
		return mutations;
	}
	
	public Mutation getMutationsById(int id) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:h2:"+"./Database/"+MyValues.DBNAME,MyValues.USER,MyValues.PASSWORD);
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM MUTATIONS WHERE id="+id+";";
		ResultSet rs = stmt.executeQuery(sql);
		Mutation m = new Mutation();
		while (rs.next()) {
			System.out.println("Get Mutation: "+rs.getInt(1));
			m.setId(rs.getInt(1));
			m.setName(rs.getString(2));
			m.setVar1(rs.getString(3));
			m.setVar2(rs.getString(4));
			m.setVar3(rs.getString(5));
			m.setObs(rs.getString(6));
			m.setSpecie(speciesRepository.getSpecieById(rs.getInt(7)));
		}
		return m;
	}
	
}
