package com.nousin.springcloud.server.storage.framework.common.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nousin.springcloud.common.entity.BaseDo;
import com.nousin.springcloud.server.storage.framework.common.dao.CrudDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service CRUD基类(请勿擅自修改)
 *
 * @author Nousin
 * @version 2019-12-27
 */
public abstract class CrudService<D extends CrudDao<T>, T extends BaseDo> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public T get(String id) {
        return dao.get(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    /**
     * 分页查询列表数据
     *
     * @param entity
     * @return
     */
    public PageInfo<T> findPage(T entity, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(dao.findList(entity));
    }

    /**
     * 新增数据
     *
     * @param entity
     * @return
     */
    @Transactional
    public int insert(T entity) {
        return dao.insert(entity);
    }

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    @Transactional
    public int update(T entity) {
        return dao.update(entity);
    }

    /**
     * 删除数据
     *
     * @param entity
     */
    @Transactional
    public void delete(T entity) {
        dao.delete(entity);
    }

}
