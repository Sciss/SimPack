/*
 * $Id: Jaro.java 757 2008-04-17 17:42:53Z kiefer $
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.ISequenceAccessor;
import simpack.api.impl.AbstractStringSimilarityMeasure;

/**
 * Jaro distance metric. From 'An Application of the Fellegi-Sunter Model of
 * Record Linkage to the 1990 U.S. Decennial Census' by William E. Winkler and
 * Yves Thibaudeau.
 * 
 * @author Silvan Hollenstein, Christoph Kiefer
 */

public class Jaro extends AbstractStringSimilarityMeasure {

	public static Logger logger = LogManager.getLogger(Jaro.class);

	public Jaro(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		super(leftAccessor, rightAccessor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		String str1 = accessor1.toString().toLowerCase();
		String str2 = accessor2.toString().toLowerCase();
		int halflen = halfLengthOfShorter(str1, str2);
		String common1 = commonChars(str1, str2, halflen);
		String common2 = commonChars(str2, str1, halflen);

		if (common1.length() != common2.length()) {
			logger.error("common1 != common2: '" + common1 + "' != '" + common2
					+ "'");
			similarity = new Double(-1d);
			setCalculated(true);
			return true;
		}
		if (common1.length() == 0 || common2.length() == 0) {
			similarity = new Double(0);
			setCalculated(true);
			return true;
		}

		int common = common1.length();
		int transpositions = transpositions(common1, common2);
		double dist = (common / ((double) str1.length()) + common
				/ ((double) str2.length()) + (common - transpositions)
				/ ((double) common)) / 3.0;
		similarity = new Double(dist);
		setCalculated(true);
		return true;
	}

	private int halfLengthOfShorter(String str1, String str2) {
		return (str1.length() > str2.length()) ? str2.length() / 2 + 1 : str1
				.length() / 2 + 1;
	}

	private String commonChars(String s, String t, int halflen) {
		StringBuilder common = new StringBuilder();
		StringBuilder copy = new StringBuilder(t);

		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			boolean foundIt = false;
			for (int j = Math.max(0, i - halflen); !foundIt
					&& j < Math.min(i + halflen, t.length()); j++) {
				if (copy.charAt(j) == ch) {
					foundIt = true;
					common.append(ch);
					copy.setCharAt(j, '*');
				}
			}
		}
		return common.toString();
	}

	private int transpositions(String common1, String common2) {
		int transpositions = 0;
		for (int i = 0; i < common1.length(); i++) {
			if (common1.charAt(i) != common2.charAt(i))
				transpositions++;
		}
		transpositions /= 2;
		return transpositions;
	}
}
