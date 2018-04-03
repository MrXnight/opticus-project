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

     private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
     private Propriete prop;
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
          mooving = false;
          positionningPoint1 = null;
          positionningPoint2 = null;
          postionningLine = null;
          listeObjet = new ArrayList<ObjetOptique>();
          this.parentFrame = parentFrame;
          setLayout(null);
          setFocusable(true);
          addMouseListener(this);
          addMouseMotionListener(this);

           this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "SUPPR_OBJECT");
           this.getActionMap().put("SUPPR_OBJECT",new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                     if(selectedObject != null){
                          removeObjetOptique(selectedObject);
                          resetFocus();
                          prop.propSelect();
                     }
                     repaint();
                }
           });

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
          if(positionningPoint1 != null && (BarreOutils.activeTool.equals(ActiveTool.MIROIR) || BarreOutils.activeTool.equals(ActiveTool.SOURCE) || BarreOutils.activeTool.equals(ActiveTool.LENTILLE))){
               postionningLine = new Line2D.Double(positionningPoint1,e.getPoint());
               repaint();
          }
          else{
               postionningLine = null;
          }
     }

     @Override
     public void mouseExited(MouseEvent e) {
          setCursor(Cursor.getDefaultCursor());
     }

     @Override
     public void mouseClicked(MouseEvent e){
          if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE) || BarreOutils.activeTool.equals(ActiveTool.SOURCE) || BarreOutils.activeTool.equals(ActiveTool.MIROIR)){
               if(positionningPoint1 != null){
                    positionningPoint2 = e.getPoint();
               }
               else{
                    positionningPoint1 = e.getPoint();
               }
               if(positionningPoint1 != null && positionningPoint2 != null ){
                    if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
                         double propFocal = prop.getEntreFocalValue();
                         if(propFocal != 0.0){
                              listeObjet.add(new Lentille(positionningPoint1,positionningPoint2,prop.getCouleurChoisi(),propFocal,this));
                              System.out.println(prop.getCouleurChoisi());
                         }
                    }
                    else if(BarreOutils.activeTool.equals(ActiveTool.SOURCE)){
                         listeObjet.add(new Source(positionningPoint1,positionningPoint2,prop.getCouleurChoisi(),this));
                    }
                    else if(BarreOutils.activeTool.equals(ActiveTool.MIROIR)){
                         listeObjet.add(new Miroir(positionningPoint1,positionningPoint2,prop.getCouleurChoisi(),this));
                    }
                    positionningPoint1 = null;
                    positionningPoint2 = null;
                    postionningLine = null;
                    repaint();
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
          if(BarreOutils.activeTool == ActiveTool.SELECT){
               for(ObjetOptique i : listeObjet){
                    i.setFocus(false);
                    if(i.distancePoint(e.getPoint())<distanceMin){
                         selectedObject = i;
                         distanceMin = i.distancePoint(e.getPoint());
                    }
                    if(i instanceof Lentille || i instanceof Source || i instanceof Miroir){
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
                    if(selectedObject instanceof Lentille){
                         prop.propLentille((Lentille)selectedObject);
                    }
                    if(selectedObject instanceof Miroir){
                         prop.propMiroir((Miroir)selectedObject);
                    }
                    if(selectedObject instanceof Source){
                         prop.propSource((Source)selectedObject);
                    }
                    System.out.println(selectedObject.getCentrex()+" et "+selectedObject.getCentrey());
               }
               else{
                    prop.propSelect();
               }
               repaint();
          }
          if(BarreOutils.activeTool == ActiveTool.SUPPR){
               for(ObjetOptique i : listeObjet){
                    i.setFocus(false);
                    if(i.distancePoint(e.getPoint())<distanceMin){
                         selectedObject = i;
                         distanceMin = i.distancePoint(e.getPoint());
                    }
                    if(i instanceof Lentille || i instanceof Source || i instanceof Miroir){
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
                    listeObjet.remove(selectedObject);
                    repaint();
               }
          }
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
                    if(selectedObject instanceof Lentille){
                         prop.propLentille((Lentille)selectedObject);
                    }
                    if(selectedObject instanceof Miroir){
                         prop.propMiroir((Miroir)selectedObject);
                    }
                    if(selectedObject instanceof Source){
                         prop.propSource((Source)selectedObject);
                    }
               }
          }
          if(selectedPoint != null && selectedObject != null){
               selectedPoint = (selectedObject).movePoint(e.getPoint(),selectedPoint);
               if(selectedObject instanceof Lentille){
                    prop.propLentille((Lentille)selectedObject);
               }
               if(selectedObject instanceof Miroir){
                    prop.propMiroir((Miroir)selectedObject);
               }
               if(selectedObject instanceof Source){
                    prop.propSource((Source)selectedObject);
               }
          }
          repaint();
     }

     @Override
     public void mouseReleased(MouseEvent e) {
          selectedPoint = null;
          mooving = false;
          repaint();
     }

     public void resetFocus(){
          if(selectedObject!=null){
               selectedObject.setFocus(false);
          }
          selectedObject = null;
          positionningPoint1 = null;
          positionningPoint2 = null;
          postionningLine = null;
          repaint();
     }

     protected void paintComponent(Graphics g){

        final int SUBDIVISIONS = 80;
        final int DRAWING_SIZE = this.getWidth();
        final int SUBDIVISION_SIZE = DRAWING_SIZE / SUBDIVISIONS;

          Graphics2D g2d = (Graphics2D) g.create();
          updateIntersection();
          g2d.setColor(Color.WHITE);
          g2d.fillRect(0,0,this.getWidth(),this.getHeight());

          g2d.setColor(new Color(226, 226, 226, 100));
          for (int i = 1; i < SUBDIVISIONS; i++) {
             int x = i * SUBDIVISION_SIZE;
             g2d.drawLine(x, 0, x, getSize().height);
          }
          for (int i = 1; i < SUBDIVISIONS; i++) {
             int y = i * SUBDIVISION_SIZE;
             g2d.drawLine(0, y, getSize().width, y);
          }


          for(ObjetOptique o : listeObjet){
               o.draw(g2d);
          }
          if(postionningLine != null){
               g2d.setColor(Color.BLACK);
               g2d.draw(postionningLine);
          }
     }

     public void updateIntersection(){
               for (ObjetOptique o : listeObjet){
                    int compteur=0;
                    if(o instanceof Source){
                         Source s = (Source)o;
                         ArrayList<Line2D> tabFaisceau = new ArrayList<Line2D>();
                         tabFaisceau.add(s.getTabFaisceau().get(0));
                         HashMap<Line2D,ObjetOptique> toNotIntersect = new HashMap<Line2D,ObjetOptique>();

                         while(compteur < tabFaisceau.size()){

                              ResultIntersectionAvecObjetOptique intersectionObjet = intersectionAvecObjetOptique(tabFaisceau.get(compteur),toNotIntersect.get(tabFaisceau.get(compteur)));
                              if(intersectionObjet.getIntersection() != null){
                                   if(intersectionObjet.getObjetIntersect() instanceof Lentille){
                                        Line2D ligneIntersect = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                        tabFaisceau.set(compteur,ligneIntersect);
                                        toNotIntersect.put(ligneIntersect,toNotIntersect.get(ligneIntersect));
                                        Lentille lentille = (Lentille)intersectionObjet.getObjetIntersect();
                                        double xTranslate = lentille.getCentrex() - intersectionObjet.getIntersection().getX();
                                        double yTranslate = lentille.getCentrey() - intersectionObjet.getIntersection().getY();
                                        Line2D ligneCentrer = Geometrie.translateLine(tabFaisceau.get(compteur),xTranslate,yTranslate);

                                        Line2D planFocal;
                                        double x = lentille.getLine().getP2().getX()-lentille.getLine().getP1().getX();
                              		double y = lentille.getLine().getP2().getY()-lentille.getLine().getP1().getY();
                              		double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                              		x = x/norme;
                              		y = y/norme;
                              		double dx = lentille.getFocal()*y;
                              		double dy = lentille.getFocal()*-x;

                                        if(dx < 0){
                                             dx = -dx;
                                             dy = -dy;
                                        }
                                        else if(dx == 0){
                                             if(dy < 0){
                                                  dx = -dx;
                                                  dy = -dy;
                                             }
                                        }
                                        Line2D ligneNormale = new Line2D.Double(new Point2D.Double(lentille.getCentrex(),lentille.getCentrey()),new Point2D.Double(lentille.getCentrex()+dx,lentille.getCentrey()+dy));
                                        if(Geometrie.produitScalaire(ligneCentrer,ligneNormale) > 0){
                                             if(lentille.getFocal()>0){
                                                  planFocal = Geometrie.translateLine(lentille.getLine(),dx,dy);
                                             }
                                             else{
                                                  planFocal = Geometrie.translateLine(lentille.getLine(),-dx,-dy);
                                             }

                                        }
                                        else{
                                             if(lentille.getFocal()>0){
                                                  planFocal = Geometrie.translateLine(lentille.getLine(),-dx,-dy);
                                             }
                                             else{
                                                  planFocal = Geometrie.translateLine(lentille.getLine(),dx,dy);
                                             }
                                        }
                                        Point2D intersectionPlanFocal = Geometrie.lineLine(ligneCentrer,planFocal);

                                        if(intersectionPlanFocal != null){
                                             ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                             if(lentille.getFocal()>0){
                                                  intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),intersectionPlanFocal),intersectionObjet.getObjetIntersect());
                                             }
                                             else{
                                                  double symetriqueX = intersectionObjet.getIntersection().getX() - intersectionPlanFocal.getX();
                                                  double symetriqueY = intersectionObjet.getIntersection().getY() - intersectionPlanFocal.getY();
                                                  Point2D pointSymetrique = new Point2D.Double(intersectionObjet.getIntersection().getX() + symetriqueX, intersectionObjet.getIntersection().getY() + symetriqueY);
                                                  intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),pointSymetrique),intersectionObjet.getObjetIntersect());

                                             }
                                             Line2D faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                             tabFaisceau.add(faisceauSortie);
                                             toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                        }
                                   }
                                   if(intersectionObjet.getObjetIntersect() instanceof Miroir){
                                        Miroir miroir = (Miroir)intersectionObjet.getObjetIntersect();

                                        Line2D ligneIntersect = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                        tabFaisceau.set(compteur,ligneIntersect);
                                        toNotIntersect.put(ligneIntersect,toNotIntersect.get(ligneIntersect));


                                        double x = miroir.getLine().getP2().getX()-miroir.getLine().getP1().getX();
                              		double y = miroir.getLine().getP2().getY()-miroir.getLine().getP1().getY();
                              		double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                              		x = x/norme;
                              		y = y/norme;
                              		double dx = -y;
                              		double dy = x;

                                        Line2D ligneNormale = new Line2D.Double(new Point2D.Double(miroir.getCentrex(),miroir.getCentrey()),new Point2D.Double(miroir.getCentrex()+dx,miroir.getCentrey()+dy));

                                        if(Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur))<0){
                                             //Line2D ligneNormaleAffichage = new Line2D.Double(new Point2D.Double(miroir.getCentrex(),miroir.getCentrey()),new Point2D.Double(miroir.getCentrex()+50*dx,miroir.getCentrey()+50*dy));
                                             //g2d.setColor(Color.BLUE);
                                             //g2d.draw(ligneNormaleAffichage);

                                             double symetrieAxialX = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dx)*2+(tabFaisceau.get(compteur).getP2().getX()-tabFaisceau.get(compteur).getP1().getX());
                                             double symetrieAxialY = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dy)*2+(tabFaisceau.get(compteur).getP2().getY()-tabFaisceau.get(compteur).getP1().getY());

                                             ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                             intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),new Point2D.Double(intersectionObjet.getIntersection().getX()+symetrieAxialX,intersectionObjet.getIntersection().getY()+symetrieAxialY)),miroir);
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
                    newintersec = Geometrie.lineLine(ligne,l.getLine());
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
                                   else {

                                   }
                              }
                              else{

                              }
                         }
                         else if(l.getLine().getP1().getY() - l.getLine().getP2().getY() == 0 && (newintersec.getX()<=Math.max(l.getLine().getP1().getX(),l.getLine().getP2().getX())
                         && newintersec.getX()>=Math.min(l.getLine().getP1().getX(),l.getLine().getP2().getX()))){
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
                    newintersec = Geometrie.lineLine(ligne,l);
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

     public void addObjetOptique(ObjetOptique objetOptique){
          listeObjet.add(objetOptique);
     }

     public void removeObjetOptique(ObjetOptique objetOptique){
          listeObjet.remove(objetOptique);
     }

     public void setPropriete(Propriete prop){
          this.prop = prop;
     }

     public ObjetOptique getSelectedObject(){
          return selectedObject;
     }

     public ArrayList<ObjetOptique> getListObjetOptique(){
       return listeObjet;
     }

     public void setListObjetOptique(ArrayList<ObjetOptique> tab){
       listeObjet = tab;
       this.repaint();
     }
}
