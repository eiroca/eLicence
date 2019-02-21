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
package net.eiroca.library.license.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

  public static final String INVALID_LICENSE = "Invalid license";
  public static final String LICENSE_FILE_NOT_FOUND = "License file not found";
  public static final String MISSING_SIGNATURE = "Missing signature";
  public static final String INVALID_SIGNATURE = "Invalid signature";

  public static final String P_EXPIRATION = "expiration-date";
  public static final String P_ISSUER = "issuer";
  public static final String P_HOLDER = "holder";
  public static final String P_SIGNATURE = "signature";
  public static final String P_PRODDUCT = "product-id";

  public static final Date EXPIRED_DATE = new Date(0);

  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

  // public static final String SIGNATURE_ALG = "SHA/DSA";
  public static final String SIGNATURE_ALG = "SHA256withRSA";
  public static final String KEY_ALG = "RSA";
  static final int KEY_SIZE = 2048;

}
