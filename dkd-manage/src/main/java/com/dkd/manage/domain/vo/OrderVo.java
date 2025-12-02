package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Order;
import lombok.Data;

@Data
public class OrderVo extends Order {
    private String skuClassName;  // 商品类型（商品类型表）
}
