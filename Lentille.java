import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;


public class Lentille extends ObjetOptique {
	protected double f;
	protected int TAILLE_MINIMALE = 30;
	protected Line2D planFocal1;
	protected Line2D planFocal2;
	protected static int compteNumero;
	protected int numero;
	protected boolean planFoc = true;

	public Lentille (double posx, double posy, double angle, Color col, double taille, double focal,JComponent parent) {
		super(posx, posy, angle, col, taille);
		this.angle = angle % (Math.PI*2);
		if(this.angle>Math.PI/2){
			this.angle = this.angle - Math.PI;
		}
		else if(this.angle<Math.PI/2){
			this.angle = this.angle + Math.PI;
		}
		f = focal;
		focus = false;
		point1 = new Point2D.Double((centrex - taille*Math.cos(angle)), (centrey - taille*Math.sin(-angle)));
		point2 = new Point2D.Double((centrex + taille*Math.cos(angle)),(centrey + taille*Math.sin(-angle)));
		line = new Line2D.Double(point1,point2);
		updatePlanFocal();
		this.parent = parent;
		compteNumero += 1;
		numero = compteNumero;
	}

	public Lentille (double posx, double posy, double angle, double taille,double focal,JComponent parent) {
		this(posx, posy, angle, Color.BLACK, taille,focal,parent);
		compteNumero += 1;
		numero = compteNumero;
	}

	public Lentille (Point2D point1,Point2D point2,Color couleur,double focal,JComponent parent){
		f = focal;
		focus = false;
		this.couleur = couleur;
		this.parent = parent;
		this.point1 = point1;
		this.point2 = point2;
		pointUpdate(point1,point2);
		updatePlanFocal();
		compteNumero += 1;
		numero = compteNumero;
	}


	public Point2D movePoint(Point2D newPoint,Point2D clickedPoint){
		Point2D result = super.movePoint(newPoint,clickedPoint);
		updatePlanFocal();
		parent.repaint();
		return result;
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
		super.move(newPosition);
		updatePlanFocal();
		parent.repaint();
	}

	public int distancePoint(Point2D p){
		return (int)(line.ptSegDist(p));
	}

	public Line2D getLine(){
		return(line);
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
		this.angle = angle % (Math.PI*2);
		if(this.angle>Math.PI/2){
			this.angle = this.angle - Math.PI;
		}
		else if(this.angle<Math.PI/2){
			this.angle = this.angle + Math.PI;
		}
		super.setAngle(this.angle);
		updatePlanFocal();
	}

	public int getNum(){
		return numero;
	}

	public void draw(Graphics2D g2d) {
		Stroke defaultStroke = g2d.getStroke();
		g2d.setColor(Color.gray);
		final float[] dash1 = {10.0f};
		final BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
		g2d.setStroke(dashed);
		if(planFoc){
			g2d.draw(planFocal1);
			g2d.draw(planFocal2);
		}

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
