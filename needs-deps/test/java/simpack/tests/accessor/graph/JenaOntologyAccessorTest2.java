/*
 * $Id: JenaOntologyAccessorTest2.java 757 2008-04-17 17:42:53Z kiefer $
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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import simpack.accessor.graph.JenaOntologyAccessor;
import simpack.api.IGraphNode;
import simpack.exception.InvalidElementException;

import com.hp.hpl.jena.ontology.OntModelSpec;

/**
 * @author Christoph Kiefer
 * @version $Id: JenaOntologyAccessorTest2.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JenaOntologyAccessorTest2 extends TestCase {

	private String ontologyURI = "http://nb.vse.cz/~svabo/oaei2007/data/confOf.owl";

	private String ontologyBaseURI = "http://confOf#";

	private String ontologyRootURI = "http://confOf#Contribution";

	private String altOntologyURI = "file:ontology" + File.separator
			+ "confOf.owl";

	private OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM_TRANS_INF;

	public void testOntology() {
		IGraphNode node, nodeP1, nodeP2;

		JenaOntologyAccessor jenaAccessor = null;
		jenaAccessor = new JenaOntologyAccessor(ontologyURI, altOntologyURI,
				ontologyBaseURI, ontologyRootURI, ontModelSpec);

		int numberclasses = 0;

		TreeSet<IGraphNode> nodes = jenaAccessor.getNodeSet();

		Iterator<IGraphNode> iterator = nodes.iterator();
		while (iterator.hasNext()) {
			node = (IGraphNode) iterator.next();
			System.out.println(node + ", ");
			Set<IGraphNode> nodes1 = null;
			try {
				nodes1 = jenaAccessor.getSuccessors(node, true);
			} catch (InvalidElementException e) {
				e.printStackTrace();
			}
			Iterator<IGraphNode> iterator1 = nodes1.iterator();
			while (iterator1.hasNext()) {
				System.out.println("    " + iterator1.next());
				numberclasses++;
			}
			numberclasses++;
		}

		System.out.println(numberclasses);
	}
}