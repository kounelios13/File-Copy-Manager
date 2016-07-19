package utilities;

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
import messages.Message;
import org.apache.commons.io.FileUtils;
@SuppressWarnings({"static-access"})
public class FileHandler extends Thread{
	private ArrayList<File> filesToCopy=null;
	private String dest=null;
	private Message msg = new Message();
	private DefaultComboBoxModel<String> model=new DefaultComboBoxModel<>();
	private JComboBox<String> comboBox = new JComboBox<>(model);
	public FileHandler(){
		
	}
	public FileHandler(ArrayList<File> fs,JComboBox<String> combo,DefaultComboBoxModel<String>model){
		filesToCopy = fs;
		comboBox = combo;
		this.model=model;
		comboBox.setModel(model);
	}
	public void log(String message){
		File logFile = new File("app\\log.txt");
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException exc) {
				// TODO Auto-generated catch block
				msg.error(null, "IOException :"+exc, "Error");	
				return;
			}
	
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));//Append to file
				StringBuilder str = new StringBuilder();
				str.append("New log:::"+System.lineSeparator()+message);
				writer.write(str.toString());
				writer.close();
			} catch (IOException exc) {
				// TODO Auto-generated catch block
				msg.error(null, "IOException :"+exc, "Error");
			}

	}
	public void setDestination(String des){
		dest = des;
	}


	public void saveList(ProgramState ps,File destFile){
		File f =destFile;
		if(destFile == null){
			msg.error(null, "Destination folder has not been selected", "Destination Empty");
			return;
		}
		if(ps.getFiles()==null || ps.getFiles().size() < 1)
		{
			msg.error(null,"No files have been selected","Empty list");
			return;
		}
		try {
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(ps);
			out.close();
		} catch (FileNotFoundException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			msg.error(null, "IOException:"+exc, "Error");
			log(exc.getMessage());
		}
		msg.info(null, "List has been saved", "Status");
	}
	private boolean copySingleFile(File f,String dest){
		Path from = Paths.get(f.getAbsolutePath());
		Path to = Paths.get(dest+"\\"+f.getName());
		CopyOption[] options = new CopyOption[]{
			      StandardCopyOption.REPLACE_EXISTING,
			      StandardCopyOption.COPY_ATTRIBUTES
			    }; 
		try{
			Files.copy(from, to, options);
			//JOptionPane.showMessageDialog(null, "");
		}
		catch(IOException io){
			System.out.println("File "+f.getName()+" could not be copied to "+to);
			log(io.getMessage());
			return false;
		}
		return true;
	}
	public boolean copyFile(File f,String dest){
		return copySingleFile(f,dest);
	}
	public boolean copyDir(File dir,String dest){
		//TODO
		File destFolder = new File(dest+"\\"+dir.getName());
		if(!destFolder.exists())
			destFolder.mkdirs();
		try {
			FileUtils.copyDirectory(dir,destFolder);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			log(e.getMessage());
		}
		System.out.println("Output dir will be:"+destFolder);
		return true;
	}
	public void run() {
		// TODO Auto-generated method stub	
		for(File f:filesToCopy){
			copySingleFile(f,dest);				
		}
		
	}
	private void checkNull(ArrayList<File> st){
		if(st==null)
			st = new ArrayList<File>();
	}
	public void deepSearch(File fileToLook,ArrayList<File> fileStorage,DefaultComboBoxModel<String> mod) throws NullPointerException{
		checkNull(fileStorage);
		if(fileToLook !=null)
			if(fileToLook.isDirectory())
			{
				//mod.addElement(fileToLook.getName()+"(Folder)");
				for(File f:fileToLook.listFiles())
					deepSearch(f,fileStorage,mod);
			}
			else if(fileToLook.isFile()){
				fileStorage.add(fileToLook);
				mod.addElement(fileToLook.getName());
			}
		else
			System.out.println("Reached an end.");
	}
	public boolean copy(File f,String dest){
		return f.isDirectory()?copyDir(f,dest):copyFile(f,dest);
	}
	public void customSearch(File f,ArrayList<File> storage,DefaultComboBoxModel<String> mod)
	throws NullPointerException
	{
		String fold=" (Folder)";
		if(f != null){
			storage.add(f);
			if(f.isDirectory()){
				mod.addElement(f.getName()+fold);
				for(File e:f.listFiles())
					customSearch(e,storage,mod);
			}
			else{
				mod.addElement(f.getName());
			}
		}
		
	}
	public void loadList() {
		// TODO Auto-generated method stub
		
	}

}
