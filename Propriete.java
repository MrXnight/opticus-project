import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.*;

public class Propriete extends JPanel implements ActionListener {
    protected JLabel nomOutil, changerNom, changerCouleur, description, labelFocal, labelTaille, labelX, labelY;
    protected JCheckBox boxPlans, boxSemiReflet;
    protected JTextField entreNom, entreX, entreY, entreTaille;
    protected JTextField entreFocal;
    protected JButton btnValider;   
    protected ZoneTracage panelDessin;
    protected static double f=10;
    protected static int n=0;
    protected JComboBox<String> choixCouleurs;
    final String[] couleurs = { "Bleu", "Vert", "Rouge", "Rose", "Orange" };

    public Propriete(int width, int height) {

         this.panelDessin = panelDessin;

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

        choixCouleurs = new JComboBox<>(couleurs);
        choixCouleurs.addActionListener(this);

        labelX = new JLabel("x =");
        labelY = new JLabel("y =");
        labelTaille = new JLabel("Taille =");
        entreX = new JTextField();
        entreY = new JTextField();
        entreTaille = new JTextField();

        labelFocal = new JLabel("f = ");
        entreFocal = new JTextField();
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
        this.add(changerCouleur);
        this.add(choixCouleurs);
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
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(changerCouleur);
        this.add(choixCouleurs);
        this.add(btnValider);
        this.repaint();
    }

    public void propLentille() {
        this.removeAll();
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
        //this.add(changerNom);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(changerCouleur);
        this.add(choixCouleurs);
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
        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        //this.add(changerNom);
        this.add(labelFocal);
        this.add(entreFocal);
        this.add(boxPlans);
        this.add(changerCouleur);
        this.add(choixCouleurs);
        this.add(btnValider);
        this.repaint();

    }

    public void propSelect() {
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
        this.add(boxSemiReflet);
        this.add(changerCouleur);
        this.add(choixCouleurs);
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
        this.add(labelX);
        this.add(entreX);
        this.add(labelY);
        this.add(entreY);
        this.add(labelTaille);
        this.add(entreTaille);
        this.add(boxSemiReflet);
        this.add(changerCouleur);
        this.add(choixCouleurs);
        this.add(btnValider);
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

    public void updateColor(String couleurChoisie) {
        Color col = new Color(255,255,255);
        switch (couleurChoisie) {
        case "Bleu":col = Color.BLUE;
            break;
        case "Vert":col = Color.GREEN;
            break;
        case "Rouge":col = Color.RED;
            break;
        case "Violet":col = Color.PINK;
            break;
        case "Orange":col = Color.ORANGE;
            break;
        default:col = Color.RED;
            break;
        }
        nomOutil.setForeground(col);
        this.repaint();
    }

    public void setZoneTracage(ZoneTracage panelDessin){
         this.panelDessin = panelDessin;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == choixCouleurs){
            JComboBox cb = (JComboBox) e.getSource();
            String couleurChoisie = (String) cb.getSelectedItem();
            updateColor(couleurChoisie);
        }
        if(e.getSource() == btnValider){
            
        }
    }

}
