import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;

public abstract class ObjetOptique {
	protected double angle;
	protected Color couleur;
	protected double taille;
	protected boolean focus;
	protected double centrex,centrey;
	protected Point2D point1;
	protected Point2D point2;

	public ObjetOptique (double posx, double posy, double angle, Color col, double taille) {
		centrex = (int)posx;
		centrey = (int)posy;
		this.angle = angle;
		this.taille = taille;
		couleur = col;
		focus = false;
	}
	public ObjetOptique (){}

	public abstract void draw (Graphics2D g2d);

	public abstract int distancePoint(Point2D p);

	public abstract void move(Point2D newPosition);

	public abstract Point2D movePoint(Point2D newPoint,Point2D clickedPoint);

	public void setFocus(boolean focus){
		this.focus = focus;
	}

	public boolean hasFocus(){
		return focus;
	}
	public double getCentrex(){
		return centrex;
	}
	public double getCentrey(){
		return centrey;
	}

	public Point2D getPoint1(){
		return point1;
	}
	public Point2D getPoint2(){
		return point2;
	}

    public Point2D getCentre(){
        Point2D centre = new Point2D.Double(centrex, centrey);
        return(centre);
    }

    public abstract Line2D getLine();
}
