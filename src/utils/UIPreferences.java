package utils;

public interface UIPreferences {

	/**
	 * An interface that defines basic rules that apply when you create a subprogram that changes ui settings such as colors(background,foreground)
	 * or fonts
	 */
	public void loadPreferences();
	public void savePreferences();
}
