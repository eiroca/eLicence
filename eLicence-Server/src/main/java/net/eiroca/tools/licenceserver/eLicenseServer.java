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
package net.eiroca.tools.licenceserver;

import net.eiroca.tools.licence.server.action.AboutAction;
import spark.ResponseTransformer;
import spark.Spark;

public class eLicenseServer {

  public static void main(final String[] args) {
    Spark.port(LicenseServer.getServerPort());
    final ResponseTransformer jSonRender = new JsonTransformer();
    Spark.get("/about", new AboutAction(), jSonRender);

    Spark.exception(Exception.class, (e, request, response) -> {
      e.printStackTrace();
    });

  }
}
