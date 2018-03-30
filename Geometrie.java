import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public final class Geometrie{

     public static Line2D translateLine(Line2D line,double transx, double transy){

           Point2D p1 = new Point2D.Double(line.getP1().getX() + transx, line.getP1().getY() + transy);
           Point2D p2 = new Point2D.Double(line.getP2().getX() + transx, line.getP2().getY() + transy);
           Line2D result = new Line2D.Double(p1, p2);
           return result;
     }

     public static Point2D lineLine(ObjetOptique a, ObjetOptique b){
          return lineLine(a.getLine(),b.getLine());
     }

     public static Point2D lineLine(Line2D a, Line2D b){
          double x1 = a.getP1().getX();
          double y1 = a.getP1().getY();
          double x2 = a.getP2().getX();
          double y2 = a.getP2().getY();
          double x3 = b.getP1().getX();
          double y3 = b.getP1().getY();
          double x4 = b.getP2().getX();
          double y4 = b.getP2().getY();
          double d = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
          if(d != 0){
               double xi=((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4))/d;
               double yi=((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4))/d;
               Point2D p = new Point2D.Double(xi,yi);
               return p;
          }
          return null;

     }

     public static double produitScalaire(Line2D line1, Line2D line2){
          double x1 = line1.getP2().getX() - line1.getP1().getX();
          double x2 = line2.getP2().getX() - line2.getP1().getX();
          double y1 = line1.getP2().getY() - line1.getP1().getY();
          double y2 = line2.getP2().getY() - line2.getP1().getY();
          return(x1*x2+y1*y2);
     }

}
