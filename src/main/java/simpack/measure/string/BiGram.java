/*
 * $Id: BiGram.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.ISequenceAccessor;
import simpack.api.impl.AbstractStringSimilarityMeasure;

/**
 * @author Christoph Kiefer
 * @version $Id: BiGram.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class BiGram extends AbstractStringSimilarityMeasure {

	public static Logger logger = LogManager.getLogger(BiGram.class);

	public BiGram(ISequenceAccessor<String> leftAccessor,
			ISequenceAccessor<String> rightAccessor) {
		super(leftAccessor, rightAccessor);
	}

	public boolean calculate() {
		Vector<String> pairs1 = pairs(accessor1.toString());
		Vector<String> pairs2 = pairs(accessor2.toString());

		double union = pairs1.size() + pairs2.size();
		// logger.debug("Union XS = " + union);
		double overlap = getOverlap(pairs1, pairs2);
		// logger.debug("Intersection XS = " + overlap);
		similarity = 2d * overlap / union;

		return true;
	}

	private Vector<String> pairs(String fullString) {
		Vector<String> pairs = new Vector<String>();
		for (int i = 0; i < fullString.length() - 1; i++) {
			pairs.add(fullString.substring(i, i + 2));
		}
		return pairs;
	}

	private double getOverlap(Vector<String> a, Vector<String> b) {
		double overlap = 0d;
		for (String s1 : a) {
			for (String s2 : b) {
				if (s1.equals(s2)) {
					overlap++;
				}
			}
		}
		return overlap;
	}

}
