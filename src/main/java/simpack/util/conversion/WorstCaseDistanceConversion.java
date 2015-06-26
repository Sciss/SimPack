/*
 * $Id: WorstCaseDistanceConversion.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.conversion;

import simpack.api.impl.AbstractDistanceConversion;

/**
 * This class implements a variation of the common distance conversion, such
 * that it takes into account a worst case distance between two entities
 * (strings, sequences, etc).
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class WorstCaseDistanceConversion extends AbstractDistanceConversion {

	private double worstCaseDistance;

	/**
	 * Constructor.
	 */
	public WorstCaseDistanceConversion() {
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes the worst case distance between two entities (strings, set, etc.)
	 * to be used to convert the distance between the entities to a similarity
	 * value.
	 * 
	 * @param worstCaseDistance
	 *            worst case distance used to convert distance to similarity
	 */
	public WorstCaseDistanceConversion(double worstCaseDistance) {
		super();
		this.worstCaseDistance = worstCaseDistance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.IDistanceConversion#convert(double)
	 */
	public double convert(double d) {
		if (worstCaseDistance != 0d) {
			return (worstCaseDistance - d) / worstCaseDistance;
		} else {
			return 0d;
		}
	}

	/**
	 * @return Returns the worstCaseDistance.
	 */
	public double getWorstCaseDistance() {
		return worstCaseDistance;
	}

	/**
	 * @param worstCaseDistance
	 *            The worstCaseDistance to set.
	 */
	public void setWorstCaseDistance(double worstCaseDistance) {
		this.worstCaseDistance = worstCaseDistance;
	}

}
