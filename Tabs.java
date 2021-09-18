import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Tabs extends JFrame{
	JTabbedPane tab=new JTabbedPane();
	JPanel lakeF = new JPanel();	
	JPanel personF= new JPanel();
	JPanel regionF= new JPanel();
	JPanel queryF= new JPanel();
	public Tabs() throws FileNotFoundException {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lakeF.add(new LakeFrame());
		personF.add(new PersonFrame());
		regionF.add(new RegionFrame());
		queryF.add(new QueryFrame());
		
		tab.add("Lake info",lakeF);
		tab.add("Region",regionF);
		tab.add("Person",personF);
		tab.add("Query",queryF);
		this.add(tab);
		
		this.setVisible(true);
	}
}
