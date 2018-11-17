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

import afip.soap.wsfev1.IvaTipo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public interface FacturacionElectronica {

    int ultimoComprobanteAutorizado(int puntoVenta, TiposComprobante tipo);

    /**
     * Usar para facturas A o B
     *
     * @param tipo
     * @param concepto
     * @param comprobante
     * @param persona
     * @param fecha
     * @param netoGravado
     * @param operacionesExentas
     * @param iva
     * @param otrosTributos
     * @return
     */
    CAE autorizarFacturaConIVA(TiposComprobante tipo, Conceptos concepto, Comprobante comprobante, Persona persona,
        LocalDate fecha, BigDecimal netoGravado, BigDecimal operacionesExentas, IVA iva, Tributos otrosTributos)
        throws ExcepcionAFIP;

    /**
     * Usar para facturas C.
     *
     * @param concepto
     * @param comprobante
     * @param persona
     * @param fecha
     * @param importeNeto
     * @param otrosTributos
     * @return
     */
    CAE autorizarFacturaSinIVA(Conceptos concepto, Comprobante comprobante, Persona persona, LocalDate fecha,
        BigDecimal importeNeto, Tributos otrosTributos) throws ExcepcionAFIP;

    List<IvaTipo> tiposIVA() throws ExcepcionAFIP;

}
