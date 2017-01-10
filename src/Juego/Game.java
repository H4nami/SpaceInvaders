package Juego;

import Audio.AudioPlayer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game extends Canvas {
	private static final long serialVersionUID = 1L;
	
	
	/** Nos pemite la aceleacion y hace el flipping*/
	private BufferStrategy strategy;
	
	
	/** Nos da verdadero si el juego esta corrriendo actualmente*/
	private boolean gameRunning = true;
	
	
	/** Lista de las entidades que existen en el juego*/
	private ArrayList<Objeto> entities = new ArrayList<Objeto>();
	
	/** La lista de las entidades que se van quitando o destruyedo en el juego */
	private ArrayList<Objeto> removeList = new ArrayList<Objeto>();
	
	/** La entidad que representa la nave del jugador*/
	private Objeto ship;
	
	
	/** La velocidad a la que se movera la nave (pixeles/segundo) */
	private double moveSpeed = 350;
	
	
	/** La ultima vez que se disparo (tiempo)*/
	private long lastFire = 0;
	
	
	/** EL intervalo de tiempo necesario entre cada shot*/
	private long firingInterval = 420;
	
	
	/** contador para los aliens*/
	private int alienCount;
	
	/** Para imprimir algun mensaje */
	private String message = "";
	
	/** Verdadero si estamos esperando para que presionen una tecla */
	private boolean waitingForKeyPress = true;
	
	/** verdadero cuando pulsamos la tecla de la izquiera */
	private boolean leftPressed = false;
	
	/** Lo mismo que el de arriba pero la tecla derecha */
	private boolean rightPressed = false;
	
	/** Ahm verdadero si se esta disparando con el space */
	private boolean firePressed = false;
	
	//cuando sucede un evento y se necesita aplicar la logica del juego 
	private boolean logicRequiredThisLoop = false;
	private HashMap<String,AudioPlayer> sfx; 
	
	
	
	
	/**
	 * construir el juego y correrlo
	 */
	public Game() {
				//la musica!
				AudioPlayer bgMusic; 
				bgMusic = new AudioPlayer("/dafunk.mp3");
				bgMusic.play();
				
				//efectos
				sfx = new HashMap<String,AudioPlayer>();
				sfx.put("disparo", new AudioPlayer("/disparo.wav"));
				sfx.put("muerte",new AudioPlayer("/muerte.wav"));
				sfx.put("perdio", new AudioPlayer("/perdio.wav"));
				sfx.put("gano", new AudioPlayer("/gano.wav"));
		
		// se crea el frame de nuestro juego
		JFrame container = new JFrame("Invasores espaciales :P");
		
		
		// se agregan paneles se setea la resolucion etc
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);
		
		
		// como usaremos un canvas, necesita hacerse el setup
		setBounds(0,0,800,600);
		panel.add(this);
		
		// Decirle al awt que no redibuje el canvas ya que nosotros nos encargaremos de ello
		setIgnoreRepaint(true);
		
		// poner visible la pantalla
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// agregamos el listener para cerrar la pantalla
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// agregar un sistema de deteccion de entradas al canvas
		// para asi poder dar respuesta a ello
		addKeyListener(new KeyInputHandler());
		
		// ponerlo disponible para poder detectar las entradas
		requestFocus();

		// crearemos un buffer que dejara al awt
		// para que nos permita acelerar los graficos
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// inicializar las identidades para que asi aparezcan al inicio
		initEntities();
	}
	
	
	
	
	
	/**
	 * Empezar un nuevo juego, esto deberia borrar lo anterior y comenzar uno nuevo
	 */
	private void startGame() {
		// borrar las identidades anteriores y crear un nuevo set
		entities.clear();
		initEntities();
		
		
		// poner en false todas las entradas que se esten presentado en ese momento
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	
	
	
	
	/**
	 * Inicializar el estado de las identidades y colocarlas
	 */
	private void initEntities() {
		// creamos la nave y la colocamos al fondo de la pantalla
		ship = new Nave(this,"sprites/ship.gif",370,550);
		entities.add(ship);
		
		// Aqui colocamos una matriz de aliens
		alienCount = 0;
		for (int row=0;row<5;row++) {
			for (int x=0;x<12;x++) {
				Objeto alien = new Aliens(this,"sprites/alien.gif",100+(x*50),(50)+row*30);
				entities.add(alien);
				alienCount++;
			}
		}
	}
	
	
	
	
	
	/**
	 * CUando ocurre un evento tenemos que indicar que corra la logica del juego (las reglas)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	
	
	
	
	/**
	 * Aqui quitamos una entidad del juego (cuando la matamos
	 * @param objeto The entity that should be removed
	 */
	public void removeEntity(Objeto objeto) {
		removeList.add(objeto);
	}
	
	
	
	
	
	/**
	 * Notifica que el jugador ha perdido
	 */
	public void notifyDeath() {
		sfx.get("perdio").play();
		message = "Oh no! Te tienen, quieres probar otra vez?";
		waitingForKeyPress = true;
		
	}
	
	
	
	
	
	/**
	 * Notifica que el jugador gano
	 */
	public void notifyWin() {
		sfx.get("gano").play();
		message = "Bien hecho, ganaste!";
		waitingForKeyPress = true;
	}
	
	
	
	
	
	/**
	 * Notifica que un alien ha muerto
	 */
	public void notifyAlienKilled() {
		// reduce el contador de los aliens para saber si aun hay vivos
		alienCount--;
		sfx.get("muerte").play();
		
		if (alienCount == 0) {
			notifyWin();
		}
		
		// si aun quedan aliens vivos, la velocidad aumentara (pixel/segundo)
		for (int i=0;i<entities.size();i++) {
			Objeto objeto = entities.get(i);
			
			if (objeto instanceof Aliens) {
				
				objeto.setHorizontalMovement(objeto.getHorizontalMovement() * 1.045);
			}
		}
	}
	
	
	
	
	
	//Aqui checaemos si ya paso el tiempo ente cada dispao paa que pueda ealiza uno nuevo
	public void tryToFire() {
		
		
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// si se cumple la condicion debe contar otra vez el tiempo que pasa
		lastFire = System.currentTimeMillis();
		Disparos shot = new Disparos(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
		entities.add(shot);
		sfx.get("disparo").play();
	}
	
	
	
	
	
	/**
	 * Esta es la parte principal del juego que correra todo el tiempo y se encarga de lo siguiente
	 * <p>
	 * - la velocidad de todos los objetos
	 * - Mover las entidades
	 * - Dibujar los objetos
	 * - los eventos del juego
	 * - Checar las entradas
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		
		// creara loops hasta que el juego se detenga
		while (gameRunning) {
			// calcula cuanto tiempo ha pasado
			// sera usado apra saber cuanto se deben mover los aliens
			// movera el loop
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// En un contexto grafico se llamara asi mismo para recrearlo
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor( new Color(0x09, 0x0D, 0x1F) );
			g.fillRect(0,0,800,600);
			
			if (!waitingForKeyPress) {
				for (int i=0;i<entities.size();i++) {
					Objeto objeto = entities.get(i);
					
					objeto.move(delta);
				}
			}
			
			// ciclar el dibujado de identidades
			for (int i=0;i<entities.size();i++) {
				Objeto objeto = entities.get(i);
				
				objeto.draw(g);
			}
			
			// Comparar cada identidad con la otra para saber si hay colisiones
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Objeto me = entities.get(p);
					Objeto him = entities.get(s);
					
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
			
			// remover cada alien que ha sido atacado
			entities.removeAll(removeList);
			removeList.clear();

			if (logicRequiredThisLoop) {
				for (int i=0;i<entities.size();i++) {
					Objeto objeto = entities.get(i);
					objeto.doLogic();
				}
				
				logicRequiredThisLoop = false;
			}
			
			
			// mensaje de espera
			if (waitingForKeyPress) {
				g.setColor(Color.CYAN);
				g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				g.drawString("Presiona cualquier tecla",(800-g.getFontMetrics().stringWidth("Presiona cualquier tecla"))/2,300);
			}
			
			// cuando se ha compeltado el dibujo, se debe "flipear" para que vuelva a empezar el proceso
			g.dispose();
			strategy.show();
			
			// Se encarga del movimiento de la nave
			ship.setHorizontalMovement(0);
			
			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);
			}
			
			// Cuando presionemos la tecla de espacio
			if (firePressed) {
				tryToFire();
			}
			
			try { Thread.sleep(10); } catch (Exception e) {}
		}
	}
	
	
	
	
	
	//esta es la clase encargada de los eventos del teclado
	private class KeyInputHandler extends KeyAdapter {
		
		//EL numero de teclas necesarias para empezar
		private int pressCount = 1;
		
	
		public void keyPressed(KeyEvent e) {
			// especificar que necesitamos cualquier tecla presionada
			if (waitingForKeyPress) {
				return;
			}
			
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
		} 
		
		/**
		 * notificacion del awt que la tecla ha sido preisonada
		 *
		 */
		public void keyReleased(KeyEvent e) {
			// espeficar que no queremos tomar el evento release
			if (waitingForKeyPress) {
				return;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
		}

		/**
		 * Notificacion del awt de que una tecla ha sido presionada y luego soltada
		 *
		 */
		public void keyTyped(KeyEvent e) {
		
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}
			
			// si presionamos escape el juego se cerrara
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	
	
	
	
	
	/**
	 * solamente crearemos una instancia que se encargara de todo el proceso de ejecucion del juego
	 */
	public static void main(String argv[]) {
		
				
		Game g =new Game();
		g.gameLoop();
	}
}
