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

import afip.soap.wsfev1.ArrayOfErr;
import afip.soap.wsfev1.Err;
import afip.soap.wsfev1.FEAuthRequest;
import afip.soap.wsfev1.FEPtoVentaResponse;
import afip.soap.wsfev1.FERecuperaLastCbteResponse;
import afip.soap.wsfev1.ServiceSoap;
import static java.util.Collections.EMPTY_SET;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class PuntosVentaDefault implements PuntosVenta {

    private final int ERROR_SIN_RESULTADOS = 602;

    private final ServiceSoap servicio;
    private final FEAuthRequest auth;

    public PuntosVentaDefault(ServiceSoap servicio, Credenciales credenciales, long cuit) {
        this.servicio = servicio;
        auth = credenciales.autorizacionFacturaElectronia(cuit);
    }

    @Override
    public Set<PuntoVenta> todos() {
        FEPtoVentaResponse respuesta = servicio.feParamGetPtosVenta(auth);
        Optional<Err> error = respuesta.getErrors().getErr().stream().findFirst();
        if (error.isPresent()) {
            if (error.get().getCode() == ERROR_SIN_RESULTADOS) {
                return EMPTY_SET;
            }
            throw new RuntimeException(formatearErrores(respuesta.getErrors()));
        }
        return respuesta
            .getResultGet()
            .getPtoVenta()
            .stream()
            .map((valor) -> new PuntoVentaInmutable(this, valor))
            .collect(toSet());
    }

    @Override
    public int ultimoNumeroAutorizado(PuntoVenta punto, TiposComprobante tipoComprobante) {
        FERecuperaLastCbteResponse respuesta = servicio.feCompUltimoAutorizado(
            auth,
            punto.numero(),
            tipoComprobante.codigo()
        );
        if (respuesta.getErrors() != null && !respuesta.getErrors().getErr().isEmpty()) {
            throw new RuntimeException(formatearErrores(respuesta.getErrors()));
        }
        return respuesta.getCbteNro();
    }

    private String formatearErrores(ArrayOfErr errores) {
        return errores
            .getErr()
            .stream()
            .map((e) -> String.format("[AFIP-%d] %s", e.getCode(), e.getMsg()))
            .collect(joining("; ", "Errores:", ""));
    }

}
