import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class LakeFrame extends JPanel {
	
	 Connection conn = null;
	 PreparedStatement state = null;
	static JTable table = new JTable();
	 JScrollPane scroller = new JScrollPane(table);
	 ResultSet rs =null;
	 int id= -1;
	JPanel lakeInfoPanel= new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel tablePanel = new JPanel();
	//JButton addButton = new JButton("Add");
	
	
	//infoPanel labels
	JLabel nameL= new JLabel("Name: ");	
    JLabel areaL = new JLabel("Area [in km^2]: ");
    JLabel depthL= new JLabel("Depth [in meters]: ");
    JLabel regionL= new JLabel("Region: ");
    JLabel personL= new JLabel("Responsible person: ");
    
    //infoPanel text fields
    
    
    
    JTextField nameTF = new JTextField();
    JTextField areaTF = new JTextField();
    JTextField depthTF = new JTextField(); 
   
   static JComboBox<String> regionsCombo = new JComboBox<String>();
   static JComboBox<String> personsCombo =new JComboBox<String>();
   
    //query panel + clear form button
   
    JButton clearFormBtn = new JButton("Clear all");
   
  //Button panel objects
    JButton addButton= new JButton("Add lake");
    JButton updateButton= new JButton("Update");
    JButton deleteButton= new JButton("Delete");
    static JComboBox<String> searchCombo = new JComboBox<String>();
    JButton searchButton = new JButton("Search");
    
    //Table panel objects
    
    
	 public LakeFrame() throws FileNotFoundException {
		
		//this.setSize(500,700);
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setLayout(new GridLayout(4,1));
		 DBHelper.fillLakeCombo(searchCombo);
		 DBHelper.fillPersonCombo(personsCombo,"");
		 DBHelper.fillRegionCombo(regionsCombo);
		
		//INFO PANEL
		lakeInfoPanel.setLayout(new GridLayout(5,2));
		
		lakeInfoPanel.add(nameL);
		lakeInfoPanel.add(nameTF);
		lakeInfoPanel.add(areaL);
		lakeInfoPanel.add(areaTF);
		lakeInfoPanel.add(depthL);
		lakeInfoPanel.add(depthTF);
		lakeInfoPanel.add(regionL);
		lakeInfoPanel.add(regionsCombo);
		lakeInfoPanel.add(personL);
		lakeInfoPanel.add(personsCombo);
		
		this.add(lakeInfoPanel);
		//queryPanel.setSize(new Dimension(500,100));
		
		
		
		//BUTTON PANEL
		//buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setSize(new Dimension(600,100));
		buttonPanel.add(addButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(searchCombo);
		buttonPanel.add(searchButton);
		buttonPanel.add(clearFormBtn);
		
		this.add(buttonPanel);
		
		
		addButton.addActionListener(new AddAction());
		deleteButton.addActionListener(new DeleteAction());
		updateButton.addActionListener(new UpdateAction());
		searchButton.addActionListener(new SearchAction());
		
		clearFormBtn.addActionListener(new ClearAllAction());
		
		//TABLE PANEL
		this.add(tablePanel);
		tablePanel.add(scroller);
		scroller.setPreferredSize(new Dimension(450,160));
		table.addMouseListener(new TableListener());
		table.setModel(DBHelper.getAllData("lake"));
		
		this.setVisible(true);
	}
	 //metoda da se isciste text fields nakon sto se klikne na neko dugme
	 public void clearForm() {
		 nameTF.setText("");
		 areaTF.setText("");
		 depthTF.setText("");		 
		
	 }
	 class TableListener implements MouseListener{

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				id = Integer.parseInt(table.getValueAt(row, 0).toString());
				String person_name = table.getValueAt(row, 4).toString();
				String region_name =table.getValueAt(row, 5).toString();
				
				if(e.getClickCount()==2) {
					row = table.getSelectedRow();
					nameTF.setText(table.getValueAt(row, 1).toString());
					areaTF.setText(table.getValueAt(row, 2).toString());
					depthTF.setText(table.getValueAt(row, 3).toString());
					//da se popune polja s onim iz tabele					
					try {
						personsCombo.setSelectedItem(DBHelper.findInfoOf("person",personsCombo, person_name));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}				
					try {
						regionsCombo.setSelectedItem(DBHelper.findInfoOf("region",regionsCombo, region_name));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
								
					
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}//end TableListener
	 //dodavanje podataka u listu
	 class AddAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			

			String selectedRegion= regionsCombo.getSelectedItem().toString();		  
			String[] regionsArr = selectedRegion.split(" ");
			String selectedPerson= personsCombo.getSelectedItem().toString();			
			String[] personsArr = selectedPerson.split(" ");
			
			 try {
				conn=DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 String sql= "INSERT INTO LAKE VALUES (null, ?, ?, ?, ?, ?)";
			 try {
				state= conn.prepareStatement(sql);
				if(nameTF.getText().matches(".*\\d.*")) 
				{
					nameTF.setText(nameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(!areaTF.getText().matches("[0-9]+")) 
				{
					areaTF.setText(areaTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
				if(!depthTF.getText().matches("[0-9]+")) 
				{
					depthTF.setText(depthTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}  
				
				state.setString(1, nameTF.getText());
				state.setFloat(2, Float.parseFloat(areaTF.getText()));
				state.setFloat(3,  Float.parseFloat(depthTF.getText()));
				if(selectedPerson!=null) {
				state.setInt(4, Integer.parseInt(personsArr[0]));}
				if(selectedRegion!=null) {
				state.setInt(5, Integer.parseInt(regionsArr[0]));}
				
				
				
				state.execute();
				table.setModel(DBHelper.getAllData("lake"));
				DBHelper.fillPersonCombo(personsCombo,"");
				DBHelper.fillRegionCombo(regionsCombo);
			} catch (SQLException | FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
                try {
                    state.close();
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
			 
			 clearForm();
			
		}
		
		 
		 
	 }
	 class DeleteAction implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					conn = DBHelper.getConnection();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				String sql = "delete from lake where lake_id="+id;
				try {
					state = conn.prepareStatement(sql);
					
					state.execute();
					id = -1;
					table.setModel(DBHelper.getAllData("lake"));
					//DBHelper.fillCombo(searchCombo);
				} catch (SQLException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					DBHelper.fillPersonCombo(personsCombo,"");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			
			
		}//end DeleteAction
	 class UpdateAction implements ActionListener{
		 

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedRegion= regionsCombo.getSelectedItem().toString();
				String[] regionsArr = selectedRegion.split(" ");
				String selectedPerson= personsCombo.getSelectedItem().toString();
				String[] personsArr = selectedPerson.split(" ");
				String name;
				float area, depth;
				int person_id, region_id;
				if(!nameTF.getText().matches(".*\\d.*")) 
				{
					name=nameTF.getText().toString();
				}else {
					name=nameTF.getText().replaceAll("[0123456789]", "");
				}
				if(areaTF.getText().matches("[0-9]+")) 
				{
					area=Float.parseFloat(areaTF.getText());
				}else {
					area=Float.parseFloat(areaTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
				if(depthTF.getText().matches("[0-9]+")) 
				{
					depth =Float.parseFloat(depthTF.getText());
				}else {
					depth =Float.parseFloat(depthTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
					person_id =Integer.parseInt(personsArr[0]);
					region_id=Integer.parseInt(regionsArr[0]);
				
				try {
					conn = DBHelper.getConnection();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				String sql = "update lake set name=? ,area= ? ,depth=? ,person_id=?, region_id=?  where lake_id=?";
				try {
					state = conn.prepareStatement(sql);
					state.setString(1, name);
					state.setFloat(2, area);
					state.setFloat(3, depth);
					state.setInt(4, person_id);
					state.setInt(5, region_id);
					state.setInt(6, id);
					state.execute();					
					table.setModel(DBHelper.getAllData("lake"));
					//DBHelper.fillCombo(searchCombo);
				} catch (SQLException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}//end UpdateAction
	 class SearchAction implements ActionListener{
		
			

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] selectedLake=searchCombo.getSelectedItem().toString().split(" ");
				try {
					conn = DBHelper.getConnection();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				int lake_id=Integer.parseInt(selectedLake[0]);
				String sql="select lake_id, lake.name, lake.area, lake.depth, person.fname, region.name from person join lake on lake.person_id=person.person_id join region on region.region_id=lake.region_id where lake_id=?";
				try {
					state = conn.prepareStatement(sql);				
					state.setInt(1, lake_id);
					rs=state.executeQuery();
										
					table.setModel(new MyModel(rs));
					//DBHelper.fillCombo(searchCombo);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}//end UpdateAction
	
	 class ClearAllAction implements ActionListener{
		 
		 @Override
			public void actionPerformed(ActionEvent e) {
			 
			 try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 clearForm();
//			 queryTF.setText("");
			 try {
				DBHelper.fillRegionCombo(regionsCombo);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				DBHelper.fillPersonCombo(personsCombo, "");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				table.setModel(DBHelper.getAllData("lake"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	 }
}
