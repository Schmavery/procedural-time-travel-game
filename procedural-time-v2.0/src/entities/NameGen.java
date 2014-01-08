package entities;

import java.util.Random;

import entities.Humanoid.Gender;

public class NameGen {
	private static String[] mPreNames = {"Bob", "Joe", "Jor", "Big", "Wil", "A'y", "Hag", "Fly", "Lar", "Jim", "Bel", "Bre", "Lee"};
	private static String[] fPreNames = {"Jor", "Big", "A'y", "Fly", "Cat", "Bel", "Bre", "Lee"};
	private static String[] mEndNames = {"bo", "ook", "bok", "ose", "arm", "rod", "son", "roy", "ray", "buk", "eep", "lob", "orn", "bob"};
	private static String[] fEndNames = {"'la", "la", "ly", "bell", "lyn", "ane"};
	private static String[] occupations = {"Friar", "Sailor", "Buccaneer",	"Dandy", "Raider", "Bandit", "Duelist", "Craftsman", "Burglar", "Dancer", "Scout"};
	private static Random rand = new Random();
	
	public static String genName(Gender g){
		switch (g) {
		case MALE:
			return mPreNames[rand.nextInt(mPreNames.length)] + mEndNames[rand.nextInt(mEndNames.length)];
		case FEMALE:
			return fPreNames[rand.nextInt(fPreNames.length)] + fEndNames[rand.nextInt(fEndNames.length)];
		default:
			return "";
		}
	}
	
	public static String genNameOccupation(Gender g){
		switch (g) {
		case MALE:
			return mPreNames[rand.nextInt(mPreNames.length)] + mEndNames[rand.nextInt(mEndNames.length)] + " the " + occupations[rand.nextInt(occupations.length)];
		case FEMALE:
			return fPreNames[rand.nextInt(fPreNames.length)] + fEndNames[rand.nextInt(fEndNames.length)] + " the " + occupations[rand.nextInt(occupations.length)];
		default:
			return "";
		}
	}
}
