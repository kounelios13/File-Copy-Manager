package utilities;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
@SuppressWarnings({})
public class Controller {
	private FileHandler handler= new FileHandler();
	public void saveList(ProgramState ps,File d){
		handler.saveList(ps,d);
	}
	public void loadList(DefaultComboBoxModel<String> mod,ArrayList<File> storage,boolean dups){
		handler.loadList(mod,storage,dups);
	}
}
