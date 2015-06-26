/*
 * $Id: ConstructedValienteTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.accessor.graph.SimpleGraphAccessor;
import simpack.api.IGraphAccessor;
import simpack.measure.graph.MaxCommonSubgraphIsoValiente;
import simpack.util.graph.GraphNode;

/**
 * @author Christoph Kiefer
 * @version $Id: ConstructedValienteTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConstructedValienteTest extends TestCase {

	private IGraphAccessor graph1, graph2;

	private GraphNode n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13,
			n14, n15, n16, n17;

	private MaxCommonSubgraphIsoValiente calc1;

	protected void setUp() throws Exception {
		graph1 = generateSampleG1();
		graph2 = generateSampleG2();
		assertNotNull(graph1);
		assertNotNull(graph2);
	}

	public void testSampleGraph() {
		calc1 = new MaxCommonSubgraphIsoValiente(graph1, graph2,
				MaxCommonSubgraphIsoValiente.DEFAULT_MIN_CLIQUE_SIZE,
				MaxCommonSubgraphIsoValiente.DEFAULT_STRUCTURE_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_LABEL_WEIGHT,
				MaxCommonSubgraphIsoValiente.DEFAULT_DENOMINATOR);
		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());
		assertEquals(calc1.getSimilarity(), 0.5d);
	}

	private IGraphAccessor generateSampleG1() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		n1 = new GraphNode("1");
		n2 = new GraphNode("2");
		n3 = new GraphNode("3");
		n4 = new GraphNode("4");
		n5 = new GraphNode("5");
		n6 = new GraphNode("6");
		n7 = new GraphNode("7");
		n8 = new GraphNode("8");
		n9 = new GraphNode("9");
		n10 = new GraphNode("10");
		n11 = new GraphNode("11");
		n12 = new GraphNode("12");
		n13 = new GraphNode("13");
		n14 = new GraphNode("14");
		n15 = new GraphNode("15");
		n16 = new GraphNode("16");
		n17 = new GraphNode("17");

		g1.setEdge(n1, n2);
		g1.setEdge(n1, n3);
		g1.setEdge(n2, n4);
		g1.setEdge(n2, n5);
		g1.setEdge(n2, n6);
		g1.setEdge(n3, n6);
		g1.setEdge(n3, n7);
		g1.setEdge(n4, n8);
		g1.setEdge(n6, n9);
		g1.setEdge(n7, n10);
		g1.setEdge(n7, n11);
		g1.setEdge(n7, n12);
		g1.setEdge(n8, n13);
		g1.setEdge(n9, n13);
		g1.setEdge(n9, n14);
		g1.setEdge(n9, n15);
		g1.setEdge(n10, n15);
		g1.setEdge(n10, n16);
		g1.setEdge(n11, n16);
		g1.setEdge(n12, n17);
		g1.setEdge(n17, n16);

		return g1;
	}

	private IGraphAccessor generateSampleG2() {
		SimpleGraphAccessor g2 = new SimpleGraphAccessor();

		n1 = new GraphNode("1");
		n2 = new GraphNode("5");
		n3 = new GraphNode("3");
		n4 = new GraphNode("10");
		n5 = new GraphNode("11");
		n6 = new GraphNode("12");
		n7 = new GraphNode("2");
		n8 = new GraphNode("17");
		n9 = new GraphNode("8");
		n10 = new GraphNode("4");
		n11 = new GraphNode("9");

		g2.setEdge(n1, n2);
		g2.setEdge(n1, n3);
		g2.setEdge(n2, n4);
		g2.setEdge(n2, n5);
		g2.setEdge(n2, n6);
		g2.setEdge(n3, n6);
		g2.setEdge(n4, n10);
		g2.setEdge(n5, n10);
		g2.setEdge(n8, n10);
		g2.setEdge(n8, n11);
		g2.setEdge(n9, n11);
		g2.setEdge(n3, n7);
		g2.setEdge(n7, n9);
		g2.setEdge(n6, n8);

		return g2;
	}
}