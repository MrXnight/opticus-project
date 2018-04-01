import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.*;
import javax.swing.JColorChooser;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class Propriete extends JPanel implements ActionListener {
    protected JLabel nomOutil, changerNom, changerCouleur, description, labelFocal, labelTaille, labelX, labelY, labelAngle;
    protected JCheckBox boxPlans, boxSemiReflet;
    protected JTextField entreNom, entreX, entreY, entreTaille, entreAngle;
    protected JTextField entreFocal;
    protected JButton btnValider,btnCouleur;
    protected ZoneTracage panelDessin;
    protected Color couleurChoisi;
    protected static double f=10;
    protected static int n=0;

    public Propriete(int width, int height) {

         this.panelDessin = null;

        this.setPreferredSize(new Dimension((int) (width * 0.2), height));

        nomOutil = new JLabel();
        nomOutil.setForeground(Color.GRAY);
        nomOutil.setFont(new Font("Cambria", Font.BOLD, 25));
        nomOutil.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        description = new JLabel();
        description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        description.setFont(new Font("Cambria", Font.PLAIN, 20));

        //changerNom = new JLabel("Entrer un nom");
        changerCouleur = new JLabel("Choisissez une couleur");

        btnCouleur = new JButton("Couleur");
        btnCouleur.addActionListener(this);
        couleurChoisi = Color.BLACK;

        labelX = new JLabel("x =");
        labelY = new JLabel("y =");
        labelTaille = new JLabel("Taille =");
        labelAngle = new JLabel("Angle =");
        entreX = new JTextField();
        Action actionEntreX = new AbstractAction()
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
        Action actionEntreY = new AbstractAction()
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
        Action actionEntreAngle = new AbstractAction()
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
        labelFocal = new JLabel("f = ");
        entreFocal = new JTextField();
        Action actionEntreFocal = new AbstractAction()
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
        boxPlans.addActionListener(this);
        boxSemiReflet = new JCheckBox("Semi réfléchissant");
        boxSemiReflet.addActionListener(this);

        btnValider = new JButton("Valider");
        btnValider.addActionListener(this);
        //String.valueOf(f)

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(nomOutil);
        this.add(description);

        this.add(new JSeparator(SwingConstants.HORIZONTAL));

    }

    public void propSource() {
        panelDessin.resetFocus();
        this.removeAll();
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

    public void propSource(Source s) {
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Source" + s.getNum());
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle source en definissant deux point dans l'espace.</html>");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        entreX.setText(String.valueOf(s.getCentrex()));
        entreY.setText(String.valueOf(s.getCentrey()));
        entreTaille.setText(String.valueOf(s.getTaille()));
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
        this.repaint();
    }

    public void propLentille() {
        panelDessin.resetFocus();
        this.removeAll();
        entreX.setText("");
        entreY.setText("");
        entreTaille.setText("");
        entreFocal.setText("");
        entreAngle.setText("");
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Lentille L");
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle lentille en definissant deux point dans l'espace.</html>");
        //changerNom.setText("Donner un nom à la lentille : ");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(labelAngle);
        this.add(entreAngle);
        //this.add(changerNom);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(btnCouleur);
        this.add(btnValider);
        this.repaint();
    }

    public void propLentille(Lentille l) {
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Lentille L" + l.getNum());
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle lentille en definissant deux point dans l'espace.</html>");
        //changerNom.setText("Donner un nom à la lentille : ");
        entreX.setText(String.valueOf(l.getCentrex()));
        entreY.setText(String.valueOf(l.getCentrey()));
        entreTaille.setText(String.valueOf(l.getTaille()));
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
        //this.add(changerNom);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(btnCouleur);
        this.repaint();

    }

    public void propSelect() {
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

    public void propMiroir() {
        panelDessin.resetFocus();
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Mirroir");
        description.setText("Placer un miroir....");
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

    public void propMiroir(Miroir m) {
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Mirroir" + m.getNum());
        description.setText("Placer un miroir....");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        entreX.setText(String.valueOf(m.getCentrex()));
        entreY.setText(String.valueOf(m.getCentrey()));
        entreTaille.setText(String.valueOf(m.getTaille()));
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
        this.repaint();
    }

    public void propSuppr() {
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Supprimer");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();

    }

    public void propScreenshot() {
        this.removeAll();
        this.add(nomOutil);
        this.add(description);
        nomOutil.setText("Capture d'ecran");
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.repaint();

    }

    public double getEntreFocalValue(){
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


    public void setZoneTracage(ZoneTracage panelDessin){
         this.panelDessin = panelDessin;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnCouleur){
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
            if(panelDessin.getSelectedObject() != null){
                 panelDessin.getSelectedObject().setColor(couleurChoisi);
            }
            System.out.println(couleurChoisi);
        }
        if(e.getSource() == btnValider){
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
    }

}
