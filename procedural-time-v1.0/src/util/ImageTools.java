package util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;

public class ImageTools {
	
	private BufferedImage toCompatibleImage(BufferedImage image)
	{
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image; 
	}
	
	/*
	public BufferedImage[][] loadSprites(Image spriteSheet, int spriteSize, int scale){
		return loadSprites(spriteSheet, spriteSize, 0, 0, scale);
	}
	*/
	public BufferedImage[][] loadBuffSprites(Image spriteSheet, int spriteSize, int scale){
		return loadBuffSprites(spriteSheet, spriteSize, 0, 0, scale);
	}

	public BufferedImage[][] loadBuffSprites(Image spriteSheet, int spriteSize, int width, int height, int scale){
		spriteSize = spriteSize/scale;
		
		if (width==0 || height==0){
			width = spriteSheet.getWidth(null)/(spriteSize + 1);
			height = spriteSheet.getHeight(null)/(spriteSize + 1);
		}
		
		BufferedImage[][] imgArray = new BufferedImage[width][height];
		
		BufferedImage spriteSheet2 = imageToBufferedImage(spriteSheet, spriteSheet.getWidth(null), spriteSheet.getHeight(null));
		
		for (int i = 0; i < width;i++){
			for (int j = 0; j < height; j++){
				BufferedImage temp = spriteSheet2.getSubimage(i*(spriteSize + 1) + 1, j*(spriteSize + 1) + 1, spriteSize, spriteSize);
				imgArray[i][j] = imageToBufferedImage(temp.getScaledInstance(spriteSize*scale, spriteSize*scale, 0), spriteSize*scale,spriteSize*scale);
			}
		}
		return imgArray;
	}

	/*
	public BufferedImage[][] loadSprites (Image spriteSheet, int spriteSize, int width, int height, int scale){
		BufferedImage[][] buffImgArray = loadBuffSprites(spriteSheet, spriteSize, width, height, scale);
		Image[][] imgArray = new Image[width][height];
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				imgArray[i][j] = (Image) buffImgArray[i][j];
			}
		}
		return imgArray;
	}*/
	
	public BufferedImage imageToBufferedImage(Image image, int width, int height)
	  {
	    BufferedImage dest = new BufferedImage(
	        width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.dispose();
	    return dest;
	  }
	
	public BufferedImage loadImage(String path){
		Image img = new ImageIcon(getClass().getResource("../resources/images/" + path)).getImage();
		BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		buffImg.getGraphics().drawImage(img, 0, 0, null);
		buffImg.getGraphics().dispose();
		//return toCompatibleImage(buffImg);
		return buffImg;
	}
	
	public Image pinkToAlpha(Image image){
		return pinkToAlpha(imageToBufferedImage(image, image.getWidth(null), image.getHeight(null)));
	}
	
	public Image pinkToAlpha(BufferedImage image){
		//int color = image.getRGB(0, 0);
		//image.get
	    ImageFilter filter = new RGBImageFilter()
	    {
	      public final int filterRGB(int x, int y, int rgb)
	      {
	        return (rgb << 8) & 0xFF00FF;
	      }
	    };

	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	  }
}
