import java.awt.*;      //Import des différentes librairies Java
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ObjetOptique implements Serializable{        //Classe abstraite de laquelle héritera tout ce qui peut être clacé sur la zone de dessin : lentilles, sources et mirroir
	protected double angle;
	protected Color couleur;
	protected double taille;
	protected boolean focus;
	protected double centrex,centrey;
	protected Point2D.Double point1;
	protected Point2D.Double point2;
	protected Line2D.Double line;
	protected transient JComponent parent;

	public ObjetOptique (double posx, double posy, double angle, Color col, double taille) {
		centrex = (int)posx;
		centrey = (int)posy;
		this.taille = taille;
		couleur = col;
		focus = false;
	}
	public ObjetOptique (){}        //Constructeur vide par défaut

	public abstract void draw (Graphics2D g2d);     //Méthode abstraite draw

	public abstract int distancePoint(Point2D p);     //Méthode abstraite de distance à un point

	public void move(Point2D newPosition) {
		double translationX = newPosition.getX() - centrex;
		double translationY = newPosition.getY() - centrey;
		centrex = newPosition.getX();
		centrey = newPosition.getY();
		point1 = new Point2D.Double(point1.getX() + translationX, point1.getY() + translationY);
		point2 = new Point2D.Double(point2.getX() + translationX, point2.getY() + translationY);
		line = new Line2D.Double(point1, point2);
		parent.repaint();
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
		parent.repaint();
	}

	public void setAngle(double angle){
		this.angle = angle % (Math.PI*2);
		if(this.angle>Math.PI/2){
			this.angle = this.angle - Math.PI;
		}
		else if(this.angle<-Math.PI/2){
			this.angle = this.angle + Math.PI;
		}
		point1 = new Point2D.Double((centrex - taille * Math.cos(angle)), (centrey - taille * Math.sin(-angle)));
		point2 = new Point2D.Double((centrex + taille * Math.cos(angle)), (centrey + taille * Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		parent.repaint();
	}

	public void setCentreX(double centreX){     //Méthode qui redéfini la coordonnée en X du centre le l'objet
		double translationX =centreX-this.centrex ;
		centrex = centreX;
		point1 = new Point2D.Double(point1.getX() + translationX, point1.getY());
		point2 = new Point2D.Double(point2.getX() + translationX, point2.getY());
		line = new Line2D.Double(point1, point2);
		parent.repaint();
	}

	public void setCentreY(double centreY){     //Méthode qui redéfini la coordonnée en Y du centre le l'objet
		double translationY = centreY-this.centrey;
		centrey = centreY;
		point1 = new Point2D.Double(point1.getX(), point1.getY() + translationY);
		point2 = new Point2D.Double(point2.getX(), point2.getY() + translationY);
		line = new Line2D.Double(point1, point2);
		parent.repaint();
	}

	public Point2D movePoint(Point2D newPoint, Point2D clickedPoint) {
		try {
			Robot robot = new Robot();

			if (point1.equals(clickedPoint)) {
				if (Math.abs(Point2D.distance(newPoint.getX(), newPoint.getY(), point2.getX(), point2.getY()))>= 50) {
					this.point1 = new Point2D.Double(newPoint.getX(),newPoint.getY());
					System.out.println(point1);
				} else {
					newPoint = point1;
					Point point1Screen = new Point((int) point1.getX(), (int) point1.getY());
					SwingUtilities.convertPointToScreen(point1Screen, parent);
					robot.mouseMove(point1Screen.x, point1Screen.y);
				}
			} else if (point2.equals(clickedPoint)) {
				if (Math.abs(Point2D.distance(point1.getX(), point1.getY(), newPoint.getX(), newPoint.getY()))>= 50) {
					this.point2 = new Point2D.Double(newPoint.getX(),newPoint.getY());
				} else {
					newPoint = point2;
					Point point2Screen = new Point((int) point2.getX(), (int) point2.getY());
					SwingUtilities.convertPointToScreen(point2Screen, parent);
					robot.mouseMove(point2Screen.x, point2Screen.y);
					System.out.println("robot");
				}
			}
			taille = Math.abs(Point.distance(point1.getX(), point1.getY(), point2.getX(), point2.getY())) / 2;
			if (point1.getX() <= point2.getX()) {
				angle = -Math.atan2(point2.getY() - point1.getY(), Math.abs(point1.getX() - point2.getX()));
				centrex = (point1.getX() + Math.cos(angle) * taille);
				centrey = (point1.getY() + Math.sin(-angle) * taille);
			} else if (point1.getX() > point2.getX()) {
				angle = -Math.atan2(point1.getY() - point2.getY(), Math.abs(point1.getX() - point2.getX()));
				centrex = (point2.getX() + Math.cos(angle) * taille);
				centrey = (point2.getY() + Math.sin(-angle) * taille);
			}
			line = new Line2D.Double(point1, point2);
			parent.repaint();
			return newPoint;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setFocus(boolean focus){        //Méthode qui permet de définir cet objet comme sélectionné
		this.focus = focus;
	}

	public boolean hasFocus(){          //Méthode qui permet de savoir si cet objet est sélectionné
		return focus;
	}
	public double getCentrex(){     //Méthode qui permet d'obtenir la coordonnée en X du centre le l'objet
		return centrex;
	}
	public double getCentrey(){     //Méthode qui permet d'obtenir la coordonnée en Y du centre le l'objet
		return centrey;
	}

	public double getTaille(){      //Méthode qui permet d'obtenir la taille de l'objet
		return taille;
	}

	public double getAngle(){       //Méthode qui permet d'obtenir l'angle de l'objet
		return angle;
	}

	public Point2D getPoint1(){     //Méthodes qui permettent d'obtenir un des deux points composant l'objet
		return point1;
	}
	public Point2D getPoint2(){
		return point2;
	}

	public void setColor(Color couleur){       //Méthode qui permet de définir la couleur
		this.couleur = couleur;
	}

    public Point2D getCentre(){
        Point2D centre = new Point2D.Double(centrex, centrey);
        return(centre);
    }

    public void setParent(JComponent parent){
	    this.parent = parent;
    }

    public abstract Line2D getLine();       //Méthode abstraite qui renvoie la ligne associée à l'objet
}
