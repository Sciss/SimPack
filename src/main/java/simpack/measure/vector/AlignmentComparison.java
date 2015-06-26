/*
 * $Id: AlignmentComparison.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.HashSet;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.accessor.string.StringAccessor;
import simpack.api.impl.AbstractFeatureVectorSimilarityMeasure;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.exception.InvalidVectorSizeException;
import simpack.measure.sequence.Levenshtein;
import simpack.util.Vector;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class AlignmentComparison extends AbstractSimilarityMeasure {

	private static final Logger logger = LogManager
			.getLogger(AlignmentComparison.class);

	private AbstractFeatureVectorSimilarityMeasure featureVector = new Cosine();

	private String textNodeComparator = "Levenshtein";

	private double thresholdText = 0.75;

	private Map<Object, Object> map1, map2;

	public AlignmentComparison(Map<Object, Object> map1,
			Map<Object, Object> map2, String textNodeComparator,
			double thresholdText) {
		this.map1 = map1;
		this.map2 = map2;
		this.textNodeComparator = textNodeComparator;
		this.thresholdText = thresholdText;
	}

	@Override
	public boolean calculate() {
		setCalculated(false);
		try {
			similarity = score();
			setCalculated(true);
			return true;
		} catch (InvalidVectorSizeException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Double score() throws InvalidVectorSizeException {
		// write all object names to a common set
		HashSet<Object> allNames = new HashSet<Object>();
		allNames.addAll(map1.keySet());
		allNames.addAll(map2.keySet());

		// create two vectors to hold the alignment
		Vector<Double> A = new Vector<Double>();
		Vector<Double> B = new Vector<Double>();

		for (Object obj : allNames) {
			// obj is contained in both maps
			if (map1.containsKey(obj) && map2.containsKey(obj)) {
				// get values
				Object valueLeft = map1.get(obj);
				Object valueRight = map2.get(obj);
				// values exist
				if (valueLeft != null && valueRight != null) {
					// values can be compared
					boolean foundMatch = compareText(valueLeft, valueRight);
					if (foundMatch) {
						A.add(1.0);
						B.add(1.0);
						A.add(2.0);
						B.add(2.0);
						// values cannot be compared
					} else {
						A.add(1.0);
						B.add(1.0);
						A.add(-0.5);
						B.add(0.5);
					}
					// values do not exist
				} else {
					A.add(1.0);
					B.add(1.0);
					// TODO: maybe use differnt penalties???
					A.add(-0.5);
					B.add(0.5);
				}
				// obj is only contained in one map
			} else if (map1.containsKey(obj)) {
				A.add(1.0);
				B.add(0.0);
			} else if (map2.containsKey(obj)) {
				A.add(0.0);
				B.add(1.0);
			}
		}

		featureVector.setVector(A, B);

		if (logger.isDebugEnabled()) {
			for (int i = 0; i < A.size(); i++) {
				System.out.println(A.get(i) + " " + B.get(i));
			}
		}

		return featureVector.getSimilarity();
	}

	private boolean compareText(Object textLeft, Object textRight) {
		String str1 = textLeft.toString();
		String str2 = textRight.toString();

		if (textNodeComparator.equals("EQUAL")) {
			if (str1.equalsIgnoreCase(str2)) {
				return true;
			} else {
				return false;
			}
		} else if (textNodeComparator.equals("Levenshtein")) {
			Levenshtein levenshtein = new Levenshtein<String>(
					new StringAccessor(str1), new StringAccessor(str2));
			double similarity = levenshtein.getSimilarity();
			if (similarity >= thresholdText) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
