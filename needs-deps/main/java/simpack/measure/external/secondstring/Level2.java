/*
 * $Id: Level2.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.external.secondstring;

import simpack.api.ISequenceAccessor;
import simpack.api.impl.external.AbstractSecondStringSimilarityMeasure;

import com.wcohen.ss.api.StringDistance;
import com.wcohen.ss.api.Tokenizer;

/**
 * This is a wrapper class for <a
 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Level2.html">com.wcohen.ss.Level2</a>
 * of the <a href="http://secondstring.sourceforge.net/">SecondString Project</a>.
 * It computes the string edit distance of two strings.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Level2 extends AbstractSecondStringSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Level2.html">com.wcohen.ss.Level2</a>.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param tokenizer
	 * @param distance
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Level2.html">com.wcohen.ss.Level2</a>
	 */
	public Level2(String str1, String str2, Tokenizer tokenizer,
			StringDistance distance) {
		super(com.wcohen.ss.Level2.class.getName(), str1, str2, new Class[] {
				Tokenizer.class, StringDistance.class }, new Object[] {
				tokenizer, distance });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessor and returns the string edit distance as computed
	 * by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Level2.html">com.wcohen.ss.Level2</a>.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * @param tokenizer
	 * @param distance
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Level2.html">com.wcohen.ss.Level2</a>
	 */
	public Level2(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor, Tokenizer tokenizer,
			StringDistance distance) {
		this(leftAccessor.toString(), rightAccessor.toString(), tokenizer,
				distance);
	}
}