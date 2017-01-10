package Juego;


import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Una entidad representa cualquier elemento que aparece en el juego
 * por ejemplo aliens, disparos, la nave, etc.
 * Notese que los doubles son usados para las posiciones y puede sonar extra;o
 * pero se debe a que si usamos doubles en lugar de enteros, las entidades
 * pueden moverse en fracciones de pixeles, sin que se corte la imagen
 */



public abstract class Objeto {
	//La localizacion actual
	protected double x;
	protected double y;
	
	//lo que representa la entidad
	protected Sprite sprite;
	
	//la velodiad horizontal
	protected double dx;
	
	//velocidad vertical
	protected double dy;
	
	// EL rectangulo virtual usado para checar la colision
	private Rectangle me = new Rectangle();
	
	private Rectangle him = new Rectangle();
	
	/**
	 * Construir la identidad basandonos en una iamgen
	 */
	public Objeto(String ref,int x,int y) {
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	
	
	
	
	//Hace la peticion apa que la imagen se mueva
	public void move(long delta) {
		// update the location of the entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	
	
	//setea la velocidad hoizontal
	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}

	
	
	//setea la velocidad vetical
	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}
	
	
	//consegui velocidad hoziontal
	public double getHorizontalMovement() {
		return dx;
	}

	//velocidad vetical
	public double getVerticalMovement() {
		return dy;
	}
	
	
	
	
	
	//dibujar la identidad en el contexto grafico
	public void draw(Graphics g) {
		sprite.draw(g,(int) x,(int) y);
	}
	
	//relizar la logica
	public void doLogic() {
	}
	

	//conseugir la posicion
	public int getX() {
		return (int) x;
	}

	
	public int getY() {
		return (int) y;
	}
	
	
	
	
	//checar si ha colisionado con alguna otra identidad
	public boolean collidesWith(Objeto other) {
		me.setBounds((int) x,(int) y,sprite.getWidth()-1,sprite.getHeight()-1);
		him.setBounds((int) other.x,(int) other.y,other.sprite.getWidth()-1,other.sprite.getHeight()-1);

		return me.intersects(him);
	}
	
	
	
	//notificar de la colision
	public abstract void collidedWith(Objeto other);
}
