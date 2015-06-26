/*
 * $Id: ConstructedGraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.graph.GraphIsomorphism;
import simpack.util.graph.GraphNode;

/**
 * @author Christoph Kiefer
 * @version $Id: ConstructedGraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConstructedGraphIsomorphismTest extends TestCase {

	private IGraphAccessor graph1, graph2a, graph2b, graph2c, graph3;

	private GraphNode n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13,
			n14, n15, n16, n17;

	private GraphIsomorphism calc1, calc2, calc3, calc4;

	public void setUp() {
		graph1 = generateSampleG1();
		graph2a = generateSampleG3("normal");
		graph2b = generateSampleG3("rename");
		graph2c = generateSampleG3("remove_node");
		graph3 = generateSampleG2("normal");
		calc1 = new GraphIsomorphism(graph1, graph2a);
		calc2 = new GraphIsomorphism(graph1, graph2b);
		calc3 = new GraphIsomorphism(graph1, graph2c);
		calc4 = new GraphIsomorphism(graph1, graph3);

		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());
		assertTrue(calc2.calculate());
		assertTrue(calc2.isCalculated());
		assertTrue(calc3.calculate());
		assertTrue(calc3.isCalculated());
		assertTrue(calc4.calculate());
		assertTrue(calc4.isCalculated());
	}

	public void testSameGraph() {
		assertNotNull(graph1);
		GraphIsomorphism g = new GraphIsomorphism(graph1, graph1);
		assertEquals(g.getSimilarity(), 1d);
	}

	public void testSampleGraph() {

		assertNotNull(graph1);
		assertNotNull(graph2a);
		assertNotNull(graph2b);
		assertNotNull(graph2c);
		assertNotNull(graph3);

		assertEquals(calc1.getSimilarity(), 1d);
		assertEquals(calc2.getSimilarity(), 0.9705882352941176);
		assertEquals(calc3.getSimilarity(), 0d);
		assertEquals(calc4.getSimilarity(), 0d);
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

		n2.addPredecessor(n1);
		n3.addPredecessor(n1);
		n4.addPredecessor(n2);
		n5.addPredecessor(n2);
		n6.addPredecessor(n2);
		n6.addPredecessor(n3);
		n7.addPredecessor(n3);
		n8.addPredecessor(n4);
		n9.addPredecessor(n6);
		n10.addPredecessor(n7);
		n11.addPredecessor(n7);
		n12.addPredecessor(n7);
		n13.addPredecessor(n8);
		n13.addPredecessor(n9);
		n14.addPredecessor(n9);
		n15.addPredecessor(n9);
		n15.addPredecessor(n10);
		n16.addPredecessor(n10);
		n16.addPredecessor(n11);
		n17.addPredecessor(n12);
		n16.addPredecessor(n17);

		n1.addSuccessor(n2);
		n1.addSuccessor(n3);
		n2.addSuccessor(n4);
		n2.addSuccessor(n5);
		n2.addSuccessor(n6);
		n3.addSuccessor(n6);
		n3.addSuccessor(n7);
		n4.addSuccessor(n8);
		n6.addSuccessor(n9);
		n7.addSuccessor(n10);
		n7.addSuccessor(n11);
		n7.addSuccessor(n12);
		n8.addSuccessor(n13);
		n9.addSuccessor(n13);
		n9.addSuccessor(n14);
		n9.addSuccessor(n15);
		n10.addSuccessor(n15);
		n10.addSuccessor(n16);
		n11.addSuccessor(n16);
		n12.addSuccessor(n17);
		n17.addSuccessor(n16);

		g1.addNode(n1);
		g1.addNode(n2);
		g1.addNode(n3);
		g1.addNode(n4);
		g1.addNode(n5);
		g1.addNode(n6);
		g1.addNode(n7);
		g1.addNode(n8);
		g1.addNode(n9);
		g1.addNode(n10);
		g1.addNode(n11);
		g1.addNode(n12);
		g1.addNode(n13);
		g1.addNode(n14);
		g1.addNode(n15);
		g1.addNode(n16);
		g1.addNode(n17);

		// System.out.println(g1);
		return g1;
	}

	private IGraphAccessor generateSampleG2(String test) {
		SimpleGraphAccessor g2 = new SimpleGraphAccessor();

		n1 = new GraphNode("1");
		n2 = new GraphNode("5");
		n3 = new GraphNode("3");
		n4 = new GraphNode("10");
		n5 = new GraphNode("11");
		n6 = new GraphNode("12");
		n8 = new GraphNode("17");
		n9 = new GraphNode("8");
		if (test.equals("rename") || test.equals("remove_node")
				|| test.equals("remove_edge")) {
			n10 = new GraphNode("16");
		} else
			n10 = new GraphNode("4");
		n11 = new GraphNode("9");

		if (!test.equals("remove_node") && !test.equals("remove_edge")) {
			n7 = new GraphNode("2");
			g2.addNode(n7);
			n7.addPredecessor(n3);
			n9.addPredecessor(n7);
			n3.addSuccessor(n7);
			n7.addSuccessor(n9);
		}
		if (!test.equals("remove_edge")) {
			n8.addPredecessor(n6);
			n6.addSuccessor(n8);
		}

		g2.addNode(n1);
		g2.addNode(n2);
		g2.addNode(n3);
		g2.addNode(n4);
		g2.addNode(n5);
		g2.addNode(n6);
		g2.addNode(n8);
		g2.addNode(n9);
		g2.addNode(n10);
		g2.addNode(n11);

		n2.addPredecessor(n1);
		n3.addPredecessor(n1);
		n4.addPredecessor(n2);
		n5.addPredecessor(n2);
		n6.addPredecessor(n2);
		n6.addPredecessor(n3);

		n10.addPredecessor(n4);
		n10.addPredecessor(n5);

		n10.addPredecessor(n8);
		n11.addPredecessor(n8);
		n11.addPredecessor(n9);

		n1.addSuccessor(n2);
		n1.addSuccessor(n3);
		n2.addSuccessor(n4);
		n2.addSuccessor(n5);
		n2.addSuccessor(n6);
		n3.addSuccessor(n6);

		n4.addSuccessor(n10);
		n5.addSuccessor(n10);

		n8.addSuccessor(n10);
		n8.addSuccessor(n11);
		n9.addSuccessor(n11);

		return g2;
	}

	private IGraphAccessor generateSampleG3(String test) {
		SimpleGraphAccessor g3 = new SimpleGraphAccessor();

		n1 = new GraphNode("1");
		n2 = new GraphNode("2");
		n3 = new GraphNode("3");
		n4 = new GraphNode("4");
		n5 = new GraphNode("5");
		n7 = new GraphNode("7");
		n8 = new GraphNode("8");
		n9 = new GraphNode("9");
		n10 = new GraphNode("10");
		n11 = new GraphNode("11");
		n13 = new GraphNode("13");
		n14 = new GraphNode("14");
		n15 = new GraphNode("15");
		n16 = new GraphNode("16");
		n17 = new GraphNode("17");
		if (test.equals("rename") || test.equals("remove_node")
				|| test.equals("remove_edge")) {
			n12 = new GraphNode("18");
		} else
			n12 = new GraphNode("12");

		if (!test.equals("remove_node") && !test.equals("remove_edge")) {
			n6 = new GraphNode("6");
			g3.addNode(n6);
			n6.addPredecessor(n2);
			n6.addPredecessor(n3);
			n9.addPredecessor(n6);
			n2.addSuccessor(n6);
			n3.addSuccessor(n6);
			n6.addSuccessor(n9);
		}
		if (!test.equals("remove_edge")) {
			n7.addPredecessor(n3);
			n3.addSuccessor(n7);
		}

		n2.addPredecessor(n1);
		n3.addPredecessor(n1);
		n4.addPredecessor(n2);
		n5.addPredecessor(n2);

		n8.addPredecessor(n4);
		n10.addPredecessor(n7);
		n11.addPredecessor(n7);
		n12.addPredecessor(n7);
		n13.addPredecessor(n8);
		n13.addPredecessor(n9);
		n14.addPredecessor(n9);
		n15.addPredecessor(n9);
		n15.addPredecessor(n10);
		n16.addPredecessor(n10);
		n16.addPredecessor(n11);
		n17.addPredecessor(n12);
		n16.addPredecessor(n17);

		n1.addSuccessor(n2);
		n1.addSuccessor(n3);
		n2.addSuccessor(n4);
		n2.addSuccessor(n5);

		n4.addSuccessor(n8);
		n7.addSuccessor(n10);
		n7.addSuccessor(n11);
		n7.addSuccessor(n12);
		n8.addSuccessor(n13);
		n9.addSuccessor(n13);
		n9.addSuccessor(n14);
		n9.addSuccessor(n15);
		n10.addSuccessor(n15);
		n10.addSuccessor(n16);
		n11.addSuccessor(n16);
		n12.addSuccessor(n17);
		n17.addSuccessor(n16);

		g3.addNode(n1);
		g3.addNode(n2);
		g3.addNode(n3);
		g3.addNode(n4);
		g3.addNode(n5);
		g3.addNode(n7);
		g3.addNode(n8);
		g3.addNode(n9);
		g3.addNode(n10);
		g3.addNode(n11);
		g3.addNode(n12);
		g3.addNode(n13);
		g3.addNode(n14);
		g3.addNode(n15);
		g3.addNode(n16);
		g3.addNode(n17);

		return g3;
	}
}
