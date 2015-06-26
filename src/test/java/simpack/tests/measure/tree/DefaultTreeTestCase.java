/*
 * $Id: DefaultTreeTestCase.java 757 2008-04-17 17:42:53Z kiefer $
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

import junit.framework.TestCase;
import simpack.util.tree.TreeNode;

/**
 * @author Christoph Kiefer
 * @version $Id: DefaultTreeTestCase.java 757 2008-04-17 17:42:53Z kiefer $
 */
public abstract class DefaultTreeTestCase extends TestCase {

	protected TreeNode tree1, tree2;

	protected TreeNode t1n1, t1n2, t1n3, t1n4, t1n5, t1n6, t1n7, t1n8, t1n9,
			t1n10, t1n11, t1n12, t1n13, t1n14, t1n15;

	protected TreeNode t2n1, t2n2, t2n3, t2n4, t2n5, t2n6, t2n7, t2n8, t2n9,
			t2n10, t2n11, t2n12, t2n13, t2n14, t2n15, t2n16, t2n17, t2n18;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		tree1 = generateSampleT1();
		tree2 = generateSampleT2();
	}

	/**
	 * Constructs the sample tree T1 on page 231 (Fig. 4.17) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T1
	 */
	protected TreeNode generateSampleT1() {
		t1n1 = new TreeNode(new String("t1n1"));
		t1n2 = new TreeNode(new String("t1n2"));
		t1n3 = new TreeNode(new String("t1n3"));
		t1n4 = new TreeNode(new String("t1n4"));
		t1n5 = new TreeNode(new String("t1n5"));
		t1n6 = new TreeNode(new String("t1n6"));
		t1n7 = new TreeNode(new String("t1n7"));
		t1n8 = new TreeNode(new String("t1n8"));
		t1n9 = new TreeNode(new String("t1n9"));
		t1n10 = new TreeNode(new String("t1n10"));
		t1n11 = new TreeNode(new String("t1n11"));
		t1n12 = new TreeNode(new String("t1n12"));
		t1n13 = new TreeNode(new String("t1n13"));
		t1n14 = new TreeNode(new String("t1n14"));
		t1n15 = new TreeNode(new String("t1n15"));

		// left
		t1n4.add(t1n2);
		t1n4.add(t1n3);
		t1n5.add(t1n4);
		t1n6.add(t1n1);
		t1n6.add(t1n5);
		t1n8.add(t1n6);
		t1n8.add(t1n7);

		// right
		t1n11.add(t1n9);
		t1n11.add(t1n10);
		t1n12.add(t1n11);
		t1n14.add(t1n12);
		t1n14.add(t1n13);

		// root
		t1n15.add(t1n8);
		t1n15.add(t1n14);

		return t1n15;
	}

	/**
	 * Constructs the sample tree T2 on page 231 (Fig. 4.17) from "Algorithms on
	 * trees and graphs" by Gabriel Valiente
	 * 
	 * @return sample tree T2
	 */
	protected TreeNode generateSampleT2() {
		t2n1 = new TreeNode(new String("t2n1"));
		t2n2 = new TreeNode(new String("t2n2"));
		t2n3 = new TreeNode(new String("t2n3"));
		t2n4 = new TreeNode(new String("t2n4"));
		t2n5 = new TreeNode(new String("t2n5"));
		t2n6 = new TreeNode(new String("t2n6"));
		t2n7 = new TreeNode(new String("t2n7"));
		t2n8 = new TreeNode(new String("t2n8"));
		t2n9 = new TreeNode(new String("t2n9"));
		t2n10 = new TreeNode(new String("t2n10"));
		t2n11 = new TreeNode(new String("t2n11"));
		t2n12 = new TreeNode(new String("t2n12"));
		t2n13 = new TreeNode(new String("t2n13"));
		t2n14 = new TreeNode(new String("t2n14"));
		t2n15 = new TreeNode(new String("t2n15"));
		t2n16 = new TreeNode(new String("t2n16"));
		t2n17 = new TreeNode(new String("t2n17"));
		t2n18 = new TreeNode(new String("t2n18"));

		// left
		t2n3.add(t2n2);
		t2n4.add(t2n1);
		t2n4.add(t2n3);

		// middle
		t2n8.add(t2n6);
		t2n8.add(t2n7);
		t2n9.add(t2n8);
		t2n11.add(t2n9);
		t2n11.add(t2n10);
		t2n12.add(t2n5);
		t2n12.add(t2n11);

		// right
		t2n16.add(t2n15);
		t2n17.add(t2n13);
		t2n17.add(t2n14);
		t2n17.add(t2n16);

		// root
		t2n18.add(t2n4);
		t2n18.add(t2n12);
		t2n18.add(t2n17);

		return t2n18;
	}
}
