/*
 * $Id: LOL.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.sequence;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.accessor.string.StringAccessor;
import simpack.api.IDistanceConversion;
import simpack.api.ISequenceAccessor;
import simpack.util.conversion.WorstCaseDistanceConversion;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class LOL<E> extends Levenshtein<E> {

	private static final Logger logger = LogManager.getLogger(LOL.class);

	public static double DEFAULT_INNER_TRESHOLD = 0.5d;

	// public static double DEFAULT_OUTER_TRESHOLD = 0.1d;

	protected double innerThreshold = DEFAULT_WEIGHT_DELETE;

	// protected double outerThreshold = DEFAULT_OUTER_TRESHOLD;

	/**
	 * Constructor.
	 * <p>
	 * Pass two sequence accessors which provide access to the sequences of
	 * which similarity should be calculated. Uses default weights
	 * {@link #DEFAULT_WEIGHT_INSERT}, {@link #DEFAULT_WEIGHT_DELETE},
	 * {@link #DEFAULT_WEIGHT_REPLACE} and {@link #DEFAULT_WEIGHT_REPLACE_EQUAL}.
	 * The passed weights must obey the cost functions c(weightInsert) +
	 * c(weightDelete) <= c(weightReplace) and c(weightInsert) + c(weightDelete) <=
	 * c(weightReplaceEqual), otherwise a
	 * {@link simpack.exception.WeightBalanceException} is thrown.
	 * 
	 * @param sequenceAccessor1
	 *            the first sequence accessor
	 * @param sequenceAccessor2
	 *            the second sequence accessor
	 */
	public LOL(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL,
				new WorstCaseDistanceConversion(), DEFAULT_INNER_TRESHOLD);
	}

	/**
	 * @param sequenceAccessor1
	 * @param sequenceAccessor2
	 * @param innerThreshold
	 */
	public LOL(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2, double innerThreshold) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL,
				new WorstCaseDistanceConversion(), innerThreshold);
	}

	/**
	 * @param sequenceAccessor1
	 * @param sequenceAccessor2
	 * @param conversion
	 */
	public LOL(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2,
			IDistanceConversion conversion) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL, conversion,
				DEFAULT_INNER_TRESHOLD);
	}

	/**
	 * @param sequenceAccessor1
	 * @param sequenceAccessor2
	 * @param conversion
	 * @param innerThreshold
	 */
	public LOL(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2,
			IDistanceConversion conversion, double innerThreshold) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL, conversion, innerThreshold);
	}

	/**
	 * Constructor.
	 * <p>
	 * Pass two sequence accessors which provide access to the sequences of
	 * which similarity should be calculated. The passed weights must obey the
	 * cost functions c(weightInsert) + c(weightDelete) <= c(weightReplace) and
	 * c(weightInsert) + c(weightDelete) <= c(weightReplaceEqual), otherwise a
	 * {@link simpack.exception.WeightBalanceException} is thrown.
	 * 
	 * @param sequenceAccessor1
	 *            the first sequence accessor
	 * @param sequenceAccessor2
	 *            the second sequence accessor
	 * @param weightInsert
	 *            weight of insert operation
	 * @param weightDelete
	 *            weight of delete operation
	 * @param weightReplace
	 *            weight of replace operation
	 * @param weightReplaceEqual
	 *            weight of replace-equal operation
	 * @param conversion
	 * @param innerThreshold
	 */
	public LOL(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2, double weightInsert,
			double weightDelete, double weightReplace,
			double weightReplaceEqual, IDistanceConversion conversion,
			double innerThreshold) {

		super(sequenceAccessor1, sequenceAccessor2, weightInsert, weightDelete,
				weightReplace, weightReplaceEqual, conversion);

		this.innerThreshold = innerThreshold;
	}

	public boolean calculate() {
		setCalculated(false);

		List<E> sequenceSrc = sequenceAccessor1.getSequence();
		List<E> sequenceTgt = sequenceAccessor2.getSequence();

		double wDist = 0d;
		double[][] matrix = new double[sequenceSrc.size() + 1][sequenceTgt
				.size() + 1];
		for (int zeile = 0; zeile <= sequenceSrc.size(); zeile++) {
			matrix[zeile][0] = zeile;
		}
		for (int spalte = 0; spalte <= sequenceTgt.size(); spalte++) {
			matrix[0][spalte] = spalte;
		}
		for (int spalte = 1; spalte <= sequenceTgt.size(); spalte++) {
			for (int zeile = 1; zeile <= sequenceSrc.size(); zeile++) {
				double valueFromLeft = matrix[zeile][spalte - 1] + weightInsert;
				double valueFromTop = matrix[zeile - 1][spalte] + weightDelete;
				double valueFromDiag = matrix[zeile - 1][spalte - 1];

				// TODO: very ugly! Think of something better here!
				String str1 = (String) sequenceSrc.get(zeile - 1);
				String str2 = (String) sequenceTgt.get(spalte - 1);

				Levenshtein<String> lev = new Levenshtein<String>(
						new StringAccessor(str1), new StringAccessor(str2),
						weightInsert, weightDelete, weightReplace,
						weightReplaceEqual, conversion);

				double sim = lev.getSimilarity();

				if (sim < innerThreshold) {
					valueFromDiag += weightReplace;
				} else {
					valueFromDiag += weightReplaceEqual;
				}

				logger
						.debug("Comparing " + str1 + " " + str2 + " sim = "
								+ sim);

				double v = Math.min(valueFromLeft, Math.min(valueFromDiag,
						valueFromTop));
				logger.debug("Writing " + v + " for [" + str1 + "," + str2
						+ "]");
				matrix[zeile][spalte] = v;

				// double wD = calculateWorstCaseDistance(str1, str2);

				// wDist += wD;
				// logger.debug("wDist for [" + str1 + "," + str2
				// + "] is " + wD);
			}
		}

		if (logger.isDebugEnabled()) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[i][j] + " ");
				}
				System.out.println();
			}
		}

		double dist = matrix[sequenceSrc.size()][sequenceTgt.size()];

		wDist = calculateWorstCaseDistance(sequenceSrc, sequenceTgt);

		logger.debug("wDist for " + sequenceSrc.toString() + " to "
				+ sequenceTgt.toString() + " is " + wDist);

		if (conversion instanceof WorstCaseDistanceConversion) {
			((WorstCaseDistanceConversion) conversion)
					.setWorstCaseDistance(wDist);
		}

		double sim = conversion.convert(dist);

		logger
				.debug("dist = " + dist + ", wDist = " + wDist + ", sim = "
						+ sim);

		similarity = new Double(sim);
		setCalculated(true);
		return true;
	}

	// protected double calculateWorstCaseDistance(String source, String target)
	// {
	// if (target.equals(source)) {
	// return 0d;
	// } else {
	// return 1d;
	// }
	// }

	protected double calculateWorstCaseDistance(List<E> source, List<E> target) {
		double sourceLen = source.size();
		double targetLen = target.size();
		double maxDistance = 0d;

		if (targetLen == sourceLen) {
			maxDistance = sourceLen * weightReplace;
		}
		if (targetLen > sourceLen) {
			maxDistance = sourceLen * weightReplace;
			maxDistance += (targetLen - sourceLen) * weightInsert;
		}
		if (targetLen < sourceLen) {
			maxDistance = targetLen * weightReplace;
			maxDistance += (sourceLen - targetLen) * weightDelete;
		}
		return maxDistance;
	}
}
