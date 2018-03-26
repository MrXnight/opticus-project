import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Source extends ObjetOptique {
	protected double f;
	protected Line2D line;
	protected int TAILLE_MINIMALE = 30;
	protected JComponent parent;
    protected ArrayList<Line2D> tabFaisceau;
	public Source (double posx, double posy, double angle, Color col, double taille,JComponent parent) {
		super(posx, posy, angle, col, taille);
		focus = false;
		point1 = new Point((int)(centrex - taille*Math.cos(angle)), (int)(centrey - taille*Math.sin(-angle)));
		point2 = new Point((int)(centrex + taille*Math.cos(angle)),(int)(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		this.parent = parent;
		tabFaisceau = new ArrayList<Line2D>();
		tabFaisceau.add(line);
	}

	public Source (double posx, double posy, double angle, double taille,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,parent);
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
			tabFaisceau.clear();
			tabFaisceau.add(line);
			return newPoint;
		}
		catch(Exception e){}
			return null;
	}

	public void move(Point newPosition){
		int translationX = newPosition.x-centrex;
		int translationY = newPosition.y-centrey;
		centrex = newPosition.x;
		centrey = newPosition.y;
		point1 = new Point(point1.x+translationX,point1.y+translationY);
		point2 = new Point(point2.x+translationX,point2.y+translationY);
		line = new Line2D.Double(point1,point2);
		tabFaisceau.clear();
		tabFaisceau.add(line);
	}

	public int distancePoint(Point p){
		return (int)(line.ptSegDist(p));
	}

	public Point getPoint1(){
		return point1;
	}
	public Point getPoint2(){
		return point2;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(couleur);
		for(Line2D i : tabFaisceau){
			g2d.draw(i);
		}
		Point pointGauche,pointDroite;
		if(point1.x <= point2.x){
			pointGauche = point1;
			pointDroite = point2;
		}
		else{
			pointGauche = point2;
			pointDroite = point1;
		}
		if(hasFocus()){
			g2d.setColor(Color.black);
			g2d.drawRect(centrex-5, centrey-5,10,10);
			g2d.drawLine(point1.x+5,point1.y,point1.x-5,point1.y);
			g2d.drawLine(point1.x,point1.y-5,point1.x,point1.y+5);
			g2d.drawLine(point2.x+5,point2.y,point2.x-5,point2.y);
			g2d.drawLine(point2.x,point2.y-5,point2.x,point2.y+5);
		}/*
		for(Line2D l : tabFaisceau){
			g2d.draw(l);
		}*/

	}
	public void ajouterFaisceau(Point point1, Point point2){
		Line2D lineFaisceau = new Line2D.Double(point1,point2);
		tabFaisceau.add(lineFaisceau);
	}

	public void setTabFaisceau(ArrayList<Line2D> tabFaisceau){
		this.tabFaisceau = tabFaisceau;
	}

	public ArrayList<Line2D> getTabFaisceau(){
		return tabFaisceau;
	}


    public Line2D getLine(){
      return(line);
    }
}
