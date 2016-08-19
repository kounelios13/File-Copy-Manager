package gui;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
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
import utils.Controller;
import utils.FileDrop;
import utils.FileHandler;
import utils.PreferencesManager;
import utils.ProgramState;
@SuppressWarnings({"serial", "static-access","unused"})
public class FileCopyManager extends JFrame {
	private Controller controller = new Controller();
	private StatusFrame status = new StatusFrame();
	private FileHandler fHandler = new FileHandler();
	private PreferencesManager pManager = new PreferencesManager(this);
	private Message msg = new Message();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File"), editMenu = new JMenu("Edit");
	private JMenuItem saveList = new JMenuItem("Save queue"),
			exit = new JMenuItem("Exit"),
			loadList = new JMenuItem("Load queue"),
			openAppDirectory = new JMenuItem("Open app folder"),
			showPreferences = new JMenuItem("Preferences"),
			exportPreferences = new JMenuItem("Export Preferences"),
			deleteApp=new JMenuItem("Delete app settings"),
			restartApp=new JMenuItem("Restart Application");
	private JCheckBoxMenuItem allowDuplicatesOption=new JCheckBoxMenuItem("Allow dupliactes in list");
	private File selectedFile = null;
	private String destinationPath = null;
	private ArrayList<File> files = new ArrayList<>();
	private JPanel panel = new JPanel();
	private JFileChooser chooser = new JFileChooser();
	private JButton addFiles, selectDestination, copyFile, copyFiles,
			deleteFile, deleteAll, openDestinationFolder,stopCopy;
	private JComboBox<String> fileNames;
	private DefaultComboBoxModel<String> model;
	private int selectedFileIndex = -1;
	private JTextField dragPanel = new JTextField(20);
	private JLabel dragLabel;
	String sep = File.separator;
	private File listFile = new File("app"+PreferencesManager.sep+"userList.dat");
	private boolean allowDuplicates = false;
	private Thread[] copyThreads = new Thread[2];
	private boolean isNull(Object...t){
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
	public void showFiles() {
		fileNames.setVisible(!files.isEmpty());
	}
	public void restart(){
		//First close the current instance of the program
		this.dispose();
		//and create a new instance
		new FileCopyManager();
	}
	@SuppressWarnings("deprecation")
	private void initUIElements() {
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
		addFiles = new JButton("Add files to copy");
		copyFiles = new JButton("Copy all files");
		deleteFile = new JButton("Delete file from list");
		selectDestination = new JButton("Select Destination Folder");
		deleteAll = new JButton("Delete all files from list");
		openDestinationFolder = new JButton("Open Destination Folder");
		openDestinationFolder.addActionListener((e)->controller.openDestination(destinationPath));
		openAppDirectory.addActionListener((e)->controller.openAppDirectory());
		stopCopy = new JButton("Stop copy operations");
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
					}
					status.dispose();
				});
				copyThreads[1].start();
			}
			catch(Exception ee){
				msg.error(panel, "Error occured.See log file for more");
				fHandler.log(ee.getMessage());
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
			if(index == -1)
				return;
			model.removeElementAt(index);
			files.remove(index);		
			showFiles();
			allowEdits();
		});
		saveList.addActionListener((e) -> {
			File dir = listFile.getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			if (!listFile.exists()) {
				try {
					listFile.createNewFile();
				} catch (Exception e1) {
					msg.error(panel, "Cannot save list.");
					fHandler.log(e1.getMessage());
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
			selectedFile = state.getSelectedFile();
			selectedFileIndex = state.getSindex();
			destinationPath = state.getPath();
			fileNames.setSelectedIndex(selectedFileIndex);
			allowEdits();
			showFiles();
			this.pack();
		});
		showPreferences.addActionListener((e) ->pManager.editPreferences());
		exit.addActionListener((e)->System.exit(0));
		exportPreferences.addActionListener((e)->pManager.exportSettings());
		deleteApp.addActionListener((e)->pManager.deleteAppSettings());
		restartApp.addActionListener((e)->restart());
		new FileDrop(dragPanel,(e)->{
			/* Prevent application from freezing
			* When dragging many files */
			new Thread(()->{
				for(File f:e){
					if(!allowDuplicates)
						if(files.indexOf(f)!= -1)
							continue;
					files.add(f);
					model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
				}
				curFrame.pack();
				allowEdits();
				showFiles();
			}).start();
		});	
		new FileDrop(this,(e)->{
			/* Prevent application from freezing
			* When dragging many files */
			new Thread(()->{
				for(File f:e){
					if(!allowDuplicates)
						if(files.indexOf(f)!= -1)
							continue;
					files.add(f);
					model.addElement(f.getName()+(f.isDirectory()?" (Folder)":""));
				}
				curFrame.pack();
				allowEdits();
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
				allowEdits();
			} else
				msg.error(null, "Operation cancelled by user", "Status");
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
		stopCopy.addActionListener((e)->{
			try{
				copyThreads[0].stop();
				copyThreads[1].stop();
			}
			catch(Exception ee){
			}
			status.dispose();
		});
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
		super(name == null ? "Copy Files" : name);
		initUIElements();
		this.setJMenuBar(menuBar);
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
		preload().setVisible(true);
		this.setSize(535, 391);
		this.setContentPane(panel);
		panel.add(dragPanel, "cell 3 8");
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	public FileCopyManager preload() {
		/*
		* See if we need to change the main UI(change colors or font size)
		* and if we need do it first and then show the app
		*/
		allowEdits();
		pManager.prepareUI();
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
				FileHandler.log(e.getMessage());
			}
			finally{
				new FileCopyManager();
			}
		 });
	}
}
