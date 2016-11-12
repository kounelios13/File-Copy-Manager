package interfaces;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
public interface UIPreferences{
	/**
	 * An interface that defines basic rules that apply when you create a subprogram that changes UI settings such as colors(background,foreground)
	 * or fonts
	 */
    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	/**
	* Provide a way to the user for loading/saving preferences
	**/
    void loadPreferences();
	void savePreferences();
	void setBg(Color bg);
	void setFg(Color fg);
	void updatePreview();
}
