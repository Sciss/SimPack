/*
 * $Id: AbstractSimAggregator.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import java.util.ArrayList;
import java.util.List;

import simpack.api.ISimilarityAggregator;

/**
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractSimAggregator implements ISimilarityAggregator {

	protected List<Double> scores = new ArrayList<Double>();

	public String explainScore(List<Double> scores) {
		String str = getName() + ": ";
		for (int i = 0; i < scores.size() - 1; i++) {
			str += scores.get(i) + " , ";
		}
		str += scores.get(scores.size() - 1);
		return str + ", sim = " + aggregate(scores);
	}

	public void clear() {
		scores.clear();
	}
	
	public void add(Double d) {
		scores.add(d);
	}
}
