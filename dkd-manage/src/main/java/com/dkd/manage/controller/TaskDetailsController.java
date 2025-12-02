package com.dkd.manage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dkd.common.core.domain.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dkd.common.annotation.Log;
import com.dkd.common.core.controller.BaseController;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.enums.BusinessType;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.service.ITaskDetailsService;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 工单详情Controller
 *
 * @author youkk
 * @date 2025-07-18
 */
@Api(tags = "工单详情管理")
@RestController
@RequestMapping("/manage/taskDetails")
public class TaskDetailsController extends BaseController
{
    @Autowired
    private ITaskDetailsService taskDetailsService;

    /**
     * 查询工单详情列表
     */
    @ApiOperation("查询工单详情列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @ApiParam(value = "工单详情查询参数", required = false) TaskDetails taskDetails)
    {
        startPage();
        List<TaskDetails> list = taskDetailsService.selectTaskDetailsList(taskDetails);
        return getDataTable(list);
    }

    /**
     * 导出工单详情列表
     */
    @ApiOperation("导出工单详情列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "导出成功"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:export')")
    @Log(title = "工单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(
            HttpServletResponse response,
            @ApiParam(value = "工单详情导出参数", required = false) TaskDetails taskDetails)
    {
        List<TaskDetails> list = taskDetailsService.selectTaskDetailsList(taskDetails);
        ExcelUtil<TaskDetails> util = new ExcelUtil<TaskDetails>(TaskDetails.class);
        util.exportExcel(response, list, "工单详情数据");
    }

    /**
     * 获取工单详情详细信息
     */
    @ApiOperation("获取工单详情详细信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:query')")
    @GetMapping(value = "/{detailsId}")
    public R<TaskDetails> getInfo(
            @ApiParam(value = "工单详情ID", required = true) @PathVariable("detailsId") Long detailsId)
    {
        return R.ok(taskDetailsService.selectTaskDetailsByDetailsId(detailsId));
    }

    /**
     * 新增工单详情
     */
    @ApiOperation("新增工单详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "创建成功"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:add')")
    @Log(title = "工单详情", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(
            @ApiParam(value = "工单详情对象", required = true) @RequestBody TaskDetails taskDetails)
    {
        return toR(taskDetailsService.insertTaskDetails(taskDetails));
    }

    /**
     * 修改工单详情
     */
    @ApiOperation("修改工单详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:edit')")
    @Log(title = "工单详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(
            @ApiParam(value = "工单详情对象", required = true) @RequestBody TaskDetails taskDetails)
    {
        return toR(taskDetailsService.updateTaskDetails(taskDetails));
    }

    /**
     * 删除工单详情
     */
    @ApiOperation("删除工单详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 400, message = "参数错误"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:remove')")
    @Log(title = "工单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{detailsIds}")
    public R remove(
            @ApiParam(value = "工单详情ID数组", required = true) @PathVariable Long[] detailsIds)
    {
        return toR(taskDetailsService.deleteTaskDetailsByDetailsIds(detailsIds));
    }

    /**
     * 查看工单补货详情
     * @param
     * @return
     */
    @ApiOperation("查看工单补货详情")
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 401, message = "用户未认证"),
            @ApiResponse(code = 403, message = "用户无权限")
    })
    @PreAuthorize("@ss.hasPermi('manage:task:list')")
    @GetMapping("/byTaskId/{taskId}")
    public R<List<TaskDetails>> getTaskDetailsBySkuId(
            @ApiParam(value = "工单ID", required = true) @PathVariable Long taskId)
    {
        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskId(taskId);
        return R.ok(taskDetailsService.selectTaskDetailsList(taskDetails));
    }
}