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
    protected static double f=10;
    protected static int n=0;
    protected JComboBox<String> choixCouleurs;
    final String[] couleurs = { "Bleu", "Vert", "Rouge", "Rose", "Orange" };

    public Propriete(int width, int height) {

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
        boxSemiReflet = new JCheckBox("Semi réfléchissant");
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
        entreX.setText(String.valueOf(l.getCentre().x));
        entreY.setText(String.valueOf(l.getCentre().y));
        entreTaille.setText(String.valueOf(l.getTaille));  
        entreFocal.setText(String.valueOf(l.getFocal()));
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

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String couleurChoisie = (String) cb.getSelectedItem();
        updateColor(couleurChoisie);
    }

}
