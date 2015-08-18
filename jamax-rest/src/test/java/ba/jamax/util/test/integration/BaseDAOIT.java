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
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import ba.jamax.util.rest.model.BaseTestEntity;
import ba.jamax.util.rest.model.Filter;
import ba.jamax.util.rest.service.BaseTestEntityService;
import ba.jamax.util.test.config.TestWebConfig;
import ba.jamax.util.test.config.WebContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		loader=WebContextLoader.class,
		classes={
			TestWebConfig.class,
		})
@TestExecutionListeners({ 
	DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
//    TransactionalTestExecutionListener.class
    DbUnitTestExecutionListener.class 
//    TransactionDbUnitTestExecutionListener.class 
})
@Transactional
@DatabaseSetup("classpath:dbunit/initial-data.xml")
public class BaseDAOIT extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	BaseTestEntityService baseTestEntityService;

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
	public void testCountByCriteria() {
		Map<String, Object> criterias = new HashMap<String, Object>();
//		Filter filter = new Filter();
//		filter.setGroupOp("test2");
//		List<BaseTestEntity> results = this.baseTestEntityDAOImpl.findByCriteria(criterias, filter, true, 0, Integer.MAX_VALUE, Order.asc("groupOp"));
//		Assert.assertNotNull(results);
//		Assert.assertEquals(0, results.size());
	}
}