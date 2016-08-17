package utils;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
public interface UIPreferences {
	/**
	 * An interface that defines basic rules that apply when you create a subprogram that changes UI settings such as colors(background,foreground)
	 * or fonts
	 */
	final static Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	public void loadPreferences();
	public void savePreferences();	
}
