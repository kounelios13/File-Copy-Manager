package interfaces;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
public interface UIPreferences{
	/**
	 * An interface that defines basic rules that apply when you create a subprogram that changes UI settings such as colors(background,foreground)
	 * or fonts
	 */
	final static Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	/**
	* Provide a way to the user for loading/saving preferences
	**/
	public void loadPreferences();
	public void savePreferences();	
	public void setBg(Color bg);
	public void setFg(Color fg);
	public void updatePreview();
}
