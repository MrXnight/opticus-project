import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Source extends ObjetOptique {
	protected int TAILLE_MINIMALE = 30;
	protected ArrayList<Line2D.Double> tabFaisceau;
	protected static int compteNumero;
	protected int numero;


	public Source (double posx, double posy, double angle, Color col, double taille,JComponent parent) {
		super(posx, posy, angle, col, taille,parent);
		tabFaisceau = new ArrayList<Line2D.Double>();
		tabFaisceau.add(line);
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
			Point2D result = super.movePoint(newPoint,clickedPoint);
			tabFaisceau.clear();
			tabFaisceau.add(line);
			parent.repaint();
			return result;
	}

	public void setAngle(double angle){
		super.setAngle(angle);
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void setCentreX(double centreX){
		super.setCentreX(centreX);
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void setCentreY(double centreY){
		super.setCentreY(centreY);
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}

	public void move(Point2D newPosition){
		super.move(newPosition);
		tabFaisceau.clear();
		tabFaisceau.add(line);
		parent.repaint();
	}


	public void draw(Graphics2D g2d) {
		final int rayonCercleSource = 20;
		g2d.setColor(Color.YELLOW);
		g2d.fillOval((int)point1.getX()-rayonCercleSource/2,(int)point1.getY()-rayonCercleSource/2,20,20);
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
		Line2D.Double lineFaisceau = new Line2D.Double(point1,point2);
		tabFaisceau.add(lineFaisceau);
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
