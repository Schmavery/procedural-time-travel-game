package entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.RandomManager;

public class Markov
{
	private Random rand;
	private HashMap<String, List<String>> dict;
	private int length;
	private List<String> starts;
	
	public Markov(List<String> corpus, int length){
		this.length = length;
		starts = new ArrayList<>();
		dict = new HashMap<>();
		generateDict(corpus);
	}
	
	public Markov(String path, int length){
		ArrayList<String> corpus = new ArrayList<>();
		try (FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr)
		){
			String s = null;
			while((s = br.readLine()) != null) {
				corpus.add(s.replaceAll("\n", ""));
			}
			fr.close();
			System.out.println("Loaded names from '" + path + "'.");
		} catch (IOException e){
			e.printStackTrace();
		}
		this.length = length;
		starts = new ArrayList<>();
		dict = new HashMap<>();
		generateDict(corpus);
	}
	
	private void generateDict(List<String> corpus){
		rand = new Random(RandomManager.getSeed("Markov"+corpus.size()));
		for (String word : corpus){
			if (word.length() < length){
				continue;
			}
			
			starts.add(word.substring(0, length));
			
			for (int i = 0; i <= word.length() - length; i++){
				String prefix = word.substring(i, i+length);
				
				if (i+length < word.length()){
					addEdge(prefix, String.valueOf(word.charAt(i+length)));
				} else {
					// End of word represented by null
					addEdge(prefix, null);
				}
			}
		}
	}
	
	private void addEdge(String prefix, String end){
		if (!dict.containsKey(prefix)){
			dict.put(prefix, new LinkedList<String>());
		}		
		dict.get(prefix).add(end);
	}
	
	public String genWord(){
		return genWord(starts.get(rand.nextInt(starts.size()-1)));
	}
	
	private String getPrediction(StringBuffer word){
		String prefix = word.substring(word.length() - length, word.length());
		if (!dict.containsKey(prefix)){
			System.out.println(prefix);
			return null;
		}
		List<String> list = dict.get(prefix);
		int index = 0;
		if (list.size() > 1){
			index = rand.nextInt(list.size()-1);
		} 
		return list.get(index);
	}
	
	public String genWord(String start){
		if (start.length() < length){
			return genWord();
		}
		StringBuffer word = new StringBuffer(start);
		String next = getPrediction(word);
		
		while (next != null){
			word.append(next);
			next = getPrediction(word);
		}
		
		return word.toString();
	}
	
	public String genWordInRange(int min, int max){
		String word;
		for(int i = 0; i < 100; i++){
			word = genWord();
			if (word.length() > min && word.length() < max){
				return word;
			}
		}
		System.out.println("Error: Could not find word in range.");
		return null;
	}
}
