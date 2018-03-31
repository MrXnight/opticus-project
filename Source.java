import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Source extends ObjetOptique {
	protected int TAILLE_MINIMALE = 30;
	protected JComponent parent;
     protected ArrayList<Line2D> tabFaisceau;
	protected HashMap<Line2D,ObjetOptique> mapFaisceau;
    protected static int compteNumero;
    protected int numero;
    
    
	public Source (double posx, double posy, double angle, Color col, double taille,JComponent parent) {
		super(posx, posy, angle, col, taille);
		focus = false;
		point1 = new Point((int)(centrex - taille*Math.cos(angle)), (int)(centrey - taille*Math.sin(-angle)));
		point2 = new Point((int)(centrex + taille*Math.cos(angle)),(int)(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		this.parent = parent;
		tabFaisceau = new ArrayList<Line2D>();
		mapFaisceau = new HashMap<Line2D,ObjetOptique>();
		mapFaisceau.put(line,null);
		tabFaisceau.add(line);
        compteNumero += 1;
        numero = compteNumero;
	}

	public Source (double posx, double posy, double angle, double taille,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,parent);
        compteNumero += 1;
        numero = compteNumero;
	}
    
	public Source (Point2D point1,Point2D point2,Color couleur,JComponent parent){
		focus = false;
		this.parent = parent;
		this.point1 = point1;
		this.point2 = point2;
		pointUpdate(point1,point2);
        compteNumero += 1;
        numero = compteNumero;
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
    
	public Point2D movePoint(Point2D newPoint,Point2D clickedPoint){
		try{
			Robot robot = new Robot();

			if(point1==clickedPoint){
				if(Math.abs(Point.distance(newPoint.getX(),newPoint.getY(),point2.getX(),point2.getY()))/2>=30){
					point1 = newPoint;
				}
				else{
					newPoint = point1;
					Point point1Screen = new Point((int)point1.getX(),(int)point1.getY());
					System.out.println("avant : "+point1Screen);
					SwingUtilities.convertPointToScreen(point1Screen,parent);
					System.out.println("après : "+point1Screen);
					robot.mouseMove(point1Screen.x,point1Screen.y);
					System.out.println("robot");
				}
			}
			else if(point2==clickedPoint){
				if(Math.abs(Point.distance(point1.getX(),point1.getY(),newPoint.getX(),newPoint.getY()))/2>=30){
					point2 = newPoint;
				}
				else{
					newPoint = point2;
					Point point2Screen = new Point((int)point2.getX(),(int)point2.getY());
					SwingUtilities.convertPointToScreen(point2Screen,parent);
					robot.mouseMove(point2Screen.x,point2Screen.y);
					System.out.println("robot");
				}
			}
			taille = Math.abs(Point.distance(point1.getX(),point1.getY(),point2.getX(),point2.getY()))/2;
			if(point1.getX() <= point2.getX()){
				angle = -Math.atan2(point2.getY()-point1.getY(),Math.abs(point1.getX()-point2.getX()));
				centrex = (int)(point1.getX()+Math.cos(angle)*taille);
				centrey = (int)(point1.getY()+Math.sin(-angle)*taille);
			}
			else if(point1.getX() > point2.getX()){
				angle = -Math.atan2(point1.getY()-point2.getY(),Math.abs(point1.getX()-point2.getX()));
				centrex = (int)(point2.getX()+Math.cos(angle)*taille);
				centrey = (int)(point2.getY()+Math.sin(-angle)*taille);
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

	public void move(Point2D newPosition){
		super.move(newPosition);
		tabFaisceau.clear();
		tabFaisceau.add(line);
	}

	public int distancePoint(Point2D p){
		return (int)(line.ptSegDist(p));
	}

	public Point2D getPoint1(){
		return point1;
	}
	public Point2D getPoint2(){
		return point2;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(couleur);
		for(Line2D i : tabFaisceau){
			g2d.draw(i);
		}
		Point2D pointGauche,pointDroite;
		if(point1.getX() <= point2.getX()){
			pointGauche = point1;
			pointDroite = point2;
		}
		else{
			pointGauche = point2;
			pointDroite = point1;
		}
		if(hasFocus()){
			g2d.setColor(Color.black);
			g2d.drawRect((int)centrex-5, (int)centrey-5,10,10);
			g2d.drawLine((int)point1.getX()+5,(int)point1.getY(),(int)point1.getX()-5,(int)point1.getY());
			g2d.drawLine((int)point1.getX(),(int)point1.getY()-5,(int)point1.getX(),(int)point1.getY()+5);
			g2d.drawLine((int)point2.getX()+5,(int)point2.getY(),(int)point2.getX()-5,(int)point2.getY());
			g2d.drawLine((int)point2.getX(),(int)point2.getY()-5,(int)point2.getX(),(int)point2.getY()+5);
		}/*
		for(Line2D l : tabFaisceau){
			g2d.draw(l);
		}*/

	}
	public void ajouterFaisceau(Point2D point1, Point2D point2){
		Line2D lineFaisceau = new Line2D.Double(point1,point2);
		tabFaisceau.add(lineFaisceau);
	}

	public void setTabFaisceau(ArrayList<Line2D> tabFaisceau){
		this.tabFaisceau = tabFaisceau;
	}

	public ArrayList<Line2D> getTabFaisceau(){
		return tabFaisceau;
	}

	public void setMapFaisceau(HashMap<Line2D,ObjetOptique> mapFaisceau){
		this.mapFaisceau = mapFaisceau;
	}

	public HashMap<Line2D,ObjetOptique> getMapFaisceau(){
		return mapFaisceau;
	}

    public Line2D getLine(){
      return(line);
    }
    
    public int getNum(){
        return numero;
    }
}
