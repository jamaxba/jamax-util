package ba.jamax.util.test.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ba.jamax.util.rest.dao.BaseTestEntityDAOImpl;
import ba.jamax.util.rest.model.BaseTestEntity;
import ba.jamax.util.rest.model.Filter;
import ba.jamax.util.test.config.TestWebConfig;
import ba.jamax.util.test.config.WebContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		loader=WebContextLoader.class,
		classes={
			TestWebConfig.class,
		})
public class BaseDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	BaseTestEntityDAOImpl baseTestEntityDAOImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
//		Mockito.when(this.sessionFactory.getCurrentSession()).thenReturn(this.session);
//		Mockito.when(this.session.createCriteria(BaseTestEntity.class)).thenReturn(this.criteria);
//		ReflectionTestUtils.setField(genericDAO, "sessionFactory", this.sessionFactory, SessionFactory.class);
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCountByCriteria() {
		Map<String, Object> criterias = new HashMap<String, Object>();
		Filter filter = new Filter();
		filter.setGroupOp("test2");
		List<BaseTestEntity> results = this.baseTestEntityDAOImpl.findByCriteria(criterias, filter, true, 0, Integer.MAX_VALUE, Order.asc("groupOp"));
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.size());
	}
}