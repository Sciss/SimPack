/*
 * $Id: JensenShannon.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.measure.it;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simpack.api.impl.AbstractSimilarityMeasure;
import simpack.util.conversion.CommonDistanceConversion;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * 
 */
public class JensenShannon extends AbstractSimilarityMeasure {

	private static Logger logger = LogManager.getLogger(JensenShannon.class);

	// natural logarithm (base e)
	private final static double log2 = Math.log(2.0);

	private double[] a, b;

	public JensenShannon(double[] a, double[] b) {
		this.a = a;
		this.b = b;
	}

	public boolean calculate() {
		setCalculated(false);
		double js = jensenShannonDivergence(a, b);
		
		similarity = new CommonDistanceConversion().convert(js);
		
		setCalculated(true);
		return true;
	}

	// Calculates the Jensen-Shannon divergence between the two distributions.
	private double jensenShannonDivergence(double[] a, double[] b) {
		double[] average = average(a, b);
		double kl1 = klDivergence(a, average);
		double kl2 = klDivergence(b, average);
		return (kl1 + kl2) / 2d;
	}

	// assumue same length
	private double[] average(double[] a, double[] b) {
		double[] average = new double[a.length];

		for (int i = 0; i < a.length; i++) {
			average[i] = (a[i] + b[i]) * 0.5d;
		}
		return average;
	}

	// assume counts sum up to 1.0
	private double klDivergence(double[] from, double[] to) {
		double result = 0d;

		for (int i = 0; i < from.length; i++) {
			double num = from[i];
			if (num == 0) {
				continue;
			}
			double num2 = to[i];
			double logFract = Math.log(num / num2);
			if (logFract == Double.NEGATIVE_INFINITY) {
				return Double.NEGATIVE_INFINITY;
			}
			result += num * (logFract / log2);
		}

		return result;
	}
}
