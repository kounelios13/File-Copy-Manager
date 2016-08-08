package utils;

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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import messages.Message;

import org.apache.commons.io.FileUtils;
public class FileHandler{
	private DefaultComboBoxModel<String> model=new DefaultComboBoxModel<>();
	private JComboBox<String> comboBox = new JComboBox<>(model);
	public FileHandler(){
		
	}
	public FileHandler(ArrayList<File> fs,JComboBox<String> combo,DefaultComboBoxModel<String>model){
		comboBox = combo;
		this.model=model;
		comboBox.setModel(model);
	}
	public static void log(String message){
		File logFile = new File("app\\log.txt");
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException exc) {
				Message.error(null, "IOException :"+exc, "Error");	
				return;
			}
	
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));//Append to file
				StringBuilder str = new StringBuilder();
				str.append("New log:::"+System.lineSeparator()+message);
				writer.write(str.toString());
				writer.close();
			} catch (IOException exc) {
				Message.error(null, "IOException :"+exc, "Error");
			}

	}
	public void setDestination(String des){
	}


	public void saveList(ProgramState ps,File destFile){
		File f =destFile;
		if(destFile == null){
			Message.error(null, "Destination folder has not been selected", "Destination Empty");
			return;
		}
		if(ps.getFiles()==null || ps.getFiles().size() < 1)
		{
			Message.error(null,"No files have been selected","Empty list");
			return;
		}
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(ps);
			out.close();
		} catch (FileNotFoundException exc) {
			log(exc.getMessage());
			exc.printStackTrace();
		} catch (IOException exc) {
			Message.error(null, "IOException:"+exc, "Error");
			log(exc.getMessage());
		}
		Message.info(null, "List has been saved", "Status");
	}
	private boolean copySingleFile(File f,String dest,boolean log){
		String fileName = f.getName();
		Path from = Paths.get(f.getAbsolutePath());
		Path to = Paths.get(dest+"\\"+fileName);
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
			if(new File(dest+"\\"+fileName).exists())
				Message.info(null,fileName+" copied successfully");
			else
				Message.error(null,fileName+" could not be copied");
		return true;
	}
	public boolean copyFile(File f,String dest,boolean log){
		return copySingleFile(f,dest,log);
	}
	public boolean copyDir(File dir,String dest){
		File destFolder = new File(dest+"\\"+dir.getName());
		if(!destFolder.exists())
			destFolder.mkdirs();
		try {
			FileUtils.copyDirectory(dir,destFolder);
		}
		catch (IOException e) {
			log(e.getMessage());
			Message.error(null,"Exception during copying directory","Error");
		}
		System.out.println("Output dir will be:"+destFolder);
		return true;
	}

	public boolean copy(File f,String dest,boolean log){
		return f.isDirectory()?copyDir(f,dest):copyFile(f,dest,log);
	}
	public String fileName(File f){
		return f.isFile()?f.getName()+(f.isDirectory()?" (Folder)":""):"";
	}
	public ProgramState loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage) {
		ProgramState temp= new ResourceLoader(this).getAppState();
			//See if we have succeed in loading everything we need so we can proccess the combobox
		if(temp == null)
			return null;

		System.out.println("Size of importes files"+temp.getFiles().size());
		if(storage.isEmpty()){
			//Storage is empty no new files added by user
			storage = temp.getFiles();
			for(File f:storage)
				mod.addElement(fileName(f));
		}
		else{
			if(JOptionPane.showConfirmDialog(null, "Do you want to keep new files?")==JOptionPane.OK_OPTION){
				/*
				 * The user wants to keep new and old files 
				 * */
				for(File f:temp.getFiles()){
					storage.add(f);
					mod.addElement(fileName(f));
				}
			}
			else{
				storage = temp.getFiles();
				mod.removeAllElements();
				for(File f:storage)
					mod.addElement(fileName(f));
			}
		}
		return temp;
	}

}
