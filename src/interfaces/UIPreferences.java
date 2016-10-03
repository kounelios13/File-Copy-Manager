/*
 * UIPreferences.java
 *
 * Created on 27.09.2016, 13:35:49
 *
 * This file is part of the File Copy Manager project.
 * 
 * The File Copy Manager is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 * 
 * The File Copy Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	public void loadPreferences();
	public void savePreferences();	
	public void setBg(Color bg);
	public void setFg(Color fg);
	public void updatePreview();
}
