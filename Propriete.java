import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.*;

public class Propriete extends JPanel {
    protected JLabel nomOutil;

    public Propriete() {
        this.setLayout(new FormLayout());

        JLabel lblSelectedtool = new JLabel("selectedTool");
        add(lblSelectedtool, "2, 2, 15, 1, center, default");

        JLabel lblTooldescription = new JLabel("toolDescription");
        add(lblTooldescription, "2, 4, 15, 1");

        JSeparator separator = new JSeparator();
        add(separator, "2, 6, 15, 1");

        JLabel lblNomDeLobjet = new JLabel("Nom de l'objet");
        add(lblNomDeLobjet, "2, 8, right, default");

        txtLentille = new JTextField();
        txtLentille.setText("lentille1");
        add(txtLentille, "4, 8, fill, default");
        txtLentille.setColumns(10);

        JLabel lblCouleur = new JLabel("Couleur ");
        add(lblCouleur, "2, 10");

        JList list = new JList();
        add(list, "4, 10, fill, fill");*/
    }

    public void propLentille() {
        nomOutil.setText("Lentille");
        this.repaint();
    }

    public void propSelect() {
        nomOutil.setText("Select !");
        this.repaint();
    }

    public void propMiroir() {
        nomOutil.setText("Mirroir !");
        this.repaint();

    }

    public void propSuppr() {
        nomOutil.setText("Suppr !");
        this.repaint();

    }

    public void propScreenshot() {
        nomOutil.setText("Screenshot !");
        this.repaint();

    }

}
