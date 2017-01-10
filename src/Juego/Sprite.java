package Juego;


import java.awt.Graphics;
import java.awt.Image;

//un srpite es solo una imagen que desplegamos 
public class Sprite {
	//la iamgen que va a ser usada
	private Image image;
	
	// se crea un nuevo sprite basado en una imagen
	public Sprite(Image image) {
		this.image = image;
	}
	
	
	
	
	//se obtienen las medidas
	public int getWidth() {
		return image.getWidth(null);
	}
	public int getHeight() {
		return image.getHeight(null);
	}
	
	
	
	
	
	//se dibuja el sprite en el contexto grafico
	public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,null);
	}
}