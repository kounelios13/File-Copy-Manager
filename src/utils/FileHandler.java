package utils;
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
import messages.Message;
import org.apache.commons.io.FileUtils;
public class FileHandler{
	String sep = File.separator + File.separator;
	public static boolean isNull(Object... items){
		for(Object o:items)
			if(o==null)
				return true;
		return false;
	}
	public static void log(String message){
		File logFile = new File("app"+File.separator+File.separator+"log.txt"),
				dir	 = new File("app");
		if(!dir.exists())
			dir.mkdirs();
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException exc) {
				Message.error(null, "IOException :"+exc);	
				return;
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));//Append to file
				StringBuilder str = new StringBuilder();
				str.append("New log:::"+System.lineSeparator()+message);
				writer.write(str.toString());
				writer.close();
			} catch (IOException exc) {
				Message.error(null, "IOException :"+exc);
				/**
				* Here we can only output the error message 
				* that prevent us from creating a '.log' file
				*/
			}
	}
	public void saveList(ProgramState ps,File destFile){
		if(isNull(destFile)){
			Message.error(null, "Destination folder has not been selected", "Destination Empty");
			return;
		}
		if(isNull(ps.getFiles()) || ps.getFiles().isEmpty())
		{
			Message.error(null,"No files have been selected","Empty list");
			return;
		}
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(destFile));
			out.writeObject(ps);
			out.close();
		} catch (FileNotFoundException exc) {
			log(exc.getMessage());
		} catch (IOException exc) {
			Message.error(null, "IOException:"+exc);
			log(exc.getMessage());
		}
		Message.info(null, "List has been saved", "Status");
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
			Message.error(null,"File "+fileName+" could not be copied to "+to);
			log(io.getMessage());
			return false;
		}
		if(log)
			if(new File(dest+sep+fileName).exists())
				Message.info(null,fileName+" copied successfully");
			else
				Message.error(null,fileName+" could not be copied");
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
			log(e.getMessage());
			Message.error(null,"Exception during copying directory");
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
			Message.error(null, "No folder selected","Missing destination folder");
			return;
		}
		try {
			Desktop.getDesktop().open(new File(dPath));
		} catch (Exception e1) {
			Message.error(null, "Could not open destination file");
			log(e1.getMessage());
		}	
	}
}
