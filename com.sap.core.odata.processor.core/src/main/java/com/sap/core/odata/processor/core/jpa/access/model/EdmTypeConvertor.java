/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.access.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmByte;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class EdmTypeConvertor {

	public static Class<?> convertToJavaType(EdmType edmType) throws ODataJPAModelException, ODataJPARuntimeException{
		if (edmType instanceof EdmString){
			return String.class;
		}
		else if (edmType instanceof EdmInt64 /*|| edmType instanceof (long.class)*/){
			return Long.TYPE;
		}
		else if (edmType instanceof EdmInt16 /*|| edmType instanceof (short.class)*/){
			return Short.TYPE;
		}
		else if (edmType instanceof EdmInt32 /*|| edmType instanceof (int.class)*/){
			return Integer.TYPE;
		}
		else if (edmType instanceof EdmDouble /*|| edmType instanceof (double.class)*/){
			return Double.TYPE;
		}
		else if (edmType instanceof EdmSingle /*||  edmType instanceof (float.class)*/){
			return Float.TYPE;
		}
		else if (edmType instanceof EdmDecimal){
			return BigDecimal.class;
		}
		else if (edmType instanceof EdmBinary){
			return byte[].class;
		}
		else if (edmType instanceof EdmByte /*||  edmType instanceof (byte.class)*/){
			return Byte.TYPE;
		}
//		else if (edmType instanceof EdmBinary){
//			return Byte[].class;
//		}
		else if (edmType instanceof EdmBoolean /*||  edmType instanceof (boolean.class)*/){
			return Boolean.TYPE;
		}
		else if (edmType instanceof EdmDateTime){
			return Date.class;
		}
//		else if (edmType instanceof EdmSimpleTypeKind.DateTime)){
//			return Calendar.class;
//		}
		else if (edmType instanceof EdmGuid){
			return UUID.class;
		}
		throw ODataJPAModelException.throwException(ODataJPAModelException.TYPE_NOT_SUPPORTED.addContent(edmType.toString()),null);
	}
}
