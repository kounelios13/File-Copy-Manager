package serializable;
import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import interfaces.UIPreferences;
public class Settings implements Serializable {
	private Color bg, fg;
	private String fontName;
	private int lblSize = 12, btnSize = 18;
	private Font[] fonts = UIPreferences.fonts;
	public boolean isFontAvailable(){
		for(Font f:fonts)
			if(f.getFontName().equals(fontName))
				return true;
		return false;	
	}
	public int getFontIndex(String name){
		for(int i=0,max=fonts.length;i<max;i++)
			if(fonts[i].getFontName().equals(name))
				return i;
		return -1;	
	}
	public void setBgColor(Color e) {
		bg = e;
	}
	public void setFgColor(Color e) {
		fg = e;
	}
	public Color getBgColor() {
		/**
		 * Default background color for most swing components
		 * */
		return bg != null ? bg:new Color(238,238,238);
	}
	public Color getFgColor() {
		/**
		 * Default foreground color for most swing components
		 * */
		return fg != null ? fg:new Color(51,51,51);
	}
	public int getLblSize() {
		return lblSize;
	}
	public void setLblSize(int lblSize) {
		this.lblSize = lblSize;
	}
	public int getBtnSize() {
		return btnSize;
	}
	public void setBtnSize(int btnSize) {
		this.btnSize = btnSize;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public Font getButtonFont() {
		return new Font(fontName, Font.PLAIN, this.btnSize);
	}
	public Font getLabelFont() {
		return new Font(fontName, Font.PLAIN, lblSize);
	}
}