package extra;
import java.awt.Desktop;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.Messages.Message;

import gui.FileCopyManager;
public class InfoPage extends JFrame{
	private static final long serialVersionUID = 1L;
	JLabel  nameLabel 		 = new JLabel(FileCopyManager.appName),
			copyrightLabel   = new JLabel("Copyright ©2016. kounelios13");
	private JPanel mainPanel = new JPanel(){{
		add(nameLabel);
		add(copyrightLabel,"wrap");
		JButton btn = new JButton("Visit my GitHub page");
		btn.addActionListener(e->{
			URL url;
			try {
				url= new URL("https://github.com/kounelios13");
				Desktop.getDesktop().browse(url.toURI());
			} catch (Exception exc) {
				Message.error("Cannot open GitHub profila page.");
			}
		});
		add(btn,"cell 1 0");
	}};
	public JLabel[] getLabels (){
		return new JLabel[]{nameLabel,copyrightLabel};
	}
	private void initUI(){
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(mainPanel);
		setSize(250,120);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	public InfoPage(){
		super("About");
		initUI();
	}
}
