
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.geom.RectangularShape;

public class AvatarTwo implements Avatar
{
	private Rectangle2D square;
	private BufferedImage image;
	private GamePanel gp;
	
	//Constructor
	public AvatarTwo(GamePanel gp) 
	{
		this.gp = gp;
		
		try 
		{
			image = ImageIO.read(new File("src/R2D2.png"));
		} 
		catch (IOException e) 
		{
			System.out.println("Error loading 2nd avatar image.");
		}
		
		
		square = new Rectangle2D.Double();
		square.setFrame(0, 0, image.getWidth() - 10, image.getHeight() - 10);
		this.setLocation(750, 180);
	}	

	public void move(double dx, double dy)
	{
		this.setLocation(getX() + dx, getY() + dy);
	}

	@Override
	public void setLocation(double x, double y) 
	{
		square.setFrame(x, y, square.getWidth(), square.getHeight());
	}

	public double getX() { return square.getX(); }

	public double getY() { return square.getY(); }
	
	@Override
	public double getBottom() { return square.getMaxY(); }

	@Override
	public void paint(Graphics2D brush) 
	{
		brush.drawImage(image, (int)getX() - 5, (int)getY() - 5, gp);
	}

	@Override
	public boolean contains(Point point) 
	{
		return square.contains(point);
	}

	@Override
	public boolean intersects(Rectangle2D.Double boundBox) 
	{
		return square.intersects(boundBox);
	}
}
