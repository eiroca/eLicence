
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
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import net.eiroca.library.core.Helper;
import net.eiroca.library.data.SortedProperties;
import net.eiroca.library.license.api.EncryptionHelper;
import net.eiroca.library.license.api.License;
import net.eiroca.library.license.api.LicenseException;
import net.eiroca.library.license.api.LicenseManager;
import net.eiroca.tools.licensetool.ToolOptions;

public class LicenseTool {

  public static final String TEMPLATE_FILE = "template.dat";
  public static final String PRIVATEKEY_FILE = "private.key";
  public static final String PUBLICKEY_FILE = "public.key";
  public static final String LICENSE_FILE = "license.dat";

  public static void main(final String[] args) {
    try {
      final Options options = ToolOptions.getOptions();
      final CommandLine opts = ToolOptions.parseOptions(options, args);
      final String action = opts.getOptionValue("action");
      String templateFile = opts.getOptionValue("template", LicenseTool.TEMPLATE_FILE);
      String privateKeyFile = opts.getOptionValue("privatekey", LicenseTool.PRIVATEKEY_FILE);
      String publicKeyFile = opts.getOptionValue("publickey", LicenseTool.PUBLICKEY_FILE);
      String licenseFile = opts.getOptionValue("license", LicenseTool.LICENSE_FILE);
      if (opts.hasOption(ToolOptions.PRODUCT)) {
        final String product = opts.getOptionValue(ToolOptions.PRODUCT);
        templateFile = product + ".template";
        licenseFile = product + ".lic";
        publicKeyFile = product + ".key";
        privateKeyFile = product + ".private_key";
      }
      if ("keys".equals(action)) {
        LicenseTool.generateKeys(privateKeyFile, publicKeyFile);
      }
      else if ("licence".equals(action)) {
        LicenseTool.createLicense(templateFile, privateKeyFile, licenseFile);
      }
      else if ("validate".equals(action)) {
        LicenseTool.validateLicense(publicKeyFile, licenseFile);
      }
      else {
        LicenseTool.generateKeys(privateKeyFile, publicKeyFile);
        LicenseTool.createLicense(templateFile, privateKeyFile, licenseFile);
        LicenseTool.validateLicense(publicKeyFile, licenseFile);
      }
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public static void validateLicense(final String publicKeyFile, final String licenseFile) throws Exception {
    System.out.println("Validating lisense " + licenseFile);
    final LicenseManager licenseManager = LicenseManager.getInstance();
    final License license = licenseManager.getLicense(publicKeyFile, licenseFile);
    System.out.println("license = " + license);
    System.out.println("valid = " + LicenseManager.isValidLicense(license));
  }

  public static void createLicense(final String templateFile, final String privateKeyFile, final String licenseFile) throws Exception {
    System.out.println("Creating lisense from  " + templateFile);
    final Properties properties = new SortedProperties();
    InputStream template = null;
    try {
      template = new FileInputStream(templateFile);
      properties.load(template);
      License.generateLicense(properties, privateKeyFile, licenseFile);
    }
    finally {
      Helper.close(template);
    }
    System.out.println("License generated in '" + licenseFile + "' file.");
  }

  private static void generateKeys(final String privateKeyFile, final String publicKeyFile) throws LicenseException {
    System.out.println("Creating keys " + privateKeyFile);
    EncryptionHelper.create(privateKeyFile, publicKeyFile);
  }

}
