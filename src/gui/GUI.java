package gui;
import java.awt.Desktop;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import messages.Message;
import net.miginfocom.swing.MigLayout;
import utilities.*;
@SuppressWarnings({"serial","static-access","unused"})
public class GUI extends JFrame{
	Controller controller = new Controller();
	private FileHandler fHandler = new FileHandler();
	private PreferencesManager pManager =new PreferencesManager(this,null);
	private Message msg=new Message();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File"),editMenu =new JMenu("Edit");
	private JMenuItem saveList=new JMenuItem("Save queue"),loadList=new JMenuItem("Load queue"),showSettings = new JMenuItem("Preferences");
	private File selectedFile = null;
	private String destinationPath;
	private ArrayList<File> files = new ArrayList<>();
	private JPanel panel = new JPanel();
	private JFileChooser chooser = new JFileChooser(); 
	private JButton addFiles,selectDestination,copyFile,copyFiles,deleteFile,deleteAll,openDestinationFolder;
	private JComboBox<String> fileNames;
	private DefaultComboBoxModel<String> model;
	private boolean loaded =false;
	private int copied=0,selectedFileIndex;
	private JTextField dragPanel = new JTextField(20);
	private JLabel dragLabel;
	private File listFile = new File("app\\userList.dat");
	private boolean initiated = false;
	private void init(){
		fileMenu.add(saveList);
		fileMenu.add(loadList);
		editMenu.add(showSettings);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		addFiles = new JButton("Add files to copy");
		copyFiles = new JButton("Copy all files");
		deleteFile = new JButton("Delete file from list");
		selectDestination = new JButton("Select Destination Folder");
		openDestinationFolder = new JButton("Open Destination Folder");
		deleteAll = new JButton("Delete all files from list");
		dragLabel = new JLabel("Drag files  here");
		model = new DefaultComboBoxModel<String>();
		JFrame curFrame = this;
		fileNames = new JComboBox<String>(model);
		fileNames.setVisible(false);
		addFiles.addActionListener((e)->{
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setDialogTitle("Select files to copy");
			int r= chooser.showOpenDialog(panel);
			if(r != JFileChooser.APPROVE_OPTION)
				return;
			for(File f:chooser.getSelectedFiles())
			{
				files.add(f);
				String name = f.getName()+ (f.isDirectory()?" (Folder)":"");
				model.addElement(name);				
			}		
			selectedFile = files.get(0);
			selectedFileIndex = 0;
			fileNames.setSelectedIndex(0);
			fileNames.setVisible(files.size() > 0);
			this.pack();
		});
		fileNames.addActionListener((e)->{
			if(files.size() == 1)
				selectedFile = files.get(0);
			else
			{
				selectedFileIndex = fileNames.getSelectedIndex();
				selectedFile =selectedFileIndex != -1 ? files.get(selectedFileIndex):null;
			}
			//System.out.println(selectedFile != null?selectedFile.getName():"No name");
		});
		copyFile = new JButton("Copy selected file");
		copyFile.addActionListener((e)->{
			if(destinationPath == null ||  selectedFile==null)
			{
				if(selectedFile == null)
					msg.error(null,"Please select a file first.","No file selected");
				else
					msg.error(null,"Please select a directory","No directory selected");
				return;
			}
			fHandler.copy(selectedFile,destinationPath);
		});

		copyFiles.addActionListener((e)->{
			if(destinationPath == null || files.size() == 0)
			{
				if(files.size()==0)
					JOptionPane.showMessageDialog(null,"No files to copy","Empty file list",JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(null,"Please Select a directory","No directory selected",JOptionPane.ERROR_MESSAGE);
				return;
			}
			/*if(!destinationPath.isEmpty()){
				System.out.println("Dir is not empty");
			}*/
			
			try
			{
				for(File f:files){
					fHandler.copy(f,destinationPath);	
				}		
			}
			catch(Exception e1){
				fHandler.log(e1.getMessage());
			}
			
		});
	
		deleteFile.addActionListener((e)->{
			//Todo 
			//check before execute if selected file is null 
			if(selectedFile == null)
			{
				msg.error(null, "No file is selected","Error");
				return;
			}
			boolean go = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete the selected item from the list?") ==JOptionPane.OK_OPTION;
			if(go){
				int index  = fileNames.getSelectedIndex();
				model.removeElementAt(index);
				files.remove(index);
			}
			
			fileNames.setVisible(files.size() > 0);
		});
		saveList.addActionListener((e)->{
			File dir = listFile.getParentFile();
			if(!dir.exists())
				dir.mkdirs();
			if(!listFile.exists()){
				try {
					listFile.createNewFile();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					msg.error(panel, "Cannot save list.", "Error");
					fHandler.log(e1.getMessage());
				}
			}
			ProgramState ps = new ProgramState(files,selectedFileIndex,destinationPath);
			controller.saveList(ps,listFile);			
		});
		loadList.addActionListener((e)->{
			//Hack to avoid ArrayOutOfBoundsException
			//When you start the program and press load once then press save and again load an exception is being thrown
			if(loaded)
				return;
			loaded = true;
			//Hack ends here
			try {
				model.removeAllElements();
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(listFile));
				ProgramState state  = (ProgramState)in.readObject();
				files = state.getFiles();
				selectedFile = state.getSelectedFile();
				for(File f:files)
				{
					
					String name = f.getName() +(f.isDirectory()?" (Folder)":"");
					model.addElement(name);
				}
				selectedFileIndex = state.getSindex();
				destinationPath = state.getPath();
				fileNames.setSelectedIndex(selectedFileIndex);
				in.close();
				this.pack();
				//state.restore(files, selectedFile, destinationPath, fileNames, model);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				msg.error(panel, "Cannot read fileList.dat","Error");
				e1.printStackTrace();
				return;			
			}
			finally{
				fileNames.setVisible(files.size() > 0);
			}
		});
		showSettings.addActionListener((e)->{
			pManager.editPreferences();
		});
		new FileDrop(dragPanel,new FileDrop.Listener() {			
			@Override
			public void filesDropped(File[] draggedFiles) {
				try{
					for(File f:draggedFiles){
						//Check if any file dropped here is a directory to get its inner files
						/*handler.customSearch(f,files,model);*/
						files.add(f);
						String name = f.isDirectory()?" (Folder)":"";
						model.addElement(f.getName()+name);
						/*deepSearch may cause nullpointerexception if too many files are passed*/
					}
				}
				catch(NullPointerException n){
					System.out.println("Hit an end.");
				}
				finally{
					fileNames.setVisible(files.size() > 0);
					curFrame.pack();
					System.out.println("Total files:"+files.size());
				}
				//Can't use this.pack()
				//cause 'this' refers to FileDrop class 	
			}
		});
	
		deleteAll.addActionListener((e)->{
			if(files.size() < 1){
				msg.info(panel, "There are no files to remove from list","Warning");
				return;
			}
			boolean go = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete tall files from list?") ==JOptionPane.OK_OPTION;
			if(go){
				files.clear();
				model.removeAllElements();
				fileNames.setVisible(false);
				msg.info(panel, "Files removed from list succcessfully","Status");
				selectedFile = null;
				selectedFileIndex = -1;				
			}
			else
				msg.info(null,"Operation cancelled by user","Status");
					
		});
		selectDestination.addActionListener((e)->{
			chooser.setDialogTitle("Select destination folder");
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.showOpenDialog(panel);
			File selected = chooser.getSelectedFile();
			if(selected != null){
				destinationPath = selected.getAbsolutePath();
			}
		});
		openDestinationFolder.addActionListener((e)->{
			if(selectedFile == null){
				msg.error(panel, "No folder selected", "Missing destination folder");
				return;
			}
			try {
				Desktop.getDesktop().open(new File(destinationPath));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				msg.error(panel, "Could not open destination file", "Error");
			}
		});
	}
	public ArrayList<JLabel> getLabels() {
		// TODO Auto-generated method stub
		JLabel[] labels = {dragLabel};
		return new ArrayList<JLabel>(Arrays.asList(labels));
	}	
	public ArrayList<JButton> getButtons(){
		/*if(!initiated){
			init();
			initiated = true;
		}
		else
			System.out.println("Already init()");*/
		JButton[] array = {addFiles,selectDestination,copyFile,copyFiles,deleteFile,deleteAll,openDestinationFolder};
		ArrayList<JButton> btns = new ArrayList<JButton>(Arrays.asList(array));
		return btns;
	}
	public GUI(String name){
		super(name == null ?"Copy Files":name);
		init();
		this.setJMenuBar(menuBar);
		panel.setLayout(new MigLayout("", "[113px][28px,grow][117px,grow][][][]", "[23px][][][][][][grow][][][grow]"));	
		panel.add(addFiles, "cell 0 0,alignx left,aligny top");
		panel.add(fileNames, "cell 1 0,alignx left,aligny center");
		panel.add(copyFiles, "cell 3 0");
		panel.add(copyFile, "cell 0 2,alignx left,aligny top");
		panel.add(openDestinationFolder, "cell 0 6");
		panel.add(selectDestination, "cell 0 5");
		panel.add(deleteAll, "cell 3 5");
		panel.add(deleteFile, "cell 3 2");
		panel.add(dragLabel, "cell 3 8");
		panel.add(dragPanel, "flowx,cell 3 9");
		preload().setVisible(true);
		this.setSize(535,391);
		this.setContentPane(panel);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public GUI preload(){
		pManager.loadPreferences();
		return this;
	}
	public GUI(){
		this(null);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		GUI gui = new GUI();
	}
	
}
