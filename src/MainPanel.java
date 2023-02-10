/*
 * This JPanel class sets the structure of the application by creating and instantiating every button and
 * JPanel with their respective layouts.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;

public class MainPanel extends JPanel
{
	public static final int EASY_LIVES = 3, NORMAL_LIVES = 2, HARD_LIVES = 1;
	
	//Instance Variables
	private GamePanel gamePanel;
	private JLabel scoreLabel;
	
	private ButtonGroup avatarButtonGroup;
	private JPanel statusRow;
	private JPanel controlPanel;
	private JPanel avatarRow;
	private JPanel enemyCountRow;
	private JPanel diffRow;
	private JPanel optionsRow;
	
	private JLabel enemyCountLabel; 
	private JLabel endGameLabel;
	private JLabel livesLabel;
	
	private AvatarRadioButton av1, av2, av3;
	private JLabel enemyPromptLabel;
	private JTextField enemyInputField;
	private JButton easyButton, normalButton, hardButton;
	private JButton startButton, pauseButton, restartButton, quitButton;
	
	private EnemyCounterHolder enemyCounterHolder;
	private GameTimer gameTimer;
	
	private Color defaultButtonBackground;
	
	private int difficultyLives;
	
	//Constructor
	public MainPanel()
	{
		super(new BorderLayout());
		enemyCounterHolder = new EnemyCounterHolder();
		
		controlPanel = new JPanel(new GridLayout(4, 1));
		
		avatarButtonGroup = new ButtonGroup();
		statusRow = new JPanel(new GridLayout(1, 2));
		avatarRow = new JPanel();
		enemyCountRow = new JPanel();
		diffRow = new JPanel();
		optionsRow = new JPanel();
		
		scoreLabel = new JLabel("Score: 0");
		livesLabel = new JLabel("Lives: 2");
		statusRow.add(scoreLabel);
		statusRow.add(livesLabel);
		
		enemyPromptLabel = new JLabel("Enter the Number of Enemies (Press \"Enter\" to Confirm): ");
		enemyInputField = new JTextField();
		enemyInputField.setColumns(5);
		enemyInputField.setEditable(true);
		enemyCountLabel = new JLabel("Number of Enemies: " + enemyCounterHolder.getEnemyCount());
		enemyInputField.addKeyListener(new EnterListener());

		gamePanel = new GamePanel(enemyCounterHolder, enemyCountLabel, scoreLabel, livesLabel, this);
		gamePanel.setPreferredSize(new Dimension(850, 650));
		
		av1 = new AvatarRadioButton("Avatar 1", true, gamePanel, avatarButtonGroup);
		av2 = new AvatarRadioButton("Avatar 2", false, gamePanel, avatarButtonGroup);
		av3 = new AvatarRadioButton("Avatar 3", false, gamePanel, avatarButtonGroup);
		avatarRow.add(av1);
		avatarRow.add(av2);
		avatarRow.add(av3);
		
		enemyCountRow.add(enemyPromptLabel);
		enemyCountRow.add(enemyInputField);
		enemyCountRow.add(enemyCountLabel);
		
		easyButton = new JButton("Easy");
		easyButton.addActionListener(new DifficultyListener(easyButton));
		defaultButtonBackground = easyButton.getBackground();
		
		normalButton = new JButton("Normal");
		normalButton.addActionListener(new DifficultyListener(normalButton));
		
		hardButton = new JButton("Hard");
		hardButton.addActionListener(new DifficultyListener(hardButton));
		
		endGameLabel = new JLabel();
		
		diffRow.add(easyButton);
		diffRow.add(normalButton);
		diffRow.add(hardButton);
		diffRow.add(endGameLabel);
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		
		pauseButton = new JButton("Pause");
		pauseButton.setVisible(false);
		pauseButton.addActionListener(new PauseListener());
		
		restartButton = new JButton("Restart");
		restartButton.setVisible(false);
		restartButton.addActionListener(new RestartListener());
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		
		optionsRow.add(startButton);
		optionsRow.add(pauseButton);
		optionsRow.add(restartButton);
		optionsRow.add(quitButton);
		
		controlPanel.add(avatarRow);
		controlPanel.add(enemyCountRow);
		controlPanel.add(diffRow);
		controlPanel.add(optionsRow);
		
		gameTimer = new GameTimer(gamePanel);
		
		this.add(statusRow, BorderLayout.NORTH);
		this.add(gamePanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.SOUTH);
		
		av1.setFocusable(false);
		av2.setFocusable(false);
		av3.setFocusable(false);
		enemyPromptLabel.setFocusable(false);
		easyButton.setFocusable(false);
		normalButton.setFocusable(false);
		hardButton.setFocusable(false);
		startButton.setFocusable(false);
		pauseButton.setFocusable(false);
		restartButton.setFocusable(false);
		quitButton.setFocusable(false);
	}
	
	//A simple method to remotely start the timer
	public void startTimer()
	{
		gameTimer.start();
	}
	
	//A simple method to remotely stop the timer
	public void stopTimer()
	{
		gameTimer.stop();
	}
	
	//A method to display a lose/win message above the optionsRow
	public void showEndGameLabel(int option)
	{
		pauseButton.setVisible(false);
		restartButton.setVisible(true);
		
		if (option == 0)
		{
			endGameLabel.setText("Game Over!");
			endGameLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
		}
		else
		{
			endGameLabel.setText("Winner!");
			endGameLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
		}
		endGameLabel.setVisible(true);
	}
	
	public int getDifficultyLives()
	{
		return difficultyLives;
	}
	
	//This inner class calls the set enemies method from the gamePanel to fill 
	//the ArrayList of enemies and starts the gameTimer to start the game. Also, it
	//hides the buttons for a clean UI.
	private class StartListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (enemyCounterHolder.getEnemyCount() > 0)
			{
				gamePanel.setEnemies();
				gameTimer.start();
				startButton.setVisible(false);
				pauseButton.setVisible(true);
				av1.setVisible(false);
				av2.setVisible(false);
				av3.setVisible(false);
				enemyPromptLabel.setVisible(false);
				enemyInputField.setVisible(false);
				easyButton.setVisible(false);
				normalButton.setVisible(false);
				hardButton.setVisible(false);
			}
		}
	}
	
	//This inner class changes the text of the pauseButton (JButton) and either pauses 
	//or resumes the game depending on what is displayed at the time of pressing.
	private class PauseListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (pauseButton.getText().equals("Resume"))
			{
				gameTimer.start();
				pauseButton.setText("Pause");
			}
			else
			{
				gameTimer.stop();
				pauseButton.setText("Resume");
			}
		}
	}
	
	//This inner class hides the win/lose text, the restartButton, reveals the pauseButton
	//and calls the restart method in the gamePanel by pressing the "Restart" button
	//when it is visible.
	private class RestartListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			endGameLabel.setVisible(false);
			restartButton.setVisible(false);
			pauseButton.setVisible(true);
			gamePanel.restart();
			
		}
	}
	
	//This inner class implements delays the gameTimer and sets the number of lives depending
	//on which difficulty button is pressed. The harder the difficulty, the faster the enemy
	//will spawn. The lives will vary from 1-3 depending on the difficulty. Each time a button
	//is pressed it will be recolored to indicate that is was pressed and reverts other difficulty
	//buttons to its default color.
	private class DifficultyListener implements ActionListener
	{
		private JButton button;
		
		public DifficultyListener(JButton button)
		{
			this.button = button;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			if (button.getText().equals("Easy"))
			{
				gameTimer.setDelay(15);
				gamePanel.setLives(3);
				livesLabel.setText("Lives: " + gamePanel.getLives());
				difficultyLives = EASY_LIVES;
				Enemy.setMoveSpeed(10); 
				easyButton.setBackground(Color.GREEN);
				normalButton.setBackground(defaultButtonBackground);
				hardButton.setBackground(defaultButtonBackground);
			}
			else if (button.getText().equals("Normal"))
			{
				gameTimer.setDelay(10);
				gamePanel.setLives(2);
				livesLabel.setText("Lives: " + gamePanel.getLives());
				difficultyLives = NORMAL_LIVES;
				Enemy.setMoveSpeed(15); 
				normalButton.setBackground(Color.YELLOW);
				easyButton.setBackground(defaultButtonBackground);
				hardButton.setBackground(defaultButtonBackground);
			}
			else
			{
				gameTimer.setDelay(5);
				gamePanel.setLives(1);
				livesLabel.setText("Lives: " + gamePanel.getLives());
				difficultyLives = HARD_LIVES;
				Enemy.setMoveSpeed(20); 
				hardButton.setBackground(Color.RED);
				easyButton.setBackground(defaultButtonBackground);
				normalButton.setBackground(defaultButtonBackground);
			}
		}
	}
	
	//This inner class sets the number of enemies. Upon and typing the desired number and pressing the 
	//"Enter" key, the number is parsed, the input field is wiped, and then sets that number to 
	//enemyCounterHolder to set the number of enemies that will spawn. It also changes the text
	//of a JLabel to let the player know the program accepted their number.
	private class EnterListener implements KeyListener
	{
		public void keyPressed(KeyEvent e) 
		{
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_ENTER:
				enemyCounterHolder.setNumOfEnemy(Integer.parseInt(enemyInputField.getText()));
				enemyCountLabel.setText("Number of Enemies: " + enemyCounterHolder.getEnemyCount());
				enemyInputField.setText(null);
				break;
			}
		}

		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}
}