/*
 * $Id: TokeniserQGramN.java 757 2008-04-17 17:42:53Z kiefer $
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import simmetrics.api.InterfaceTermHandler;
import simmetrics.api.InterfaceTokeniser;
import simmetrics.wordhandlers.DummyStopTermHandler;

/**
 * @author Christoph Kiefer
 * @version $Id: TokeniserQGramN.java 757 2008-04-17 17:42:53Z kiefer $
 */
public class TokeniserQGramN implements InterfaceTokeniser, Serializable {

	/**
	 * stopWordHandler used by the tokenisation.
	 */
	private InterfaceTermHandler stopWordHandler = new DummyStopTermHandler();

	private int n;

	public TokeniserQGramN(int n) {
		this.n = n;
	}

	public String getDelimiters() {
		return "";
	}

	public String getShortDescriptionString() {
		return "TokeniserQGramN";
	}

	public InterfaceTermHandler getStopWordHandler() {
		return stopWordHandler;
	}

	public void setStopWordHandler(InterfaceTermHandler stopWordHandler) {
		this.stopWordHandler = stopWordHandler;
	}

	public Vector tokenize(String input) {
		final Vector returnVect = new Vector();
		int curPos = 0;
		final int length = input.length() - n + 1;
		while (curPos < length) {
			final String term = input.substring(curPos, curPos + n);
			if (!stopWordHandler.isWord(term)) {
				returnVect.add(term);
			}
			curPos++;
		}

		return returnVect;
	}

	public Set tokenizeToSet(String input) {
		final Set returnSet = new HashSet();
		returnSet.addAll(tokenize(input));
		return returnSet;
	}

}
