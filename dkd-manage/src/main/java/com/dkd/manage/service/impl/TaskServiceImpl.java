package com.dkd.manage.service.impl;

import java.rmi.ServerException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.domain.dto.TaskDetailsDto;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;
import com.dkd.manage.mapper.VendingMachineMapper;
import com.dkd.manage.service.IEmpService;
import com.dkd.manage.service.ITaskDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.TaskMapper;
import com.dkd.manage.domain.Task;
import com.dkd.manage.service.ITaskService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工单Service业务层处理
 *
 * @author youkk
 * @date 2025-07-18
 */
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private VendingMachineMapper vendingMachineMapper;
    @Autowired
    private IEmpService empService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITaskDetailsService taskDetailsService;

    /**
     * 查询工单
     *
     * @param taskId 工单主键
     * @return 工单
     */
    @Override
    public Task selectTaskByTaskId(Long taskId) {
        return taskMapper.selectTaskByTaskId(taskId);
    }

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单
     */
    @Override
    public List<Task> selectTaskList(Task task) {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增工单
     *
     * @param task 工单
     * @return 结果
     */
    @Override
    public int insertTask(Task task) {
        task.setCreateTime(DateUtils.getNowDate());
        return taskMapper.insertTask(task);
    }

    /**
     * 修改工单
     *
     * @param task 工单
     * @return 结果
     */
    @Override
    public int updateTask(Task task) {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除工单
     *
     * @param taskIds 需要删除的工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskIds(Long[] taskIds) {
        return taskMapper.deleteTaskByTaskIds(taskIds);
    }

    /**
     * 删除工单信息
     *
     * @param taskId 工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskId(Long taskId) {
        return taskMapper.deleteTaskByTaskId(taskId);
    }

    @Override
    public List<TaskVo> getTaskList(Task task) {
        return taskMapper.selectTaskVoList(task);
    }

    @Override
    public List<Emp> getEmpList(String innerCode) {
        VendingMachine vendingMachine = vendingMachineMapper.selectVendingMachineByInnerCode(innerCode);
        if (vendingMachine != null) {
//            return taskMapper.getEmpList(vendingMachine.getRegionId());
        }
        return List.of();
    }

    /**
     * 添加工单
     *
     * @param taskDto
     * @return
     */

    @Transactional
    @Override
    public int insertTaskDto(TaskDto taskDto) throws ServerException {
        // 1. 获取设备信息
        VendingMachine vm = vendingMachineMapper.selectVendingMachineByInnerCode(taskDto.getInnerCode());
        if (vm == null) {
            throw new ServerException("设备不存在");
        }
        // 2. 检查工单与设备状态是否匹配
        checkTask(vm.getVmStatus(), taskDto.getProductTypeId());
        // 3. 查看是否有正在处理的工单
        hasTask(taskDto);
        // 4. 查询并校验员工是否存在
        Emp emp = empService.selectEmpById(taskDto.getUserId());
        if (emp == null) {
            throw new ServiceException("员工不存在");
        }
        // 5. 校验员工区域是否匹配
        if (!emp.getRegionId().equals(vm.getRegionId())) {
            throw new ServiceException("员工区域与设备区域不匹配");
        }
        // 6. 补充其余属性信息，并封装工单
        Task task = BeanUtil.copyProperties(taskDto, Task.class);
        task.setTaskStatus(DkdContants.TASK_STATUS_CREATE);
        task.setUserName(emp.getUserName());
        task.setRegionId(vm.getRegionId());
        task.setAddr(vm.getAddr());
        // 7. 自动自增创建工单编号
        task.setTaskCode(generateTaskCode());

        int taskResult =  taskMapper.insertTask(task);

        // 8. 判断是否为补货工单
        if (taskDto.getProductTypeId().equals(DkdContants.TASK_TYPE_SUPPLY)) {
            List<TaskDetailsDto> taskDetails = taskDto.getDetails();
            if (CollUtil.isEmpty(taskDetails)) {
                throw new ServerException("补货工单不能为空");
            }
            List<TaskDetails> taskDetailsList = taskDetails.stream().map(taskDetailsDto -> {
                TaskDetails details = BeanUtil.copyProperties(taskDetailsDto, TaskDetails.class);
                details.setTaskId(task.getTaskId());
                return details;
            }).collect(Collectors.toList());
            // 9. 批量保存补货工单详情
            taskDetailsService.batchTaskDetailsList(taskDetailsList);
        }
        return taskResult;
    }

    /**
     * 取消工单
     *
     * @param task
     * @return
     */
    @Override
    public int cancelTask(Task task) throws ServerException {
        Task taskDb = taskMapper.selectTaskByTaskId(task.getTaskId());
        if (taskDb.getTaskStatus().equals(DkdContants.TASK_STATUS_CANCEL) || taskDb.getTaskStatus().equals(DkdContants.TASK_STATUS_FINISH)){
            throw new ServerException("工单已经取消或者完成");
        }
        task.setTaskStatus(DkdContants.TASK_STATUS_CANCEL);
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /** 以下是辅助方法 **/

    /**
     * 检查工单与设备状态是否匹配
     *
     * @param vmStatus
     * @param productTypeId
     * @throws ServerException
     */
    public void checkTask(Long vmStatus, Long productTypeId) throws ServerException {
        // 投放
        if (productTypeId == DkdContants.TASK_TYPE_DEPLOY && vmStatus == DkdContants.VM_STATUS_RUNNING) {
            throw new ServerException("设备正在运行中， 无法投放");
        }
        // 撤机
        if (productTypeId == DkdContants.TASK_TYPE_REVOKE && vmStatus == DkdContants.VM_STATUS_NODEPLOY) {
            throw new ServerException("设备未运行， 无法撤机");
        }
        // 补货
        if (productTypeId == DkdContants.TASK_TYPE_SUPPLY && vmStatus == DkdContants.VM_STATUS_NODEPLOY) {
            throw new ServerException("设备未投放， 无法补货");
        }
        // 维护
        if (productTypeId == DkdContants.TASK_TYPE_REPAIR && vmStatus == DkdContants.VM_STATUS_NODEPLOY) {
            throw new ServerException("设备未投放， 无法维护");
        }
    }

    /**
     * 验证是否存在正在进行的工单
     *
     * @param taskDto
     * @throws ServerException
     */
    public void hasTask(TaskDto taskDto) throws ServerException {
        List<Long> taskStatusList = new ArrayList<>();
        taskStatusList.add(DkdContants.TASK_STATUS_CREATE);
        taskStatusList.add(DkdContants.TASK_STATUS_PROGRESS);
        for (Long taskStatus : taskStatusList) {
            Task task = new Task();
            task.setInnerCode(taskDto.getInnerCode());
            task.setProductTypeId(taskDto.getProductTypeId()); // 设置工单类型
            task.setTaskStatus(taskStatus);
            List<Task> taskList = taskMapper.selectTaskList(task);
            if (taskList != null && taskList.size() > 0) {
                throw new ServerException("该设备待处理或正在处理工单，无法创建新工单");
            }
        }
    }

    /**
     * 自动创建工单编号
     *
     * @return String 工单编号
     */
    public String generateTaskCode() {
        String dataStr = DateUtils.getDate().replaceAll("-", "");
        String key = "dkd.task.code" + dataStr;
        if (!redisTemplate.hasKey(key)) {
            // key缓存不存在，则设置初始key为1， 缓存1天
            redisTemplate.opsForValue().set(key, 1, Duration.ofDays(1));
            return dataStr + "0001";
        }
        // key缓存存在，则自增1, 也可以使用hultool工具的StrUtil.padPre()方法
        return dataStr + String.format("%04d", redisTemplate.opsForValue().increment(key));
    }

}
