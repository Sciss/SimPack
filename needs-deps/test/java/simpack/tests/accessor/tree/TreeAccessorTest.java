/*
 * $Id: TreeAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.accessor.tree;

import junit.framework.TestCase;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import simpack.accessor.tree.AstTreeAccessor;
import simpack.accessor.tree.FamixTreeAccessor;
import simpack.api.ITreeAccessor;
import simpack.api.ITreeNode;
import simpack.util.tree.TreeUtil;
import ch.toe.famix.FAMIXInstance;
import ch.toe.famix.model.Attribute;
import ch.toe.famix.model.Class;
import ch.toe.famix.model.FormalParameter;
import ch.toe.famix.model.Method;
import ch.toe.famix.model.Model;
import ch.toe.famix.model.Package;

/**
 * @author Tobias Sager
 * @version $Id: TreeAccessorTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TreeAccessorTest extends TestCase {

	private FAMIXInstance famix;

	private ASTNode astNode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		famix = generateFamixModel();
		assertNotNull(famix);

		astNode = generateAstModel();
		assertNotNull(astNode);
	}

	public void testAstAccessor() {
		ITreeAccessor accessor = new AstTreeAccessor(astNode);
		ITreeNode root = accessor.getRoot();
		assertNotNull(root);
		assertEquals(TreeUtil.calculateSize(root), 25);
	}

	/**
	 * Check FAMIXAccessor
	 */
	public void testFAMIXAccessor() {
		ITreeAccessor accessor = new FamixTreeAccessor(famix);
		ITreeNode root = accessor.getRoot();
		assertNotNull(root);
		assertEquals(TreeUtil.calculateSize(root), 8);
	}

	/**
	 * Generate a sample <code>ASTNode</code>.
	 * 
	 * @return the generated <code>ASTNode</code>
	 */
	public static ASTNode generateAstModel() {
		String source = "" + "package internal.test;" + ""
				+ "public class Tester {" + "	private int field = 1;"
				+ "	public Tester() {" + "		field = 2;" + "	}"
				+ "	public int getField() {"
				+ "		return Integer.toInt(new String(field));" + "	}" + "}";
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		return parser.createAST(null);

	}

	/**
	 * Generate a <code>FAMIXInstance</code>.
	 * 
	 * @return the generated <code>FAMIXInstance</code>
	 */
	public static FAMIXInstance generateFamixModel() {
		Model model = new Model("TestSuite", "n/a", "3", "Java");
		FAMIXInstance ret = new FAMIXInstance(model);

		Package pkgBase = ret.addPackage("base", null);
		Package pkgTest = new Package("test", pkgBase);
		pkgTest.setBelongsTo(pkgBase);

		Class clazz = new Class("HelloWorld");
		pkgTest.getClasses().add(clazz);
		clazz.getAttributes().add(new Attribute("output", clazz));

		Method method = new Method("printText", "printText(String)", clazz);
		clazz.getMethods().add(method);
		method.getFormalParameters().add(
				new FormalParameter("output", method, new Integer(1)));

		return ret;
	}
}
