import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.*;

public class BarreOutils extends JToolBar implements ActionListener{

    JButton btnSelect, btnLentille, btnMiroir, btnSuppr, btnScreenshot;
    public static ActiveTool activeTool = ActiveTool.NULL;

    public BarreOutils() {
        super("Barre d'outils",JToolBar.VERTICAL);
        btnSelect = new JButton(new ImageIcon("images/select.png"));
        btnLentille = new JButton(new ImageIcon("images/lens.png"));
        btnMiroir = new JButton(new ImageIcon("images/mirror.png"));
        btnSuppr = new JButton(new ImageIcon("images/suppr.png"));
        btnScreenshot = new JButton(new ImageIcon("images/screenshot.png"));

        btnSelect.addActionListener(this);
        btnLentille.addActionListener(this);
        btnMiroir.addActionListener(this);
        btnSuppr.addActionListener(this);
        btnScreenshot.addActionListener(this);

        this.add(btnSelect);
        this.add(btnLentille);
        this.add(btnMiroir);
        this.add(btnSuppr);
        this.add(btnScreenshot);
    }


    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if(btn == btnLentille){
            BarreOutils.activeTool = ActiveTool.LENTILLE;
            System.out.println(BarreOutils.activeTool+" est actif !");
        }

        if(btn == btnMiroir){
             BarreOutils.activeTool = ActiveTool.SOURCE;
             System.out.println(BarreOutils.activeTool+" est actif !");
        }

        if(btn == btnSelect){
            BarreOutils.activeTool = ActiveTool.SELECT;
             System.out.println(BarreOutils.activeTool+" est actif !");
        }

        if(btn == btnSuppr){
            System.out.println("Suppr !");
        }

        if(btn == btnScreenshot){
            System.out.println("Screen !");
        }

    }
}
