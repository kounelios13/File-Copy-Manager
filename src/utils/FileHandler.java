package utils;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static messages.Message.info;
import static messages.Message.error;
import gui.StatusFrame;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.apache.commons.io.FileUtils;
@SuppressWarnings({"unused"})
public class FileHandler{
	private Timer timer;
	public static String sep = File.separator + File.separator;
	private StatusFrame status = null;
	public static boolean isNull(Object... items){
		for(Object o:items)
			if(o==null)
				return true;
		return false;
	}
	public static boolean watchForUpdates(String fileName){
		File temp = new File(fileName);
		// If file is app//settings.dat
		// getParent() returns the directory of the file -> "app"
		Path target = Paths.get(temp.getParent());
		try{
			Boolean isFolder = (Boolean)Files.getAttribute(target,
                    "basic:isDirectory", NOFOLLOW_LINKS);
			if(!isFolder)
				throw new IllegalArgumentException("Path: " +target+ " is not a folder");
		}
		catch(Exception e){
			log(e);
		}
		FileSystem fs = target.getFileSystem();
		 try (WatchService service = fs.newWatchService()) {
            // We register the path to the service
            // We watch for creation events
            target.register(service, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE); 
            // Start the infinite polling loop
            WatchKey key = null;
            while (true) {
                key = service.take();
                // Dequeueing events
                Kind<?> kind = null;
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    // Get the type of the event
                    kind = watchEvent.kind();
                    if (OVERFLOW == kind) {
                        continue; // loop
                    }
                    @SuppressWarnings("unchecked")
					Path newPath = ((WatchEvent<Path>) watchEvent).context();
                   	if(kind == ENTRY_MODIFY)
                   		if(newPath.equals(fileName))
                   			return true;
                }
                if (!key.reset()) {
                    break; // loop
                }
            }
        } catch (IOException ioe) {
            log(ioe);
        } catch (InterruptedException ie) {
            log(ie);
        }
		return false;
	}
	public static void log(Throwable th){
		log(th.getMessage());
	}
	public static void log(String message){
		File logFile = new File("app"+sep+"log.txt"),
				dir	 = new File("app");
		/**
		 * Make sure that 'app' directory exists 
		 * before creating log file*/
		if(!dir.exists())
			dir.mkdirs();
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException exc) {
				error("IOException :"+exc);	
				return;
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));//Append to file
				StringBuilder str = new StringBuilder();
				str.append(System.lineSeparator()+"\t||||--------_Log Message_-------||||\n");
				str.append(System.lineSeparator()+message);
				writer.write(str.toString());
				writer.close();
			} catch (IOException exc) {
				/**
				* Here we can only output the error message 
				* that prevent us from creating a '.log' file
				*/
				error("IOException :"+exc.getMessage());
			}
	}
	public FileHandler(){
	}
	public FileHandler(StatusFrame sframe){
		this.status = sframe;
	}
	public void saveList(ProgramState ps,File destFile){
		if(isNull(destFile)){
			error(null, "Destination folder has not been selected", "Destination Empty");
			return;
		}
		if(isNull(ps.getFiles()) || ps.getFiles().isEmpty())
		{
			error(null,"No files have been selected","Empty list");
			return;
		}
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(destFile));
			out.writeObject(ps);
			out.close();
		} catch (FileNotFoundException exc) {
			log(exc);
		} catch (IOException exc) {
			error("IOException:"+exc);
			log(exc);
		}
		info(null, "List has been saved", "Status");
	}
	public String getNewName(File f,String dest){
		return dest+f.getName();
	}
	public void setStatusFrame(StatusFrame sf){
		status = sf;
	}
	public long getCopyProgress(File victim,String dest)
		throws Exception
	{
		/*
		 * This method should be used 
		 * while copying a file to update a progress bar 
		 * so as the user knows how much of the file is remaining to copy
		 * Maybe we should pass a JProgress Bar (or model)
		 * */
		/*
		 * Calculate the size of the file to copy
		 * */
		long initSize = victim.length();
		long copied = 0;
		boolean errorAppeared = false;
		/*
		 * Now create a file input stream to manipulate the file at the destination
		 * path(Get  size at a specific time via FileInputSream.available())
		 * */
		File output = new File(getNewName(victim,dest));
		FileInputStream fis;
		try {
			fis = new FileInputStream(output);
			copied = fis.available();
			fis.close();
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
			log(exc);
		}	
			/*
			 * Timer is not working 
			 * replace with java.util.timer
			 * http://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds
			 * */
		return copied;
	}		
	private boolean copySingleFile(File f,String dest,boolean log){
		String fileName = f.getName();
		Path from = Paths.get(f.getAbsolutePath());
		Path to = Paths.get(dest+sep+fileName);
		CopyOption[] options = new CopyOption[]{
			      StandardCopyOption.REPLACE_EXISTING,
			      StandardCopyOption.COPY_ATTRIBUTES
			    }; 
		try{
			Files.copy(from, to, options);
		}
		catch(IOException io){
			error("File "+fileName+" could not be copied to "+to);
			log(io);
			return false;
		}
		if(log)
			if(new File(dest+sep+fileName).exists())
				info(fileName+" copied successfully");
			else
				error(fileName+" could not be copied");
		return true;
	}
	public boolean copyFile(File f,String dest,boolean log){
		return copySingleFile(f,dest,log);
	}
	public boolean copyDir(File dir,String dest){
		File destFolder = new File(dest+sep+dir.getName());
		if(!destFolder.exists())
			destFolder.mkdirs();
		try {
			FileUtils.copyDirectory(dir,destFolder);
		}
		catch (IOException e) {
			log(e);
			error("Exception during copying directory");
		}
		return true;
	}
	public boolean copy(File f,String dest,boolean log){
		return f.isDirectory()?copyDir(f,dest):copyFile(f,dest,log);
	}
	public String fileName(File f){
		return f.isFile()?f.getName()+(f.isDirectory()?" (Folder)":""):"";
	}
	public ProgramState loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage){
		ProgramState temp = new ResourceLoader(this).getAppState();
		//isNull causes NullPointerException
		if(isNull(temp)||isNull(temp.getFiles()))
			return null;
		if(!storage.isEmpty()){
			/*
			* While loading old files we found out that there are new files
			* so we ask the user if they want to keep them	 
			*/
			if(JOptionPane.showConfirmDialog(null,"Do you want to keep old files?")!=JOptionPane.OK_OPTION){
				mod.removeAllElements();
				storage.clear();
			}
		}
		for(File f:temp.getFiles()){
			storage.add(f);
			mod.addElement(fileName(f));				
		}
		return temp;
	}
	public void openDestination(String dPath) {
		if (isNull(dPath)) {
			error(null, "No folder selected","Missing destination folder");
			return;
		}
		try {
			Desktop.getDesktop().open(new File(dPath));
		} catch (Exception e1) {
			error("Could not open destination file");
			log(e1);
		}	
	}
	public void openAppDirectory() {
		File dir = new File("app");
		if(!dir.exists()){
			error("No app folder has been created in your system.");
			return;
		}
		try {
			Desktop.getDesktop().open(dir);
		} catch (Exception e1) {
			error("Could not open app folder");
			log(e1);
		}
	}
}
