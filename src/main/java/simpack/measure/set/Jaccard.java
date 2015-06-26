/*
 * $Id: Jaccard.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.set;

import java.util.HashSet;
import java.util.Set;

import simpack.api.impl.AbstractSimilarityMeasure;

/**
 * Jaccard-based set similarity measure implementation. The Jaccard distance
 * between two sets is the ratio of the size of their intersection to the size
 * of their union.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Jaccard extends AbstractSimilarityMeasure {

	private Set<? extends Object> s1, s2;

	public Jaccard(Set<? extends Object> s1, Set<? extends Object> s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	public boolean calculate() {
		setCalculated(false);
		similarity = new Double(score(s1, s2));
		setCalculated(true);
		return true;
	}

	private double score(Set<? extends Object> s1, Set<? extends Object> s2) {
		if (!s1.isEmpty() && !s2.isEmpty()) {
			// intersection
			Set set = new HashSet();
			set.addAll(s1);
			set.retainAll(s2);
			double numCommon = set.size();
			return numCommon / (s1.size() + s2.size() - numCommon);
		} else {
			return 0d;
		}
	}
}
