/*
 * $Id: AbstractSecondStringSimilarityMeasure.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl.external;

import java.lang.reflect.Constructor;

import simpack.api.impl.AbstractSequenceSimilarityMeasure;

import com.wcohen.ss.AbstractStringDistance;

/**
 * SecondStringSimilarityMeasure is the abstract base class for all external
 * SecondString (http://secondstring.sourceforge.net/) string similarity
 * measures to computed the distance between two strings. Returns the string
 * distance of the two passed strings as computed by the given measure. Uses the
 * Java Reflection API to find and instantiate the appropriate class.
 * 
 * @author Christoph Kiefer
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public abstract class AbstractSecondStringSimilarityMeasure extends
		AbstractSequenceSimilarityMeasure {

	private AbstractStringDistance measure;

	private String str1, str2;

	/**
	 * Constructor.
	 * <p>
	 * Default empty constructor.
	 */
	public AbstractSecondStringSimilarityMeasure() {
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes the class name of a string distance measure and two strings to be
	 * compared as input. Returns the string distance of the two passed strings
	 * as computed by the given measure. This is achieved by using the Java
	 * Reflection API to find and instanciate the class of the specified
	 * measure.
	 * 
	 * @param measureName
	 *            name of the measure class in the form
	 *            'com.wcohen.ss.XYZ.class.getName()'
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 */
	public AbstractSecondStringSimilarityMeasure(String measureName, String str1,
			String str2) {

		Class clazz = getClass(measureName);
		if (clazz == null) {
			return;
		}

		Object instance;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (instance == null || !(instance instanceof AbstractStringDistance)) {
			return;
		}

		this.measure = (AbstractStringDistance) instance;
		this.str1 = str1;
		this.str2 = str2;
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes the class name of a string distance measure and two strings to be
	 * compared as input. Additionally takes two arrays specifying the types of
	 * the arguments of a specific constructor. Returns the string distance of
	 * the two passed strings as computed by the given measure. This is achieved
	 * by using the Java Reflection API to find and instanciate the class of the
	 * specified measure.
	 * 
	 * @param measureName
	 *            name of the measure class in the form
	 *            'com.wcohen.ss.XYZ.class.getName()'
	 * @param constructorParameterTypes
	 *            the types of the arguements of the specific construtor that
	 *            should be used to instantiate the passed string distance
	 *            measure
	 * @param constructorParameters
	 *            the parameters for the specific constructor that should be
	 *            used to instantiate the given string distance measure
	 * @param str1
	 *            the first string
	 * @param str2
	 *            the second string
	 */
	public AbstractSecondStringSimilarityMeasure(String measureName, String str1,
			String str2, Class[] constructorParameterTypes,
			Object[] constructorParameters) {

		Class clazz = getClass(measureName);
		if (clazz == null) {
			return;
		}

		Constructor constructor;
		try {
			constructor = clazz.getConstructor(constructorParameterTypes);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (constructor == null) {
			return;
		}

		Object instance;
		try {
			instance = constructor.newInstance(constructorParameters);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (instance == null || !(instance instanceof AbstractStringDistance)) {
			return;
		}

		this.measure = (AbstractStringDistance) instance;
		this.str1 = str1;
		this.str2 = str2;
	}

	/**
	 * Returns a class object.
	 * 
	 * @param className
	 *            the name of the class to be returned, name of the class should
	 *            be in the form 'com.wcohen.ss.XYZ.class.getName()'
	 * @return the class with the given class name, null if the class cannot be
	 *         found on the classpath
	 */
	private Class getClass(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.impl.SimilarityMeasure#calculate()
	 */
	public boolean calculate() {
		setCalculated(false);
		if (measure != null) {
			similarity = measure.score(str1, str2);
			setCalculated(true);
			return true;
		}
		return false;
	}
}
