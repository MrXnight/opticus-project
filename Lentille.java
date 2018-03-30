import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Lentille extends ObjetOptique {
	protected double f;
	protected Line2D line;
	protected int TAILLE_MINIMALE = 30;
	protected JComponent parent;
	protected Line2D planFocal1;
	protected Line2D planFocal2;
	public Lentille (double posx, double posy, double angle, Color col, double taille, double focal,JComponent parent) {
		super(posx, posy, angle, col, taille);
		f = focal;
		focus = false;
		point1 = new Point2D.Double((centrex - taille*Math.cos(angle)), (centrey - taille*Math.sin(-angle)));
		point2 = new Point2D.Double((centrex + taille*Math.cos(angle)),(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		updatePlanFocal();
		this.parent = parent;
	}

	public Lentille (double posx, double posy, double angle, double taille,double focal,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,focal,parent);
	}

	public Lentille (Point2D point1,Point2D point2,Color couleur,double focal,JComponent parent){
		f = focal;
		focus = false;
		this.parent = parent;
		this.point1 = point1;
		this.point2 = point2;
		pointUpdate(point1,point2);
		updatePlanFocal();
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
				if(Math.abs(Point2D.distance(point1.getX(),point1.getY(),newPoint.getX(),newPoint.getY()))/2>=30){
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
			updatePlanFocal();
			return newPoint;
		}
		catch(Exception e){}
			return null;
	}

	public void updatePlanFocal(){
		double x = line.getP2().getX()-line.getP1().getX();
		double y = line.getP2().getY()-line.getP1().getY();
		double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
		x = x/norme;
		y = y/norme;
		double dx = f*y;
		double dy = f*-x;
		/*Point2D p1 = new Point2D.Double(line.getP1().getX()+dx,line.getP1().getY()+dy);
		Point2D p1 = new Point2D.Double(line.getP2().getX()+dx,line.getP2().getY()+dy);*/
		planFocal1 = Geometrie.translateLine(line,dx,dy);
		planFocal2 = Geometrie.translateLine(line,-dx,-dy);
	}

	public void move(Point2D newPosition){
		double translationX = newPosition.getX()-centrex;
		double translationY = newPosition.getY()-centrey;
		centrex = newPosition.getX();
		centrey = newPosition.getY();
		point1 = new Point2D.Double(point1.getX()+translationX,point1.getY()+translationY);
		point2 = new Point2D.Double(point2.getX()+translationX,point2.getY()+translationY);
		line = new Line2D.Double(point1,point2);
		updatePlanFocal();
	}

	public int distancePoint(Point2D p){
		return (int)(line.ptSegDist(p));
	}

	public Line2D getLine(){
       return(line);
     }


	public double getFocal(){
		return f;
	}

	public void draw(Graphics2D g2d) {
		Stroke defaultStroke = g2d.getStroke();
		g2d.setColor(Color.gray);
		final float[] dash1 = {10.0f};
		final BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
		g2d.setStroke(dashed);
		g2d.draw(planFocal1);
		g2d.draw(planFocal2);
		g2d.setStroke(defaultStroke);
		g2d.setColor(couleur);
		g2d.draw(line);

		Point2D pointGauche,pointDroite;
		if(point1.getX() <= point2.getX()){
			pointGauche = point1;
			pointDroite = point2;
		}
		else{
			pointGauche = point2;
			pointDroite = point1;
		}
		if(f>0){
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int)((int)pointDroite.getX() - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( ((int)pointDroite.getY() + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int)((int)pointDroite.getX() - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)( ((int)pointGauche.getX() + (10)*(-Math.sin(-angle)+Math.cos(angle))) ),(int)( ((int)pointGauche.getY() + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)( (pointGauche.getX() + (10)*(Math.sin(-angle)+Math.cos(angle))) ),(int)( ((int)pointGauche.getY() + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(f<0){
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)((int)pointGauche.getX() - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( ((int)pointGauche.getY() + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)((int)pointGauche.getX() - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointGauche.getY() + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int) ((int)pointDroite.getX() + (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int) ((int)pointDroite.getX() + (10)*(Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(hasFocus()){
			g2d.setColor(Color.black);
			g2d.drawRect((int)centrex-5, (int)centrey-5,10,10);
			g2d.drawLine((int)point1.getX()+5,(int)point1.getY(),(int)point1.getX()-5,(int)point1.getY());
			g2d.drawLine((int)point1.getX(),(int)point1.getY()-5,(int)point1.getX(),(int)point1.getY()+5);
			g2d.drawLine((int)point2.getX()+5,(int)point2.getY(),(int)point2.getX()-5,(int)point2.getY());
			g2d.drawLine((int)point2.getX(),(int)point2.getY()-5,(int)point2.getX(),(int)point2.getY()+5);
		}
	}
}
