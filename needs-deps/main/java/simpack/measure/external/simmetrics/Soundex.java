/*
 * $Id: Soundex.java 757 2008-04-17 17:42:53Z kiefer $
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

import simmetrics.api.AbstractStringMetric;
import simpack.api.ISequenceAccessor;
import simpack.api.impl.external.AbstractSimMetricsSimilarityMeasure;

/**
 * This is a wrapper class for <a
 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>
 * of the <a href="http://www.dcs.shef.ac.uk/~sam/simmetrics/">SimMetrics
 * Project</a>.
 * <p>
 * It computes the string similarity of two strings.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Soundex extends AbstractSimMetricsSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>
	 */
	public Soundex(String str1, String str2) {
		super(simmetrics.similaritymetrics.Soundex.class.getName(), str1, str2);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string edit distance as computed by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 * @param internalComparisonMetric
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>
	 */
	public Soundex(String str1, String str2,
			AbstractStringMetric internalComparisonMetric) {
		super(simmetrics.similaritymetrics.Soundex.class.getName(), str1, str2,
				new Class[] { AbstractStringMetric.class },
				new Object[] { internalComparisonMetric });
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessor and returns the string edit distance as computed
	 * by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>
	 */
	public Soundex(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		this(leftAccessor.toString(), rightAccessor.toString());
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes two StringAccessor and returns the string edit distance as computed
	 * by <a
	 * href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>.
	 * Additional constructor arguments specify how the similarity is
	 * calculated.
	 * 
	 * @param leftAccessor
	 *            the first string accessor
	 * @param rightAccessor
	 *            the second string accessor
	 * @param internalComparisonMetric
	 * 
	 * @see <a
	 *      href="http://www.dcs.shef.ac.uk/~sam/simmetrics/similaritymetrics/Soundex.html">simmetrics.similaritymetrics.Soundex</a>
	 */
	public Soundex(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor,
			AbstractStringMetric internalComparisonMetric) {
		this(leftAccessor.toString(), rightAccessor.toString(),
				internalComparisonMetric);
	}
}