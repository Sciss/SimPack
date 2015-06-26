/*
 * $Id: Vector.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util;

import simpack.exception.InvalidVectorSizeException;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Vector<E extends Number> extends java.util.Vector<E> {

	private static final long serialVersionUID = -6016841594387442712L;

	/**
	 * Constructor.
	 * <p>
	 */
	public Vector() {
		super();
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructs a vector of size <code>size</code>.
	 * 
	 * @param size
	 *            the size of the constructed vector
	 */
	public Vector(int size) {
		super(size);
	}

	/**
	 * Constructor.
	 * <p>
	 * Constructs a vector of size <code>size</code> and adds all the elements
	 * of <code>values</code> to this vector.
	 * 
	 * @param values
	 */
	public Vector(E[] values) {
		this(values.length);
		for (E d : values) {
			this.add(d);
		}
	}

	/**
	 * @param vector
	 * @return the dot vector product
	 * @throws InvalidVectorSizeException
	 * 
	 * TODO Comment me
	 */
	public double getDotProduct(simpack.util.Vector<? extends Number> vector)
			throws InvalidVectorSizeException {
		if (this.size() != vector.size()) {
			throw new InvalidVectorSizeException();
		}

		int pos = 0;
		double dotProduct = 0d;
		for (Number v : vector) {
			dotProduct += v.doubleValue() * this.elementAt(pos++).doubleValue();
		}

		return dotProduct;
	}

	/**
	 * @param vector
	 * @return the addition of the vectors
	 * @throws InvalidVectorSizeException
	 * 
	 * TODO Comment me
	 */
	public Vector<? extends Number> getVectorAddition(
			Vector<? extends Number> vector) throws InvalidVectorSizeException {
		if (this.size() != vector.size()) {
			throw new InvalidVectorSizeException();
		}

		int pos = 0;
		Vector<Double> result = new simpack.util.Vector<Double>(this.size());
		for (Number v : this) {
			Double d = new Double(v.doubleValue()
					+ vector.elementAt(pos).doubleValue());
			result.add(d);
		}

		return result;
	}

	/**
	 * @param vector
	 * @param p
	 * @return the calculated Minowski distance
	 * @throws InvalidVectorSizeException
	 * 
	 * TODO Comment me
	 */
	public double getMinkowskiDistance(Vector<? extends Number> vector, double p)
			throws InvalidVectorSizeException {
		if (this.size() != vector.size()) {
			throw new InvalidVectorSizeException();
		}

		int pos = 0;
		double sum = 0d;
		for (Number v : this) {
			double abs = Math.abs(v.doubleValue()
					- vector.elementAt(pos++).doubleValue());
			sum += Math.pow(abs, p);
		}

		return Math.pow(sum, 1d / p);
	}

	/**
	 * @param vector
	 * @return the calculated Euclidean distance
	 * @throws InvalidVectorSizeException
	 * 
	 * TODO Comment me
	 */
	public double getEuclideanDistance(Vector<? extends Number> vector)
			throws InvalidVectorSizeException {
		return getMinkowskiDistance(vector, 2d);
	}

	/**
	 * @param vector
	 * @return the calculated Manhattan distance
	 * @throws InvalidVectorSizeException
	 * 
	 * TODO Comment me
	 */
	public double getManhattanDistance(Vector<? extends Number> vector)
			throws InvalidVectorSizeException {
		return getMinkowskiDistance(vector, 1d);
	}

	/**
	 * @param p
	 * @return
	 * 
	 * TODO Comment me
	 */
	public double getNorm(double p) {
		double d = 0d;
		for (Number v : this) {
			d += Math.pow(v.doubleValue(), p);
		}
		d = Math.pow(d, 1d / p);
		return d;
	}

	/**
	 * @return
	 * 
	 * TODO Comment me
	 */
	public double getMean() {
		double d = 0d;
		for (Number v : this) {
			d += v.doubleValue();
		}
		return d / this.size();
	}

	/**
	 * @return
	 * 
	 * TODO Comment me
	 */
	public double getVariance() {
		double d = 0d;
		double mean = getMean();
		for (Number v : this) {
			d += Math.pow(v.doubleValue() - mean, 2d);
		}
		return d / this.size();
	}

	/**
	 * @return
	 * 
	 * TODO Comment me
	 */
	public double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}

	/**
	 * @return
	 * 
	 * TODO Comment me
	 */
	public Vector<? extends Number> convertToZScore() {
		double mean = getMean();
		double std = getStandardDeviation();
		System.out.println(mean + " " + std);
		Vector<Double> toZScore = new Vector<Double>(this.size());
		for (Number v : this) {
			toZScore.add((v.doubleValue() - mean) / std);
		}
		return toZScore;
	}
}