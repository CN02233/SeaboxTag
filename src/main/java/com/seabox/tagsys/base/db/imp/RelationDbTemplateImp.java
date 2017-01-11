package com.seabox.tagsys.base.db.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.seabox.tagsys.sys.entity.PageBean;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;

import com.seabox.tagsys.base.db.RelationDbTemplate;

/**
 * 关系数据库访问层,再此类中对SPRING提供的sqlsessionTemplate进行封装。
 * 另外该类实现RelationDbTemplate接口，防止未来换其他实现时改动过大，直接替换该实现类即可
 * 
 * @author SongChaoqun 20151214
 *
 */
// @Repository("relationDbTemplate")
public class RelationDbTemplateImp implements RelationDbTemplate {

	@Resource
	protected SqlSessionTemplate mybatisTemplate;

	@Override
	public int insert(String statement) {
		// TODO Auto-generated method stub
		return mybatisTemplate.insert(statement);
	}

	@Override
	public int insert(String statement, Object parameter) {
		// TODO Auto-generated method stub
		return mybatisTemplate.insert(statement, parameter);
	}

	@Override
	public <E> List<E> selectList(String arg) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectList(arg);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectList(statement, parameter);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectList(statement, parameter, rowBounds);
	}

	@Override
	public <V, K> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectMap(statement, parameter, mapKey);
	}

	@Override
	public <V, K> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectMap(statement, parameter, mapKey, rowBounds);
	}

	@Override
	public <V, K> Map<K, V> selectMap(String statement, String mapKey) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectMap(statement, mapKey);
	}

	@Override
	public <T> T selectOne(String statement) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectOne(statement);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		// TODO Auto-generated method stub
		return mybatisTemplate.selectOne(statement, parameter);
	}

	@Override
	public int update(String statement) {
		// TODO Auto-generated method stub
		return mybatisTemplate.update(statement);
	}

	@Override
	public int update(String statement, Object parameter) {
		// TODO Auto-generated method stub
		return mybatisTemplate.update(statement, parameter);
	}

	@Override
	public int delete(String statement) {
		// TODO Auto-generated method stub
		return mybatisTemplate.update(statement);
	}

	@Override
	public int delete(String statement, Object parameter) {
		// TODO Auto-generated method stub
		return mybatisTemplate.update(statement, parameter);
	}

	/**
	 * 分页查询,参数MAP中需要带出currPage当前页参数
	 * 
	 * @param statement
	 *            查询数据SQL
	 * @param cntStatement
	 *            查询数据总量SQL
	 * @param parameter
	 *            请求参数
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <E> PageBean<E> selectListForPage(String statement, String cntStatement, Object parameter) {
		// TODO Auto-generated method stub

		Integer currPage = new Integer((String) ((Map) parameter).get("currPage"));
		Integer pageSize = new Integer((String) ((Map) parameter).get("pageSize"));
		Integer startCurr = (currPage - 1) * pageSize;
		//Integer endCurr = startCurr + pageSize;

		Map paramNew = (Map) parameter;
		paramNew.put("beginIndex", startCurr);
		paramNew.put("pageSize", pageSize);

		PageBean<E> bean = new PageBean<E>();
		bean.setCurrPage(currPage);
		bean.setPageSize(pageSize);
		Integer pageCntData = mybatisTemplate.selectOne(cntStatement, paramNew);
		bean.setTotalNum(pageCntData);
		if (parameter instanceof Map) {

		}
		List<E> resultList = mybatisTemplate.selectList(statement, paramNew);
		bean.setList(resultList);

		return bean;
	}

}
