package utils;
import static messages.Message.error;
import static messages.Message.info;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
@SuppressWarnings({})
public class FileHandler{
	/**
	 * 
	 * See this web site for an alternative way to copy files while showing graphical progress
	 * http://filecopylibrary.sourceforge.net/
	 * */
	private static String sep = File.separator + File.separator;
	public static boolean isNull(Object... items){
		for(Object o:items)
			if(o==null)
				return true;
		return false;
	}
	public static void log(Throwable th){
		log(th);
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
	public void saveList(ProgramState ps,File destFile){
		if(isNull(destFile)){
			error("Destination folder has not been selected", "Destination Empty");
			return;
		}
		if(isNull(ps.getFiles()) || ps.getFiles().isEmpty())
		{
			error("No files have been selected","Empty list");
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
		info("List has been saved");
	}
	public String getDestinationName(File f,String dest){
		return dest+f.getName();
	}
	@Deprecated
	public long getCopyProgress(File victim,String dest)
		throws Exception
	{
		/*
		 * Return the progress
		 * **/
		//Deprecated
		//To be removed
		return 0;
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
	private String fileName(File f){
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
		File target = new File(dPath);
		if (isNull(dPath) || !target.exists()) {
			error(target.exists()?"No folder selected":"Destination folder does not exist"
				,"Missing destination folder");
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
