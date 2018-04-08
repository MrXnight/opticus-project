import java.awt.event.*;    //Import des différentes librairies Java
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.*;

public class BarreOutils extends JToolBar implements ActionListener{

     private JButton btnSelect, btnSource, btnLentille, btnMiroir, btnSuppr, btnScreenshot;
     public static ActiveTool activeTool = ActiveTool.SELECT;
     private Propriete prop;

     public BarreOutils(Propriete prop) {       //Constructeur de la classe qui affiche tout les boutons de la barre d'outil et qui choisit l'outil sélection au lancement du programme
          super("Barre d'outils",JToolBar.VERTICAL);
          this.prop = prop;

          //Instanciation des différents boutons de la barre d'outils
          btnSelect = new JButton(new ImageIcon("images/select.png"));
          btnSource = new JButton(new ImageIcon("images/source.png"));
          btnLentille = new JButton(new ImageIcon("images/lens.png"));
          btnMiroir = new JButton(new ImageIcon("images/mirror.png"));
          btnSuppr = new JButton(new ImageIcon("images/suppr.png"));
          btnScreenshot = new JButton(new ImageIcon("images/screenshot.png"));

          //On ajoute les listeners aux boutons
          btnSelect.addActionListener(this);
          btnSource.addActionListener(this);
          btnLentille.addActionListener(this);
          btnMiroir.addActionListener(this);
          btnSuppr.addActionListener(this);
          btnScreenshot.addActionListener(this);

          //On ajoute les boutons au JToolBar
          this.add(btnSelect);
          this.add(btnSource);
          this.add(btnLentille);
          this.add(btnMiroir);
          this.add(btnSuppr);
          this.add(btnScreenshot);
          prop.propSelect();
     }


     public void actionPerformed(ActionEvent e) {       //Lors d'un clic sur un bouton outil, on met à jour l'outil actif dans la variable activeTool et on affiche dans le panel propriété les informations correspondantes
          JButton btn = (JButton) e.getSource();

          if(btn == btnSource){
               if(!BarreOutils.activeTool.equals(ActiveTool.SOURCE)){
                    BarreOutils.activeTool = ActiveTool.SOURCE;
                    //System.out.println(BarreOutils.activeTool+" est actif !");
                    prop.propSource(); //affichage des propriétés
               }
          }

          if(btn == btnLentille){
               if(!BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
                    BarreOutils.activeTool = ActiveTool.LENTILLE;
                    //System.out.println(BarreOutils.activeTool+" est actif !");
                    prop.propLentille(); //affichage des propriétés

               }
          }

          if(btn == btnMiroir){
               if(!BarreOutils.activeTool.equals(ActiveTool.MIROIR)){
                    BarreOutils.activeTool = ActiveTool.MIROIR;
                    //System.out.println(BarreOutils.activeTool+" est actif !");
                    prop.propMiroir(); //affichage des propriétés
               }
          }

          if(btn == btnSelect){
               if(!BarreOutils.activeTool.equals(ActiveTool.SELECT)){
                    BarreOutils.activeTool = ActiveTool.SELECT;
                    prop.propSelect(); //affichage des propriétés
                    //System.out.println(BarreOutils.activeTool+" est actif !");
               }

          }

          if(btn == btnSuppr){
               if(!BarreOutils.activeTool.equals(ActiveTool.SUPPR)){
                    BarreOutils.activeTool = ActiveTool.SUPPR;
                    //System.out.println(BarreOutils.activeTool+" est actif !");
                    prop.propSuppr(); //affichage des propriétés
               }
          }

          if(btn == btnScreenshot){
               if(!BarreOutils.activeTool.equals(ActiveTool.SCREENSHOT)){
                    BarreOutils.activeTool = ActiveTool.SCREENSHOT;
                    //System.out.println(BarreOutils.activeTool+" est actif !");
                    prop.propScreenshot(); //affichage des propriétés
               }
          }

     }
}
