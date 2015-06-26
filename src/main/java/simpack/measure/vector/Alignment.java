/*
 * $Id: Alignment.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.impl.AbstractFeatureVectorSimilarityMeasure;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.exception.InvalidVectorSizeException;

/**
 * This measure first aligns two input vectors along their content, generates
 * binary vectors from the alignment, and compares the binary vectors by the
 * passed feature vector similarity measure.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Alignment<E> extends AbstractSimilarityMeasure {

	private static Logger logger = LogManager.getLogger(Alignment.class);

	private Vector<E> v1, v2;

	private Vector<E> v1Aligned, v2Aligned;

	private simpack.util.Vector<Double> v1AlignedBinary, v2AlignedBinary;

	private AbstractFeatureVectorSimilarityMeasure m;

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @param m
	 *            the feature vector similarity measure to compare the
	 *            generated, aligned binary vectors.
	 */
	public Alignment(Vector<E> v1, Vector<E> v2,
			AbstractFeatureVectorSimilarityMeasure m) {
		this.m = m;
		this.v1 = v1;
		this.v2 = v2;
		align();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		try {
			m.setVector(v1AlignedBinary, v2AlignedBinary);
			if (m.calculate()) {
				setCalculated(true);
				similarity = m.getSimilarity();
				return m.isCalculated();
			} else {
				return false;
			}
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Generates the alignemnt of the input vectors as two binary vectors.
	 */
	private void align() {
		Vector<E> t1 = new Vector<E>(v1.size());
		Vector<E> t12 = new Vector<E>(v1.size());
		Vector<E> t2 = new Vector<E>(v2.size());

		for (E element : v1) {
			t1.add(element);
			t12.add(element);
		}

		for (E element : v2) {
			t2.add(element);
		}

		// v1 without intersection
		v1.removeAll(v2);
		// v2 without intersection
		v2.removeAll(t1);

		// intersection
		t1.retainAll(t2);

		// merge
		v1.addAll(t1);
		v1.addAll(v2);

		Vector<E> collection = v1;

		v1 = t12;
		v2 = t2;

		logger.debug(collection.toString());
		logger.debug(v1.toString());
		logger.debug(v2.toString());

		v1Aligned = new Vector<E>();
		v2Aligned = new Vector<E>();
		v1AlignedBinary = new simpack.util.Vector<Double>();
		v2AlignedBinary = new simpack.util.Vector<Double>();

		for (E element : collection) {
			if (v1.contains(element)) {
				v1AlignedBinary.add(1d);
				v1Aligned.add(element);
			} else {
				v1AlignedBinary.add(0d);
				v1Aligned.add(null);
			}
			if (v2.contains(element)) {
				v2AlignedBinary.add(1d);
				v2Aligned.add(element);
			} else {
				v2AlignedBinary.add(0d);
				v2Aligned.add(null);
			}
		}

		logger.debug(v1AlignedBinary.toString());
		logger.debug(v2AlignedBinary.toString());
	}
}