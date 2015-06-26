/*
 * $Id: ASTFullTransformer.java 757 2008-04-17 17:42:53Z kiefer $
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
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import simpack.api.ITreeNode;
import simpack.util.tree.TreeNode;

/**
 * @author Beat Fluri
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class ASTFullTransformer extends ASTVisitor {

	public final static int THEN_STATEMENT = 90;

	public final static int ELSE_STATEMENT = 91;

	public final static int ARRAY_DIMENSION = 92;

	public final static int TYPE_ARGUMENTS = 93;

	public final static int ARGUMENTS = 94;

	public final static int BODY = 95;

	public final static int MODIFIERS = 96;

	public final static int SUPER_INTERFACE_TYPES = 97;

	public final static int ENUM_CONSTANTS = 98;

	public final static int BODY_DECLARATIONS = 99;

	public final static int FRAGMENTS = 100;

	public final static int INITIALIZERS = 101;

	public final static int UPDATERS = 102;

	public final static int EXTENDED_OPERANDS = 103;

	public final static int PARAMETERS = 104;

	public final static int CATCH_CLAUSES = 105;

	public final static int FINALLY = 106;

	public final static int THROW = 107;

	/**
	 * General leaf label
	 */
	public final static int IDENTIFIER = 128;

	private Stack<ITreeNode> fStack = new Stack<ITreeNode>();

	public ASTFullTransformer() {
		fStack.clear();
	}

	public ASTFullTransformer(ITreeNode root) {
		fStack.clear();
		fStack.push(root);
	}

	public boolean visit(AnnotationTypeDeclaration node) {
		// ignore
		return false;
	}

	public void endVisit(AnnotationTypeDeclaration node) {
		// do nothing, since is ignored
	}

	public boolean visit(AnnotationTypeMemberDeclaration node) {
		// ignore
		return false;
	}

	public void endVisit(AnnotationTypeMemberDeclaration node) {
		// do nothing, since is ignored
	}

	public boolean visit(AnonymousClassDeclaration node) {
		push(node);
		return true;
	}

	public void endVisit(AnonymousClassDeclaration node) {
		pop();
	}

	public boolean visit(ArrayAccess node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ArrayAccess node) {
		pop();
	}

	public boolean visit(ArrayCreation node) {
		pushEmptyNode(node);

		node.getType().accept(this);
		if (node.dimensions().isEmpty()) {
			node.getInitializer().accept(this);
		} else {
			visitList(ARRAY_DIMENSION, node.dimensions());
		}
		return false;
	}

	public void endVisit(ArrayCreation node) {
		pop();
	}

	public boolean visit(ArrayInitializer node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ArrayInitializer node) {
		pop();
	}

	public boolean visit(ArrayType node) {
		return true;
	}

	public void endVisit(ArrayType node) {
		// do nothing
	}

	public boolean visit(AssertStatement node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(AssertStatement node) {
		pop();
	}

	public boolean visit(Assignment node) {
		return false;
	}

	public void endVisit(Assignment node) {
		// do nothing
	}

	public boolean visit(Block node) {
		return true;
	}

	public void endVisit(Block node) {
		// do nothing
	}

	public boolean visit(BlockComment node) {
		// not interesting at the moment
		return false;
	}

	public void endVisit(BlockComment node) {
		// do nothing
	}

	public boolean visit(BooleanLiteral node) {
		pushValuedNode(node, String.valueOf(node.booleanValue()));
		return false;
	}

	public void endVisit(BooleanLiteral node) {
		pop();
	}

	public boolean visit(BreakStatement node) {
		String label = node.getLabel() != null ? node.getLabel().toString()
				: "";
		pushValuedNode(node, label);
		return false;
	}

	public void endVisit(BreakStatement node) {
		pop();
	}

	public boolean visit(CastExpression node) {
		pushEmptyNode(node);
		return false;
	}

	public void endVisit(CastExpression node) {
		pop();
	}

	public boolean visit(CatchClause node) {
		SimpleType exception = (SimpleType) node.getException().getType();
		pushValuedNode(node, exception.getName().getFullyQualifiedName());

		if (node.getBody() != null) {
			// since exception type is used as value, visit children by hand
			node.getBody().accept(this);
		}
		return false;
	}

	public void endVisit(CatchClause node) {
		pop();
	}

	public boolean visit(CharacterLiteral node) {
		pushValuedNode(node, String.valueOf(node.charValue()));
		return false;
	}

	public void endVisit(CharacterLiteral node) {
		pop();
	}

	public boolean visit(ClassInstanceCreation node) {
		pushEmptyNode(node);

		// add expression only if exists
		if (node.getExpression() != null) {
			node.getExpression().accept(this);
		}

		// add type arguments to tree only if is not empty
		visitList(TYPE_ARGUMENTS, node.typeArguments());

		node.getType().accept(this);

		// add arguments to tree only if is not empty
		visitList(ARGUMENTS, node.arguments());

		// add anonymous class declaration only if exists
		if (node.getAnonymousClassDeclaration() != null) {
			node.getAnonymousClassDeclaration().accept(this);
		}

		// children visited by hand
		return false;
	}

	public void endVisit(ClassInstanceCreation node) {
		pop();
	}

	public boolean visit(CompilationUnit node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(CompilationUnit node) {
		pop();
	}

	public boolean visit(ConditionalExpression node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ConditionalExpression node) {
		pop();
	}

	public boolean visit(ConstructorInvocation node) {
		pushEmptyNode(node);

		// add type arguments only if is not empty
		visitList(TYPE_ARGUMENTS, node.typeArguments());

		// add arguments only if is not empty
		visitList(ARGUMENTS, node.arguments());

		return false;
	}

	public void endVisit(ConstructorInvocation node) {
		pop();
	}

	public boolean visit(ContinueStatement node) {
		String label = node.getLabel() != null ? node.getLabel().toString()
				: "";
		pushValuedNode(node, label);
		return false;
	}

	public void endVisit(ContinueStatement node) {
		pop();
	}

	public boolean visit(DoStatement node) {
		pushEmptyNode(node);

		// change ordering
		node.getExpression().accept(this);
		pushLabel(BODY);
		node.getBody().accept(this);
		pop();
		return false;
	}

	public void endVisit(DoStatement node) {
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
		pushEmptyNode(node);

		// visit children by hand to introduce BODY node
		node.getParameter().accept(this);
		node.getExpression().accept(this);
		pushLabel(BODY);
		node.getBody().accept(this);
		pop();
		return false;
	}

	public void endVisit(EnhancedForStatement node) {
		pop();
	}

	public boolean visit(EnumConstantDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());

		// visit children by hand because Name is used as value
		if (node.getJavadoc() != null) {
			node.getJavadoc().accept(this);
		}

		visitList(MODIFIERS, node.modifiers());
		visitList(ARGUMENTS, node.arguments());

		if (node.getAnonymousClassDeclaration() != null) {
			node.getAnonymousClassDeclaration().accept(this);
		}

		return false;
	}

	public void endVisit(EnumConstantDeclaration node) {
		pop();
	}

	public boolean visit(EnumDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());

		// visit children by hand becuase Name is used as value
		if (node.getJavadoc() != null) {
			node.getJavadoc().accept(this);
		}

		visitList(MODIFIERS, node.modifiers());
		visitList(SUPER_INTERFACE_TYPES, node.superInterfaceTypes());
		visitList(ENUM_CONSTANTS, node.enumConstants());
		visitList(BODY_DECLARATIONS, node.bodyDeclarations());

		return false;
	}

	public void endVisit(EnumDeclaration node) {
		pop();
	}

	public boolean visit(ExpressionStatement node) {
		// skip; only containing expression is interesting
		return true;
	}

	public void endVisit(ExpressionStatement node) {
		// do nothing; see visit(ExpressionStatement)
	}

	public boolean visit(FieldAccess node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		node.getExpression().accept(this);
		return false;
	}

	public void endVisit(FieldAccess node) {
		pop();
	}

	public boolean visit(FieldDeclaration node) {
		pushEmptyNode(node);
		return false;
	}

	public void endVisit(FieldDeclaration node) {
		pop();
	}

	public boolean visit(ForStatement node) {
		pushEmptyNode(node);
		visitList(INITIALIZERS, node.initializers());
		if (node.getExpression() != null) {
			node.getExpression().accept(this);
		}
		visitList(UPDATERS, node.updaters());
		pushLabel(BODY);
		node.getBody().accept(this);
		return false;
	}

	public void endVisit(ForStatement node) {
		pop();
	}

	public boolean visit(IfStatement node) {
		node.getExpression().accept(this);

		if (node.getThenStatement() != null) {
			pushLabel(THEN_STATEMENT);
			node.getThenStatement().accept(this);
			pop();
		}
		if (node.getElseStatement() != null) {
			pushLabel(ELSE_STATEMENT);
			node.getElseStatement().accept(this);
			pop();
		}
		return false;
	}

	public void endVisit(IfStatement node) {
		pop();
	}

	public boolean visit(ImportDeclaration node) {
		String name = node.getName().getFullyQualifiedName();
		if (node.isOnDemand()) {
			name += ".*";
		}
		pushValuedNode(node, name);
		return false;
	}

	public void endVisit(ImportDeclaration node) {
		pop();
	}

	public boolean visit(InfixExpression node) {
		pushValuedNode(node, node.getOperator().toString());
		node.getLeftOperand().accept(this);
		node.getRightOperand().accept(this);
		visitList(EXTENDED_OPERANDS, node.extendedOperands());
		return false;
	}

	public void endVisit(InfixExpression node) {
		pop();
	}

	public boolean visit(Initializer node) {
		pushEmptyNode(node);
		if (node.getJavadoc() != null) {
			node.getJavadoc().accept(this);
		}
		if (node.getBody() != null) {
			pushLabel(BODY);
			node.getBody().accept(this);
			pop();
		}
		return false;
	}

	public void endVisit(Initializer node) {
		pop();
	}

	public boolean visit(InstanceofExpression node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(InstanceofExpression node) {
		pop();
	}

	public boolean visit(Javadoc node) {
		return false;
	}

	public void endVisit(Javadoc node) {
		// do nothing
	}

	public boolean visit(LabeledStatement node) {
		pushValuedNode(node, node.getLabel().getFullyQualifiedName());
		node.getBody().accept(this);
		return false;
	}

	public void endVisit(LabeledStatement node) {
		pop();
	}

	public boolean visit(LineComment node) {
		// ignore
		return false;
	}

	public void endVisit(LineComment node) {
		// do nothing, since is ignored
	}

	public boolean visit(MarkerAnnotation node) {
		// ignore
		return false;
	}

	public void endVisit(MarkerAnnotation node) {
		// do nothing, since is ignored
	}

	public boolean visit(MemberRef node) {
		// ignore
		return false;
	}

	public void endVisit(MemberRef node) {
		// do nothing, since is ignored
	}

	public boolean visit(MemberValuePair node) {
		// ignore
		return false;
	}

	public void endVisit(MemberValuePair node) {
		// do nothing, since is ignored
	}

	public boolean visit(MethodDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());

		if (node.getReturnType2() != null) {
			node.getReturnType2().accept(this);
		}
		visitList(TYPE_ARGUMENTS, node.typeParameters());
		visitList(PARAMETERS, node.parameters());
		if (node.getBody() != null) {
			node.getBody().accept(this);
		}
		return false;
	}

	public void endVisit(MethodDeclaration node) {
		pop();
	}

	public boolean visit(MethodInvocation node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		if (node.getExpression() != null) {
			node.getExpression().accept(this);
		}
		visitList(TYPE_ARGUMENTS, node.typeArguments());
		visitList(ARGUMENTS, node.arguments());
		return false;
	}

	public void endVisit(MethodInvocation node) {
		pop();
	}

	public boolean visit(MethodRef node) {
		// ignore
		return false;
	}

	public void endVisit(MethodRef node) {
		// do nothing, since is ignored
	}

	public boolean visit(MethodRefParameter node) {
		// ignore
		return false;
	}

	public void endVisit(MethodRefParameter node) {
		// do nothing, since is ignored
	}

	public boolean visit(Modifier node) {
		return false;
	}

	public void endVisit(Modifier node) {
		// do nothing
	}

	public boolean visit(NormalAnnotation node) {
		// ignore
		return false;
	}

	public void endVisit(NormalAnnotation node) {
		// do nothing, since is ignored
	}

	public boolean visit(NullLiteral node) {
		return false;
	}

	public void endVisit(NullLiteral node) {
		// do nothing
	}

	public boolean visit(NumberLiteral node) {
		return false;
	}

	public void endVisit(NumberLiteral node) {
		// do nothing
	}

	public boolean visit(PackageDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		if (node.getJavadoc() != null) {
			node.getJavadoc().accept(this);
		}
		return false;
	}

	public void endVisit(PackageDeclaration node) {
		pop();
	}

	public boolean visit(ParameterizedType node) {
		pushEmptyNode(node);
		node.getType().accept(this);
		visitList(TYPE_ARGUMENTS, node.typeArguments());
		return false;
	}

	public void endVisit(ParameterizedType node) {
		pop();
	}

	public boolean visit(ParenthesizedExpression node) {
		return true;
	}

	public void endVisit(ParenthesizedExpression node) {
		// do nothing
	}

	public boolean visit(PostfixExpression node) {
		pushValuedNode(node, node.getOperator().toString());
		return true;
	}

	public void endVisit(PostfixExpression node) {
		pop();
	}

	public boolean visit(PrefixExpression node) {
		pushValuedNode(node, node.getOperator().toString());
		return true;
	}

	public void endVisit(PrefixExpression node) {
		pop();
	}

	public boolean visit(PrimitiveType node) {
		pushValuedNode(node, node.getPrimitiveTypeCode().toString());
		return false;
	}

	public void endVisit(PrimitiveType node) {
		pop();
	}

	public boolean visit(QualifiedName node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(QualifiedName node) {
		pop();
	}

	public boolean visit(QualifiedType node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(QualifiedType node) {
		pop();
	}

	public boolean visit(ReturnStatement node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ReturnStatement node) {
		pop();
	}

	public boolean visit(SimpleName node) {
		pushValuedNode(node, node.getIdentifier());
		return false;
	}

	public void endVisit(SimpleName node) {
		pop();
	}

	public boolean visit(SimpleType node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		return false;
	}

	public void endVisit(SimpleType node) {
		pop();
	}

	public boolean visit(SingleMemberAnnotation node) {
		// ignore
		return false;
	}

	public void endVisit(SingleMemberAnnotation node) {
		pop();
	}

	public boolean visit(SingleVariableDeclaration node) {
		pushValuedNode(node, node.getName().getIdentifier());
		// pushEmptyNode(node);
		visitList(MODIFIERS, node.modifiers());
		node.getType().accept(this);
		if (node.getInitializer() != null) {
			node.getInitializer().accept(this);
		}
		return false;
	}

	public void endVisit(SingleVariableDeclaration node) {
		pop();
	}

	public boolean visit(StringLiteral node) {
		pushValuedNode(node, node.getLiteralValue());
		return false;
	}

	public void endVisit(StringLiteral node) {
		pop();
	}

	public boolean visit(SuperConstructorInvocation node) {
		pushEmptyNode(node);
		if (node.getExpression() != null) {
			node.getExpression().accept(this);
		}
		visitList(TYPE_ARGUMENTS, node.typeArguments());
		visitList(ARGUMENTS, node.arguments());
		return false;
	}

	public void endVisit(SuperConstructorInvocation node) {
		pop();
	}

	public boolean visit(SuperFieldAccess node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		node.getQualifier().accept(this);
		return false;
	}

	public void endVisit(SuperFieldAccess node) {
		pop();
	}

	public boolean visit(SuperMethodInvocation node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		node.getQualifier().accept(this);
		visitList(TYPE_ARGUMENTS, node.typeArguments());
		visitList(ARGUMENTS, node.arguments());
		return false;
	}

	public void endVisit(SuperMethodInvocation node) {
		pop();
	}

	public boolean visit(SwitchCase node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(SwitchCase node) {
		pop();
	}

	public boolean visit(SwitchStatement node) {
		pushEmptyNode(node);
		node.getExpression().accept(this);
		visitList(BODY, node.statements());
		return false;
	}

	public void endVisit(SwitchStatement node) {
		pop();
	}

	public boolean visit(SynchronizedStatement node) {
		pushEmptyNode(node);
		node.getExpression().accept(this);
		pushLabel(BODY);
		node.getBody().accept(this);
		pop();
		return false;
	}

	public void endVisit(SynchronizedStatement node) {
		pop();
	}

	public boolean visit(TagElement node) {
		// ignore
		return false;
	}

	public void endVisit(TagElement node) {
		// do nothing, since is ignored
	}

	public boolean visit(TextElement node) {
		pushValuedNode(node, node.getText());
		return false;
	}

	public void endVisit(TextElement node) {
		pop();
	}

	public boolean visit(ThisExpression node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ThisExpression node) {
		pop();
	}

	public boolean visit(ThrowStatement node) {
		pushEmptyNode(node);
		return true;
	}

	public void endVisit(ThrowStatement node) {
		pop();
	}

	public boolean visit(TryStatement node) {
		pushEmptyNode(node);
		pushLabel(BODY);
		node.getBody().accept(this);
		pop();
		visitList(CATCH_CLAUSES, node.catchClauses());
		pushLabel(FINALLY);
		node.getFinally();
		pop();
		return false;
	}

	public void endVisit(TryStatement node) {
		pop();
	}

	public boolean visit(TypeDeclaration node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		if (node.getJavadoc() != null) {
			node.getJavadoc().accept(this);
		}
		visitList(MODIFIERS, node.modifiers());
		visitList(TYPE_ARGUMENTS, node.typeParameters());
		if (node.getSuperclassType() != null) {
			node.getSuperclassType().accept(this);
		}
		visitList(SUPER_INTERFACE_TYPES, node.superInterfaceTypes());
		visitList(BODY_DECLARATIONS, node.bodyDeclarations());
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
		visitList(MODIFIERS, node.modifiers());
		node.getType().accept(this);
		visitList(FRAGMENTS, node.fragments());
		return false;
	}

	public void endVisit(VariableDeclarationExpression node) {
		pop();
	}

	public boolean visit(VariableDeclarationFragment node) {
		pushValuedNode(node, node.getName().getFullyQualifiedName());
		if (node.getInitializer() != null) {
			node.getInitializer().accept(this);
		}
		return false;
	}

	public void endVisit(VariableDeclarationFragment node) {
		pop();
	}

	public boolean visit(VariableDeclarationStatement node) {
		pushEmptyNode(node);
		visitList(MODIFIERS, node.modifiers());
		node.getType().accept(this);
		visitList(FRAGMENTS, node.fragments());
		return false;
	}

	public void endVisit(VariableDeclarationStatement node) {
		pop();
	}

	public boolean visit(WhileStatement node) {
		pushEmptyNode(node);
		node.getExpression().accept(this);
		pushLabel(BODY);
		node.getBody().accept(this);
		pop();
		return false;
	}

	public void endVisit(WhileStatement node) {
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
		push(new Integer(label));
	}

	private void pushEmptyNode(ASTNode node) {
		push(node);
	}

	private void pushValuedNode(ASTNode node, String value) {
		push(node);
	}

	private void push(Object node) {
		TreeNode n = new TreeNode(node);
		if (!fStack.isEmpty()) {
			getCurrentParent().add(n);
		}
		fStack.push(n);
	}

	private void pop() {
		fStack.pop();
	}

	private ITreeNode getCurrentParent() {
		return fStack.peek();
	}
}
