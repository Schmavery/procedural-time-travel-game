package editor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;

public class ImageTools {
	
	
	public Image[][] loadSprites(Image spriteSheet, int spriteSize, int scale){
	return loadSprites(spriteSheet, spriteSize, 0, 0, scale);
	}

	public Image[][] loadSprites(Image spriteSheet, int spriteSize, int width, int height, int scale){
		spriteSize = spriteSize/scale;
		
		if (width==0 || height==0){
			width = spriteSheet.getWidth(null)/(spriteSize + 1);
			height = spriteSheet.getHeight(null)/(spriteSize + 1);
		}
		
		Image[][] imgArray = new Image[width][height];
		
		BufferedImage spriteSheet2 = imageToBufferedImage(spriteSheet, spriteSheet.getWidth(null), spriteSheet.getHeight(null));
		
		for (int i = 0; i < width;i++){
			for (int j = 0; j < height; j++){
				BufferedImage temp = spriteSheet2.getSubimage(i*(spriteSize + 1) + 1, j*(spriteSize + 1) + 1, spriteSize, spriteSize);
				imgArray[i][j] = (Image) temp.getScaledInstance(spriteSize*scale, spriteSize*scale, 0);
			}
		}
		
		
		
		return imgArray;
	}
	
	private BufferedImage imageToBufferedImage(Image image, int width, int height)
	  {
	    BufferedImage dest = new BufferedImage(
	        width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.dispose();
	    return dest;
	  }
	
	public Image loadImage(String path){
		return new ImageIcon(getClass().getResource("../resources/images/" + path)).getImage();
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
