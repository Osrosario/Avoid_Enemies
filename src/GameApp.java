/*
 * @Authors Nicholas Tourony, Omar Rosario
 * December 5, 2020
 * Avoid Enemy Game
 * 
 * This program is a game in which the player's objective is avoid the enemy NPC to win. Upon launching the
 * game, the player is met with three choices. The program is designed so the player can choose between 3 predetermined
 * avatars, the number of enemies they want to avoid, and the difficulty of the game. The difficulty of the game will depend on 
 * how many lives/chances the player has to avoid all enemies. Once the "Start" button is clicked, a count down timer will 
 * appear. At this point, the player has to options to move their avatar, either through dragging it with mouse or using the 
 * "Up" and "Down" keys on the keyboard, being restrained to the right side of the screen. If the player avoids the enemy, 
 * another will spawn randomly from the left until all enemies are exhausted. Each time an enemy is avoided, the player gains
 * a point. If the player makes contact with an enemy, the enemy will despawn immediately, another will spawn if there are
 * any left, and a point and life will be subtracted. Win or lose, the game will display a message. If the player wants to try 
 * again, the player can do so by pressing the restart button. The number of enemies will remain the same. When the player is 
 * satisfied, a "Quit" button is available terminating the program and completing the simulation. 
 * 
 */

import javax.swing.JFrame;

public class GameApp extends JFrame
{
	private MainPanel mainPanel;
	
	public GameApp()
	{
		super("Avoid Enemy Game");
		this.setSize(900, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel = new MainPanel();
		
		this.add(mainPanel);
		this.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new GameApp();
	}
}
