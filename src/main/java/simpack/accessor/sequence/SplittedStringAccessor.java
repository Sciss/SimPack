/*
 * $Id: SplittedStringAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import simpack.api.ISequenceAccessor;
import simpack.api.impl.AbstractCollectionAccessor;

/**
 * This is an accessor class to access (parts of) a string that is split based
 * on a split pattern.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * @deprecated simpack.accessor.string.StringAccessor should be used instead.
 */
public class SplittedStringAccessor extends AbstractCollectionAccessor<String>
		implements ISequenceAccessor<String> {

	/**
	 * Constructor.
	 * <p>
	 * The constructor takes as string <tt>str</tt> as input and a string
	 * pattern <tt>patternStr</tt> upon which <tt>str</tt> is split.
	 * 
	 * @param str
	 *            the input string to be split
	 * @param patternStr
	 *            the string split pattern
	 */
	public SplittedStringAccessor(String str, String patternStr) {
		elements = new ArrayList<String>();
		String[] strings = str.split(patternStr);
		for (int i = 0; i < strings.length; i++) {
			elements.add(strings[i]);
		}
	}

	/**
	 * Returns the frequency of a string in the collection.
	 * 
	 * @param s
	 *            the string to find its frequency
	 * @return frequency of the string in the collection
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.CollectionAccessor#getElementFrequencies()
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
		List<String> sequence = new Vector<String>();
		for (String str : elements) {
			sequence.add(str);
		}
		return sequence;
	}
}