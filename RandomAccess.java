package files;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {
	
	/**
	 * Treat the file as an array of (unsigned) 8-bit values and sort them 
	 * in-place using a bubble-sort algorithm.
	 * You may not read the whole file into memory! 
	 * @param file
	 */
	public static void sortBytes(RandomAccessFile file) throws IOException {
		// indicates whether sorting is done
		boolean sorted = false;
		//iterate through 'array'
		while(!sorted) {
			sorted = true;
			for(int i = 0; i < file.length(); i++) {
				for(int j = 0; j < file.length() - 1; j++) {
					file.seek(j);
					// convert to unsigned bytes
					int num1 = file.readByte() & 0xff;
					int num2 = file.readByte() & 0xff;

					// sort if necessary
					if(num1 > num2) {
						sorted = false;

						file.seek(j);
						// swap values
						file.write(num2);
						file.write(num1);
					}
				}
			}
		}
	}
	
	/**
	 * Treat the file as an array of unsigned 24-bit values (stored MSB first) and sort
	 * them in-place using a bubble-sort algorithm. 
	 * You may not read the whole file into memory! 
	 * @param file
	 * @throws IOException
	 */
	public static void sortTriBytes(RandomAccessFile file) throws IOException {
		// indicates whether sorting is done
		boolean sorted = false;

		file.seek(0);
		//iterate through 'array'
		while(!sorted) {
			sorted = true;
			int prevNum = 0;
			for (int i = 0; i < file.length() - 2; i += 3) {
				// convert value of number
				int num = (file.read() << 16) | (file.read() << 8) | file.read();
				// make comparison
				if (num < prevNum) {
					sorted = false;

					// swap nums
					file.seek(i - 3);
					byte[] write1 = {(byte) (num >>> 16), (byte) (num >>> 8), (byte) num};
					byte[] write2 = {(byte) (prevNum >> 16), (byte) (prevNum >> 8), (byte) (prevNum)};
					file.write(write1);
					file.write(write2);
				} else {
					prevNum = num;
				}
			}
		}
	}
}
