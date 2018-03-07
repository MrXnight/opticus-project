import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class Toolbar extends JMenuBar {
	JMenu menuFichier, submenu, menuAffiche, menuCredit ;
	JMenuItem itemImport , itemSave,itemQuit,itemCredit, menuItem2, menuItem3, menuItem4;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;
	
	public Toolbar(){

		//FICHIER
		menuFichier = new JMenu("Fichier");
		this.add(menuFichier);

		//Item importer, on crée un fileChooser qui permet d'importer un ficher ATTENTION IL FAUDRA APPLIQUER
		//UN FILTRE SUR L'EXTENSION DU FICHIER CHOISIE
		itemImport = new JMenuItem(new AbstractAction("Importer/Charger un fichier"){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Importation demandee");
				final JFileChooser fc = new JFileChooser();
    			int returnVal = fc.showOpenDialog(Toolbar.this);
    			if(returnVal == JFileChooser.APPROVE_OPTION) {
       			System.out.println("You chose to open this file: " +
            	fc.getSelectedFile().getName());
    			}
			}
		});
		itemImport.setIcon(new ImageIcon("images/import.png"));
		menuFichier.add(itemImport);

		//Item suvegarder
		itemSave = new JMenuItem(new AbstractAction("Sauvegarder"){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Sauvegarde demandee");
			}
		});
		itemSave.setIcon(new ImageIcon("images/save.png"));
		menuFichier.add(itemSave);

		//on crée un séprateur
		menuFichier.addSeparator();

		//Item quitter
		itemQuit = new JMenuItem(new AbstractAction("Quitter !"){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		itemQuit.setIcon(new ImageIcon("images/exit.png"));
		menuFichier.add(itemQuit);


		//on crée un séprateur
		menuFichier.addSeparator();

		menuItem2 = new JMenuItem("Deuxi\u00e8me option");
		menuFichier.add(menuItem2);

		menuItem3 = new JMenuItem("Troisi\u00e8me option");
		menuFichier.add(menuItem3);


		//AFFICHAGE
		menuAffiche = new JMenu("Affichage");
		this.add(menuAffiche);

		
		//A PROPOS
		menuCredit = new JMenu("A Propos");
		this.add(menuCredit);
		
		//on crée item crédit
		itemCredit = new JMenuItem(new AbstractAction("Credit"){
			@Override
			public void actionPerformed(ActionEvent e) {
				Credit cred = new Credit();
			}
		});
		itemCredit.setIcon(new ImageIcon("images/credit.png"));
		menuCredit.add(itemCredit);
		
	}
	
	
}
	
