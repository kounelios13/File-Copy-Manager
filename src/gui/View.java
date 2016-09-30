/*
 * View.java
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
package gui;
import interfaces.IView;
import javax.swing.JFrame;
@SuppressWarnings("serial")
public  class View extends JFrame implements IView{
	public View(String title,int width,int height){
		super(title);
		this.setSize(width, height);
	}
	public View(String title,int width,int height,boolean resizable){
		this.setResizable(resizable);
	}
	public void toggleUI(){
		this.setVisible(!this.isVisible());
	}
}
