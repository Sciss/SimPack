/*
 * $Id: Lin.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.it;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.exception.InvalidElementException;

/**
 * This class implements the information theoretic approach of a similarity
 * measure proposed by D. Lin in "An Information-Theoretic Definition of
 * Similarity" (1998)
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Lin extends AbstractSimilarityMeasure {

	static Logger logger = LogManager.getLogger(Lin.class);

	private double N, numCommons, numElemX, numElemY;

	private IGraphAccessor graphAccessor;

	private IGraphNode x, y;

	/**
	 * Constructor.
	 * <p>
	 * All necessary elements for the similarity computation of two elements x
	 * and y (in two sets or other data structure) are directly passed to the
	 * measure.
	 * 
	 * @param N
	 *            total number of elements in reference set/structure/tree/graph
	 * @param numCommons
	 *            number of common/shared elements between two elements x and y
	 * @param numElemX
	 *            number of occurrence of element x
	 * @param numElemY
	 *            number of occurrence of element y
	 */
	public Lin(double N, double numCommons, double numElemX, double numElemY) {
		this.N = N;
		this.numCommons = numCommons;
		this.numElemX = numElemX;
		this.numElemY = numElemY;
	}

	/**
	 * Constructor.
	 * <p>
	 * A tree accessor and two tree nodes of the tree the accessor provides
	 * access to are passed. The similarity is to be computed between tree nodes
	 * <code>x</code> and <code>y</code>.
	 * 
	 * @param graphAccessor
	 *            tree accessor to access the corresponding tree
	 * @param x
	 *            first tree node
	 * @param y
	 *            second tree node
	 * @throws InvalidElementException
	 *             thrown if nodes are not in tree
	 */
	public Lin(IGraphAccessor graphAccessor, IGraphNode x, IGraphNode y)
			throws InvalidElementException {
		this.graphAccessor = graphAccessor;
		this.x = x;
		this.y = y;

		if (!graphAccessor.contains(x)) {
			throw new InvalidElementException(x.toString() + " not in set");
		}

		if (!graphAccessor.contains(y)) {
			throw new InvalidElementException(y.toString() + " not in set");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		double p_x = 0d, p_y = 0d, p_z = 0d;
		setCalculated(false);

		// calculate with given numbers
		if (graphAccessor == null) {
			p_z = numCommons / N;
			p_x = numElemX / N;
			p_y = numElemY / N;
			similarity = new Double((2d * Math.log(p_z))
					/ (Math.log(p_x) + Math.log(p_y)));
			setCalculated(true);
			return true;
		} else {
			try {
				double size = graphAccessor.size();

				int nX = graphAccessor.getSuccessors(x, false).size() + 1;
				p_x = nX / new Double(size);
				int nY = graphAccessor.getSuccessors(y, false).size() + 1;
				p_y = nY / new Double(size);

				IGraphNode mrca = graphAccessor.getMostRecentCommonAncestor(x,
						y);

				int nXY = graphAccessor.getSuccessors(mrca, false).size() + 1;

				p_z = nXY / new Double(size);

				if (p_z == 1d) {
					similarity = new Double(0d);
				} else {
					similarity = new Double((2d * Math.log(p_z))
							/ (Math.log(p_x) + Math.log(p_y)));
				}

				setCalculated(true);
				return true;

			} catch (InvalidElementException ex) {
				ex.printStackTrace();
				return false;
			}
		}
	}
}