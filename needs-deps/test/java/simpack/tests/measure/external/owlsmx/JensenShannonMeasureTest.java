/*
 * $Id: JensenShannonMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tests.measure.external.owlsmx;

import junit.framework.TestCase;
import simpack.measure.external.owlsmx.JensenShannonMeasure;
import simpack.util.corpus.StringUtils;

/**
 * @author Christoph Kiefer
 * @version $Id: JensenShannonMeasureTest.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class JensenShannonMeasureTest extends TestCase {

	private String profileDesc1 = new String(
			"It is the most used service for acknowledgement of the hotel in a city of a country.");

	private String profileDesc2 = new String(
			"This service returns accomodation information, hotel, restaurant etc in the city of the country.");

	/*
	 * Term frequencies str1: the 2 of 2 most 1 acknowledgement 1 service 1
	 * country 1 a 2 it 1 hotel 1 is 1 in 1 city 1 used 1 for 1
	 */

	/*
	 * Term frequencies str2: the 2 of 1 returns 1 service 1 restaurant 1
	 * country 1 information 1 etc 1 hotel 1 accomodation 1 this 1 in 1 city 1
	 */

	private String profileDesc3 = new String(
			"It is the most frequently used service to get information about hotels in the city of the country.");

	private String profileDesc4 = new String(
			"The service returns information about accommodation, about hotels, and about restaurants in the city of the country.");

	public void testCalculationSimilarity() {
		JensenShannonMeasure js = new JensenShannonMeasure(profileDesc1,
				profileDesc2);
		assertNotNull(js);
		assertTrue(js.calculate());
		assertTrue(js.isCalculated());
		assertEquals(js.getSimilarity(), new Double(computeResultByHand()));
	}

	public void testCalculationSimilarity2() {
		String clean3 = StringUtils.clean(profileDesc3);
		String clean4 = StringUtils.clean(profileDesc4);

		// most frequent us servic get inform about hotel citi countri

		// servic return inform about accommod about hotel about restaur citi
		// countri

		/*
		 * Term frequencies clean3: all 1s
		 */

		/*
		 * Term frequencies clean4: all 1s, about 3
		 */

		JensenShannonMeasure js = new JensenShannonMeasure(clean3, clean4);
		assertNotNull(js);
		assertTrue(js.calculate());
		assertTrue(js.isCalculated());
		assertEquals(js.getSimilarity(), new Double(computeResultByHand2()));
	}

	public void testCalculationSimilaritySameTerms() {
		JensenShannonMeasure js = new JensenShannonMeasure(profileDesc1,
				profileDesc1);
		assertNotNull(js);
		assertTrue(js.calculate());
		assertTrue(js.isCalculated());
		assertEquals(js.getSimilarity(), new Double(1d));
	}

	/**
	 * Cumbersomely reverse engineered from the OWLS-MX measures.
	 * 
	 * @return the similarity of the input strings
	 */
	private double computeResultByHand() {
		// relative term frequencies
		simpack.util.Vector<Double> v1 = new simpack.util.Vector<Double>();
		simpack.util.Vector<Double> v2 = new simpack.util.Vector<Double>();

		double sum1 = 17d;
		double sum2 = 14d;

		v1.add(1d / sum1); // it
		v2.add(0d / sum2); // it

		v1.add(1d / sum1); // is
		v2.add(0d / sum2); // is

		v1.add(2d / sum1); // the
		v2.add(2d / sum2); // the

		v1.add(1d / sum1); // most
		v2.add(0d / sum2); // most

		v1.add(1d / sum1); // used
		v2.add(0d / sum2); // used

		v1.add(1d / sum1); // for
		v2.add(0d / sum2); // for

		v1.add(2d / sum1); // of
		v2.add(1d / sum2); // of

		v1.add(1d / sum1); // in
		v2.add(1d / sum2); // in

		v1.add(2d / sum1); // a
		v2.add(0d / sum2); // a

		v1.add(0d / sum1); // this
		v2.add(1d / sum2); // this

		v1.add(0d / sum1); // etc
		v2.add(1d / sum2); // etc

		v1.add(1d / sum1); // acknowledgement
		v2.add(0d / sum2); // acknowledgement

		v1.add(1d / sum1); // service
		v2.add(1d / sum2); // service

		v1.add(1d / sum1); // country
		v2.add(1d / sum2); // country

		v1.add(1d / sum1); // hotel
		v2.add(1d / sum2); // hotel

		v1.add(1d / sum1); // city
		v2.add(1d / sum2); // city

		v1.add(0d / sum1); // returns
		v2.add(1d / sum2); // returns

		v1.add(0d / sum1); // restaurant
		v2.add(1d / sum2); // restaurant

		v1.add(0d / sum1); // information
		v2.add(1d / sum2); // information

		v1.add(0d / sum1); // accomodation
		v2.add(1d / sum2); // accomodation

		double sum = 0d;
		for (int i = 0; i < v1.size(); i++) {
			if (((v1.get(i) != 0) && (v2.get(i) != 0))) {
				double tmp = h(v1.get(i) + v2.get(i)) - h(v1.get(i))
						- h(v2.get(i));
				if (!new Double(tmp).isNaN())
					sum += tmp;
			}
		}

		double v = -0.5d * sum / Math.log(2d);

		System.out.println(v);
		return v;
	}

	private double computeResultByHand2() {
		// relative term frequencies
		simpack.util.Vector<Double> v1 = new simpack.util.Vector<Double>();
		simpack.util.Vector<Double> v2 = new simpack.util.Vector<Double>();

		// most frequent us servic get inform about hotel citi countri

		// servic return inform about accommod about hotel about restaur citi
		// countri

		double sum1 = 10d;
		double sum2 = 11d;

		v1.add(1d / sum1); // most
		v2.add(0d / sum2); // most

		v1.add(1d / sum1); // frequent
		v2.add(0d / sum2); // frequent

		v1.add(1d / sum1); // us
		v2.add(0d / sum2); // us

		v1.add(1d / sum1); // servic
		v2.add(1d / sum2); // servic

		v1.add(1d / sum1); // get
		v2.add(0d / sum2); // get

		v1.add(1d / sum1); // inform
		v2.add(1d / sum2); // inform

		v1.add(1d / sum1); // countri
		v2.add(1d / sum2); // countri

		v1.add(1d / sum1); // hotel
		v2.add(1d / sum2); // hotel

		v1.add(1d / sum1); // citi
		v2.add(1d / sum2); // citi

		v1.add(0d / sum1); // return
		v2.add(1d / sum2); // return

		v1.add(0d / sum1); // restaur
		v2.add(1d / sum2); // restaur

		v1.add(1d / sum1); // about
		v2.add(3d / sum2); // about

		v1.add(0d / sum1); // accommod
		v2.add(1d / sum2); // accommod

		double sum = 0d;
		for (int i = 0; i < v1.size(); i++) {
			if (((v1.get(i) != 0) && (v2.get(i) != 0))) {
				double tmp = h(v1.get(i) + v2.get(i)) - h(v1.get(i))
						- h(v2.get(i));
				if (!new Double(tmp).isNaN())
					sum += tmp;
			}
		}

		double v = -0.5d * sum / Math.log(2d);

		System.out.println(v);

		return v;
	}

	private double h(double x) {
		return -x * Math.log(x);
	}
}