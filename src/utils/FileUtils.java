package utils;
import java.io.File;
public class FileUtils {
	public static boolean isSpecialFile(File f,char criteria){
		return f.getName().indexOf(criteria)!=-1;
	}
	public static boolean isSpecialFile(String file,char criteria){
		return isSpecialFile(new File(file),criteria);
	}
	public static boolean renameFile(File f,String newName){
		return f.renameTo(new File(newName));
	}
	public static boolean renameFile(File f,File newFile){
		return f.renameTo(newFile);
	}
	public static boolean delete(File f){
		return f.delete();
	}
	public static boolean delete(String fileName){
		return delete(new File(fileName));
	}
}
