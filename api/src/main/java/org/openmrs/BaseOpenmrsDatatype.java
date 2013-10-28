/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs;

import org.openmrs.customdatatype.SerializingCustomDatatype;

/**
 * This is a superclass for custom datatypes for data
 * 
 * @since 1.10
 */
public abstract class BaseOpenmrsDatatype<T extends OpenmrsObject> extends SerializingCustomDatatype<T> {
	
	/**
	 * @see org.openmrs.customdatatype.SerializingCustomDatatype#serialize(java.lang.Object)
	 */
	public String serialize(T typedValue) {
		if (typedValue == null)
			return null;
		return typedValue.getUuid();
	}
}