/*
 * $Id: JensenShannonMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.external.owlsmx;

import simpack.api.impl.external.AbstractOWLSMXSimilarityMeasure;

/**
 * This is a wrapper class for owlsmx.similaritymeasures.JensenShannonMeasure of
 * the <a href="http://www.dfki.de/~klusch/owls-mx/">OWLS-MX Project</a>.
 * 
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class JensenShannonMeasure extends AbstractOWLSMXSimilarityMeasure {

	/**
	 * Constructor.
	 * <p>
	 * Takes two strings and returns the string similarity as computed by
	 * owlsmx.similaritymeasures.JensenShannonMeasure
	 * 
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 */
	public JensenShannonMeasure(String str1, String str2) {
		super(owlsmx.similaritymeasures.JensenShannonMeasure.class.getName(),
				str1, str2);
	}
}