import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Source extends ObjetOptique {
	protected ArrayList<Line2D.Double> tabFaisceau; //On utilise un ArrayList de Line2D pour stocker tous les faisceaux d'une source
	protected static int compteNumero; //Pour compter le nombre de lentille instanciées depuis le début du programme
	protected int numero;


	public Source (double posx, double posy, double angle, Color col, double taille,JComponent parent) { //Constructeur de l'objet Source
		super(posx, posy, angle, col, taille,parent);
		tabFaisceau = new ArrayList<Line2D.Double>();
		tabFaisceau.add(line); //On ajoute au tableau la line qui est le premier faisceu de base
		compteNumero += 1;
		numero = compteNumero;
	}

	public Source (double posx, double posy, double angle, double taille,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,parent);
	}

	public Source (Point2D point1,Point2D point2,Color couleur,JComponent parent){
		focus = false;
		this.couleur = couleur;
		tabFaisceau = new ArrayList<Line2D.Double>();
		this.parent = parent;
		this.point1 = new Point2D.Double(point1.getX(),point1.getY());
		this.point2 = new Point2D.Double(point2.getX(),point2.getY());
		pointUpdate(point1,point2);
		compteNumero += 1;
		numero = compteNumero;
		tabFaisceau.add(line);
	}

	public Point2D movePoint(Point2D newPoint,Point2D clickedPoint){
			Point2D result = super.movePoint(newPoint,clickedPoint); //On utilise le movePoint de la classe mère et on clear notre ArrayList et on remet la line de base pour recalculer tous faisceaux et intersections
			tabFaisceau.clear();
			tabFaisceau.add(line);
			parent.repaint();
			return result;
	}

	public void setAngle(double angle){
		super.setAngle(angle); //De meme on change l'angle, on clear l'ArrayList et on remet la line de base
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void setCentreX(double centreX){
		super.setCentreX(centreX); //On change la coordonnée centreX, on clear l'ArrayList et on remet la line de base
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void setCentreY(double centreY){
		super.setCentreY(centreY); //On change la coordonnée centreY, on clear l'ArrayList et on remet la line de base
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void move(Point2D newPosition){
		super.move(newPosition); //On le centre, on clear l'ArrayList et on remet la line de base
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}


	public void draw(Graphics2D g2d) {
		final int rayonCercleSource = 20;
		g2d.setColor(Color.YELLOW);
		g2d.fillOval((int)point1.getX()-rayonCercleSource/2,(int)point1.getY()-rayonCercleSource/2,20,20); //On dessine l'origine de la source qui sert de repère (mettre en commentaire ces lignes pour enlever le cercle jaune)
		g2d.setColor(couleur);
		for(Line2D i : tabFaisceau){ //On draw tous les faisceaux de la source
			g2d.draw(i);
		}

		if(hasFocus()){ //Comme pour les autres objets, on trace les croix et le carré centrale si l'objet a le focus
			g2d.setColor(Color.black);
			g2d.drawRect((int)centrex-5, (int)centrey-5,10,10);
			g2d.drawLine((int)point1.getX()+5,(int)point1.getY(),(int)point1.getX()-5,(int)point1.getY());
			g2d.drawLine((int)point1.getX(),(int)point1.getY()-5,(int)point1.getX(),(int)point1.getY()+5);
			g2d.drawLine((int)point2.getX()+5,(int)point2.getY(),(int)point2.getX()-5,(int)point2.getY());
			g2d.drawLine((int)point2.getX(),(int)point2.getY()-5,(int)point2.getX(),(int)point2.getY()+5);
		}
	}


	public void setTabFaisceau(ArrayList<Line2D.Double> tabFaisceau){
		this.tabFaisceau = tabFaisceau;
	}

	public ArrayList<Line2D.Double> getTabFaisceau(){
		return tabFaisceau;
	}

    public int getNum(){
        return numero;
    }
}
