package gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import extra.InfoPage;
import extra.StatusFrame;
import extra.XString;
import messages.Message;
import net.miginfocom.swing.MigLayout;
import serializable.ProgramState;
import utils.Controller;
import utils.FileDrop;
import utils.FileHandler;
import utils.PreferencesManager;
import utils.ResourceLoader;
@SuppressWarnings({"serial"})
//This is the main class of the program
//This is what the user sees when they run the program
public class FileCopyManager extends ApplicationScreen{
	public static String appName  = "File Copy Manager v1.6.5.4";
	public static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
	private PreferencesManager pManager 		    = new PreferencesManager(this);
	private JCheckBoxMenuItem allowDuplicatesOption = new JCheckBoxMenuItem("Allow duplicates in list");
	private Controller controller = new Controller();
	private FileHandler fHandler  = controller.getFileHandler();	
	private JMenuBar menuBar   	  = new JMenuBar();
	private JMenu   fileMenu   	  = new JMenu("File"),
				    editMenu   	  = new JMenu("Edit"),
				    aboutMenu	  = new JMenu("Help");
	private JMenuItem saveList 	  = new JMenuItem("Save queue"),
			exit 			   	  = new JMenuItem("Exit"),
			loadList           	  = new JMenuItem("Load queue"),
			openAppDirectory   	  = new JMenuItem("Open app folder"),
			showPreferences    	  = new JMenuItem("Preferences"),
			changeLookAndFeel	  = new JMenuItem("Change Look & Feel"),
			exportPreferences  	  = new JMenuItem("Export Preferences"),
			deleteApp          	  = new JMenuItem("Delete app settings"),
			restartApp		   	  = new JMenuItem("Restart Application"),
			showAboutInfo		  = new JMenuItem("About File Copy Manager");
	private File      selectedFile = null;
	private String destinationPath = null;
	private ArrayList<File> files  = new ArrayList<>();
	private JPanel panel = new JPanel();
	private JFileChooser chooser = new JFileChooser();
	private JButton addFiles, selectDestination, copyFile, copyFiles,
			deleteFile, deleteAll, openDestinationFolder,stopCopy;
	private JComboBox<String> fileNames;
	private DefaultComboBoxModel<String> model;
	private int selectedFileIndex = -1;
	private JTextField dragPanel = new JTextField(20);
	private JLabel dragLabel;
	private File listFile = new File("app/userList.dat");
	private boolean allowDuplicates = false;
	private Thread[] copyThreads = new Thread[2];
	private StatusFrame status = new StatusFrame(this);
	private ThemeChanger themeEditor = new ThemeChanger(this);
	protected Component[] getViewsToUpdate(){
		return new Component[]{this,pManager};
	}
	private static boolean isNull(Object...t){
		return FileHandler.isNull(t);
	}
	private void allowCopy(){
		boolean condition = !files.isEmpty() && destinationPath != null;
		copyFile.setEnabled(condition);
		copyFiles.setEnabled(condition);
		openDestinationFolder.setEnabled(destinationPath != null);
	}
	private void allowDelete(){
		boolean condition = !files.isEmpty();
		deleteFile.setEnabled(condition);
		deleteAll.setEnabled(condition);
	}
	private void allowEdits(){
		/*
		 * Check if we want to enable delete and copy buttons
		 */
		allowCopy();
		allowDelete();
	}
	private void createList(ArrayList<File> files){
		model.removeAllElements();
		for(File f:files)
			model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
		if(!files.isEmpty())
			fileNames.setSelectedIndex(selectedFileIndex);
	}
	private void initDragAreas(){
		new FileDrop(selectDestination,(e)->{
			for(File f:e)
			{
				if(f.isDirectory())
				{
					/*
					 * Find the first directory that was dragged*/
					destinationPath = f.getAbsolutePath();
					allowCopy();
					break;
				}
			}
		});
		new FileDrop(dragPanel,(e)->{
			/* Prevent application from freezing
			* When dragging many files */
			new Thread(()->{
				updateList(e);
				pack();
			}).start();
		});	
		new FileDrop(this,(e)->{
			/* Prevent application from freezing
			* When dragging many files */
			new Thread(()->{
				updateList(e);
				pack();
			}).start();
		});
	}
	@Override
	public String toString(){
		return "FileCopyManager";
	}
	public void updateLookAndFeel(String laf){
		try{
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(pManager);
		}
		catch(Exception e){
			FileHandler.log(e);
		}
	}
	public void updateList(File[] e){
		for(File f:e){
			if(!allowDuplicates)
				//The file already exists in the list.Do not add it again
				if(files.indexOf(f)!= -1)
					continue;
			files.add(f);
			model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
		}
		allowEdits();
		showFiles();
		this.pack();
	}
	public void showFiles() {
		fileNames.setVisible(!files.isEmpty());
	}
	public ApplicationScreen restart(){
		//First close the current instance of the program
		this.dispose();
		/**
		 * Dispose all Frames before restarting the application
		 * */
		for(Frame frame:Frame.getFrames())
			frame.dispose();
		//and create a new instance
		FileCopyManager fm = new FileCopyManager(appName);
		fm.toggleUI();
		return fm;
	}
	private JButton btn(String name){
		return new JButton(name);
	}
	protected void initUIElements() {
		//Create the theme changer in a new Thread
		SwingUtilities.invokeLater(()->{
			themeEditor = new ThemeChanger(this);
			themeEditor.setResizable(false);
		});
		this.setJMenuBar(menuBar);
		fileMenu.add(saveList);
		fileMenu.add(loadList);
		fileMenu.addSeparator();
		fileMenu.add(openAppDirectory);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		editMenu.add(changeLookAndFeel);
		editMenu.add(showPreferences);
		editMenu.add(exportPreferences);
		editMenu.add(restartApp);
		editMenu.addSeparator();
		editMenu.add(deleteApp);
		editMenu.addSeparator();
		aboutMenu.add(showAboutInfo);
		allowDuplicatesOption.addActionListener((e)->allowDuplicates = allowDuplicatesOption.isSelected());
		editMenu.add(allowDuplicatesOption);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		addFiles	 	   	  = btn("Add files to copy");
		copyFiles 	 	   	  = btn("Copy all files");
		deleteFile 	 	   	  = btn("Delete file from list");
		selectDestination  	  = btn("Select Destination Folder");
		deleteAll 		   	  = btn("Delete all files from list");
		openDestinationFolder = btn("Open Destination Folder");
		openDestinationFolder.addActionListener((e)->controller.openDestination(destinationPath));
		openAppDirectory.addActionListener((e)->controller.openAppDirectory());
		stopCopy = new JButton("Stop copy operations");
		stopCopy.setVisible(false);
		model = new DefaultComboBoxModel<String>();
		fileNames = new JComboBox<String>(model);
		fileNames.setVisible(false);
		addFiles.addActionListener((e) -> {
			chooser.setDialogTitle("Select files to copy");
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int r = chooser.showOpenDialog(panel);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			for (File f : chooser.getSelectedFiles()) {
				if(!allowDuplicates)
					if(files.indexOf(f) != -1)
						continue;
				files.add(f);
				String name = f.getName()
						+ (f.isDirectory() ? " (Folder)" : "");
				model.addElement(name);
			}
			selectedFile = files.get(0);
			selectedFileIndex = 0;
			fileNames.setSelectedIndex(0);
			allowEdits();
			showFiles();
			this.pack();
		});
		fileNames.addActionListener((e) -> {
			selectedFileIndex = files.isEmpty()?-1:fileNames.getSelectedIndex();
			selectedFile = selectedFileIndex == -1 ?null:files.get(selectedFileIndex);
		});
		copyFile = new JButton("Copy selected file");
		copyFile.addActionListener((e) -> {
			String message = destinationPath== null && selectedFile == null?"Select at least a file and a destination folder "
					:selectedFile == null?" Select at least one file to copy":"Select a directory to copy selected file(s) in";
			if (isNull(destinationPath,selectedFile)) {
				Message.error(panel, message);
				return;
			}
			/*
			* Show progress while copying a file
			**/
			status.setVisible(true);
			copyThreads[0]=new Thread(()->{
				status.update(selectedFile);
				//Since there is no progress bar displayed to the user
				//If there is only one file and is a directory
				//it takes a while to copy
				//Let the user know that the program actually copies the contents of the folder
				//By showing them the whole process
				if(files.size()==1 && selectedFile.isDirectory()) {
					for (File f : selectedFile.listFiles()) {
						status.update(f);
						fHandler.copy(f, destinationPath + "/" + selectedFile.getName(), false);
					}
				}
				else{
					fHandler.copy(selectedFile, destinationPath,true);
					// File may have been copied or an error occurred
					// No matter what hide progress
				}
				status.dispose();
			});
			copyThreads[0].start();
		});
		copyFiles.addActionListener((e) -> {
			if(isNull(destinationPath))
				Message.error(panel, "Please select a destination folder","No destination folder selected");
			//No need to start a new thread if there is nothing to copy
			if(files.isEmpty())
				return;
			try{
				copyThreads[1]=
				new Thread(()->{
					status.setVisible(true);
					//Since there is no progress bar displayed to the user
					//If there is only one file and is a directory
					//it takes a while to copy
					//Let the user know that the program actually copies the contents of the folder
					//By showing them the whole process
					if(files.size()==1 && selectedFile.isDirectory()){
						File[] contents = selectedFile.listFiles();
						for(File f:contents){
							status.update(f);
							fHandler.copy(f, destinationPath+"/"+selectedFile.getName(),false);
						}
					}		
					else{
						for(File f:files){
							int curIndex = files.indexOf(f);
							fileNames.setSelectedIndex(curIndex);
							status.update(f);
							fHandler.copy(f, destinationPath, false);
						}
					}
					status.dispose();
				});
				copyThreads[1].start();
			}
			catch(Exception ee){
				Message.error(panel, "Error occured.See log file for more");
				FileHandler.log(ee);
			}
			finally{
				status.toggleUI();
			}
		});
		deleteFile.addActionListener((e) -> {
			if (isNull(selectedFile)) {
				Message.error("No file is selected");
				return;
			}		
			int index = fileNames.getSelectedIndex();
			/***
			 * If index is -1
			 * it means that either there are no files
			 * or something is wrong
			 * */
			if(index == -1)
				return;
			model.removeElementAt(index);
			files.remove(index);		
			showFiles();
			allowEdits();
		});
		saveList.addActionListener((e) -> {
			if(isNull(destinationPath) || files.isEmpty()){
				XString err = new XString(files.isEmpty() && isNull(destinationPath)?
						"Please add some files and select a destination folder.":files.isEmpty()?
								"You haven't added any file.":"If you want to save your list please select"
									+" a destination folder for your files.");
				Message.error(err.toString());
				return;
			}
			ProgramState ps = new ProgramState(files, selectedFileIndex,destinationPath,allowDuplicates);
			controller.saveList(ps, listFile);
		});
		loadList.addActionListener((e) -> {
			ProgramState state = controller.loadList(model, files);
			if(isNull(state))
				return;
			allowDuplicates = state.allowDuplicates();
			allowDuplicatesOption.setSelected(allowDuplicates);
			ResourceLoader.checkFileExistence(files);
			/**
			 * While loading the list 
			 * check if the saved destination folder exists
			 * and if the saved selectedFile is still available to the application
			 * */
			boolean selectedFileExists = state.getSelectedFile().exists(),
					destinationExists  = new File(state.getPath()).exists();
			selectedFile 		= !selectedFileExists ? null:state.getSelectedFile();
			selectedFileIndex	= selectedFileExists  ? state.getSindex():-1;
			destinationPath		= destinationExists   ? state.getPath():null;
			createList(files);
			try{
				fileNames.setSelectedIndex(selectedFileIndex);
			}
			catch(IllegalArgumentException exc){
				if(!files.isEmpty()){
					fileNames.setSelectedIndex(0);
					selectedFileIndex = 0;
				}
				FileHandler.log(exc);				
			}
			allowEdits();
			showFiles();
			this.pack();
		});
		changeLookAndFeel.addActionListener((e)->themeEditor.activate());
		showPreferences.addActionListener((e) ->pManager.editPreferences());
		exit.addActionListener((e)->System.exit(0));
		exportPreferences.addActionListener((e)->pManager.exportSettings());
		deleteApp.addActionListener((e)->{
			pManager.deleteAppSettings();
			File dir = new File("app");
			//When deleting application and theme settings
			//Make sure to revert back to nimbus look and feel
			//To avoid glitches
			// fixes #19 and #21
			SwingUtilities.invokeLater(()->{
				if(!dir.exists()){
					try{
						UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
						SwingUtilities.updateComponentTreeUI(this);
						//If settings have been deleted restart the application
						restart();
					}
					catch(Exception exc){}
				}
			});
		});
		restartApp.addActionListener((e)->restart());
		showAboutInfo.addActionListener((e)->{
			SwingUtilities.invokeLater(()->new InfoPage());
		});
		deleteAll.addActionListener((e) -> {
			if (files.isEmpty()) {
				Message.warning(panel,"There are no files to remove from list");
				return;
			}
			boolean eraseList = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete all files from list?") == JOptionPane.OK_OPTION;
			if (eraseList) {
				files.clear();
				model.removeAllElements();
				fileNames.setVisible(false);
				selectedFile 		= null;
				selectedFileIndex 	= -1;
				Message.info(panel,"Files removed from list succcessfully");
				allowEdits();
			} else
				Message.error("Operation cancelled by user");
		});
		selectDestination.addActionListener((e) -> {
			chooser.setDialogTitle("Select destination folder");
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.showOpenDialog(panel);
			File selected = chooser.getSelectedFile();
			if (!isNull(selected)){
				destinationPath = selected.getAbsolutePath();
				allowEdits();
			}else
				Message.error(panel,"Invalid destination");
		});
		dragPanel.setEditable(false);
		initDragAreas();
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
		/**
		 * Arguments for super()
		 * String title ,int width , int height
		 * By setting resizable to false 
		 * we stop user from resizing the main UI frame
		 */
		super(appName,535,391,false);
		initUIElements();
		panel.setLayout(new MigLayout("", "[113px][28px,grow][117px,grow][][]", "[23px][][][][][][][grow][][][27.00][][-11.00,grow]"));
		panel.add(addFiles, "cell 0 0,alignx left,aligny top");
		panel.add(fileNames, "cell 1 0,alignx left,aligny center");
		panel.add(copyFiles, "cell 3 0");
		panel.add(copyFile, "cell 0 2,alignx left,aligny top");
		panel.add(selectDestination, "cell 0 5");
		panel.add(deleteAll, "cell 3 5");
		panel.add(deleteFile, "cell 3 2");
		panel.add(openDestinationFolder, "cell 0 6");
		panel.add(stopCopy, "cell 3 6");
		dragLabel = new JLabel("Drag files  here");
		panel.add(dragLabel, "flowy,cell 3 7");
		panel.add(dragPanel, "cell 3 8");
		this.setContentPane(panel);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		preload();
	}
	public void preload() {
		/*
		* See if we need to change the main UI(change colors or font size)
		* and if we need do it first and then show the app
		*/
		allowEdits();
		pManager.prepareUI();
		//toggleUI();
	}
	public FileCopyManager() {
		this(null);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			//Hackish way to overcome
			//the problem of partial apply of custom look and feel class
			//Create an invisible frame,restart it and then make it visible
			new FileCopyManager(appName).restart();
		 });
	}
}
