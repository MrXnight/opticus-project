import java.awt.*;      //Import des différentes librairies Java
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

public class FenetrePrincipale extends JFrame implements ActionListener{        //Cette classe correspond à la fenêtre dans laquelle toute l'interface graphique va être affichée

	private final double ratio = 0.8;
	private JPanel mainPanel;
	private ZoneTracage panelDessin;

	public FenetrePrincipale (){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}


		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     //On détermine la taille de la fenêtre en fonction de celle de l'écran de l'utilisateur
		int width = (int)(ratio*screenSize.getWidth());
		int height = (int)(ratio*screenSize.getHeight());


		this.setSize(width,height);         //On paramètre le titre la dimension de la fenêtre
		this.setTitle("Opticus Magicus");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(false);
		//on crée le panel principal
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		panelDessin = new ZoneTracage(this);

		//on instancie la barre de menu
		Toolbar tool = new Toolbar(panelDessin);
		this.setJMenuBar(tool);

		//on instancie le panel de propriété
		Propriete prop = new Propriete(width, height);

		mainPanel.add(prop, BorderLayout.EAST);

		BarreOutils barre = new BarreOutils(prop);
		mainPanel.add(barre, BorderLayout.WEST);

		mainPanel.add(panelDessin,BorderLayout.CENTER);

		this.add(mainPanel);

		prop.setZoneTracage(panelDessin);
		panelDessin.setPropriete(prop);

		this.setVisible(true);

	}

	public static void main(String[] args){
		FenetrePrincipale fen = new FenetrePrincipale();
	}

	public void actionPerformed(ActionEvent e) {
	}

}
