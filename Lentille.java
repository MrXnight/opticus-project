import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Lentille extends ObjetOptique {
	protected double f;
	protected Line2D line;
	protected int TAILLE_MINIMALE = 30;
	protected JComponent parent;
	public Lentille (double posx, double posy, double angle, Color col, double taille, double focal,JComponent parent) {
		super(posx, posy, angle, col, taille);
		f = focal;
		focus = false;
		point1 = new Point((int)(centrex - taille*Math.cos(angle)), (int)(centrey - taille*Math.sin(-angle)));
		point2 = new Point((int)(centrex + taille*Math.cos(angle)),(int)(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		this.parent = parent;
	}

	public Lentille (double posx, double posy, double angle, double taille,double focal,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,focal,parent);
	}

	public Lentille (Point point1,Point point2,Color couleur,double focal,JComponent parent){
		f = focal;
		focus = false;
		this.parent = parent;
		this.point1 = point1;
		this.point2 = point2;
		pointUpdate(point1,point2);
	}

	public void pointUpdate(Point pt1,Point pt2){
		taille = Math.abs(Point.distance(pt1.x,pt1.y,pt2.x,pt2.y))/2;
		if(pt1.x <= pt2.x){
			angle = -Math.atan2(pt2.y-pt1.y,Math.abs(pt1.x-pt2.x));
			centrex = (int)(pt1.x+Math.cos(angle)*taille);
			centrey = (int)(pt1.y+Math.sin(-angle)*taille);
		}
		else if(pt1.x > pt2.x){
			angle = -Math.atan2(pt1.y-pt2.y,Math.abs(pt1.x-pt2.x));
			centrex = (int)(pt2.x+Math.cos(angle)*taille);
			centrey = (int)(pt2.y+Math.sin(-angle)*taille);
		}
		line = new Line2D.Double(pt1,pt2);
	}

	public Point movePoint(Point newPoint,Point clickedPoint){
		try{
			Robot robot = new Robot();

			if(point1==clickedPoint){
				if(Math.abs(Point.distance(newPoint.x,newPoint.y,point2.x,point2.y))/2>=30){
					point1 = newPoint;
				}
				else{
					newPoint = point1;
					Point point1Screen = new Point(point1);
					System.out.println("avant : "+point1Screen);
					SwingUtilities.convertPointToScreen(point1Screen,parent);
					System.out.println("après : "+point1Screen);
					robot.mouseMove(point1Screen.x,point1Screen.y);
					System.out.println("robot");
				}
			}
			else if(point2==clickedPoint){
				if(Math.abs(Point.distance(point1.x,point1.y,newPoint.x,newPoint.y))/2>=30){
					point2 = newPoint;
				}
				else{
					newPoint = point2;
					Point point2Screen = new Point(point2);
					SwingUtilities.convertPointToScreen(point2Screen,parent);
					robot.mouseMove(point2Screen.x,point2Screen.y);
					System.out.println("robot");
				}
			}
			taille = Math.abs(Point.distance(point1.x,point1.y,point2.x,point2.y))/2;
			if(point1.x <= point2.x){
				angle = -Math.atan2(point2.y-point1.y,Math.abs(point1.x-point2.x));
				centrex = (int)(point1.x+Math.cos(angle)*taille);
				centrey = (int)(point1.y+Math.sin(-angle)*taille);
			}
			else if(point1.x > point2.x){
				angle = -Math.atan2(point1.y-point2.y,Math.abs(point1.x-point2.x));
				centrex = (int)(point2.x+Math.cos(angle)*taille);
				centrey = (int)(point2.y+Math.sin(-angle)*taille);
			}
			System.out.println("angle : "+angle);
			line = new Line2D.Double(point1,point2);
			return newPoint;
		}
		catch(Exception e){}
			return null;
	}

	public void move(Point newPosition){
		centrex = newPosition.x;
		centrey = newPosition.y;
		point1 = new Point((int)(centrex - taille*Math.cos(angle)), (int)(centrey - taille*Math.sin(-angle)));
		point2 = new Point((int)(centrex + taille*Math.cos(angle)),(int)(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
	}

	public int distancePoint(Point p){
		return (int)(line.ptSegDist(p));
	}

	public Line2D getLine(){
       return(line);
     }
     
     public Line2D translate(int transx, int transy){
		 
		 Point p1 = new Point (point1.x + transx, point1.y + transy);
		 Point p2 = new Point (point2.x + transx, point2.y + transy);
		 Line2D line = new Line2D.Double(p1, p2);
		 return line;
	 }

	public void lineFocal(Graphics2D g2d, Point intersec){
		int transx = intersec.x - centrex;
		int transy = centrey-intersec.y;
		
		Line2D lineOrigine = translate(transx, transy);
		g2d.draw(lineOrigine);
	
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(couleur);
		g2d.draw(line);
		Point pointGauche,pointDroite;
		if(point1.x <= point2.x){
			pointGauche = point1;
			pointDroite = point2;
		}
		else{
			pointGauche = point2;
			pointDroite = point1;
		}
		if(f>0){
			g2d.drawLine(pointDroite.x,pointDroite.y,(int)(pointDroite.x - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( (pointDroite.y + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine(pointDroite.x,pointDroite.y,(int)(pointDroite.x - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( (pointDroite.y + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine(pointGauche.x,	pointGauche.y,(int)( (pointGauche.x + (10)*(-Math.sin(-angle)+Math.cos(angle))) ),(int)( (pointGauche.y + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine(pointGauche.x,	pointGauche.y,(int)( (pointGauche.x + (10)*(Math.sin(-angle)+Math.cos(angle))) ),(int)( (pointGauche.y + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(f<0){
			g2d.drawLine(pointGauche.x,pointGauche.y,(int)(pointGauche.x - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( (pointGauche.y + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine(pointGauche.x,pointGauche.y,(int)(pointGauche.x - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( (pointGauche.y + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine(pointDroite.x,pointDroite.y,(int) (pointDroite.x + (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( (pointDroite.y + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine(pointDroite.x,pointDroite.y,(int) (pointDroite.x + (10)*(Math.sin(-angle)+Math.cos(angle))) ,(int)( (pointDroite.y + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(hasFocus()){
			g2d.setColor(Color.black);
			g2d.drawRect(centrex-5, centrey-5,10,10);
			g2d.drawLine(point1.x+5,point1.y,point1.x-5,point1.y);
			g2d.drawLine(point1.x,point1.y-5,point1.x,point1.y+5);
			g2d.drawLine(point2.x+5,point2.y,point2.x-5,point2.y);
			g2d.drawLine(point2.x,point2.y-5,point2.x,point2.y+5);
		}
	}
}
