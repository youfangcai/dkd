package com.dkd.manage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.dkd.manage.domain.OrderMonthCollect;
import com.dkd.manage.service.IOrderMonthCollectService;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.common.core.page.TableDataInfo;

/**
 * 销售情况管理Controller
 * 
 * @author youkk
 * @date 2025-07-20
 */
@RestController
@RequestMapping("/manage/collect")
public class OrderMonthCollectController extends BaseController
{
    @Autowired
    private IOrderMonthCollectService orderMonthCollectService;

    /**
     * 查询销售情况管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderMonthCollect orderMonthCollect)
    {
        startPage();
        List<OrderMonthCollect> list = orderMonthCollectService.selectOrderMonthCollectList(orderMonthCollect);
        return getDataTable(list);
    }

    /**
     * 导出销售情况管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:export')")
    @Log(title = "销售情况管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderMonthCollect orderMonthCollect)
    {
        List<OrderMonthCollect> list = orderMonthCollectService.selectOrderMonthCollectList(orderMonthCollect);
        ExcelUtil<OrderMonthCollect> util = new ExcelUtil<OrderMonthCollect>(OrderMonthCollect.class);
        util.exportExcel(response, list, "销售情况管理数据");
    }

    /**
     * 获取销售情况管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderMonthCollectService.selectOrderMonthCollectById(id));
    }

    /**
     * 新增销售情况管理
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:add')")
    @Log(title = "销售情况管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderMonthCollect orderMonthCollect)
    {
        return toAjax(orderMonthCollectService.insertOrderMonthCollect(orderMonthCollect));
    }

    /**
     * 修改销售情况管理
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:edit')")
    @Log(title = "销售情况管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderMonthCollect orderMonthCollect)
    {
        return toAjax(orderMonthCollectService.updateOrderMonthCollect(orderMonthCollect));
    }

    /**
     * 删除销售情况管理
     */
    @PreAuthorize("@ss.hasPermi('manage:collect:remove')")
    @Log(title = "销售情况管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(orderMonthCollectService.deleteOrderMonthCollectByIds(ids));
    }
}
