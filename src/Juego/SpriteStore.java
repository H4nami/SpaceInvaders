package Juego;


import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

//es un gesto de recursos para poder enlazar las iamgenes desde la caprte con el juego
public class SpriteStore {
	//una instancia de nuestra clase
	private static SpriteStore single = new SpriteStore();
	
	//se llama a esta clase
	public static SpriteStore get() {
		return single;
	}
	
	
	// se crea un sprite en la cache
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	
	
	
	
	// se obtiene un sprite de los que ya estan guardados
	public Sprite getSprite(String ref) {
		// si ya existe uno en la cache pues simplemente se usa ese
		if (sprites.get(ref) != null) {
			return sprites.get(ref);
		}
		
		// de otra manera se tiene que ir y cargarlo desde el resource loader
		BufferedImage sourceImage = null;
		
		try {
			//aqui se podria hacer directamente la ruta de la carpeta pero pues para que sea masdinamico asi
			URL url = this.getClass().getClassLoader().getResource(ref);
			
			if (url == null) {
				fail("Can't find ref: "+ref);
			}
			
			// se usa imageIO para la lectura de la iamgen
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: "+ref);
		}
		
		// Se crea una iamgen acelerada de nuestro archivo y es almacenado en la cache
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// se dibuja el archivo en una iamgen acelerada
		image.getGraphics().drawImage(sourceImage,0,0,null);
		
		// se crea el sprite se mete a la cache y es retornado
		Sprite sprite = new Sprite(image);
		sprites.put(ref,sprite);
		
		return sprite;
	}
	
	
	
	
	
	//un metodo de utilidad por si algo falla
	private void fail(String message) {
		// Pues imprimimos un mensaje que no se encontraron los recursos y se cierra el juego
		System.err.println(message);
		System.exit(0);
	}
}