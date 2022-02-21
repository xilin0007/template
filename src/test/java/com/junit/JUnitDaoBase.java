package com.junit;

/**
 * @author rent
 * @desc junit测试基类
 */
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.fxl.log.LogTool;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(Junit4ClassRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:conf/spring-context.xml"})
//@ContextConfiguration(locations = {"file:src/main/resources/conf/spring-context.xml"})
public class JUnitDaoBase extends AbstractJUnit4SpringContextTests {
	
	protected LogTool log = LogTool.getInstance(new Object[]{this.getClass()});
	
	//protected Logger logger = Logger.getLogger(this.getClass());
	
	//extends AbstractJUnit4SpringContextTests  AbstractTransactionalJUnit4SpringContextTests
	//protected Logger logger = Logger.getLogger(this.getClass());
	/*@Override
	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}*/
}
