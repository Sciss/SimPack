/*
 * $Id: StringUtils.java 757 2008-04-17 17:42:53Z kiefer $
 *
 * Copyright (C) 2004-2008 by the Dynamic and Distributed Information Systems
 * Group at the University of Zurich, Switzerland
 *
 * All inquiries regarding the copyrights of this project should be addressed
 * to:
 *
 * Prof. Abraham Bernstein, PhD
 * Dynamic and Distributed Information Systems Group
 * Department of Informatics
 * University of Zurich
 * Binzmuehlestrasse 14
 * CH-8050 Zurich, Switzerland
 *
 * SimPack is licensed under the GNU Lesser General Public License
 *
 * Details can be found at http://www.gnu.org/licenses/lgpl.html or at
 * http://www.ifi.uzh.ch/ddis/simpack.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA
 *
 */
package simpack.util.corpus;

import java.util.Vector;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class StringUtils {

	// public static PorterStemmer DEFAULT_STEMMER = new PorterStemmer();
	public static LucenePorterStemmer DEFAULT_STEMMER = new LucenePorterStemmer();

	/**
	 * Cleans a string. The string might be a single string (word) or a sequence
	 * of strings. (1) converts every character of the input string to lower
	 * case, (2) removes punctuation, (3) removes stopwords, and (4) stems every
	 * word of the input string. Non-letter strings get filtered out by the
	 * stemmer.
	 * 
	 * @param in
	 *            input string to be cleaned
	 * 
	 * @return cleaned string
	 */
	public static String clean(String in) throws RuntimeException {
		// everything is converted to lower case
		String input = in.toLowerCase();
		// punctuation is replaced
		input = input.replaceAll("\\p{Punct}+", " ");
		// stopwords are removed
		input = Stopwords.remove(input);
		// stem every string in the remaining input string
		String[] temp = null;
		temp = input.split(" ");
		Vector<String> stems = new Vector<String>();
		for (int i = 0; i < temp.length; i++) {
			stems.add(DEFAULT_STEMMER.stem(temp[i]));
		}
		input = "";
		for (String s : stems) {
			input += s + " ";
		}
		// again check for (stemmed) stopwords
		input = Stopwords.remove(input.trim());
		return input;
	}
}
