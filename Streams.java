package files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Streams {
	/**
	 * Read from an InputStream until a quote character (") is found, then read
	 * until another quote character is found and return the bytes in between the two quotes. 
	 * If no quote character was found return null, if only one, return the bytes from the quote to the end of the stream.
	 * @param in
	 * @return A list containing the bytes between the first occurrence of a quote character and the second.
	 */
	public static List<Byte> getQuoted(InputStream in) throws IOException {
		// initialize return list, increment, and whether or not we have found quotes yet
		List<Byte> output = new ArrayList<>();
		int inc;
		boolean foundQuotes = false;

		// read Bytes
		while ((inc = in.read()) != -1) {
			if ((char) inc == '"') {
				// if found quotes
				if (foundQuotes) {
					// if closing quotes
					break;
				} else {
					// opening quotes
					foundQuotes = true;
				}
			} else if (foundQuotes) {
				// record between quotes
				output.add(Byte.valueOf((byte) inc));
			}
		}
		if(foundQuotes) {
			return output;
		} else {
			return null;
		}
	}
	
	/**
	 * Read from the input until a specific string is read, return the string read up to (not including) the endMark.
	 * @param in the Reader to read from
	 * @param endMark the string indicating to stop reading. 
	 * @return The string read up to (not including) the endMark (if the endMark is not found, return up to the end of the stream).
	 */
	public static String readUntil(Reader in, String endMark) throws IOException {
		// initialize return String, increment, and whether or not we have found endMark yet
		StringBuilder output = new StringBuilder();
		int inc;

		// read bytes
		while ((inc = in.read()) != -1) {
			output.append((char) inc);
			// check if we found endMark
			int index = output.indexOf(endMark);
			if (index != -1) {
				// we found endMark, need to return string without endMark
				return output.substring(0, index);
			}
		}
		return null;
	}
	
	/**
	 * Copy bytes from input to output, ignoring all occurrences of badByte.
	 * @param in
	 * @param out
	 * @param badByte
	 */
	public static void filterOut(InputStream in, OutputStream out, byte badByte) throws IOException {
		//initialize byte array
		byte[] byteArray = new byte[1]; // want to read byte by byte
		int inc;
		while ((inc = in.read(byteArray)) != -1) {
			if (byteArray[0] != badByte) {
				// include only good bytes
				out.write(byteArray, 0, inc);
			}
		}
	}
	
	/**
	 * Read a 40-bit (unsigned) integer from the stream and return it. The number is represented as five bytes, 
	 * with the most-significant byte first. 
	 * If the stream ends before 5 bytes are read, return -1.
	 * @param in
	 * @return the number read from the stream
	 */
	public static long readNumber(InputStream in) throws IOException {
		long dig;
		int counter = 0;
		long result = 0;

		// read number
		while((dig = in.read()) != -1) {
			counter++;
			// convert to unsigned number
			result = (result << 8) + (dig & 0xff);
		}
		// if stream ends before 5 bytes are read
		if(counter < 5) {
			return -1;
		}
		return result;

	}
}
