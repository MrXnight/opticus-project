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
     public static Line2D[] bordures;

     public ZoneTracage(JFrame parentFrame){
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
          //listeObjet.add(s2);       
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
          bordures = new Line2D[4];
		  bordures[0] = new Line2D.Double(0,0,this.getWidth(),0);
          bordures[1] = new Line2D.Double(this.getWidth(),0,this.getWidth(),this.getHeight());
          bordures[2] = new Line2D.Double(0,this.getHeight(),this.getWidth(),this.getHeight());
          bordures[3] = new Line2D.Double(0,0,0,this.getHeight());
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
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,this.getWidth(),this.getHeight());
        for (ObjetOptique o : listeObjet){
			o.draw(g2d);
            if(o instanceof Source){
				Source s = (Source)o;
				intersection(g2d, s);
			}
		}
        if(postionningLine != null){
			g2d.draw(postionningLine);
        }
        repaint();
	}
	public Point intersection(Graphics2D g2d, Source s){
		ArrayList<Point> tabIntersection = new ArrayList<Point>();
		Point newintersec = new Point();
		Point intersec = new Point();//Pour toute les sources determination de l'equation de droite
		for(ObjetOptique l:listeObjet){ //Pour tout les objets optique autres que des sources
			if(l instanceof Lentille || l instanceof Miroir){
				lineLine(s,l);
				Line2D faisceau= new Line2D.Double(s.getPoint1(), newintersec);
				if(l.getPoint1().x - l.getPoint2().x == 0){
					if(newintersec.y <= Math.max(l.getPoint1().y,l.getPoint2().y) && newintersec.y >= Math.min(l.getPoint1().y,l.getPoint2().y)){
						if(s.getPoint2().x-s.getPoint1().x > 0 && newintersec.x >= s.getPoint1().x){
							tabIntersection.add(newintersec);
						}
						else if(s.getPoint2().x-s.getPoint1().x < 0 && newintersec.x <= s.getPoint1().x){
							tabIntersection.add(newintersec);
						}
						else if(s.getPoint2().x-s.getPoint1().x == 0){
							if(s.getPoint2().y-s.getPoint1().y >0 && newintersec.y >= s.getPoint1().y){
								tabIntersection.add(newintersec);
							}
							else if(s.getPoint2().y-s.getPoint1().y <0 && newintersec.y <= s.getPoint1().y){
								tabIntersection.add(newintersec);
							}
							else{
								return null;
							}
						}
						else{
							return null;
						}
					}
				}
				else if(l.getLine().getBounds().contains(newintersec)){
					System.out.println("intersect");
					if(s.getPoint2().x-s.getPoint1().x > 0 && newintersec.x >= s.getPoint1().x){
						tabIntersection.add(newintersec);
					}
					else if(s.getPoint2().x-s.getPoint1().x < 0 && newintersec.x <= s.getPoint1().x){
						tabIntersection.add(newintersec);
					}
					else if(s.getPoint2().x-s.getPoint1().x == 0){
						System.out.println("s1.x - s2.x = 0");
						if(s.getPoint2().y-s.getPoint1().y >0 && newintersec.y >= s.getPoint1().y){
							tabIntersection.add(newintersec);
						}
						else if(s.getPoint2().y-s.getPoint1().y <0 && newintersec.y <= s.getPoint1().y){
							tabIntersection.add(newintersec);
						}
						else{
							return null;
						}
					}
					else{
						return null;
					}
				}
				else{
					return null;
				}
			}
		}
        if(tabIntersection.size() !=0){
			intersec = tabIntersection.get(0);
            for(Point x:tabIntersection){
				if((s.getCentre()).distance(x)<(s.getCentre()).distance(intersec)){
					intersec = x;
                }
            }
        }
        g2d.drawLine(intersec.x+10,intersec.y,intersec.x-10,intersec.y);
        g2d.drawLine(intersec.x,intersec.y-10,intersec.x,intersec.y+10);
        g2d.drawLine(intersec.x+10,intersec.y,intersec.x-10,intersec.y);
        g2d.drawLine(intersec.x,intersec.y-10,intersec.x,intersec.y+10);
		//System.out.println(intersec);
		return intersec;
	}
    
    public Point crossed(ObjetOptique a, ObjetOptique b){
        double ax1 = a.getPoint1().x;
        double ay1 = a.getPoint1().y;
        double ax2 = a.getPoint2().x;
        double ay2 = a.getPoint2().y;
        double acoeff = (ay2-ay1)/(ax2-ax1); //coef directeur de l'equation de droite de la source
        double aordo = ay1 - acoeff*ax1;    //ordonée à l'origine
        
        double bx1 = b.getPoint1().x;
        double by1 = b.getPoint1().y;
        double bx2 = b.getPoint2().x;
        double by2 = b.getPoint2().y;

        if(bx2-bx1 == 0){			
			Point i = new Point((int)bx1,(int)(acoeff*bx1 + aordo)); 
			System.out.println(i);
			return i;
		}
		else{
			double bcoeff = (by2-by1)/(bx2-bx1); //coef directeur de l'equation de droite de la lentille
			double bordo = by1 - bcoeff*bx1;    //ordonée à l'origine
			double xi = (aordo-bordo)/(bcoeff-acoeff);
			Point i = new Point((int)xi,(int)(acoeff*xi + aordo)); 
			return i;
		}    
    }
	public Point lineLine(ObjetOptique a, ObjetOptique b){
		int x1 = a.getPoint1().x;
		int y1 = a.getPoint1().y;
		int x2 = a.getPoint2().x;
		int y2 = a.getPoint2().y;
		int x3 = b.getPoint1().x;
		int y3 = b.getPoint1().y;
		int x4 = b.getPoint2().x;
		int y4 = b.getPoint2().y;
		int d = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
		System.out.println("d = "+d);
		if(d != 0){
			double xi=(double)((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4))/(double)(d);
			double yi=(double)((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4))/(double)(d);
			Point i = new Point((int)xi,(int)yi);
			return(i);
		}
		return null;
		
	}
    
    public boolean isCrossed(ObjetOptique a, Point p){
        double xmin = Math.min(a.getPoint1().x,a.getPoint2().x);
        double xmax = Math.max(a.getPoint1().x,a.getPoint2().x);
        double ymin = Math.min(a.getPoint1().y,a.getPoint2().y);
        double ymax = Math.max(a.getPoint1().y,a.getPoint2().y);
        
        if(p.x<xmax && p.y<ymax && p.x>xmin && p.y>ymin){
            return true;
        }
        return false;
    }
}
