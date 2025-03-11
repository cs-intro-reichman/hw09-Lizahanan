import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		In newReader = new In(fileName);
        String text = newReader.readAll();
        for (int i = 0; i < text.length() - windowLength ; ++i) {
            String key = text.substring(i, i + windowLength);
            if (CharDataMap.get(key) == null) {
                CharDataMap.put(key, new List());
            }
            CharDataMap.get(key).update(text.charAt(i + windowLength));
            calculateProbabilities(CharDataMap.get(key));
        }

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
		int totalChars = 0;
        ListIterator it = probs.listIterator(0);
        // Count the total number of characters in the list
        while(it.hasNext() && it != null) {
            totalChars += it.next().count; //for each charData object in the list, add its count to totalChars
        }
        // Calculate the probabilities of each character
        //cumulative probability
        double cp = 0.0;
        for(int i=0; i<probs.getSize(); i++) {
            CharData current = probs.get(i);
            //probability of the current character
            current.p = (double)current.count / totalChars;
            //cumulative probability of the elements up to the current element
            current.cp = (double)cp + current.p;
            cp += current.p;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        // Generate a random number between 0 and 1
		double r = randomGenerator.nextDouble();
        CharData[] charDataArray = probs.toArray();
        //for each charData object in the list, if the random number is less than the cumulative probability of the object, return the character of the object
        for(CharData cd : charDataArray){
            if(r < cd.cp){
                return cd.chr;
            }
        }
        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		StringBuilder str = new StringBuilder();
        str.append(initialText);
        while(str.length() < textLength){
            String key = str.substring(str.length() - windowLength);
            str.append(getRandomChar(CharDataMap.get(key)));
        }
        return str.toString();
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
    }
}
