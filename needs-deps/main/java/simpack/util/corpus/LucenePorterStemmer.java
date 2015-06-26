/*
 * $Id: LucenePorterStemmer.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.util.corpus;

import java.lang.reflect.Method;

import net.sf.snowball.SnowballProgram;

/**
 * @author Christoph Kiefer
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
public class LucenePorterStemmer {

	private SnowballProgram stemmer;

	private Method stemMethod;

	public LucenePorterStemmer() {
		try {
			Class stemClass = Class
					.forName("net.sf.snowball.ext.PorterStemmer");
			stemmer = (SnowballProgram) stemClass.newInstance();
			stemMethod = stemClass.getMethod("stem", new Class[0]);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	public String stem(String in) {
		stemmer.setCurrent(in);
		try {
			stemMethod.invoke(stemmer, new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		return stemmer.getCurrent();
	}
}
