package ba.jamax.util.rest.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import ba.jamax.util.rest.dao.GenericDAO;
import ba.jamax.util.rest.model.Filter;
import ba.jamax.util.rest.model.TestEntity;
import ba.jamax.util.rest.service.GenericService;

public class GenericServiceTest {

	GenericService<TestEntity> genericService = new TestService();
	GenericDAO<TestEntity> genericDAO = new TestDAO();
	private SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
	private Session session = Mockito.mock(Session.class);
	private Criteria criteria = Mockito.mock(Criteria.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
		Mockito.when(this.sessionFactory.getCurrentSession()).thenReturn(this.session);
		Mockito.when(this.session.createCriteria(TestEntity.class)).thenReturn(this.criteria);
		ReflectionTestUtils.setField(genericDAO, "sessionFactory", this.sessionFactory, SessionFactory.class);
		genericService.setDAO(genericDAO);
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindById() {
		TestEntity testEntity = this.genericService.findById(4L);
		Assert.assertNull(testEntity);
	}
	@Test
	public void testAddNew() {
		TestEntity testEntity = new TestEntity();
		testEntity.setTitle("test");
		testEntity = this.genericService.addNew(testEntity);
		Assert.assertNotNull(testEntity);
	}
	@Test
	public void testDelete() {
		TestEntity testEntity = new TestEntity();
		testEntity.setTitle("test");
		this.genericService.delete(testEntity);
	}
	@Test
	public void testUpdate() {
		TestEntity testEntity = new TestEntity();
		testEntity.setTitle("test2");
		this.genericService.update(testEntity);
	}
	@Test
	public void testFindByCriteria() {
		Map<String, Object> criterias = new HashMap<String, Object>();
		Filter filter = new Filter();
		filter.setGroupOp("test2");
		List<TestEntity> results = this.genericService.findByCriteria(criterias, filter, true, 0, Integer.MAX_VALUE, Order.asc("groupOp"));
		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.size());
	}
}