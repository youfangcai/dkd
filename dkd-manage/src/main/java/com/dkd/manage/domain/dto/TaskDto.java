package com.dkd.manage.domain.dto;

import com.dkd.manage.domain.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long createType;  // 创建类型
    private String innerCode;
    private Long userId; // 执行人
    private Long assignorId; // 指派人
    private Long productTypeId; // 工单类型
    private String desc; // 描述信息
    private List<TaskDetailsDto> details; // 补货详情

}
