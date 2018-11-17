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

import javax.xml.parsers.*;
import org.hamcrest.*;
import static org.junit.Assert.assertThat;
import org.junit.*;
import org.junit.internal.matchers.*;
import org.w3c.dom.*;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class CredencialesDOMTest {

    @Test
    public void devuelveTokenYSignCorrectos() throws ParserConfigurationException {
        assertThat("credenciales", new CredencialesDOM(documento(), null), credencialesEsperadas());
    }

    private Document documento() throws ParserConfigurationException {
        final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document doc = db.newDocument();
        final Element loginTicketResponse = doc.createElement("loginTicketResponse");
        doc.appendChild(loginTicketResponse);
        final Element credenciales = doc.createElement("credentials");
        loginTicketResponse.appendChild(credenciales);
        credenciales.appendChild(elemento(doc, "token", "1234"));
        credenciales.appendChild(elemento(doc, "sign", "abcd"));
        return doc;
    }

    private Matcher<Credenciales> credencialesEsperadas() {
        return new TypeSafeMatcher<Credenciales>() {
            @Override
            public boolean matchesSafely(Credenciales item) {
                return "1234".equals(item.token()) && "abcd".equals(item.sign());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("token=").appendValue("1234").appendText(" sign=").appendValue("abcd");
            }

        };
    }

    private Element elemento(Document document, String nombre, String contenido) {
        Element elemento = document.createElement(nombre);
        elemento.setTextContent(contenido);
        return elemento;
    }

}
