/*
 * $Id: ShortestPath.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.IDistanceConversion;
import simpack.api.IGraphAccessor;
import simpack.api.IGraphNode;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.exception.InvalidElementException;

/**
 * This class can be used to compute the length of the shortest path between
 * nodes in a graph, or alternatively, to get a similarity value depending on
 * the length of the shortest path by passing an approriate distance to
 * similarity conversion function.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ShortestPath extends AbstractSimilarityMeasure {

	static Logger logger = LogManager.getLogger(ShortestPath.class);

	private IGraphAccessor graphAccessor;

	private IGraphNode nodeA, nodeB;

	private IDistanceConversion conversion;

	/**
	 * Constructor.
	 * <p>
	 * Takes a graph accessor and two nodes of the graph. The accessor is used
	 * to access the graph structur of the graph nodes.
	 * 
	 * @param accessor
	 *            graph accessor
	 * @param nodeA
	 *            the first node
	 * @param nodeB
	 *            the second node
	 * @throws InvalidElementException
	 *             an exception is thrown if the nodes are not valid, i.e., if
	 *             they do not occur in the graph
	 */
	public ShortestPath(IGraphAccessor accessor, IGraphNode nodeA,
			IGraphNode nodeB) throws InvalidElementException {
		this.graphAccessor = accessor;
		this.nodeA = nodeA;
		this.nodeB = nodeB;

		if (!accessor.contains(nodeA)) {
			throw new InvalidElementException(nodeA.toString()
					+ " not in graph");
		}

		if (!accessor.contains(nodeB)) {
			throw new InvalidElementException(nodeB.toString()
					+ " not in graph");
		}
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes a graph accessor and two nodes of the graph. The accessor is used
	 * to access the graph structur of the graph nodes. The conversion function
	 * converts the distance to a similarity value.
	 * 
	 * @param accessor
	 *            graph accessor
	 * @param nodeA
	 *            the first node
	 * @param nodeB
	 *            the second node
	 * @param conversion
	 *            a conversion function to convert length of the shortest path
	 *            to a similarity value
	 * @throws InvalidElementException
	 *             an exception is thrown if the nodes are not valid, i.e., if
	 *             they do not occur in the graph
	 */
	public ShortestPath(IGraphAccessor accessor, IGraphNode nodeA,
			IGraphNode nodeB, IDistanceConversion conversion)
			throws InvalidElementException {
		this.graphAccessor = accessor;
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.conversion = conversion;

		if (!accessor.contains(nodeA)) {
			throw new InvalidElementException(nodeA.toString()
					+ " not in graph");
		}

		if (!accessor.contains(nodeB)) {
			throw new InvalidElementException(nodeB.toString()
					+ " not in graph");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ICalculator#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		try {
			double distance = graphAccessor.getShortestPath(nodeA, nodeB);
			if (conversion != null) {
				similarity = new Double(conversion.convert(distance));
			} else {
				similarity = new Double(distance);
			}
			setCalculated(true);
			return true;
		} catch (InvalidElementException ex) {
			ex.printStackTrace();
			return false;
		}
	}
}