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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import java.io.Serializable;
import javax.persistence.Version;

/**
 * This base class for PKs Version is needed for the transaction
 *
 * @author dshurtleff
 * @param <T>
 */
public abstract class BasePK<T>
		implements Serializable, Comparable<T>
{

	@Version
	private String storageVersion;

	public BasePK()
	{
	}

	public abstract String pkValue();

	@Override
	public int compareTo(Object o)
	{
		if (o == null) {
			return -1;
		} else {
			if (o instanceof BasePK) {
				return ReflectionUtil.compareObjects(this.pkValue(), ((BasePK) o).pkValue());
			} else {
				return -1;
			}
		}
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

}
