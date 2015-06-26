/*
 * $Id: SourceCodeEntity.java 757 2008-04-17 17:42:53Z kiefer $
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

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * @author Beat Fluri
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class SourceCodeEntity {

	/*public static class SourceCodeEntityKeyword {
				
		private String fName = null;
		private int fTypeCode = 0;
		
		private SourceCodeEntityKeyword(String name, int code) {
			fName = name;
			fTypeCode = code;
		}
				
		public int getTypeCode() {
			return fTypeCode;
		}
		
		public String toString() {
			return fName;
		}
		
		public static ChangeType toChangeType(int typeCode) {
			return TYPES[typeCode];
		}

		private static final SourceCodeEntityKeyword[] KEYWORDS;
		
		static {
			KEYWORDS = new SourceCodeEntityKeyword[100];
			KEYWORDS[ARRAY_ACCESS] = new SourceCodeEntityKeyword("ArrayAccess", ARRAY_ACCESS);
			KEYWORDS[ARRAY_CREATION] = new SourceCodeEntityKeyword("ArrayCreation", ARRAY_CREATION);
			KEYWORDS[ARRAY_INITIALIZER] = new  SourceCodeEntityKeyword("ArrayType", ARRAY_TYPE);
			KEYWORDS[ASSERT_STATEMENT] = new SourceCodeEntityKeyword("AssertStatement", ASSERT_STATEMENT);
			KEYWORDS[ASSIGNMENT] = new SourceCodeEntityKeyword("Assignment", ASSIGNMENT);
			KEYWORDS[BOOLEAN_LITERAL] = new SourceCodeEntityKeyword("BooleanLiteral", BOOLEAN_LITERAL);
			KEYWORDS[BREAK_STATEMENT] = new SourceCodeEntityKeyword("BreakStatement", BREAK_STATEMENT);
			KEYWORDS[CAST_EXPRESSION] = new SourceCodeEntityKeyword("CastExpression", CAST_EXPRESSION);
			KEYWORDS[CATCH_CLAUSE] = new SourceCodeEntityKeyword("CatchClause", CATCH_CLAUSE);
			KEYWORDS[CHARACTER_LITERAL] = new SourceCodeEntityKeyword("CharacterLiteral", CHARACTER_LITERAL);
			KEYWORDS[CLASS_INSTANCE_CREATION] = new SourceCodeEntityKeyword("ClassInstanceCreation", CLASS_INSTANCE_CREATION);
			KEYWORDS[COMPILATION_UNIT] = new SourceCodeEntityKeyword("CompilationUnit", COMPILATION_UNIT);
			KEYWORDS[]
		}
		
	}*/

	public static final int ARRAY_ACCESS = ASTNode.ARRAY_ACCESS;
	public static final int ARRAY_CREATION = ASTNode.ARRAY_CREATION;
	public static final int ARRAY_INITIALIZER = ASTNode.ARRAY_INITIALIZER;
	public static final int ARRAY_TYPE = ASTNode.ARRAY_TYPE;
	public static final int ASSERT_STATEMENT = ASTNode.ASSERT_STATEMENT;
	public static final int ASSIGNMENT = ASTNode.ASSIGNMENT;
	public static final int BLOCK = ASTNode.BLOCK;
	public static final int BOOLEAN_LITERAL = ASTNode.BOOLEAN_LITERAL;
	public static final int BREAK_STATEMENT = ASTNode.BREAK_STATEMENT;
	public static final int CAST_EXPRESSION = ASTNode.CAST_EXPRESSION;
	public static final int CATCH_CLAUSE = ASTNode.CATCH_CLAUSE;
	public static final int CHARACTER_LITERAL = ASTNode.CHARACTER_LITERAL;
	public static final int CLASS_INSTANCE_CREATION = ASTNode.CLASS_INSTANCE_CREATION;
	public static final int COMPILATION_UNIT = ASTNode.COMPILATION_UNIT;
	public static final int CONDITIONAL_EXPRESSION = ASTNode.CONDITIONAL_EXPRESSION;
	public static final int CONSTRUCTOR_INVOCATION = ASTNode.CONSTRUCTOR_INVOCATION;
	public static final int CONTINUE_STATEMENT = ASTNode.CONTINUE_STATEMENT;
	public static final int DO_STATEMENT = ASTNode.DO_STATEMENT;
	public static final int EMPTY_STATEMENT = ASTNode.EMPTY_STATEMENT;
	public static final int EXPRESSION_STATEMENT = ASTNode.EXPRESSION_STATEMENT;
	public static final int FIELD_ACCESS = ASTNode.FIELD_ACCESS;
	public static final int FIELD_DECLARATION = ASTNode.FIELD_DECLARATION;
	public static final int FOR_STATEMENT = ASTNode.FOR_STATEMENT;
	public static final int IF_STATEMENT = ASTNode.IF_STATEMENT;
	public static final int IMPORT_DECLARATION = ASTNode.IMPORT_DECLARATION;
	public static final int INFIX_EXPRESSION = ASTNode.INFIX_EXPRESSION;
	public static final int INITIALIZER = ASTNode.INITIALIZER;
	public static final int JAVADOC = ASTNode.JAVADOC;
	public static final int LABELED_STATEMENT = ASTNode.LABELED_STATEMENT;
	public static final int METHOD_DECLARATION = ASTNode.METHOD_DECLARATION;
	public static final int METHOD_INVOCATION = ASTNode.METHOD_INVOCATION;
	public static final int NULL_LITERAL = ASTNode.NULL_LITERAL;
	public static final int NUMBER_LITERAL = ASTNode.NUMBER_LITERAL;
	public static final int PACKAGE_DECLARATION = ASTNode.PACKAGE_DECLARATION;
	public static final int PARENTHESIZED_EXPRESSION = ASTNode.PARENTHESIZED_EXPRESSION;
	public static final int POSTFIX_EXPRESSION = ASTNode.POSTFIX_EXPRESSION;
	public static final int PREFIX_EXPRESSION = ASTNode.PREFIX_EXPRESSION;
	public static final int PRIMITIVE_TYPE = ASTNode.PRIMITIVE_TYPE;
	public static final int QUALIFIED_NAME = ASTNode.QUALIFIED_NAME;
	public static final int RETURN_STATEMENT = ASTNode.RETURN_STATEMENT;
	public static final int SIMPLE_NAME = ASTNode.SIMPLE_NAME;
	public static final int SIMPLE_TYPE = ASTNode.SIMPLE_TYPE;
	public static final int SINGLE_VARIABLE_DECLARATION = ASTNode.SINGLE_VARIABLE_DECLARATION;
	public static final int STRING_LITERAL = ASTNode.STRING_LITERAL;
	public static final int SUPER_CONSTRUCTOR_INVOCATION = ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
	public static final int SUPER_FIELD_ACCESS = ASTNode.SUPER_FIELD_ACCESS;
	public static final int SUPER_METHOD_INVOCATION = ASTNode.SUPER_METHOD_INVOCATION;
	public static final int SWITCH_CASE = ASTNode.SWITCH_CASE;
	public static final int SWITCH_STATEMENT = ASTNode.SWITCH_STATEMENT;
	public static final int SYNCHRONIZED_STATEMENT = ASTNode.SYNCHRONIZED_STATEMENT;
	public static final int THIS_EXPRESSION = ASTNode.THIS_EXPRESSION;
	public static final int THROW_STATEMENT = ASTNode.THROW_STATEMENT;
	public static final int TRY_STATEMENT = ASTNode.TRY_STATEMENT;
	public static final int TYPE_DECLARATION = ASTNode.TYPE_DECLARATION;
	public static final int TYPE_DECLARATION_STATEMENT = ASTNode.TYPE_DECLARATION_STATEMENT;
	public static final int TYPE_LITERAL = ASTNode.TYPE_LITERAL;
	public static final int VARIABLE_DECLARATION_EXPRESSION = ASTNode.VARIABLE_DECLARATION_EXPRESSION;
	public static final int VARIABLE_DECLARATION_FRAGMENT = ASTNode.VARIABLE_DECLARATION_FRAGMENT;
	public static final int VARIABLE_DECLARATION_STATEMENT = ASTNode.VARIABLE_DECLARATION_STATEMENT;
	public static final int WHILE_STATEMENT = ASTNode.WHILE_STATEMENT;
	public static final int INSTANCEOF_EXPRESSION = ASTNode.INSTANCEOF_EXPRESSION;
	public static final int LINE_COMMENT = ASTNode.LINE_COMMENT;
	public static final int BLOCK_COMMENT = ASTNode.BLOCK_COMMENT;
	public static final int TAG_ELEMENT = ASTNode.TAG_ELEMENT;
	public static final int TEXT_ELEMENT = ASTNode.TEXT_ELEMENT;
	public static final int MEMBER_REF = ASTNode.MEMBER_REF;
	public static final int METHOD_REF = ASTNode.MEMBER_REF;
	public static final int METHOD_REF_PARAMETER = ASTNode.METHOD_REF_PARAMETER;
	public static final int ENHANCED_FOR_STATEMENT = ASTNode.ENHANCED_FOR_STATEMENT;
	public static final int ENUM_DECLARATION = ASTNode.ENUM_DECLARATION;
	public static final int ENUM_CONSTANT_DECLARATION = ASTNode.ENUM_CONSTANT_DECLARATION;
	public static final int TYPE_PARAMETER = ASTNode.TYPE_PARAMETER;
	public static final int PARAMETERIZED_TYPE = ASTNode.PARAMETERIZED_TYPE;
	public static final int QUALIFIED_TYPE = ASTNode.QUALIFIED_TYPE;
	public static final int WILDCARD_TYPE = ASTNode.WILDCARD_TYPE;
	public static final int NORMAL_ANNOTATION = ASTNode.NORMAL_ANNOTATION;
	public static final int MARKER_ANNOTATION = ASTNode.MARKER_ANNOTATION;
	public static final int SINGLE_MEMBER_ANNOTATION = ASTNode.SINGLE_MEMBER_ANNOTATION;
	public static final int MEMBER_VALUE_PAIR = ASTNode.MEMBER_VALUE_PAIR;
	public static final int ANNOTATION_TYPE_DECLARATION = ASTNode.ANNOTATION_TYPE_DECLARATION;
	public static final int ANNOTATION_TYPE_MEMBER_DECLARATION = ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION;
	public static final int MODIFIER = ASTNode.MODIFIER;
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
	public final static int CLASS = 108;
	public final static int METHOD = 109;
	public final static int ATTRIBUTE = 110;
	
	private Long id;
	
	private String fName = null;
	private String fUniqueName = null;
	private int fType = -1;
	private int fDepth = 0;
	
	public SourceCodeEntity() { }
	
	public SourceCodeEntity(String name, int type) {
		this(name, name, type, -1);
	}
	
	public SourceCodeEntity(String name, int type, int depth) {
		this(name, name, type, depth);
	}
	
	public SourceCodeEntity(
			String name,
			String uniqueName,
			int type,
			int depth)
	{
		fName = name;
		fUniqueName = uniqueName;
		fType = type;
		fDepth = depth;
	}

	protected void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}

	public void setName(String name) {
		fName = name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return fName;
	}

	/**
	 * @return Returns the uniqueName.
	 */
	public String getUniqueName() {
		return fUniqueName;
	}
	
	public void setUniqueName(String uniqueName) {
		fUniqueName = uniqueName;
	}
	
	public int getType() {
		return fType;
	}
	
	public static String getSourceCodeEntityKeyword(int keyword) {
		switch (keyword) {
		case THEN_STATEMENT:
			return "ThenBlock";
		case ELSE_STATEMENT:
			return "ElseBlock";
		case ARRAY_DIMENSION:
			return "ArrayDimension";
		case TYPE_ARGUMENTS:
			return "TypeArguments";
		case ARGUMENTS:
			return "Arguments";
		case BODY:
			return "Body";
		case MODIFIERS:
			return "Modifiers";
		case SUPER_INTERFACE_TYPES:
			return "SuperInterfaces";
		case ENUM_CONSTANTS:
			return "EnumConstants";
		case BODY_DECLARATIONS:
			return "BodyDeclarations";
		case FRAGMENTS:
			return "Fragments";
		case INITIALIZERS:
			return "Initializers";
		case UPDATERS:
			return "Updaters";
		case EXTENDED_OPERANDS:
			return "ExtendedOperands";
		case PARAMETERS:
			return "Parameters";
		case CATCH_CLAUSES:
			return "CatchClauses";
		case FINALLY:
			return "FinallyBlock";
		case THROW:
			return "Throw";
		case CLASS:
			return "Class";
		case METHOD:
			return "Method";
		case ATTRIBUTE:
			return "Attribute";
		default:
			String type = ASTNode.nodeClassForType(keyword).toString();
			return type.substring(type.lastIndexOf('.') + 1);
		}
	}
	
	public void setType(int type) {
		fType = type;
	}
	
	public int getDepth() {
		return fDepth;
	}
	
	public void setDepth(int depth) {
		fDepth = depth;
	}
	
	public boolean equals(Object other) {
		if (! (other instanceof SourceCodeEntity)) {
			return false;
		} else {
			SourceCodeEntity o = (SourceCodeEntity) other;
			return o.getType() == getType() &&
				o.getUniqueName().equals(getUniqueName());
		}
	}
}