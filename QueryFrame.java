import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class QueryFrame extends JPanel{

	 Connection conn = null;
	 PreparedStatement state = null;
	 static JTable table = new JTable();
	 JScrollPane scroller = new JScrollPane(table);
	 ResultSet rs =null;
	 int id= -1;
	
	 JPanel queryPanel = new JPanel();
	 JLabel fnameL= new JLabel("Person name:");
	 JLabel lakeNameL= new JLabel("Lake name:");
	 JPanel tablePanel = new JPanel();
	 JTextField fnameTF = new JTextField();
	 JTextField lakeNameTF = new JTextField();
	 JButton searchQueryBtn= new JButton("Query");
	 JPanel buttonPanel = new JPanel();
	public QueryFrame() {
		
		this.setLayout(new GridLayout(3,1));
		queryPanel.setLayout(new GridLayout(2,1));
		queryPanel.add(fnameL);
		queryPanel.add(fnameTF);
		queryPanel.add(lakeNameL);		
		queryPanel.add(lakeNameTF);
		
		buttonPanel.add(searchQueryBtn);
		this.add(queryPanel);
		searchQueryBtn.addActionListener(new SearchQueryAction());
		this.add(buttonPanel);
		this.add(tablePanel);
		tablePanel.add(scroller);
		scroller.setPreferredSize(new Dimension(450,160));
		
	}
	 class SearchQueryAction implements ActionListener{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String personName = fnameTF.getText();
				String lakeName = lakeNameTF.getText();
				
				if(personName != "" && lakeName != "") {
				try {
					conn = DBHelper.getConnection();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				String sql="select lake.name, lake.area, lake.depth, person.fname,region.name from person join lake on lake.person_id = person.person_id join region on region.region_id=lake.region_id where person.fname=? and lake.name=?";
				try {
					state = conn.prepareStatement(sql);
					
					state.setString(1, personName);
					state.setString(2, lakeName);
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
			
		}//end UpdateAction
}
