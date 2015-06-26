/*
 * $Id: CommonTreeOperationsTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.tree;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import simpack.accessor.tree.FamixTreeAccessor;
import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.util.tree.TreeNode;
import simpack.util.tree.comparator.NamedTreeNodeComparator;
import ch.toe.famix.FAMIXInstance;
import ch.toe.famix.model.Attribute;
import ch.toe.famix.model.Class;
import ch.toe.famix.model.FormalParameter;
import ch.toe.famix.model.Method;
import ch.toe.famix.model.Model;
import ch.toe.famix.model.Package;

/**
 * @author Christoph Kiefer
 * @version $Id: CommonTreeOperationsTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class CommonTreeOperationsTest extends TestCase {

	private ITreeNode tree2;

	private FAMIXInstance famixInstance;

	private FamixTreeAccessor accessor;

	private ITreeNode t2n1, t2n2, t2n3, t2n4, t2n5, t2n6, t2n7;

	protected void setUp() throws Exception {
		super.setUp();
		tree2 = generateSampleT2();
		assertNotNull(tree2);

		famixInstance = generateFamixInstance();
		assertNotNull(famixInstance);

		accessor = new FamixTreeAccessor(famixInstance);
		assertNotNull(accessor);

		ITreeNode root = accessor.getRoot();
		assertNotNull(root);
		assertEquals(root.toString(), "FAMIXInstance");
	}

	public void testFAMIXTreeMRCAOfNodes() {
		String node1 = new String("HelloWorld.printText(String).output");
		String node2 = new String("HelloUnderWorld.input");

		ITreeNode mrca = null;
		try {
			mrca = accessor.getMostRecentCommonAncestor(new TreeNode(node1),
					new TreeNode(node2));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		assertNotNull(mrca);
		assertEquals(mrca.toString(), "base.test");
	}

	public void testFAMIXTreeDescendantsOfNode() {
		// descendants returned in preorder
		Set<ITreeNode> descendants = null;
		try {
			descendants = accessor.getDescendants(new TreeNode("base"));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		Set<ITreeNode> correctDescendants = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		correctDescendants.add(new TreeNode("base.test"));
		correctDescendants.add(new TreeNode("HelloWorld"));
		correctDescendants.add(new TreeNode("HelloWorld.output"));
		correctDescendants.add(new TreeNode("HelloWorld.printText(String)"));
		correctDescendants.add(new TreeNode(
				"HelloWorld.printText(String).output"));
		correctDescendants.add(new TreeNode("HelloUnderWorld"));
		correctDescendants.add(new TreeNode("HelloUnderWorld.input"));

		assertEquals(descendants, correctDescendants);
	}

	public void testFAMIXTreeAncestorsOfNode() {
		Set<ITreeNode> ancestors = null;
		try {
			ancestors = accessor.getAncestors(new TreeNode(
					"HelloWorld.printText(String).output"));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		Set<ITreeNode> correctAncestors = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());

		correctAncestors.add(new TreeNode("FAMIXInstance"));
		correctAncestors.add(new TreeNode("base"));
		correctAncestors.add(new TreeNode("base.test"));
		correctAncestors.add(new TreeNode("HelloWorld"));
		correctAncestors.add(new TreeNode("HelloWorld.printText(String)"));

		assertEquals(ancestors, correctAncestors);
	}

	public void testFAMIXTreeChildrenOfNode() {
		Set<ITreeNode> children = null;
		try {
			children = accessor.getChildren(new TreeNode("HelloWorld"));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		Set<ITreeNode> correctChildren = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		correctChildren.add(new TreeNode("HelloWorld.output"));
		correctChildren.add(new TreeNode("HelloWorld.printText(String)"));

		assertEquals(children, correctChildren);
	}

	public void testFAMIXTreeParentsOfNode() {
		Set<ITreeNode> parents = null;
		try {
			parents = accessor.getParents(new TreeNode("HelloWorld"));
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}

		Set<ITreeNode> correctParents = new TreeSet<ITreeNode>(
				new NamedTreeNodeComparator());
		correctParents.add(new TreeNode("base.test"));

		assertEquals(parents, correctParents);
	}

	public void testFAMIXContainsNode() {
		assertTrue(accessor.contains(new TreeNode("HelloWorld")));
		assertFalse(accessor.contains(new TreeNode("HelloMyWorld")));
	}

	public void testFAMIXTreeSize() {
		assertEquals(accessor.size(), 10);
	}

	public void testFAMIXTreeException() {
		try {
			Set<ITreeNode> nodes = nodes = accessor.getChildren(new TreeNode(
					"HelloMyWorld"));
			fail("This should throw an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
		try {
			Set<ITreeNode> nodes = nodes = accessor
					.getDescendants(new TreeNode("HelloMyWorld"));
			fail("This should throw an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
		try {
			ITreeNode mrca = accessor.getMostRecentCommonAncestor(new TreeNode(
					"HelloMyWorld"), new TreeNode("HelloUnderWorld"));
			fail("This should throw an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
		try {
			ITreeNode mrca = accessor.getMostRecentCommonAncestor(new TreeNode(
					"HelloWorld"), new TreeNode("HelloMyUnderWorld"));
			fail("This should throw an InvalidNodeException");
		} catch (InvalidElementException e) {
			assertTrue(true);
		}
	}

	private FAMIXInstance generateFamixInstance() {
		Model model = new Model("TestSuite", "n/a", "3", "Java");
		FAMIXInstance ret = new FAMIXInstance(model);

		Package pkgBase = ret.addPackage("base", null);
		Package pkgTest = new Package("test", pkgBase);
		pkgTest.setBelongsTo(pkgBase);

		Class clazz = new Class("HelloWorld");
		pkgTest.getClasses().add(clazz);
		clazz.getAttributes().add(new Attribute("output", clazz));

		Class clazz2 = new Class("HelloUnderWorld");
		pkgTest.getClasses().add(clazz2);
		clazz2.getAttributes().add(new Attribute("input", clazz2));

		Method method = new Method("printText", "printText(String)", clazz);
		clazz.getMethods().add(method);
		method.getFormalParameters().add(
				new FormalParameter("output", method, new Integer(1)));

		return ret;
	}

	/**
	 * Constructs the sample tree T1 on page 56 (Fig. 2.1) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T2
	 */
	private ITreeNode generateSampleT2() {
		t2n1 = new TreeNode(new String("t2n1"));
		t2n2 = new TreeNode(new String("t2n2"));
		t2n3 = new TreeNode(new String("t2n3"));
		t2n4 = new TreeNode(new String("t2n4"));
		t2n5 = new TreeNode(new String("t2n5"));
		t2n6 = new TreeNode(new String("t2n6"));
		t2n7 = new TreeNode(new String("t2n7"));

		// right
		t2n4.add(t2n5);
		t2n4.add(t2n6);
		t2n3.add(t2n4);
		t2n3.add(t2n7);

		// root
		t2n1.add(t2n2);
		t2n1.add(t2n3);

		return t2n1;
	}

}
