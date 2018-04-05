import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.Serializable;


public class Lentille extends ObjetOptique implements Serializable{
	protected double f;
	protected int TAILLE_MINIMALE = 30; //Taille minimal d'une lentille
	protected Line2D.Double planFocal1;
	protected Line2D.Double planFocal2;
	protected static int compteNumero; // compteur des lentilles instanciés
	protected int numero;
	protected boolean planFoc = true;

	public Lentille (double posx, double posy, double angle, Color col, double taille, double focal,JComponent parent) {
		super(posx, posy, angle, col, taille,parent); //On appelle le constructeur d'objet optique pour les paramètres généraux
		f = focal; //On définie la focal de lentille
		updatePlanFocal(); //On met à jour les plans focaux
		compteNumero += 1;
		numero = compteNumero;
	}

	public Lentille (double posx, double posy, double angle, double taille,double focal,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,focal,parent); // même constructeur avec la couleur noir par défaut
	}

	public Lentille (Point2D point1,Point2D point2,Color couleur,double focal,JComponent parent){ //Constructeur où on créer cette fois-ci un objet à partir de deux points
		f = focal;
		focus = false;
		this.couleur = couleur;
		this.parent = parent;
		this.point1 = new Point2D.Double(point1.getX(),point1.getY());
		this.point2 = new Point2D.Double(point2.getX(),point2.getY());
		pointUpdate(point1,point2);
		updatePlanFocal();
		compteNumero += 1;
		numero = compteNumero;
	}


	public Point2D movePoint(Point2D newPoint,Point2D clickedPoint){
		Point2D result = super.movePoint(newPoint,clickedPoint); // on utilise le movePoint d'ObjetOptique et en plus on met à jour les plans focaux et on repaint la ZoneTracage
		updatePlanFocal();
		parent.repaint();
		return result;
	}


	public void updatePlanFocal(){
		//On calcul d'abord les composantes de la normale à la lentille et on les normes
		double x = line.getP2().getX()-line.getP1().getX();
		double y = line.getP2().getY()-line.getP1().getY();
		double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
		x = x/norme;
		y = y/norme;
		double dx = f*y; //On multiplie les composantes normés par la focal et on obtient le vecteur de translation que l'on va utiliser avec translateLine
		double dy = f*-x;
		/*Point2D p1 = new Point2D.Double(line.getP1().getX()+dx,line.getP1().getY()+dy);
		Point2D p1 = new Point2D.Double(line.getP2().getX()+dx,line.getP2().getY()+dy);*/
		planFocal1 = Geometrie.translateLine(line,dx,dy); //On translate la ligne de la lentille selon la direction normale dans un sens
		planFocal2 = Geometrie.translateLine(line,-dx,-dy); //Et dans l'autre pour avoir les plans focaux dans toute les directions
	}

	public void move(Point2D newPosition){
		super.move(newPosition);
		updatePlanFocal();
		parent.repaint();
	}



	public void setAffichagePlanFocal(boolean value){
		planFoc = value;
	}

	public boolean getAffichagePlanFocal(){
		return planFoc;
	}

	public double getFocal(){
		return f;
	}

	public void setFocal(double focal){
		this.f = focal;
		updatePlanFocal();
	}

	public void setCentreX(double centreX){
		super.setCentreX(centreX);
		updatePlanFocal();
	}

	public void setCentreY(double centreY){
		super.setCentreY(centreY);
		updatePlanFocal();
	}

	public void setAngle(double angle){
		super.setAngle(angle);
		updatePlanFocal();
	}

	public int getNum(){
		return numero;
	}

	public void draw(Graphics2D g2d) {
		Stroke defaultStroke = g2d.getStroke(); //On recupère le stroke de base
		g2d.setColor(Color.gray);
		final float[] dash1 = {10.0f};
		final BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f); //On définit un nouveau stroke qui dessine en pointillé
		g2d.setStroke(dashed);
		if(planFoc){ //On regarde d'abord si l'utilisateur a activé l'affichage des plans focaux
			g2d.draw(planFocal1); //On dessine les deux plans focaux en pointillés
			g2d.draw(planFocal2);
		}

		g2d.setStroke(defaultStroke); //On remet le stroke par défaut pour les prochains dessins
		g2d.setColor(couleur); //On met la couleur définie pour cet objet
		g2d.draw(line); //On dessine la ligne de la lentille

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
			//Si f est > 0 on dessine les flèches vers l'intérieur pour une lentille convergente
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int)((int)pointDroite.getX() - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( ((int)pointDroite.getY() + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int)((int)pointDroite.getX() - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)( ((int)pointGauche.getX() + (10)*(-Math.sin(-angle)+Math.cos(angle))) ),(int)( ((int)pointGauche.getY() + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)( (pointGauche.getX() + (10)*(Math.sin(-angle)+Math.cos(angle))) ),(int)( ((int)pointGauche.getY() + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(f<0){
			//Si f est < 0 on dessine les flèches vers l'extèrieure pour une lentille divergente
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)((int)pointGauche.getX() - (10)*(Math.sin(-angle)+Math.cos(angle))),(int)( ((int)pointGauche.getY() + (10)*(-Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointGauche.getX(),(int)pointGauche.getY(),(int)((int)pointGauche.getX() - (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointGauche.getY() + (10)*(-Math.sin(-angle)-Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int) ((int)pointDroite.getX() + (10)*(-Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(Math.sin(-angle)+Math.cos(angle)))) );
			g2d.drawLine((int)pointDroite.getX(),(int)pointDroite.getY(),(int) ((int)pointDroite.getX() + (10)*(Math.sin(-angle)+Math.cos(angle))) ,(int)( ((int)pointDroite.getY() + (10)*(Math.sin(-angle)-Math.cos(angle)))) );
		}
		if(hasFocus()){ //Si la lentille a le focus on déssine une croix au point 1 et 2 et le carré au centre pour les différents mouvements
			g2d.setColor(Color.black);
			g2d.drawRect((int)centrex-5, (int)centrey-5,10,10);
			g2d.drawLine((int)point1.getX()+5,(int)point1.getY(),(int)point1.getX()-5,(int)point1.getY());
			g2d.drawLine((int)point1.getX(),(int)point1.getY()-5,(int)point1.getX(),(int)point1.getY()+5);
			g2d.drawLine((int)point2.getX()+5,(int)point2.getY(),(int)point2.getX()-5,(int)point2.getY());
			g2d.drawLine((int)point2.getX(),(int)point2.getY()-5,(int)point2.getX(),(int)point2.getY()+5);
		}
	}
}
