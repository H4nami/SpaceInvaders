package Juego;


//identidad para la nave del jugador
public class Nave extends Objeto {
	//el entorno
	private Game game;
	
	// se crea la identidad, la posicion, etc
	public Nave(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game;
	}
	
	
	
	
	
	
	public void move(long delta) {
		
		//limites del movimiento, es decir los bordes
		if ((dx < 0) && (x < 10)) {
			return;
		}
		if ((dx > 0) && (x > 750)) {
			return;
		}
		
		super.move(delta);
	}
	
	
	
	
	
	//notificador de colision
	public void collidedWith(Objeto other) {
		if (other instanceof Aliens) {
			game.notifyDeath();
		}
	}
}