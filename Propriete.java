import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.*;
import java.awt.*;

public class Propriete extends JPanel implements ActionListener {
    protected JLabel nomOutil, changerNom, changerCouleur, description;
    protected JTextField entreNom;
    protected JComboBox<String> choixCouleurs;
    final String[] couleurs = { "Bleu", "Vert", "Rouge", "Rose", "Orange" };

    public Propriete(int width, int height) {

        this.setPreferredSize(new Dimension((int) (width * 0.2), height));

        nomOutil = new JLabel();
        nomOutil.setForeground(Color.GRAY);
        nomOutil.setFont(new Font("Serif", Font.BOLD, 25));
        nomOutil.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        description = new JLabel();
        description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        description.setFont(new Font("Serif", Font.PLAIN, 20));

        changerNom = new JLabel("Entrer un nom");
        changerCouleur = new JLabel("Choisissez une couleur");

        choixCouleurs = new JComboBox<>(couleurs);
        choixCouleurs.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(nomOutil);
        this.add(description);

        this.add(new JSeparator(SwingConstants.HORIZONTAL));
        this.add(changerNom);
        this.add(changerCouleur);
        this.add(choixCouleurs);

    }

    public void propLentille() {
        nomOutil.setText("Lentille");
        description.setText(
            "<html>Cet outil vous permet de creer une nouvelle lentille en definissant deux point dans l'espace.</html>");
        changerNom.setText("Donner un nom à la lentille : ");
        this.repaint();
    }

    public void propSelect() {
        nomOutil.setText("Selectionner");
        description.setText(
            "<html>Permet de deplacer des objets deja placés </html>");
        this.repaint();
    }

    public void propMiroir() {
        nomOutil.setText("Mirroir");
        description.setText("Placer un miroir....");
        this.repaint();

    }

    public void propSuppr() {
        nomOutil.setText("Supprimer");
        this.repaint();

    }

    public void propScreenshot() {
        nomOutil.setText("Capture d'ecran");
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
