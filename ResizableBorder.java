import java.awt.Color;      //Import des différentes librairies Java
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ResizableBorder implements Border {    //Cette classe peret de déplacer la barre d'outil n'importe où dans la fenêtre
     private int dist = 8;

     int locations[] = {
          SwingConstants.NORTH_WEST,
          SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST,
          SwingConstants.SOUTH_EAST
     };

     int cursors[] = {
          Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
          Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
     };

     ResizableBorder(int dist){
          this.dist = dist;
     }

     @Override
     public Insets getBorderInsets(Component component) {
          return new Insets(dist, dist, dist, dist);
     }
     @Override
     public boolean isBorderOpaque(){
          return false;
     }

     @Override
     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h){
          g.setColor(Color.white);
          g.drawRect(x + dist / 2, y + dist / 2, w - dist, h - dist);
          if (c.hasFocus()) {
               g.setColor(Color.black);
               g.drawRect(x + dist / 2, y + dist / 2, w - dist, h - dist);
               for (int i = 0; i < locations.length; i++) {
                    Rectangle rect = drawRectangles(x, y, w, h, locations[i]);
                    g.setColor(Color.WHITE);
                    g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
                    g.setColor(Color.BLACK);
                    g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
               }
          }
     }

     private Rectangle drawRectangles(int x, int y, int w, int h, int location){
          switch (location) {
               case SwingConstants.NORTH_WEST:
               return new Rectangle(x, y, dist, dist);
               case SwingConstants.NORTH_EAST:
               return new Rectangle(x + w - dist, y, dist, dist);
               case SwingConstants.SOUTH_WEST:
               return new Rectangle(x, y + h - dist, dist, dist);
               case SwingConstants.SOUTH_EAST:
               return new Rectangle(x + w - dist, y + h - dist, dist, dist);
          }
          return null;
     }
     public int getCursor(MouseEvent me) {

        Component c = me.getComponent();
        int w = c.getWidth();
        int h = c.getHeight();

        for (int i = 0; i < locations.length; i++) {
            Rectangle rect = drawRectangles(0, 0, w, h, locations[i]);
            if (rect.contains(me.getPoint())) {
                return cursors[i];
            }
        }

        return Cursor.MOVE_CURSOR;
    }

}
