package facebroke.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

public class LoremGenerator {
	
	// Word list lifted from https://gist.github.com/rviscomi/1479649
	private static final String[] words = {"lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
											"adipiscing", "elit", "curabitur", "vel", "hendrerit", "libero",
											"eleifend", "blandit", "nunc", "ornare", "odio", "ut",
											"orci", "gravida", "imperdiet", "nullam", "purus", "lacinia",
											"a", "pretium", "quis", "congue", "praesent", "sagittis",
											"laoreet", "auctor", "mauris", "non", "velit", "eros",
											"dictum", "proin", "accumsan", "sapien", "nec", "massa",
											"volutpat", "venenatis", "sed", "eu", "molestie", "lacus",
											"quisque", "porttitor", "ligula", "dui", "mollis", "tempus",
											"at", "magna", "vestibulum", "turpis", "ac", "diam",
											"tincidunt", "id", "condimentum", "enim", "sodales", "in",
											"hac", "habitasse", "platea", "dictumst", "aenean", "neque",
											"fusce", "augue", "leo", "eget", "semper", "mattis",
											"tortor", "scelerisque", "nulla", "interdum", "tellus", "malesuada",
											"rhoncus", "porta", "sem", "aliquet", "et", "nam",
											"suspendisse", "potenti", "vivamus", "luctus", "fringilla", "erat",
											"donec", "justo", "vehicula", "ultricies", "varius", "ante",
											"primis", "faucibus", "ultrices", "posuere", "cubilia", "curae",
											"etiam", "cursus", "aliquam", "quam", "dapibus", "nisl",
											"feugiat", "egestas", "class", "aptent", "taciti", "sociosqu",
											"ad", "litora", "torquent", "per", "conubia", "nostra",
											"inceptos", "himenaeos", "phasellus", "nibh", "pulvinar", "vitae",
											"urna", "iaculis", "lobortis", "nisi", "viverra", "arcu",
											"morbi", "pellentesque", "metus", "commodo", "ut", "facilisis",
											"felis", "tristique", "ullamcorper", "placerat", "aenean", "convallis",
											"sollicitudin", "integer", "rutrum", "duis", "est", "etiam",
											"bibendum", "donec", "pharetra", "vulputate", "maecenas", "mi",
											"fermentum", "consequat", "suscipit", "aliquam", "habitant", "senectus",
											"netus", "fames", "quisque", "euismod", "curabitur", "lectus",
											"elementum", "tempor", "risus", "cras"};
	
	private int avgSentenceLength = 8;
	private int minSentenceLength = 3;
	private int upperRandom = (avgSentenceLength - minSentenceLength) * 2;
	private Random r;
	
	
	/**
	 * Build a new Lorem Ipsum generator
	 * @param seed - null or integer if you want a seed
	 * @param avgSentenceLength
	 * @param minSentenceLength
	 */
	public LoremGenerator (Long seed) {
		
		if (seed == null) {
			r = new SecureRandom();
		}else {
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		    buffer.putLong(seed);
			r = new SecureRandom(buffer.array());
		}
	}
	
	
	/**
	 * Get a random string with words. First letter is capitalized
	 * @param len
	 * @return Random string
	 */
	public String getWords(int len) {
		if(len < 1) {
			return "";
		}
		
		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < len; i++) {
			buffer.append(words[r.nextInt(words.length)]);
			if(i+1 < len) {
				buffer.append(" ");
			}
		}
		
		return buffer.substring(0, 1).toUpperCase() + buffer.substring(1);
	}

	
	/**
	 * Get a certain number of random sentences in one string
	 * @param numSentences
	 * @return Random sentences in one string
	 */
	public String getSentences(int numSentences) {
		if (numSentences < 1) {
			return "";
		}
		
		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < numSentences; i++) {
			buffer.append(getWords(minSentenceLength + r.nextInt(upperRandom)) + ".");
			
			if(i+1 < numSentences) {
				buffer.append(" ");
			}
		}
		
		return buffer.toString();
	}


	public int getAvgSentenceLength() {
		return avgSentenceLength;
	}


	public void setAvgSentenceLength(int avgSentenceLength) {
		this.avgSentenceLength = avgSentenceLength;
		this.upperRandom = (avgSentenceLength - minSentenceLength) * 2;
	}


	public int getShortestSentenceLength() {
		return minSentenceLength;
	}


	public void setShortestSentenceLength(int shortestSentenceLength) {
		this.minSentenceLength = shortestSentenceLength;
		this.upperRandom = (avgSentenceLength - minSentenceLength) * 2;
	}
	
	
}
