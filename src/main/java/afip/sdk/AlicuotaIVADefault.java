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
 */package afip.sdk;

import static afip.sdk.Numeros.doubleConPrecision;
import afip.soap.wsfev1.*;
import java.math.*;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class AlicuotaIVADefault implements AlicuotaIVA {

    private final TipoIVA tipo;
    private final BigDecimal baseImponible;
    private final BigDecimal importe;

    public AlicuotaIVADefault(TipoIVA tipo, BigDecimal baseImponible, BigDecimal importe) {
        this.tipo = tipo;
        this.baseImponible = baseImponible;
        this.importe = importe;
    }

    @Override
    public AlicIva aAlicuota() {
        final AlicIva a = new AlicIva();
        a.setId(tipo.codigo());
        a.setBaseImp(doubleConPrecision(baseImponible));
        a.setImporte(doubleConPrecision(importe));
        return a;
    }

    @Override
    public BigDecimal importe() {
        return importe;
    }

    public AlicuotaIVADefault sumar(BigDecimal baseImponible, BigDecimal importe) {
        return new AlicuotaIVADefault(tipo, this.baseImponible.add(baseImponible), this.importe.add(importe));
    }

    public boolean tieneImporte() {
        return importe.compareTo(BigDecimal.ZERO) > 0;
    }

    boolean tieneValores(BigDecimal baseImponible, BigDecimal importe) {
        return this.baseImponible.compareTo(baseImponible) == 0 && this.importe.compareTo(importe) == 0;
    }

    @Override
    public String toString() {
        return "AlicuotaIVADefault{" + "tipo=" + tipo + ", baseImponible=" + baseImponible + ", importe=" + importe + '}';
    }

}
