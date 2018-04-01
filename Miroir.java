import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Miroir extends ObjetOptique {

	protected JComponent parent;
	final Double ecart = 10.0;
	protected static int compteNumero;
	protected int numero;
	protected boolean semiReflechissant = false;

	public Miroir(double posx, double posy, double angle, Color col, double taille, JComponent parent) {
		super(posx, posy, angle, col, taille);
		point1 = new Point2D.Double((centrex - taille * Math.cos(angle)), (centrey - taille * Math.sin(-angle)));
		point2 = new Point2D.Double((centrex + taille * Math.cos(angle)), (centrey + taille * Math.sin(-angle)));
		line = new Line2D.Double(point1, point2);
		this.parent = parent;
		compteNumero += 1;
		numero = compteNumero;
	}

	public Miroir(double posx, double posy, double angle, double taille) {
		super(posx, posy, angle, Color.BLACK, taille);
		compteNumero += 1;
		numero = compteNumero;
	}

	public Miroir(Point2D point1,Point2D point2,Color couleur,JComponent parent){
		focus = false;
		this.parent = parent;
		this.point1 = point1;
		this.point2 = point2;
		pointUpdate(point1,point2);
		compteNumero += 1;
		numero = compteNumero;
	}




	public int distancePoint(Point2D p) {
		return (int) (line.ptSegDist(p));
	}


	public Point2D movePoint(Point2D newPoint, Point2D clickedPoint) {
		try {
			Robot robot = new Robot();

			if (point1 == clickedPoint) {
				if (Math.abs(Point.distance(newPoint.getX(), newPoint.getY(), point2.getX(), point2.getY()))
				/ 2 >= 30) {
					point1 = newPoint;
				} else {
					newPoint = point1;
					Point point1Screen = new Point((int) point1.getX(), (int) point1.getY());
					SwingUtilities.convertPointToScreen(point1Screen, parent);
					robot.mouseMove(point1Screen.x, point1Screen.y);
				}
			} else if (point2 == clickedPoint) {
				if (Math.abs(Point2D.distance(point1.getX(), point1.getY(), newPoint.getX(), newPoint.getY()))
				/ 2 >= 30) {
					point2 = newPoint;
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
				centrex = (int) (point1.getX() + Math.cos(angle) * taille);
				centrey = (int) (point1.getY() + Math.sin(-angle) * taille);
			} else if (point1.getX() > point2.getX()) {
				angle = -Math.atan2(point1.getY() - point2.getY(), Math.abs(point1.getX() - point2.getX()));
				centrex = (int) (point2.getX() + Math.cos(angle) * taille);
				centrey = (int) (point2.getY() + Math.sin(-angle) * taille);
			}
			line = new Line2D.Double(point1, point2);
			return newPoint;
		} catch (Exception e) {
		}
		return null;
	}

	public Line2D getLine() {
		return (line);
	}

	public int getNum(){
		return numero;
	}

	public void setSemiReflechissant(boolean value){
		semiReflechissant = value;
	}

	public boolean getSemiReflechissant(){
		return semiReflechissant;
	}

	public void draw(Graphics2D g2d) {

		g2d.setColor(couleur);

		double lentx = taille * Math.cos(angle);
		double lenty = taille * Math.sin(angle);
		g2d.draw(line);

		//on trace les traits perpendiculaires au miroir pour symboliser la face non reflechissante

		Point2D hach1 = new Point2D.Double(centrex, centrey);
		Point2D hach2 = new Point2D.Double((point2.getY() - point1.getY()) /(taille*0.3) + centrex, (point1.getX() - point2.getX()) /(taille*0.3)+ centrey);
		Line2D hachure = new Line2D.Double(hach1, hach2);

		Double deltaX = Math.cos(angle) * ecart;
		Double deltaY = Math.sin(angle) * ecart;

		for (int i = (int) -taille / 10; i < (int) taille / 10 + 1; i++) {
			g2d.draw(Geometrie.translateLine(hachure, deltaX * i, -deltaY * i));
		}

		//on determine l'equation ax+b=y du miroir (pas utile pour le moement)
		Double coeffDirecteur;
		if (point2.getX() - point1.getX() != 0) {
			coeffDirecteur = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
		} else {
			coeffDirecteur = 0.0;
		}
		Double constante = point1.getY() - coeffDirecteur * point1.getX();

		//On trace les hitobox du miroir

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
