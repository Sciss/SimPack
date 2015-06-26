/*
 * $Id: SubgraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
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
import simpack.measure.graph.SubgraphIsomorphism;
import simpack.util.graph.GraphNode;

/**
 * @author Christoph Kiefer
 * @version $Id: SubgraphIsomorphismTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class SubgraphIsomorphismTest extends TestCase {

	public void testSameGraph() {
		SubgraphIsomorphism g = new SubgraphIsomorphism(generateSampleG3(),
				generateSampleG3(), "Levenshtein", 2, 0.5, 0.5, "average",
				false);
		assertEquals(g.getSimilarity(), 1d);
	}

	private IGraphAccessor generateSampleG1() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section
		// --> 1
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section",
				"1");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section
		// --> 1
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#section",
				"1");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Section
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Section");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> http://www.w3.org/2000/01/rdf-schema#Class
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://www.w3.org/2000/01/rdf-schema#Class");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f30
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f30");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f31
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f310");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> A chapter (or section or whatever) of a book having its own
		// title.@en
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"A chapter (or section or whatever) of a book having its own title.@en");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> BookPart@en
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"BookPart@en");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Section
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Section");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> http://www.w3.org/2002/07/owl#Class
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://www.w3.org/2002/07/owl#Class");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// setEdge(
		// g1,
		// "http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
		// "http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f48
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f48");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> http://www.w3.org/2000/01/rdf-schema#Resource
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://www.w3.org/2000/01/rdf-schema#Resource");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f32
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f32");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f47
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f47");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f49
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f49");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// -->
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Entry
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Entry");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> -1101d91e:117c4a712f4:-7f33
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"-1101d91e:117c4a712f4:-7f33");

		// Edge:
		// http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter
		// --> http://www.w3.org/2000/01/rdf-schema#Resource
		setEdge(
				g1,
				"http://oaei.ontologymatching.org/2007/benchmarks/205/onto.rdf#Chapter",
				"http://www.w3.org/2000/01/rdf-schema#Resource");

		return g1;
	}

	private IGraphAccessor generateSampleG2() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		setEdge(g1, "A", "B");
		setEdge(g1, "A", "C");
		setEdge(g1, "B", "D");
		setEdge(g1, "B", "G");
		setEdge(g1, "A", "E");
		setEdge(g1, "E", "F");

		return g1;
	}

	private IGraphAccessor generateSampleG3() {
		SimpleGraphAccessor g1 = new SimpleGraphAccessor();

		setEdge(g1, "A", "B");
		setEdge(g1, "B", "1");
		setEdge(g1, "B", "2");
		setEdge(g1, "A", "C");
		setEdge(g1, "A", "D");
		setEdge(g1, "A", "E");
		setEdge(g1, "A", "F");
		setEdge(g1, "A", "G");
		setEdge(g1, "A", "H");
		// setEdge(g1, "A", "I");
		// setEdge(g1, "A", "J");
		// setEdge(g1, "A", "K");

		return g1;
	}

	private void setEdge(SimpleGraphAccessor g, String source, String target) {
		g.setEdge(new GraphNode(source), new GraphNode(target));
	}
}
