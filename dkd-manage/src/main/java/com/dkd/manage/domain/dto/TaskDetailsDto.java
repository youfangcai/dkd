package com.dkd.manage.domain.dto;

import lombok.Data;

@Data
public class TaskDetailsDto {
    private Integer id;
    private Long expectCapacity; // 补货数量
    private Long skuId;
    private String skuName;
    private String skuImage;
}
