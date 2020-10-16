/**
 *
 * Copyright (C) 1999-2020 Enrico Croce - AGPL >= 3.0
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 **/
package net.eiroca.library.license.api;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.xml.bind.DatatypeConverter;
import net.eiroca.library.system.LibFile;

public class EncryptionHelper {

  public static void create(final String privateUri, final String publicUri) throws LicenseException {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constants.KEY_ALG);
      keyGen.initialize(Constants.KEY_SIZE, new SecureRandom());
      final KeyPair keyPair = keyGen.generateKeyPair();
      LibFile.writeBytes(publicUri, keyPair.getPublic().getEncoded());
      LibFile.writeBytes(privateUri, keyPair.getPrivate().getEncoded());
    }
    catch (final Exception e) {
      throw new LicenseException(e);
    }
  }

  public static PublicKey readPublicKey(final String uri) throws Exception {
    final byte[] bytes = LibFile.getBytesFrom(uri);
    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
    final KeyFactory keyFactory = KeyFactory.getInstance(Constants.KEY_ALG);
    return keyFactory.generatePublic(keySpec);
  }

  public static PrivateKey readPrivateKey(final String uri) throws Exception {
    final byte[] bytes = LibFile.getBytesFrom(uri);
    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
    final KeyFactory keyFactory = KeyFactory.getInstance(Constants.KEY_ALG);
    final PrivateKey key = keyFactory.generatePrivate(keySpec);
    return key;
  }

  public static String sign(final byte[] message, final PrivateKey privateKey) throws Exception {
    final Signature dsa = Signature.getInstance(Constants.SIGNATURE_ALG);
    dsa.initSign(privateKey);
    dsa.update(message);
    final byte[] signature = dsa.sign();
    final String encoded = DatatypeConverter.printBase64Binary(signature);
    return encoded;
  }

}
