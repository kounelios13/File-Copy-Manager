package utilities;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings({})
public class Controller {

	private FileHandler handler= new FileHandler();
	public void saveList(ArrayList<File> files,File d){
		handler.saveList(files,d);
	}
	public void loadList(){
		
	}
}
