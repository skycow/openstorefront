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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Classifies a new Alert")
public class AlertType
		extends LookupEntity
{

	public static final String USER_DATA = "USERD";
	public static final String SYSTEM_ERROR = "SYSERROR";
	public static final String COMPONENT_SUBMISSION = "CMPSUB";

	public AlertType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(USER_DATA, newLookup(AlertType.class, USER_DATA, "User Data"));
		codeMap.put(SYSTEM_ERROR, newLookup(AlertType.class, SYSTEM_ERROR, "System Error"));
		codeMap.put(COMPONENT_SUBMISSION, newLookup(AlertType.class, COMPONENT_SUBMISSION, "Component Submission"));
		return codeMap;
	}

}
