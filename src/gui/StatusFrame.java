package gui;
import java.awt.EventQueue;

import javax.swing.*;
@SuppressWarnings({"serial"})
public class StatusFrame extends JFrame{

	//public String fName;
	private JPanel panel = new JPanel();
	private JLabel currentFileLabel = new JLabel("Current File");
	public StatusFrame(){
		
		panel.add(currentFileLabel);
		this.setSize(400,400);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setContentPane(panel);

	}
	public void showStatus(){
		this.setVisible(true);
	}
	public StatusFrame text(String s){
		currentFileLabel.setText("Copying "+s+" .");
		return this;
	}
	public static void main(String[] args) {
		 EventQueue.invokeLater(()->System.out.println("No worries"));
	}
}
