package core.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GraphWriter {
	public static enum GraphType {CLUSTER, WORK_QOL, SOCIAL_QOL};
	public static StringBuilder[] outputs = new StringBuilder[GraphType.values().length];
	public static long totalTime = 0;
	
	private GraphWriter(){/* singleton */};
	
	
	public static void resetTime(){
		totalTime = 0;
	}
	
	/**
	 * Add the tick's deltaTime to the GraphWriter's internal counter.
	 * @param deltaTime
	 */
	public static void update(long deltaTime){
		totalTime += deltaTime;
	}
	
	/**
	 * Log an entry 'str' at the current timestep for type 'type'.
	 * @param type
	 * @param str
	 */
	public static void log(GraphType type, String str){
		if (outputs[type.ordinal()] == null){
			outputs[type.ordinal()] = new StringBuilder();
		}
		outputs[type.ordinal()].append(totalTime + "," + str + "\n");
	}
	
	
	/**
	 * Write the log for 'type' to file 'filename' 
	 * @param type
	 * @param filename
	 */
	public static void write(GraphType type, String filename){
		if (outputs[type.ordinal()] == null){
			System.err.println("Could not save empty "+type.name()+" graph to filename '"+filename+"'.");
			return;
		}
		try (PrintWriter out = new PrintWriter(filename)){
			out.write(outputs[type.ordinal()].toString());
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Could not save "+type.name()+" graph to filename '"+filename+"'.");
			e.printStackTrace();
		}
	}
}
