import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

public class Toolbar extends JMenuBar {
	JMenu menuFichier, submenu, menuAffiche, menuCredit ;
	JMenuItem itemImport , itemSave,itemQuit,itemCredit, menuItem2, menuItem3, menuItem4;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;
	ZoneTracage panelDessin;

	public Toolbar(ZoneTracage trace){
		this.panelDessin = trace; //On récupère une référence vers notre ZoneTracage pour pouvoir créer des objets optiques et mettre à jour la zone de dessin

		//On créé le menu Fichier
		menuFichier = new JMenu("Fichier");
		this.add(menuFichier);

		//Item importer, on crée un fileChooser qui permet d'importer un ficher et on utilise le filtre OpticusFilter
		itemImport = new JMenuItem(new AbstractAction("Importer/Charger un fichier"){
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Importation demandee");
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new OpticusFilter()); //On définie le filtre à utiliser
    			int returnVal = fc.showOpenDialog(Toolbar.this); //On ouvre la fenêtre de dialogue
    			if(returnVal == JFileChooser.APPROVE_OPTION) {
       				//System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
					String path = fc.getSelectedFile().getPath(); //Si l'utilisateur valide on récupère le fichier sélectionné
					//System.out.println(path);

					try {
						FileInputStream fileStream = new FileInputStream(path); //On ouvre le fichier selectionné
						BufferedInputStream bf = new BufferedInputStream(fileStream);
						ObjectInputStream object = new ObjectInputStream(bf); //On lit le fichier et on le deserialize
						panelDessin.setListObjetOptique((ArrayList<ObjetOptique>)(object.readObject()));  //On charge notre liste d'objets optiques comme la nouvelle liste
						//System.out.println("Objet chargé : ");

						object.close(); //On ferme le fichier

					} catch (Exception i) {
						i.printStackTrace();
					}

    			}
			}
		});
		itemImport.setIcon(new ImageIcon("images/import.png"));
		menuFichier.add(itemImport);

		//Item sauvegarder
		itemSave = new JMenuItem(new AbstractAction("Sauvegarder"){
			@Override
			public void actionPerformed(ActionEvent event) { //De même que pour le chargement de fichier mais ici on sauvegarde
				//System.out.println("Importation demandee");
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new OpticusFilter());
				int returnVal = fc.showSaveDialog(Toolbar.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					//System.out.println("You chose to save to this file: " + fc.getSelectedFile().getPath());
					String path = fc.getSelectedFile().getPath();
					//System.out.println(path);

					try {
						File file = new File((String)path+"."+OpticusFilter.EXTENSION);
						FileOutputStream fileOut = new FileOutputStream(file);
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(panelDessin.getListObjetOptique()); //On écrit nos objets serializés dans le fichier .opticus
						out.close();
						fileOut.close();
						//System.out.printf("Serialized ! ");
					} catch (IOException i) {
						i.printStackTrace();
					}
				}
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
				System.exit(0); //On quitte le programme
			}
		});
		itemQuit.setIcon(new ImageIcon("images/exit.png"));
		menuFichier.add(itemQuit);

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
