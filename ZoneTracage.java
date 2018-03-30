import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.beans.*;
import java.util.Scanner;
import java.util.HashMap;

public class ZoneTracage extends JPanel implements MouseMotionListener,MouseListener{

     final class ResultIntersectionAvecObjetOptique {
          private final Point2D intersection;
          private final ObjetOptique objetIntersect;

          public ResultIntersectionAvecObjetOptique(Point2D first, ObjetOptique second) {
               this.intersection = first;
               this.objetIntersect = second;
          }

          public Point2D getIntersection() {
               return intersection;
          }

          public ObjetOptique getObjetIntersect() {
               return objetIntersect;
          }
     }

     //Lentille l1 = new Lentille(200,200,0,Color.BLACK,100,20,this);
     //Lentille l2 = new Lentille(500,200,Math.PI/2,Color.GREEN,100,20,this);
     Lentille l3 = new Lentille(200,500,Math.PI/4,Color.RED,100,100,this);
     Miroir m1 = new Miroir(200,200,Math.PI/3, Color.BLACK, 60,this);
     private int cursor;
     private ObjetOptique selectedObject;
     private Point2D selectedPoint;
     private ArrayList<ObjetOptique> listeObjet;
     private boolean mooving;
     private Point2D positionningPoint1 , positionningPoint2 ;
     private Line2D.Double postionningLine;
     private JFrame parentFrame;
     public static Line2D[] bordures;

     public ZoneTracage(JFrame parentFrame){
          this.setVisible(true);
          Source s1 = new Source(300,300,0,Color.RED,30,this);
          //Source s2 = new Source(400,500,0,Color.BLUE,30,this);
          mooving = false;
          positionningPoint1 = null;
          positionningPoint2 = null;
          postionningLine = null;
          listeObjet = new ArrayList<ObjetOptique>();
          this.parentFrame = parentFrame;
          //listeObjet.add(l1);
          //listeObjet.add(l2);
          listeObjet.add(l3);
          listeObjet.add(s1);
          listeObjet.add(m1);
          //listeObjet.add(s2);
          setLayout(null);
          setFocusable(true);
          addMouseListener(this);
          addMouseMotionListener(this);
          addMouseListener(new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                    ((JComponent)(e.getSource())).requestFocusInWindow();
               }
          });
          bordures = new Line2D[4];
          bordures[0] = new Line2D.Double(0,0,getWidth(),0);
          bordures[1] = new Line2D.Double(getWidth(),0,getWidth(),getHeight());
          bordures[2] = new Line2D.Double(0,getHeight(),getWidth(),getHeight());
          bordures[3] = new Line2D.Double(0,0,0,getHeight());
          this.addComponentListener(new ComponentListener() {
               public void componentResized(ComponentEvent e) {
                    bordures[0] = new Line2D.Double(0,0,getWidth(),0);
                    bordures[1] = new Line2D.Double(getWidth(),0,getWidth(),getHeight());
                    bordures[2] = new Line2D.Double(0,getHeight(),getWidth(),getHeight());
                    bordures[3] = new Line2D.Double(0,0,0,getHeight());
               }
               public void componentHidden(ComponentEvent e){

               }
               public void componentMoved(ComponentEvent e){

               }
               public void componentShown(ComponentEvent e){
                    bordures[0] = new Line2D.Double(0,0,getWidth(),0);
                    bordures[1] = new Line2D.Double(getWidth(),0,getWidth(),getHeight());
                    bordures[2] = new Line2D.Double(0,getHeight(),getWidth(),getHeight());
                    bordures[3] = new Line2D.Double(0,0,0,getHeight());
               }
          });
          this.setVisible(true);
     }

     @Override
     public void mouseMoved(MouseEvent e) {
          boolean moveCursor = false;
          for(ObjetOptique i : listeObjet){
               if ((e.getX() < i.getCentrex()+5 && e.getX() >i.getCentrex()-5 && e.getY()<i.getCentrey()+5 && e.getY()>i.getCentrey()-5 && i.hasFocus()) || mooving) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    cursor = Cursor.MOVE_CURSOR;
                    moveCursor = true;
               }
               else if(moveCursor == false){
                    setCursor(Cursor.getDefaultCursor());
                    cursor = Cursor.DEFAULT_CURSOR;
               }
          }
          if(positionningPoint1 != null && !BarreOutils.activeTool.equals(ActiveTool.NULL)){
               postionningLine = new Line2D.Double(positionningPoint1,e.getPoint());
          }
     }

     @Override
     public void mouseExited(MouseEvent e) {
          setCursor(Cursor.getDefaultCursor());
     }

     @Override
     public void mouseClicked(MouseEvent e){
          if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
               if(positionningPoint1 != null){
                    positionningPoint2 = e.getPoint();
               }
               else{
                    positionningPoint1 = e.getPoint();
               }
               if(positionningPoint1 != null && positionningPoint2 != null){
                    final Object[] array = new Object[2];
                    array[0] = "Veuillez renseigner la focal de la lentille :";
                    JTextField textField = new JTextField(10);
                    array[1] = textField;
                    final JOptionPane optionPane = new JOptionPane(
                    array,
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION,
                    null,
                    null);

                    final JDialog dialog = new JDialog(parentFrame,"Focal",true);
                    dialog.setContentPane(optionPane);
                    //textField.addActionListener(this);
                    dialog.setDefaultCloseOperation(
                    JDialog.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(new WindowAdapter() {
                         public void windowClosing(WindowEvent we) {
                              optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
                         }
                    });
                    optionPane.addPropertyChangeListener(
                    new PropertyChangeListener() {
                         public void propertyChange(PropertyChangeEvent e) {
                              String prop = e.getPropertyName();

                              if (dialog.isVisible()
                              && (e.getSource() == optionPane)
                              && (prop.equals(JOptionPane.VALUE_PROPERTY))
                              && !textField.getText().isEmpty()) {
                                   //If you were going to check something
                                   //before closing the window, you'd do
                                   //it here.
                                   System.out.println(textField.getText());
                                   dialog.setVisible(false);
                              }
                         }
                    });
                    dialog.pack();
                    dialog.setVisible(true);
                    listeObjet.add(new Lentille(positionningPoint1,positionningPoint2,Color.BLACK,Integer.parseInt(textField.getText()),this));
                    positionningPoint1 = null;
                    positionningPoint2 = null;
                    postionningLine = null;
                    BarreOutils.activeTool = ActiveTool.SELECT;
               }
          }
     }

     @Override
     public void mouseEntered(MouseEvent e){
     }

     @Override
     public void mousePressed(MouseEvent e) {
          int distanceMin = 5;
          selectedObject = null;
          selectedPoint = null;
          System.out.println("Pressed");
          for(ObjetOptique i : listeObjet){
               i.setFocus(false);
               if(i.distancePoint(e.getPoint())<distanceMin){
                    selectedObject = i;
                    distanceMin = i.distancePoint(e.getPoint());
               }
               if(i instanceof Lentille || i instanceof Source){
                    if((i).getPoint1().distance(e.getPoint())<(i).getPoint2().distance(e.getPoint()) && (i).getPoint2().distance(e.getPoint()) != (i).getPoint1().distance(e.getPoint())){
                         if((i).getPoint1().distance(e.getPoint())<5){
                              selectedPoint = (i).getPoint1();
                         }
                    }
                    else if((i).getPoint1().distance(e.getPoint())>(i).getPoint2().distance(e.getPoint()) && (i).getPoint2().distance(e.getPoint())<5 && (i).getPoint2().distance(e.getPoint()) != (i).getPoint1().distance(e.getPoint())) {
                         selectedPoint = (i).getPoint2();
                    }
               }
          }
          if(selectedObject != null){
               selectedObject.setFocus(true);
               System.out.println(selectedObject.getCentrex()+" et "+selectedObject.getCentrey());
          }
          revalidate();
          repaint();
     }

     @Override
     public void mouseDragged(MouseEvent e) {
          if(selectedObject != null && selectedPoint == null){
               if((e.getX() < selectedObject.getCentrex()+5 &&
               e.getX() >selectedObject.getCentrex()-5 &&
               e.getY()<selectedObject.getCentrey()+5 &&
               e.getY()>selectedObject.getCentrey()-5 &&
               selectedObject.hasFocus() ) ||
               mooving ){
                    selectedObject.move(e.getPoint());
                    mooving = true;
               }
          }
          if(selectedPoint != null && selectedObject != null){
               selectedPoint = (selectedObject).movePoint(e.getPoint(),selectedPoint);
          }
          revalidate();
          repaint();
     }

     @Override
     public void mouseReleased(MouseEvent e) {
          selectedPoint = null;
          mooving = false;
     }


     protected void paintComponent(Graphics g){
          Graphics2D g2d = (Graphics2D) g.create();
          updateIntersection();
          g2d.setColor(Color.WHITE);
          g2d.fillRect(0,0,this.getWidth(),this.getHeight());
          for(ObjetOptique o : listeObjet){
               o.draw(g2d);
          }
          repaint();
     }

     private void updateIntersection(){
               for (ObjetOptique o : listeObjet){
                    int compteur=0;
                    if(o instanceof Source){
                         Source s = (Source)o;
                         ArrayList<Line2D> tabFaisceau = new ArrayList<Line2D>();
                         tabFaisceau.add(s.getTabFaisceau().get(0));
                         System.out.println(s.getTabFaisceau().size());
                         HashMap<Line2D,ObjetOptique> toNotIntersect = new HashMap<Line2D,ObjetOptique>();

                         while(compteur < tabFaisceau.size()){
                              ResultIntersectionAvecObjetOptique intersectionObjet = intersectionAvecObjetOptique(tabFaisceau.get(compteur),toNotIntersect.get(tabFaisceau.get(compteur)));
                              if(intersectionObjet.getIntersection() != null){
                                   if(intersectionObjet.getObjetIntersect() instanceof Lentille){
                                        Line2D ligneIntersect = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                        tabFaisceau.set(compteur,ligneIntersect);
                                        toNotIntersect.put(ligneIntersect,toNotIntersect.get(ligneIntersect));
                                        Lentille lentille = (Lentille)intersectionObjet.getObjetIntersect();
                                        double dx = lentille.getCentrex() - intersectionObjet.getIntersection().getX();
                                        double dy = lentille.getCentrey() - intersectionObjet.getIntersection().getY();
                                        Line2D ligneCentrer = Geometrie.translateLine(tabFaisceau.get(compteur),dx,dy);
                                        Point2D intersectionPlanFocal = lineLine(ligneCentrer,lentille.getPlanFocal());

                                        if(intersectionPlanFocal != null){
                                             ResultIntersectionAvecObjetOptique intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),intersectionPlanFocal),intersectionObjet.getObjetIntersect());
                                             Line2D faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                             tabFaisceau.add(faisceauSortie);
                                             toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                        }
                                   }
                                   else if(intersectionObjet.getObjetIntersect() == null){
                                        Line2D faisceauSortie = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                        tabFaisceau.set(compteur,faisceauSortie);
                                        toNotIntersect.put(faisceauSortie,toNotIntersect.get(tabFaisceau.get(compteur)));
                                        compteur = tabFaisceau.size();
                                   }
                              }
                              compteur++;
                         }
                         s.setTabFaisceau(tabFaisceau);

                    }
               }
     }

     public ResultIntersectionAvecObjetOptique intersectionAvecObjetOptique(Line2D ligne,ObjetOptique objectToNotIntersect){

          ArrayList<Point2D> tabIntersection = new ArrayList<Point2D>();
          ArrayList<ObjetOptique> tabObjetOptique = new ArrayList<ObjetOptique>();
          Point2D newintersec = new Point2D.Double();
          Point2D intersec = new Point2D.Double();//Pour toute les sources determination de l'equation de droite
          ObjetOptique resultObjet = null;
          Point2D resultPoint = null;

          for(ObjetOptique l:listeObjet){ //Pour tout les objets optique autres que des sources
               //if(objectToNotIntersect == l){
               //}
               if((l instanceof Lentille || l instanceof Miroir) && objectToNotIntersect != l){
                    newintersec = lineLine(ligne,l.getLine());
                    if(newintersec != null){
                         Line2D faisceau = new Line2D.Double(ligne.getP1(), newintersec);
                         if(l.getPoint1().getX() - l.getPoint2().getX() == 0){
                              if(newintersec.getY() <= Math.max(l.getPoint1().getY(),l.getPoint2().getY()) && newintersec.getY() >= Math.min(l.getPoint1().getY(),l.getPoint2().getY())){
                                   if(ligne.getP2().getX()-ligne.getP1().getX() > 0 && newintersec.getX() >= ligne.getP1().getX()){
                                        tabIntersection.add(newintersec);
                                        tabObjetOptique.add(l);
                                   }
                                   else if(ligne.getP2().getX()-ligne.getP1().getX() < 0 && newintersec.getX() <= ligne.getP1().getX()){
                                        tabIntersection.add(newintersec);
                                        tabObjetOptique.add(l);
                                   }
                                   else if(ligne.getP2().getX()-ligne.getP1().getX() == 0){
                                        if(ligne.getP2().getY()-ligne.getP1().getY() >0 && newintersec.getY() >= ligne.getP1().getY()){
                                             tabIntersection.add(newintersec);
                                             tabObjetOptique.add(l);
                                        }
                                        else if(ligne.getP2().getY()-ligne.getP1().getY() <0 && newintersec.getY() <= ligne.getP1().getY()){
                                             tabIntersection.add(newintersec);
                                             tabObjetOptique.add(l);
                                        }
                                        else{
                                        }
                                   }
                                   else{
                                   }
                              }
                         }
                         else if(l.getLine().getBounds().contains(newintersec)){
                              if(ligne.getP2().getX()-ligne.getP1().getX() > 0 && newintersec.getX() >= ligne.getP1().getX()){
                                   tabIntersection.add(newintersec);
                                   tabObjetOptique.add(l);
                              }
                              else if(ligne.getP2().getX()-ligne.getP1().getX() < 0 && newintersec.getX() <= ligne.getP1().getX()){
                                   tabIntersection.add(newintersec);
                                   tabObjetOptique.add(l);
                              }
                              else if(ligne.getP2().getX()-ligne.getP1().getX() == 0){
                                   if(ligne.getP2().getY()-ligne.getP1().getY() >0 && newintersec.getY() >= ligne.getP1().getY()){
                                        tabIntersection.add(newintersec);
                                        tabObjetOptique.add(l);
                                   }
                                   else if(ligne.getP2().getY()-ligne.getP1().getY() <0 && newintersec.getY() <= ligne.getP1().getY()){
                                        tabIntersection.add(newintersec);
                                        tabObjetOptique.add(l);
                                   }
                                   else{
                                   }
                              }
                              else{
                              }
                         }
                         else{
                         }
                    }
               }
          }
          if(tabIntersection.size() > 0){
               intersec = tabIntersection.get(0);
               resultObjet = tabObjetOptique.get(0);
               for(int c=0;c<tabIntersection.size();c++){
                    if((ligne.getP1()).distance(tabIntersection.get(c))<(ligne.getP1()).distance(intersec)){
                         intersec = tabIntersection.get(c);
                         resultObjet = tabObjetOptique.get(c);
                    }
               }
               //System.out.println(intersec);
               resultPoint = intersec;
          }
          else{
               for(Line2D l : bordures){
                    newintersec = lineLine(ligne,l);
                    if(newintersec!=null){
                         if(ligne.getP2().getX()-ligne.getP1().getX() > 0 && newintersec.getX() >= ligne.getP1().getX()){
                              resultPoint = newintersec;
                         }
                         else if(ligne.getP2().getX()-ligne.getP1().getX() < 0 && newintersec.getX() <= ligne.getP1().getX()){
                              resultPoint = newintersec;
                         }
                         else if(ligne.getP2().getX()-ligne.getP1().getX() == 0){
                              if(ligne.getP2().getY()-ligne.getP1().getY() < 0 && newintersec.getY() <= ligne.getP1().getY()){
                                   resultPoint = newintersec;
                              }
                              else if(ligne.getP2().getY()-ligne.getP1().getY() > 0 && newintersec.getY() >= ligne.getP1().getY()){
                                   resultPoint = newintersec;
                              }
                         }
                    }
               }
          }
          return (new ResultIntersectionAvecObjetOptique(resultPoint,resultObjet));
     }

     public Point2D lineLine(ObjetOptique a, ObjetOptique b){
          return lineLine(a.getLine(),b.getLine());
     }

     public Point2D lineLine(Line2D a, Line2D b){
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
}
