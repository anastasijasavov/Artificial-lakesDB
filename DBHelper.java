
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

import javax.swing.JComboBox;



public class DBHelper {
	
	static Connection conn=null;
	static PreparedStatement state = null;
	static MyModel model = null;
	static ResultSet result = null;
	
	 static Connection getConnection() throws FileNotFoundException {
		 String connString="", userName="", pass="";
	        try {
	        	//trazi config fajl da bi se izvukli podaci za connection string
	            Class.forName("org.h2.Driver");
	            File file =
	            	      new File("C:\\Users\\Anastasija\\eclipse-workspace\\Reservoir tracker\\config-file.txt");
	           
	            Scanner sc = new Scanner(file);
	            //dok postoji nov red, neka procita prva tri reda iz fajla koji su redom connstring, username i sifra i neka
	            //breakuje kad procita ta tri reda
	            while (sc.hasNextLine()) {
	            	 connString=sc.nextLine().trim();
	            	 
	            	 userName=sc.nextLine().trim();
	            	 
	            	 pass=sc.nextLine().trim();
	            	break;
	            }
	            //konekcija	 
	            conn= DriverManager.getConnection(connString,userName,pass);
	            sc.close();
	            
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException throwables) {
	            throwables.printStackTrace();
	        }
	        return conn;
	    }
	 //2 comboboxa mogu da se popunjavaju, person ili ako je u pitanju search combobox onda se traze ljudi koji do sad nisu zaduzivani za jezero
	 static void fillPersonCombo(JComboBox<String> combo, String query) throws FileNotFoundException  {
		 String sql;
			conn = getConnection();
			
			if(query=="search") {
				//combobox koji ispisuje sve ljude zaduzene za jezero, koristi se u person tabu
				 sql = "select distinct person.person_id,fname, lname from person ";
			} else {
				//koristi se za lake tab
				//jedna osoba moze da bude zaduzena za samo jedno jezero i zato u bazi imamo unique foreign key+ispisuje sve ljude koji nemaju vezu s jezerom
			     sql = "select distinct person.person_id,fname, lname from person left join lake on person.person_id  = lake.person_id where lake.person_id is null";
			}
			try {
				state = conn.prepareStatement(sql);
				result = state.executeQuery();   //combo.setModel(aModel);
				
				combo.removeAllItems();
				//getObject1 je id a getObject2=ime osobe
				while(result.next()) {	
					if(result.getObject(1).toString()==null) {
						combo.setSelectedItem("");
					} else {
						String item = result.getObject(1).toString() + " " + result.getObject(2);		
						//dodavanje u combobox
						combo.addItem(item);		
					}
				}//end while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//end fillCombo
	 //search combo koji ima sva jezera u lake tab
	 static void fillLakeCombo(JComboBox<String> combo) throws FileNotFoundException{
		 conn = getConnection();
			
			String sql = "select lake_id, name from lake";
			try {
				state = conn.prepareStatement(sql);
				result = state.executeQuery();   //combo.setModel(aModel);
				combo.removeAllItems();
				while(result.next()) {					
						String item = result.getObject(1).toString() + " " + result.getObject(2);						
						combo.addItem(item);		
					
				}//end while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 //popunjava se region search combo, tj svi regioni
	 static void fillRegionCombo(JComboBox<String> combo) throws FileNotFoundException {
			
			conn = getConnection();
			String sql = "select distinct region_id,name from region";
			try {
				state = conn.prepareStatement(sql);
				result = state.executeQuery();   //combo.setModel(aModel);
				combo.removeAllItems();
				while(result.next()) {
					String item = result.getObject(1).toString() + " " + result.getObject(2);
					combo.addItem(item);
				}//end while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//end fillCombo
	 //trazi se id regiona ili odgovornog lica i kad se double clickuje na jedan red iz tabele selectuje se odredjena region/osoba iz combobox za region/osobu 
	 static String findInfoOf(String columnName, JComboBox<String> combo, String name)  throws FileNotFoundException{
		 String sql;
		 String returnVal="";
		 conn = getConnection();
		
		 
			try {	
				 if(columnName=="person") {
					  sql = "select distinct person.person_id, fname from person left join lake on person.person_id = lake.person_id where lake.person_id is null or person.fname= ?";
					  state = conn.prepareStatement(sql);
					  state.setString(1, name);
				 } else {
					  sql = "select distinct region.region_id, region.name from region left join lake on lake.region_id=region.region_id or region.name=?";
					  state = conn.prepareStatement(sql);
					  state.setString(1, name);
				 }			
				
				
				result = state.executeQuery();   //combo.setModel(aModel);	
				combo.removeAllItems();
				while(result.next()) {
									
					String item = result.getObject(1).toString() + " " + result.getObject(2);	
					String [] itemArr = item.split(" ");
					
					if(itemArr[1].compareTo(name)==0)
					{									   
						combo.addItem(item);
						returnVal=item;
							
					}else 
					{
						combo.addItem(item);						
					}					
				
				}
				
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnVal;
			
	 }
	//popunjava se panel za tabelu sa tabelom iz baze
	 static MyModel getAllData(String tableName)  throws FileNotFoundException{
		 String sql="";
			conn = getConnection();
			if(tableName=="lake") {
				sql = "select lake_id, lake.name, lake.area, lake.depth, person.fname, region.name from person "
						+ "join lake on person.person_id=lake.person_id "
						+ "join region on region.region_id= lake.region_id";
			}
			if(tableName=="region") {
				 sql = "select * from region ";
			} 
			if(tableName=="person") {
				 sql = "select * from person ";
			}
			try {
				state = conn.prepareStatement(sql);
				result = state.executeQuery();
				model = new MyModel(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return model;
			
		}//end getAllData
}
