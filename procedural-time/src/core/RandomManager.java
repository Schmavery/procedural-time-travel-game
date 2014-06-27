package core;

import java.util.Random;

public class RandomManager {
	
	private static final int numSeeds = 100;
	
	private static int masterSeed;
	private static Random rand;
	private static int[] seeder;
	
	public static void init(int seed){
		masterSeed = seed;
		rand = new Random(seed);
		seeder = new int[numSeeds];
		
		for (int i = 0; i < numSeeds; i++){
			seeder[i] = rand.nextInt();
		}
	}
	
	/**
	 * Returns a random (but reproducible) seed corresponding to an identifier
	 * @param id Identifier
	 * @return random seed
	 */
	public static int getSeed(int id){
		return seeder[id % numSeeds];
	}
	
	public static int getMasterSeed(){ return masterSeed;}
}
