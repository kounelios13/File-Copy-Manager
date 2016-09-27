/*
 * ProgramState.java
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
package utils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
@SuppressWarnings({"serial"})
public class ProgramState implements Serializable{
	private ArrayList<File> files;
	private int index;
	private String path;
	private boolean duplicates = false;
	public String getPath(){
		return path;
	}
	public File getSelectedFile(){
		return files.get(index);
	}
	public ArrayList<File> getFiles(){
		return files;
	}
	public int getSindex(){
		return index;
	}
	public boolean allowDuplicates(){
		return duplicates;
	}
	public ProgramState(ArrayList<File> ar,int selectedIndex,String p,boolean dups){
		files = ar;
		path = p;
		index =selectedIndex;
		duplicates = dups;
	}
}
