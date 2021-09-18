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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;



//import RegionFrame.RegionAddAction;

public class RegionFrame extends JPanel{
	ResultSet rs= null;
	Connection conn = null;
	 PreparedStatement state = null;
	 JTable table = new JTable();
	 JScrollPane scroller = new JScrollPane(table);
	// JFrame regionFrame= new JFrame();
	int id= -1;
	JPanel regionInfoPanel= new JPanel();
	JPanel regionButtonPanel = new JPanel();
	JPanel regionTablePanel = new JPanel();
	
	JLabel nameL= new JLabel("Name: ");	
    JLabel areaL = new JLabel("Area: ");
    JLabel populationL= new JLabel("Population: ");
	
	JTextField nameTF = new JTextField();
    JTextField areaTF = new JTextField();
    JTextField populationTF = new JTextField();   
    
    JButton regionAddButton= new JButton("Add");
    JButton regionUpdateButton= new JButton("Update");
    JButton regionDeleteButton= new JButton("Delete");
    JButton regionSearchButton = new JButton("Search");
    JButton regionClearBtn = new JButton("Clear all");
    JComboBox<String> searchCombo= new JComboBox<String>();
    
    public RegionFrame() throws FileNotFoundException {
    	 this.setSize(500,500);
 	//	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
 		
 		this.setLayout(new GridLayout(3,1));
 		DBHelper.fillRegionCombo(searchCombo);
 		
 		//INFO PANEL
 		regionInfoPanel.setLayout(new GridLayout(3,2));
 				
 		regionInfoPanel.add(nameL);
 		regionInfoPanel.add(nameTF);
 		regionInfoPanel.add(areaL);
 		regionInfoPanel.add(areaTF);
 		regionInfoPanel.add(populationL);
 		regionInfoPanel.add(populationTF);
 				
 		this.add(regionInfoPanel);
 		
 		
 		
 		regionButtonPanel.add(regionAddButton);
 		regionButtonPanel.add(regionDeleteButton);
		regionButtonPanel.add(regionUpdateButton);
		regionButtonPanel.add(searchCombo);
		regionButtonPanel.add(regionSearchButton);
		regionButtonPanel.add(regionClearBtn);
		regionAddButton.addActionListener(new RegionAddAction());
		regionDeleteButton.addActionListener(new RegionDeleteAction());
		regionUpdateButton.addActionListener(new RegionUpdateAction());
		regionSearchButton.addActionListener(new RegionSearchAction());
		regionClearBtn.addActionListener(new RegionClearAllAction());
 		this.add(regionButtonPanel);
 		
 	
 		regionTablePanel.add(scroller);
		scroller.setPreferredSize(new Dimension(450,160));
		table.addMouseListener(new TableListener());
		table.setModel(DBHelper.getAllData("region"));
		
		this.add(regionTablePanel);
		
 		this.setVisible(true);
    }
    public void clearForm() {
		 nameTF.setText("");
		 areaTF.setText("");
		 populationTF.setText("");		 
		 //personTF.setText("");		 
		
	 }
    class TableListener implements MouseListener{

    	@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getValueAt(row, 0).toString());			
			if(e.getClickCount()==2) {
				row = table.getSelectedRow();
				nameTF.setText(table.getValueAt(row, 1).toString());
				areaTF.setText(table.getValueAt(row, 2).toString());
				populationTF.setText(table.getValueAt(row, 3).toString());
				
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
    
    class RegionAddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			 try {
				conn=DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 String sql= "INSERT INTO REGION VALUES (null, ?, ?, ?)";
			 try {
				 if(nameTF.getText().matches(".*\\d.*")) 
					{
						nameTF.setText(nameTF.getText().replaceAll("[0123456789]", ""));
					}
					if(!areaTF.getText().matches("[0-9]+")) 
					{
						areaTF.setText(areaTF.getText().toLowerCase().replaceAll("[a-z]", ""));
					}
					if(!populationTF.getText().matches("[0-9]+")) {
						populationTF.setText(populationTF.getText().toLowerCase().replaceAll("[a-z]", ""));
					}
					
				state= conn.prepareStatement(sql);				
				state.setString(1, nameTF.getText());
				state.setFloat(2, Float.parseFloat(areaTF.getText()));
				state.setInt(3,  Integer.parseInt(populationTF.getText()));
				
				state.execute();
				table.setModel(DBHelper.getAllData("region"));
				
				LakeFrame.personsCombo.removeAllItems();
	  			LakeFrame.regionsCombo.removeAllItems();
	  			
				DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");
				DBHelper.fillRegionCombo(LakeFrame.regionsCombo);
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
			 LakeFrame.personsCombo.removeAllItems();
	  		 LakeFrame.regionsCombo.removeAllItems();
	  		 try {
				DBHelper.fillRegionCombo(searchCombo);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 try {
				DBHelper.fillRegionCombo(LakeFrame.regionsCombo);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 clearForm();
		}
    }
    class RegionDeleteAction implements ActionListener{
    	
    	
    	 @Override
			public void actionPerformed(ActionEvent e) {
    		 	//optionPane.show();
    		 int choice=JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this row? If you delete it, all the associated lakes will be deleted too.","Select an option...",JOptionPane.YES_NO_OPTION); 
    		 if(choice==JOptionPane.YES_OPTION) {
     			
				deleteReg();
    		 }
    	   }
			
		}//end DeleteAction
    public void deleteReg() {
    	try {
			conn = DBHelper.getConnection();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String sql = "delete from region where region_id=?";
		try {
			state = conn.prepareStatement(sql);
			state.setInt(1, id);
			state.execute();
			id = -1;
			table.setModel(DBHelper.getAllData("region"));
			//DBHelper.fillCombo(searchCombo);
		} catch (SQLException | FileNotFoundException e1) {					
			e1.printStackTrace();
		}
		LakeFrame.personsCombo.removeAllItems();
			LakeFrame.regionsCombo.removeAllItems();
			try {
			LakeFrame.table.setModel(DBHelper.getAllData("lake"));	  			
			DBHelper.fillRegionCombo(searchCombo);			
			DBHelper.fillLakeCombo(LakeFrame.searchCombo);				
			DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");				
			DBHelper.fillRegionCombo(LakeFrame.regionsCombo);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    class RegionUpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String sql = "update region set name=? ,area=? ,population=? where region_id=?";
			try {
				if(nameTF.getText().matches(".*\\d.*")) 
				{
					nameTF.setText(nameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(!areaTF.getText().matches("[0-9]+")) 
				{
					areaTF.setText(areaTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
				if(!populationTF.getText().matches("[0-9]+")) {
					populationTF.setText(populationTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
				state = conn.prepareStatement(sql);
				state.setString(1, nameTF.getText().toString());
				state.setFloat(2, Float.parseFloat(areaTF.getText()));
				state.setInt(3, Integer.parseInt(populationTF.getText()));
				state.setInt(4, id);
				state.execute();					
				table.setModel(DBHelper.getAllData("region"));
				//DBHelper.fillCombo(searchCombo);
			
				DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");
			
				DBHelper.fillLakeCombo(LakeFrame.searchCombo);
			
				DBHelper.fillRegionCombo(LakeFrame.regionsCombo);
				
				
				LakeFrame.table.setModel(DBHelper.getAllData("lake"));
			} catch (FileNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}//end UpdateAction
  
    class RegionSearchAction implements ActionListener{
		
		

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] selectedRegion=searchCombo.getSelectedItem().toString().split(" ");
			try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String sql="select * from region where region_id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, Integer.parseInt(selectedRegion[0]));
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
		
	}//end RegionSearchAction
    class RegionClearAllAction implements ActionListener{
		 
		 @Override
			public void actionPerformed(ActionEvent e) {
			 
			 try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 clearForm();			 						
			 try {
				table.setModel(DBHelper.getAllData("region"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	 }
}
    
