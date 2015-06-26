/*
 * $Id: XMLIterator.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ch.toe.famix.Visitor;

/**
 * The use of this class is to build a tree from an XML document, or just a part
 * of an XML document. You can pass any <code>Node</code> of an XML document
 * to the method {@link #scanNodes(Node)}. The passed <code>Node</code> will
 * then be the root of the tree.
 * <p>
 * In the constructor, you can choose whether attribute nodes should be part of
 * the tree as well (this is actually not in conformance with the DOM model, but
 * can be helpful anyway). This is done by setting the parameter
 * {@link #inclAttributeName}. If you set it to false, attribute names are just
 * omitted.<br>
 * You can additionally choose whether the values should also be taken into the
 * tree. Values in this case would be the text nodes.
 * 
 * @author Silvan Hollenstein
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */

public class XMLIterator {

	private static final Logger LOG = LogManager.getLogger(XMLIterator.class);

	private Visitor visitor;

	/**
	 * This will include the attribute's name in the tree, thus the attribute
	 * node would be inserted as a child of its ownerElement.
	 */
	private boolean inclAttributeName = true;

	/**
	 * If inclValues is set to true, not only the tags are included in the tree,
	 * but also the text nodes, respectively their values, will be inserted into
	 * the tree.
	 */
	private boolean inclValues = false;

	public XMLIterator(boolean inclAttributeName, boolean inclValues) {
		this.inclAttributeName = inclAttributeName;
		this.inclValues = inclValues;
	}

	/**
	 * Set the visitor which is going to handle the events triggered by
	 * {@link #scanNodes(Node)} and {@link #scanAttributes(Node)}.
	 * 
	 * @param visitor
	 */
	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	/**
	 * Scan all nodes below the passed parameter <code>Node</code> in a
	 * preorder traversal. When passing a node, it will trigger an event that is
	 * passed to {@link Visitor}, which in turn must handle these events.
	 * 
	 * @param node
	 */
	public void scanNodes(Node node) {

		switch (node.getNodeType()) {

		case Node.TEXT_NODE:
			Text textNode = (Text) node;
			String text = textNode.getData().trim();
			if ((text != null) && text.length() > 0 && inclValues)
				visitor.visit(node);
			else
				return;
			break;

		// Document nodes in XQuery are not restricted to have only one node.
		case Node.DOCUMENT_NODE:
			if (node.getChildNodes().getLength() == 1) // This is the normal
				// case
				node = node.getFirstChild(); // This retrieves the root
			// element

			// ! Here is explicit no break ! Go on to ELEMENT_NODE

		case Node.ELEMENT_NODE:
			visitor.visit(node);
			if (node.hasAttributes() && inclAttributeName) {
				scanAttributes(node);
			}

			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child == null) {
					LOG.debug("child not found for node: " + node.getNodeName()
							+ "; children = " + children.getLength());
				}
				// We have to separate out attribute nodes, since in eXist the
				// attributes
				// are also children of an element. (This is actually not in
				// conformance
				// with the DOM model of the W3C.)
				if (child.getNodeType() != Node.ATTRIBUTE_NODE) {
					scanNodes(child);
				}
			}
			break;

		default:
			visitor.visit(node);
			break;

		}
		visitor.endVisit(null);
	}

	/**
	 * Scan all Attributes of the parameter Node. For each attribute it will
	 * trigger an event as in {@link #scanNodes(Node)}. Thus, attributes are
	 * going to be nodes as well in the tree.
	 * 
	 * @param node
	 */
	private void scanAttributes(Node node) {
		NamedNodeMap attrs = node.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			visitor.visit(attr);
			visitor.endVisit(null);
		}
	}

}
