import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

public class FenetrePrincipale extends JFrame implements ActionListener{

	private final double ratio = 0.8;
	private JPanel mainPanel,panelDessin;

	public FenetrePrincipale (){

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)(ratio*screenSize.getWidth());
		int height = (int)(ratio*screenSize.getHeight());


		this.setSize(width,height);
		this.setTitle("Opticus Magicus");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//on crée le panel principal
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		panelDessin = new ZoneTracage();

		//on instancie la barre de menu
		Toolbar tool = new Toolbar();
		this.setJMenuBar(tool);

		BarreOutils barre = new BarreOutils();
		mainPanel.add(barre, BorderLayout.WEST);

		mainPanel.add(new Button("BAS"), BorderLayout.SOUTH);
		mainPanel.add(new Button("DROITE"), BorderLayout.EAST);
		mainPanel.add(panelDessin,BorderLayout.CENTER);

		this.add(mainPanel);
		this.setVisible(true);

	}

	public static void main(String[] args){
		FenetrePrincipale fen = new FenetrePrincipale();
	}

	public void actionPerformed(ActionEvent e) {
	}


}
