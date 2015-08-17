package ba.jamax.util.rest.persistence;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ba.jamax.util.rest.model.TestEntity;
import ba.jamax.util.rest.util.GenericUtils;

public class GenericUtilsTest {
	
	private GenericUtils<TestEntity> genericUtils = new GenericUtils<TestEntity>();


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetGetter() {
		Method getGroupOp = genericUtils.getGetter(TestEntity.class, "title");
		String methodName = getGroupOp.getName();
		Assert.assertTrue(methodName.equals("getTitle"));
	}
}