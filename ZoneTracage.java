import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.beans.*;

public class ZoneTracage extends JPanel implements MouseMotionListener,MouseListener{

     //Lentille l1 = new Lentille(200,200,0,Color.BLACK,100,20,this);
     //Lentille l2 = new Lentille(500,200,Math.PI/2,Color.GREEN,100,20,this);
     Lentille l3 = new Lentille(200,500,Math.PI/4,Color.RED,100,-20,this);
     private int cursor;
     private ObjetOptique selectedObject;
     private Point selectedPoint;
     private ArrayList<ObjetOptique> listeObjet;
     private boolean mooving;
     private Point positionningPoint1 , positionningPoint2 ;
     private Line2D.Double postionningLine;
     private JFrame parentFrame;

     public ZoneTracage(JFrame parentFrame){
		  Source s1 = new Source(300,300,0,Color.GREEN,30,this);
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
          setSize(500,500);
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
               if(i instanceof Lentille){
                    if(((Lentille)i).getPoint1().distance(e.getPoint())<((Lentille)i).getPoint2().distance(e.getPoint()) && ((Lentille)i).getPoint2().distance(e.getPoint()) != ((Lentille)i).getPoint1().distance(e.getPoint())){
                         if(((Lentille)i).getPoint1().distance(e.getPoint())<5){
                              selectedPoint = ((Lentille)i).getPoint1();
                         }
                    }
                    else if(((Lentille)i).getPoint1().distance(e.getPoint())>((Lentille)i).getPoint2().distance(e.getPoint()) && ((Lentille)i).getPoint2().distance(e.getPoint())<5 && ((Lentille)i).getPoint2().distance(e.getPoint()) != ((Lentille)i).getPoint1().distance(e.getPoint())) {
                         selectedPoint = ((Lentille)i).getPoint2();
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
               selectedPoint = ((Lentille)selectedObject).movePoint(e.getPoint(),selectedPoint);
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
          g2d.setColor(Color.WHITE);
          g2d.fillRect(0,0,this.getWidth(),this.getHeight());
          for (ObjetOptique o : listeObjet){
               o.draw(g2d);
          }
          if(postionningLine != null){
               g2d.draw(postionningLine);
          }
          intersection();
          repaint();
     }
	public void intersection(){
		ArrayList<Point> tabIntersection = new ArrayList<Point>();
		Point intersec = new Point();
		Point newintersec = new Point();
		for(ObjetOptique s:listeObjet){ //Pour toute les sources determination de l'equation de droite
			if(s instanceof Source){
				double x1 = s.getPoint1().x;
				double y1 = s.getPoint1().y;

				double x2 = s.getPoint2().x;
				double y2 = s.getPoint2().y;

				double coeffs = (y2-y1)/(x2-x1); //coef directeur de l'equation de droite de la source
				double ordos = y1 - coeffs*x1;    //ordonée à l'origine
			
				Line2D faisceau = new Line2D.Double();

				for(ObjetOptique l:listeObjet){ //Pour tout les objets optique autres que des sources
					if(l instanceof Lentille || l instanceof Miroir){

						x1 = l.getPoint1().x;
						y1 = l.getPoint1().y;

						x2 = l.getPoint2().x;
						y2 = l.getPoint2().y;

						double coeffl = (y2-y1)/(x2-x1); //coef directeur de l'equation de droite de l'objet optique
						double ordol = y1 - coeffl*x1;    //ordonée à l'origine

						double xi = (ordol-ordos)/(coeffs-coeffl);
						newintersec = new Point((int)xi, (int)(coeffl*xi + ordol));    //point d'intersection des deux droites
						//System.out.println(newintersec+" lentille centre " +l.getCentre());
						faisceau = new Line2D.Double(s.getCentre(),newintersec);
     
						if( (s.getPoint1()).distance(newintersec)<(s.getPoint1()).distance(intersec) && (l.getLine()).intersectsLine(faisceau)){
							intersec = newintersec;
						}
					}
				}
			}
		}
		tabIntersection.add(intersec);
		//System.out.println(intersec);
	}
}
