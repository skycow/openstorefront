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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.ScheduledReportView;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/scheduledreports")
@APIDescription("Allows access to scheduled reports")
public class ScheduledReportResource
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Gets scheduled report records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ScheduledReportView.class)
	public Response getReports(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ScheduledReport reportExample = new ScheduledReport();
		reportExample.setActiveStatus(filterQueryParams.getStatus());
		List<ScheduledReport> reports = service.getPersistenceService().queryByExample(ScheduledReport.class, reportExample);
		reports = filterQueryParams.filter(reports);

		GenericEntity<List<ScheduledReportView>> entity = new GenericEntity<List<ScheduledReportView>>(ScheduledReportView.toReportView(reports))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets a schedule report record.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Report.class)
	@Path("/{id}")
	public Response getReport(
			@PathParam("id") String scheduleReportId
	)
	{
		ScheduledReport reportExample = new ScheduledReport();
		reportExample.setScheduleReportId(scheduleReportId);
		ScheduledReport report = service.getPersistenceService().queryOneByExample(ScheduledReport.class, reportExample);
		return sendSingleEntityResponse(report);
	}

	@POST
	@RequireAdmin
	@APIDescription("Schedules a new Report")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postAlert(ScheduledReport scheduledReport)
	{
		return handleSaveScheduledReport(scheduledReport, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a Scheduled Report Record")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response updateEntityValue(
			@PathParam("id")
			@RequiredParam String scheduledReportId,
			ScheduledReport scheduledReport)
	{

		ScheduledReport existing = service.getPersistenceService().findById(ScheduledReport.class, scheduledReportId);
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		scheduledReport.setScheduleReportId(scheduledReportId);
		return handleSaveScheduledReport(scheduledReport, false);
	}

	private Response handleSaveScheduledReport(ScheduledReport scheduledReport, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(scheduledReport);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getReportService().saveScheduledReport(scheduledReport);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/scheduledreports/" + scheduledReport.getScheduleReportId())).entity(scheduledReport).build();
		} else {
			return Response.ok(scheduledReport).build();
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Activates a Scheduled Report")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ScheduledReport.class)
	@Path("/{id}/activate")
	public Response activatesAlert(
			@PathParam("id") String scheduleReportId)
	{
		ScheduledReport scheduledReport = service.getPersistenceService().setStatusOnEntity(ScheduledReport.class, scheduleReportId, ScheduledReport.ACTIVE_STATUS);
		return sendSingleEntityResponse(scheduledReport);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Inactivates a Scheduled Report")
	@Path("/{id}")
	public void inactiveAlert(
			@PathParam("id") String scheduleReportId)
	{
		service.getPersistenceService().setStatusOnEntity(ScheduledReport.class, scheduleReportId, ScheduledReport.INACTIVE_STATUS);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Deletes a schedule report record")
	@Path("/{id}/force")
	public void deleteReport(
			@PathParam("id") String scheduleReportId)
	{
		service.getReportService().deleteScheduledReport(scheduleReportId);
	}

}
