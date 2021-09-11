
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.eiroca.library.core.Helper;
import net.eiroca.library.data.SortedProperties;
import net.eiroca.library.license.api.EncryptionHelper;
import net.eiroca.library.license.api.License;
import net.eiroca.library.license.api.LicenseException;
import net.eiroca.library.license.api.LicenseManager;

public class BulkGenerator {

  private static final String DEFAULT_LICENCE = "default";
  private static final String BASEPATH = "./Products";
  private static final String KEYS_DIR = ".keys";
  private static final String TEMPLATE_DIR = "templates";
  private static final String TEMPLATE_SUFFIX = ".template";
  private static final String LICENSE_DIR = "licenses";
  private static final String LICENSE_SUFFIX = ".lic";
  private static final String KEY_SUFFIX = ".key";

  private static final String EXPIRATION_DATE = "expiration-date";
  private static final String PRODUCT_ID = "product-id";

  private static final Map<Character, Long> TIME_UNIT = new HashMap<>();
  static {
    BulkGenerator.TIME_UNIT.put(new Character('a'), new Long((long)365 * 24 * 60 * 60 * 1000));
    BulkGenerator.TIME_UNIT.put(new Character('m'), new Long((long)30 * 24 * 60 * 60 * 1000));
    BulkGenerator.TIME_UNIT.put(new Character('d'), new Long((long)1 * 24 * 60 * 60 * 1000));
  }

  static SimpleDateFormat expirationFormat = new SimpleDateFormat("yyyy/MM/dd");

  static long duration = (long)365 * 24 * 60 * 60 * 1000;
  static String publicKeyFormat = "{0}{1}.key";
  static String privateKeyFormat = "{0}{1}.private_key";

  public static void main(final String[] args) {
    final File folder = new File(BulkGenerator.BASEPATH);
    final File[] listOfFiles = folder.listFiles();
    for (final File listOfFile : listOfFiles) {
      if (listOfFile.isDirectory()) {
        final String productName = listOfFile.getName();
        try {
          BulkGenerator.processProduct(productName, BulkGenerator.BASEPATH + "/" + productName + "/");
        }
        catch (final Exception e) {
          System.err.println("Unable to create the licence " + e);
        }
      }
    }
  }

  private static void processProduct(final String productName, final String productPath) throws Exception {
    System.out.println("Product " + productName);
    final String keyPath = productPath + BulkGenerator.KEYS_DIR + "/";
    final File keyDir = new File(productPath + BulkGenerator.KEYS_DIR);
    if (!keyDir.exists()) {
      keyDir.mkdirs();
    }
    if (!keyDir.isDirectory()) {
      System.err.println("Invalid product directory (" + BulkGenerator.KEYS_DIR + " is not a directory");
      return;
    }
    final String privateKeyPath = MessageFormat.format(BulkGenerator.privateKeyFormat, keyPath, productName);
    final String publicKeyPath = MessageFormat.format(BulkGenerator.publicKeyFormat, keyPath, productName);
    if ((!BulkGenerator.fileExists(publicKeyPath)) || (!BulkGenerator.fileExists(privateKeyPath))) {
      BulkGenerator.generateKeys(privateKeyPath, publicKeyPath);
    }
    final String templateDir = productPath + BulkGenerator.TEMPLATE_DIR;
    final File folder = new File(templateDir);
    folder.mkdirs();
    final File[] listOfFiles = folder.listFiles();
    for (final File listOfFile : listOfFiles) {
      if (listOfFile.isFile()) {
        final String templatePath = listOfFile.getPath();
        if (templatePath.endsWith(BulkGenerator.TEMPLATE_SUFFIX)) {
          final String name = listOfFile.getName();
          final String templateName = name.substring(0, name.length() - BulkGenerator.TEMPLATE_SUFFIX.length());
          final String licenceName = templateName.equals(BulkGenerator.DEFAULT_LICENCE) ? BulkGenerator.DEFAULT_LICENCE : productName;
          final String licenseDir = productPath + BulkGenerator.LICENSE_DIR + "/" + templateName + "/";
          final File Licensefolder = new File(licenseDir);
          Licensefolder.mkdirs();
          final String licensePath = licenseDir + licenceName + BulkGenerator.LICENSE_SUFFIX;
          final String outkeyPath = licenseDir + productName + BulkGenerator.KEY_SUFFIX;
          BulkGenerator.createLicense(productName, templatePath, privateKeyPath, licensePath);
          BulkGenerator.verifyLicence(licensePath, publicKeyPath);
          if (!Paths.get(outkeyPath).toFile().exists()) {
            Files.copy(Paths.get(publicKeyPath), Paths.get(outkeyPath));
          }
        }
      }
    }
  }

  private static boolean verifyLicence(final String licensePath, final String publicKeyPath) throws LicenseException {
    final LicenseManager manger = LicenseManager.getInstance();
    final License lic = manger.getLicense(publicKeyPath, licensePath);
    return LicenseManager.isValidLicense(lic);
  }

  private static boolean fileExists(final String path) {
    return new File(path).exists();
  }

  private static void generateKeys(final String privateKeyFile, final String publicKeyFile) throws LicenseException {
    System.out.println("Creating keys " + privateKeyFile);
    EncryptionHelper.create(privateKeyFile, publicKeyFile);
  }

  public static void createLicense(final String productName, final String templateFile, final String privateKeyFile, final String licenseFile) throws Exception {
    final Properties properties = new SortedProperties();
    InputStream template = null;
    try {
      template = new FileInputStream(templateFile);
      properties.load(template);
      properties.setProperty(BulkGenerator.PRODUCT_ID, productName);
      String expiration = properties.getProperty(BulkGenerator.EXPIRATION_DATE);
      final long now = System.currentTimeMillis();
      if (expiration == null) {
        expiration = BulkGenerator.expirationFormat.format(new Date(now + BulkGenerator.duration));
        properties.setProperty(BulkGenerator.EXPIRATION_DATE, expiration);
      }
      else {
        expiration = expiration.trim();
        if (expiration.startsWith("+")) {
          int endPos = expiration.length();
          final Character unit = expiration.charAt(expiration.length() - 1);
          Long unitMS = BulkGenerator.TIME_UNIT.get(unit);
          if (unitMS == null) {
            unitMS = BulkGenerator.TIME_UNIT.get('d');
          }
          else {
            endPos--;
          }
          final long dur = Helper.getLong(expiration.substring(1, endPos), 1) * unitMS;
          expiration = BulkGenerator.expirationFormat.format(new Date(now + dur));
          properties.setProperty(BulkGenerator.EXPIRATION_DATE, expiration);
        }
      }
      License.generateLicense(properties, privateKeyFile, licenseFile);
    }
    finally {
      Helper.close(template);
    }
    System.out.println("License generated in '" + licenseFile + "' file.");
  }

}
