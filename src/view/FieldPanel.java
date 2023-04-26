package view;

import java.awt.GridLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FieldPanel extends JPanel{
	public FieldPanel() {
		super();
		this.setVisible(true);
		this.setLayout(new GridLayout(0,7));
		this.revalidate();
		this.repaint();
	}
}
