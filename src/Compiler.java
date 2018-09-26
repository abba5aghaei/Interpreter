import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;

public class Compiler extends JFrame {
	
	private String code;
	private Scanner scanner;
	private Parser parser;
	
	public Compiler() {
		
		setResizable(false);
		setTitle("Compiler - Simlpe Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 20, 935, 610);
		
		scanner = new Scanner();
		parser = new Parser();
		
		JMenuItem mnScan = new JMenuItem("Scan");
		mnScan.setEnabled(false);
		
		JFileChooser explorer = new JFileChooser();
		explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		explorer.setMultiSelectionEnabled(false);
		explorer.setCurrentDirectory(new File("./"));
		
		JPanel Pane = new JPanel();
		Pane.setBackground(Color.DARK_GRAY);
		setContentPane(Pane);
		Pane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(470, 25, 450, 400);
		Pane.add(scrollPane);
		
		JTextPane result_field = new JTextPane();
		scrollPane.setViewportView(result_field);
		result_field.setFont(new Font("Arial",Font.PLAIN,16));
		result_field.setEditable(false);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(10, 25, 450, 400);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		Pane.add(scrollPane2);
		
		JScrollPane scrollPane3 = new JScrollPane();
		scrollPane3.setBounds(10, 450, 450, 100);
		scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Pane.add(scrollPane3);
		
		JTextPane paper = new JTextPane();
		paper.setFont(new Font("Arial",Font.PLAIN,16));
		scrollPane2.setViewportView(paper);
		paper.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent ke) {
				
				mnScan.setEnabled(true);
			}
			public void keyReleased(KeyEvent ke) {}
			public void keyPressed(KeyEvent ke) {}});
		
		JLabel label_here = new JLabel("Enter your code here:");
		label_here.setForeground(Color.WHITE);
		label_here.setBounds(10, 5, 140, 15);
		Pane.add(label_here);
		
		JTextPane errors_field = new JTextPane();
		errors_field.setBackground(Color.red.darker());
		errors_field.setForeground(Color.white);
		errors_field.setEditable(false);
		scrollPane3.setViewportView(errors_field);
		
		JScrollPane scrollPane4 = new JScrollPane();
		scrollPane4.setBounds(470, 450, 450, 100);
		scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Pane.add(scrollPane4);
		
		JTextPane table_field = new JTextPane();
		table_field.setBackground(Color.blue);
		table_field.setForeground(Color.white);
		table_field.setEditable(false);
		scrollPane4.setViewportView(table_field);
		
		JLabel lblResult = new JLabel("Result:");
		lblResult.setForeground(Color.WHITE);
		lblResult.setBounds(470, 5, 50, 15);
		Pane.add(lblResult);
		
		JLabel label_errors = new JLabel("Errors:");
		label_errors.setForeground(Color.WHITE);
		label_errors.setBounds(10, 430, 60, 15);
		Pane.add(label_errors);
		
		JLabel label_table = new JLabel("Symbol Table:");
		label_table.setForeground(Color.WHITE);
		label_table.setBounds(470, 430, 80, 15);
		Pane.add(label_table);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mnOpen = new JMenuItem("Select source file");
		mnFile.add(mnOpen);
		mnOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				
				int result = explorer.showOpenDialog(null);
				
				if(result == JFileChooser.CANCEL_OPTION)
					explorer.cancelSelection();
				
				else {
					code = "";
					String temp;
					
					File source = explorer.getSelectedFile();
					
					try {
						BufferedReader input = new BufferedReader(new FileReader(source));
						
						try {
							while((temp = input.readLine()) != null) {
								
								code += temp+"\n";
							}
							paper.setText(code);
							
							input.close();
						}
						catch (IOException e) { errors_field.setText(errors_field.getText()+"Eror ocurred in reading file!"+"\n"); }
					}
					catch (IOException e) { errors_field.setText(errors_field.getText()+"Can not open file!"+"\n"); }
				}
			}});
		
		JMenuItem mnExit = new JMenuItem("Exit");
		mnFile.add(mnExit);
		mnExit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				System.exit(0);
			}});
		JMenu mnRun = new JMenu("Run");
		menuBar.add(mnRun);
		
		JMenuItem mnParse = new JMenuItem("Parse");
		mnParse.setEnabled(false);
		mnParse.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				parser.parse(scanner.tokens);
				showSteps();
				showTree();
			}});
		JMenuItem mnSP = new JMenuItem("Scan & Parse");
		mnSP.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				result_field.setText(scanner.scan(paper.getText()));
				String e = "";
				errors_field.setText("");
				for(int i=0 ; i<scanner.error.size() ; i++)
					e += scanner.error.elementAt(i)+"\n";
				
				errors_field.setText(e);
				
				String s = "";
				for(int i=0 ; i<scanner.symbolTable[0].size() ; i++)
					s += scanner.symbolTable[0].elementAt(i)+
					"\t"+scanner.symbolTable[1].elementAt(i)+
					"\t"+scanner.symbolTable[2].elementAt(i)+
					"\t"+scanner.symbolTable[3].elementAt(i)+"\n";
				
				table_field.setText(s);
				
				result_field.setText(result_field.getText()+"\n"+scanner.message);
				
				parser.parse(scanner.tokens);
				showSteps();
				showTree();
			}});
		mnRun.add(mnScan);
		mnScan.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				result_field.setText(scanner.scan(paper.getText()));
				String e = "";
				errors_field.setText("");
				for(int i=0 ; i<scanner.error.size() ; i++)
					e += scanner.error.elementAt(i)+"\n";
				
				errors_field.setText(e);
				
				String s = "";
				for(int i=0 ; i<scanner.symbolTable[0].size() ; i++)
					s += scanner.symbolTable[0].elementAt(i)+
					"\t"+scanner.symbolTable[1].elementAt(i)+
					"\t"+scanner.symbolTable[2].elementAt(i)+
					"\t"+scanner.symbolTable[3].elementAt(i)+"\n";
				
				table_field.setText(s);
				
				result_field.setText(result_field.getText()+"\n"+scanner.message);
				mnParse.setEnabled(true);
			}});
		mnRun.add(mnParse);
		mnRun.add(mnSP);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mnAbout = new JMenuItem("About");
		mnHelp.add(mnAbout);
		mnAbout.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
				JOptionPane.showMessageDialog(null,
				"Programmer: Seyed abbas aghaei\nSN: 9319753\nCompiler project\nLanguage: Simple Calculator\n26/3/1396");
			}});
		
	}
	
	void showSteps() {
		
		JFrame stepsFrame = new JFrame("Parse Steps");
		stepsFrame.setType(Type.UTILITY);
		stepsFrame.setResizable(false);
		stepsFrame.setBounds(300, 40, 400, 500);
		stepsFrame.setLayout(null);
		stepsFrame.getContentPane().setBackground(Color.gray);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 380, 340);
		stepsFrame.add(scrollPane);
		
		Object[] names = { "Stack","Next Token","Rule" };
		Object[][] data = new Object[parser.steps.size()/3][3];
		
		for(int i=0 , k=0 ; i<data.length ; i++)
			for(int j=0 ; j<3 ; j++, k++)
				data[i][j] = parser.steps.elementAt(k);
		
		JTable table = new JTable(data, names);
		scrollPane.setViewportView(table);
		stepsFrame.setVisible(true);
		
		JLabel label_errors = new JLabel("Errors:");
		label_errors.setBounds(5, 345, 60, 15);
		stepsFrame.add(label_errors);
		
		JScrollPane scrollPane3 = new JScrollPane();
		scrollPane3.setBounds(5, 360, 380, 100);
		scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		stepsFrame.add(scrollPane3);
		
		JTextPane errors_field = new JTextPane();
		errors_field.setBackground(Color.red.darker());
		errors_field.setForeground(Color.white);
		errors_field.setEditable(false);
		scrollPane3.setViewportView(errors_field);
		String e = "";
		for(int i=0 ; i<parser.error.size() ; i++)
			e += parser.error.elementAt(i)+"\n";
		
		errors_field.setText(e);
	}
	
	void showTree() {
		
		JFrame treeFrame = new JFrame("Parse Tree");
		treeFrame.setType(Type.UTILITY);
		treeFrame.setResizable(false);
		treeFrame.setBounds(700, 100, 400, 380);
		treeFrame.setLayout(null);
		treeFrame.getContentPane().setBackground(Color.gray);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 380, 340);
		treeFrame.add(scrollPane);
		
		JTree tree = new JTree();
		scrollPane.setViewportView(tree);
		treeFrame.setVisible(true);
	}
}
