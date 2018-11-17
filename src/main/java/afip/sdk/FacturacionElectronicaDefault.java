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
import afip.soap.wsfev1.*;
import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Martín Straus <a href="mailto:martinstraus@gmail.com">martinstraus@gmail.com</a>
 */
public class FacturacionElectronicaDefault implements FacturacionElectronica {

    private final ServiceSoap servicio;
    private final FEAuthRequest auth;

    public FacturacionElectronicaDefault(ServiceSoap servicio, Credenciales credenciales, long cuit) {
        this.servicio = servicio;
        auth = credenciales.autorizacionFacturaElectronia(cuit);
    }

    @Override
    public int ultimoComprobanteAutorizado(int puntoVenta, TiposComprobante tipo) {
        return servicio.feCompUltimoAutorizado(auth, puntoVenta, tipo.codigo()).getCbteNro();
    }

    @Override
    public CAE autorizarFacturaConIVA(TiposComprobante tipo, Conceptos concepto, Comprobante comprobante,
        Persona persona, LocalDate fecha, BigDecimal netoGravado, BigDecimal operacionesExentas, IVA iva,
        Tributos otrosTributos) throws ExcepcionAFIP {
        return solicitarCAE(requestConIVA(tipo, concepto, comprobante, persona, fecha, netoGravado, operacionesExentas,
            iva, otrosTributos));
    }

    private FECAERequest requestConIVA(TiposComprobante tipo, Conceptos concepto, Comprobante comprobante, Persona persona,
        LocalDate fecha, BigDecimal netoGravado, BigDecimal operacionesExentas, IVA iva, Tributos otrosTributos) {
        final FECAEDetRequest detalle = new FECAEDetRequest();
        BigDecimal netoNoGravado = BigDecimal.ZERO;
        BigDecimal total = netoNoGravado.add(operacionesExentas).add(netoGravado).add(iva.total()).add(otrosTributos.total());
        detalle.setConcepto(concepto.codigo());
        detalle.setCbteDesde(comprobante.numero());
        detalle.setCbteHasta(comprobante.numero());
        detalle.setDocTipo(persona.tipo().codigo());
        detalle.setDocNro(persona.numero());
        detalle.setCbteFch(DateTimeFormatter.ofPattern("yyyyMMdd").format(fecha));
        detalle.setImpTotal(doubleConPrecision(total));
        detalle.setImpTotConc(doubleConPrecision(netoNoGravado));
        detalle.setImpNeto(doubleConPrecision(netoGravado));
        detalle.setImpOpEx(doubleConPrecision(operacionesExentas));
        detalle.setImpTrib(doubleConPrecision(otrosTributos.total()));
        detalle.setImpIVA(doubleConPrecision(iva.total()));
        detalle.setMonId(Monedas.PESOS_ARGENTINOS.codigo());
        detalle.setMonCotiz(1d);
        detalle.setIva(iva(iva));
        ponerTributosSiTiene(otrosTributos, detalle);

        final ArrayOfFECAEDetRequest detalles = new ArrayOfFECAEDetRequest();
        detalles.getFECAEDetRequest().add(detalle);

        final FECAERequest request = new FECAERequest();
        request.setFeCabReq(cabecera(comprobante.puntoVenta(), tipo));
        request.setFeDetReq(detalles);
        return request;
    }

    @Override
    public CAE autorizarFacturaSinIVA(Conceptos concepto, Comprobante comprobante, Persona persona, LocalDate fecha,
        BigDecimal importeNeto, Tributos otrosTributos) throws ExcepcionAFIP {
        return solicitarCAE(requestSinIVA(concepto, comprobante, persona, fecha, importeNeto, otrosTributos));
    }

    private FECAERequest requestSinIVA(Conceptos concepto, Comprobante comprobante, Persona persona,
        LocalDate fecha, BigDecimal importeNeto, Tributos otrosTributos) {
        final FECAEDetRequest detalle = new FECAEDetRequest();
        detalle.setConcepto(concepto.codigo());
        detalle.setCbteDesde(comprobante.numero());
        detalle.setCbteHasta(comprobante.numero());
        detalle.setDocTipo(persona.tipo().codigo());
        detalle.setDocNro(persona.numero());
        detalle.setCbteFch(DateTimeFormatter.ofPattern("yyyyMMdd").format(fecha));
        BigDecimal total = importeNeto.add(otrosTributos.total());
        detalle.setImpTotal(doubleConPrecision(total));
        detalle.setImpTotConc(0d);
        detalle.setImpNeto(doubleConPrecision(importeNeto));
        detalle.setImpOpEx(0d);
        detalle.setImpTrib(doubleConPrecision(otrosTributos.total()));
        detalle.setImpIVA(0);
        detalle.setMonId(Monedas.PESOS_ARGENTINOS.codigo());
        detalle.setMonCotiz(1d);
        ponerTributosSiTiene(otrosTributos, detalle);

        final ArrayOfFECAEDetRequest detalles = new ArrayOfFECAEDetRequest();
        detalles.getFECAEDetRequest().add(detalle);

        final FECAERequest request = new FECAERequest();
        request.setFeCabReq(cabecera(comprobante.puntoVenta(), TiposComprobante.FACTURA_C));
        request.setFeDetReq(detalles);
        return request;
    }

    private ArrayOfAlicIva iva(IVA iva) {
        final ArrayOfAlicIva arrya = new ArrayOfAlicIva();
        iva.alicuotas().stream().map(AlicuotaIVA::aAlicuota).forEach((alicuota) -> arrya.getAlicIva().add(alicuota));
        return arrya;
    }

    private void ponerTributosSiTiene(Tributos tributos, FECAEDetRequest detalle) {
        ArrayOfTributo array = tributos(tributos);
        if (!array.getTributo().isEmpty()) {
            detalle.setTributos(array);
        }
    }

    private ArrayOfTributo tributos(Tributos tributos) {
        final ArrayOfTributo array = new ArrayOfTributo();
        tributos.todos().stream().map(Tributo::aTributo).forEach((tributo) -> array.getTributo().add(tributo));
        return array;
    }

    private FECAECabRequest cabecera(int puntoVenta, TiposComprobante tipo) {
        final FECAECabRequest cabecera = new FECAECabRequest();
        cabecera.setCantReg(1);
        cabecera.setPtoVta(puntoVenta);
        cabecera.setCbteTipo(tipo.codigo());
        return cabecera;
    }

    public List<IvaTipo> tiposIVA() throws ExcepcionAFIP {
        IvaTipoResponse respuesta = servicio.feParamGetTiposIva(auth);
        errorTecnico(respuesta.getErrors());
        return respuesta.getResultGet().getIvaTipo();
    }

    private CAE solicitarCAE(FECAERequest request) throws ExcepcionAFIP {
        final FECAEResponse respuesta = servicio.fecaeSolicitar(auth, request);
        Optional<ExcepcionAFIP> errorTecnico = errorTecnico(respuesta);
        if (errorTecnico.isPresent()) {
            throw errorTecnico.get();
        }
        Optional<ExcepcionAFIP> errorFuncional = errorFuncional(respuesta);
        if (errorFuncional.isPresent()) {
            throw errorFuncional.get();
        }
        return new CAEResponse(respuesta.getFeDetResp().getFECAEDetResponse().get(0));
    }

    /**
     * Asume que se solicitó un único CAE.
     *
     * @param respuesta La respuesta generada por la AFIP.
     * @return Si hay errores, la excepción a lanzar.
     */
    private Optional<ExcepcionAFIP> errorFuncional(FECAEResponse respuesta) {
        List<FECAEDetResponse> respuestas = respuesta.getFeDetResp().getFECAEDetResponse();
        if (respuestas == null || respuestas.isEmpty()) {
            return Optional.empty();
        }
        Optional<FECAEDetResponse> detalle = respuestas.stream().findFirst();
        return detalle.flatMap(this::errorFuncional);
    }

    private Optional<ExcepcionAFIP> errorFuncional(FECAEDetResponse respuesta) {
        return "R".equals(respuesta.getResultado())
            ? Optional.of(new ExcepcionAFIP(errores(respuesta.getObservaciones())))
            : Optional.empty();
    }

    private Optional<ExcepcionAFIP> errorTecnico(FECAEResponse respuesta) {
        return errorTecnico(respuesta.getErrors());
    }

    private Optional<ExcepcionAFIP> errorTecnico(ArrayOfErr errores) {
        return errores != null && errores.getErr() != null && !errores.getErr().isEmpty()
            ? Optional.of(new ExcepcionAFIP(errores(errores)))
            : Optional.empty();
    }

    private List<Error> errores(ArrayOfObs observaciones) {
        return observaciones.getObs().stream().map((obs) -> new Error(obs.getCode(), obs.getMsg())).collect(toList());
    }

    private List<Error> errores(ArrayOfErr errores) {
        return errores.getErr().stream().map((err) -> new Error(err.getCode(), err.getMsg())).collect(toList());
    }

    
}
