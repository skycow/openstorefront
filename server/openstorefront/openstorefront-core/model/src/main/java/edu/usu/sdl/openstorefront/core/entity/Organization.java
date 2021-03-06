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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Base64;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class Organization
		extends StandardEntity<Organization>
{

	@PK
	@NotNull
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@APIDescription("The organization id is the key")
	private String organizationId;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@APIDescription("This is the name of the organization")
	private String name;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@Sanitize(BasicHTMLSanitizer.class)
	private String description;

	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = OrganizationType.class)
	private String organizationType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	private String homeUrl;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String address;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String agency;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String department;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String contactName;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String contactPhone;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_EMAIL)
	@Pattern(regexp = OpenStorefrontConstant.EMAIL_PATTERN)
	@Sanitize(TextSanitizer.class)
	private String contactEmail;

	public Organization()
	{
	}

	public String nameToKey()
	{
		return Organization.toKey(getName());
	}

	public static String toKey(String name)
	{
		String key = null;
		if (name != null) {
			key = Base64.getUrlEncoder().encodeToString(name.toUpperCase().getBytes());
		}
		return key;
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		Organization updated = (Organization) entity;

		this.setName(updated.getName());
		this.setOrganizationId(nameToKey());
		this.setAddress(updated.getAddress());
		this.setAgency(updated.getAgency());
		this.setContactEmail(updated.getContactEmail());
		this.setContactName(updated.getContactName());
		this.setContactPhone(updated.getContactPhone());
		this.setDepartment(updated.getDepartment());
		this.setDescription(updated.getDescription());
		this.setHomeUrl(updated.getHomeUrl());
		this.setOrganizationType(updated.getOrganizationType());
	}

	public String getOrganizationId()
	{
		return organizationId;
	}

	public void setOrganizationId(String name)
	{
		this.organizationId = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getOrganizationType()
	{
		return organizationType;
	}

	public void setOrganizationType(String organizationType)
	{
		this.organizationType = organizationType;
	}

	public String getHomeUrl()
	{
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl)
	{
		this.homeUrl = homeUrl;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAgency()
	{
		return agency;
	}

	public void setAgency(String agency)
	{
		this.agency = agency;
	}

	public String getDepartment()
	{
		return department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public String getContactName()
	{
		return contactName;
	}

	public void setContactName(String contactName)
	{
		this.contactName = contactName;
	}

	public String getContactPhone()
	{
		return contactPhone;
	}

	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}

	public String getContactEmail()
	{
		return contactEmail;
	}

	public void setContactEmail(String contactEmail)
	{
		this.contactEmail = contactEmail;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
