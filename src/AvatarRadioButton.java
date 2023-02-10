
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class AvatarRadioButton extends JRadioButton
{
	//Instance Variables
	private GamePanel gamePanel;
	
	//Constructor
	public AvatarRadioButton(String label, boolean option, GamePanel gamePanel, ButtonGroup buttonGroup)
	{
		super(label, option);
		this.gamePanel = gamePanel;
		buttonGroup.add(this);
		this.addActionListener(new AvatarRadioButtonListener(label));
	}
		
	//Inner class that deals with the actions of the radio buttons
	private class AvatarRadioButtonListener implements ActionListener
	{
		private String label;
		
		public AvatarRadioButtonListener(String label) 
		{
			this.label = label;
		}
		
		//Depending on the specific behavior of the radio button set the behaviorHolder to that behavior
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (label.equals("Avatar 1"))
			{
				gamePanel.setAvatar(GamePanel.AVATAR_ONE);
			}
			else if (label.equals("Avatar 2"))
			{
				gamePanel.setAvatar(GamePanel.AVATAR_TWO);
			}
			else
			{
				gamePanel.setAvatar(GamePanel.AVATAR_THREE);
			}
		}
			
	}
}
