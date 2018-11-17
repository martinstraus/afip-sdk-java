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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class AlicuotaIVADefaultTest {

    @Test
    public void sumarIncrementaImportes() {
        assertThat(
            "alicuotaIVA",
            new AlicuotaIVADefault(TipoIVA._21, BigDecimal.ZERO, BigDecimal.ZERO).sumar(new BigDecimal(100), new BigDecimal(21)),
            tieneValores(new BigDecimal("100"), new BigDecimal("21"))
        );

    }

    private Matcher<AlicuotaIVADefault> tieneValores(BigDecimal baseImponible, BigDecimal importe) {
        return new TypeSafeMatcher<AlicuotaIVADefault>() {
            @Override
            public boolean matchesSafely(AlicuotaIVADefault item) {
                return item.tieneValores(baseImponible, importe);
            }

            @Override
            public void describeTo(Description description) {
                description
                    .appendText("baseImponible=")
                    .appendValue(baseImponible)
                    .appendText(", importe=")
                    .appendValue(importe);
            }
        };
    }
}
