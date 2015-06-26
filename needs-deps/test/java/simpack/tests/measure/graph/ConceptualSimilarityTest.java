/*
 * $Id: ConceptualSimilarityTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.exception.InvalidElementException;
import simpack.measure.graph.ConceptualSimilarity;
import simpack.util.graph.GraphNode;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: ConceptualSimilarityTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class ConceptualSimilarityTest extends TestCase {

	private String ontologyURI = "http://127.0.0.1/ontology/simplified_sumo.owl";

	private String ontologyBaseURI = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String ontologyRootURI = "http://127.0.0.1/ontology/simplified_sumo.owl#SUMORoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	public void testConSim() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double((2d * 6d)
				/ (8d + 2d * 6d)));
	}

	public void testConSimInvalidNodeException() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#HoofedMammalalalal";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#BodyPart";

		try {
			ConceptualSimilarity conSim = new ConceptualSimilarity(
					new JenaOntologyAccessor(ontologyURI, altOntologyURL,
							ontologyBaseURI, ontologyRootURI, ontModelSpec),
					new GraphNode(new String(ontClassA)), new GraphNode(
							new String(ontClassB)));
			fail("Should raise an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
	}

	public void testConSimMultiplePathPossible() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Industry";

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double(
				(2d * 3d / (6d + 2d * 3d))));
	}

	public void testConSimSameNode() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#IndependentState";
		String ontClassB = ontClassA;

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double(1));
	}

	public void testConSimOneIsAncestorOfOther() {
		String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#Vertebrate";
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Organism";

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double((2d * 5d)
				/ (2d + 2d * 5d)));
	}

	public void testConSimOneIsRootOfOntology() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Object";

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double(1d / (3d + 1d)));
	}

	public void testConSimOneIsRootOfTreeMutliplePathsPossible() {
		String ontClassA = ontologyRootURI;
		String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Transaction";

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double(1d / (2d + 1d)));
	}

	public void testConSimOneBothRootOfTree() {
		String ontClassA = ontologyRootURI;
		String ontClassB = ontologyRootURI;

		ConceptualSimilarity conSim = null;
		try {
			conSim = new ConceptualSimilarity(new JenaOntologyAccessor(
					ontologyURI, altOntologyURL, ontologyBaseURI,
					ontologyRootURI, ontModelSpec), new GraphNode(new String(
					ontClassA)), new GraphNode(new String(ontClassB)));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(conSim);
		assertTrue(conSim.calculate());
		assertTrue(conSim.isCalculated());
		assertEquals(conSim.getSimilarity(), new Double(1d));
	}
}