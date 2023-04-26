package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import view.FieldPanel;
import view.GameView;
import view.PlayerPanel;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;

public class controller implements GameListener, ActionListener {
	@SuppressWarnings("unused")
	private controller control;
	private Game game;
	private GameView window;
	Hero player1;
	Hero player2;
	private PlayerPanel Top;
	private FieldPanel topField;
	private FieldPanel botField;
	private PlayerPanel Bot;
	private JButton b1 = new JButton();
	private JButton b2 = new JButton();
	private JButton b3 = new JButton();
	private JButton b4 = new JButton();
	private JButton b5 = new JButton();
	private JTextArea topText = new JTextArea(
			"Player 1, Select a hero to start!");
	private JTextArea topStats = new JTextArea();
	private JTextArea botStats = new JTextArea();
	private String p1name;
	private String p2name;
	private JButton topCancel = new JButton("Cancel");
	private JButton topUse = new JButton("Use");
	private JButton topAbility = new JButton("Ability");
	private JButton topSummon = new JButton("Summon");
	private JButton topAttack = new JButton("Attack");
	private JButton topEnd = new JButton("End Turn");
	private JButton botCancel = new JButton("Cancel");
	private JButton botUse = new JButton("Use");
	private JButton botAbility = new JButton("Ability");
	private JButton botSummon = new JButton("Summon");
	private JButton botAttack = new JButton("Attack");
	private JButton botEnd = new JButton("End Turn");
	private JButton Selected = new JButton();
	private JButton Target = new JButton();
	private ArrayList<JButton> topHandCards = new ArrayList<JButton>();
	private ArrayList<JButton> topFieldCards = new ArrayList<JButton>();
	private ArrayList<JButton> botHandCards = new ArrayList<JButton>();
	private ArrayList<JButton> botFieldCards = new ArrayList<JButton>();
	private Clip clip;
	private Clip fx;

	public controller() throws FullHandException, CloneNotSupportedException {

		window = new GameView();
		playSound("Sounds/Main_Title.wav");
		window.setTitle("HEARTHSTONE");
		window.setIconImage(new ImageIcon("images/LOGO.jpg").getImage());
		topText.setEditable(false);
		topText.setOpaque(false);
		window.getStartTopPanel().add(topText);
		ImageIcon Hunter = new ImageIcon("gifs/Hunter.gif");
		ImageIcon Mage = new ImageIcon("gifs/Mage.gif");
		ImageIcon Paladin = new ImageIcon("gifs/Paladin.gif");
		ImageIcon Priest = new ImageIcon("gifs/Priest.gif");
		ImageIcon Warlock = new ImageIcon("gifs/Warlock.gif");
		b1.setPreferredSize(new Dimension(
				window.getStartMidPanel().getWidth() / 5, window
						.getStartMidPanel().getHeight()));
		b2.setPreferredSize(new Dimension(
				window.getStartMidPanel().getWidth() / 5, window
						.getStartMidPanel().getHeight()));
		b3.setPreferredSize(new Dimension(
				window.getStartMidPanel().getWidth() / 5, window
						.getStartMidPanel().getHeight()));
		b4.setPreferredSize(new Dimension(
				window.getStartMidPanel().getWidth() / 5, window
						.getStartMidPanel().getHeight()));
		b5.setPreferredSize(new Dimension(
				window.getStartMidPanel().getWidth() / 5, window
						.getStartMidPanel().getHeight()));
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b1.setActionCommand("Hunter");
		b2.setActionCommand("Mage");
		b3.setActionCommand("Paladin");
		b4.setActionCommand("Priest");
		b5.setActionCommand("Warlock");
		window.getStartMidPanel().add(b1);
		window.getStartMidPanel().add(b2);
		window.getStartMidPanel().add(b3);
		window.getStartMidPanel().add(b4);
		window.getStartMidPanel().add(b5);
		b1.setIcon(Hunter);
		b2.setIcon(Mage);
		b3.setIcon(Paladin);
		b4.setIcon(Priest);
		b5.setIcon(Warlock);
		b1.setOpaque(false);
		b1.setContentAreaFilled(false);
		b1.setBorderPainted(false);
		b2.setOpaque(false);
		b2.setContentAreaFilled(false);
		b2.setBorderPainted(false);
		b3.setOpaque(false);
		b3.setContentAreaFilled(false);
		b3.setBorderPainted(false);
		b4.setOpaque(false);
		b4.setContentAreaFilled(false);
		b4.setBorderPainted(false);
		b5.setOpaque(false);
		b5.setContentAreaFilled(false);
		b5.setBorderPainted(false);
		window.add(window.getStartMidPanel(), BorderLayout.CENTER);
		window.add(window.getStartTopPanel(), BorderLayout.NORTH);
		window.setVisible(true);
		window.revalidate();
		window.repaint();
	}

	public void onGameOver() {
		clip.stop();
		playFX("Sounds/victory_screen_start.wav");
		if (player1.getCurrentHP() <= 0) {
			JOptionPane.showMessageDialog(null, p2name + "IS THE WINNER",
					"CONGRATS", JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, p1name + "IS THE WINNER",
					"CONGRATS", JOptionPane.INFORMATION_MESSAGE);
		window.dispose();
		try {
			control = new controller();
		} catch (FullHandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Top(Hero h) {
		Top = new PlayerPanel(window.getHeight(), window.getWidth(), player1);
		Top.getPlayer().addActionListener(this);
		Top.setBackground(Color.orange);
		window.add(Top);
		//
		topStats.setOpaque(false);
		topStats.setEditable(false);
		topStats.setText(p1name + "\n" + "HP " + player1.getCurrentHP()
				+ "\n\n" + "Mana " + player1.getCurrentManaCrystals() + "/"
				+ player1.getTotalManaCrystals() + "\n\n" + "Deck "
				+ player1.getDeck().size());
		topStats.setFont(new Font("Segoe Script", Font.BOLD, 15));
		topStats.setForeground(Color.RED);
		Top.getStatsPanel().add(topStats);
		JTextArea space = new JTextArea("              ");
		space.setOpaque(false);
		Top.getStatsPanel().add(space);
		topCancel.addActionListener(this);
		topAttack.addActionListener(this);
		topAbility.addActionListener(this);
		topSummon.addActionListener(this);
		topEnd.addActionListener(this);
		topUse.addActionListener(this);
		Top.getActionsPanel().add(topAbility);
		Top.getActionsPanel().add(topSummon);
		Top.getActionsPanel().add(topAttack);
		Top.getActionsPanel().add(topUse);
		Top.getActionsPanel().add(topCancel);
		Top.getActionsPanel().add(topEnd);

		topSummon.setEnabled(false);
		topAttack.setEnabled(false);
		topUse.setEnabled(false);
		//
		for (int i = 0; i < player1.getHand().size(); i++) {
			String name = player1.getHand().get(i).getName();
			String ManaCost = player1.getHand().get(i).getManaCost() + "";
			String Rarity = player1.getHand().get(i).getRarity() + "";
			String color = "";
			JButton b = new JButton();
			switch (Rarity) {
			case "BASIC":
				color = "ff968d";
				break;
			case "COMMON":
				color = "069a09";
				break;
			case "RARE":
				color = "0080f7";
				break;
			case "EPIC":
				color = "7101da";
				break;
			case "LEGENDARY":
				color = "d5b500";
				break;
			}
			if (player1.getHand().get(i) instanceof Minion) {
				b.setText("<html><b>Minion: </b>"
						+ "<b><u>"
						+ name
						+ "</b></u>"
						+ "</n> <center>Cost: "
						+ ManaCost
						+ "</n><center>HP: "
						+ ((Minion) player1.getHand().get(i)).getMaxHP()
						+ "</n><center>Attack: "
						+ ((Minion) player1.getHand().get(i)).getAttack()
						+ "</n><center>Shield: "
						+ (((Minion) player1.getHand().get(i)).isDivine() ? "YES"
								: "NO")
						+ "</n><center>Sleeping: "
						+ (((Minion) player1.getHand().get(i)).isSleeping() ? "YES"
								: "NO")
						+ "</n><center>Taunt: "
						+ (((Minion) player1.getHand().get(i)).isTaunt() ? "YES"
								: "NO") + "</n><center><font color=#" + color
						+ ">" + Rarity + "</font>");
			} else {
				b.setText("<html><b>Spell: </b>" + "<b><u>" + name + "</b></u>"
						+ "</n> <center>Cost: " + ManaCost
						+ "</n><center><font color=#" + color + ">" + Rarity
						+ "</font>");
			}
			b.addActionListener(this);
			topHandCards.add(b);
			Top.getCardsPanel().add(b);
		}
	}

	public void TopField() {
		topField = new FieldPanel();
		topField.setBackground(Color.DARK_GRAY);
		window.add(topField);
	}

	public void BotField() {
		botField = new FieldPanel();
		botField.setBackground(Color.DARK_GRAY);
		window.add(botField);

	}

	public void Bot(Hero h) {
		Bot = new PlayerPanel(window.getHeight(), window.getWidth(), player2);
		Bot.getPlayer().addActionListener(this);
		Bot.setBackground(Color.orange);
		window.add(Bot);
		//
		botStats.setOpaque(false);
		botStats.setEditable(false);
		botStats.setText(p2name + "\n" + "HP " + player2.getCurrentHP()
				+ "\n\n" + "Mana " + player2.getCurrentManaCrystals() + "/"
				+ player2.getTotalManaCrystals() + "\n\n" + "Deck "
				+ player2.getDeck().size());
		botStats.setFont(new Font("Segoe Script", Font.BOLD, 15));
		botStats.setForeground(Color.RED);
		Bot.getStatsPanel().add(botStats);
		JTextArea space = new JTextArea("              ");
		space.setOpaque(false);
		Bot.getStatsPanel().add(space);
		//
		botCancel.addActionListener(this);
		botAttack.addActionListener(this);
		botAbility.addActionListener(this);
		botSummon.addActionListener(this);
		botEnd.addActionListener(this);
		botUse.addActionListener(this);
		Bot.getActionsPanel().add(botAbility);
		Bot.getActionsPanel().add(botSummon);
		Bot.getActionsPanel().add(botAttack);
		Bot.getActionsPanel().add(botUse);
		Bot.getActionsPanel().add(botCancel);
		Bot.getActionsPanel().add(botEnd);
		botSummon.setEnabled(false);
		botAttack.setEnabled(false);
		botUse.setEnabled(false);
		//
		for (int i = 0; i < player2.getHand().size(); i++) {
			String name = player2.getHand().get(i).getName();
			String ManaCost = player2.getHand().get(i).getManaCost() + "";
			String Rarity = player2.getHand().get(i).getRarity() + "";
			String color = "";
			JButton b = new JButton();

			switch (Rarity) {
			case "BASIC":
				color = "ff968d";
				break;
			case "COMMON":
				color = "069a09";
				break;
			case "RARE":
				color = "0080f7";
				break;
			case "EPIC":
				color = "7101da";
				break;
			case "LEGENDARY":
				color = "d5b500";
				break;
			}
			if (player2.getHand().get(i) instanceof Minion) {
				b.setText("<html><b>Minion: </b>"
						+ "</n>"
						+ "<b><u>"
						+ name
						+ "</b></u>"
						+ "</n> <center>Cost: "
						+ ManaCost
						+ "</n><center>HP: "
						+ ((Minion) player2.getHand().get(i)).getMaxHP()
						+ "</n><center>Attack: "
						+ ((Minion) player2.getHand().get(i)).getAttack()
						+ "</n><center>Shield: "
						+ (((Minion) player2.getHand().get(i)).isDivine() ? "YES"
								: "NO")
						+ "</n><center>Sleeping: "
						+ (((Minion) player2.getHand().get(i)).isSleeping() ? "YES"
								: "NO")
						+ "</n><center>Taunt: "
						+ (((Minion) player2.getHand().get(i)).isTaunt() ? "YES"
								: "NO") + "</n><center><font color=#" + color
						+ ">" + Rarity + "</font>");
			} else {
				b.setText("<html><b>Spell: </b>" + "</n>" + "<b><u>" + name
						+ "</b></u>" + "</n> <center>Cost: " + ManaCost
						+ "</n><center><font color=#" + color + ">" + Rarity
						+ "</font>");
			}
			b.addActionListener(this);
			botHandCards.add(b);
			Bot.getCardsPanel().add(b);

		}

	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (player1 == null || player2 == null) {
			try {
				createHeros(b.getActionCommand());
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			} catch (FullHandException e1) {
				e1.printStackTrace();
			}
		} else if (b.getActionCommand() == "Ability") {
			try {
				if (!(game.getCurrentHero() instanceof Mage)
						&& !(game.getCurrentHero() instanceof Priest)) {
					game.getCurrentHero().useHeroPower();
				} else if (game.getCurrentHero() instanceof Mage) {
					if (Selected == Top.getPlayer())
						((Mage) game.getCurrentHero()).useHeroPower(player1);
					else if (Selected == Bot.getPlayer()) {
						((Mage) game.getCurrentHero()).useHeroPower(player2);
					} else if (topFieldCards.contains(Selected)) {
						((Mage) game.getCurrentHero()).useHeroPower(player1
								.getField()
								.get(topFieldCards.indexOf(Selected)));
					} else if (botFieldCards.contains(Selected)) {
						((Mage) game.getCurrentHero()).useHeroPower(player2
								.getField()
								.get(botFieldCards.indexOf(Selected)));
					}
				} else if (game.getCurrentHero() instanceof Priest) {
					if (Selected == Top.getPlayer())
						((Priest) game.getCurrentHero()).useHeroPower(player1);
					else if (Selected == Bot.getPlayer()) {
						((Priest) game.getCurrentHero()).useHeroPower(player2);
					} else if (topFieldCards.contains(Selected)) {
						((Priest) game.getCurrentHero()).useHeroPower(player1
								.getField()
								.get(topFieldCards.indexOf(Selected)));
					} else if (botFieldCards.contains(Selected)) {
						((Priest) game.getCurrentHero()).useHeroPower(player2
								.getField()
								.get(botFieldCards.indexOf(Selected)));
					}

				}

			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "SORRY",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = null;
			onAddCard();
			onChange();

		} else if (e.getActionCommand().equals("Attack") && Selected != null
				&& Target != null) {
			if (player1 == game.getCurrentHero()) {
				if (Target == Bot.getPlayer()) {
					try {
						game.getCurrentHero().attackWithMinion(
								game.getCurrentHero().getField()
										.get(topFieldCards.indexOf(Selected)),
								game.getOpponent());
					} catch (CannotAttackException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (TauntBypassException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotSummonedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					try {
						game.getCurrentHero().attackWithMinion(
								game.getCurrentHero().getField()
										.get(topFieldCards.indexOf(Selected)),
								game.getOpponent().getField()
										.get(botFieldCards.indexOf(Target)));
					} catch (CannotAttackException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (TauntBypassException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotSummonedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					}
			} else if (player2 == game.getCurrentHero()) {
				if (Target == Top.getPlayer()) {
					try {
						game.getCurrentHero().attackWithMinion(
								game.getCurrentHero().getField()
										.get(botFieldCards.indexOf(Selected)),
								game.getOpponent());
					} catch (CannotAttackException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (TauntBypassException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotSummonedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					}
				} else
					try {
						game.getCurrentHero().attackWithMinion(
								game.getCurrentHero().getField()
										.get(botFieldCards.indexOf(Selected)),
								game.getOpponent().getField()
										.get(topFieldCards.indexOf(Target)));
					} catch (CannotAttackException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (TauntBypassException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					} catch (NotSummonedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"SORRY", JOptionPane.INFORMATION_MESSAGE);
					}
			}
			topAttack.setEnabled(false);
			botAttack.setEnabled(false);
			onChange();
		}

		else if (b.getActionCommand() == "End Turn") {
			try {
				game.endTurn();
			} catch (FullHandException | CloneNotSupportedException e1) {
				FullHandException x = (FullHandException) e1;
				Card c = x.getBurned();
				if (c instanceof Minion) {
					Minion mon = (Minion) c;
					JOptionPane.showMessageDialog(
							null,
							"CARD BURNED\n" + "Name: " + mon.getName()
									+ "\nMana: " + mon.getManaCost() + "\nHP: "
									+ mon.getMaxHP() + "\nAttack: "
									+ mon.getAttack() + "\nRarity: "
									+ mon.getRarity() + "\nSleeping: "
									+ (mon.isSleeping() ? "YES" : "NO")
									+ "\nShield: "
									+ (mon.isDivine() ? "YES" : "NO")
									+ "\nTaunt: "
									+ (mon.isTaunt() ? "YES" : "NO"), x
									.getMessage().toUpperCase(),
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"CARD BURNED\n" + "Name: " + c.getName()
									+ "\nMana: " + c.getManaCost()
									+ "\nRarity: " + c.getRarity(), x
									.getMessage().toUpperCase(),
							JOptionPane.INFORMATION_MESSAGE);

				}
			}
			onEndTurn();

		} else if ((Selected != null && Selected == b)
				|| b.getActionCommand().equals("Cancel")) {
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = null;
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			topSummon.setEnabled(false);
			botSummon.setEnabled(false);
			topUse.setEnabled(false);
			topAttack.setEnabled(false);
			botAttack.setEnabled(false);
			botUse.setEnabled(false);
			playFX("Sounds/FX_Warlock_Cancel.wav");
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}

		} else if (topHandCards.contains(b)) {
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = b;
			Selected.setBackground(Color.red);
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			Card c = player1.getHand().get(topHandCards.indexOf(Selected));
			if (c instanceof Minion) {
				topSummon.setEnabled(true);
				topUse.setEnabled(false);
				topAttack.setEnabled(false);
			} else if (!(c instanceof MinionTargetSpell || c instanceof LeechingSpell)) {
				topUse.setEnabled(true);
				topSummon.setEnabled(false);
				topAttack.setEnabled(false);
			}

			else {
				topUse.setEnabled(false);
				topSummon.setEnabled(false);
				topAttack.setEnabled(false);
			}
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}

		} else if (Selected != null
				&& topHandCards.contains(Selected)
				&& (player1.getHand().get(topHandCards.indexOf(Selected)) instanceof MinionTargetSpell || player1
						.getHand().get(topHandCards.indexOf(Selected)) instanceof LeechingSpell)
				&& (topFieldCards.contains(b) || botFieldCards.contains(b))) {
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = b;
			Target.setBackground(Color.GREEN);
			topUse.setEnabled(true);
		} else if (Selected != null
				&& topHandCards.contains(Selected)
				&& player1.getHand().get(topHandCards.indexOf(Selected)) instanceof HeroTargetSpell
				&& (Bot.getPlayer() == b || Top.getPlayer() == b)) {
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = b;
			Target.setBackground(Color.GREEN);
			topUse.setEnabled(true);
		}

		else if (botHandCards.contains(b)) {
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = b;
			Selected.setBackground(Color.red);
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			Card c = player2.getHand().get(botHandCards.indexOf(Selected));
			if (c instanceof Minion) {
				botSummon.setEnabled(true);
				botUse.setEnabled(false);
				botAttack.setEnabled(false);
			} else if (!(c instanceof MinionTargetSpell || c instanceof LeechingSpell)) {
				botUse.setEnabled(true);
				botSummon.setEnabled(false);
				botAttack.setEnabled(false);
			} else {
				botUse.setEnabled(false);
				botSummon.setEnabled(false);
				botAttack.setEnabled(false);
			}
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}

		} else if (Selected != null
				&& botHandCards.contains(Selected)
				&& (player2.getHand().get(botHandCards.indexOf(Selected)) instanceof MinionTargetSpell || player2
						.getHand().get(botHandCards.indexOf(Selected)) instanceof LeechingSpell)
				&& (topFieldCards.contains(b) || botFieldCards.contains(b))) {
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = b;
			Target.setBackground(Color.GREEN);
			botUse.setEnabled(true);

		} else if (Selected != null
				&& botHandCards.contains(Selected)
				&& player2.getHand().get(botHandCards.indexOf(Selected)) instanceof HeroTargetSpell
				&& (Top.getPlayer() == b || Bot.getPlayer() == b)) {
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = b;
			Target.setBackground(Color.GREEN);
			botUse.setEnabled(true);

		} else if (b.getActionCommand().equals("Summon")
				|| b.getActionCommand().equals("Use")) {
			onSummon();

		}

		else if ((topFieldCards.contains(b) && player1 == game.getCurrentHero())
				|| (botFieldCards.contains(b) && player2 == game
						.getCurrentHero())) {
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = b;
			Selected.setBackground(Color.red);
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			topSummon.setEnabled(false);
			topUse.setEnabled(false);
			topAttack.setEnabled(false);
			botSummon.setEnabled(false);
			botUse.setEnabled(false);
			botAttack.setEnabled(false);
			if ((game.getCurrentHero() instanceof Mage || game.getCurrentHero() instanceof Priest)
					&& player1 == game.getCurrentHero()) {
				topAbility.setEnabled(true);
			} else if ((game.getCurrentHero() instanceof Mage || game
					.getCurrentHero() instanceof Priest)
					&& player2 == game.getCurrentHero()) {
				botAbility.setEnabled(true);
			}
		} else if (Selected == null
				&& (game.getCurrentHero() instanceof Mage || game
						.getCurrentHero() instanceof Priest)
				&& (topFieldCards.contains(b) || botFieldCards.contains(b))) {
			if (game.getCurrentHero() == player1) {
				topAbility.setEnabled(true);
			} else
				botAbility.setEnabled(true);
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = b;
			Selected.setBackground(Color.red);
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
		} else if (Selected != null && topFieldCards.contains(Selected)
				&& (botFieldCards.contains(b) || b == Bot.getPlayer())) {
			Target = b;
			Target.setBackground(Color.GREEN);
			topAttack.setEnabled(true);
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}
		} else if (Selected != null && botFieldCards.contains(Selected)
				&& (topFieldCards.contains(b) || b == Top.getPlayer())) {
			Target = b;
			Target.setBackground(Color.GREEN);
			botAttack.setEnabled(true);
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}
		} else if ((game.getCurrentHero() instanceof Mage || game
				.getCurrentHero() instanceof Priest)
				&& (b == Top.getPlayer() || b == Bot.getPlayer())) {
			if (game.getCurrentHero() == player1) {
				topAbility.setEnabled(true);
			} else
				botAbility.setEnabled(true);
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = b;
			Selected.setBackground(Color.red);
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
		}

		else {
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			}
			if (Selected != null)
				Selected.setBackground(new JButton().getBackground());
			Selected = null;
			if (Target != null)
				Target.setBackground(new JButton().getBackground());
			Target = null;
			topAttack.setEnabled(false);
			topUse.setEnabled(false);
			topSummon.setEnabled(false);
			botAttack.setEnabled(false);
			botSummon.setEnabled(false);
			botUse.setEnabled(false);

		}
	}

	public void createHeros(String h) throws IOException,
			CloneNotSupportedException, FullHandException {
		if (player1 == null) {
			createHero1(h);
		} else
			createHero2(h);
	}

	public void createHero1(String h) throws IOException,
			CloneNotSupportedException {
		switch (h) {
		case "Hunter":
			player1 = new Hunter();
			break;
		case "Mage":
			player1 = new Mage();
			break;
		case "Paladin":
			player1 = new Paladin();
			break;
		case "Priest":
			player1 = new Priest();
			break;
		case "Warlock":
			player1 = new Warlock();
			break;
		}
		while (p1name == null || p1name.isEmpty()) {
			p1name = JOptionPane.showInputDialog("ENTER YOUR NAME PLAYER 1: ");
			if (p1name == null) {
				player1 = null;
				return;
			}
		}
		p1name = p1name.toUpperCase();
		JOptionPane.showMessageDialog(null,
				p1name + " will play as ".toUpperCase() + h.toUpperCase(),
				"PLAYER1", JOptionPane.INFORMATION_MESSAGE);

		topText.setText("Player 2, Select a hero to start!");
	}

	public void createHero2(String h) throws IOException,
			CloneNotSupportedException, FullHandException {
		switch (h) {
		case "Hunter":
			player2 = new Hunter();
			break;
		case "Mage":
			player2 = new Mage();
			break;
		case "Paladin":
			player2 = new Paladin();
			break;
		case "Priest":
			player2 = new Priest();
			break;
		case "Warlock":
			player2 = new Warlock();
			break;
		}
		while (p2name == null || p2name.isEmpty()) {
			p2name = JOptionPane.showInputDialog("ENTER YOUR NAME PLAYER 2: ");
			if (p2name == null) {
				player2 = null;
				return;
			}
		}
		p2name = p2name.toUpperCase();
		JOptionPane.showMessageDialog(null,
				p2name + " will play as ".toUpperCase() + h.toUpperCase(),
				"PLAYER2", JOptionPane.INFORMATION_MESSAGE);
		window.getContentPane().removeAll();
		window.repaint();
		window.playWindow();
		game = new Game(player1, player2);
		game.setListener(this);
		Top(player1);
		TopField();
		BotField();
		Bot(player2);
		if (player1 == game.getCurrentHero()) {
			// JOptionPane.showOptionDialog(null, p1name,"Empty?",
			// JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null,
			// new Object[]{}, null);

			for (int i = 0; i < player2.getHand().size(); i++) {
				botHandCards.get(i).setVisible(false);
			}
			botEnd.setEnabled(false);
			botAbility.setEnabled(false);
			botCancel.setEnabled(false);
			Top.setBorder(new TitledBorder(new EtchedBorder(), p1name
					+ "'s TURN"));
			Bot.setBorder(new TitledBorder(new EtchedBorder(), p2name
					+ " ----> Cards in hand:" + player2.getHand().size()));

		} else {
			// JOptionPane.showOptionDialog(null, p2name,"Empty?",
			// JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null,
			// new Object[]{}, null);

			for (int i = 0; i < player1.getHand().size(); i++) {
				topHandCards.get(i).setVisible(false);

			}
			Bot.setBorder(new TitledBorder(new EtchedBorder(), p2name
					+ "'s TURN"));
			Top.setBorder(new TitledBorder(new EtchedBorder(), p1name
					+ " ----> Cards in hand:" + player1.getHand().size()));
			topEnd.setEnabled(false);
			topAbility.setEnabled(false);
			topCancel.setEnabled(false);
		}
		if (game.getCurrentHero() instanceof Mage
				|| game.getCurrentHero() instanceof Priest) {
			botAbility.setEnabled(false);
			topAbility.setEnabled(false);
		}
		clip.stop();
		window.revalidate();
		window.repaint();
		playSound("Sounds/Duel.wav");
	}

	public void onAddCard() {
		Hero x = game.getCurrentHero();
		if (x == player1) {
			topHandCards = new ArrayList<JButton>();
			Top.getCardsPanel().removeAll();
		} else {
			botHandCards = new ArrayList<JButton>();
			Bot.getCardsPanel().removeAll();
		}
		for (int i = 0; i < x.getHand().size(); i++) {
			String name = x.getHand().get(i).getName();
			String ManaCost = x.getHand().get(i).getManaCost() + "";
			String Rarity = x.getHand().get(i).getRarity() + "";
			String color = "";
			JButton b = new JButton();
			switch (Rarity) {
			case "BASIC":
				color = "ff968d";
				break;
			case "COMMON":
				color = "069a09";
				break;
			case "RARE":
				color = "0080f7";
				break;
			case "EPIC":
				color = "7101da";
				break;
			case "LEGENDARY":
				color = "d5b500";
				break;
			}
			if (x.getHand().get(i) instanceof Minion) {
				b.setText("<html><b>Minion: </b>"
						+ "<b><u>"
						+ name
						+ "</b></u>"
						+ "</n> <center>Cost: "
						+ ManaCost
						+ "</n><center>HP: "
						+ ((Minion) x.getHand().get(i)).getMaxHP()
						+ "</n><center>Attack: "
						+ ((Minion) x.getHand().get(i)).getAttack()
						+ "</n><center>Shield: "
						+ (((Minion) x.getHand().get(i)).isDivine() ? "YES"
								: "NO")
						+ "</n><center>Sleeping: "
						+ (((Minion) x.getHand().get(i)).isSleeping() ? "YES"
								: "NO")
						+ "</n><center>Taunt: "
						+ ((((Minion) x.getHand().get(i)).isTaunt()) ? "YES"
								: "NO") + "</n><center><font color=#" + color
						+ ">" + Rarity + "</font>");
			} else {
				b.setText("<html><b>Spell: </b>" + "<b><u>" + name + "</b></u>"
						+ "</n> <center>Cost: " + ManaCost
						+ "</n><center><font color=#" + color + ">" + Rarity
						+ "</font>");
			}
			b.addActionListener(this);
			if (x == player1) {
				topHandCards.add(b);
				Top.getCardsPanel().add(b);
			} else {
				botHandCards.add(b);
				Bot.getCardsPanel().add(b);
			}

		}
	}

	public void onEndTurn() {
		playFX("Sounds/FX_EndTurn_Up.wav");
		if (Selected != null)
			Selected.setBackground(new JButton().getBackground());
		Selected = null;
		if (Target != null)
			Target.setBackground(new JButton().getBackground());
		Target = null;
		onAddCard();
		if (player1 == game.getCurrentHero()) {
			topStats.setText(p1name + "\n" + "HP " + player1.getCurrentHP()
					+ "\n\n" + "Mana " + player1.getCurrentManaCrystals() + "/"
					+ player1.getTotalManaCrystals() + "\n\n" + "Deck "
					+ player1.getDeck().size());
			// botStats.setText(p2name + "\n" + "HP " + player2.getCurrentHP()
			// + "\n\n" + "Mana " + player2.getCurrentManaCrystals() + "/"
			// + player2.getTotalManaCrystals() + "\n\n" + "Deck "
			// + player2.getDeck().size());
			for (int i = 0; i < player2.getHand().size(); i++) {
				botHandCards.get(i).setVisible(false);

			}
			for (int i = 0; i < player1.getHand().size(); i++) {
				topHandCards.get(i).setVisible(true);

			}
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			} else {
				topAbility.setEnabled(true);
			}
			Top.setBorder(new TitledBorder(new EtchedBorder(), p1name
					+ "'s TURN"));
			Bot.setBorder(new TitledBorder(new EtchedBorder(), p2name
					+ " ----> Cards in hand:" + player2.getHand().size()));
			topEnd.setEnabled(true);
			topCancel.setEnabled(true);
			botAbility.setEnabled(false);
			botEnd.setEnabled(false);
			botSummon.setEnabled(false);
			botCancel.setEnabled(false);
			botUse.setEnabled(false);
		} else {
			botStats.setText(p2name + "\n" + "HP " + player2.getCurrentHP()
					+ "\n\n" + "Mana " + player2.getCurrentManaCrystals() + "/"
					+ player2.getTotalManaCrystals() + "\n\n" + "Deck "
					+ player2.getDeck().size());
			// topStats.setText(p1name + "\n" + "HP " + player1.getCurrentHP()
			// + "\n\n" + "Mana " + player1.getCurrentManaCrystals() + "/"
			// + player1.getTotalManaCrystals() + "\n\n" + "Deck "
			// + player1.getDeck().size());
			for (int i = 0; i < player1.getHand().size(); i++) {
				topHandCards.get(i).setVisible(false);

			}
			for (int i = 0; i < player2.getHand().size(); i++) {
				botHandCards.get(i).setVisible(true);

			}
			if (game.getCurrentHero() instanceof Mage
					|| game.getCurrentHero() instanceof Priest) {
				botAbility.setEnabled(false);
				topAbility.setEnabled(false);
			} else {
				botAbility.setEnabled(true);
			}
			Bot.setBorder(new TitledBorder(new EtchedBorder(), p2name
					+ "'s TURN"));
			Top.setBorder(new TitledBorder(new EtchedBorder(), p1name
					+ " ----> Cards in hand:" + player1.getHand().size()));
			botEnd.setEnabled(true);
			botCancel.setEnabled(true);
			topAbility.setEnabled(false);
			topEnd.setEnabled(false);
			topUse.setEnabled(false);
			topSummon.setEnabled(false);
			topCancel.setEnabled(false);
		}
		onChange();
	}

	public void onChange() {
		topStats.setText(p1name + "\n" + "HP " + player1.getCurrentHP()
				+ "\n\n" + "Mana " + player1.getCurrentManaCrystals() + "/"
				+ player1.getTotalManaCrystals() + "\n\n" + "Deck "
				+ player1.getDeck().size());
		botStats.setText(p2name + "\n" + "HP " + player2.getCurrentHP()
				+ "\n\n" + "Mana " + player2.getCurrentManaCrystals() + "/"
				+ player2.getTotalManaCrystals() + "\n\n" + "Deck "
				+ player2.getDeck().size());
		topFieldCards = new ArrayList<JButton>();
		botFieldCards = new ArrayList<JButton>();
		topField.removeAll();
		botField.removeAll();
		for (int i = 0; i < player1.getField().size(); i++) {
			String name = player1.getField().get(i).getName();
			String ManaCost = player1.getField().get(i).getManaCost() + "";
			String Rarity = player1.getField().get(i).getRarity() + "";
			String color = "";
			JButton mon = new JButton();

			switch (Rarity) {
			case "BASIC":
				color = "ff968d";
				break;
			case "COMMON":
				color = "069a09";
				break;
			case "RARE":
				color = "0080f7";
				break;
			case "EPIC":
				color = "7101da";
				break;
			case "LEGENDARY":
				color = "d5b500";
				break;
			}
			mon.setText("<html><b>Minion: </b>"
					+ "<b><u>"
					+ name
					+ "</b></u>"
					+ "</n> <center>Cost: "
					+ ManaCost
					+ "</n><center>HP: "
					+ ((Minion) player1.getField().get(i)).getCurrentHP()
					+ "</n><center>Attack: "
					+ ((Minion) player1.getField().get(i)).getAttack()
					+ "</n><center>Shield: "
					+ (((Minion) player1.getField().get(i)).isDivine() ? "YES"
							: "NO")
					+ "</n><center>Sleeping: "
					+ (((Minion) player1.getField().get(i)).isSleeping() ? "YES"
							: "NO")
					+ "</n><center>Taunt: "
					+ (((Minion) player1.getField().get(i)).isTaunt() ? "YES"
							: "NO") + "</n><center><font color=#" + color + ">"
					+ Rarity + "</font>");
			mon.addActionListener(this);
			topField.add(mon);
			topFieldCards.add(mon);
		}
		for (int i = 0; i < player2.getField().size(); i++) {
			String name = player2.getField().get(i).getName();
			String ManaCost = player2.getField().get(i).getManaCost() + "";
			String Rarity = player2.getField().get(i).getRarity() + "";
			String color = "";
			JButton mon = new JButton();

			switch (Rarity) {
			case "BASIC":
				color = "ff968d";
				break;
			case "COMMON":
				color = "069a09";
				break;
			case "RARE":
				color = "0080f7";
				break;
			case "EPIC":
				color = "7101da";
				break;
			case "LEGENDARY":
				color = "d5b500";
				break;
			}
			mon.setText("<html><b>Minion: </b>"
					+ "</n>"
					+ "<b><u>"
					+ name
					+ "</b></u>"
					+ "</n> <center>Cost: "
					+ ManaCost
					+ "</n><center>HP: "
					+ ((Minion) player2.getField().get(i)).getCurrentHP()
					+ "</n><center>Attack: "
					+ ((Minion) player2.getField().get(i)).getAttack()
					+ "</n><center>Shield: "
					+ (((Minion) player2.getField().get(i)).isDivine() ? "YES"
							: "NO")
					+ "</n><center>Sleeping: "
					+ (((Minion) player2.getField().get(i)).isSleeping() ? "YES"
							: "NO")
					+ "</n><center>Taunt: "
					+ (((Minion) player2.getField().get(i)).isTaunt() ? "YES"
							: "NO") + "</n><center><font color=#" + color + ">"
					+ Rarity + "</font>");
			mon.addActionListener(this);
			botField.add(mon);
			botFieldCards.add(mon);
		}
		window.revalidate();
		window.repaint();

	}

	public void onSummon() {
		if (game.getCurrentHero() == player1) {
			Card c = player1.getHand().get(topHandCards.indexOf(Selected));
			if (c instanceof Minion) {
				try {
					player1.playMinion((Minion) c);
					topHandCards.remove(Selected);
					Top.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_MinionSummon01_DrawFromHand_01.wav");
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			else if (c instanceof AOESpell) {
				try {
					game.getCurrentHero().castSpell((AOESpell) c,
							game.getOpponent().getField());
					topHandCards.remove(Selected);
					Top.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}
				;

			} else if (c instanceof FieldSpell) {
				try {
					game.getCurrentHero().castSpell((FieldSpell) c);
					topHandCards.remove(Selected);
					Top.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}

			} else if (c instanceof HeroTargetSpell
					|| c instanceof MinionTargetSpell) {
				if (Target == Top.getPlayer())
					try {
						game.getCurrentHero().castSpell((HeroTargetSpell) c,
								player1);
					} catch (NotYourTurnException | NotEnoughManaException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else if (Target == Bot.getPlayer())
					try {
						game.getCurrentHero().castSpell((HeroTargetSpell) c,
								player2);
					} catch (NotYourTurnException | NotEnoughManaException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else if (topFieldCards.contains(Target))
					try {
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player1.getField().get(
										topFieldCards.indexOf(Target)));
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else
					try {
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player2.getField().get(
										botFieldCards.indexOf(Target)));
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				topHandCards.remove(Selected);
				Top.getCardsPanel().remove(Selected);
				playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
			} else if (c instanceof MinionTargetSpell) {
				try {
					if (topFieldCards.contains(Target))
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player1.getField().get(
										topFieldCards.indexOf(Target)));
					else
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player2.getField().get(
										botFieldCards.indexOf(Target)));
					topHandCards.remove(Selected);
					Top.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException
						| InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}

			} else if (c instanceof LeechingSpell) {
				try {
					game.getCurrentHero().castSpell(
							(LeechingSpell) c,
							game.getOpponent().getField()
									.get(botFieldCards.indexOf(Target)));
					topHandCards.remove(Selected);
					Top.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}

			}

		} else {
			Card c = player2.getHand().get(botHandCards.indexOf(Selected));
			if (c instanceof Minion) {
				try {
					player2.playMinion((Minion) c);
					botHandCards.remove(Selected);
					Bot.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_MinionSummon01_DrawFromHand_01.wav");
				} catch (NotYourTurnException | NotEnoughManaException
						| FullFieldException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (c instanceof AOESpell) {
				try {
					game.getCurrentHero().castSpell((AOESpell) c,
							game.getOpponent().getField());
					botHandCards.remove(Selected);
					Bot.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}
				;

			} else if (c instanceof FieldSpell) {
				try {
					game.getCurrentHero().castSpell((FieldSpell) c);
					botHandCards.remove(Selected);
					Bot.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}

			} else if (c instanceof HeroTargetSpell
					|| c instanceof MinionTargetSpell) {
				if (Target == Top.getPlayer())
					try {
						game.getCurrentHero().castSpell((HeroTargetSpell) c,
								player1);
					} catch (NotYourTurnException | NotEnoughManaException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else if (Target == Bot.getPlayer())
					try {
						game.getCurrentHero().castSpell((HeroTargetSpell) c,
								player2);
					} catch (NotYourTurnException | NotEnoughManaException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else if (topFieldCards.contains(Target))
					try {
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player1.getField().get(
										topFieldCards.indexOf(Target)));
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				else
					try {
						game.getCurrentHero().castSpell(
								(MinionTargetSpell) c,
								player2.getField().get(
										botFieldCards.indexOf(Target)));
					} catch (NotYourTurnException | NotEnoughManaException
							| InvalidTargetException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(),
								"INFO", JOptionPane.INFORMATION_MESSAGE);
					}
				botHandCards.remove(Selected);
				Bot.getCardsPanel().remove(Selected);
				playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
			} else if (c instanceof LeechingSpell) {
				try {
					game.getCurrentHero().castSpell(
							(LeechingSpell) c,
							game.getOpponent().getField()
									.get(topFieldCards.indexOf(Target)));
					botHandCards.remove(Selected);
					Bot.getCardsPanel().remove(Selected);
					playFX("Sounds/FX_FreezeEvent_SpellCast.wav");
				} catch (NotYourTurnException | NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"INFO", JOptionPane.INFORMATION_MESSAGE);
				}

			}

		}
		onChange();
		onAddCard();
		if (Selected != null)
			Selected.setBackground(new JButton().getBackground());
		Selected = null;
		if (Target != null)
			Target.setBackground(new JButton().getBackground());
		Target = null;
		botSummon.setEnabled(false);
		botUse.setEnabled(false);
		topUse.setEnabled(false);
		topSummon.setEnabled(false);
	}

	public void playSound(String filepath) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(filepath).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();

		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void playFX(String filepath) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(filepath).getAbsoluteFile());
			fx = AudioSystem.getClip();
			fx.open(audioInputStream);
			fx.start();

		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FullHandException,
			CloneNotSupportedException {
		new controller();
	}

}
