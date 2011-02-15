package pl.jw.mbank.biz.common;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class) FIXME: czy ma byc?
@TransactionConfiguration
@ContextConfiguration("classpath:spring-hibernate.xml")
public abstract class MBankTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	protected SessionFactory sessionFactory;

	@Before
	public void init() {
	}

}
