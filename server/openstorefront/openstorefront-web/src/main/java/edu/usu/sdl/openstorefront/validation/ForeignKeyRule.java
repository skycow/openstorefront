/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.util.FK;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Field value needs to exist in foreign Table Doesn't support compound keys
 *
 * @author dshurtleff
 */
public class ForeignKeyRule
		extends BaseRule
{

	@Override
	protected boolean validate(Field field, Object dataObject)
	{
		boolean valid = true;

		if (dataObject != null) {
			FK fk = field.getAnnotation(FK.class);
			if (fk != null) {
				try {
					String value = BeanUtils.getProperty(dataObject, field.getName());
					if (value != null) {
						Class fkClass = fk.value();
						ServiceProxy serviceProxy = new ServiceProxy();
						Object entity = serviceProxy.getPersistenceService().findById(fkClass, value);
						if (entity == null) {
							valid = false;
						}
					}
				} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
					throw new OpenStorefrontRuntimeException("Unexpected error occur trying get original field value.", ex);
				}
			}
		}

		return valid;
	}

	@Override
	protected String getMessage()
	{
		return "Value is not exist foreign relationship";
	}

	@Override
	protected String getValidationRule(Field field)
	{
		FK fk = field.getAnnotation(FK.class);
		if (fk != null) {
			return "Field is required to be in foreign key: " + fk.value().getSimpleName();
		}
		return "Field is required in foreign key (FK annotation not found).";
	}

}