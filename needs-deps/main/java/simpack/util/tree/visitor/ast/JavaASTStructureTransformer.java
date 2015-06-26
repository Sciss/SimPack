/*
 * $Id: JavaASTStructureTransformer.java 757 2008-04-17 17:42:53Z kiefer $
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
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;


/**
 * @author Beat Fluri
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class JavaASTStructureTransformer extends ASTVisitor {

	private int fStructureDepth = 0;

	private int fOverallDepth = 0;

	private Stack<Node> fStack = new Stack<Node>();

	public JavaASTStructureTransformer(Node root) {
		fStack.clear();
		fStack.push(root);
	}

	public int getOverallDepth() {
		return fOverallDepth;
	}

	public boolean visit(AssertStatement node) {
		pushValuedNode(node, node.getExpression().toString() + ":"
				+ node.getMessage().toString());
		return false;
	}

	public void endVisit(AssertStatement node) {
		pop();
	}

	public boolean visit(Block node) {
		// skip block as it is not interesting
		return true;
	}

	public void endVisit(Block node) {
		// do nothing
	}

	public boolean visit(BreakStatement node) {
		pushValuedNode(node, node.getLabel() != null ? node.getLabel()
				.toString() : "");
		return false;
	}

	public void endVisit(BreakStatement node) {
		pop();
	}

	public boolean visit(CatchClause node) {
		pushValuedNode(node, ((SimpleType) node.getException().getType())
				.getName().getFullyQualifiedName());
		increaseLevel();
		// since exception type is used as value, visit children by hand
		node.getBody().accept(this);
		return false;
	}

	public void endVisit(CatchClause node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(ConstructorInvocation node) {
		pushValuedNode(node, node.toString());
		return false;
	}

	public void endVisit(ConstructorInvocation node) {
		pop();
	}

	public boolean visit(ContinueStatement node) {
		pushValuedNode(node, node.getLabel() != null ? node.getLabel()
				.toString() : "");
		return false;
	}

	public void endVisit(ContinueStatement node) {
		pop();
	}

	public boolean visit(DoStatement node) {
		pushValuedNode(node, node.getExpression().toString());
		increaseLevel();
		return true;
	}

	public void endVisit(DoStatement node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(EmptyStatement node) {
		pushEmptyNode(node);
		return false;
	}

	public void endVisit(EmptyStatement node) {
		pop();
	}

	public boolean visit(EnhancedForStatement node) {
		pushValuedNode(node, node.getParameter().toString() + ":"
				+ node.getExpression().toString());
		increaseLevel();
		return true;
	}

	public void endVisit(EnhancedForStatement node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(ExpressionStatement node) {
		pushValuedNode(node.getExpression(), node.toString());
		return false;
	}

	public void endVisit(ExpressionStatement node) {
		pop();
	}

	public boolean visit(ForStatement node) {
		pushValuedNode(node, node.getExpression().toString());
		increaseLevel();
		return true;
	}

	public void endVisit(ForStatement node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(IfStatement node) {
		String expression = node.getExpression().toString();
		push(node.getNodeType(), expression);
		increaseLevel();
		if (node.getThenStatement() != null) {
			push(SourceCodeEntity.THEN_STATEMENT, expression);
			node.getThenStatement().accept(this);
			pop();
		}
		if (node.getElseStatement() != null) {
			push(SourceCodeEntity.ELSE_STATEMENT, expression);
			node.getElseStatement().accept(this);
			pop();
		}
		return false;
	}

	public void endVisit(IfStatement node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(LabeledStatement node) {
		pushValuedNode(node, node.getLabel().getFullyQualifiedName());
		node.getBody().accept(this);
		return false;
	}

	public void endVisit(LabeledStatement node) {
		pop();
	}

	public boolean visit(ReturnStatement node) {
		pushValuedNode(node, node.getExpression() != null ? node
				.getExpression().toString() : "");
		return false;
	}

	public void endVisit(ReturnStatement node) {
		pop();
	}

	public boolean visit(SuperConstructorInvocation node) {
		pushValuedNode(node, node.toString());
		return false;
	}

	public void endVisit(SuperConstructorInvocation node) {
		pop();
	}

	public boolean visit(SwitchCase node) {
		pushValuedNode(node, node.getExpression() != null ? node
				.getExpression().toString() : "default");
		return false;
	}

	public void endVisit(SwitchCase node) {
		pop();
	}

	public boolean visit(SwitchStatement node) {
		pushValuedNode(node, node.getExpression().toString());
		increaseLevel();
		visitList(node.statements());
		return false;
	}

	public void endVisit(SwitchStatement node) {
		fStructureDepth--;
		pop();
	}

	public boolean visit(SynchronizedStatement node) {
		pushValuedNode(node, node.getExpression().toString());
		return true;
	}

	public void endVisit(SynchronizedStatement node) {
		pop();
	}

	public boolean visit(ThrowStatement node) {
		pushValuedNode(node, node.getExpression().toString());
		return false;
	}

	public void endVisit(ThrowStatement node) {
		pop();
	}

	public boolean visit(TryStatement node) {
		pushEmptyNode(node);
		pushLabel(SourceCodeEntity.BODY);
		increaseLevel();
		node.getBody().accept(this);
		pop();
		fStructureDepth--;
		visitList(SourceCodeEntity.CATCH_CLAUSES, node.catchClauses());
		if (node.getFinally() != null) {
			pushLabel(SourceCodeEntity.FINALLY);
			increaseLevel();
			node.getFinally().accept(this);
			fStructureDepth--;
			pop();
		}
		return false;
	}

	public void endVisit(TryStatement node) {
		pop();
	}

	public boolean visit(VariableDeclarationStatement node) {
		push(node.getNodeType(), node.toString());
		return false;
	}

	public void endVisit(VariableDeclarationStatement node) {
		pop();
	}

	public boolean visit(WhileStatement node) {
		push(node.getNodeType(), node.getExpression().toString());
		increaseLevel();
		return true;
	}

	public void endVisit(WhileStatement node) {
		fStructureDepth--;
		pop();
	}

	private void visitList(List list) {
		for (Iterator it = list.iterator(); it.hasNext();) {
			((ASTNode) it.next()).accept(this);
		}
	}

	private void visitList(int parentLabel, List list) {
		if (!list.isEmpty()) {
			pushLabel(parentLabel);
			visitList(list);
			pop();
		}
	}

	private void pushLabel(int label) {
		push(label, "");
	}

	private void pushValuedNode(ASTNode node, String value) {
		push(node.getNodeType(), value);
	}

	private void pushEmptyNode(ASTNode node) {
		push(node.getNodeType(), "");
	}

	private void push(int label, String value) {
		Node n = new Node(label, value.trim(), new SourceCodeEntity(value
				.trim(), label, fStructureDepth));
		n.setStatementDepth(fStructureDepth);

		getCurrentParent().add(n);
		fStack.push(n);
	}

	private void pop() {
		fStack.pop();
	}

	private Node getCurrentParent() {
		return fStack.peek();
	}

	private void increaseLevel() {
		fStructureDepth++;
		fOverallDepth = fStructureDepth > fOverallDepth ? fStructureDepth
				: fOverallDepth;
	}
}
