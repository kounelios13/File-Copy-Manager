package serializable;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
@SuppressWarnings({"serial"})
/*
* This class is used to save information about the list of files a user has added and the destination folder
* */
public class ProgramState implements Serializable{
	private ArrayList<File> files;
	private int index;
	private String path;
	private boolean duplicates = false;
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
	public boolean allowDuplicates(){
		return duplicates;
	}
	public ProgramState(ArrayList<File> ar,int selectedIndex,String p,boolean dups){
		files = ar;
		path = p;
		index =selectedIndex;
		duplicates = dups;
	}
}
