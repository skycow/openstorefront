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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ArticleTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class UsageReport
		extends BaseReport
{

	private List<String> userTrackingUsers = new ArrayList<>();

	public UsageReport(Report report)
	{
		super(report);
	}

	@Override
	protected void gatherData()
	{
		updateReportTimeRange();

		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setActiveStatus(UserTracking.ACTIVE_STATUS);
		userTrackingExample.setTrackEventTypeCode(TrackEventCode.LOGIN);

		UserTracking userTrackingStartExample = new UserTracking();
		userTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

		UserTracking userTrackingEndExample = new UserTracking();
		userTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

		QueryByExample queryByExample = new QueryByExample(userTrackingExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<UserTracking> userTrackings = service.getPersistenceService().queryByExample(UserTracking.class, queryByExample);
		for (UserTracking userTracking : userTrackings) {
			userTrackingUsers.add(userTracking.getCreateUser());
		}
	}

	private void updateReportTimeRange()
	{
		if (report.getReportOption().getPreviousDays() != null) {
			Instant instant = Instant.now();
			instant = instant.minus(1, ChronoUnit.DAYS);
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(new Date(instant.toEpochMilli())));
			report.getReportOption().setEndDts(TimeUtil.endOfDay(new Date(instant.toEpochMilli())));
		}
	}

	@Override
	protected void writeReport()
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;

		//write header
		cvsGenerator.addLine("Usage Report", sdf.format(TimeUtil.currentDate()));
		cvsGenerator.addLine("Data Time Range:  ", sdf.format(report.getReportOption().getStartDts()) + " - " + sdf.format(report.getReportOption().getEndDts()));
		cvsGenerator.addLine("Summary");
		cvsGenerator.addLine(
				"# Logins",
				"Current Active Watches",
				"Component Views",
				"Component Resources Clicked",
				"Article Views"
		);

		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setActiveStatus(UserWatch.ACTIVE_STATUS);
		long activeWatches = service.getPersistenceService().countByExample(userWatchExample);

		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);

		ComponentTracking componentTrackingStartExample = new ComponentTracking();
		componentTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

		ComponentTracking componentTrackingEndExample = new ComponentTracking();
		componentTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

		QueryByExample queryByExample = new QueryByExample(componentTrackingExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		long componentViews = service.getPersistenceService().countByExample(queryByExample);

		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
		long componentLinkClicks = service.getPersistenceService().countByExample(queryByExample);

		ArticleTracking articleTrackingExample = new ArticleTracking();
		articleTrackingExample.setActiveStatus(ArticleTracking.ACTIVE_STATUS);
		articleTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);

		ArticleTracking articletTrackingStartExample = new ArticleTracking();
		articletTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

		ArticleTracking articleTrackingEndExample = new ArticleTracking();
		articleTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

		queryByExample = new QueryByExample(articleTrackingExample);
		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(articletTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(articleTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		long articleViews = service.getPersistenceService().countByExample(queryByExample);

		cvsGenerator.addLine(
				userTrackingUsers.size(),
				activeWatches,
				componentViews,
				componentLinkClicks,
				articleViews
		);

		cvsGenerator.addLine("Details");
		cvsGenerator.addLine(
				"Username",
				"User GUID",
				"User Organization",
				"User Type",
				"User Email",
				"# Logins",
				"Component Viewed",
				"Component Resources Click",
				"Article Views"
		);

		UserProfile userProfileExample = new UserProfile();
		List<UserProfile> allUsers = service.getPersistenceService().queryByExample(UserProfile.class, userProfileExample);
		Map<String, UserProfile> userMap = new HashMap<>();
		allUsers.forEach(user -> {
			userMap.put(user.getUsername(), user);
		});

		Map<String, Long> trackMap = new HashMap<>();
		userTrackingUsers.forEach(username -> {
			if (trackMap.containsKey(username)) {
				trackMap.put(username, trackMap.get(username) + 1);
			} else {
				trackMap.put(username, 1L);
			}
		});

		for (String username : trackMap.keySet()) {
			UserProfile userProfile = userMap.get(username);

			componentTrackingExample = new ComponentTracking();
			componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
			componentTrackingExample.setCreateUser(username);

			componentTrackingStartExample = new ComponentTracking();
			componentTrackingStartExample.setEventDts(report.getReportOption().getStartDts());
			componentTrackingEndExample = new ComponentTracking();
			componentTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

			queryByExample = new QueryByExample(componentTrackingExample);
			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(componentTrackingStartExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(componentTrackingEndExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
			specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			long userComponentViews = service.getPersistenceService().countByExample(queryByExample);

			componentTrackingExample.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
			long userComponentLinkClicks = service.getPersistenceService().countByExample(queryByExample);

			articleTrackingExample = new ArticleTracking();
			articleTrackingExample.setActiveStatus(ArticleTracking.ACTIVE_STATUS);
			articleTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
			articleTrackingExample.setCreateUser(username);

			articletTrackingStartExample = new ArticleTracking();
			articletTrackingStartExample.setEventDts(report.getReportOption().getStartDts());

			articleTrackingEndExample = new ArticleTracking();
			articleTrackingEndExample.setEventDts(report.getReportOption().getEndDts());

			queryByExample = new QueryByExample(articleTrackingExample);
			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(articletTrackingStartExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			specialOperatorModel = new SpecialOperatorModel();
			specialOperatorModel.setExample(articleTrackingEndExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
			specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			long userArticleViews = service.getPersistenceService().countByExample(queryByExample);

			cvsGenerator.addLine(
					userProfile.getUsername(),
					userProfile.getExternalGuid() != null ? userProfile.getExternalGuid() : userProfile.getInternalGuid(),
					userProfile.getOrganization(),
					TranslateUtil.translate(UserTypeCode.class, userProfile.getUserTypeCode()),
					userProfile.getEmail(),
					trackMap.get(username),
					userComponentViews,
					userComponentLinkClicks,
					userArticleViews
			);
		}

	}

}
