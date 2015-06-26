/*
 * $Id: ConstructedSubgraphIsomorphismGroupTest.java 757 2008-04-17 17:42:53Z kiefer $
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
 * @version $Id: ConstructedSubgraphIsomorphismGroupTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConstructedSubgraphIsomorphismGroupTest extends TestCase {

	private IGraphAccessor graph1, graph2;

	private SubgraphIsomorphism calc1;

	protected void setUp() throws Exception {
		graph1 = generateSampleG1();
		graph2 = generateSampleG2();
		calc1 = new SubgraphIsomorphism(graph1, graph2, "Levenshtein", 1, 0.5,
				0.5, "average", true);
		assertNotNull(graph1);
		assertNotNull(graph2);

		assertTrue(calc1.calculate());
		assertTrue(calc1.isCalculated());
	}

	public void testSampleGraph() {
		assertEquals(calc1.getSimilarity(), 0.2608695652173913);
	}

	private IGraphAccessor generateSampleG1() {
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

	private IGraphAccessor generateSampleG2() {
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