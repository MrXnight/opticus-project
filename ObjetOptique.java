import java.awt.*;      //Import des différentes librairies Java
import javax.swing.*;
import java.awt.geom.*;
import java.io.Serializable;


/**
 * La classe ObjetOptique est la classe mère abstraite dont tous les objets optiques héritent.
 * Elle impose certaines méthodes comme la méthode draw et distance.
 * Elle intègre un certaines nombre d'attributs nécessaires comme l'angle de l'objet avec le repère, sa couleur...
 */
public abstract class ObjetOptique implements Serializable{        /** The angle. */
        //Classe abstraite de laquelle héritera tout ce qui peut être tracé dans la zone de dessin : lentilles, sources et mirroir
	protected double angle;
	
	/** la couleur. */
	protected Color couleur;
	
	/** la taille. */
	protected double taille;
	
	/** le focus : savoir si l'objet a été sélectionné par l'utilisateur*/
	protected boolean focus;
	
	/** coordonnée en y et en x du centre de l'objet */
	protected double centrex,centrey;
	
	/** Le 1er point2D qui définit la position de l'objet. */
	protected Point2D.Double point1;
	
	/** Le 2�me point2D qui d�finit la position de l'objet. */
	protected Point2D.Double point2;
	
	/** la ligne qui relie les deux points 2D que l'on affiche */
	protected Line2D.Double line;
	
	/** Le parent. */
	protected transient JComponent parent;

	/**
	 * Instantiates un nouvel objet optique.
	 *
	 * @param posx la position en x
	 * @param posy la position en y
	 * @param angle l'angle
	 * @param col la couleur
	 * @param taille la taille
	 */
	public ObjetOptique (double posx, double posy, double angle, Color col, double taille) {
		centrex = (int)posx;
		centrey = (int)posy;
		this.taille = taille;
		couleur = col;
		focus = false;
	}
	
	/**
	 * Instantiates a new objet optique.
	 */
	public ObjetOptique (){}        //Constructeur vide par d�faut

	/**
	 * Draw. Elle définit la façon dont un objet optique est dessiné.
	 * 
	 *
	 * @param g2d l'objet Graphics2D du panel de la zone de traçage
	 */
	public abstract void draw (Graphics2D g2d);     //Méthode abstraite draw

	/**
	 * Calcule la distance entre un point et la ligne.
	 *
	 * @param p le point à partir duquel on veut mesurer
	 * @return la distance en int
	 */
	public abstract int distancePoint(Point2D p);     //Méthode abstraite de distance à un point

	/**
	 * Move.Permet de déplacer la ligne
	 *
	 * @param newPosition le point2D qui définit le nouveau centre de l'objet optique
	 */
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

	/**
	 * Point update. M�thode qui actualise tous les attributs de l'objet après un d�placement par exemple
	 *
	 * @param pt1 le point1
	 * @param pt2 le point 2
	 */
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

	/**
	 * Sets the angle. Méthode qui définit l'angle de l'objet
	 *
	 * @param angle le nouvel angle
	 */
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

	/**
	 * Sets the centre X.
	 *
	 * @param centreX le nouveau centre X
	 */
	public void setCentreX(double centreX){     //Méthode qui redéfini la coordonnée en X du centre le l'objet
		double translationX =centreX-this.centrex ;
		centrex = centreX;
		point1 = new Point2D.Double(point1.getX() + translationX, point1.getY());
		point2 = new Point2D.Double(point2.getX() + translationX, point2.getY());
		line = new Line2D.Double(point1, point2);
		parent.repaint();
	}

	/**
	 * Sets the centre Y.
	 *
	 * @param centreY the new centre Y
	 */
	public void setCentreY(double centreY){     //M�thode qui redéfini la coordonnée en Y du centre de l'objet
		double translationY = centreY-this.centrey;
		centrey = centreY;
		point1 = new Point2D.Double(point1.getX(), point1.getY() + translationY);
		point2 = new Point2D.Double(point2.getX(), point2.getY() + translationY);
		line = new Line2D.Double(point1, point2);
		parent.repaint();
	}

	/**
	 * Move point. Méthode qui permet de déplacer un point de la lentille et de calculer les nouveaux attributs de l'objets
	 *
	 * @param newPoint the new point
	 * @param clickedPoint the clicked point
	 * @return the point 2D qui sera le centre de l'objet optique
	 */
	public Point2D movePoint(Point2D newPoint, Point2D clickedPoint) {
		try {
			Robot robot = new Robot();

			if (point1.equals(clickedPoint)) {
				if (Math.abs(Point2D.distance(newPoint.getX(), newPoint.getY(), point2.getX(), point2.getY()))>= 50) {
					this.point1 = new Point2D.Double(newPoint.getX(),newPoint.getY());
					//System.out.println(point1);
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
					//System.out.println("robot");
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

	/**
	 * Sets the focus.
	 *
	 * @param focus the new focus
	 */
	public void setFocus(boolean focus){        //Méthode qui permet de définir cet objet comme sélectionné
		this.focus = focus;
	}

	/**
	 * Checks for focus.
	 *
	 * @return true, if successful
	 */
	public boolean hasFocus(){          //Méthode qui permet de savoir si cet objet est sélectionné
		return focus;
	}
	
	/**
	 * Gets the centrex.
	 *
	 * @return the centrex
	 */
	public double getCentrex(){     //Méthode qui permet d'obtenir la coordonnée en X du centre le l'objet
		return centrex;
	}
	
	/**
	 * Gets the centrey.
	 *
	 * @return the centrey
	 */
	public double getCentrey(){     //Méthode qui permet d'obtenir la coordonnée en Y du centre le l'objet
		return centrey;
	}

	/**
	 * Gets the taille.
	 *
	 * @return the taille
	 */
	public double getTaille(){      //Méthode qui permet d'obtenir la taille de l'objet
		return taille;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle(){       //Méthode qui permet d'obtenir l'angle de l'objet
		return angle;
	}

	/**
	 * Gets the point 1.
	 *
	 * @return the point 1
	 */
	public Point2D getPoint1(){     //Méthodes qui permettent d'obtenir un des deux points composant l'objet
		return point1;
	}
	
	/**
	 * Gets the point 2.
	 *
	 * @return the point 2
	 */
	public Point2D getPoint2(){
		return point2;
	}

	/**
	 * Sets the color.
	 *
	 * @param couleur the new color
	 */
	public void setColor(Color couleur){       //Méthode qui permet de définir la couleur
		this.couleur = couleur;
	}

    /**
     * Gets the centre.
     *
     * @return the centre
     */
    public Point2D getCentre(){
        Point2D centre = new Point2D.Double(centrex, centrey);
        return(centre);
    }

    /**
     * Sets the parent.
     *
     * @param parent the new parent
     */
    public void setParent(JComponent parent){
	    this.parent = parent;
    }

    /**
     * Gets the line.
     *
     * @return the line
     */
    public abstract Line2D getLine();       //Méthode abstraite qui renvoie la ligne associée à l'objet
}
