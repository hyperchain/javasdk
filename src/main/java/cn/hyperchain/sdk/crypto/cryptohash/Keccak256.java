// $Id: Keccak256.java 189 2010-05-14 21:21:46Z tp $

package cn.hyperchain.sdk.crypto.cryptohash;

/**
 * <p>This class implements the Keccak-256 digest algorithm under the
 * {@link cn.hyperchain.sdk.crypto.cryptohash.Digest} API.</p>
 * <p>
 * <pre>
 * ==========================(LICENSE BEGIN)============================
 *
 * Copyright (c) 2007-2010  Projet RNRT SAPHIR
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * ===========================(LICENSE END)=============================
 * </pre>
 *
 * @author Thomas Pornin &lt;thomas.pornin@cryptolog.com&gt;
 * @version $Revision: 189 $
 */

public class Keccak256 extends KeccakCore {

    /**
     * Create the engine.
     */
    public Keccak256() {
    }

    /**
     * @see cn.hyperchain.sdk.crypto.cryptohash.Digest
     */
    public Digest copy() {
        return copyState(new Keccak256());
    }

    /**
     * @see cn.hyperchain.sdk.crypto.cryptohash.Digest
     */
    public int getDigestLength() {
        return 32;
    }
}
