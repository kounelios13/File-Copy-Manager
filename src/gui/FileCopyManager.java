package gui;
import java.awt.Color;
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
import messages.Message;
import net.miginfocom.swing.MigLayout;
import utils.Controller;
import utils.FileDrop;
import utils.FileHandler;
import utils.PreferencesManager;
import utils.ProgramState;
import utils.ResourceLoader;
@SuppressWarnings({"all","serial", "static-access"})
public class FileCopyManager extends View{
	public static String appName  = "File Copy Manager v1.6.4.0";
	private PreferencesManager pManager 		    = new PreferencesManager(this);
	private JCheckBoxMenuItem allowDuplicatesOption = new JCheckBoxMenuItem("Allow dupliactes in list");
	private Controller controller = new Controller();
	private StatusFrame status 	  = new StatusFrame();
	private FileHandler fHandler  = new FileHandler(status);	
	private Message 	 msg   	  = new Message();
	private JMenuBar menuBar   	  = new JMenuBar();
	private JMenu   fileMenu   	  = new JMenu("File"),
				    editMenu   	  = new JMenu("Edit");
	private JMenuItem saveList 	  = new JMenuItem("Save queue"),
			exit 			   	  = new JMenuItem("Exit"),
			loadList           	  = new JMenuItem("Load queue"),
			openAppDirectory   	  = new JMenuItem("Open app folder"),
			showPreferences    	  = new JMenuItem("Preferences"),
			exportPreferences  	  = new JMenuItem("Export Preferences"),
			deleteApp          	  = new JMenuItem("Delete app settings"),
			restartApp		   	  = new JMenuItem("Restart Application");
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
	private File listFile = new File("app"+PreferencesManager.sep+"userList.dat");
	private boolean allowDuplicates = false;
	private Thread[] copyThreads = new Thread[2];
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
			}).start();
		});	
		new FileDrop(this,(e)->{
			/* Prevent application from freezing
			* When dragging many files */
			new Thread(()->{
				updateList(e);
			}).start();
		});
	}
	public void updateList(File[] e){
		for(File f:e){
			if(!allowDuplicates)
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
	public void restart(){
		//First close the current instance of the program
		this.dispose();
		//and create a new instance
		new FileCopyManager(appName);
	}
	private void initUIElements() {
		this.setJMenuBar(menuBar);
		fileMenu.add(saveList);
		fileMenu.add(loadList);
		fileMenu.addSeparator();
		fileMenu.add(openAppDirectory);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		editMenu.add(showPreferences);
		editMenu.add(exportPreferences);
		editMenu.add(restartApp);
		editMenu.addSeparator();
		editMenu.add(deleteApp);
		editMenu.addSeparator();
		allowDuplicatesOption.addActionListener((e)->allowDuplicates = allowDuplicatesOption.isSelected());
		editMenu.add(allowDuplicatesOption);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		addFiles	 	   	  = new JButton("Add files to copy");
		copyFiles 	 	   	  = new JButton("Copy all files");
		deleteFile 	 	   	  = new JButton("Delete file from list");
		selectDestination  	  = new JButton("Select Destination Folder");
		deleteAll 		   	  = new JButton("Delete all files from list");
		openDestinationFolder = new JButton("Open Destination Folder");
		openDestinationFolder.addActionListener((e)->controller.openDestination(destinationPath));
		openAppDirectory.addActionListener((e)->controller.openAppDirectory());
		stopCopy = new JButton("Stop copy operations");
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
			String message = destinationPath== null && selectedFile == null?"Selecte at least a file and a destination folder "
					:selectedFile == null?" Select at least one file to copy":"Select a directory to copy selected file(s) in";
			if (isNull(destinationPath,selectedFile)) {
				msg.error(panel, message);
				return;
			}
			copyThreads[0]=new Thread(()->{
				fHandler.copy(selectedFile, destinationPath,true);
			});
			copyThreads[0].start();
		});
		copyFiles.addActionListener((e) -> {
			fHandler.setStatusFrame(status);
			if(isNull(destinationPath))
				msg.error(panel, "Please select a destination folder","No destination folder selected");
			try{
				copyThreads[1]=
				new Thread(()->{
					for(File f:files){
						int curIndex = files.indexOf(f);
						fileNames.setSelectedIndex(curIndex);
						status.text(f.getName()).showStatus();
						fHandler.copy(f,destinationPath,false);
						/*if(f.isFile()){
							updateProgress(f);
						}*/
					}
					status.dispose();
				});
				copyThreads[1].start();
			}
			catch(Exception ee){
				msg.error(panel, "Error occured.See log file for more");
				fHandler.log(ee);
			}
			finally{
				status.dispose();
			}
		});
		deleteFile.addActionListener((e) -> {
			if (isNull(selectedFile)) {
				msg.error(null, "No file is selected", "Error");
				return;
			}		
			int index = fileNames.getSelectedIndex();
			/**
			 * If index is -1
			 * it means that either there are no files
			 * */
			if(index == -1)
				return;
			model.removeElementAt(index);
			files.remove(index);		
			showFiles();
			allowEdits();
		});
		saveList.addActionListener((e) -> {
			if(isNull(destinationPath)){
				msg.error(panel, "If you want to save your list please select a destination folder for your files.");
				return;
			}
			File dir = listFile.getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			if (!listFile.exists()) {
				try {
					listFile.createNewFile();
				} catch (Exception e1) {
					msg.error(panel, "Cannot save list.");
					fHandler.log(e1);
				}
			}
			ProgramState ps = new ProgramState(files, selectedFileIndex,destinationPath,allowDuplicates);
			controller.saveList(ps, listFile);
		});
		loadList.addActionListener((e) -> {
			ProgramState state = controller.loadList(model, files);
			if(state != null){
				allowDuplicates = state.allowDuplicates();
				allowDuplicatesOption.setSelected(allowDuplicates);
			}
			else
				return;
			if(ResourceLoader.validateFiles(files)){
				
				/**
				 * More of a warning here since there is not an error
				 * */
				msg.warning(panel, "Some of the files you saved last time do not exist and have been deleted from your list.");
			}
			createList(files);
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
			try{
				fileNames.setSelectedIndex(selectedFileIndex);
			}
			catch(IllegalArgumentException exc){
				if(!files.isEmpty()){
					fileNames.setSelectedIndex(0);
					selectedFileIndex = 0;
				}
				fHandler.log(exc);				
			}
			allowEdits();
			showFiles();
			this.pack();
		});
		showPreferences.addActionListener((e) ->pManager.editPreferences());
		exit.addActionListener((e)->System.exit(0));
		exportPreferences.addActionListener((e)->pManager.exportSettings());
		deleteApp.addActionListener((e)->pManager.deleteAppSettings());
		restartApp.addActionListener((e)->restart());
		deleteAll.addActionListener((e) -> {
			if (files.isEmpty()) {
				msg.info(panel,"There are no files to remove from list","Warning");
				return;
			}
			boolean go = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete tall files from list?") == JOptionPane.OK_OPTION;
			if (go) {
				files.clear();
				model.removeAllElements();
				fileNames.setVisible(false);
				selectedFile 		= null;
				selectedFileIndex 	= -1;
				msg.info(panel,"Files removed from list succcessfully","Status");
				allowEdits();
			} else
				msg.error(null, "Operation cancelled by user");
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
				msg.error(panel,"Invalid destination");
		});
		initDragAreas();
		stopCopy.setVisible(false);
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
		 */
		super((isNull(name) ? appName : name),535,391);
		initUIElements();
		panel.setBackground(Color.white);
		panel.setLayout(new MigLayout("", "[113px][28px,grow][117px,grow][][]", "[23px][][][][][][][grow][][][][][grow]"));
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
		/*
		 * By setting resizable to false 
		 * we stop user from resizing the main UI frame**/
		this.setResizable(false);
		preload();
	}
	public void preload() {
		/*
		* See if we need to change the main UI(change colors or font size)
		* and if we need do it first and then show the app
		*/
		allowEdits();
		pManager.prepareUI();
		toggleUI();
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
				FileHandler.log(e);
			}
			finally{
				new FileCopyManager(appName);
			}
		 });
	}
}
