package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.OrderMonthCollect;

/**
 * 销售情况管理Service接口
 * 
 * @author youkk
 * @date 2025-07-20
 */
public interface IOrderMonthCollectService 
{
    /**
     * 查询销售情况管理
     * 
     * @param id 销售情况管理主键
     * @return 销售情况管理
     */
    public OrderMonthCollect selectOrderMonthCollectById(Long id);

    /**
     * 查询销售情况管理列表
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 销售情况管理集合
     */
    public List<OrderMonthCollect> selectOrderMonthCollectList(OrderMonthCollect orderMonthCollect);

    /**
     * 新增销售情况管理
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 结果
     */
    public int insertOrderMonthCollect(OrderMonthCollect orderMonthCollect);

    /**
     * 修改销售情况管理
     * 
     * @param orderMonthCollect 销售情况管理
     * @return 结果
     */
    public int updateOrderMonthCollect(OrderMonthCollect orderMonthCollect);

    /**
     * 批量删除销售情况管理
     * 
     * @param ids 需要删除的销售情况管理主键集合
     * @return 结果
     */
    public int deleteOrderMonthCollectByIds(Long[] ids);

    /**
     * 删除销售情况管理信息
     * 
     * @param id 销售情况管理主键
     * @return 结果
     */
    public int deleteOrderMonthCollectById(Long id);
}
