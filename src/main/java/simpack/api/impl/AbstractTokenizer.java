/*
 * $Id: AbstractTokenizer.java 757 2008-04-17 17:42:53Z kiefer $
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
package simpack.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import simpack.api.IToken;
import simpack.api.ITokenizer;
import simpack.tokenizer.tokens.BasicToken;

/**
 * This class implements a simple interning mechanism using the template method
 * pattern.
 * 
 * Implementor only have to override the template method
 * {@link #buildStringTokens(String)} to provide an algorithm for tokenizing and
 * this class will take care of the rest.
 * 
 * @author Michael Wuersch
 * @version $Revision: 756 $ $Date: 2008-04-17 19:52:02 +0200 (Thu, 17 Apr 2008) $
 * 
 */
// TODO Reusing a particular tokenizer instance over and over again will prevent
// the strings from being deallocated by gc. Solutions? --> added clear method
// as quick'n'dirty fix.
public abstract class AbstractTokenizer implements ITokenizer {
	private Map<String, IToken> fTokenMap = new TreeMap<String, IToken>();

	private int fNextIndex = 0;

	/**
	 * Tokenizes the given String s into tokens. Uses a template-Pattern to
	 * ensure, that the tokens are interned.
	 * 
	 * Note: The pool of interned Strings might grow overtime, if the same
	 * instance of an implementor is used over and over again for tokenizing.
	 * Invoking the {@link #clear()}-method will clear the pool.
	 * 
	 * @param s
	 *            the string that shall be tokenized
	 * @return a list of ITokens
	 */
	public List<IToken> tokenize(String s) {
		List<IToken> resultTokens = new ArrayList<IToken>();
		String[] stringTokens = buildStringTokens(s);

		for (String stringToken : stringTokens) {
			IToken token = intern(stringToken);
			resultTokens.add(token);
		}

		return resultTokens;
	}

	/**
	 * Template-Method invoked by {@link #tokenize(String)}. Implementors have
	 * to provide an algorithm for splitting the given String s into single
	 * tokens (represented by Strings).
	 * 
	 * Example implementation:
	 * 
	 * <code>
	 * protected abstract String[] buildStringTokens(String s){
	 *      return s.split("\s");
	 * }
	 * <code>
	 * 
	 * This implementation would split the given String s by whitespaces. 
	 * E.g. the String "Hello My World" would be converted into
	 * the tokens "Hello", "My" and "World". 
	 * 
	 * @param s The String that shall be tokenized.
	 * @return The tokens represented by Strings.
	 * 
	 * @see simpack.tokenizer.DummyTokenizer for a dummy implementation.
	 */
	protected abstract String[] buildStringTokens(String s);

	/**
	 * Converts a given String s into a IToken (which is then stored in a Map
	 * for interning reasons) and returns it.
	 * 
	 * @param s
	 *            The String that shall be converted into an IToken.
	 * @return The interned IToken.
	 */
	protected IToken intern(String s) {
		IToken token = fTokenMap.get(s);

		if (token == null) {
			token = new BasicToken(++fNextIndex, s);
			fTokenMap.put(s, token);
		}

		return token;
	}

	/**
	 * Clears the pool of interned strings.
	 */
	public void clear() {
		fTokenMap.clear();
	}
}