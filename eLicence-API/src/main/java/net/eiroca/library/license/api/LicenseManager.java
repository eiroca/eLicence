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

import java.io.InputStream;
import java.security.PublicKey;
import net.eiroca.library.core.Helper;
import net.eiroca.library.data.SortedProperties;
import net.eiroca.library.system.LibFile;
import net.eiroca.library.system.Logs;

public class LicenseManager {

  private static LicenseManager licenseManager = new LicenseManager();

  public static LicenseManager getInstance() {
    return LicenseManager.licenseManager;
  }

  public static boolean isValidLicense(final License license) {
    return (license != null) && license.isValid() && !license.isExpired();
  }

  public License getLicense(final String product, final boolean createNew) {
    final String licFile = product + ".lic";
    final String defaultLicFile = "default.lic";
    final String keyFile = product + ".key";
    License license = null;
    try {
      license = getLicense(keyFile, licFile, defaultLicFile);
    }
    catch (final LicenseException e) {
      Logs.ignore(e);
      if (createNew) {
        license = new License(product);
      }
    }
    return license;
  }

  public License getLicense(final String publicKeyFile, final String... licensePaths) throws LicenseException {
    License license = null;
    try {
      InputStream inputStream = null;
      inputStream = LibFile.findResource(licensePaths);
      if (inputStream == null) { throw new LicenseNotFoundException(); }
      license = loadLicense(inputStream, publicKeyFile);
    }
    catch (final LicenseNotFoundException e) {
      throw e;
    }
    catch (final Exception e) {
      throw new LicenseException(e);
    }
    return license;
  }

  private License loadLicense(final InputStream inputStream, final String publicKeyFile) throws Exception {
    final License license = new License((SortedProperties)Helper.loadProperties(inputStream, true));
    final PublicKey publicKey = EncryptionHelper.readPublicKey(publicKeyFile);
    license.validate(publicKey);
    return license;
  }

}
