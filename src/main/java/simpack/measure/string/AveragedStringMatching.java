/*
 * $Id: AveragedStringMatching.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.string;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.accessor.string.StringAccessor;
import simpack.api.ISequenceAccessor;
import simpack.api.impl.AbstractSequenceSimilarityMeasure;
import simpack.api.impl.AbstractStringSimilarityMeasure;
import simpack.exception.InvalidSimilarityMeasureNameException;
import simpack.measure.sequence.Levenshtein;

/**
 * This class implements the similarity measure proposed in "Measuring
 * Similarity Between Ontologies" by A. Maedche and S. Staab.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class AveragedStringMatching extends AbstractStringSimilarityMeasure {

	static Logger logger = LogManager.getLogger(AveragedStringMatching.class);

	/**
	 * Constructor.
	 * <p>
	 * Takes two sequence accessors as inputs and measures similarity of each of
	 * the elements of the sequences by the use of an inner similarity measure,
	 * in the case, the levenshtein edit distance measure.
	 * 
	 * @param sa1
	 *            the first sequence
	 * @param sa2
	 *            the second sequence
	 */
	public AveragedStringMatching(ISequenceAccessor<String> sa1,
			ISequenceAccessor<String> sa2)
			throws InvalidSimilarityMeasureNameException {
		super(sa1, sa2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		double sum = 0d;
		List<String> seq1 = accessor1.getSequence();

		for (String str1 : seq1) {
			double max = Double.NEGATIVE_INFINITY;
			List<String> seq2 = accessor2.getSequence();

			for (String str2 : seq2) {
				AbstractSequenceSimilarityMeasure m = null;
				double s = 0d;
				m = new Levenshtein<String>(new StringAccessor(str1),
						new StringAccessor(str2));
				s = m.getSimilarity();
				logger.debug(str1 + " <-> " + str2 + " = " + s);
				if (s >= max) {
					max = s;
				}
			}
			sum += max;
		}

		double asm = sum / seq1.size();
		similarity = new Double(asm);
		setCalculated(true);
		return true;
	}
}