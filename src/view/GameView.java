package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameView	extends JFrame {
	
	private JPanel topPanel = new JPanel();
	private JPanel midPanel = new JPanel();
	
	public GameView() {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			topPanel.setOpaque(true);
			topPanel.setLayout(new FlowLayout());
			topPanel.setBackground(Color.ORANGE);
			midPanel.setLayout(new GridLayout(0,5));
			midPanel.setOpaque(true);
			midPanel.setBackground(Color.BLACK);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
		}
	
	public JPanel getStartTopPanel() {
		return topPanel;
	}
	
	public JPanel getStartMidPanel() {
		return midPanel;
	}
	
	public void playWindow() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setLayout(new GridLayout(4,0));
		this.revalidate();
		this.repaint();
	}
	
	
	
	public static void main(String[] args) {
		
	}
}
	

