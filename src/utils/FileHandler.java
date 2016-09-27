package utils;
import static messages.Message.error;
import static messages.Message.info;
import java.awt.Component;
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
import ch.fhnw.filecopier.CopyJob;
import ch.fhnw.filecopier.FileCopier;
import ch.fhnw.filecopier.FileCopierPanel;
import ch.fhnw.filecopier.Source;
import static utils.FileUtils.*;
public class FileHandler{
	/**
	 * 
	 * See this web site for an alternative way to copy files while showing graphical progress
	 * http://filecopylibrary.sourceforge.net/
	 * */
	private static String sep = File.separator + File.separator;
	private FileCopierPanel copierPanel = new FileCopierPanel();
	private FileCopier copyEngine = new FileCopier();
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
	public boolean isSpecialNameHandled(File f){
		String oldName = f.getAbsolutePath();
		/*if(oldName.contains("+"))
			info("");*/
		String fixedName = oldName.replace("+","_");
		return f.renameTo(new File(fixedName));
	}
	public Component getCopyPanel(){
		copierPanel.setFileCopier(copyEngine);
		return copierPanel; 
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
	public boolean copy(File f,String dest,boolean log){
		//info("trying to copy "+f.getName());
		if(isSpecialFile(f,'+')){
			String logText = "File contains '+' character.Trying to rename it in order to copy it";
			String oldName = f.getAbsolutePath();
			String originalFileName = f.getName();
			File fixedFile = new File(oldName.replace('+', '_'));
			boolean renamed =renameFile(f,fixedFile);
			if(renamed)
				logText += "File renamed in order to be copied.\n";
			else{
				logText +="File couldn't be renamed.Cannot copy file.\n";
				log(logText);
				return false;
			}			
			//Renamed file re-execute copy() to copy file
			boolean copied = copy(fixedFile,dest,log);
			if(copied){
				logText += "Managed to copy file\n";
				File finalFile = new File(dest+"/"+oldName);
				if(!fixedFile.renameTo(finalFile))
					logText += "Couldn't rename file back to its original name";
				renameFile(finalFile, new File(dest+"/"+originalFileName));
				renameFile(f,new File(f.getParentFile()+"/"+originalFileName));
			}
			else{
				logText += "File could not be copied to destination.\n";
			}
			log(logText);
		}
		Source[] src= {new Source(f.getAbsolutePath())};
		try {
			copyEngine.copy(new CopyJob(src,new String[]{dest}));
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			error("Couldn't copy "+f.getName()+" to "+dest);
			log(exc);
		}
		return false;
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
