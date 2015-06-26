/*
 * $Id: MaxGraphIsoCoveringValienteTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.graph;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import simpack.accessor.graph.SimpleGraphAccessor;
import simpack.api.IGraphAccessor;
import simpack.measure.graph.MaxGraphIsoCoveringValiente;
import simpack.util.graph.Clique;
import simpack.util.graph.GraphNode;

/**
 * @author Christoph Kiefer
 * @version $Id: MaxGraphIsoCoveringValienteTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class MaxGraphIsoCoveringValienteTest extends TestCase {

	public static Logger logger = Logger
			.getLogger(MaxGraphIsoCoveringValienteTest.class);

	private IGraphAccessor graph1, graph2;

	private MaxGraphIsoCoveringValiente calc1;

	protected void setUp() throws Exception {
		graph1 = generateSampleG1();
		graph2 = generateSampleG2();
		assertNotNull(graph1);
		assertNotNull(graph2);
	}

	public void testSampleGraph() {
		calc1 = new MaxGraphIsoCoveringValiente(graph1, graph2, 1,
				MaxGraphIsoCoveringValiente.DEFAULT_STRUCTURE_WEIGHT,
				MaxGraphIsoCoveringValiente.DEFAULT_LABEL_WEIGHT,
				MaxGraphIsoCoveringValiente.DEFAULT_DENOMINATOR,
				MaxGraphIsoCoveringValiente.DEFAULT_GRAPH_TO_COVER,
				MaxGraphIsoCoveringValiente.DEFAULT_COVERAGE_STYLE);

		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());

		if (logger.isDebugEnabled()) {
			for (Clique c : calc1.getCovering()) {
				System.out.println("Covering " + c.getClique().toString() + " "
						+ c.getSimilarity());
			}
		}

		assertEquals(calc1.getSimilarity(), (-3d / graph1.size()) + 1d);
	}

	public void test2SampleGraph() {
		calc1 = new MaxGraphIsoCoveringValiente(graph1, graph2, 1,
				MaxGraphIsoCoveringValiente.DEFAULT_STRUCTURE_WEIGHT,
				MaxGraphIsoCoveringValiente.DEFAULT_LABEL_WEIGHT,
				MaxGraphIsoCoveringValiente.DEFAULT_DENOMINATOR,
				MaxGraphIsoCoveringValiente.DEFAULT_GRAPH_TO_COVER, true);

		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());

		if (logger.isDebugEnabled()) {
			for (Clique c : calc1.getCovering()) {
				System.out.println("Both Covering " + c.getClique().toString()
						+ " " + c.getSimilarity());
			}
		}

		assertEquals(calc1.getSimilarity(), (-2d / graph1.size()) + 1d);
	}

	private IGraphAccessor generateSampleG1() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("1");
		GraphNode n2 = new GraphNode("2");
		GraphNode n3 = new GraphNode("3");
		GraphNode n4 = new GraphNode("4");
		GraphNode n5 = new GraphNode("5");
		GraphNode n6 = new GraphNode("6");

		g1.setEdge(n1, n2);
		g1.setEdge(n1, n3);
		g1.setEdge(n2, n4);
		g1.setEdge(n3, n4);
		g1.setEdge(n4, n5);
		g1.setEdge(n4, n6);
		g1.setEdge(n5, n6);

		return g1;
	}

	private IGraphAccessor generateSampleG2() {
		SimpleGraphAccessor g2 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("1");
		GraphNode n2 = new GraphNode("2");
		GraphNode n3 = new GraphNode("3");
		GraphNode n4 = new GraphNode("4");

		g2.setEdge(n1, n2);
		g2.setEdge(n1, n3);
		g2.setEdge(n3, n4);

		return g2;
	}
}