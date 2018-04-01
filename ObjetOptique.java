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
	protected Line2D line;

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

	public void move(Point2D newPosition) {
		double translationX = newPosition.getX() - centrex;
		double translationY = newPosition.getY() - centrey;
		centrex = newPosition.getX();
		centrey = newPosition.getY();
		point1 = new Point2D.Double(point1.getX() + translationX, point1.getY() + translationY);
		point2 = new Point2D.Double(point2.getX() + translationX, point2.getY() + translationY);
		line = new Line2D.Double(point1, point2);
	}
	
	public void pointUpdate(Point2D pt1,Point2D pt2){
		taille = Math.abs(Point2D.distance(pt1.getX(),pt1.getY(),pt2.getX(),pt2.getY()))/2;
		if(pt1.getX() <= pt2.getX()){
			angle = -Math.atan2(pt2.getY()-pt1.getY(),Math.abs(pt1.getX()-pt2.getX()));
			centrex = (int)(pt1.getX()+Math.cos(angle)*taille);
			centrey = (int)(pt1.getY()+Math.sin(-angle)*taille);
		}
		else if(pt1.getX() > pt2.getX()){
			angle = -Math.atan2(pt1.getY()-pt2.getY(),Math.abs(pt1.getX()-pt2.getX()));
			centrex = (int)(pt2.getX()+Math.cos(angle)*taille);
			centrey = (int)(pt2.getY()+Math.sin(-angle)*taille);
		}
		line = new Line2D.Double(pt1,pt2);
	}

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

	public double getTaille(){
		return taille;
	}

	public double getAngle(){
		return angle;
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
