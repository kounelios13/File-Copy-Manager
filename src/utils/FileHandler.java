/*
 * FileHandler.java
 *
 * Created on 27.09.2016, 13:35:49
 *
 * This file is part of the File Copy Manager project.
 * 
 * The File Copy Manager is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 * 
 * The File Copy Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package utils;
import static messages.Message.error;
import static messages.Message.info;
import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;
import ch.fhnw.filecopier.FileCopier;
import ch.fhnw.filecopier.FileCopierPanel;
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
			FileInputStream fis = new FileInputStream(f);
			if(!f.exists())
				error("File not exists");
			FileOutputStream fos= new FileOutputStream(dest+"/"+f.getName());
			FileChannel outChannel  = fos.getChannel(),
						  inChannel = fis.getChannel();
			ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(null,"Copying",fis);
			pmis.getProgressMonitor().setMillisToPopup(10);
			pmis.getProgressMonitor().setNote("Copying "+f.getName());
			outChannel.transferFrom(inChannel, 0, f.length());
			outChannel.close();
			fis.close();
			fos.close();
			outChannel.close();
			inChannel.close();
		} catch (FileNotFoundException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
		return destFile.exists();
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
