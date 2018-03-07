import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.Font;
import javax.swing.Timer;
import java.awt.EventQueue;	
import java.awt.event.*;


/**
 * Classe qui créée et gère la page des crédits
 * @author Antoine BAILLET
 */
public class Credit extends JFrame implements ActionListener{
	private final ImageIcon yoshi = new ImageIcon(Credit.class.getResource("images/vega.gif"));
	private JButton quitter = new JButton ("Revenir au menu");
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
		JPanel ligne3 = new JPanel();
		
		
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
				
		
		textArea = new JTextArea("	texte a ecrire ^^...................................Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ");
		//taille=1092
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true); // creation du texte et de la mise en page
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Serif", Font.ITALIC, 30));
		
		
		scroll = new JScrollPane(textArea); // on l'inclue dans un scrolleur pour défiler le texte
		
		
		JLabel image = new JLabel();
		/*JLabel content = new JLabel();*/
		
		image.setIcon(yoshi);
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

