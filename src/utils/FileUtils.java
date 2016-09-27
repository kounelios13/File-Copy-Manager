/*
 * FileUtils.java
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
public class FileUtils {
	public static boolean isSpecialFile(File f,char criteria){
		return f.getName().indexOf(criteria)!=-1;
	}
	public static boolean isSpecialFile(String file,char criteria){
		return isSpecialFile(new File(file),criteria);
	}
	public static boolean renameFile(File f,String newName){
		return f.renameTo(new File(newName));
	}
	public static boolean renameFile(File f,File newFile){
		return f.renameTo(newFile);
	}
	public static boolean delete(File f){
		if(f.isDirectory())
			for(File cur:f.listFiles())
				delete(cur);
		return f.delete();
	}
	public static boolean delete(String fileName){
		return delete(new File(fileName));
	}
	
}
