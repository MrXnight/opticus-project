import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import javax.swing.*;

/**
 * La classe miroir permet de creer un nouvel objet optique : le miroir semi ou non réflchissant
 */
public class Miroir extends ObjetOptique {


	/** On déclare l'écart. */
	private final Double ecart = 10.0;
	
	/** On déclare le compteur pour définir le nom d'un nouveau miroir*/
	protected static int compteNumero;
	
	/** Le numéro du miroir instancié */
	protected int numero;
	
	/** Booléen pour définir si le miroir est semi-reflechissant ou non */
	protected boolean semiReflechissant;

	/**
	 * Instantiates a new miroir.
	 *
	 * @param posx la position en x
	 * @param posy la position en y
	 * @param angle l'angle entre la lentille et le repère du plan
	 * @param col la couleur
	 * @param taille la taille
	 * @param parent le parent
	 */
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

	/**
	 * Instantiates a new miroir.
	 *
	 * @param point1 le point2D 1
	 * @param point2 le point2D 2
	 * @param couleur la couleur
	 * @param parent le parent
	 */
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




	/* (non-Javadoc)
	 * @see ObjetOptique#distancePoint(java.awt.geom.Point2D)
	 */
	public int distancePoint(Point2D p) {
		return (int) (line.ptSegDist(p));
	}


	/* (non-Javadoc)
	 * @see ObjetOptique#getLine()
	 */
	public Line2D getLine() {
		return (line);
	}

	/**
	 * Recupérer le numéro du miroir.
	 *
	 * @return le numéro du miroir
	 */
	public int getNum(){
		return numero;
	}

	/**
	 * Defini le miroir comme semi-reflechissant ou non .
	 *
	 * @param boolean, vrai si on veut que ce soit réflechissant
	 */
	public void setSemiReflechissant(boolean value){
		semiReflechissant = value;
	}

	/**
	 * Récupère l'état du miroir.
	 *
	 * @return vrai si le miroir est reflechissant, faux sinon
	 */
	public boolean getSemiReflechissant(){
		return semiReflechissant;
	}

	/* (non-Javadoc)
	 * @see ObjetOptique#draw(java.awt.Graphics2D)
	 */
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
