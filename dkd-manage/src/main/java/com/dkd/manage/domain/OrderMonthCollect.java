package com.dkd.manage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dkd.common.annotation.Excel;
import com.dkd.common.core.domain.BaseEntity;

/**
 * 销售情况管理对象 tb_order_month_collect
 * 
 * @author youkk
 * @date 2025-07-20
 */
public class OrderMonthCollect extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 合作商Id */
    @Excel(name = "合作商Id")
    private Long partnerId;

    /** 合作商名称 */
    @Excel(name = "合作商名称")
    private String partnerName;

    /** 区域Id */
    @Excel(name = "区域Id")
    private Long regionId;

    /** 地区名称 */
    @Excel(name = "地区名称")
    private String regionName;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private Long orderTotalMoney;

    /** 订单总数 */
    @Excel(name = "订单总数")
    private Long orderTotalCount;

    /** 月份 */
    @Excel(name = "月份")
    private Long month;

    /** 年份 */
    @Excel(name = "年份")
    private Long year;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }
    public void setPartnerName(String partnerName) 
    {
        this.partnerName = partnerName;
    }

    public String getPartnerName() 
    {
        return partnerName;
    }
    public void setRegionId(Long regionId) 
    {
        this.regionId = regionId;
    }

    public Long getRegionId() 
    {
        return regionId;
    }
    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }
    public void setOrderTotalMoney(Long orderTotalMoney) 
    {
        this.orderTotalMoney = orderTotalMoney;
    }

    public Long getOrderTotalMoney() 
    {
        return orderTotalMoney;
    }
    public void setOrderTotalCount(Long orderTotalCount) 
    {
        this.orderTotalCount = orderTotalCount;
    }

    public Long getOrderTotalCount() 
    {
        return orderTotalCount;
    }
    public void setMonth(Long month) 
    {
        this.month = month;
    }

    public Long getMonth() 
    {
        return month;
    }
    public void setYear(Long year) 
    {
        this.year = year;
    }

    public Long getYear() 
    {
        return year;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("partnerId", getPartnerId())
            .append("partnerName", getPartnerName())
            .append("regionId", getRegionId())
            .append("regionName", getRegionName())
            .append("orderTotalMoney", getOrderTotalMoney())
            .append("orderTotalCount", getOrderTotalCount())
            .append("month", getMonth())
            .append("year", getYear())
            .toString();
    }
}
