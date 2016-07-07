package utilities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
@SuppressWarnings({"serial"})
public class ProgramState implements Serializable{
	private ArrayList<File> files;
	private int index;
	private String path;
	public String getPath(){
		return path;
	}
	public File getSelectedFile(){
		return files.get(index);
	}
	public ArrayList<File> getFiles(){
		return files;
	}
	public int getSindex(){
		return index;
	}	
	public ProgramState(ArrayList<File> ar,int selectedIndex,String p){
		files = ar;
		path = p;
		index =selectedIndex;
	}
}
