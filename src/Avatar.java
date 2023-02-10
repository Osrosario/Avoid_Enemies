/*
 * Interface to ensure each avatar class contains the methods below.
 */

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public interface Avatar 
{
	public void setLocation(double x, double y);
	public double getX();
	public double getY();
	public double getBottom();
	public void move(double dx, double dy);
	public void paint(Graphics2D brush);
	public boolean contains(Point point);
	public boolean intersects(Rectangle2D.Double boundBox);
}
