/*
 * $Id: XMLTreeEditDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.io.IOException;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import simpack.accessor.tree.SimpleTreeAccessor;
import simpack.api.ITreeNode;
import simpack.exception.InvalidElementException;
import simpack.measure.tree.TreeEditDistance;
import simpack.util.conversion.WorstCaseDistanceConversion;
import simpack.util.tree.comparator.NamedTreeNodeComparator;
import simpack.util.xml.XMLIterator;
import simpack.util.xml.XMLVisitor;

/**
 * @author Christoph Kiefer
 * @version $Id: XMLTreeEditDistanceTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class XMLTreeEditDistanceTest extends TestCase {

	private ITreeNode tree1, tree2;

	private TreeEditDistance calc;

	String xmlFilePath1 = "xml/doc1.xml";

	String xmlFilePath2 = "xml/doc2.xml";

	protected void setUp() throws Exception {
		tree1 = generateSampleT1();
		tree2 = generateSampleT2();
	}

	public void testEditDistance() {
		assertNotNull(tree1);
		assertNotNull(tree2);

		try {
			calc = new TreeEditDistance(new SimpleTreeAccessor(tree1),
					new SimpleTreeAccessor(tree2),
					new NamedTreeNodeComparator(),
					new WorstCaseDistanceConversion());

			assertNotNull(calc);
			assertTrue(calc.calculate());
			assertTrue(calc.isCalculated());
			assertEquals(calc.getTreeEditDistance(), 47d);
			assertEquals(calc.getSimilarity(), 0.8629737609329446d);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InvalidElementException e) {
			e.printStackTrace();
		}
	}

	private ITreeNode generateSampleT1() {
		// load XML document into a DOM object
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setValidating(false);
		DocumentBuilder domBuilder;
		try {
			domBuilder = domFactory.newDocumentBuilder();
			Document xmlDoc = domBuilder.parse(xmlFilePath1);
			Node rootNode = xmlDoc.getDocumentElement();

			// convert DOM object into a ITreeNode in order to calculate the
			// Tree Edit Distance
			XMLVisitor visitor = new XMLVisitor();
			XMLIterator xml = new XMLIterator(true, true);
			xml.setVisitor(visitor);
			xml.scanNodes(rootNode);
			ITreeNode tree = visitor.getTree();
			// printTree(tree, 0);

			// Enumeration en = tree.preorderEnumeration();
			// while (en.hasMoreElements()) {
			// System.out.println("aaaa");
			// System.out.println(en.nextElement().toString());
			// System.out.println("bbbb");
			// }

			return tree;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private ITreeNode generateSampleT2() {
		// load XML document into a DOM object
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setValidating(false);
		DocumentBuilder domBuilder;
		try {
			domBuilder = domFactory.newDocumentBuilder();
			Document xmlDoc = domBuilder.parse(xmlFilePath2);
			Node rootNode = xmlDoc.getDocumentElement();

			// convert DOM object into a ITreeNode in order to calculate the
			// Tree Edit Distance
			XMLVisitor visitor = new XMLVisitor();
			XMLIterator iter = new XMLIterator(true, true);
			iter.setVisitor(visitor);
			iter.scanNodes(rootNode);
			ITreeNode tree = visitor.getTree();
			// printTree(tree, 0);

			// Enumeration en = tree.preorderEnumeration();
			// while (en.hasMoreElements()) {
			// System.out.println("cccc");
			// System.out.println(en.nextElement().toString());
			// System.out.println("dddd");
			// }

			return tree;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void printTree(ITreeNode root, int depth) {
		if (root != null) {
			indent(depth);
			Node n = (Node) root.getUserObject();

			if (n.getNodeType() == Node.TEXT_NODE) {
				Text textNode = (Text) n;
				String text = textNode.getData().trim(); // Strip off space
				if ((text != null) && text.length() > 0) // If non-empty
					System.out.println(text); // print text
			} else {
				System.out.println(n.toString());
			}

			Enumeration en = root.children();

			while (en.hasMoreElements()) {
				printTree((ITreeNode) en.nextElement(), depth + 1);
			}
		}
	}

	private void indent(int depth) {
		for (int i = 0; i < depth; i++) {
			System.out.print("  ");
		}
	}
}
