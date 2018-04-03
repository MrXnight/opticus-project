import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Miroir extends ObjetOptique {


	private final Double ecart = 10.0;
	protected static int compteNumero;
	protected int numero;
	protected boolean semiReflechissant;

	public Miroir(double posx, double posy, double angle, Color col, double taille, JComponent parent) {
		super(posx, posy, angle, col, taille);
		semiReflechissant = false;
		point1 = new Point2D.Double((centrex - taille * Math.cos(angle)), (centrey - taille * Math.sin(-angle)));
		point2 = new Point2D.Double((centrex + taille * Math.cos(angle)), (centrey + taille * Math.sin(-angle)));
		line = new Line2D.Double(point1, point2);
		this.parent = parent;
		compteNumero += 1;
		numero = compteNumero;
	}

	public Miroir(Point2D point1,Point2D point2,Color couleur,JComponent parent){
		focus = false;
		semiReflechissant = false;
		this.couleur = couleur;
		this.parent = parent;
		this.point1 = new Point2D.Double(point1.getX(),point1.getY());
		this.point2 = new Point2D.Double(point2.getX(),point2.getY());
		pointUpdate(point1,point2);
		compteNumero += 1;
		numero = compteNumero;
	}




	public int distancePoint(Point2D p) {
		return (int) (line.ptSegDist(p));
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
		if(!semiReflechissant){
			Point2D hach1 = new Point2D.Double(centrex, centrey);
			Point2D hach2 = new Point2D.Double((point2.getY() - point1.getY()) /(taille*0.3) + centrex, (point1.getX() - point2.getX()) /(taille*0.3)+ centrey);
			Line2D hachure = new Line2D.Double(hach1, hach2);

			Double deltaX = Math.cos(angle) * ecart;
			Double deltaY = Math.sin(angle) * ecart;

			for (int i = (int) -taille / 10; i < (int) taille / 10 + 1; i++) {
				g2d.draw(Geometrie.translateLine(hachure, deltaX * i, -deltaY * i));
			}
		}

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
