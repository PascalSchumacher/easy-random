/*
 * The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */

package io.github.benas.jpopulator;

import io.github.benas.jpopulator.api.Random;
import io.github.benas.jpopulator.api.RandomObjectGenerationException;

/**
 * The core implementation of the {@link Random} interface.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RandomObjectPopulator implements Random {

    private java.util.Random random;

    RandomObjectPopulator() {
        this.random = new java.util.Random();
    }

    RandomObjectPopulator(final long seed) {
        this.random = new java.util.Random(seed);
    }

    /**
     * Get the next random object of type <T>.
     *
     * @param type the type of the random object to generate
     * @return a random instance of the type <T>
     */
    @Override
    public <T> T nextObject(Class<? extends T> type) throws RandomObjectGenerationException{
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RandomObjectGenerationException("Unable to generate a random instance of type " + type, e);
        }
    }
}
