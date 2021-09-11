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

public class LicenseException extends Exception {

  private static final long serialVersionUID = 1997460156399574267L;

  public LicenseException(final Throwable cause) {
    super(Constants.INVALID_LICENSE, cause);
  }

  public LicenseException() {
    super(Constants.INVALID_LICENSE);
  }

  public LicenseException(final String message) {
    super(message);
  }

}
