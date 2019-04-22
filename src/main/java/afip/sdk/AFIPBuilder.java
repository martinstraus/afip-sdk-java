/*
 * The MIT License
 *
 * Copyright 2019 Martín Straus.
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

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Martín Straus <martin.straus@ventapp.com.ar>
 */
public class AFIPBuilder {

    private final Autenticacion autenticacion;
    private final long cuit;

    public AFIPBuilder(Autenticacion autenticacion, long cuit) {
        this.autenticacion = autenticacion;
        this.cuit = cuit;
    }

    public AFIP homologacion() {
        try {
            return new AFIPDefault(
                autenticacion,
                new URL("https://wswhomo.afip.gov.ar/wsfev1/service.asmx?WSDL"),
                cuit
            );
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AFIP produccion() {
        try {
            return new AFIPDefault(
                autenticacion,
                new URL("https://servicios1.afip.gov.ar/wsfev1/service.asmx?WSDL"),
                cuit
            );
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
