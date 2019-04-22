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

import afip.soap.wsfev1.FEAuthRequest;
import java.time.LocalDateTime;

/**
 *
 * @author Martín Straus <martin.straus@ventapp.com.ar>
 */
public class CredencialesDefault implements Credenciales {

    private final String token;
    private final String sign;
    private final LocalDateTime expiration;

    public CredencialesDefault(String token, String sign, LocalDateTime expiration) {
        this.token = token;
        this.sign = sign;
        this.expiration = expiration;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public String sign() {
        return sign;
    }

    @Override
    public LocalDateTime expiracion() {
        return expiration;
    }

    @Override
    public FEAuthRequest autorizacionFacturaElectronia(long cuit) {
        FEAuthRequest req = new FEAuthRequest();
        req.setCuit(cuit);
        req.setSign(sign());
        req.setToken(token());
        return req;
    }

}
