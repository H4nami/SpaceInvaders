package Juego;



//esta es la identidad para el disparo
public class Disparos extends Objeto {
	
	
	
	
	
	// La velocidad vertical del tiro
	private double moveSpeed = -300;
	
	//Donde se lleva a cabo la accion
	private Game game;
	
	
	//verdadero si ya ha disparado a algo
	private boolean used = false;
	
	//para crear un nuevo disparo
	public Disparos(Game game,String sprite,int x,int y) {
		super(sprite,x,y);
		this.game = game;
		
		dy = moveSpeed;
	}

	
	
	
	
	//peticion del tiempo transcurrido para el disparo
	public void move(long delta) {
		super.move(delta);
		
		if (y < -100) {
			game.removeEntity(this);
		}
	}
	
	
	
	
	
	//Notificacion de colision
	public void collidedWith(Objeto other) {
		// Esto es para prevenir que si ya matamos a un alien, muera el siguiente
		if (used) {
			return;
		}
		
		// si le dimos a un alien, que se muera :P
		if (other instanceof Aliens) {
			// quitar los aliens muertos
			game.removeEntity(this);
			game.removeEntity(other);
			
			// notificar que el alien ha muerto
			game.notifyAlienKilled();
			used = true;
		}
	}
}