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
package net.eiroca.tools.licence.server.action;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.eiroca.library.server.ServerResponse;
import net.eiroca.tools.licence.server.util.LicenseServer;

public class AboutAction implements Handler {

  private final ServerResponse aboutMe = new ServerResponse(0, LicenseServer.SERVER_APINAME + " " + LicenseServer.SERVER_APIVERS);

  @Override
  public void handle(Context ctx) throws Exception {
    ctx.status(200);
    ctx.json(aboutMe);
  }

}
