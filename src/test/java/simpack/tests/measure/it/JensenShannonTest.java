/*
 * $Id: JensenShannonTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.it;

import junit.framework.TestCase;
import simpack.measure.it.JensenShannon;

/**
 * @author Christoph Kiefer
 * @version $Id: JensenShannonTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JensenShannonTest extends TestCase {

	private double[] a, b, average;

	public void setUp() {
		a = new double[] { 1d / 11d, 1d / 11d, 1d / 11d, 1d / 11d, 1d / 11d,
				1d / 11d, 1d / 11d, 1d / 11d, 1d / 11d, 1d / 11d, 1d / 11d, 0,
				0, 0 };

		b = new double[] { 0, 0, 0, 1d / 9d, 0, 1d / 9d, 2d / 9d, 0, 0,
				1d / 9d, 1d / 9d, 1d / 9d, 1d / 9d, 1d / 9d };

		average = new double[] { 1d / 22d, 1d / 22d, 1d / 22d,
				(1d / 11d + 1d / 9d) * 0.5, 1d / 22d,
				(1d / 11d + 1d / 9d) * 0.5, (1d / 11d + 2d / 9d) * 0.5,
				1d / 22d, 1d / 22d, (1d / 11d + 1d / 9d) * 0.5,
				(1d / 11d + 1d / 9d) * 0.5, 1d / 18d, 1d / 18d, 1d / 18d };

	}

	public void test() {
		JensenShannon js = null;
		js = new JensenShannon(a, b);

		assertNotNull(js);
		assertTrue(js.calculate());
		assertTrue(js.isCalculated());

		double result1 = 0d;
		for (int i = 0; i < a.length; i++) {
			double num = a[i];
			if (num == 0) {
				continue;
			}
			double num2 = average[i];
			double logFract = Math.log(num / num2);
			result1 += num * (logFract / Math.log(2));
		}

		double result2 = 0d;
		for (int i = 0; i < b.length; i++) {
			double num = b[i];
			if (num == 0) {
				continue;
			}
			double num2 = average[i];
			double logFract = Math.log(num / num2);
			result2 += num * (logFract / Math.log(2));
		}

		System.out.println(js.getSimilarity());
		assertEquals(js.getSimilarity(), 1d / (1d + (result1 + result2) / 2d));
	}

	public void testSame() {
		JensenShannon js = null;
		js = new JensenShannon(a, a);

		assertNotNull(js);
		assertTrue(js.calculate());
		assertTrue(js.isCalculated());

		assertEquals(js.getSimilarity(), 1d);
	}

}