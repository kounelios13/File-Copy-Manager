package gui;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import utils.FileHandler;
@SuppressWarnings({"serial"})
public class StatusFrame extends JFrame{
	private Timer timer;
	private JPanel panel = new JPanel();
	//value,extend,min,max
	DefaultBoundedRangeModel model   = new DefaultBoundedRangeModel(0,1,0,100);
	private JLabel currentFileLabel  = new JLabel("Current File");
	private JProgressBar progressBar = new JProgressBar(model);
	private FileHandler fileHandler  = new FileHandler(this);
	public void updateProgessBar(long prog){
		int value = (int)prog / 100;
		model.setValue(value);
	}
	public void requestStatus(File file,String path){
		timer = new Timer(400,(e)->{
			long pg = fileHandler.getCopyProgress(file,path);
			if(pg == 100){
				stopTimer();
				return;
			}
			updateProgessBar(pg);
			System.out.println("Requested status for "+file.getName()+" .Status:"+pg);
		});
		timer.start();
	}
	public void stopTimer(){
		timer.stop();
	}
	public StatusFrame(){
		panel.setBackground(new Color(18,193,17));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{182, 69, 146, 0};
		gbl_panel.rowHeights = new int[]{17, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		currentFileLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_currentFileLabel = new GridBagConstraints();
		gbc_currentFileLabel.insets = new Insets(0, 0, 5, 5);
		gbc_currentFileLabel.gridx = 1;
		gbc_currentFileLabel.gridy = 0;
		panel.add(currentFileLabel, gbc_currentFileLabel);
		this.setSize(600,100);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setContentPane(panel);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.anchor = GridBagConstraints.WEST;
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 1;
		panel.add(progressBar, gbc_progressBar);
	}
	public void showStatus(){
		this.setVisible(true);
	}
	public StatusFrame text(String s){
		this.setTitle("Copying "+s+" .");
		progressBar.getModel().setValue(0);
		currentFileLabel.setText("Copying "+s+" .");
		this.pack();
		return this;
	}
	public static void main(String[] args) {
		 EventQueue.invokeLater(()->new StatusFrame().setVisible(true));
	}
}
