package gui;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
@SuppressWarnings({"serial"})
public class StatusFrame extends JFrame{
	private JPanel panel = new JPanel();
	private JLabel currentFileLabel = new JLabel("Current File");
	public StatusFrame(){
		//currentFileLabel.setFont(getFont().deriveFont((float) 18.0));
		panel.setBackground(new Color(18,193,17));
		currentFileLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(currentFileLabel);
		this.setSize(600,100);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setContentPane(panel);

	}
	public void showStatus(){
		this.setVisible(true);
	}
	public StatusFrame text(String s){
		currentFileLabel.setText("Copying "+s+" .");
		this.pack();
		return this;
	}
	public static void main(String[] args) {
		 EventQueue.invokeLater(()->new StatusFrame().setVisible(true));
	}
}
