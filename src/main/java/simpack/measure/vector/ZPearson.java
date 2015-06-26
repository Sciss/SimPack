/*
 * $Id: ZPearson.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.vector;

import simpack.api.impl.AbstractFeatureVectorSimilarityMeasure;
import simpack.exception.InvalidVectorSizeException;

/**
 * Computes the Pearson's correlation coefficient.
 * <p>
 * The correlation between two variables reflects the degree to which the
 * variables are related. The most common measure of correlation is the Pearson
 * Product Moment Correlation (called Pearson's correlation for short).
 * <p>
 * A simple looking formula can be used if the numbers are converted into z
 * scores. For example, a z score of 2 means the original score was 2 standard
 * deviations above the mean. Note that the z distribution will only be a normal
 * distribution if the original distribution is normal (from HyperStat:
 * http://davidmlane.com/hyperstat/).
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ZPearson extends AbstractFeatureVectorSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Default convenience constructor. The vectors <tt>v1</tt> and
	 * <tt>v2</tt> have to be set by the caller by calling the
	 * <tt>setVectors()</tt> setter method.
	 */
	public ZPearson() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param v1
	 *            first vector
	 * @param v2
	 *            second vector
	 * 
	 * @throws InvalidVectorSizeException
	 */
	public ZPearson(simpack.util.Vector<? extends Number> v1,
			simpack.util.Vector<? extends Number> v2)
			throws InvalidVectorSizeException {
		super(v1, v2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.measure.SimilarityMeasure#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);

		double sumXY = 0;
		try {
			sumXY = v1.getDotProduct(v2);
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return false;
		}
		double sumXsumY = v1.getNorm(1d) * v2.getNorm(1d);
		double sumX2 = Math.pow(v1.getNorm(2d), 2d);
		double sumX22 = Math.pow(v1.getNorm(1d), 2d);
		double sumY2 = Math.pow(v2.getNorm(2d), 2d);
		double sumY22 = Math.pow(v2.getNorm(1d), 2d);
		double N = v1.size();

		// check division by zero
		if (Math.sqrt((sumX2 - (sumX22 / N)) * (sumY2 - (sumY22 / N))) == 0d) {
			similarity = new Double(0d);
		} else {
			similarity = new Double((sumXY - (sumXsumY / N)))
					/ Math
							.sqrt((sumX2 - (sumX22 / N))
									* (sumY2 - (sumY22 / N)));
		}

		// normalization
		similarity = 0.5d * (similarity + 1d);

		setCalculated(true);
		return true;
	}
}
