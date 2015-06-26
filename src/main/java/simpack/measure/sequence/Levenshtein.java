/*
 * $Id: Levenshtein.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.api.impl.AbstractSequenceSimilarityMeasure;
import simpack.exception.WeightBalanceException;
import simpack.util.conversion.WorstCaseDistanceConversion;

/**
 * This class implements a similarity measure specified by the Levenshtein
 * distance (edit distance) of two objects.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Levenshtein<E> extends AbstractSequenceSimilarityMeasure<E> {

	private static final Logger logger = LogManager.getLogger(Levenshtein.class);

	/**
	 * Default weight of insert operation.
	 */
	public static double DEFAULT_WEIGHT_INSERT = 1d;

	/**
	 * Default weight of delete operation.
	 */
	public static double DEFAULT_WEIGHT_DELETE = 1d;

	/**
	 * Default weight of replace operation if object are non-equal.
	 */
	public static double DEFAULT_WEIGHT_REPLACE = 1d;

	/**
	 * Default weight of replace operation if objects are equal.
	 */
	public static double DEFAULT_WEIGHT_REPLACE_EQUAL = 0d;

	/**
	 * Default weight of insert operation.
	 */
	protected double weightInsert = DEFAULT_WEIGHT_INSERT;

	/**
	 * Default weight of delete operation.
	 */
	protected double weightDelete = DEFAULT_WEIGHT_DELETE;

	/**
	 * Default weight of replace operation if objects are non-equal.
	 */
	protected double weightReplace = DEFAULT_WEIGHT_REPLACE;

	/**
	 * Default weight of replace operation if object are equal.
	 */
	protected double weightReplaceEqual = DEFAULT_WEIGHT_REPLACE_EQUAL;

	/**
	 * Conversion function to convert edit distance to similarity.
	 */
	protected IDistanceConversion conversion;

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
	public Levenshtein(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL, new WorstCaseDistanceConversion());
	}

	public Levenshtein(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2,
			IDistanceConversion conversion) {
		this(sequenceAccessor1, sequenceAccessor2, DEFAULT_WEIGHT_INSERT,
				DEFAULT_WEIGHT_DELETE, DEFAULT_WEIGHT_REPLACE,
				DEFAULT_WEIGHT_REPLACE_EQUAL, conversion);
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
	 */
	public Levenshtein(ISequenceAccessor<E> sequenceAccessor1,
			ISequenceAccessor<E> sequenceAccessor2, double weightInsert,
			double weightDelete, double weightReplace,
			double weightReplaceEqual, IDistanceConversion conversion) {

		super(sequenceAccessor1, sequenceAccessor2);

		if ((weightInsert + weightDelete < weightReplace)
				|| (weightInsert + weightDelete < weightReplaceEqual)) {
			throw new WeightBalanceException(
					"c(weightInsert) + c(weightDelete) <= c(weightReplace)");
		}

		this.weightInsert = weightInsert;
		this.weightDelete = weightDelete;
		this.weightReplace = weightReplace;
		this.weightReplaceEqual = weightReplaceEqual;
		this.conversion = conversion;
	}

	public boolean calculate() {
		setCalculated(false);

		List<E> sequenceSrc = sequenceAccessor1.getSequence();
		List<E> sequenceTgt = sequenceAccessor2.getSequence();

		logger.debug(sequenceSrc.toString());
		logger.debug(sequenceTgt.toString());

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

				if (!sequenceSrc.get(zeile - 1).equals(
						sequenceTgt.get(spalte - 1))) {
					valueFromDiag += weightReplace;
				} else {
					valueFromDiag += weightReplaceEqual;
				}
				matrix[zeile][spalte] = Math.min(valueFromLeft, Math.min(
						valueFromDiag, valueFromTop));
			}
		}

		double dist = matrix[sequenceSrc.size()][sequenceTgt.size()];
		double wDist = 0d;
		if (conversion instanceof WorstCaseDistanceConversion) {
			wDist = calculateWorstCaseDistance(sequenceAccessor1.toString(),
					sequenceAccessor2.toString());
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

	protected double calculateWorstCaseDistance(String source, String target) {
		double sourceLen = source.length();
		double targetLen = target.length();
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

	public static void main(String[] args) {
		Levenshtein<String> l = null;
		try {
			l = new Levenshtein<String>(new StringAccessor("Levenshtein"),
					new StringAccessor("shteinLeven"));
			System.out.println(l.getSimilarity());
		} catch (WeightBalanceException e) {
			e.printStackTrace();
		}
	}
}
