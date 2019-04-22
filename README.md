# afip-sdk

SDK para web services de AFIP.

## Autenticación y autorización

### Configuración en AFIP

1. Generar una clave privada (por ejemplo con OpenSSL).
2. Generar una solicitud de certificado (CSR) con esa clave privada.
3. Usar el CSR con las herramientas de la AFIP para generar el certificado.
4. En Java, guardar el certificado en un _key store_.

### Autenticación

Para autenticarse, hay que enviar una solicitud CMS (Cryptographic Message Syntax), encriptada con la clave privada.
Para ello:

1. Abrir el _key store_.
2. Obtener la clave privada.
3. Armar el ticket de autenticación.
4. Encriptar el ticket con la clave privada.
5. Codificar el resultado usando Base64.
6. Usar el WSAA para obtener un token.
    * Para invocar, a su vez, se tiene que reconocer el certificado de la AFIP. Ese certificado se debe almacenar en 
otro _key store_, o agregarlo al _key store_ por defecto de la JVM.

El token sirve como máximo durante 24 horas.

### Autorización.

Para factura electrónica, se tiene que autorizar al certificado en cada servicio particular. Todos los web services
esperan un elemento XML con la información de autorización: token, sign, y el CUIT sobre el que se va a operar.

<<<<<<< HEAD
## Ejemplos

=======
## Uso del SDK

### Autenticación

La autenticación se realiza con la clase ```AutenticacionEndpoint```. La clase
debe encriptar el ticket de solicitud, para lo que usa una implementación de
```EncriptadorCMS```. ```EncriptadorCMS```, a su vez, necesita que exista el 
archivo _keystore_ (generado con los pasos mencionados anteriormente).
```AutenticacionEndpoint``` tiene dos extensiones de conveniencia, que ya 
incluyen las URLs del WSAA para homologación y producción:
* ```afip.sdk.homologacion.Autenticacion```
* ```afip.sdk.produccion.Autenticacion```

Si el archivo de _keystore_ es ```keystore.pfx```, entonces:

```
java.io.File keystore = new java.io.File("/ruta/al/archivo/keystore.pfx");
EncriptadorCMS encriptador = new EncriptadorCMS(
    new java.io.FileInputStream(keystore),
    "claveDelKeystore".toCharArray(),
    "1" // Este es el identificador del signer en el keystore
);
Autenticacion autenticacion = new afip.sdk.homologacion.Autenticacion(encriptador);
Credenciales credenciales = autenticacion.autenticar(Servicios.FACTURACION_ELECTRONICA);
```

Luego, el sign y el token se usan en cada web service.
>>>>>>> 058a6ad07a7557c28468774dfab3bf4e0a22545e
