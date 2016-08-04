package gui;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import messages.Message;
import net.miginfocom.swing.MigLayout;
import utilities.Controller;
import utilities.FileDrop;
import utilities.FileHandler;
import utilities.PreferencesManager;
import utilities.ProgramState;
import utilities.ResourceLoader;
@SuppressWarnings({"serial", "static-access"})
public class FileCopyManager extends JFrame {
	Controller controller = new Controller();
	StatusFrame status = new StatusFrame();
	private FileHandler fHandler = new FileHandler();
	private ResourceLoader rc = new ResourceLoader(fHandler);
	private PreferencesManager pManager = new PreferencesManager(this);
	private Message msg = new Message();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File"), editMenu = new JMenu("Edit");
	private JMenuItem saveList = new JMenuItem("Save queue"),
			exit = new JMenuItem("Exit"),
			loadList = new JMenuItem("Load queue"),
			showPreferences = new JMenuItem("Preferences"),
			exportPreferences = new JMenuItem("Export Preferences"),
			deleteApp=new JMenuItem("Delete app settings"),
			restartApp=new JMenuItem("Restart Application");
	private File selectedFile = null;
	private String destinationPath;
	private ArrayList<File> files = new ArrayList<>();
	private JPanel panel = new JPanel();
	private JFileChooser chooser = new JFileChooser();
	private JButton addFiles, selectDestination, copyFile, copyFiles,
			deleteFile, deleteAll, openDestinationFolder;
	private JComboBox<String> fileNames;
	private DefaultComboBoxModel<String> model;
	private int selectedFileIndex;
	private JTextField dragPanel = new JTextField(20);
	private JLabel dragLabel;
	String sep = File.separator;
	private File listFile = new File("app"+PreferencesManager.sep+"userList.dat");
	public void showFiles() {
		fileNames.setVisible(files.size() > 0);
	}
	public void restart(){
		//First close the current instance of the program
		this.dispose();
		//and create a new instance
		new FileCopyManager();
	}
	private void initUIElements() {
		fileMenu.add(saveList);
		fileMenu.add(loadList);
		fileMenu.add(exit);
		editMenu.add(showPreferences);
		editMenu.add(exportPreferences);
		editMenu.add(restartApp);
		editMenu.add(deleteApp);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		addFiles = new JButton("Add files to copy");
		copyFiles = new JButton("Copy all files");
		deleteFile = new JButton("Delete file from list");
		selectDestination = new JButton("Select Destination Folder");
		openDestinationFolder = new JButton("Open Destination Folder");
		deleteAll = new JButton("Delete all files from list");
		model = new DefaultComboBoxModel<String>();
		JFrame curFrame = this;
		fileNames = new JComboBox<String>(model);
		fileNames.setVisible(false);
		addFiles.addActionListener((e) -> {
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setDialogTitle("Select files to copy");
			int r = chooser.showOpenDialog(panel);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			for (File f : chooser.getSelectedFiles()) {
				files.add(f);
				String name = f.getName()
						+ (f.isDirectory() ? " (Folder)" : "");
				model.addElement(name);
			}
			selectedFile = files.get(0);
			selectedFileIndex = 0;
			fileNames.setSelectedIndex(0);
			fileNames.setVisible(files.size() > 0);
			this.pack();
		});
		fileNames.addActionListener((e) -> {
			selectedFileIndex = files.size()==1?0:fileNames.getSelectedIndex();
			selectedFile = selectedFileIndex == -1 ?null:files.get(selectedFileIndex);
		});
		copyFile = new JButton("Copy selected file");
		copyFile.addActionListener((e) -> {
			String message = destinationPath== null && selectedFile == null?"Selecte at least a file and a destination folder "
					:selectedFile == null?" Select at least one file to copy":"Select a directory to copy selected file(s) in";
			if (destinationPath == null || selectedFile == null) {
				msg.error(panel, message);
				return;
			}
			new Thread(()->{
				fHandler.copy(selectedFile, destinationPath,true);
			}).start();
		});

		copyFiles.addActionListener((e) -> {
			if(destinationPath == null)
				msg.error(panel, "Please select a destination folder","No destination folder selected");
			try{
				new Thread(()->{
					for(File f:files){
						int curIndex = files.indexOf(f);
						fileNames.setSelectedIndex(curIndex);
						status.text(f.getName()).showStatus();
						fHandler.copy(f,destinationPath,false);		
					}
					status.dispose();
				}).start();
			}
			catch(Exception ee){
				msg.error(panel, "Error occured.Se log file for more", "Error");
				fHandler.log(ee.getMessage());
			}
			finally{
				status.dispose();
			}
		});

		deleteFile.addActionListener((e) -> {
			if (selectedFile == null) {
				msg.error(null, "No file is selected", "Error");
				return;
			}		
			int index = fileNames.getSelectedIndex();
			if(index == -1)
				return;
			model.removeElementAt(index);
			files.remove(index);		
			fileNames.setVisible(files.size() > 0);
		});
		saveList.addActionListener((e) -> {
			File dir = listFile.getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			if (!listFile.exists()) {
				try {
					listFile.createNewFile();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					msg.error(panel, "Cannot save list.", "Error");
					fHandler.log(e1.getMessage());
				}
			}
			ProgramState ps = new ProgramState(files, selectedFileIndex,destinationPath);
			controller.saveList(ps, listFile);
		});
		loadList.addActionListener((e) -> {
			ProgramState state = rc.getAppState();
			boolean clearList = state !=  null;		
			// Hack ends here
			if(clearList){
					if(files.size() > 0){
						if(JOptionPane.showConfirmDialog(null, "There are new files added to the list.Do you want to keep them?") == JOptionPane.OK_OPTION){
							for(File f:state.getFiles()){
								files.add(f);
								model.addElement(f.getName()+(f.isDirectory()?" (Folder)":" "));
							}
						}
						else{
							model.removeAllElements();
							files = state.getFiles();
							for(File f:files)
								model.addElement(f.getName()+ (f.isDirectory()?" (Folder)":" "));
						}
					}//files.size() > 0
					else{
						files=state.getFiles();
						for(File f:files)
							model.addElement(f.getName()+ (f.isDirectory()?" (Folder)":" "));
					}
					selectedFile = state.getSelectedFile();
					selectedFileIndex = state.getSindex();
					destinationPath = state.getPath();
					fileNames.setSelectedIndex(selectedFileIndex);
					this.pack();
				}//clearList
				fileNames.setVisible(files.size() > 0);
			
		});
		showPreferences.addActionListener((e) ->pManager.editPreferences());
		exit.addActionListener((e)->System.exit(0));
		exportPreferences.addActionListener((e)->pManager.exportSettings());
		deleteApp.addActionListener((e)->pManager.deleteAppSettings());
		restartApp.addActionListener((e)->restart());
		new FileDrop(dragPanel,(e)->{
			//Prevent application from freezing
			//When dragging many files
			new Thread(()->{
				for(File f:e){
					files.add(f);
					model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
				}
				curFrame.pack();
				showFiles();
			}).start();
		});	
		new FileDrop(this,(e)->{
			//Prevent application from freezing
			//When dragging many files
			new Thread(()->{
				for(File f:e){
					files.add(f);
					model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
				}
				curFrame.pack();
				showFiles();
			}).start();
		});
		deleteAll.addActionListener((e) -> {
			if (files.size() < 1) {
				msg.info(panel,"There are no files to remove from list","Warning");
				return;
			}
			boolean go = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete tall files from list?") == JOptionPane.OK_OPTION;
			if (go) {
				files.clear();
				model.removeAllElements();
				fileNames.setVisible(false);
				msg.info(panel,"Files removed from list succcessfully","Status");
				selectedFile = null;
				selectedFileIndex = -1;
			} else
				msg.error(null, "Operation cancelled by user", "Status");

		});
		selectDestination.addActionListener((e) -> {
			chooser.setDialogTitle("Select destination folder");
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.showOpenDialog(panel);
			File selected = chooser.getSelectedFile();
			if (selected != null)
				destinationPath = selected.getAbsolutePath();
			else
				msg.error(panel,"Invalid destination");
		});
		openDestinationFolder.addActionListener((e) -> {
			if (destinationPath == null) {
				msg.error(panel, "No folder selected","Missing destination folder");
				return;
			}
			try {
				Desktop.getDesktop().open(new File(destinationPath));
			} catch (Exception e1) {
				msg.error(panel, "Could not open destination file", "Error");
				fHandler.log(e1.getMessage());
			}
		});
	}
	public JLabel[] getLabels() {
		JLabel[] labels = {dragLabel};
		return labels;
	}
	public JButton[] getButtons() {
		JButton[] array = {addFiles, selectDestination, copyFile, copyFiles,
				deleteFile, deleteAll, openDestinationFolder};
		return array;
	}
	public FileCopyManager(String name) {
		super(name == null ? "Copy Files" : name);
		initUIElements();
		this.setJMenuBar(menuBar);
		panel.setBackground(Color.white);
		panel.setLayout(new MigLayout("", "[113px][28px,grow][117px,grow][]", "[23px][][][][][][grow][][][][grow]"));
		panel.add(addFiles, "cell 0 0,alignx left,aligny top");
		panel.add(fileNames, "cell 1 0,alignx left,aligny center");
		panel.add(copyFiles, "cell 3 0");
		panel.add(copyFile, "cell 0 2,alignx left,aligny top");
		panel.add(openDestinationFolder, "cell 0 6");
		panel.add(selectDestination, "cell 0 5");
		panel.add(deleteAll, "cell 3 5");
		panel.add(deleteFile, "cell 3 2");
		dragLabel = new JLabel("Drag files  here");
		panel.add(dragLabel, "flowy,cell 3 6");
		preload().setVisible(true);
		this.setSize(535, 391);
		this.setContentPane(panel);
		panel.add(dragPanel, "cell 3 7");
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	public FileCopyManager preload() {
		/*
		* See if we need to change the main UI(change colors or font size)
		*and if we need do it first and then show the app
		*/
		if(pManager.settingsFile.exists())
			pManager.loadPreferences();
		return this;
	}
	public FileCopyManager() {
		this(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			 try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			}
			catch (Throwable e) {
				new FileHandler().log(e.getMessage());
			}
			finally{
				new FileCopyManager();
			}
		 });
	}
}
