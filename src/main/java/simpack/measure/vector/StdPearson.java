/*
 * $Id: StdPearson.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.util.Vector;

/**
 * Computes the Pearson's correlation coefficient.
 * <p>
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class StdPearson extends AbstractFeatureVectorSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Default convenience constructor. The vectors <tt>v1</tt> and
	 * <tt>v2</tt> have to be set by the caller by calling the
	 * <tt>setVectors()</tt> setter method.
	 */
	public StdPearson() {
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
	public StdPearson(simpack.util.Vector<? extends Number> v1,
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

		double meanV1 = v1.getMean();
		double meanV2 = v2.getMean();

		Vector<Double> v1Clone = new Vector<Double>();
		Vector<Double> v2Clone = new Vector<Double>();

		for (Number n : v1) {
			v1Clone.add(n.doubleValue() - meanV1);
		}
		for (Number n : v2) {
			v2Clone.add(n.doubleValue() - meanV2);
		}

		double dot = 0d;
		double v1CloneNorm = 0d;
		double v2CloneNorm = 0d;
		try {
			dot = v1Clone.getDotProduct(v2Clone);

			v1CloneNorm = v1Clone.getNorm(2d);
			v2CloneNorm = v2Clone.getNorm(2d);

		} catch (InvalidVectorSizeException ex) {
			ex.printStackTrace();
		}

		// check division by zero
		if (v1CloneNorm == 0d || v2CloneNorm == 0d) {
			similarity = new Double(0d);
		} else {
			similarity = new Double(dot / (v1CloneNorm * v2CloneNorm));
		}

		// normalization
		similarity = 0.5d * (similarity + 1d);

		setCalculated(true);
		return true;
	}
}
