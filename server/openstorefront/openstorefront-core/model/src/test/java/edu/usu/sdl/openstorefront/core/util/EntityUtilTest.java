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
package edu.usu.sdl.openstorefront.core.util;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class EntityUtilTest
{

	/**
	 * Test of isObjectsDifferent method, of class EntityUtil.
	 */
	@Test
	public void testIsObjectsDifferent()
	{
		System.out.println("isObjectsDifferent");

		Component component = new Component();
		component.setName("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = EntityUtil.isObjectsDifferent(component, component, consumeFieldsOnly);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareObjects method, of class EntityUtil.
	 */
	@Test
	public void testCompareObjects()
	{
		System.out.println("compareObjects");

		UserTypeCode userTypeCode = new UserTypeCode();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = EntityUtil.isObjectsDifferent(userTypeCode, userTypeCode, consumeFieldsOnly);
		assertEquals(expResult, result);

		UserTypeCode userTypeCodeDiff = new UserTypeCode();
		userTypeCodeDiff.setCode("Test");
		userTypeCodeDiff.setDescription("Test2");

		expResult = true;
		result = EntityUtil.isObjectsDifferent(userTypeCode, userTypeCodeDiff, consumeFieldsOnly);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getPKField method, of class EntityUtil.
	 *
	 * @throws java.lang.IllegalAccessException
	 */
	@Test
	public void testGetPKField() throws IllegalArgumentException, IllegalAccessException
	{
		System.out.println("getPKField");
		Component entity = new Component();
		entity.setComponentId("Test");

		Field result = EntityUtil.getPKField(entity);
		result.setAccessible(true);
		if ("Test".equals(result.get(entity)) == false) {
			fail("Unable to get Id");
		}

	}

	/**
	 * Test of isPKFieldGenerated method, of class EntityUtil.
	 */
	@Test
	public void testIsPKFieldGenerated()
	{
		System.out.println("isPKFieldGenerated");
		Component entity = new Component();
		boolean expResult = true;
		boolean result = EntityUtil.isPKFieldGenerated(entity);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getPKFieldValue method, of class EntityUtil.
	 */
	@Test
	public void testGetPKFieldValue()
	{
		System.out.println("getPKFieldValue");
		Component entity = new Component();
		entity.setComponentId("Test");
		String expResult = "Test";
		String result = EntityUtil.getPKFieldValue(entity);
		assertEquals(expResult, result);

	}

	/**
	 * Test of updatePKFieldValue method, of class EntityUtil.
	 */
	@Test
	public void testUpdatePKFieldValue()
	{
		System.out.println("updatePKFieldValue");
		Component entity = new Component();
		String value = "Test";
		EntityUtil.updatePKFieldValue(entity, value);
		assertEquals("Test", entity.getComponentId());
	}

	/**
	 * Test of compareConsumeFields method, of class EntityUtil.
	 */
	@Test
	public void testCompareConsumeFields()
	{
		System.out.println("compareConsumeFields");
		Component component = new Component();
		component.setName("Test");
		component.setActiveStatus(Component.ACTIVE_STATUS);

		Component component2 = new Component();
		component2.setName("Test");
		component2.setActiveStatus(Component.ACTIVE_STATUS);
		int expResult = 0;
		int result = EntityUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

		component2 = new Component();
		component2.setName("Test2");
		expResult = -1;
		result = EntityUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

	}

}
