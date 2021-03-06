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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class AttributeAll
{

	private AttributeType attributeType;
	private List<AttributeCode> attributeCodes = new ArrayList<>();

	public AttributeAll()
	{
	}

	public AttributeType getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType)
	{
		this.attributeType = attributeType;
	}

	public List<AttributeCode> getAttributeCodes()
	{
		return attributeCodes;
	}

	public void setAttributeCodes(List<AttributeCode> attributeCodes)
	{
		this.attributeCodes = attributeCodes;
	}

}
