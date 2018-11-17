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

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 *
 * @author Martín Straus <martin.straus@fit.com.ar>
 */
public class LoginTicketRequest {

    private static final String PLANTILLA = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<loginTicketRequest version=\"1.0\">"
        + "    <header>"
        //+ "        <source>%1$s</source>"
        //+ "        <destination>%2$s</destination>"
        + "        <uniqueId>%3$s</uniqueId>"
        + "        <generationTime>%4$s</generationTime>"
        + "        <expirationTime>%5$s</expirationTime>"
        + "    </header>"
        + "    <service>%6$s</service>"
        + "</loginTicketRequest>";

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private final String destinationDN;
    private final String service;
    private LocalDateTime generacion;
    private LocalDateTime expiracion;

    public LoginTicketRequest(String destinationDN, String service) {
        this.destinationDN = destinationDN;
        this.service = service;
    }

    public String crearNuevoTicket(String signerDN) {
        final String UniqueId = new Long(new Date().getTime() / 1000).toString();
        generacion = LocalDateTime.now().minusHours(1);
        expiracion = generacion.plusDays(1).minusMinutes(1);

        return String.format(
            PLANTILLA,
            signerDN,
            destinationDN,
            UniqueId,
            formatter.format(generacion),
            formatter.format(expiracion),
            service
        );
    }

    public LocalDateTime generacion() {
        return generacion;
    }

    public LocalDateTime expiracion() {
        return expiracion;
    }
    
    
}
