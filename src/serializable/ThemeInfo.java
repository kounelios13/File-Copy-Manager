package serializable;
import java.io.Serializable;
public class ThemeInfo implements Serializable{
	private String themeName;
	private int index;
	public ThemeInfo(String theme,int comboIndex){
		themeName = theme;
		index = comboIndex;
	}
	public String getThemeName(){
		return themeName;
	}
	public int getThemeIndex(){
		return index;
	}
	public void setThemeName(String name){
		themeName = name;
	}
	public void setThemeIndex(int index){
		this.index = index;
	}
}
