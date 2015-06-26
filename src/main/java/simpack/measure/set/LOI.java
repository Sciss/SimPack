/*
 * $Id: LOI.java 757 2008-04-17 17:42:53Z kiefer $
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
 * Implementation of the LOI (loss of information) based similarity measure
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class LOI extends AbstractSimilarityMeasure {

	private Set<? extends Object> s1, s2;

	public LOI(Set<? extends Object> s1, Set<? extends Object> s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	public boolean calculate() {
		setCalculated(false);
		double overall = s1.size() + s2.size();
		if (overall == 0d) {
			similarity = new Double(0d);
		} else {
			HashSet<? extends Object> s3 = new HashSet<Object>(s1);
			s1.removeAll(new HashSet<Object>(s2));
			s2.removeAll(new HashSet<Object>(s3));
			double s1_not_s2 = s1.size();
			double s2_not_s1 = s2.size();
			double loi = (s1_not_s2 + s2_not_s1) / overall;
			similarity = new Double(1d - loi);
		}
		setCalculated(true);
		return true;
	}
}