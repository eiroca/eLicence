/**
 *
 * Copyright (C) 1999-2019 Enrico Croce - AGPL >= 3.0
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
package net.eiroca.tools.licence.server.util;

import net.eiroca.library.core.Helper;

public class LicenseServer {

  public static final int SERVER_PORT = 2001;
  public static final String CFG_SERVERPORT = "server.port";
  public static final String SERVER_APINAME = "License Server";
  public static final String SERVER_APIVERS = "0.0.2";

  public static int getServerPort() {
    final int port = Helper.getInt(System.getProperty(LicenseServer.CFG_SERVERPORT), LicenseServer.SERVER_PORT);
    return port;
  }
}
