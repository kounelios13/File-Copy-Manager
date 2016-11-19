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
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import messages.Message;
import serializable.ProgramState;
import serializable.ThemeInfo;
import org.apache.commons.io.FileUtils;
//@SuppressWarnings("unused")
/*
* This class is used to handle an file operations like copying
* */
public class FileHandler{
	private static String sep = File.separator + File.separator;
	private static boolean logApplicationErrors = true;
	private ResourceLoader loader = new ResourceLoader(this);
	public static void setAdvancedLogs(boolean value){
		/*
		 * Decide if we want to log errors messages that were not caused by an exception
		 * but from a mistake made by the user
		 * **/
		logApplicationErrors = value;
	}
	public static boolean isNull(Object... items){
		for(Object o:items)
			if(o==null)
				return true;
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
				String errorMessage = 
						(logFile.exists()?"IOException occured:":"Couldn't create log file:")
							+"\n"+exc.getMessage();	
				error(errorMessage);
				if(logFile.exists())
					log(errorMessage);
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
				if(!logFile.canWrite())
				{
					logFile.setWritable(true);
					if(logFile.canWrite())
						log(message);
					else
						error("Cannot write to log file.Please change permissions manually");
				}
				else
					error("IOException :"+exc.getMessage());
				if(logFile.exists() && logFile.canWrite())
					log(exc);
			}
	}
	public FileHandler(){
	}
	public void saveList(ProgramState ps,File destFile){
		if(isNull(destFile)){
			error("Destination folder has not been selected", "Destination Empty");
			if(logApplicationErrors)
				log("No destination folder has been selected");
			return;
		}
		if(isNull(ps.getFiles()) || ps.getFiles().isEmpty())
		{
			error("No files have been selected","Empty list");
			if(logApplicationErrors)
				log("No files selected to copy");
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
		return dest+"/"+f.getName();
	}
	public File getDestFile(File srcFile,String dest){
		return new File(getDestinationName(srcFile,dest));
	}
	@SuppressWarnings("all")
	public boolean copy(File f,String dest,boolean log){
		/**
		 * 
		 * */
		File destFile = getDestFile(f,dest);
		try {
			if(f.isDirectory()){
				FileUtils.copyDirectory(f,destFile);
				//info("copy completed");
			}else{
				FileUtils.copyFile(f, destFile);
				//info("copy completed");
			}	
		} catch (FileNotFoundException exc) {
			log(exc);
		} catch (IOException exc) {
			log(exc);
		}
		return destFile.exists();
	}
	private String fileName(File f){
		return f.isFile()?f.getName()+(f.isDirectory()?" (Folder)":""):"";
	}
	public ProgramState loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage){
		ProgramState temp = loader.getAppState();
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
		// Check if Desktop class is supported in the platform the program is running on
		if(!Desktop.isDesktopSupported()){
			String msg = "The platform you are using does not support Desktop class";
			Message.error(msg, "Unsupported Operation");
			log(msg);
			return;
		}
		if (isNull(dPath) || !target.exists()) {
			error(target.exists()?"No folder selected":"Destination folder does not exist"
				,"Missing destination folder");
			return;
		}
		try {
			Desktop.getDesktop().open(target);
		} catch (Exception e1) {
			error("Could not open destination file");
			log(e1);
		}	
	}
	public void openAppDirectory() {
		File dir = new File("app");
		// Check if Desktop class is supported in the platform the program is running on
		if(!Desktop.isDesktopSupported()){
			String msg = "The platform you are using does not support Desktop class";
			Message.error(msg, "Unsupported Operation");
			log(msg);
			return;
		}
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
	public void saveLookAndFeel(ThemeInfo info) {
		File lookAndFeel = new File("app/lookAndFeel.dat");
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(lookAndFeel) );
			out.writeObject(info);
		} catch (FileNotFoundException exc) {
			try {
				lookAndFeel.createNewFile();
			} catch (IOException exc1) {
				// TODO Auto-generated catch block
				if(!lookAndFeel.exists()){
					log("Couldn't create look and feel file");
					try {
						out.close();
					} catch (IOException exc2) {
						// TODO Auto-generated catch block
						log(exc2);
					}
					return;
				}
				log(exc1);
			}
			finally{
				saveLookAndFeel(info);
			}
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			log(exc);
		}
			try {
				out.close();
			} catch (IOException exc) {
				// TODO Auto-generated catch block
				log(exc);
			}
	}
	public ThemeInfo getThemeInfo(){
		return loader.getThemeInfo();
	}
}
