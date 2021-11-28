/**
 *
 * Copyright (C) 1999-2021 Enrico Croce - AGPL >= 3.0
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import net.eiroca.library.core.Helper;
import net.eiroca.library.core.LibDate;
import net.eiroca.library.data.SortedProperties;

/**
 * Class for storing license data
 *
 */
public class License {

  boolean valid = false;
  private final SortedProperties features;

  public License(final String product) {
    features = new SortedProperties();
    features.put(Constants.P_PRODDUCT, product);
    valid = false;
  }

  public License(final SortedProperties features) {
    this.features = features;
    valid = false;
  }

  public boolean isValid() {
    return valid;
  }

  public String getExpiration() {
    return getFeature(Constants.P_EXPIRATION);
  }

  public String getIssuer() {
    return getFeature(Constants.P_ISSUER);
  }

  public String getHolder() {
    return getFeature(Constants.P_HOLDER);
  }

  public String getProduct() {
    return getFeature(Constants.P_PRODDUCT);
  }

  public Date getExpirationDate() {
    return Helper.getDate(getExpiration(), Constants.DATE_FORMAT, Constants.EXPIRED_DATE);
  }

  public boolean isExpired() {
    return System.currentTimeMillis() > getExpirationDate().getTime();
  }

  public int getDaysUntillExpire() {
    return LibDate.getNumberOfDays(new Date(), getExpirationDate());
  }

  public String getFeature(final String name) {
    return features.getProperty(name);
  }

  public List<String> getFeatureNames() {
    final List<String> featureNames = new ArrayList<>();
    for (final Object name : features.keySet()) {
      featureNames.add(String.valueOf(name));
    }
    return featureNames;
  }

  @Override
  public String toString() {
    return features.toString();
  }

  public void validate(final PublicKey publicKey) throws LicenseException {
    if (!features.containsKey(Constants.P_SIGNATURE)) { throw new LicenseException(Constants.MISSING_SIGNATURE); }
    final String signature = (String)features.remove(Constants.P_SIGNATURE);
    final String encoded = features.toString();
    try {
      if (!verify(encoded.getBytes(), signature, publicKey)) { throw new LicenseException(Constants.INVALID_SIGNATURE); }
    }
    catch (final Exception e) {
      throw new LicenseException(e);
    }
    valid = true;
  }

  private boolean verify(final byte[] message, final String signature, final PublicKey publicKey) throws Exception {
    final Signature dsa = Signature.getInstance(Constants.SIGNATURE_ALG);
    dsa.initVerify(publicKey);
    dsa.update(message);
    final byte[] decoded = Base64.getDecoder().decode(signature);
    return dsa.verify(decoded);
  }

  public static String generateLicense(final Properties features, final String privateKeyFile, final String LicenseFile) throws LicenseException {
    OutputStream output = null;
    try {
      output = new FileOutputStream(LicenseFile);
      final String license = License.generateLicense(features, privateKeyFile, output);
      return license;
    }
    catch (final FileNotFoundException e) {
      throw new LicenseException(e);
    }
    finally {
      Helper.close(output);
    }
  }

  public static String generateLicense(final Properties features, final String privateKeyFile, final OutputStream output) throws LicenseException {
    try {
      final PrivateKey privateKey = EncryptionHelper.readPrivateKey(privateKeyFile);
      final String encoded = features.toString();
      final String signature = EncryptionHelper.sign(encoded.getBytes(), privateKey);
      final Properties properties = new SortedProperties();
      properties.putAll(features);
      properties.setProperty(Constants.P_SIGNATURE, signature);
      properties.store(output, "License file");
      return signature;
    }
    catch (final Exception e) {
      throw new LicenseException(e);
    }
  }

}
