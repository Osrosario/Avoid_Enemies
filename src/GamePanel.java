/*
 * This JPanel is responsible for creating and instantiating the avatars and enemy, and contains the method in
 * which the enemy and avatar interact. It's also responsible for manipulating the score and lives values.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel
{
	//Associations
	private EnemyCounterHolder ech;
	private JLabel enemyCountLabel, scoreLabel, livesLabel;
	private MainPanel mainPanel;
	
	//Instance Variables
	private ArrayList<Enemy> enemyQueue = new ArrayList<Enemy>();
	
	public static final int AVATAR_ONE = 1, AVATAR_TWO = 2, AVATAR_THREE = 3;

	private Avatar avatar;
	private Enemy activeEnemy;
	private Timer countDownTimer;
	
	private JLabel countDownLabel;
	private int lastMouseX, lastMouseY;
	private int score;
	private int lives = 2;
	private int countDown = 3;
	private boolean startedGame = false;
	private boolean endedGame = false;
	private boolean clickedOnAvatar;
	
	//Constructor
	public GamePanel(EnemyCounterHolder ech, JLabel enemyCountLabel, JLabel scoreLabel, JLabel livesLabel, MainPanel mainPanel)
	{
		super();
		this.setSize(500, 500);
		this.setPreferredSize(new Dimension(500, 500));
		this.setBackground(Color.LIGHT_GRAY);
		
		this.ech = ech;
		this.enemyCountLabel = enemyCountLabel;
		this.scoreLabel = scoreLabel;
		this.livesLabel = livesLabel;
		this.mainPanel = mainPanel;
		
		avatar = new AvatarOne(this);
		
		countDownLabel = new JLabel();
		countDownLabel.setFont(new Font("Monospace", Font.PLAIN, 100));
		this.add(countDownLabel);
		
		countDownTimer = new Timer(1000, new StartTimer());
		
		this.addMouseListener(new PressListener());
		this.addMouseMotionListener(new DragListener());
		
		this.setFocusable(true);
		this.addKeyListener(new MoveKeyListener());
	}
	
	//This method is responsible for animating the enemy and avatar. If the game has not started, 
	//an enemy will be removed from the enemyQueue and the number of enemies will be subtracted by
	//one to display the remaining enemies bar the one shown in the panel. When started, the method will
	//call the count down timer. Once the timer reaches a number below 0, the enemy will appear and move
	//in a random direction while allowing the player to move the avatar. If the enemy, gets pass the
	//player, a point will be received. If the player is hit, the number of lives and score will be 
	//subtracted by one. If there are no enemies left or player's live reaches zero, the animation will stop.
	public void move()
	{
		if (!startedGame)
		{
			activeEnemy = enemyQueue.remove(0);
			ech.decrementEnemyCount();
			enemyCountLabel.setText("Number of Enemies Left: " + ech.getEnemyCount());
			startedGame = true;
		}
		
		countDownTimer.start();
		
		if (countDown == -1)
		{
			countDownLabel.setVisible(false);
			countDownTimer.stop();
			
			activeEnemy.move();

			//If Enemy is avoided
			if (activeEnemy.getX() > 900)
			{
				score++;
				scoreLabel.setText("Score: " + score);
				
				if (ech.getEnemyCount() > 0)
				{
					activeEnemy = enemyQueue.remove(0);
					ech.decrementEnemyCount();
					enemyCountLabel.setText("Number of Enemies Left: " + ech.getEnemyCount());
				}
				else
				{
					mainPanel.showEndGameLabel(1);
					mainPanel.stopTimer();
					endedGame = true;
				}
			}

			//If Enemy and Avatar intersect
			if (avatar.intersects(activeEnemy.getBoundBox()))
			{
				score--;
				scoreLabel.setText("Score: " + score);
				lives--;
				livesLabel.setText("Lives: " + lives);
				
				if (lives > 0 && enemyQueue.size() == 0)
				{
					mainPanel.showEndGameLabel(0);
					mainPanel.stopTimer();
					endedGame = true;
				}
				else if (lives > 0)
				{
					activeEnemy = enemyQueue.remove(0);
					ech.decrementEnemyCount();
					enemyCountLabel.setText("Number of Enemies Left: " + ech.getEnemyCount());
				}
				else 
				{
					mainPanel.showEndGameLabel(0);
					mainPanel.stopTimer();
					endedGame = true;
				}
			}
		}
		repaint();
	}
	
	//This method instantiates the avatar depending on what the player chooses
	public void setAvatar(int avatarType)
	{
		if (avatarType == AVATAR_ONE)
        {
        	avatar = new AvatarOne(this);
        }
        else if (avatarType == AVATAR_TWO)
        {
        	avatar = new AvatarTwo(this);
        }
        else
        {
        	avatar = new AvatarThree(this);
        }
		
		repaint();
	}
	
	//Adds the number of enemies to the enemyQueue. Number is retrieved from 
	//EnemyCounterHolder class.
	public void setEnemies()
	{
		for (int x = 0; x < ech.getNumOfEnemy(); x++)
		{
			enemyQueue.add(new Enemy(this));
		}
	}
	
	public void setLives(int lives)
	{
		this.lives = lives;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	//This method resets all values needed to restart the animation with correct and
	//initial values
	public void restart()
	{
		activeEnemy.setLocation(0, 900);
		repaint();
		ech.resetEnemyCount();
		setEnemies();
		enemyCountLabel.setText("Number of Enemies Left: " + ech.getEnemyCount());
		lives = mainPanel.getDifficultyLives();
		livesLabel.setText("Lives: " + lives);
		countDown = 3;
		countDownLabel.setText("" + countDown);
		score = 0;
		scoreLabel.setText("Score: " + score);
		endedGame = false;
		startedGame = false;
		countDownLabel.setVisible(true);
		mainPanel.startTimer();
	}
	
	//This method allows the enemy and avatar to be appear inside the JPanel. It will not display the
	//enemy until the count down timer stops.
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D brush = (Graphics2D)g;
		
		if (!startedGame)
		{
			avatar.paint(brush);
		}
		else
		{
			avatar.paint(brush);
	        activeEnemy.paint(brush);
		}
		
		if (endedGame)
		{
			avatar.paint(brush);
		}
	}
	
	//This inner class rewrites the countDownLabel and subtracts the countDown value
	//to simulate a timer.
	private class StartTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			countDownLabel.setText("" + countDown);
			countDown--;
		}
	}
	
	//This inner class sets the integer values to the x/y coordinate of the mouse when it
	//is pressed and determines if the mouse position is bounds of the avatar. This allows the
	//player to press on the avatar and move his mouse anywhere on the screen without having to 
	//worry about staying inside the application window.
	private class PressListener implements MouseListener
    {
		public void mousePressed(MouseEvent e) 
    	{
    		if (!endedGame)
    		{
    			lastMouseX = e.getX();
    			lastMouseY = e.getY();
    			
    			clickedOnAvatar = (avatar.contains((new Point(lastMouseX, lastMouseY))));
    		}
		}
    	
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
    }
    
	//This inner class allows the avatar to be dragged if the position of the mouse when
	//pressed was in bounds of the avatar.
    private class DragListener implements MouseMotionListener
    {
		public void mouseDragged(MouseEvent e) 
		{
			if (!endedGame)
			{
				if (clickedOnAvatar)
				{									  
					int dy = e.getY() - lastMouseY;
					
					if (dy > 0)
					{
						if (avatar.getBottom() < 490)
						{
							avatar.move(0, dy);
						}
					}
					else
					{
						if (avatar.getY() > 5)
						{
							avatar.move(0, dy);
						}
					}
					
					lastMouseX = e.getX();
					lastMouseY = e.getY();
					repaint();
				}
			}

		}

		public void mouseMoved(MouseEvent e) {}
    }
    
    //This inner class allows the avatar to moved by the "Up" and "Down" arrow keys
    //on the keyboard
    private class MoveKeyListener implements KeyListener
    {
		public void keyPressed(KeyEvent e) 
		{
			if (!endedGame)
			{
				switch(e.getKeyCode()) 
				{
				case KeyEvent.VK_UP:
					if (avatar.getY() > 5)
					{
						avatar.move(0, -8);
					}
					repaint();
					break;
					
				case KeyEvent.VK_DOWN:
					if (avatar.getBottom() < 480)
					{
						avatar.move(0, 8);
					}
					repaint();
					break;
				}
			}
		}

		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
    }
}
