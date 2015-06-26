/*
 * $Id: MedianAggregator.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.aggregator;

import java.util.Collections;
import java.util.List;

import simpack.api.impl.AbstractSimAggregator;

/**
 * @author Christoph Kiefer
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class MedianAggregator extends AbstractSimAggregator {

	public String getName() {
		return "MedianAgg";
	}

	public double aggregate(List<Double> scores) {
		return median(scores);
	}

	public double aggregateSoFar(double d) {
		add(d);
		return aggregate(scores);
	}

	private Double median(List<Double> scores) {
		Collections.sort(scores);

		int s = scores.size();
		if ((s % 2) == 0) {
			// even number of elements
			// median is the mean of the two middle numbers
			// but we use the Untermedian
			int index = (s / 2) - 1;
			// Obermedian
			// int index = (s / 2);
			double median = scores.get(index);
			return median;
		} else {
			// odd number of elements
			// median is simply the middle number
			int index = s / 2;
			double median = scores.get(index);
			return median;
		}
	}
}
