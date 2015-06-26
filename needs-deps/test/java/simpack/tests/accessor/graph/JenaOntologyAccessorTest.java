/*
 * $Id: JenaOntologyAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.accessor.graph;

import java.io.File;

import junit.framework.TestCase;
import simpack.accessor.graph.JenaOntologyAccessor;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: JenaOntologyAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JenaOntologyAccessorTest extends TestCase {

	private String ontologyURI = "http://127.0.0.1/ontology/simplified_sumo.owl";

	private String ontologyBaseURI = "http://127.0.0.1/ontology/simplified_sumo.owl#";

	private String ontologyRootURI = "http://127.0.0.1/ontology/simplified_sumo.owl#SUMORoot";

	private String altOntologyURL = "file:ontology" + File.separator
			+ "simplified_sumo.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_RDFS_INF;

	public void testMaxDirectedPathLength() {
		JenaOntologyAccessor accessor = null;

		accessor = new JenaOntologyAccessor(ontologyURI, altOntologyURL,
				ontologyBaseURI, ontologyRootURI, ontModelSpec);

		double l = accessor.getMaximumDirectedPathLength();

		assertEquals(l, new Double(24));
	}
}