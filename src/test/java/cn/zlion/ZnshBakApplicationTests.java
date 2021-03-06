package cn.zlion;

import cn.zlion.dao.ClusterDataDao;
import cn.zlion.dao.DataResultDao;
import cn.zlion.domain.Sheet;
import cn.zlion.pagenationUtil.PageResult;
import cn.zlion.service.TableNameException;
import cn.zlion.service.YbTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ZnshBakApplicationTests {

//	@Autowired
//	private YbTestService ybTestService;
//	@Autowired
//	private DataResultDao dataResultDao;

	@Autowired
	private ClusterDataDao clusterDataDao;
	private final static SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void contextLoads() {
//		PageResult pageResult = clusterDataDao.findByTimeAndPage("TestApp", 1, 100, "T_RESULT_TASK", );
//		List<Map<String, Object>> datas = (List<Map<String,Object>>) pageResult.get("data");
//		for (Map data : datas){
//			System.out.println(data.get("pk"));
//		}

//		List<Sheet> sheets = ybTestService.sheets();
//		System.out.println(sheets);

//		clusterDataDao.findByTimeAndPage()
		try{
			Date date = sim.parse("2016-09-27 11:01:41.831000");
			System.out.println(date.getTime());
		}catch (ParseException e){
			e.printStackTrace();
		}
	}

}
