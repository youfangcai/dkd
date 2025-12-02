package com.dkd.manage.service;

import java.rmi.ServerException;
import java.util.List;

import com.dkd.manage.domain.Emp;
import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;

/**
 * 工单Service接口
 * 
 * @author youkk
 * @date 2025-07-18
 */
public interface ITaskService 
{
    /**
     * 查询工单
     * 
     * @param taskId 工单主键
     * @return 工单
     */
    public Task selectTaskByTaskId(Long taskId);

    /**
     * 查询工单列表
     * 
     * @param task 工单
     * @return 工单集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增工单
     * 
     * @param task 工单
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改工单
     * 
     * @param task 工单
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 批量删除工单
     * 
     * @param taskIds 需要删除的工单主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     * 删除工单信息
     * 
     * @param taskId 工单主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单集合
     */
    public List<TaskVo> getTaskList(Task task);

    /**
     * 根据设备内部编号查询该区域内的设备管理员
     * @param innerCode
     * @return
     */
    public List<Emp> getEmpList(String innerCode);

    /**
     * 新增工单
     * @param taskDto
     * @return
     */
    public int insertTaskDto(TaskDto taskDto) throws ServerException;

    /**
     * 取消工单
     * @param task
     * @return
     */
    public int cancelTask(Task task) throws ServerException;
}
