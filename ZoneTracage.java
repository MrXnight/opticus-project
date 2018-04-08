import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.*;

import java.beans.*;
import java.util.HashMap;

public class ZoneTracage extends JPanel implements MouseMotionListener,MouseListener{ //On créé une classe locale qui va servir juste pour le resultat de la fonction intersectionAvecObjetOptique

     final class ResultIntersectionAvecObjetOptique {
          private final Point2D intersection; //On veut que cette classe result est pour attribut le point2D (qui est une intersection) et l'objet avec lequel notre faisceau a intersecté
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
     private Propriete prop;  //référence vers l'objet propriété
     private int cursor;
     private ObjetOptique selectedObject; //l'objet selectionné, null si il n'y a pas d'objet selectionné
     private Point2D selectedPoint; //le point selectionné, null si il n'y a pas de point selectionné
     private ArrayList<ObjetOptique> listeObjet; //La liste d'objet créée qui contient les objets
     private boolean mooving;
     private Point2D positionningPoint1 , positionningPoint2 ;
     private Line2D.Double postionningLine;
     private JFrame parentFrame;
     public Line2D[] bordures;

     public ZoneTracage(JFrame parentFrame){
          mooving = false;
          positionningPoint1 = null;
          positionningPoint2 = null;
          postionningLine = null;
          listeObjet = new ArrayList<ObjetOptique>();
          this.parentFrame = parentFrame; //Le parent qui contient la zone de dessin
          setLayout(null);
          addMouseListener(this);
          addMouseMotionListener(this);

          this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), "SUPPR_OBJECT"); //On set un keyEvent sur la touche suppr pour pouvoir supprimer un objet sélectionné avec la touche suppr
          this.getActionMap().put("SUPPR_OBJECT",new AbstractAction(){
               @Override
               public void actionPerformed(ActionEvent e) {
                    if(selectedObject != null){ //On supprime seulement l'objet si on a un objet selectionné
                         removeObjetOptique(selectedObject);
                         resetFocus();
                         prop.propSelect();
                    }
                    repaint();
               }
          });

          //On définit des lignes qui correspondent aux bordures de la zone de dessin pour détecter l'intersection entre un rayon et le bord de la zone de dessin
          bordures = new Line2D[4];
          bordures[0] = new Line2D.Double(0,0,getWidth(),0);
          bordures[1] = new Line2D.Double(getWidth(),0,getWidth(),getHeight());
          bordures[2] = new Line2D.Double(0,getHeight(),getWidth(),getHeight());
          bordures[3] = new Line2D.Double(0,0,0,getHeight());
          this.addComponentListener(new ComponentListener() {
               public void componentResized(ComponentEvent e) { //Si le composant est redimmensionné on update les bordures
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
          for(ObjetOptique i : listeObjet){ //On regarde si il y a un objet selectionné et si la souris passe sur le carré centrale, si c'est le cas on change le curseur
               if ((e.getX() < i.getCentrex()+5 && e.getX() >i.getCentrex()-5 && e.getY()<i.getCentrey()+5 && e.getY()>i.getCentrey()-5 && i.hasFocus()) || mooving) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    cursor = Cursor.MOVE_CURSOR;
                    moveCursor = true;
               }
               else if(moveCursor == false ){ //Sinon on met le curseur de base
                    setCursor(Cursor.getDefaultCursor());
                    cursor = Cursor.DEFAULT_CURSOR;
               }
          }
          //Si on a un objet selectionné, on update la postionningLine pour avoir un aperçu de ce que l'on va dessiner
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
          cursor = Cursor.DEFAULT_CURSOR;
     }

     @Override
     public void mouseClicked(MouseEvent e){
          if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE) || BarreOutils.activeTool.equals(ActiveTool.SOURCE) || BarreOutils.activeTool.equals(ActiveTool.MIROIR)){
          //Si on est en train de créer un objet et que l'on clique et que l'on pas encore clické, alors on sauvegarde la position du premier click
               if(positionningPoint1 != null){
                    positionningPoint2 = e.getPoint();
               }
               else{ //Si on a déjà clické une fois on sauvegarde la position du deuxième point
                    positionningPoint1 = e.getPoint();
               }
               if(positionningPoint1 != null && positionningPoint2 != null ){ //Si on les deux points de sauvegardes alors on va créer l'objet correspondant à l'outil selectionné
                    if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
                         double propFocal = prop.getEntreFocalValue(); //Pour la lentille il faut avoir précisé une focale dans la fenêtre propriété
                         if(propFocal != 0.0){ //On vérifie que la focale n'est pas égale à 0
                              listeObjet.add(new Lentille(positionningPoint1,positionningPoint2,prop.getCouleurChoisi(),propFocal,this));
                              //System.out.println(prop.getCouleurChoisi());
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
          double distanceMin = 5;
          selectedObject = null;
          selectedPoint = null;
          //Si on presse la souris et que l'on a l'outil select actif on va regarder si on est à moins de 5 de distance d'un objet et parmi ceux ci on prend le plus proche
          if(BarreOutils.activeTool == ActiveTool.SELECT){
               for(ObjetOptique i : listeObjet){
                    i.setFocus(false);
                    if(i.distancePoint(e.getPoint())<distanceMin){
                         selectedObject = i;
                         distanceMin = i.distancePoint(e.getPoint());
                    }
                    if(i instanceof Lentille || i instanceof Source || i instanceof Miroir){
                      //Si on a déjà sélectionné un objet, on regarde si le clic a été fait sur un des deux points ou sur le carré centrale
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
                    //System.out.println(selectedObject.getCentrex()+" et "+selectedObject.getCentrey());
               }
               else{
                    prop.propSelect();
               }
               repaint();
          }
          //De même qu'avec l'outil select pour la selection d'un objet sauf que ici une fois que l'on a détecté l'objet le plus proche, on le supprime
          if(BarreOutils.activeTool == ActiveTool.SUPPR){ //Permet de supprimer en pressant la souris avec l'outil delete actif

               for(ObjetOptique i : listeObjet){
                    i.setFocus(false);
                    if(i.distancePoint(e.getPoint())<distanceMin){
                         selectedObject = i;
                         distanceMin = i.distancePoint(e.getPoint());
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
       //Si on a selectionné un objet et pas un de ces deux points, on regarde si le curseur est dans le carré centrale et si c'est le cas on fait bouger tous l'objet
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
          //Cette fois ci on a selectionné un point de l'objet selectionné et on bouge seulement ce point ci de l'objet
          if(selectedPoint != null && selectedObject != null){
               //(System.out.println(selectedPoint);
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
       //on reset les variables à null et false quand on relache la souris
          selectedPoint = null;
          mooving = false;
          repaint();
     }

     public void resetFocus(){
       //Quand on clic dans le vide on reset le focus pour ne plus avoir d'objet selectionné et si on n'est pas en train de placer un objet on reset les positionningPoint
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

          //Permet de tracer la grille de fond
          final int SUBDIVISIONS = 80;
          final int DRAWING_SIZE = this.getWidth();
          final int SUBDIVISION_SIZE = DRAWING_SIZE / SUBDIVISIONS;

          Graphics2D g2d = (Graphics2D) g.create();
          updateIntersection(); //On update toutes les intersections ce qui permet de mettre à jour les tabFaisceau de toute les sources
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

          //On trace tous les objets optiques
          for(ObjetOptique o : listeObjet){
               o.draw(g2d);
          }
          //Si on est en train de créer un objet on trace la postionningLine
          if(postionningLine != null){
               g2d.setColor(Color.BLACK);
               g2d.draw(postionningLine);
          }
     }

     public void updateIntersection(){
          for (ObjetOptique o : listeObjet){ //On parcourt tous les objets mais on va seulement étudier le cas des sources
               int compteur=0;
               if(o instanceof Source){
                    Source s = (Source)o;
                    ArrayList<Line2D.Double> tabFaisceau = new ArrayList<Line2D.Double>(); //On récupère le tabFaisceau d'une source (si c'est la première fois le tabFaisceau ne contient que la line de base de la source sans aucune intersection)
                    tabFaisceau.add(s.getTabFaisceau().get(0));
                    HashMap<Line2D,ObjetOptique> toNotIntersect = new HashMap<Line2D,ObjetOptique>(); //Cette HashMap va stocker les temporairemnt stocker les faisceaux en clé et objet Optique en value car un faisceau ne peut pas intersecter deux fois le même objet

                    while(compteur < tabFaisceau.size()){ //On parcourt tant que l'on a pas traité tous les faisceaux d'une source
                        //On récupère l'intersection du faisceau avec un objet, on récupère le Point2D et l'ObjetOptique  dans notre classe locale définie au début
                         ResultIntersectionAvecObjetOptique intersectionObjet = intersectionAvecObjetOptique(tabFaisceau.get(compteur),toNotIntersect.get(tabFaisceau.get(compteur)));

                         if(intersectionObjet.getIntersection() != null){ //Si l'intersection existe
                              if(intersectionObjet.getObjetIntersect() instanceof Lentille){ //si c'est une intersection avec une lentille
                                    //On va tracer le faisceau jusqu'a la lentille, du point 1 du faisceau originelle jusqu'à l'intersection
                                   Line2D.Double ligneIntersect = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                   //On va remplacer l'ancien faisceau par ce nouveau
                                   tabFaisceau.set(compteur,ligneIntersect);
                                   //On le met aussi dans notre HashMap pour garder une trace de l'objet avec lequel le faisceau a interagit
                                   toNotIntersect.put(ligneIntersect,toNotIntersect.get(ligneIntersect));
                                   /*On va maintenant devoir trouver comment le rayon sort de la lentille, pour cela on utilise iune technique d'optique géométrique en traçcant le
                                   rayon paralèle au faisceau passant par le centre de la lentille et intersectant le plan focal, cette intersection sera aussi
                                   le point de passage du faisceau sortant de la lentille*/
                                   Lentille lentille = (Lentille)intersectionObjet.getObjetIntersect();
                                   double xTranslate = lentille.getCentrex() - intersectionObjet.getIntersection().getX();
                                   double yTranslate = lentille.getCentrey() - intersectionObjet.getIntersection().getY();
                                   Line2D ligneCentrer = Geometrie.translateLine(tabFaisceau.get(compteur),xTranslate,yTranslate);

                                   Line2D.Double planFocal;
                                   double x = lentille.getLine().getP2().getX()-lentille.getLine().getP1().getX();
                                   double y = lentille.getLine().getP2().getY()-lentille.getLine().getP1().getY();
                                   //On norme les vecteurs pour pouvoir
                                   double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                                   x = x/norme;
                                   y = y/norme;
                                   double dx = lentille.getFocal()*y;
                                   double dy = lentille.getFocal()*-x;
                                   //On se débrouille pour que la normale à la lentille soit toujours vers la droite et si la lentille est horizontale, alors la normale est vers le haut
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

                                   /*Pour savoir de quel côté arrive le faisceau on fait un produit scalaire avec la normale, en sachant que peut importe les positions relative des points 1 et 2,
                                   la position de la normale est toujours connu, on peut donc en déduire la position du rayon incident */
                                   if(Geometrie.produitScalaire(ligneCentrer,ligneNormale) > 0){
                                        if(lentille.getFocal()>0){
                                             planFocal = (Line2D.Double)Geometrie.translateLine(lentille.getLine(),dx,dy);
                                        }
                                        else{
                                             planFocal = (Line2D.Double)Geometrie.translateLine(lentille.getLine(),-dx,-dy);
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
                                   //On a donc trouver le plan focal en ayant fait attention juste avant de vérifier si la lentille est convergent ou divergente
                                   //On cherche ensuite l'intersection (méthode lineLine) entre le plan focal et la ligne paralèle passant par le centre
                                   Point2D intersectionPlanFocal = Geometrie.lineLine(ligneCentrer,planFocal);

                                   if(intersectionPlanFocal != null){
                                        ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                        if(lentille.getFocal()>0){
                                          //On sait donc par ou doit passer notre rayon mais on doit trouver jusqu'ou il va aller (bordures, nouvelles objet optiques ... )

                                             intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),intersectionPlanFocal),intersectionObjet.getObjetIntersect());
                                        }
                                        else{
                                          //Si la lentille est divergente alors on trace le rayon sortant sauf que en faisant ainsi, il se trouve du même coté que le rayon incident ce qui n'est pas possible pour un rayon réél
                                          //On fait donc une symetrie du point d'intersection du rayon centré et du plan focal par rapport au premier point d'intersection entre le rayon et la lentille pour prolonger le rayon du bon côté
                                             double symetriqueX = intersectionObjet.getIntersection().getX() - intersectionPlanFocal.getX();
                                             double symetriqueY = intersectionObjet.getIntersection().getY() - intersectionPlanFocal.getY();
                                             Point2D pointSymetrique = new Point2D.Double(intersectionObjet.getIntersection().getX() + symetriqueX, intersectionObjet.getIntersection().getY() + symetriqueY);
                                             intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),pointSymetrique),intersectionObjet.getObjetIntersect());

                                        }
                                        //On peut ensuite ajouter le rayone trouvé comme un nouveau rayon et non pas remplacé comme pour avant
                                        Line2D.Double faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                        tabFaisceau.add(faisceauSortie);
                                        toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                   }
                              }
                              if(intersectionObjet.getObjetIntersect() instanceof Miroir){
                                //Pareil que pour la lentille sauf que ici on fait une symetrie par rapport à la normale du miroir tout en vérifiant que le rayon arrive du bon coté du miroir
                                   Miroir miroir = (Miroir)intersectionObjet.getObjetIntersect();

                                   Line2D.Double ligneIntersect = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                   tabFaisceau.set(compteur,ligneIntersect);
                                   toNotIntersect.put(ligneIntersect,toNotIntersect.get(ligneIntersect));


                                   double x = miroir.getLine().getP2().getX()-miroir.getLine().getP1().getX();
                                   double y = miroir.getLine().getP2().getY()-miroir.getLine().getP1().getY();
                                   double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                                   x = x/norme;
                                   y = y/norme;
                                   double dx = -y;
                                   double dy = x;

                                   Line2D.Double ligneNormale = new Line2D.Double(new Point2D.Double(miroir.getCentrex(),miroir.getCentrey()),new Point2D.Double(miroir.getCentrex()+dx,miroir.getCentrey()+dy));

                                   if(Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur))<0 && !miroir.getSemiReflechissant()){
                                        //Line2D ligneNormaleAffichage = new Line2D.Double(new Point2D.Double(miroir.getCentrex(),miroir.getCentrey()),new Point2D.Double(miroir.getCentrex()+50*dx,miroir.getCentrey()+50*dy));
                                        //g2d.setColor(Color.BLUE);
                                        //g2d.draw(ligneNormaleAffichage);

                                        double symetrieAxialX = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dx)*2+(tabFaisceau.get(compteur).getP2().getX()-tabFaisceau.get(compteur).getP1().getX());
                                        double symetrieAxialY = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dy)*2+(tabFaisceau.get(compteur).getP2().getY()-tabFaisceau.get(compteur).getP1().getY());

                                        ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                        intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),new Point2D.Double(intersectionObjet.getIntersection().getX()+symetrieAxialX,intersectionObjet.getIntersection().getY()+symetrieAxialY)),miroir);
                                        Line2D.Double faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                        tabFaisceau.add(faisceauSortie);
                                        toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                   }
                                   else if(miroir.getSemiReflechissant()){
                                     //Si le miroir est semi-réfléchissant, on va créer deux rayon, un réfléchi comme pour le miroir juste avant et un transmis qui est juste la continuité du rayon incicent
                                        ResultIntersectionAvecObjetOptique intersectionRayonSortieNonReflechie;
                                        double continuiteRayonX = tabFaisceau.get(compteur).getP2().getX()-tabFaisceau.get(compteur).getP1().getX();
                                        double continuiteRayonY = tabFaisceau.get(compteur).getP2().getY()-tabFaisceau.get(compteur).getP1().getY();
                                        intersectionRayonSortieNonReflechie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),new Point2D.Double(intersectionObjet.getIntersection().getX()+continuiteRayonX,intersectionObjet.getIntersection().getY()+continuiteRayonY)),miroir);
                                        Line2D.Double faisceauSortieNonReflechie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortieNonReflechie.getIntersection());
                                        tabFaisceau.add(faisceauSortieNonReflechie);
                                        toNotIntersect.put(faisceauSortieNonReflechie,intersectionObjet.getObjetIntersect());
                                        if(Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur))<0 && !miroir.getSemiReflechissant()){
                                             double symetrieAxialX = -(Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dx)*2+(tabFaisceau.get(compteur).getP2().getX()-tabFaisceau.get(compteur).getP1().getX());
                                             double symetrieAxialY = -(Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dy)*2+(tabFaisceau.get(compteur).getP2().getY()-tabFaisceau.get(compteur).getP1().getY());

                                             ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                             intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),new Point2D.Double(intersectionObjet.getIntersection().getX()+symetrieAxialX,intersectionObjet.getIntersection().getY()+symetrieAxialY)),miroir);
                                             Line2D.Double faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                             tabFaisceau.add(faisceauSortie);
                                             toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                        }
                                        else{
                                             double symetrieAxialX = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dx)*2+(tabFaisceau.get(compteur).getP2().getX()-tabFaisceau.get(compteur).getP1().getX());
                                             double symetrieAxialY = (Geometrie.produitScalaire(ligneNormale,tabFaisceau.get(compteur)))*(-dy)*2+(tabFaisceau.get(compteur).getP2().getY()-tabFaisceau.get(compteur).getP1().getY());

                                             ResultIntersectionAvecObjetOptique intersectionRayonSortie;
                                             intersectionRayonSortie = intersectionAvecObjetOptique(new Line2D.Double(intersectionObjet.getIntersection(),new Point2D.Double(intersectionObjet.getIntersection().getX()+symetrieAxialX,intersectionObjet.getIntersection().getY()+symetrieAxialY)),miroir);
                                             Line2D.Double faisceauSortie = new Line2D.Double(intersectionObjet.getIntersection(),intersectionRayonSortie.getIntersection());
                                             tabFaisceau.add(faisceauSortie);
                                             toNotIntersect.put(faisceauSortie,intersectionObjet.getObjetIntersect());
                                        }
                                   }

                              }
                              //Si on n'intersecte avec aucun objet, alors on doit intersecter avec une bordure, on trace le rayon et on remplace l'ancien et on n'en ajoute pas à notre ArrayList
                              //ce qui va faire que compteur va atteindre la taille de notre ArrayList et donc finir la boucle
                              else if(intersectionObjet.getObjetIntersect() == null){
                                   Line2D.Double faisceauSortie = new Line2D.Double(tabFaisceau.get(compteur).getP1(),intersectionObjet.getIntersection());
                                   tabFaisceau.set(compteur,faisceauSortie);
                                   toNotIntersect.put(faisceauSortie,toNotIntersect.get(tabFaisceau.get(compteur)));
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
               if((l instanceof Lentille || l instanceof Miroir) && objectToNotIntersect != l){ //On va tester l'intersection de la line avec tous les objets existants sauf l'objet avec lequel elle ne doit pas interragir
                    newintersec = Geometrie.lineLine(ligne,l.getLine());
                    if(newintersec != null){
                         Line2D faisceau = new Line2D.Double(ligne.getP1(), newintersec);
                         /*On va ici traiter un grand nombre de cas particulier, on doit effectivement vérifier si l'intersection trouver avec lineLine appartient bien à l'objet trouvé' car lineLine fait commme
                         si c'était des droites. Pour ce faire nous avons utilisé un rectangle autour de l'objet (tracé grâce au deux points de l'objet) et si l'intersection est contenue dans ce rectangle, alros l'intersection est valide
                         Mais il a aussi fallut prendre en compte les cas ou le rectangle n'était pas valide (dans le cas ou les lignes sont parfaitement horizontales ou vertivales), il a donc fallu comparer les coordonnées des points de la lignes
                         de l'objet avec les coordonnées du point d'intersection*/
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
          //On a donc potentiellement trouvé plusieurs intersection, il faut donc prendre la plus proche de notre point de départ du faisceau
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
          else{ //Si on a aucune intersection avec les objets on trouve l'intersection avec les bordures
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
          for(ObjetOptique objets : tab){
            //on reset le parent, surtout utile pour quand on charge un fichier d'ObjetOptique, puisque le parent n'a pas pu être sauvegardé
               objets.setParent(this);
          }
          this.repaint();
     }
}
