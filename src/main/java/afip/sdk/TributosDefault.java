/*
 * The MIT License
 *
 * Copyright 2018 Martin Straus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package afip.sdk;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class TributosDefault implements Tributos {

    private final Set<Tributo> tributos;

    public TributosDefault() {
        this.tributos = Collections.EMPTY_SET;
    }

    @Override
    public BigDecimal total() {
        return tributos.stream().map(Tributo::importe).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Set<Tributo> todos() {
        return tributos;
    }

}
