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

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import static java.util.Arrays.asList;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Basado en este ejemplo: http://www.afip.gob.ar/ws/WSAA/ejemplos/wsaa_client_java.tgz
 * @author Martín Straus <martin.straus@fit.com.ar>
 */
public class EncriptadorCMS {

    private static KeyStore keyStore(InputStream inputStream, char[] password) throws KeyStoreException, IOException,
        NoSuchAlgorithmException, CertificateException {
        final KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(inputStream, password);
        return keyStore;
    }

    private final KeyStore keyStore;
    private final char[] password;
    private final String signer;

    public EncriptadorCMS(InputStream inputStreamKeyStore, char[] password, String signer) throws KeyStoreException,
        IOException, NoSuchAlgorithmException, CertificateException {
        this(keyStore(inputStreamKeyStore, password), password, signer);
    }

    public EncriptadorCMS(KeyStore keyStore, char[] password, String signer) throws KeyStoreException,
        IOException, NoSuchAlgorithmException, CertificateException {
        this.password = password;
        this.signer = signer;
        this.keyStore = keyStore;
    }

    public byte[] crearCMS(LoginTicketRequest loginTicketRequest) {
        try {
            final X509Certificate certificate = (X509Certificate) keyStore.getCertificate(signer);
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            final CertStore cstore = certificateStore(certificate);
            final CMSSignedDataGenerator gen = dataGenerator(keyStore, certificate, cstore);
            final String ticket = loginTicketRequest.crearNuevoTicket(certificate.getSubjectDN().toString());
            final CMSSignedData signedCMS = gen.generate(new CMSProcessableByteArray(ticket.getBytes()), true, "BC");
            return signedCMS.getEncoded();
        } catch (IOException | IllegalArgumentException | InvalidAlgorithmParameterException | KeyStoreException
            | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableKeyException | CertStoreException
            | CMSException e) {
            throw new RuntimeException(e);
        }
    }

    private CMSSignedDataGenerator dataGenerator(KeyStore ks, X509Certificate certificate, CertStore cstore)
        throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertStoreException,
        CMSException {
        final CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSigner((PrivateKey) ks.getKey(signer, password), certificate, CMSSignedDataGenerator.DIGEST_SHA1);
        generator.addCertificatesAndCRLs(cstore);
        return generator;
    }

    private CertStore certificateStore(X509Certificate certificate) throws InvalidAlgorithmParameterException,
        NoSuchAlgorithmException, NoSuchProviderException {
        return CertStore.getInstance(
            "Collection",
            new CollectionCertStoreParameters(asList(certificate)),
            "BC"
        );
    }
}
