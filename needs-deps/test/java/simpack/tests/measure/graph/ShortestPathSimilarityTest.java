/*
 * $Id: ShortestPathSimilarityTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.io.File;

import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.exception.InvalidElementException;
import simpack.measure.graph.ShortestPath;
import simpack.util.conversion.CommonDistanceConversion;
import simpack.util.graph.GraphNode;

import com.hp.hpl.jena.ontology.OntModelSpec;

import junit.framework.TestCase;

/**
 * @author Christoph Kiefer
 * @version $Id: ShortestPathSimilarityTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ShortestPathSimilarityTest extends TestCase {

	private String ontologyURI = "http://127.0.0.1/ontology/simplified_sumo.owl";

	private String ontologyBaseURI = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String ontologyRootURI = "http://127.0.0.1/ontology/simplified_sumo.owl#SUMORoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	public void testShortestPath() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1d / (1d + 8d)));
	}

	public void testShortestPathInvalidNodeException() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammalalalal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
			fail("Should raise an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
	}

	public void testShortestPathMultiplePathPossible() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Industry";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1d / (1d + 6d)));
	}

	public void testShortestPathSameNode() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = ontClassA;

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1));
	}

	public void testShortestPathOneIsAncestorOfOther() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#Vertebrate";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Organism";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1d / (1d + 2d)));
	}

	public void testShortestPathOneIsRootOfOntology() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Object";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1d / (1d + 3d)));
	}

	public void testShortestPathOneIsRootOfOntologyMutliplePathsPossible() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Transaction";

		ShortestPath sp = null;
		try {
			sp = new ShortestPath(new JenaOntologyAccessor(ontologyURI,
					altOntologyURL, ontologyBaseURI, ontologyRootURI,
					ontModelSpec), new GraphNode(new String(ontClassA)),
					new GraphNode(new String(ontClassB)),
					new CommonDistanceConversion());
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(sp);
		assertTrue(sp.calculate());
		assertTrue(sp.isCalculated());
		assertEquals(sp.getSimilarity(), new Double(1d / (1d + 2d)));
	}
}