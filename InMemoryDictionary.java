package dict;

import java.io.*;
import java.util.*;

/**
 * Implements a persistent dictionary that can be held entirely in memory.
 * When flushed, it writes the entire dictionary back to a file.
 * 
 * The file format has one keyword per line:
 * <pre>word:def1:def2:def3,...</pre>
 * 
 * Note that an empty definition list is allowed (in which case the entry would have the form: <pre>word:</pre> 
 * 
 * @author talm
 *
 */
public class InMemoryDictionary extends TreeMap<String,String> implements PersistentDictionary  {
	private static final long serialVersionUID = 1L; // (because we're extending a serializable class)

	File file;

	public InMemoryDictionary(File dictFile) {
		this.file = dictFile;
	}

	@Override
	public void open() throws IOException {
		// check if file exists
		if(!this.file.exists()) {
			return;
		}
		// initialize buffer
		BufferedReader buffer = new BufferedReader(new FileReader(this.file));


		for(String nextLine = buffer.readLine(); nextLine != null; nextLine = buffer.readLine()) {
			String word = null;
			String def = null;
			int length = nextLine.length();
			int index = nextLine.indexOf(':');

			// if we have no colon, we have empty definition
			if(index == -1) {
				word = nextLine;
				def = null;
			} else {
				// separate by the colon
				word = nextLine.substring(0, index);
				def = nextLine.substring(index + 1, length);
			}
			put(word, def);
		}
		buffer.close();
	}
	@Override
	public void close() throws IOException {
		FileWriter writer = new FileWriter(this.file);
		// loop to add dictionary word and definitions
		for(Map.Entry<String, String> dict : entrySet()) {
			writer.append(dict.getKey() + ":" + dict.getValue() + "\n");
		}
		writer.close();
	}
}
