/*
 * $Id: CharacterSequenceAccessor.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.accessor.sequence;

import java.util.ArrayList;
import java.util.List;

import simpack.api.ISequenceAccessor;

import com.wcohen.ss.api.Token;
import com.wcohen.ss.api.Tokenizer;
import com.wcohen.ss.tokens.NGramTokenizer;
import com.wcohen.ss.tokens.SimpleTokenizer;

/**
 * Takes a string as input and tokenizes the string by the use of an
 * 1-gram-tokenizer, i.e., it splits the input string into single characters.
 * 
 * @author Christoph Kiefer
 * @author Tobias Sager
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * @deprecated simpack.accessor.string.StringAccessor should be used instead.
 */
public class CharacterSequenceAccessor implements ISequenceAccessor {

	public static boolean IGNORE_CASE = true;

	public static boolean IGNORE_PUNCTUATION = true;

	protected boolean ignoreCase = IGNORE_CASE;

	protected boolean ignorePunctuation = IGNORE_PUNCTUATION;

	private String str;

	private Tokenizer tokenizer;

	/**
	 * Constructor.
	 * <p>
	 * Takes a string as input. Cases and punctuation are ignored by default.
	 * 
	 * @param str
	 *            the input string
	 */
	public CharacterSequenceAccessor(String str) {
		this(str, CharacterSequenceAccessor.IGNORE_CASE,
				CharacterSequenceAccessor.IGNORE_PUNCTUATION);
	}

	/**
	 * Constructor.
	 * <p>
	 * Takes a string as input and uses a 1-gram-tokenizer that tokenizes the
	 * string into single characters.
	 * 
	 * @param str
	 *            the input string
	 * @param ignoreCase
	 *            if true, the string is treated case in-sensitive, otherwise
	 *            case sensitive
	 * @param ignorePunctuation
	 *            if true punctuation of the string is ignored, i.e., characters
	 *            such as semi-colons are skipped
	 */
	public CharacterSequenceAccessor(String str, boolean ignoreCase,
			boolean ignorePunctuation) {
		this.str = str;
		this.ignoreCase = ignoreCase;
		this.ignorePunctuation = ignorePunctuation;
		this.tokenizer = new NGramTokenizer(1, 1, false, new SimpleTokenizer(
				ignorePunctuation, ignoreCase));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simpack.api.ISequenceAccessor#getSequence()
	 */
	public List<Object> getSequence() {
		ArrayList<Object> ret = new ArrayList<Object>();
		Token[] tokens = tokenizer.tokenize(str);
		for (int i = 0; i < tokens.length; i++) {
			Token tok = tokens[i];
			ret.add(tok.getValue());
		}
		return ret;
	}
}