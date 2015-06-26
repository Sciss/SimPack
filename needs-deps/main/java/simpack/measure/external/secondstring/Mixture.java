/*
 * $Id: Mixture.java 757 2008-04-17 17:42:53Z kiefer $
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

import com.wcohen.ss.api.Tokenizer;

import simpack.api.ISequenceAccessor;
import simpack.api.impl.external.AbstractSecondStringSimilarityMeasure;

/**
 * This is a wrapper class for <a
 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>
 * of the <a href="http://secondstring.sourceforge.net/">SecondString Project</a>.
 * <p>
 * It computes the string edit distance of two strings.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Mixture extends AbstractSecondStringSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>
	 */
	public Mixture(String str1, String str2) {
		super(com.wcohen.ss.Mixture.class.getName(), str1, str2);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>.
	 * Additional constructor arguments specify how the distance is calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param tokenizer
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>
	 */
	public Mixture(String str1, String str2, Tokenizer tokenizer) {
		super(com.wcohen.ss.Mixture.class.getName(), str1, str2,
				new Class[] { Tokenizer.class }, new Object[] { tokenizer });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessors and returns the string edit distance as
	 * computed by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>
	 */
	public Mixture(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		this(leftAccessor.toString(), rightAccessor.toString());
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessor and returns the string edit distance as computed
	 * by <a
	 * href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>.
	 * Additional constructor arguments specify how the distance is calculated.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * @param tokenizer
	 * 
	 * @see <a
	 *      href="http://secondstring.sourceforge.net/javadoc/com/wcohen/secondstring/Mixture.html">com.wcohen.ss.Mixture</a>
	 */
	public Mixture(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor, Tokenizer tokenizer) {
		this(leftAccessor.toString(), rightAccessor.toString(), tokenizer);
	}
}