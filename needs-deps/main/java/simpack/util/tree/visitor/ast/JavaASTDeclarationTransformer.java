/*
 * $Id: JavaASTDeclarationTransformer.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.tree.visitor.ast;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * @author Beat Fluri
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class JavaASTDeclarationTransformer extends ASTVisitor {

	/**
	 * General leaf label
	 */
	public final static int IDENTIFIER = 128;

	private Stack<Node> fStack = new Stack<Node>();

	public JavaASTDeclarationTransformer(Node root) {
		fStack.clear();
		fStack.push(root);
	}

	public boolean visit(Block node) {
		// skip block as it is not interesting
		return true;
	}

	public void endVisit(Block node) {
		// do nothing pop is not needed (see visit(Block))
	}

	public boolean visit(FieldDeclaration node) {
		// pushEmptyNode(node, "FieldDeclaration");
		// javadoc is not interesting at the moment
		/*
		 * if (node.getJavadoc() != null) { node.getJavadoc().accept(this); }
		 */
		visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		node.getType().accept(this);
		visitList(SourceCodeEntity.FRAGMENTS, node.fragments());
		return false;
	}

	public void endVisit(FieldDeclaration node) {
		pop();
	}

	public boolean visit(Javadoc node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(Javadoc node) {
		pop();
	}

	public boolean visit(MethodDeclaration node) {
		// pushValuedNode(node, node.getName().getFullyQualifiedName());

		/*
		 * if (node.getJavadoc() != null) { node.getJavadoc().accept(this); }
		 */
		visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		if (node.getReturnType2() != null) {
			node.getReturnType2().accept(this);
		}
		visitList(SourceCodeEntity.TYPE_ARGUMENTS, node.typeParameters());
		visitList(SourceCodeEntity.PARAMETERS, node.parameters());
		visitList(SourceCodeEntity.THROW, node.thrownExceptions());
		// ignore body, since only declaration is interesting
		// node.getBody().accept(this);
		return false;
	}

	public void endVisit(MethodDeclaration node) {
		pop();
	}

	public boolean visit(Modifier node) {
		pushValuedNode(node, node.getKeyword().toString());
		return false;
	}

	public void endVisit(Modifier node) {
		pop();
	}

	public boolean visit(ParameterizedType node) {
		pushEmptyNode(node);
		node.getType().accept(this);
		visitList(SourceCodeEntity.TYPE_ARGUMENTS, node.typeArguments());
		return false;
	}

	public void endVisit(ParameterizedType node) {
		pop();
	}

	public boolean visit(PrimitiveType node) {
		pushValuedNode(node, node.getPrimitiveTypeCode().toString());
		return false;
	}

	public void endVisit(PrimitiveType node) {
		pop();
	}

	public boolean visit(QualifiedType node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(QualifiedType node) {
		pop();
	}

	public boolean visit(SimpleType node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		return false;
	}

	public void endVisit(SimpleType node) {
		pop();
	}

	public boolean visit(SingleVariableDeclaration node) {
		boolean isNotParam = getCurrentParent().getLabel() != SourceCodeEntity.PARAMETERS;
		pushValuedNode(node, node.getName().getIdentifier());
		// pushEmptyNode(node);
		if (isNotParam) {
			visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		}
		node.getType().accept(this);
		// initializer not interesting at the moment
		/*
		 * if (node.getInitializer() != null) {
		 * node.getInitializer().accept(this); }
		 */
		return false;
	}

	public void endVisit(SingleVariableDeclaration node) {
		pop();
	}

	public boolean visit(TypeDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());

		// javadoc not interesting at the moment
		/*
		 * if (node.getJavadoc() != null) { node.getJavadoc().accept(this); }
		 */
		visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		visitList(SourceCodeEntity.TYPE_ARGUMENTS, node.typeParameters());
		if (node.getSuperclassType() != null) {
			node.getSuperclassType().accept(this);
		}
		visitList(SourceCodeEntity.SUPER_INTERFACE_TYPES, node
				.superInterfaceTypes());
		// visitList(BODY_DECLARATIONS, node.bodyDeclarations());
		return false;
	}

	public void endVisit(TypeDeclaration node) {
		pop();
	}

	public boolean visit(TypeDeclarationStatement node) {
		// skip, only type declaration is interesting
		return true;
	}

	public void endVisit(TypeDeclarationStatement node) {
		// do nothing
	}

	public boolean visit(TypeLiteral node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(TypeLiteral node) {
		pop();
	}

	public boolean visit(TypeParameter node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		visitList(node.typeBounds());
		return false;
	}

	public void endVisit(TypeParameter node) {
		pop();
	}

	public boolean visit(VariableDeclarationExpression node) {
		pushEmptyNode(node);
		visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		node.getType().accept(this);
		visitList(SourceCodeEntity.FRAGMENTS, node.fragments());
		return false;
	}

	public void endVisit(VariableDeclarationExpression node) {
		pop();
	}

	public boolean visit(VariableDeclarationFragment node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		// initializer not interesting at the moment
		/*
		 * if (node.getInitializer() != null) {
		 * node.getInitializer().accept(this); }
		 */
		return false;
	}

	public void endVisit(VariableDeclarationFragment node) {
		pop();
	}

	public boolean visit(VariableDeclarationStatement node) {
		pushEmptyNode(node);
		visitList(SourceCodeEntity.MODIFIERS, node.modifiers());
		node.getType().accept(this);
		visitList(SourceCodeEntity.FRAGMENTS, node.fragments());
		return false;
	}

	public void endVisit(VariableDeclarationStatement node) {
		pop();
	}

	public boolean visit(WildcardType node) {
		String bound = node.isUpperBound() ? "extends" : "super";
		pushValuedNode(node, bound);
		return true;
	}

	public void endVisit(WildcardType node) {
		pop();
	}

	private void visitList(List list) {
		for (Iterator it = list.iterator(); it.hasNext();) {
			((ASTNode) it.next()).accept(this);
		}
	}

	private void visitList(int parentLabel, List list) {
		pushLabel(parentLabel);
		if (!list.isEmpty()) {
			// pushLabel(parentLabel);
			visitList(list);
			// pop();
		}
		pop();
	}

	private void pushLabel(int label) {
		push(label, "");
	}

	private void pushEmptyNode(ASTNode node) {
		push(node.getNodeType(), "");
	}

	private void pushValuedNode(ASTNode node, String value) {
		push(node.getNodeType(), value);
	}

	private void push(int label, String value) {
		SourceCodeEntity st = null;
		// if (stLabel != null) {
		st = new SourceCodeEntity(value.trim(), label, 0);
		// }
		Node n = new Node(label, value.trim(), st);
		getCurrentParent().add(n);
		fStack.push(n);
	}

	private void pop() {
		fStack.pop();
	}

	private Node getCurrentParent() {
		return fStack.peek();
	}
}
