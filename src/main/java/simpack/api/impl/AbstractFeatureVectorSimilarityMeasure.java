/*
 * $Id: AbstractFeatureVectorSimilarityMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import simpack.api.IDistanceConversion;
import simpack.exception.InvalidVectorSizeException;
import simpack.util.conversion.CommonDistanceConversion;

/**
 * This is the abstract base class for all vector-based similarity measures. The
 * similarity between two vectors is computed by the concrete vector similarity
 * measure which extends this abstract base class.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractFeatureVectorSimilarityMeasure extends AbstractSimilarityMeasure {

	public static final IDistanceConversion CONVERSION = new CommonDistanceConversion();

	protected IDistanceConversion conversion = AbstractFeatureVectorSimilarityMeasure.CONVERSION;

	protected simpack.util.Vector<? extends Number> v1;

	protected simpack.util.Vector<? extends Number> v2;

	/**
	 * Constructor.
	 * <p>
	 * Default convenience constructor. The vectors <tt>v1</tt> and
	 * <tt>v2</tt> have to be set by the caller by calling the
	 * <tt>setVectors()</tt> setter method.
	 */
	public AbstractFeatureVectorSimilarityMeasure() {

	}

	/**
	 * Constructor.
	 * <p>
	 * Two vectors are passed to the constructor. The similarity of the vectors
	 * is computed by the vector-based similarity measure which extends this
	 * abstract base class.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * 
	 * @throws InvalidVectorSizeException
	 * 
	 * @see simpack.measure.vector.Cosine
	 * @see simpack.measure.vector.Dice
	 * @see simpack.measure.vector.Euclidean
	 * @see simpack.measure.vector.Jaccard
	 * @see simpack.measure.vector.Overlap
	 * @see simpack.measure.vector.ZPearson
	 * @see simpack.util.Vector
	 */
	public AbstractFeatureVectorSimilarityMeasure(
			simpack.util.Vector<? extends Number> v1,
			simpack.util.Vector<? extends Number> v2)
			throws InvalidVectorSizeException {

		if (v1.size() != v2.size()) {
			throw new InvalidVectorSizeException();
		}

		this.v1 = v1;
		this.v2 = v2;
	}

	/**
	 * Sets the vectors to be compared by this feature vector-based simliarity
	 * measure.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @throws InvalidVectorSizeException
	 */
	public void setVector(simpack.util.Vector<? extends Number> v1,
			simpack.util.Vector<? extends Number> v2)
			throws InvalidVectorSizeException {

		if (v1.size() != v2.size()) {
			throw new InvalidVectorSizeException();
		}

		this.v1 = v1;
		this.v2 = v2;
	}
}
