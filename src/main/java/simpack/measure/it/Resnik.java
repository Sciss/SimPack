
/*
 * $Id: Resnik.java 757 2008-04-17 17:42:53Z kiefer $
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
 * measure proposed by P. Resnik in "Using Information Content to Evaluate
 * Semantic Similarity in a Taxonomy" (1995)
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Resnik extends AbstractSimilarityMeasure {

	static Logger logger = LogManager.getLogger(Resnik.class);

	private double N, numCommons;

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
	 */
	public Resnik(double N, double numCommons) {
		this.N = N;
		this.numCommons = numCommons;
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
	public Resnik(IGraphAccessor graphAccessor, IGraphNode x, IGraphNode y)
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
		setCalculated(false);

		if (graphAccessor == null) {
			similarity = new Double(-1d * Math.log(numCommons / N));
			setCalculated(true);
			return true;
			// } else if (x.equals(y)) {
			// similarity = new Double(-1d
			// * Math.log(new Double(1d / Integer.MAX_VALUE) /
			// Integer.MAX_VALUE));
			// setCalculated(true);
			// return true;
		} else {
			IGraphNode mrca = null;
			try {
				mrca = graphAccessor.getMostRecentCommonAncestor(x, y);
				if (logger.isDebugEnabled()) {
					System.out.println("MRCA of " + x.toString() + " and "
							+ y.toString() + " is " + mrca.toString());
				}

				double size = graphAccessor.size();
				double num = 0;

				num = graphAccessor.getSuccessors(mrca, false).size() + 1;

				double p = num / size;
				if (p == 0d) {
					similarity = new Double(0d);
				} else {
					similarity = new Double(-1d * Math.log(p));
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