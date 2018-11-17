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

import afip.soap.wsfev1.PtoVenta;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
class PuntoVentaInmutable implements PuntoVenta {

    private final PuntosVenta repositorio;
    private final int numero;
    private final PuntoVenta.TipoEmision tipoEmision;
    private final boolean bloqueado;
    private final LocalDate fechaBaja;

    public PuntoVentaInmutable(PuntosVenta repositorio, PtoVenta ptoVentaWS) {
        this(
            repositorio,
            ptoVentaWS.getNro(),
            TipoEmision.valueOf(ptoVentaWS.getEmisionTipo()),
            ptoVentaWS.getBloqueado().equals("S"),
            LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(ptoVentaWS.getFchBaja()))
        );
    }

    public PuntoVentaInmutable(PuntosVenta repositorio, int numero, TipoEmision tipoEmision, boolean bloqueado,
        LocalDate fechaBaja) {
        this.repositorio = repositorio;
        this.numero = numero;
        this.tipoEmision = tipoEmision;
        this.bloqueado = bloqueado;
        this.fechaBaja = fechaBaja;
    }

    @Override
    public int numero() {
        return numero;
    }

    @Override
    public PuntoVenta.TipoEmision tipoEmision() {
        return tipoEmision;
    }

    @Override
    public boolean bloqueado() {
        return bloqueado;
    }

    @Override
    public LocalDate fechaBaja() {
        return fechaBaja;
    }

    @Override
    public int ultimoNumeroAutorizado(TiposComprobante tipo) {
        return repositorio.ultimoNumeroAutorizado(this, tipo);
    }

}
