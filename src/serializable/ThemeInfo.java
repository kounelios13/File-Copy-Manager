package serializable;
import java.io.Serializable;
public class ThemeInfo implements Serializable{
	public static ThemeInfo getDummyThemInfo(){
		return new ThemeInfo("javax.swing.plaf.nimbus.NimbusLookAndFeel","Default",0,0);
	}
	private String lookAndFeelName,
		themeName = "Default";
	private int index,themeIndex;
	public ThemeInfo(String lookAndFeel,String theme,int comboIndex,int themeIndex){
		lookAndFeelName = lookAndFeel;
		index = comboIndex;
		themeName = theme;
		this.themeIndex = themeIndex;
	}
	public String getLookAndFeelName(){
		return lookAndFeelName;
	}
	public int getLookAndFeelIndex(){
		return index;
	}
	public String getThemeName(){
		return themeName;
	}
	public int getThemeIndex(){
		return themeIndex;
	}
	public void setLookAndFeelName(String name){
		lookAndFeelName = name;
	}
	public void setThemeName(String name){
		themeName = name;
	}
	public void setLookAndFeelIndex(int index){
		this.index = index;
	}
	public void setThemeIndex(int index){
		this.themeIndex = index;
	}
	public ThemeInfo fixUnsupportedThemeInfo(Object o){
		ThemeInfo th = (ThemeInfo)o;
		th.setThemeIndex(0);
		th.setThemeName("Default");
		return th;
	}
}
