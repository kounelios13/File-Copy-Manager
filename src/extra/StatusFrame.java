package extra;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;
import gui.FileCopyManager;
import gui.View;
import net.miginfocom.swing.MigLayout;
public class StatusFrame extends View{
	JLabel fileNameLabel = new JLabel("Copying :");
	@Override
	public String toString(){
		return this.getClass().getName();
	}
	@Override
	protected void initUIElements() {
		JPanel panel = new JPanel(){{
			setLayout(new MigLayout());
			add(fileNameLabel);
		}};
		this.setContentPane(panel);
		this.setVisible(false);
		this.pack(); 
	}
	public StatusFrame(FileCopyManager fm){
		super("Progress",1000,200);
		initUIElements();
		this.setLocationRelativeTo(fm.getContentPane());
	}
	public void update(File file){
		update(file.getName());
	}
	private void update(String fileName){
		fileNameLabel.setText("Copying :"+fileName);
	}
}
