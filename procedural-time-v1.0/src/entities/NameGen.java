package entities;

import java.util.Random;

public class NameGen {
	private static String[] preNames = {"Bob", "Joe", "Jor", "Big", "Wil", "A'y", "Hag", "Fly", "Lar", "Cat", "Jim", "Bel", "Bre", "Lee"};
	private static String[] endNames = {"'la", "ook", "bok", "ose", "arm", "rod", "son", "roy", "ray", "buk", "bel", "eep", "lob", "orn"};
	private static String[] occupations = {"Friar", "Sailor", "Buccaneer",	"Dandy", "Raider", "Bandit", "Duelist", "Craftsman", "Burglar", "Dancer", "Scout"};
	private static Random rand = new Random();
	
	public static String gen(){
		return preNames[rand.nextInt(preNames.length)] + endNames[rand.nextInt(endNames.length)] + " the " + occupations[rand.nextInt(occupations.length)];
	}
}
