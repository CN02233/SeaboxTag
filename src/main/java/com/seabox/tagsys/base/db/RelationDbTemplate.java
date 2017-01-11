package com.seabox.tagsys.base.db;

import java.util.List;
import java.util.Map;

import com.seabox.tagsys.sys.entity.PageBean;
import org.apache.ibatis.session.RowBounds;

/**
 * 关系型数据库Interface接口
 * @author SongChaoqun
 *
 */
public interface RelationDbTemplate {
	int insert(String statement);
	
	int insert(String statement, Object parameter);
	
	<E> List<E> selectList(String arg);
		
	<E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds);

	<V, K> Map<K,V> selectMap(String statement, Object parameter, String mapKey);
	
	<V, K> Map<K,V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds);
	
	<V, K> Map<K,V> selectMap(String statement, String mapKey);
	
	<T>T selectOne(String statement);
	
	<T>T selectOne(String statement, Object parameter);
	
	int update(String statement);
	
	int update(String statement, Object parameter);

	<E> List<E> selectList(String statement, Object parameter);
	
	int delete(String statement);
	
	int delete(String statement,Object parameter);
	
	<E> PageBean<E> selectListForPage(String statement, String cntStatement, Object parameter);
}
