/* File: WordSalad.java - April 2018 */
package week09;

import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Iterator;

/**
 * Skeleton implementation of the WordSalad class.
 *
 * @author Michael Albert
 * @author Oleg Mazhanov
 */
public class WordSalad implements Iterable<String> {

	private WordNode first;
	private WordNode last;

	public WordSalad() {
		this.first = null;
		this.last = null;
	}

	public WordSalad(java.util.List<String> words) {
		for (String word : words) {
			addLast(word);
		}
	}

	public void add(String word) {
		if (this.first == null) {
			this.first = new WordNode(word, null);
			this.last = this.first;
			return;
		}
		WordNode newFirst = new WordNode(word, this.first);
		this.first = newFirst;
	}

	public void addLast(String word) {
		if (this.first == null) {
			add(word);
			return;
		}
		WordNode newLast = new WordNode(word, null);
		this.last.next = newLast;
		this.last = newLast;
	}

	private class WordNode {
		private String word;
		private WordNode next;

		private WordNode(String word, WordNode next) {
			this.word = word;
			this.next = next;
		}

	}

	public java.util.Iterator<String> iterator() {
		return new java.util.Iterator<String>() {
			private WordNode current = first;

			public boolean hasNext() {
				return current != null;
			}

			public String next() {
				String result = current.word;
				current = current.next;
				return result;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public String toString() {
		StringBuilder result = new StringBuilder("[");
		WordNode node = first;
		while (node != null) {
			result.append(node.word);
			result.append(node.next == null ? "" : ", ");
			node = node.next;
		}
		return result.toString() + "]";
	}

	/**
     *  Method distribute is intended to distribute WordSalad object into 
     *  an array of k instances of WordSalad objects.
     *  
     *  @param k integer - the size of array to distribute
     *  @return WordSalad array with size equal k
     *
     */
	public WordSalad[] distribute(int k) {
		WordSalad[] wsarray = new WordSalad[k];
		String word = null;
		int blocknum = 0;
		for (Iterator<String> single = this.iterator(); single.hasNext();) {
			word = (String) single.next();
			if (wsarray[blocknum] == null) {
				wsarray[blocknum] = new WordSalad();
			}
			wsarray[blocknum].addLast(word);
			if (blocknum < k - 1) {
				blocknum++;
			} else {
				blocknum = 0;
			}
		}
		return wsarray;
	}

	/**
     *  Method chop is intended to chop WordSalad object into 
     *  an array of k instances of WordSalad objects.
     *  @param k integer - the size of array to chop
     *  @return WordSalad array with size equal k
     *       
     */
	public WordSalad[] chop(int k) {
		String word = "";
		int counter = 0;
		WordSalad[] wsarray = new WordSalad[k];
		for (int i = 0; i < k; i++) {
			wsarray[i] = new WordSalad();
		}
		int wslength = 0;
		int chops = k;
		for (Iterator<String> it = this.iterator(); it.hasNext();) {
			it.next();
			wslength++;
		}

		for (Iterator<String> iterator = this.iterator(); iterator.hasNext();) {
			for (int i = 0; i < k; i++) {
				counter = 0;
				for (int l = 0; l < (wslength % chops == 0 ? (int) (wslength / chops)
						: (int) (wslength / chops) + 1); l++) {
					word = iterator.next();
					wsarray[i].addLast(word);
					counter++;
				}
				wslength = wslength - counter;
				chops = (chops > 1 ? chops - 1 : 1);
			}
		}
		return wsarray;
	}
	/**
     *  Method split is intended to split ordSalad object into 
     *  an array of k instances of WordSalad objects.
     *  
     *  @param k integer - the size of array to split
     *  @return WordSalad array with size equal k
     */
	public WordSalad[] split(int k) {
		String tempword = "";
		int text_to_split = 0;
		for (Iterator<String> i = this.iterator(); i.hasNext();) {
			i.next();
			text_to_split++;
		}
		int arraysize = 0;
		int text_to_split_copy = text_to_split;
		int words_to_pickup = (text_to_split % k == 0 ? (int) text_to_split / k : (int) text_to_split / k + 1);
		int words_to_pickup_copy = words_to_pickup;
		while (text_to_split_copy > 0) {
			arraysize++;
			text_to_split_copy -= words_to_pickup_copy;
			words_to_pickup_copy = (text_to_split_copy % k == 0 ? (int) text_to_split_copy / k : (int) text_to_split_copy / k + 1);
		}
		WordSalad[] wsarray = new WordSalad[arraysize];
		WordSalad a = this;
		int interval = (int)text_to_split/words_to_pickup;
		while (text_to_split > 0) {
			for (int j = 0; j < arraysize; j++) {
				int counter = 0;
				WordSalad wstoend = new WordSalad();
				WordSalad wstogo = new WordSalad();
				for (Iterator<String> iterator = a.iterator(); iterator.hasNext();) {
					tempword = iterator.next();
					if  (( counter == 0))  {
						wstogo.addLast(tempword);
					} else {
						wstoend.addLast(tempword);
					}
					if (counter < interval) {
						counter++;
					} else {
						counter = 0;
					}
				}
				text_to_split = text_to_split - words_to_pickup;
				words_to_pickup = (text_to_split % k == 0 ? (int) text_to_split / k : (int) text_to_split / k + 1);
				a = wstoend;
				wsarray[j] = wstogo;
				if (words_to_pickup != 0 ) {
					interval = (int)text_to_split/words_to_pickup;
				}
			}
		}
		return wsarray;
	}
	/**
     *  Method merge is intended to merge WordSalad array 
     *  into WordSalad object.
     *  
     *  @param wsmembers is WordSalad array to operate merge
     *  @return WordSalad class before distribute (original)
     */
	public static WordSalad merge(WordSalad[] wsmembers) {
		WordSalad wordsalad = new WordSalad();
		int i = 0;
		String s = "";
		int[] notempty = new int[wsmembers.length];
		Iterator<String>[] iterators = new Iterator[wsmembers.length];
		for (int j = 0; j < wsmembers.length; j++) {
			iterators[j] = wsmembers[j].iterator();
			notempty[j] = 1;
		}
		while (true) {
			if (iterators[i].hasNext()) {
				s = iterators[i].next();
				wordsalad.addLast(s);
			} else {
				notempty[i] = 0;
				int flag = 0;
				for (int m = 0; m < notempty.length; m++) {
					flag = flag + notempty[m];
				}
				if (flag == 0) {
					return wordsalad;
				}
			}
			if (i < wsmembers.length - 1) {
				i++;
			} else {
				i = 0;
			}
		}
	}
	/**
     *  Method join is intended to join WordSalad array 
     *  into WordSalad object.
     *  
     *  @param wsmembers is WordSalad array to operate join
     *  @return WordSalad class before chop (original)
     */

	public static WordSalad join(WordSalad[] wsmembers) {
		WordSalad wordsalad = new WordSalad();
		for (int i = 0; i < wsmembers.length; i++) {
			for (Iterator<String> block = wsmembers[i].iterator(); block.hasNext();) {
				String word = (String) block.next();
				wordsalad.addLast(word);
			}
		}
		return wordsalad;
	}

	/**
     *  Method recombine is intended to recombine WordSalad array 
     *  into WordSalad object.
     *  
     *  @param blocks is WordSalad array size
     *  @param k integer is number blocks of WordSalad array
     *  @return WordSalad class before split (original)
     */

	public static WordSalad recombine(WordSalad[] blocks, int k) {
		return null;
	}

}
