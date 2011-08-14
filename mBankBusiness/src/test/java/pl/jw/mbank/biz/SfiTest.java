package pl.jw.mbank.biz;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.jw.mbank.biz.common.MBankTest;
import pl.jw.mbank.common.dto.SfiData;
import pl.jw.mbank.common.dto.pk.SfiPk;
import pl.jw.mbank.common.request.ISfi;

public class SfiTest extends MBankTest {

	@Autowired
	protected ISfi sfiDao;

	@Test
	public void sfi() {

		String nazwa = "blblas";

		SfiData sfiData = new SfiData();
		sfiData.setName(nazwa);

		SfiPk pk = sfiDao.add(sfiData);

		sfiData = sfiDao.get(sfiData);

		sessionFactory.getCurrentSession().flush();

		Assert.assertNotNull(sfiData);
		Assert.assertEquals(nazwa, sfiData.getName());

	}

	@Test
	public void get() throws Exception {
		List<SfiData> dataList = sfiDao.get();

		sessionFactory.getCurrentSession().flush();

		Assert.assertNotNull(dataList);

	}
}
