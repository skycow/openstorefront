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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import java.text.MessageFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Connections to JIRA
 *
 * @author dshurtleff
 */
public class JiraManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(JiraManager.class.getName());

	private static BlockingQueue<JiraClient> clientPool;
	private static int maxPoolSize;

	public static void init()
	{
		String poolSize = PropertiesManager.getValue(PropertiesManager.KEY_JIRA_POOL_SIZE, "20");
		maxPoolSize = Convert.toInteger(poolSize);
		clientPool = new ArrayBlockingQueue<>(maxPoolSize, true);

		log.log(Level.FINE, MessageFormat.format("Filling Pool to: {0}", poolSize));
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl(PropertiesManager.getValue(PropertiesManager.KEY_JIRA_URL));
		connectionModel.setUsername(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		for (int i = 0; i < maxPoolSize; i++) {
			clientPool.offer(new JiraClient(connectionModel));
		}
	}

	public static void cleanup()
	{
		if (clientPool != null) {
			if (getAvavilableConnections() != maxPoolSize) {
				log.log(Level.WARNING, MessageFormat.format("{0} jira connections were in process. ", getAvavilableConnections()));
			}
		}
	}

	public static JiraClient getClient()
	{
		int waitTimeSeconds = Convert.toInteger(PropertiesManager.getValue(PropertiesManager.KEY_JIRA_CONNECTION_WAIT_TIME, "60"));
		try {
			JiraClient jiraClient = clientPool.poll(waitTimeSeconds, TimeUnit.SECONDS);
			if (jiraClient == null) {
				throw new OpenStorefrontRuntimeException("Unable to retrieve Jira Connection in time.  No resource available.", "Adjust jira pool size appropriate to load or try again", ErrorTypeCode.INTEGRATION);
			}
			jiraClient.initConnection();
			return jiraClient;
		} catch (InterruptedException ex) {
			throw new OpenStorefrontRuntimeException("Unable to retrieve Jira Connection - wait interrupted.  No resource available.", "Adjust jira pool size appropriate to load.", ex, ErrorTypeCode.INTEGRATION);
		}
	}

	public static void releaseClient(JiraClient jiraClient)
	{
		clientPool.offer(jiraClient);
	}

	public static int getMaxConnections()
	{
		return maxPoolSize;
	}

	public static int getAvavilableConnections()
	{
		return maxPoolSize - clientPool.remainingCapacity();
	}

	@Override
	public void initialize()
	{
		JiraManager.init();
	}

	@Override
	public void shutdown()
	{
		JiraManager.cleanup();
	}

}
