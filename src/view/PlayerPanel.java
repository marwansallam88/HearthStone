package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import model.heroes.Hero;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel{
	private statsPanel statsPanel = new statsPanel();
	private JPanel actionsPanel = new JPanel();
	private JPanel cardsPanel = new JPanel();
	private JScrollPane scrollCardsPanel = new JScrollPane(cardsPanel);
	private JButton player = new JButton();
	public PlayerPanel(int h,int w, Hero hero) {
		super();
		scrollCardsPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setSize(w, h/5);
		this.setLayout(new GridBagLayout());
		scrollCardsPanel.setLayout(new ScrollPaneLayout());
		GridBagConstraints c = new GridBagConstraints();
		JPanel imagePanel = new JPanel();
		imagePanel.setVisible(true);
		imagePanel.setLayout(new BorderLayout());
		imagePanel.setSize(this.getWidth()/8,this.getHeight());
		String x = hero.getName();
		player.setSize(imagePanel.getWidth(),imagePanel.getHeight());
		ImageIcon i = new ImageIcon();
		switch(x) {
		case "Rexxar":i=new ImageIcon("images/Hunter.jpg");break;
		case "Jaina Proudmoore":i=new ImageIcon("images/Mage.jpeg");break;
		case "Uther Lightbringer":i=new ImageIcon("images/Paladin.jpeg");break;
		case "Anduin Wrynn":i=new ImageIcon("images/Priest.jpeg");break;
		case "Gul'dan":i=new ImageIcon("images/Warlock.jpeg");break;
		}	
		Image image = i.getImage();
		Image newimg = image.getScaledInstance(player.getWidth(), player.getHeight(),  java.awt.Image.SCALE_SMOOTH); 
		i = new ImageIcon(newimg);
		player.setIcon(i);
		player.setOpaque(false);player.setContentAreaFilled(false);player.setBorderPainted(false);
		imagePanel.add(player,BorderLayout.CENTER);
		c.fill=GridBagConstraints.HORIZONTAL;
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=0;
		c.weightx = 0;
		c.weighty = 0;
		this.add(imagePanel,c);
		c.gridx=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.fill=GridBagConstraints.VERTICAL;
		c.weightx = 0;
		c.weighty = 0;
		c.ipady = 10;
		this.add(statsPanel,c);
		c.gridx=2;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.fill=GridBagConstraints.VERTICAL;
		c.weightx = 0;
		c.weighty = 0;
		c.ipady = 10;
		this.add(actionsPanel,c);
		c.gridx=3;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.fill=GridBagConstraints.VERTICAL;
		c.weightx = 1;
		c.fill=1;
		this.add(scrollCardsPanel,c);
		statsPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
		actionsPanel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
		actionsPanel.setLayout(new GridLayout(4,0));
		cardsPanel.setLayout(new GridLayout(0,10));
		imagePanel.setBackground(Color.black);
		cardsPanel.setBackground(Color.black);
		actionsPanel.setBackground(Color.black);
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	public JPanel getStatsPanel() {
		return statsPanel;
	}
	public JPanel getActionsPanel() {
		return actionsPanel;
	}
	public JScrollPane getScrollCardsPanel() {
		return scrollCardsPanel;
	}
	public JPanel getCardsPanel() {
		return cardsPanel;
	}
	public JButton getPlayer(){
		return player;
	}
	
}
