package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.Emp;

/**
 * 工单员工Service接口
 * 
 * @author youkk
 * @date 2025-07-12
 */
public interface IEmpService 
{
    /**
     * 查询工单员工
     * 
     * @param id 工单员工主键
     * @return 工单员工
     */
    public Emp selectEmpById(Long id);

    /**
     * 查询工单员工列表
     * 
     * @param emp 工单员工
     * @return 工单员工集合
     */
    public List<Emp> selectEmpList(Emp emp);

    /**
     * 新增工单员工
     * 
     * @param emp 工单员工
     * @return 结果
     */
    public int insertEmp(Emp emp);

    /**
     * 修改工单员工
     * 
     * @param emp 工单员工
     * @return 结果
     */
    public int updateEmp(Emp emp);

    /**
     * 批量删除工单员工
     * 
     * @param ids 需要删除的工单员工主键集合
     * @return 结果
     */
    public int deleteEmpByIds(Long[] ids);

    /**
     * 删除工单员工信息
     * 
     * @param id 工单员工主键
     * @return 结果
     */
    public int deleteEmpById(Long id);
}
