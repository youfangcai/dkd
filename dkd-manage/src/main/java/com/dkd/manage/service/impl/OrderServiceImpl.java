package com.dkd.manage.service.impl;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import com.dkd.common.utils.DateUtils;
import com.dkd.common.utils.uuid.IdUtils;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.domain.vo.OrderVo;
import com.dkd.manage.service.ISkuClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.OrderMapper;
import com.dkd.manage.domain.Order;
import com.dkd.manage.service.IOrderService;

/**
 * 订单管理Service业务层处理
 * 
 * @author youkk
 * @date 2025-07-20
 */
@Service
public class OrderServiceImpl implements IOrderService 
{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISkuClassService skuClassService;

    /**
     * 查询订单管理
     * 
     * @param id 订单管理主键
     * @return 订单管理
     */
    @Override
    public OrderVo selectOrderById(String id)
    {
        Order order = orderMapper.selectOrderById(id);
        OrderVo orderVo = BeanUtil.copyProperties(order, OrderVo.class);
        if (orderVo.getClassId() == null){
            orderVo.setSkuClassName("暂时未设置类型");
            return orderVo;
        }
        orderVo.setSkuClassName(skuClassService.selectSkuClassByClassId(order.getClassId()).getClassName());
        return orderVo;
    }

    /**
     * 查询订单管理列表
     * 
     * @param order 订单管理
     * @return 订单管理
     */
    @Override
    public List<Order> selectOrderList(Order order)
    {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单管理
     * 
     * @param order 订单管理
     * @return 结果
     */
    @Override
    public int insertOrder(Order order)
    {
        // 生成 id
        order.setId(new IdUtils().randomUUID());
        order.setCreateTime(DateUtils.getNowDate());
        return orderMapper.insertOrder(order);
    }

    /**
     * 修改订单管理
     * 
     * @param order 订单管理
     * @return 结果
     */
    @Override
    public int updateOrder(Order order)
    {
        order.setUpdateTime(DateUtils.getNowDate());
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单管理
     * 
     * @param ids 需要删除的订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderByIds(String[] ids)
    {
        return orderMapper.deleteOrderByIds(ids);
    }

    /**
     * 删除订单管理信息
     * 
     * @param id 订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderById(String id)
    {
        return orderMapper.deleteOrderById(id);
    }
}
