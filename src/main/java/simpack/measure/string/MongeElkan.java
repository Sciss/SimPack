/*
 * $Id: MongeElkan.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.api.IToken;
import simpack.api.impl.AbstractStringSimilarityMeasure;
import simpack.measure.sequence.Levenshtein;
import simpack.tokenizer.SplittedStringTokenizer;

/**
 * @author Christoph Kiefer
 * @version $Id: MongeElkan.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class MongeElkan extends AbstractStringSimilarityMeasure {

	public static Logger logger = LogManager.getLogger(MongeElkan.class);

	public MongeElkan(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		super(leftAccessor, rightAccessor);
	}

	public boolean calculate() {
		// split the strings into tokens for comparison
		List<IToken> str1Tokens = new SplittedStringTokenizer("\\s+")
				.tokenize(accessor1.toString());
		List<IToken> str2Tokens = new SplittedStringTokenizer("\\s+")
				.tokenize(accessor2.toString());

		double sumMatches = 0d;
		double maxFound;
		for (IToken str1Token : str1Tokens) {
			maxFound = 0d;
			for (IToken str2Token : str2Tokens) {

				double found = new Levenshtein<String>(new StringAccessor(
						str1Token.getValue()), new StringAccessor(str2Token
						.getValue())).getSimilarity();

				if (found > maxFound) {
					maxFound = found;
				}
			}
			sumMatches += maxFound;
		}

		similarity = sumMatches / (double) str1Tokens.size();
		return true;
	}
}
