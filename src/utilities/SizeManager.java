package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFrame;
@SuppressWarnings({"serial"})
public class SizeManager extends JFrame implements UIPreferences {

	private File file = new File("app"+File.separator+File.separator+"sm.dat");
	private int height,width;
	@Override
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public void loadPreferences() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void savePreferences() {
		// TODO Auto-generated method stub
		ObjectOutputStream out;
		try {
		  out = new ObjectOutputStream(new FileOutputStream(file));
		  out.writeObject(new Settings(height,width));
		  out.close();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
		finally{
			
		}
	}

	class Settings implements Serializable{
		private int h,w;
		public int getHeight(){
			return h;
		}
		public int getWidth(){
			return w;
		}
		public Settings(int a,int b){
			h=a;
			w=b;
		}
	}
}
