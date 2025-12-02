package com.dkd.framework.aspectj;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.dkd.common.annotation.DataScope;
import com.dkd.common.core.domain.BaseEntity;
import com.dkd.common.core.domain.entity.SysRole;
import com.dkd.common.core.domain.entity.SysUser;
import com.dkd.common.core.domain.model.LoginUser;
import com.dkd.common.core.text.Convert;
import com.dkd.common.utils.SecurityUtils;
import com.dkd.common.utils.StringUtils;
import com.dkd.framework.security.context.PermissionContextHolder;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable
    {
        clearDataScope(point); // 清理数据权限，防止sql注入
        handleDataScope(point, controllerDataScope); // 处理数据权限
    }

    // 处理数据权限
    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            SysUser currentUser = loginUser.getUser(); // 获取当前对象的信息
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin())
            {
                // 普通用户的对该方法的权限字符
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext()); // 获取目标方法的权限字符串
                /* 设置数据范围（权限）过滤条件，根据当前用户，部门别名，用户别名，权限字符对 切点对象 进行数据过滤 */
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user 用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     * @param permission 权限字符
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String permission)
    {
        StringBuilder sqlString = new StringBuilder(); // 创建一个StringBuilder对象，用于拼接sql语句
        List<String> conditions = new ArrayList<String>(); // 创建一个List对象，用于保存数据权限过滤条件，用于去重

        for (SysRole role : user.getRoles()) // 遍历当前用户所拥有的角色
        {
            String dataScope = role.getDataScope(); // 获取当前角色的数据权限
            // 1. 如果当前角色的数据权限已经添加过了，则不添加
            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope))
            {
                continue;
            }
            // 2. 该角色没有该字符串权限，则不添加
            if (StringUtils.isNotEmpty(permission) && StringUtils.isNotEmpty(role.getPermissions())
                    && !StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission)))
            {
                continue;
            }
            // 3. 获取当前角色所拥有的部门权限
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            }
            // 4. 如果当前角色为自定义权限
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
                // 拼接当前角色拥有的自定义部位权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            }
            // 5. 如果当前角色为本部门权限
            else if (DATA_SCOPE_DEPT.equals(dataScope))
            {
                // 拼接本部权限
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            }
            // 6. 如果当前角色为部门及以下权限
            else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
            {
                // 拼接本部门及以下权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            }
            // 7. 如果当前角色为仅本人权限
            else if (DATA_SCOPE_SELF.equals(dataScope))
            {
                // 拼接本人权限
                if (StringUtils.isNotBlank(userAlias))
                {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                }
                else
                {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据，没有userAlias别名意味着
                    sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope); // 保存，用于后面去重
        }

        // 多角色情况下，所有角色都不包含传递过来的权限字符，这个时候sqlString也会为空，所以要限制一下,不查询任何数据
        if (StringUtils.isEmpty(conditions))
        {
            sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
            {
                BaseEntity baseEntity = (BaseEntity) params;
                /*关键： 设置数据权限过滤条件，即sql语句的where条件 */
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint)
    {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
