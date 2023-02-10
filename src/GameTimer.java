
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class GameTimer extends Timer
{
	private GamePanel gp;
	
	//Constructor
	public GameTimer(GamePanel gp)
	{
		super(10, null);
		this.gp = gp;
		this.addActionListener(new GameListener());
	}
	
	private class GameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			gp.move();
		}
	}
}
