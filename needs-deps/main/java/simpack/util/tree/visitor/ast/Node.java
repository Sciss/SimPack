/*
 * $Id: Node.java 757 2008-04-17 17:42:53Z kiefer $
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

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Beat Fluri
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class Node extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 42L;

	private boolean fMatched = false;

	private boolean fOrdered = true;

	private int fLabel = -1;

	private String fValue = null;

	private SourceCodeEntity fEntity = null;

	private int fStatementDepth = -1;

	public Node() {
		super();
	}

	public Node(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public Node(Object userObject) {
		super(userObject);
	}

	public Node(int label, String value, SourceCodeEntity entity) {
		super();
		fLabel = label;
		fValue = value;
		fEntity = entity;
	}

	public void setUnmatched() {
		fMatched = false;
	}

	public void setMatched() {
		fMatched = true;
	}

	public boolean isMatched() {
		return fMatched;
	}

	public void setOutOfOrder() {
		fOrdered = false;
	}

	public void setInOrder() {
		fOrdered = true;
	}

	public boolean isInOrder() {
		return fOrdered;
	}

	public int getLabel() {
		return fLabel;
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		fValue = value;
	}

	public void setStatementDepth(int depth) {
		fStatementDepth = depth;
	}

	public int getStatementDepth() {
		return fStatementDepth;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		switch (fLabel) {
		case SourceCodeEntity.IF_STATEMENT:
			sb.append(controlStructure("if"));
			break;
		case SourceCodeEntity.WHILE_STATEMENT:
			sb.append(controlStructure("while"));
			break;
		case SourceCodeEntity.THEN_STATEMENT:
			sb.append("then");
			break;
		case SourceCodeEntity.ELSE_STATEMENT:
			sb.append("else");
			break;
		case SourceCodeEntity.RETURN_STATEMENT:
			sb.append("return " + fValue);
			break;
		default:
			sb.append(fValue);
			break;
		}
		return sb.toString();
	}

	private String controlStructure(String keyword) {
		return keyword + " (" + fValue + ")";
	}

	public SourceCodeEntity getEntity() {
		return fEntity;
	}

	public void setEntity(SourceCodeEntity entity) {
		fEntity = entity;
	}
}