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

import static afip.sdk.Numeros.doubleConPrecision;
import java.math.*;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class TributoDefault implements Tributo {

    private final short codigo;
    private final BigDecimal alicuota;
    private final BigDecimal baseImponible;
    private final String descripcion;

    public TributoDefault(short codigo, BigDecimal alicuota, BigDecimal baseImponible, String descripcion) {
        this.codigo = codigo;
        this.alicuota = alicuota;
        this.baseImponible = baseImponible;
        this.descripcion = descripcion;
    }

    @Override
    public afip.soap.wsfev1.Tributo aTributo() {
        afip.soap.wsfev1.Tributo tributo = new afip.soap.wsfev1.Tributo();
        tributo.setAlic(alicuota.doubleValue());
        tributo.setBaseImp(baseImponible.doubleValue());
        tributo.setDesc(descripcion);
        tributo.setId(codigo);
        tributo.setImporte(doubleConPrecision(importe()));
        return tributo;
    }

    @Override
    public BigDecimal importe() {
        return baseImponible.multiply(alicuota);
    }

}
