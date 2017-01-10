package Juego;

//identidad del alien
public class Aliens extends Objeto {
	
	//velocidad horizontal
	private double moveSpeed = 75;
	// el entorno en el que existe
	private Game game;
	
	/**
	 * Crear un nuevo alien
	 * se necesita saber donde se va a colocar
	 * es decir la posicon 
	 */
	public Aliens(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game;
		dx = -moveSpeed;
	}

	
	
	
	/**
	 * EL alien se movera en base al tiempo transcurrido
	 * 
	 * @param delta es el tiempo que ha transucrrido
	 */
	public void move(long delta) {
		// si se ha llegado al limite de la izquierda se llama a la logica apra el cambio de movimient
		if ((dx < 0) && (x < 10)) {
			game.updateLogic();
		}
		//lo mismo pero al otro lado :P
		if ((dx > 0) && (x > 750)) {
			game.updateLogic();
		}
		
		// proceder cone l movimiento normal
		super.move(delta);
	}
	
	
	
	

	public void doLogic() {
		dx = -dx;
		y += 10;
		
		// si se llego al tope inferior entonces el jugador pierde
		if (y > 570) {
			game.notifyDeath();
		}
	}
	
	
	
	
	
	/**
	 * Notificacion de colision
	 */
	public void collidedWith(Objeto other) {
		//no recuerdo donde colque esto :$
	}
}