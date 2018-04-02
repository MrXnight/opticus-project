import java.awt.Dimension;      //Import des différentes librairies Java
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.*;
import javax.swing.JColorChooser;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Propriete extends JPanel implements ActionListener{           //Cette classe correspond au paneau de droite dans le programme
    protected JLabel nomOutil, changerCouleur, description, labelFocal, labelTaille, labelX, labelY, labelAngle;
    protected JCheckBox boxPlans, boxSemiReflet;
    protected JTextField entreX, entreY, entreTaille, entreAngle, entreFocal;
    protected JButton btnValider,btnCouleur, btnSupprimer;
    protected ZoneTracage panelDessin;
    protected Color couleurChoisi;
    protected static double f=10;
    protected static int n=0;

    public Propriete(int width, int height) {       //Constructeur de la classe

         this.panelDessin = null;

        this.setPreferredSize(new Dimension((int) (width * 0.2), height));

        nomOutil = new JLabel();                //On initialise tout les objets graphique qui apparaîtrons dans les propriétés
        nomOutil.setForeground(Color.GRAY);
        nomOutil.setFont(new Font("Cambria", Font.BOLD, 25));
        nomOutil.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        description = new JLabel();
        description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        description.setFont(new Font("Cambria", Font.PLAIN, 20));

        changerCouleur = new JLabel("Choisissez une couleur");

        btnCouleur = new JButton("Couleur");
        btnCouleur.addActionListener(this);
        couleurChoisi = Color.BLACK;

        labelX = new JLabel("x =");
        labelY = new JLabel("y =");
        labelTaille = new JLabel("Taille =");
        labelAngle = new JLabel("Angle =");
        labelFocal = new JLabel("f = ");

        entreX = new JTextField();
        Action actionEntreX = new AbstractAction()      //Permet d'actualiser les coordonnée X d'un objet sélectionné
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                  if(panelDessin.getSelectedObject() != null){
                     panelDessin.getSelectedObject().setCentreX(Double.parseDouble(entreX.getText()));
                     panelDessin.repaint();
                 }
            }
        };
        entreX.addActionListener(actionEntreX);

        entreY = new JTextField();
        Action actionEntreY = new AbstractAction()      //Permet d'actualiser les coordonnée Y d'un objet sélectionné
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                  if(panelDessin.getSelectedObject() != null){
                     panelDessin.getSelectedObject().setCentreY(Double.parseDouble(entreY.getText()));
                     panelDessin.repaint();
                 }
            }
        };
        entreY.addActionListener(actionEntreY);

        entreTaille = new JTextField();

        entreAngle = new JTextField();
        Action actionEntreAngle = new AbstractAction()  //Permet d'actualiser l'angle d'un objet sélectionné
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                  if(panelDessin.getSelectedObject() != null){
                      panelDessin.getSelectedObject().setAngle(Double.parseDouble(entreAngle.getText())*Math.PI/180.0);
                      panelDessin.repaint();
                 }
             }
        };
        entreAngle.addActionListener(actionEntreAngle);

        entreFocal = new JTextField();
        Action actionEntreFocal = new AbstractAction()  //Permet d'actualiser la distance focale d'une lentille selectionnée
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                  if(panelDessin.getSelectedObject() instanceof Lentille){
                    ((Lentille)panelDessin.getSelectedObject()).setFocal(Double.parseDouble(entreFocal.getText()));
                    panelDessin.repaint();
               }
             }
        };
        entreFocal.addActionListener(actionEntreFocal);

        boxPlans = new JCheckBox("Afficher plan focal");
        boxPlans.addItemListener(new ItemListener(){
             public void itemStateChanged(ItemEvent e){
                  Object source = e.getItemSelectable();
                  if(source == boxPlans){
                       if (e.getStateChange() == ItemEvent.DESELECTED){
                            if(panelDessin.getSelectedObject() instanceof Lentille){
                                 ((Lentille)panelDessin.getSelectedObject()).setAffichagePlanFocal(false);
                            }
                       }
                       else if(e.getStateChange() == ItemEvent.SELECTED){
                            if(panelDessin.getSelectedObject() instanceof Lentille){
                                 ((Lentille)panelDessin.getSelectedObject()).setAffichagePlanFocal(true);
                            }
                       }
                  }
                  panelDessin.repaint();
             }
        });

        boxSemiReflet = new JCheckBox("Semi réfléchissant");
        boxSemiReflet.addActionListener(this);

        btnValider = new JButton("Valider");
        btnValider.addActionListener(this);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(this);


        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(nomOutil);         //On affiche les paramètres du premier outil sélectionné
        this.add(description);
        this.add(new JSeparator(SwingConstants.HORIZONTAL));

    }

    public void propSource() {      //Methode qui affiche tout les caractéristique relative à un objet Source (affichage appelé par l'outil qui créé les sources)
        panelDessin.resetFocus();
        this.removeAll();
        entreTaille.setEditable(true);
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Source");
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle source en definissant deux point dans l'espace.</html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(btnCouleur);
        this.add(btnValider);
        this.repaint();
    }

    public void propSource(Source s) {      //Methode qui affiche tout les caractéristique relative à un objet Source lorsqu'il est sélectionné (affichage appelé par le clic sur une source)
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Source S" + s.getNum());
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        entreX.setText(String.valueOf(s.getCentrex()));
        entreY.setText(String.valueOf(s.getCentrey()));
        entreTaille.setText(String.valueOf(s.getTaille()));
        entreTaille.setEditable(false);
        entreAngle.setText(String.valueOf(Math.round((s.getAngle()*180/Math.PI*100))/100.0));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(btnCouleur);
        this.add(btnValider);
        this.add(btnSupprimer);
        this.repaint();
    }

    public void propLentille() {        //Methode qui affiche tout les caractéristique relative à un objet Lentille (affichage appelé par l'outil qui créé les lentilles)
        panelDessin.resetFocus();
        this.removeAll();
        entreX.setText("");
        entreY.setText("");
        entreTaille.setText("");
        entreTaille.setEditable(true);
        entreFocal.setText("");
        entreAngle.setText("");
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Lentille");
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle lentille en definissant deux point dans l'espace.</html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(btnCouleur);
        this.add(btnValider);
        this.repaint();
    }

    public void propLentille(Lentille l) {      //Methode qui affiche tout les caractéristique relative à un objet Lentille lorsqu'il est sélectionné (affichage appelé par le clic sur une lentille)
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Lentille L" + l.getNum());
        entreX.setText(String.valueOf(l.getCentrex()));
        entreY.setText(String.valueOf(l.getCentrey()));
        entreTaille.setText(String.valueOf(l.getTaille()));
        entreTaille.setEditable(false);
        entreFocal.setText(String.valueOf(l.getFocal()));
        boxPlans.setSelected(l.getAffichagePlanFocal());
        entreAngle.setText(String.valueOf(Math.round((l.getAngle()*180/Math.PI*100))/100.0));
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(btnCouleur);
        this.add(btnValider);
        this.add(btnSupprimer);
        this.repaint();

    }

    public void propSelect() {      //Méthode qui permet d'afficher les propriété de l'outil sélectionner
         if(panelDessin != null){
              panelDessin.resetFocus();
         }
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Selectionner");
        description.setText(
            "<html>Permet de deplacer des objets deja placés </html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();
    }

    public void propMiroir() {      //Methode qui affiche tout les caractéristique relative à un objet Miroir (affichage appelé par l'outil qui créé les miroir)
        panelDessin.resetFocus();
        this.removeAll();
        entreTaille.setEditable(true);
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Mirroir");
        description.setText(
            "<html>Cet outil vous permet de creer un nouveau miroir en definissant deux point dans l'espace.</html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(boxSemiReflet);
        this.add(btnCouleur);
        this.add(btnValider);
        this.repaint();
    }

    public void propMiroir(Miroir m) {      //Methode qui affiche tout les caractéristique relative à un objet Miroir lorsqu'il est sélectionné (affichage appelé par le clic sur un miroir)
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Mirroir M" + m.getNum());
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        entreX.setText(String.valueOf(m.getCentrex()));
        entreY.setText(String.valueOf(m.getCentrey()));
        entreTaille.setText(String.valueOf(m.getTaille()));
        entreTaille.setEditable(false);
        boxSemiReflet.setSelected(m.getSemiReflechissant());
        entreAngle.setText(String.valueOf(Math.round((m.getAngle()*180/Math.PI*100))/100.0));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        this.add(boxSemiReflet);
        this.add(btnCouleur);
        this.add(btnValider);
        this.add(btnSupprimer);
        this.repaint();
    }

    public void propSuppr() {          //Méthode qui permet d'afficher les propriété de l'outil supprimer
        panelDessin.resetFocus();
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Supprimer");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();

    }

    public void propScreenshot() {      //Méthode qui permet d'afficher les propriété de l'outil capture d'écran
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Capture d'ecran");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();

    }

    public double getEntreFocalValue(){     //Méthode qui permet de détecter un éventuel oubli de renseignement de la distance focale par l'utilisateur lorsqu'il créé une lentille
         try{
              return Double.parseDouble(entreFocal.getText());
         }
         catch(Exception e){
              JOptionPane.showMessageDialog(null,"Veuillez entrer une valeur de focal f.");
         }
         return 0.0;
    }

    public Color getCouleurChoisi(){
         return couleurChoisi;
    }


    public void setZoneTracage(ZoneTracage panelDessin){       //Méthode qui permet de récupérer le paneau de la zone de dessin sans passer par le constructeur pour qu'elle soit actualisée
         this.panelDessin = panelDessin;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnCouleur){                    //Affichage de la fenêtre de changement de couleur
             JColorChooser chooser = new JColorChooser(Color.BLACK);
             AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
             chooser.setPreviewPanel(new JPanel());
             for (int i = 0; i < oldPanels.length; i++) {
                  String clsName = oldPanels[i].getClass().getName();
                  if (!clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
                       chooser.removeChooserPanel(oldPanels[i]);
                  }
             }
            JDialog diag = JColorChooser.createDialog(this.getParent(),"Couleur de l'objet",true,chooser,new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                      couleurChoisi = chooser.getColor();
                 }
            },null);
            diag.setVisible(true);
            if(panelDessin.getSelectedObject() != null){                //On applique la couleur à l'objet sélectionné
                 panelDessin.getSelectedObject().setColor(couleurChoisi);
                 panelDessin.repaint();
            }
            System.out.println(couleurChoisi);
        }
        if(e.getSource() == btnValider){                    //Actualisation des propriété d'un objet sélectionné par le clic sur le bouton valider
             if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
                  try{
                       double focal = Double.parseDouble(entreFocal.getText());
                       double positionX = Double.parseDouble(entreX.getText());
                       double positionY = Double.parseDouble(entreY.getText());
                       double taille = Double.parseDouble(entreTaille.getText());
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180;
                       panelDessin.addObjetOptique(new Lentille(positionX,positionY,angle,couleurChoisi,taille,focal,panelDessin));
                       panelDessin.repaint();
                  }
                  catch(Exception except){
                       JOptionPane.showMessageDialog(null,"Veuillez renseigner toute les valeurs nécessaire à la création d'une lentille !");
                  }
             }
             else if(BarreOutils.activeTool.equals(ActiveTool.SOURCE)){
                  try{
                       double positionX = Double.parseDouble(entreX.getText());
                       double positionY = Double.parseDouble(entreY.getText());
                       double taille = Double.parseDouble(entreTaille.getText());
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180;
                       panelDessin.addObjetOptique(new Source(positionX,positionY,angle,couleurChoisi,taille,panelDessin));
                       panelDessin.repaint();
                  }
                  catch(Exception except){
                       JOptionPane.showMessageDialog(null,"Veuillez renseigner toute les valeurs nécessaire à la création d'une source !");
                  }
             }
             else if(BarreOutils.activeTool.equals(ActiveTool.MIROIR)){
                  try{
                       double positionX = Double.parseDouble(entreX.getText());
                       double positionY = Double.parseDouble(entreY.getText());
                       double taille = Double.parseDouble(entreTaille.getText());
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180;
                       panelDessin.addObjetOptique(new Miroir(positionX,positionY,angle,couleurChoisi,taille,panelDessin));
                       panelDessin.repaint();
                  }
                  catch(Exception except){
                       JOptionPane.showMessageDialog(null,"Veuillez renseigner toute les valeurs nécessaire à la création d'un miroir !");
                  }
             }
        }
        if(e.getSource() == btnSupprimer){                    //Affichage de la fenêtre de changement de couleur
            panelDessin.removeObjetOptique(panelDessin.getSelectedObject());
            panelDessin.resetFocus();
            this.propSelect();
            panelDessin.repaint();
        }
    }

}
