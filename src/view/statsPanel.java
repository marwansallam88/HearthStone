package view;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class statsPanel extends JPanel{
	public statsPanel() {
		super();
		this.setLayout(new FlowLayout());
		this.setVisible(true);
	}
	@Override
	public void paintComponent (Graphics g)
	{
	   ImageIcon ii = new ImageIcon("images/statsBackground2.png");
	   Image i = ii.getImage();
	   super.paintComponent(g);
	   g.drawImage(i, 0, 0,this.getWidth(), this.getHeight(), this);
	}


}
