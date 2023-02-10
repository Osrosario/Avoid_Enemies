
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy 
{
	//Associations
	private GamePanel gp;
	
	//Instance Variables
	private Rectangle2D.Double square;
	private BufferedImage image;
	
	private double start, goal;
	private static double moveSpeed;
	
	//Constructor
	public Enemy(GamePanel gp)
	{
		try 
		{
			image = ImageIO.read(new File("TieFighter.png"));
		} 
		catch (IOException e) 
		{
			System.out.println("Error loading enemy image.");
		}
		
		square = new Rectangle2D.Double(); 
		square.setFrame(0, 0, image.getWidth() - 10, image.getHeight() - 10); 
		
		this.gp = gp;
		
		start = (int) (Math.random() * 400);
		goal = (int) (Math.random() * 500);
		
		this.setLocation(0, start);
	}
	
	public void move()
	{
		double move = (goal - start) / 85.0;
		this.setLocation(getX() + moveSpeed, getY() + move);
	}
	
	public void setLocation(double x, double y) 
	{
		square.setFrame(x, y, square.getWidth(), square.getHeight());
	}
	
	public double getX() { return square.getX(); }

	public double getY() { return square.getY(); } 
	
	public Rectangle2D.Double getBoundBox() { return square; } 
	
	public void paint(Graphics2D brush) 
	{
		brush.drawImage(image, (int)getX() - 5, (int)getY() - 5, gp);
	}
	
	public static void setMoveSpeed(int speed)
	{
		moveSpeed = speed;
	}
	
	public boolean contains(Point point) 
	{
		return square.contains(point);
	}
}
