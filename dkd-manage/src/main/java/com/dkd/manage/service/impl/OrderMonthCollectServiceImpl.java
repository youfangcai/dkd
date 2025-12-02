package com.dkd.manage.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.OrderMonthCollectMapper;
import com.dkd.manage.domain.OrderMonthCollect;
import com.dkd.manage.service.IOrderMonthCollectService;

/**
 * 销售情况管理Service业务层处理
 * 
 * @author youkk
 * @date 2025-07-20
 */
@Service
public class OrderMonthCollectServiceImpl implements IOrderMonthCollectService 
{
    @Autowired
    private OrderMonthCollectMapper orderMonthCollectMapper;

    /**
     * 查询销售情况管理
     * 
     * @param id 销售情况管理主键
     * @return 销售情况管理
     */
    @Override
    public OrderMonthCollect selectOrderMonthCollectById(Long id)
    {
        return orderMonthCollectMapper.selectOrderMonthCollectById(id);
    }

    /**
     * 查询销售情况管理列表
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 销售情况管理
     */
    @Override
    public List<OrderMonthCollect> selectOrderMonthCollectList(OrderMonthCollect orderMonthCollect)
    {
        return orderMonthCollectMapper.selectOrderMonthCollectList(orderMonthCollect);
    }

    /**
     * 新增销售情况管理
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 结果
     */
    @Override
    public int insertOrderMonthCollect(OrderMonthCollect orderMonthCollect)
    {
        return orderMonthCollectMapper.insertOrderMonthCollect(orderMonthCollect);
    }

    /**
     * 修改销售情况管理
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 结果
     */
    @Override
    public int updateOrderMonthCollect(OrderMonthCollect orderMonthCollect)
    {
        return orderMonthCollectMapper.updateOrderMonthCollect(orderMonthCollect);
    }

    /**
     * 批量删除销售情况管理
     * 
     * @param ids 需要删除的销售情况管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderMonthCollectByIds(Long[] ids)
    {
        return orderMonthCollectMapper.deleteOrderMonthCollectByIds(ids);
    }

    /**
     * 删除销售情况管理信息
     * 
     * @param id 销售情况管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderMonthCollectById(Long id)
    {
        return orderMonthCollectMapper.deleteOrderMonthCollectById(id);
    }
}
