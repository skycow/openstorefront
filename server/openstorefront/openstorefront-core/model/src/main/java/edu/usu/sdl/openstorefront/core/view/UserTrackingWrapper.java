/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps tracking record
 *
 * @author dshurtleff
 */
public class UserTrackingWrapper
		extends ListWrapper
{

	@DataType(UserTracking.class)
	private List<UserTracking> userTracking = new ArrayList<>();

	public UserTrackingWrapper()
	{
	}

	public UserTrackingWrapper(List<UserTracking> userTracking, long totalNumber)
	{
		this.totalNumber = totalNumber;
		this.results = userTracking.size();
		this.userTracking = userTracking;
	}

	public List<UserTracking> getUserTracking()
	{
		return userTracking;
	}

	public void setUserTracking(List<UserTracking> userTracking)
	{
		this.userTracking = userTracking;
	}

}
