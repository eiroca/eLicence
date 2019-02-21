/**
 *
 * Copyright (C) 2001-2019 eIrOcA (eNrIcO Croce & sImOnA Burzio) - AGPL >= 3.0
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
package net.eiroca.tools.licensetool;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ToolOptions {

  public static final String PRODUCT = "product";

  public static Options getOptions() {
    final Options options = new Options();
    options.addOption("h", "help", false, "Help");
    options.addOption("a", "action", true, "Action (keys, generate, validate)");
    options.addOption("t", "template", true, "Licence template file");
    options.addOption("l", "license", true, "Licence file");
    options.addOption("p", ToolOptions.PRODUCT, true, "Product Name");
    options.addOption("privatekey", true, "privatekey file");
    options.addOption("publickey", true, "publickey file");
    return options;
  }

  public static void Help(final Options options) {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("LicenseTool", options);
    System.exit(1);
  }

  public static CommandLine parseOptions(final Options options, final String[] args) {
    CommandLine line = null;
    final CommandLineParser parser = new DefaultParser();
    try {
      line = parser.parse(options, args);
    }
    catch (final ParseException exp) {
      System.err.println(exp.getMessage());
      ToolOptions.Help(options);
    }
    if (line.hasOption('h')) {
      ToolOptions.Help(options);
    }
    return line;
  }

}
