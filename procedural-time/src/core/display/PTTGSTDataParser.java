package core.display;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

/**
 * Parses data for spritesheets according to the 
 * pttg-sprite-tool data format, found on:
 * https://github.com/Schmavery/pttg-sprite-tool
 */
public final class PTTGSTDataParser {
	private enum ParserState {DEFAULT, ANCHOR, HOOKS, BOUNDS, COLLISION};
	public static void load(SpriteSheet ss, SpriteManager sm){
		String[] data = ss.readData().split("\n");
		StringBuilder sb = new StringBuilder();
		
		for (String str : data){
			if (str.startsWith("img")){
				sb.setLength(0);
				sb.append(str+"\n");
			} else if (str.startsWith("anim")){
				sb.setLength(0);
				sb.append(str+"\n");
			} else if (str.startsWith("endimg")){
				Image img = new Image(ss);
				loadImage(sb.toString(), img);
				sm.addImage(img);
			} else if (str.startsWith("endanim")){
				Animation2 anim = new Animation2(ss);
				loadAnim(sb.toString(), anim, sm);
				sm.addAnim(anim);
			} else {
				sb.append(str+"\n");
			}
		}
	}
	
	public static void loadImage(String data, Image img){
		ParserState state = ParserState.DEFAULT;
		SpriteHook tmpHook = null;
		Rectangle rect = new Rectangle();
		Point pt;
		
		
		for (String l : data.split("\n")){
			switch (state){
			case ANCHOR:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					img.setAnchor(parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1)));
					state = ParserState.DEFAULT;
				}
				break;
			case HOOKS:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					tmpHook = new SpriteHook("", pt);
				} else if (l.matches("name \\[\\[.+\\]\\]") && tmpHook != null){
					String name = l.substring(l.indexOf("[[")+2, l.indexOf("]]"));
					tmpHook.setName(name);
					img.addHook(tmpHook);
				} else if (l.startsWith("endhooks")){
					state = ParserState.DEFAULT;
				}
				break;
			case BOUNDS:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					rect.setLocation(pt);
				} else if (l.matches("dim +\\([0-9]+, ?[0-9]+\\)")){
					// Parse width and height as a point for code reuse ;)
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					rect.setSize(pt.getX(), pt.getY());
					img.setBounds(rect);
					state = ParserState.DEFAULT;
				}
				break;
			case COLLISION:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					img.getPoly().addPoint(pt.getX(), pt.getY());
				} else if (l.startsWith("endcollision")){
					state = ParserState.DEFAULT;
				}
				break;
			case DEFAULT:
				if (l.startsWith("anchor")){
					state = ParserState.ANCHOR;
				} else if (l.startsWith("hooks")){
					state = ParserState.HOOKS;
				} else if (l.startsWith("img")){
					state = ParserState.BOUNDS;
					rect = new Rectangle();
				} else if (l.startsWith("collision")){
					state = ParserState.COLLISION;
				} else if (l.startsWith("img ")){
					String idStr = l.substring(4);
					if (idStr.matches("\\d+")){
						img.setId(Integer.valueOf(idStr));
					}
				}
				break;
			}
			
		}
	}
	
	public static void loadAnim(String data, Animation2 anim, SpriteManager sm){
		// Parsed Image Data
		for (String l : data.split("\n")){
			if (l.startsWith("anim ")){
				anim.setName(l.substring(l.indexOf("[[")+2, l.indexOf("]]")));
			} else if (l.startsWith("pause ")){
				anim.setPause(Integer.valueOf(l.replaceAll("[^\\d]", "")));
			} else if (l.startsWith("frame ")){
				int frameId = Integer.valueOf(l.replaceAll("[^\\d]", ""));
				anim.addFrame(sm.getImage(anim.getSpriteSheet(), frameId));
			}
		}
	}
	
	/**
	 * Accepts a String of form "(x,y)" and translates it
	 * a point with corresponding x and y values.  There is
	 * a reasonable amount of flexibility in the string format.
	 * The only requirement is that the coords be comma-separated.
	 * @param str String describing a point.
	 * @return Point corresponding to the input String.
	 */
	private static Point parsePoint(String str){
 		String xStr = str.substring(0, str.indexOf(","));
		String yStr = str.substring(str.indexOf(","));
		// Strip non-numeric chars
		xStr = xStr.replaceAll("[^\\d]", "");
		yStr = yStr.replaceAll("[^\\d]", "");
		Point pt = null;
		try {
			pt = new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));
		} catch (NumberFormatException e){
			System.out.println("Invalid point parsed: "+ str);
		}
		return pt;
	}
}
