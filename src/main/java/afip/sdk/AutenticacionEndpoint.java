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

import afip.soap.wsaa.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class AutenticacionEndpoint implements Autenticacion {

    private final String endpoint;
    private final String dn;
    private final EncriptadorCMS encriptador;

    public AutenticacionEndpoint(String endpoint, String dn, EncriptadorCMS encriptador) {
        this.endpoint = endpoint;
        this.dn = dn;
        this.encriptador = encriptador;
    }

    @Override
    public Credenciales autenticar(String servicio) {
        LoginTicketRequest ticket = new LoginTicketRequest(dn, servicio);
        return new CredencialesDOM(parsear(login(encriptador.crearCMS(ticket))), ticket.expiracion());
    }

    private String login(byte[] cms) {
        try {
            return new LoginCMSService().getLoginCms().loginCms(new String(Base64.getEncoder().encode(cms)));
        } catch (LoginFault_Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Document parsear(String respuesta) {
        try {
            return DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(respuesta.getBytes(Charset.defaultCharset())));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
