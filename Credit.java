import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.Timer;
import java.awt.event.*;


/**
 * Classe qui créée et gère la page des crédits
 * @author Antoine BAILLET
 */
public class Credit extends JFrame implements ActionListener{
	private final ImageIcon magicus = new ImageIcon(Credit.class.getResource("images/magicus.gif"));
	private JButton quitter = new JButton ("Revenir à Opticus Magicus !");
	private JButton scrollHaut = new JButton("Haut");
	private JButton scrollBas = new JButton("Bas");
	private Boolean versLeBas;
	private JScrollPane scroll;
	private JTextArea textArea;
	private int i = 0;
	private boolean running = false;
		
	/**
	 * Constructeur qui génère la page de crédits
	*/
	public Credit(){
		
		this.setTitle("Cr\u00e9dit");  // on paramètre la fenêtre
		this.setSize(720, 580);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		JPanel container = new JPanel(); // déclaration des différents panel
		JPanel titre = new JPanel();
		JPanel ligne1 = new JPanel();
		JPanel ligne2 = new JPanel();
		
		
		scrollHaut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				versLeBas = false;
				if(!running){
					scroll();
				}
			}
		});
		
		scrollBas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				versLeBas = true;
				if(!running){
					scroll();
				}
			}
		});
				
		
		textArea = new JTextArea(
			"C'est avant tout l'histoire d'un projet qui a mal tourné... Un soir de pleine lune après la fameuse journée Premier Cycle où les \" Qu'est-ce que se passe ? \" reignaient à tout va, un groupe d'étudiants venait de trouver une idée fabuleuse pour son projet d'informatique. Les jeux n'étant pas tolérées, ils avaient alors cru choisir un sujet original... Il n'en était rien, après de brèves discussions avec les autres groupes de la classe, la magie noire que constitue l'optique géométrique avait été l'élue de nombreux étudiants qui vouaient décidément un culte profond pour ce domaine dans lequel peu brillaient notamment lors de la démonstration de la relation de Newton (cf. partiel 1er semestre 1ère année). Cette histoire se poursuit donc avec des nuits à plancher sur les rayons incidents et ses intersections capricieuses à travers des Line2D capricieuses. Java est-il fait pour l'optique géométrique ? Nul ne saurait le dire mais ce que ces braves étudiants peuvent affirmer c'est que dans ce projet d'info, ils auront fait (compris ?) plus de maths que pendant la dernière IE. Au final, le \".magicus\" est né, une extension de fichier qui envahira bientôt les ordinateurs du monde entier. Enrichi des meilleurs tableaux d'objets optiques dénichés aux 4 coins du monde, il saura ravir vos désirs de vergence et de plan focaux !");
		//taille=1092
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true); // creation du texte et de la mise en page
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Serif", Font.ITALIC, 30));
		
		
		scroll = new JScrollPane(textArea); // on l'inclue dans un scrolleur pour défiler le texte
		
		
		JLabel image = new JLabel();
		/*JLabel content = new JLabel();*/
		
		image.setIcon(magicus);
		Animation anim = new Animation(image, "Bienvenue dans la page Cr\u00e9dit !!", 32);
        anim.start();
	
		
		titre.setLayout(new BoxLayout(titre, BoxLayout.LINE_AXIS));
		titre.add(image);
		
		ligne1.setLayout(new BoxLayout(ligne1, BoxLayout.LINE_AXIS));
		ligne1.add(scrollHaut);
		ligne1.add(scrollBas);
		
		ligne2.setLayout(new BoxLayout(ligne2, BoxLayout.LINE_AXIS));
		ligne2.add(quitter);
		
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		
		container.add(titre);
		container.add(scroll);
		container.add(ligne1);
		container.add(ligne2);
		
		this.getContentPane().add(container);
		
		quitter.addActionListener(this);

		this.setVisible(true);
		
		scroll.getVerticalScrollBar().setUnitIncrement(1);
	}
	
	public void actionPerformed(ActionEvent evt) {
		//On récupère le bouton source de l'event
		JButton source = (JButton) evt.getSource();
		
		if(source == quitter){ 
			
			//On désaffiche les crédits
			this.dispose();
		}
	}
	
	/**
	 * Méthode qui permet de mettre le programme en pause durant une certaines durée
	 * @param ms Entier correspondant au temps en millisecond pendant lequel le programme sera en pause
	 */
	public void pause(int ms){
		try{//On essaye de mettre le thread en pause
			Thread.sleep(ms);
		}catch(InterruptedException e){}
	}
	
	/**
	 * Méthode qui permet le scroll automatique du text 
	 */
	public void scroll(){
		//On met un delai de 30 ms pour le scroll-auto
		int timerDelay = 30;
		
		running = true;
		
		//On créer un timer qui permet le scroll
		new Timer(timerDelay, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				//On place le curseur de la scroll bar à l'indice i et on incrémente
				scroll.getVerticalScrollBar().setValue(i);
				
				
				if(versLeBas){
					i++;
				}else{
					i--;
				}
				
				if(i>=scroll.getVerticalScrollBar().getHeight()*2.4 || i<=0){
					running = false;
					((Timer)e.getSource()).stop();
				}
				
			}
		}).start();
	}
}

class Animation implements ActionListener { //classe dédiée à l'animation du JLabel

    private static final int RATE = 12; //vitesse de l'animation
    private final Timer timer = new Timer(1000 / RATE, this);
    private final JLabel label;
    private final String s;
    private final int n;
    private int index;

    public Animation(JLabel label, String s, int n) {
		
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.label = label;
        this.s = sb + s + sb;
        this.n = n;
        label.setFont(new Font("Serif", Font.ITALIC, 36));
        label.setText(sb.toString());
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        index++;
        if (index > s.length() - n) {
            index = 0;
		}
        label.setText(s.substring(index, index + n));
	}
}

