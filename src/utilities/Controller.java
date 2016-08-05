package utilities;
import java.io.File;
@SuppressWarnings({})
public class Controller {
	private FileHandler handler= new FileHandler();
	public void saveList(ProgramState ps,File d){
		handler.saveList(ps,d);
	}
	public void loadList(){
		handler.loadList();
	}
}
