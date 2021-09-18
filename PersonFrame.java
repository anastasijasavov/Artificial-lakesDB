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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


//import PersonFrame.PersonAddAction;

public class PersonFrame extends JPanel {
	
	ResultSet rs= null;
	int id=-1;
	Connection conn = null;
	 PreparedStatement state = null;
	 JTable table = new JTable();
	 JScrollPane scroller = new JScrollPane(table);
	JPanel personInfoPanel= new JPanel();
	JPanel personButtonPanel = new JPanel();
	JPanel personTablePanel = new JPanel();
	
	JLabel fnameL= new JLabel("Name: ");	
    JLabel lnameL = new JLabel("Last name: ");
    JLabel ageL= new JLabel("Age: ");
    JLabel commentL= new JLabel("Comment: ");
    
	JTextField fnameTF = new JTextField();
    JTextField lnameTF = new JTextField();
    JTextField ageTF = new JTextField();
    JTextField commentTF = new JTextField();
    
    JButton personAddButton= new JButton("Add");
    JButton personUpdateButton= new JButton("Update");
    JButton personDeleteButton= new JButton("Delete");
    JButton personSearchButton= new JButton("Search");
    JButton personClearBtn= new JButton("Clear all");
    
    JComboBox<String> searchCombo = new JComboBox<String>();
    
    public PersonFrame() throws FileNotFoundException {
    	DBHelper.fillPersonCombo(searchCombo, "search");
    	 this.setSize(500,500);
 		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
 		
 		this.setLayout(new GridLayout(3,1));
 		
 		
 		//INFO PANEL
			personInfoPanel.setLayout(new GridLayout(4,2));
			
			personInfoPanel.add(fnameL);
			personInfoPanel.add(fnameTF);
			personInfoPanel.add(lnameL);
			personInfoPanel.add(lnameTF);
			personInfoPanel.add(ageL);
			personInfoPanel.add(ageTF);
			personInfoPanel.add(commentL);
			personInfoPanel.add(commentTF);
			
	 		this.add(personInfoPanel);	
	 	
	 		personAddButton.addActionListener(new PersonAddAction());
	 		personDeleteButton.addActionListener(new PersonDeleteAction());
	 		personUpdateButton.addActionListener(new PersonUpdateAction());
	 		personSearchButton.addActionListener(new PersonSearchAction());
	 		personClearBtn.addActionListener(new PersonClearAllAction());
	 		
	 		personButtonPanel.add(personAddButton);
	 		personButtonPanel.add(personDeleteButton);
	 		personButtonPanel.add(personUpdateButton);
	 		personButtonPanel.add(searchCombo);
	 		personButtonPanel.add(personSearchButton);
	 		personButtonPanel.add(personClearBtn);
	 		
	 		this.add(personButtonPanel);
	 		
	 		personTablePanel.add(scroller);
			scroller.setPreferredSize(new Dimension(450,160));
			table.setModel(DBHelper.getAllData("person"));
			table.addMouseListener(new TableListener());
			this.add(personTablePanel);
			
	 		this.setVisible(true);

    }
    
    public void clearForm() {
		 fnameTF.setText("");
		 lnameTF.setText("");
		 ageTF.setText("");		 
		 commentTF.setText("");		 
		 //personTF.setText("");		 
		
	 }
    class TableListener implements MouseListener{

    	@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			id = Integer.parseInt(table.getValueAt(row, 0).toString());	
			//on double click complete the values
			if(e.getClickCount()==2) {
				row = table.getSelectedRow();
				fnameTF.setText(table.getValueAt(row, 1).toString());
				lnameTF.setText(table.getValueAt(row, 2).toString());
				ageTF.setText(table.getValueAt(row, 3).toString());
				commentTF.setText(table.getValueAt(row, 4).toString());
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
    class PersonAddAction implements ActionListener{

  		@Override
  		public void actionPerformed(ActionEvent e) {
  			
  			 try {
				conn=DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
  			 String sql= "INSERT INTO PERSON VALUES (null, ?, ?, ?, ?)";
  			 try {
  				if(fnameTF.getText().matches(".*\\d.*")) 
				{
					fnameTF.setText(fnameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(lnameTF.getText().matches(".*\\d.*")) 
				{
					lnameTF.setText(lnameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(!ageTF.getText().matches("[0-9]+")) {
					ageTF.setText(ageTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
  				 
  				state= conn.prepareStatement(sql);
  				
  				state.setString(1, fnameTF.getText());
  				state.setString(2, lnameTF.getText());
  				state.setByte(3,  Byte.parseByte(ageTF.getText()));
  				state.setString(4, commentTF.getText());
  				
  				state.execute();
  				table.setModel(DBHelper.getAllData("person"));
  				
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
				DBHelper.fillPersonCombo(searchCombo, "search");
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
  			try {
				DBHelper.fillLakeCombo(LakeFrame.searchCombo);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			try {
				DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
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
    class PersonDeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			 int choice=JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this row? If you delete it, all the associated lakes will be deleted too.","Select an option...",JOptionPane.YES_NO_OPTION); 
    		 if(choice==JOptionPane.YES_OPTION) {    			
				deletePerson();
    		 }
			
		
	}//end DeleteAction
		
		public void deletePerson() {
			try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String sql = "delete from person where person_id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1,id);
				state.execute();
				
				id = -1;
				table.setModel(DBHelper.getAllData("person"));
				//DBHelper.fillCombo(searchCombo);
			} catch (SQLException | FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				DBHelper.fillLakeCombo(LakeFrame.searchCombo);
			
				DBHelper.fillPersonCombo(searchCombo, "search");
			
				DBHelper.fillPersonCombo(LakeFrame.personsCombo,"");
			
				DBHelper.fillRegionCombo(LakeFrame.regionsCombo);
			
				LakeFrame.table.setModel(DBHelper.getAllData("lake"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		}
    class PersonUpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String sql = "update person set fname=? ,lname= ? ,age=?, comment=? where person_id=?";
			try {
				if(fnameTF.getText().matches(".*\\d.*")) 
				{
					fnameTF.setText(fnameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(lnameTF.getText().matches(".*\\d.*")) 
				{
					lnameTF.setText(lnameTF.getText().replaceAll("[0123456789]", ""));
				}
				if(!ageTF.getText().matches("[0-9]+")) {
					ageTF.setText(ageTF.getText().toLowerCase().replaceAll("[a-z]", ""));
				}
				
				state = conn.prepareStatement(sql);
				
				state.setString(1, fnameTF.getText().toString());
				state.setString(2, lnameTF.getText().toString());
				state.setByte(3, Byte.parseByte(ageTF.getText()));
				state.setString(4, commentTF.getText().toString());
				state.setInt(5, id);
				state.execute();
				
				table.setModel(DBHelper.getAllData("person"));
				
				LakeFrame.table.setModel(DBHelper.getAllData("lake"));

			} catch (SQLException | FileNotFoundException e1) {
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
			
		}
		
    }
   

    class PersonSearchAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] selectedPerson=searchCombo.getSelectedItem().toString().split(" ");
			try {
				conn = DBHelper.getConnection();
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String sql="select * from person where person_id=?";
			try {
				state = conn.prepareStatement(sql);
				state.setInt(1, Integer.parseInt(selectedPerson[0]));
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
		
	}
    class PersonClearAllAction implements ActionListener{
		 
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
				table.setModel(DBHelper.getAllData("person"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	 }
  }
