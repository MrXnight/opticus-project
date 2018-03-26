import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class Miroir extends ObjetOptique {

	private Line2D line;

	public Miroir (double posx, double posy, double angle, Color col, double taille) {
		super(posx, posy, angle, col, taille);
	}

	public Miroir (double posx, double posy, double angle, double taille) {
		super(posx, posy, angle, Color.BLACK, taille);
	}

	public int distancePoint(Point p){
		return 0;
	}

	public void move(Point newPosition){

	}
	public Point movePoint(Point newPoint,Point clickedPoint){
		return null;
		//à implémenter
	}

	public Line2D getLine(){
       return(line);
     }

	public void draw(Graphics2D g2d) {
		g2d.setColor(couleur);
		double lentx = taille*Math.cos(angle);
		double lenty = taille*Math.sin(angle);
		g2d.drawLine((int)(-lentx),(int)(-lenty) ,(int)(+lentx),(int)(+lenty));
	}

}
