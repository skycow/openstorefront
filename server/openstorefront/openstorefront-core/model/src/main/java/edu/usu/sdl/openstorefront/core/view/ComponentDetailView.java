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
import edu.usu.sdl.openstorefront.core.annotation.ParamTypeDescription;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SecurityMarkingType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * ComponentDetailView Resource
 *
 * @author dshurtleff
 */
public class ComponentDetailView
		extends StandardEntityView
{

	@NotNull
	@ParamTypeDescription("Key")
	private String componentId;

	@NotNull
	private String guid;

	@NotNull
	private String name;

	@NotNull
	private String description;

	@DataType(ComponentRelationshipView.class)
	private List<ComponentRelationshipView> relationships = new ArrayList<>();

	@NotNull
	private String organization;

	private Date releaseDate;
	private String version;

	@NotNull
	private String activeStatus;

	@NotNull
	private Date lastActivityDts;

	private Date lastViewedDts;

	private Date today;

	@NotNull
	private String createUser;

	@NotNull
	private Date createDts;

	@NotNull
	private String updateUser;

	@NotNull
	private Date updateDts;

	@NotNull
	private Date approvedDate;
	private String approvalState;

	private String componentType;

	@NotNull
	private String approvedUser;

	private Date submittedDts;
	private String notifyOfApprovalEmail;

	private String componentSecurityMarkingType;
	private String componentSecurityMarkingDescription;
	private Integer componentSecurityMarkingRank;
	private String componentSecurityMarkingStyle;

	private ComponentEvaluationView evaluation = new ComponentEvaluationView();

	@DataType(ComponentQuestionView.class)
	private List<ComponentQuestionView> questions = new ArrayList<>();

	@DataType(ComponentAttributeView.class)
	private List<ComponentAttributeView> attributes = new ArrayList<>();

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	@DataType(ComponentMetadataView.class)
	private List<ComponentMetadataView> metadata = new ArrayList<>();

	@DataType(ComponentMediaView.class)
	private List<ComponentMediaView> componentMedia = new ArrayList<>();

	@DataType(ComponentContactView.class)
	private List<ComponentContactView> contacts = new ArrayList<>();

	@DataType(ComponentResourceView.class)
	private List<ComponentResourceView> resources = new ArrayList<>();

	@DataType(ComponentReviewView.class)
	private List<ComponentReviewView> reviews = new ArrayList<>();

	@DataType(ComponentExternalDependencyView.class)
	private List<ComponentExternalDependencyView> dependencies = new ArrayList<>();

	private long componentViews = 0;

	public ComponentDetailView()
	{
	}

	public void setComponentDetails(Component component)
	{
		name = component.getName();
		guid = component.getGuid();
		description = component.getDescription();
		approvedDate = component.getApprovedDts();
		approvedUser = component.getApprovedUser();
		approvalState = component.getApprovalState();
		createUser = component.getCreateUser();
		createDts = component.getCreateDts();
		version = component.getVersion();
		activeStatus = component.getActiveStatus();
		releaseDate = component.getReleaseDate();
		organization = component.getOrganization();
		submittedDts = component.getSubmittedDts();
		notifyOfApprovalEmail = component.getNotifyOfApprovalEmail();
		lastActivityDts = component.getLastActivityDts();
		componentType = component.getComponentType();

		componentSecurityMarkingType = component.getSecurityMarkingType();

		if (StringUtils.isNotBlank(component.getSecurityMarkingType())) {

			Service service = ServiceProxyFactory.getServiceProxy();
			SecurityMarkingType securityMarkingType = service.getLookupService().getLookupEnity(SecurityMarkingType.class, componentSecurityMarkingType);

			if (securityMarkingType != null) {
				componentSecurityMarkingDescription = securityMarkingType.getDescription();
				componentSecurityMarkingRank = securityMarkingType.getSortOrder();
				componentSecurityMarkingStyle = securityMarkingType.getHighlightStyle();
			}
		}

		this.toStandardView(component);
		this.toStandardViewBaseEntities(tags);
		this.toStandardView(questions);
		this.toStandardView(attributes);
		this.toStandardView(metadata);
		this.toStandardView(componentMedia);
		this.toStandardView(contacts);
		this.toStandardView(resources);
		this.toStandardView(relationships);
		this.toStandardView(reviews);
		this.toStandardView(dependencies);
		this.toStandardView(evaluation.getEvaluationSections());

	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public ComponentEvaluationView getEvaluation()
	{
		return evaluation;
	}

	public void setEvaluation(ComponentEvaluationView evaluation)
	{
		this.evaluation = evaluation;
	}

	public List<ComponentAttributeView> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttributeView> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentMetadataView> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<ComponentMetadataView> metadata)
	{
		this.metadata = metadata;
	}

	public List<ComponentMediaView> getComponentMedia()
	{
		return componentMedia;
	}

	public void setComponentMedia(List<ComponentMediaView> componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	public List<ComponentContactView> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ComponentContactView> contacts)
	{
		this.contacts = contacts;
	}

	public List<ComponentResourceView> getResources()
	{
		return resources;
	}

	public void setResources(List<ComponentResourceView> resources)
	{
		this.resources = resources;
	}

	public List<ComponentReviewView> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ComponentReviewView> reviews)
	{
		this.reviews = reviews;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Date getApprovedDate()
	{
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate)
	{
		this.approvedDate = approvedDate;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public List<ComponentQuestionView> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<ComponentQuestionView> questions)
	{
		this.questions = questions;
	}

	public List<ComponentExternalDependencyView> getDependencies()
	{
		return dependencies;
	}

	public void setDependencies(List<ComponentExternalDependencyView> dependencies)
	{
		this.dependencies = dependencies;
	}

	public List<ComponentTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public long getComponentViews()
	{
		return componentViews;
	}

	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	public Date getLastViewedDts()
	{
		return lastViewedDts;
	}

	public void setLastViewedDts(Date lastViewedDts)
	{
		this.lastViewedDts = lastViewedDts;
	}

	public Date getToday()
	{
		return today;
	}

	public void setToday(Date today)
	{
		this.today = today;
	}

	public Date getSubmittedDts()
	{
		return submittedDts;
	}

	public void setSubmittedDts(Date submittedDts)
	{
		this.submittedDts = submittedDts;
	}

	public String getNotifyOfApprovalEmail()
	{
		return notifyOfApprovalEmail;
	}

	public void setNotifyOfApprovalEmail(String notifyOfApprovalEmail)
	{
		this.notifyOfApprovalEmail = notifyOfApprovalEmail;
	}

	public List<ComponentRelationshipView> getRelationships()
	{
		return relationships;
	}

	public void setRelationships(List<ComponentRelationshipView> relationships)
	{
		this.relationships = relationships;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentSecurityMarkingType()
	{
		return componentSecurityMarkingType;
	}

	public void setComponentSecurityMarkingType(String componentSecurityMarkingType)
	{
		this.componentSecurityMarkingType = componentSecurityMarkingType;
	}

	public String getComponentSecurityMarkingDescription()
	{
		return componentSecurityMarkingDescription;
	}

	public void setComponentSecurityMarkingDescription(String componentSecurityMarkingDescription)
	{
		this.componentSecurityMarkingDescription = componentSecurityMarkingDescription;
	}

	public Integer getComponentSecurityMarkingRank()
	{
		return componentSecurityMarkingRank;
	}

	public void setComponentSecurityMarkingRank(Integer componentSecurityMarkingRank)
	{
		this.componentSecurityMarkingRank = componentSecurityMarkingRank;
	}

	public String getComponentSecurityMarkingStyle()
	{
		return componentSecurityMarkingStyle;
	}

	public void setComponentSecurityMarkingStyle(String componentSecurityMarkingStyle)
	{
		this.componentSecurityMarkingStyle = componentSecurityMarkingStyle;
	}

}
