package demos;

import javax.swing.*;
@SuppressWarnings({"serial"})
public class Bar extends JFrame{
	DefaultBoundedRangeModel model = new DefaultBoundedRangeModel();
	JPanel panel = new JPanel();
	JProgressBar jbar = new JProgressBar(model);
	private void init(){
		JButton b = new JButton("update");
		b.addActionListener((e)->go());
		this.setContentPane(panel);
		panel.add(jbar);
		panel.add(b);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	public void go(){	
		new Thread(()->{
			model.setMaximum(100);
			model.setMinimum(0);
			model.setValue(0);
			int i=0;
			while(i <101){
				try {
	                while (i <= 100 || true) {
	                    model.setValue(i);
	                    i++;
	                    Thread.sleep(50);
	                }
	            } catch (InterruptedException ex) {
	                model.setValue(model.getMaximum());
	            }
			}
		}).start();
	}
	public Bar(){
		init();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->new Bar().go());
	}
}
