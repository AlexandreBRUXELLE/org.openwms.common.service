/*
 * Copyright 2005-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.common;

/**
 * A CommonMessageCodes.
 *
 * @author Heiko Scherrer
 */
public final class CommonMessageCodes {

    public static final String ACCOUNT_NO_DEFAULT ="COMMON.ACCOUNT_NO_DEFAULT";
    public static final String ACCOUNT_NOT_FOUND ="COMMON.ACCOUNT_NOT_FOUND";
    /** Code to signal that no TransportUnit with given Barcode exists. */
    public static final String BARCODE_NOT_FOUND ="COMMON.BARCODE_NOT_FOUND";
    public static final String LOCATION_NOT_FOUND = "COMMON.LOCATION_NOT_FOUND";
    public static final String LOCATION_GROUP_NOT_FOUND = "COMMON.LOCATION_GROUP_NOT_FOUND";
    public static final String TRANSPORT_UNIT_TYPE_NOT_FOUND = "COMMON.TRANSPORT_UNIT_TYPE_NOT_FOUND";

    private CommonMessageCodes() {
    }
}
