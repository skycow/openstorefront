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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.ReportService;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.report.BaseReport;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles report work-flow
 *
 * @author dshurtleff
 */
public class ReportServiceImpl
		extends ServiceProxy
		implements ReportService
{

	private static final Logger log = Logger.getLogger(OrientPersistenceService.class.getName());

	@Override
	public Report queueReport(Report report)
	{
		Objects.requireNonNull(report, "Report is required");

		report.setReportId(persistenceService.generateId());
		report.setRunStatus(RunStatus.PENDING);
		report.populateBaseCreateFields();
		report = persistenceService.persist(report);
		return report;
	}

	@Override
	public Report generateReport(Report report)
	{
		Objects.requireNonNull(report, "Report is required");

		//save Report
		Report managedReport;
		if (StringUtils.isBlank(report.getReportId())) {
			managedReport = queueReport(report);
		} else {
			managedReport = persistenceService.findById(Report.class, report.getReportId());
		}
		if (managedReport == null) {
			throw new OpenStorefrontRuntimeException("Unable to find report.", "Check report id: " + report.getReportId());
		}

		managedReport.setRunStatus(RunStatus.WORKING);
		managedReport.setUpdateDts(TimeUtil.currentDate());
		managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
		managedReport = persistenceService.persist(managedReport);

		//run report
		try {
			BaseReport reportGenerator = BaseReport.getReport(report);
			reportGenerator.generateReport();

			managedReport.setRunStatus(RunStatus.COMPLETE);
			managedReport.setUpdateDts(TimeUtil.currentDate());
			managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			managedReport = persistenceService.persist(managedReport);
		} catch (Exception e) {
			managedReport.setRunStatus(RunStatus.ERROR);
			managedReport.setUpdateDts(TimeUtil.currentDate());
			managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			managedReport = persistenceService.persist(managedReport);

			ErrorInfo errorInfo = new ErrorInfo(e, null);
			errorInfo.setErrorTypeCode(ErrorTypeCode.REPORT);
			errorInfo.setInputData("Report: " + report.getReportType() + " Format: " + report.getReportFormat() + " Report Id: " + report.getReportId() + " Create Date: " + report.getCreateDts());
			getSystemService().generateErrorTicket(errorInfo);

			//Note: we don't need to rethrow as we want to track report error separatly.
		}

		return managedReport;
	}

	@Override
	public void deleteReport(String reportId)
	{
		Report existingReport = persistenceService.findById(Report.class, reportId);
		if (existingReport != null) {

			Path path = existingReport.pathToReport();
			if (path != null) {
				if (path.toFile().exists()) {
					boolean success = path.toFile().delete();
					if (success == false) {
						log.log(Level.WARNING, MessageFormat.format("Unable to remove old report: {0}", path.toString()));
					}
				}
			}
			persistenceService.delete(existingReport);
		}
	}

	@Override
	public Map<String, List<String>> getSupportedFormats()
	{
		Map<String, List<String>> formatMap = new HashMap<>();

		//For now they only support csv
		List<ReportType> reportTypes = getLookupService().findLookup(ReportType.class);
		reportTypes.stream().forEach((reportType) -> {
			List<String> formats = new ArrayList<>();
			formats.add(ReportFormat.CSV);
			formatMap.put(reportType.getCode(), formats);
		});

		return formatMap;
	}

	@Override
	public ScheduledReport saveScheduledReport(ScheduledReport scheduledReport)
	{
		Objects.requireNonNull(scheduledReport, "Schedule report is required");

		ScheduledReport existing = persistenceService.findById(ScheduledReport.class, scheduledReport.getScheduleReportId());
		if (existing != null) {
			existing.updateFields(scheduledReport);
			persistenceService.persist(existing);
		} else {
			scheduledReport.setScheduleReportId(persistenceService.generateId());
			scheduledReport.populateBaseCreateFields();
			persistenceService.persist(scheduledReport);
		}
		return scheduledReport;
	}

	@Override
	public void deleteScheduledReport(String scheduledReportId)
	{
		ScheduledReport scheduledReport = persistenceService.findById(ScheduledReport.class, scheduledReportId);
		if (scheduledReport != null) {
			persistenceService.delete(scheduledReport);
		}
	}

}
