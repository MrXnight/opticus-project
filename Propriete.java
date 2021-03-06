import java.awt.Dimension;      //Import des différentes librairies Java
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.*;
import javax.swing.JColorChooser;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Propriete extends JPanel implements ActionListener{           //Cette classe correspond au paneau de droite dans le programme qui permet de modifier les paramètres d'un objet existant ou de créer un objet en renseignant différents paramètres
    protected JLabel nomOutil, changerCouleur, description, labelFocal, labelTaille, labelX, labelY, labelAngle;
    protected JCheckBox boxPlans, boxSemiReflet;
    protected JTextField entreX, entreY, entreTaille, entreAngle, entreFocal;
    protected JButton btnValider,btnCouleur, btnSupprimer;
    protected ZoneTracage panelDessin;
    protected Color couleurChoisi;
    protected Font font1;

    protected final int hauteurTextField = 15;

    public Propriete(int width, int height) {       //Constructeur de la classe
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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

        font1 = new Font("Cambria", Font.PLAIN, 20);

        labelX = new JLabel("x =");
        labelY = new JLabel("y =");
        labelTaille = new JLabel("Taille =");
        labelAngle = new JLabel("Angle =");
        labelFocal = new JLabel("f = ");



        entreX = new JTextField();
        entreX.setFont(font1);
        entreX.setHorizontalAlignment(JTextField.CENTER);
        //Les actionPerformed sont appelé lorsque l'on appuie sur entrée dans un JTextField
        Action actionEntreX = new AbstractAction()      //Permet d'actualiser les coordonnée X d'un objet sélectionné
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                  if(panelDessin.getSelectedObject() != null){
                     panelDessin.getSelectedObject().setCentreX(Double.parseDouble(entreX.getText())); //On récupère la coordonnée en de l'objet et on set la nouvelle coordonnée en x
                     panelDessin.repaint();
                 }
            }
        };
        entreX.addActionListener(actionEntreX);

        entreY = new JTextField();
        entreY.setFont(font1);
        entreY.setHorizontalAlignment(JTextField.CENTER);
        //entreY.setPreferredSize( new Dimension( (int) (width * 0.2), hauteurTextField ) );
        Action actionEntreY = new AbstractAction()      //Permet d'actualiser les coordonnée Y d'un objet sélectionné
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                  if(panelDessin.getSelectedObject() != null){
                     panelDessin.getSelectedObject().setCentreY(Double.parseDouble(entreY.getText())); //On récupère la coordonnée en de l'objet et on set la nouvelle coordonnée en y
                     panelDessin.repaint();
                 }
            }
        };
        entreY.addActionListener(actionEntreY);

        entreTaille = new JTextField();
        entreTaille.setFont(font1);
        entreTaille.setHorizontalAlignment(JTextField.CENTER);

        entreAngle = new JTextField();
        entreAngle.setFont(font1);
        entreAngle.setHorizontalAlignment(JTextField.CENTER);

        Action actionEntreAngle = new AbstractAction()  //Permet d'actualiser l'angle d'un objet sélectionné
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                  if(panelDessin.getSelectedObject() != null){
                      panelDessin.getSelectedObject().setAngle(Double.parseDouble(entreAngle.getText())*Math.PI/180.0); // on récupère la valeur de l'angle dans la zone de texte et on le convertit en radian pour set le nouvelle angle
                      panelDessin.repaint();
                 }
             }
        };
        entreAngle.addActionListener(actionEntreAngle);

        entreFocal = new JTextField();
        entreFocal.setFont(font1);
        entreFocal.setHorizontalAlignment(JTextField.CENTER);
        Action actionEntreFocal = new AbstractAction()  //Permet d'actualiser la distance focale d'une lentille selectionnée
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                  if(panelDessin.getSelectedObject() instanceof Lentille){
                    ((Lentille)panelDessin.getSelectedObject()).setFocal(Double.parseDouble(entreFocal.getText())); //Pour les lentilles on récupère la focal et on set la nouvelle focal
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
        boxSemiReflet.addItemListener(new ItemListener(){
             public void itemStateChanged(ItemEvent e){
                  Object source = e.getItemSelectable();
                  if(source == boxSemiReflet){
                       if (e.getStateChange() == ItemEvent.DESELECTED){
                            if(panelDessin.getSelectedObject() instanceof Miroir){
                                 ((Miroir)panelDessin.getSelectedObject()).setSemiReflechissant(false);
                            }
                       }
                       else if(e.getStateChange() == ItemEvent.SELECTED){
                            if(panelDessin.getSelectedObject() instanceof Miroir){
                                 ((Miroir)panelDessin.getSelectedObject()).setSemiReflechissant(true);
                            }
                       }
                  }
                  panelDessin.repaint();
             }
        });

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
            "<html>Cet outil vous permet de créer une nouvelle source en definissant deux point dans l'espace.</html>");
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
        nomOutil.setText("Source S" + s.getNum()); //On affiche les propriétés de l'objet selectionné
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        entreX.setText(String.valueOf(s.getCentrex()));
        entreY.setText(String.valueOf(s.getCentrey()));
        entreTaille.setText(String.valueOf(s.getTaille()));
        entreTaille.setEditable(false);
        entreAngle.setText(String.valueOf(Math.round((s.getAngle()*180/Math.PI*100))/100.0)); //On convertit l'angle de radian vers degré de l'objet selectionné
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
            "<html>Cet outil vous permet de créer une nouvelle lentille en definissant d'abord la distance focale puis deux point dans l'espace (ou alors en saisissant les paramètres ci-dessous).</html>");
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
        this.add(btnCouleur);
        this.add(btnValider);
        this.repaint();
    }

    public void propLentille(Lentille l) {      //Methode qui affiche tout les caractéristique relative à un objet Lentille lorsqu'il est sélectionné (affichage appelé par le clic sur une lentille)
        this.removeAll();
        this.add(nomOutil);
        nomOutil.setText("Lentille L" + l.getNum()); //On affiche les propriétés de l'objet selectionné
        entreX.setText(String.valueOf(l.getCentrex()));
        entreY.setText(String.valueOf(l.getCentrey()));
        entreTaille.setText(String.valueOf(l.getTaille()));
        entreTaille.setEditable(false);
        entreFocal.setText(String.valueOf(l.getFocal()));
        boxPlans.setSelected(l.getAffichagePlanFocal());
        entreAngle.setText(String.valueOf(Math.round((l.getAngle()*180/Math.PI*100))/100.0)); //On convertit l'angle de radian vers degré
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
            "<html>Cet outil permet de séléctionner et déplacer des objets déjà placés dans la zone de traçage</html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();
    }

    public void propMiroir() {      //Methode qui affiche tout les caractéristique relative à un objet Miroir (affichage appelé par l'outil qui créé les miroir)
        panelDessin.resetFocus();
        this.removeAll();
        entreTaille.setEditable(true);
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Miroir");
        description.setText(
            "<html>Cet outil vous permet de créer un nouveau miroir en definissant deux point dans l'espace.</html>");
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
        entreAngle.setText(String.valueOf(Math.round((m.getAngle()*180/Math.PI*100))/100.0)); //On convertit l'angle de radian vers degré
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

        //Récupération des dimensions de l'image
        int w = panelDessin.getWidth();
        int h = panelDessin.getHeight();

        //Création de l'image par le biais d'un buffer
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panelDessin.paint(g);

        //Récupération du nom et du path que choisi l'utilisateur
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarde du système optique");
        int userSelection = fileChooser.showSaveDialog(panelDessin);

        //Sauvegarde du fichier au format .png
        if(userSelection == JFileChooser.APPROVE_OPTION){
            String file_name = fileChooser.getSelectedFile().getPath() + ".png";
            File outputFile = new File(file_name);
            try{
                ImageIO.write(bi, "png", outputFile);
            } catch(IOException ex){
                ex.printStackTrace();
            }
            //Message de confirmation
            JOptionPane.showMessageDialog(null, "Image sauvegardee", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public double getEntreFocalValue(){     //Méthode qui permet de détecter un éventuel oubli de renseignement de la distance focale par l'utilisateur lorsqu'il créé une lentille
         try{
              return Double.parseDouble(entreFocal.getText());
         }
         catch(Exception e){
              JOptionPane.showMessageDialog(null,"Veuillez entrer une valeur de focale f.");
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
            //System.out.println(couleurChoisi);
        }
        if(e.getSource() == btnValider){                    //On valide les paramètres rentré dans les zones de textes pour créer l'objet correspondant à l'outil selectionné
             if(BarreOutils.activeTool.equals(ActiveTool.LENTILLE)){
                  try{
                       double focal = Double.parseDouble(entreFocal.getText());
                       double positionX = Double.parseDouble(entreX.getText());
                       double positionY = Double.parseDouble(entreY.getText());
                       double taille = Double.parseDouble(entreTaille.getText());
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180; //On convertit l'angle de degré vers radian
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
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180; //On convertit l'angle de degré vers radian
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
                       double angle = Double.parseDouble(entreAngle.getText())*Math.PI/180; //On convertit l'angle de degré vers radian
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
