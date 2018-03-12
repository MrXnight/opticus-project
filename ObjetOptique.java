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
	protected int centrex,centrey;
	protected Point point1;
	protected Point point2;

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

	public abstract int distancePoint(Point p);

	public abstract void move(Point newPosition);

	public void setFocus(boolean focus){
		this.focus = focus;
	}

	public boolean hasFocus(){
		return focus;
	}
	public int getCentrex(){
		return centrex;
	}
	public int getCentrey(){
		return centrey;
	}

	public Point getPoint1(){
		return point1;
	}
	public Point getPoint2(){
		return point2;
	}
	
    public Point getCentre(){
        Point centre = new Point(centrex, centrey);
        return(centre);
    }

    public abstract Line2D getLine();
}
