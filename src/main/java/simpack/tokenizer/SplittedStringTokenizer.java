/*
 * $Id: SplittedStringTokenizer.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.tokenizer;

import simpack.api.impl.AbstractTokenizer;

/**
 * Example implementation. This tokenizer splits a given String s using a
 * regular express
 * 
 * @author Michael Wuersch
 * @version $Revision: 752 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 */
// TODO Improve this tokenizer so that multiple (different) tokenizers might
// wrapp each other.
public class SplittedStringTokenizer extends AbstractTokenizer {
	private String pattern;

	/**
	 * Constructor.
	 * <p>
	 * 
	 * @param pattern
	 *            Regular expressios for splitting.
	 */
	public SplittedStringTokenizer(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Splits the String s by the pattern specified by invoking the constructor.
	 */
	protected String[] buildStringTokens(String s) {
		return s.split(pattern);
	}
}
