/*
 * $Id: Dice.java 757 2008-04-17 17:42:53Z kiefer $
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
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Dice extends AbstractFeatureVectorSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Default convenience constructor. The vectors <tt>v1</tt> and
	 * <tt>v2</tt> have to be set by the caller by calling the
	 * <tt>setVectors()</tt> setter method.
	 */
	public Dice() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @throws InvalidVectorSizeException
	 */
	public Dice(simpack.util.Vector<? extends Number> v1,
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
		double A = 0;
		try {
			A = v1.getDotProduct(v2);
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return false;
		}
		double B = v1.getNorm(2.0);
		double C = v2.getNorm(2.0);

		similarity = new Double(2.0 * A / (Math.pow(B, 2.0) + Math.pow(C, 2.0)));
		setCalculated(true);
		return true;
	}
}