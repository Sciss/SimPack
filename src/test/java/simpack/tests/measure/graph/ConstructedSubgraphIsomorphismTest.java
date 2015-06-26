/*
 * $Id: ConstructedSubgraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import simpack.accessor.graph.SimpleGraphAccessor;
import simpack.api.IGraphAccessor;
import simpack.measure.graph.SubgraphIsomorphism;
import simpack.util.graph.GraphNode;
import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: ConstructedSubgraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConstructedSubgraphIsomorphismTest extends TestCase {

	private IGraphAccessor graph1, graph2a, graph2b, graph2c, graph2d, graph3a,
			graph3b, graph3c, graph3d, graph4, graph5;

	private SubgraphIsomorphism calc1, calc2, calc3, calc4, calc5, calc6,
			calc7, calc8, calc9;

	protected void setUp() throws Exception {
		graph1 = generateSampleG1();
		graph2a = generateSampleG2("normal");
		graph2b = generateSampleG2("rename");
		graph2c = generateSampleG2("remove_node");
		graph2d = generateSampleG2("remove_edge");
		graph3a = generateSampleG3("normal");
		graph3b = generateSampleG3("rename");
		graph3c = generateSampleG3("remove_node");
		graph3d = generateSampleG3("remove_edge");
		graph4 = generateSampleG4();
		graph5 = generateSampleG5();
		
		assertNotNull(graph1);
		assertNotNull(graph2a);
		assertNotNull(graph2b);
		assertNotNull(graph2c);
		assertNotNull(graph2d);
		assertNotNull(graph3a);
		assertNotNull(graph3b);
		assertNotNull(graph3c);
		assertNotNull(graph3d);
		assertNotNull(graph4);
		assertNotNull(graph5);
		
		calc1 = new SubgraphIsomorphism(graph1, graph2a);
		calc2 = new SubgraphIsomorphism(graph1, graph2b);
		calc3 = new SubgraphIsomorphism(graph1, graph2c);
		calc4 = new SubgraphIsomorphism(graph1, graph3a);
		calc5 = new SubgraphIsomorphism(graph1, graph3b);
		calc6 = new SubgraphIsomorphism(graph1, graph3c);
		calc7 = new SubgraphIsomorphism(graph1, graph2d);
		calc8 = new SubgraphIsomorphism(graph1, graph3d);
		calc9 = new SubgraphIsomorphism(graph4, graph5);

		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());
		assertTrue(calc2.calculate());
		assertTrue(calc2.isCalculated());
		assertTrue(calc3.calculate());
		assertTrue(calc3.isCalculated());
		assertTrue(calc4.calculate());
		assertTrue(calc4.isCalculated());
		assertTrue(calc5.calculate());
		assertTrue(calc5.isCalculated());
		assertTrue(calc6.calculate());
		assertTrue(calc6.isCalculated());
		assertTrue(calc7.calculate());
		assertTrue(calc7.isCalculated());
		assertTrue(calc8.calculate());
		assertTrue(calc8.isCalculated());
		assertTrue(calc9.calculate());
		assertTrue(calc9.isCalculated());
	}

	public void testSampleGraph() {
		assertEquals(calc1.getSimilarity(), 0.4642857142857143);
		assertEquals(calc2.getSimilarity(), 0.5);
		assertEquals(calc3.getSimilarity(), 0.4629629629629629);
		assertEquals(calc4.getSimilarity(), 1d);
		assertEquals(calc5.getSimilarity(), 0.9852941176470589);
		assertEquals(calc6.getSimilarity(), 0.9545454545454546);
		assertEquals(calc7.getSimilarity(), 0.42592592592592593);
		assertEquals(calc8.getSimilarity(), 0.8939393939393939);
	}

	private IGraphAccessor generateSampleG1() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("1");
		GraphNode n2 = new GraphNode("2");
		GraphNode n3 = new GraphNode("3");
		GraphNode n4 = new GraphNode("4");
		GraphNode n5 = new GraphNode("5");
		GraphNode n6 = new GraphNode("6");
		GraphNode n7 = new GraphNode("7");
		GraphNode n8 = new GraphNode("8");
		GraphNode n9 = new GraphNode("9");
		GraphNode n10 = new GraphNode("10");
		GraphNode n11 = new GraphNode("11");
		GraphNode n12 = new GraphNode("12");
		GraphNode n13 = new GraphNode("13");
		GraphNode n14 = new GraphNode("14");
		GraphNode n15 = new GraphNode("15");
		GraphNode n16 = new GraphNode("16");
		GraphNode n17 = new GraphNode("17");

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

	private IGraphAccessor generateSampleG2(String test) {
		SimpleGraphAccessor g2 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("1");
		GraphNode n2 = new GraphNode("5");
		GraphNode n3 = new GraphNode("3");
		GraphNode n4 = new GraphNode("10");
		GraphNode n5 = new GraphNode("11");
		GraphNode n6 = new GraphNode("12");
		GraphNode n8 = new GraphNode("17");
		GraphNode n9 = new GraphNode("8");
		GraphNode n10 = new GraphNode("16");

		if (test.equals("rename") || test.equals("remove_node")
				|| test.equals("remove_edge")) {
			n10 = new GraphNode("16");
		} else {
			n10 = new GraphNode("4");
		}

		GraphNode n11 = new GraphNode("9");

		if (!test.equals("remove_node") && !test.equals("remove_edge")) {
			GraphNode n7 = new GraphNode("2");
			g2.setEdge(n3, n7);
			g2.setEdge(n7, n9);
		}
		if (!test.equals("remove_edge")) {
			g2.setEdge(n6, n8);
		}

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

		return g2;
	}

	private IGraphAccessor generateSampleG3(String test) {
		SimpleGraphAccessor g3 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("1");
		GraphNode n2 = new GraphNode("2");
		GraphNode n3 = new GraphNode("3");
		GraphNode n4 = new GraphNode("4");
		GraphNode n5 = new GraphNode("5");
		GraphNode n6 = new GraphNode("6");
		GraphNode n7 = new GraphNode("7");
		GraphNode n8 = new GraphNode("8");
		GraphNode n9 = new GraphNode("9");
		GraphNode n10 = new GraphNode("10");
		GraphNode n11 = new GraphNode("11");
		GraphNode n13 = new GraphNode("13");
		GraphNode n14 = new GraphNode("14");
		GraphNode n15 = new GraphNode("15");
		GraphNode n16 = new GraphNode("16");
		GraphNode n17 = new GraphNode("17");
		GraphNode n12 = new GraphNode("18");

		if (test.equals("rename") || test.equals("remove_node")
				|| test.equals("remove_edge")) {
			n12 = new GraphNode("18");
		} else
			n12 = new GraphNode("12");

		if (!test.equals("remove_node") && !test.equals("remove_edge")) {
			n6 = new GraphNode("6");
			g3.setEdge(n2, n6);
			g3.setEdge(n3, n6);
			g3.setEdge(n6, n9);
		}

		if (!test.equals("remove_edge")) {
			g3.setEdge(n3, n7);
		}

		g3.setEdge(n1, n2);
		g3.setEdge(n1, n3);
		g3.setEdge(n2, n4);
		g3.setEdge(n2, n5);
		g3.setEdge(n4, n8);
		g3.setEdge(n7, n10);
		g3.setEdge(n7, n11);
		g3.setEdge(n7, n12);
		g3.setEdge(n8, n13);
		g3.setEdge(n9, n13);
		g3.setEdge(n9, n14);
		g3.setEdge(n9, n15);
		g3.setEdge(n10, n15);
		g3.setEdge(n10, n16);
		g3.setEdge(n11, n16);
		g3.setEdge(n12, n17);
		g3.setEdge(n17, n16);

		return g3;
	}

	private IGraphAccessor generateSampleG4() {
		SimpleGraphAccessor g4 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("A");
		GraphNode n2 = new GraphNode("B");
		GraphNode n3 = new GraphNode("C");
		GraphNode n4 = new GraphNode("D");
		GraphNode n5 = new GraphNode("E");
		GraphNode n6 = new GraphNode("F");
		GraphNode n7 = new GraphNode("G");
		GraphNode n8 = new GraphNode("H");
		GraphNode n9 = new GraphNode("I");
		GraphNode n10 = new GraphNode("J");
		GraphNode n11 = new GraphNode("K");

		g4.setEdge(n1, n2); // A-B
		g4.setEdge(n1, n3); // A-C
		g4.setEdge(n2, n4); // B-D
		g4.setEdge(n2, n5); // B-E
		g4.setEdge(n2, n6); // B-F
		g4.setEdge(n3, n6); // C-F
		g4.setEdge(n3, n7); // C-G
		g4.setEdge(n6, n8); // F-H
		g4.setEdge(n6, n9); // F-I
		g4.setEdge(n6, n10); // F-J
		g4.setEdge(n6, n11); // F-K
		g4.setEdge(n7, n8); // G-H
		g4.setEdge(n7, n9); // G-I
		g4.setEdge(n7, n10); // G-J
		g4.setEdge(n7, n11); // G-K

		return g4;
	}

	private IGraphAccessor generateSampleG5() {
		SimpleGraphAccessor g5 = new SimpleGraphAccessor();

		GraphNode n1 = new GraphNode("a");
		GraphNode n2 = new GraphNode("b");
		GraphNode n3 = new GraphNode("c");
		GraphNode n4 = new GraphNode("d");
		GraphNode n5 = new GraphNode("e");
		GraphNode n6 = new GraphNode("f");
		GraphNode n7 = new GraphNode("g");
		GraphNode n8 = new GraphNode("h");
		GraphNode n9 = new GraphNode("i");
		GraphNode n10 = new GraphNode("j");
		GraphNode n11 = new GraphNode("k");
		GraphNode n12 = new GraphNode("l");

		g5.setEdge(n1, n2); // a-b
		g5.setEdge(n1, n3); // a-c
		g5.setEdge(n1, n4); // a-d
		g5.setEdge(n1, n5); // a-e
		g5.setEdge(n2, n6); // b-f
		g5.setEdge(n2, n7); // b-g
		g5.setEdge(n2, n8); // b-h
		g5.setEdge(n3, n9); // c-i
		g5.setEdge(n4, n12); // d-l
		g5.setEdge(n5, n10); // e-j
		g5.setEdge(n5, n11); // e-k

		return g5;
	}
}
