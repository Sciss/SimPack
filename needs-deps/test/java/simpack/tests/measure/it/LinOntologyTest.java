/*
 * $Id: LinOntologyTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.it;

import java.io.File;

import junit.framework.TestCase;
import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.exception.InvalidElementException;
import simpack.measure.it.Lin;
import simpack.util.graph.GraphNode;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: LinOntologyTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class LinOntologyTest extends TestCase {

	private String SUMO_OntologyURI = "http://127.0.0.1/ontology/simplified_sumo.owl";

	private String SUMO_OntologyBaseURI = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String SUMO_OntologyRootURI = "http://127.0.0.1/ontology/simplified_sumo.owl#SUMORoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private String ontClassA = "http://127.0.0.1/ontology/simplified_sumo.owl#Shrimp";

	private String ontClassB = "http://127.0.0.1/ontology/simplified_sumo.owl#Wine";

	private String ontClassC = "http://lsdis.cs.uga.edu/proj/semdis/testbed/#Politician";

	private String ontClassD = "http://127.0.0.1/ontology/simplified_sumo.owl#Shellfish";

	private String ontClassE = "http://127.0.0.1/ontology/simplified_sumo.owl#Entity";

	private String ontClassF = "http://127.0.0.1/ontology/simplified_sumo.owl#DualObjectProcess";

	private String ontClassG = "http://127.0.0.1/ontology/simplified_sumo.owl#Meat";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	public void testJenaOntologyAccessor() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassA), new GraphNode(
					ontClassB));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(2d * Math.log(19d / 121d)
				/ (Math.log(1d / 121d) + Math.log(1d / 121d)));
		assertEquals(lin.getSimilarity(), result);
	}

	public void testCalculateSimilarityInvalidClassName() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassA), new GraphNode(
					ontClassC));
			fail("Should raise an InvalidNodException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
	}

	public void testCalculateSimilaritySameClass() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassA), new GraphNode(
					ontClassA));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(2d * Math.log(3d / 121d)
				/ (Math.log(1d / 121d) + Math.log(1d / 121d)));
		assertEquals(lin.getSimilarity(), result);
	}

	public void testCalculateSimilarityOneIsParentOfOther() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassA), new GraphNode(
					ontClassD));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(2d * Math.log(3d / 121d)
				/ (Math.log(1d / 121d) + Math.log(3d / 121d)));
		assertEquals(lin.getSimilarity(), result);
	}

	public void testCalculateSimilarityOneIsAncestorOfOther() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassA), new GraphNode(
					ontClassG));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(2d * Math.log(14d / 121d)
				/ (Math.log(1d / 121d) + Math.log(14d / 121d)));
		assertEquals(lin.getSimilarity(), result);
	}

	public void testCalculateSimilarityMRCAIsRootOfOntology() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassE), new GraphNode(
					ontClassF));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(0d);
		assertEquals(lin.getSimilarity(), result);
	}

	public void testCalculateSimilarityOneIsRootOfOntology() {
		Lin lin = null;
		try {
			lin = new Lin(new JenaOntologyAccessor(SUMO_OntologyURI,
					altOntologyURL, SUMO_OntologyBaseURI, SUMO_OntologyRootURI,
					ontModelSpec), new GraphNode(ontClassE), new GraphNode(
					SUMO_OntologyRootURI));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(lin);
		assertTrue(lin.calculate());
		assertTrue(lin.isCalculated());
		Double result = new Double(0d);
		assertEquals(lin.getSimilarity(), result);
	}
}