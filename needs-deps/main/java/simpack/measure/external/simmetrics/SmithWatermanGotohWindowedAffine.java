/*
 * $Id: SmithWatermanGotohWindowedAffine.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.external.simmetrics;

import simmetrics.api.AbstractAffineGapCost;
import simmetrics.api.AbstractSubstitutionCost;
import simpack.api.ISequenceAccessor;
import simpack.api.impl.external.AbstractSimMetricsSimilarityMeasure;

/**
 * This is a wrapper class for <a
 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
 * of the <a href="http://www.dcs.shef.ac.uk/~sam/simmetrics/">SimMetrics
 * Project</a>.
 * <p>
 * It computes the string similarity of two strings.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class SmithWatermanGotohWindowedAffine extends
		AbstractSimMetricsSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param gapCostFunc
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractAffineGapCost gapCostFunc) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2,
				new Class[] { AbstractAffineGapCost.class },
				new Object[] { gapCostFunc });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param gapCostFunc
	 * @param costFunc
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractAffineGapCost gapCostFunc, AbstractSubstitutionCost costFunc) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2, new Class[] {
						AbstractAffineGapCost.class,
						AbstractSubstitutionCost.class }, new Object[] {
						gapCostFunc, costFunc });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param gapCostFunc
	 * @param costFunc
	 * @param affineGapWindowSize
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractAffineGapCost gapCostFunc,
			AbstractSubstitutionCost costFunc, int affineGapWindowSize) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2, new Class[] {
						AbstractAffineGapCost.class,
						AbstractSubstitutionCost.class, Integer.TYPE },
				new Object[] { gapCostFunc, costFunc, affineGapWindowSize });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param gapCostFunc
	 * @param affineGapWindowSize
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractAffineGapCost gapCostFunc, int affineGapWindowSize) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2, new Class[] {
						AbstractAffineGapCost.class, Integer.TYPE },
				new Object[] { gapCostFunc, affineGapWindowSize });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param costFunc
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractSubstitutionCost costFunc) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2,
				new Class[] { AbstractSubstitutionCost.class },
				new Object[] { costFunc });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param costFunc
	 * @param affineGapWindowSize
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			AbstractSubstitutionCost costFunc, int affineGapWindowSize) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2, new Class[] {
						AbstractSubstitutionCost.class, Integer.TYPE },
				new Object[] { costFunc, affineGapWindowSize });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param affineGapWindowSize
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/SmithWatermanGotohWindowedAffine.html">simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine</a>
	 */
	public SmithWatermanGotohWindowedAffine(String str1, String str2,
			int affineGapWindowSize) {
		super(
				simmetrics.similaritymetrics.SmithWatermanGotohWindowedAffine.class
						.getName(), str1, str2, new Class[] { Integer.TYPE },
				new Object[] { affineGapWindowSize });
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		this(leftAccessor.toString(), rightAccessor.toString());
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractAffineGapCost gapCostFunc) {
		this(leftAccessor.toString(), rightAccessor.toString(), gapCostFunc);
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractAffineGapCost gapCostFunc, AbstractSubstitutionCost costFunc) {
		this(leftAccessor.toString(), rightAccessor.toString(), gapCostFunc,
				costFunc);
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractSubstitutionCost costFunc) {
		this(leftAccessor.toString(), rightAccessor.toString(), costFunc);
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractAffineGapCost gapCostFunc,
			AbstractSubstitutionCost costFunc, int affineGapWindowSize) {
		this(leftAccessor.toString(), rightAccessor.toString(), gapCostFunc,
				costFunc, affineGapWindowSize);
	}

	public SmithWatermanGotohWindowedAffine(
			ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractSubstitutionCost costFunc, int affineGapWindowSize) {
		this(leftAccessor.toString(), rightAccessor.toString(), costFunc,
				affineGapWindowSize);
	}
}