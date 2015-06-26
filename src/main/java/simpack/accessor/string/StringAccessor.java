/*
 * $Id: StringAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simpack.api.ISequenceAccessor;
import simpack.api.IToken;
import simpack.api.ITokenizer;
import simpack.api.impl.AbstractCollectionAccessor;
import simpack.tokenizer.DummyTokenizer;

/**
 * @author Christoph Kiefer
 * @author Michael Wuersch
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class StringAccessor extends AbstractCollectionAccessor<String> implements
		ISequenceAccessor<String> {

	private String s;

	/**
	 * Constructor.
	 * <p>
	 * Takes a string as input. Uses default tokenizer
	 * {@link com.wcohen.ss.tokens.SimpleTokenizer}
	 * 
	 * @param s
	 *            initial string
	 */
	public StringAccessor(String s) {
		this(s, new DummyTokenizer());
	}

	/**
	 * Constructor. Takes a string and corresponding string tokenizer as inputs.
	 * 
	 * @param s
	 * @param tokenizer
	 */
	public StringAccessor(String s, ITokenizer tokenizer) {
		this.s = s;
		// tokenizer.setIgnoreCase(false);
		// tokenizer.setIgnorePunctuation(false);
		// add to collection
		elements = new Vector<String>();
		List<IToken> tokens = tokenizer.tokenize(s);
		for (IToken token : tokens) {
			elements.add(token.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wcohen.ss.api.StringWrapper#length()
	 */
	public int length() {
		return getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wcohen.ss.api.StringWrapper#unwrap()
	 */
	public String unwrap() {
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wcohen.ss.api.StringWrapper#charAt(int)
	 */
	public char charAt(int i) {
		return s.charAt(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IStringAccessor#getTokenAt(int)
	 */
	public String getTokenAt(int i) {
		return elements.toArray(new String[] {})[i];
	}

	/**
	 * Counts how often a substring s occurs in the sequence which is wrapped by
	 * this accessor
	 * 
	 * @param s
	 *            the substring whose occurence will be counted
	 * @return how often the subtring s occured
	 */
	public int getElementFrequency(String s) {
		int f = 0;
		String[] strings = elements.toArray(new String[] {});
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals(s)) {
				f++;
			}
		}
		return f;
	}

	/**
	 * Counts how often an element occurs in the sequence wrapped by this
	 * accessor
	 * 
	 * @return an array of integers indicating how often each element occurs in
	 *         the sequence wrapped by this accessor. The integer at the i-th
	 *         position of the array counts how often the i-th element of the
	 *         sequence occurs in the whole sequence.
	 */
	public int[] getElementFrequencies() {
		String[] strings = elements.toArray(new String[] {});
		int[] frequencies = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			frequencies[i] = getElementFrequency(strings[i]);
		}
		return frequencies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ISequenceAccessor#getSequence()
	 */
	public List<String> getSequence() {
		return new ArrayList<String>(elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return unwrap();
	}
}