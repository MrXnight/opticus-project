import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public final class Geometrie{

     public static Line2D translateLine(Line2D line,double transx, double transy){

           Point2D p1 = new Point2D.Double(line.getP1().getX() + transx, line.getP1().getY() + transy);
           Point2D p2 = new Point2D.Double(line.getP2().getX() + transx, line.getP2().getY() + transy);
           Line2D result = new Line2D.Double(p1, p2);
           return result;
     }


}
