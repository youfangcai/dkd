package com.dkd.manage.mapper;

import java.util.List;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.vo.NodeVo;

/**
 * 点位Mapper接口
 * 
 * @author youfc
 * @date 2025-07-10
 */
public interface NodeMapper 
{
    /**
     * 查询点位
     * 
     * @param id 点位主键
     * @return 点位
     */
    public Node selectNodeById(Long id);

    /**
     * 查询点位列表
     * 
     * @param node 点位
     * @return 点位集合
     */
    public List<Node> selectNodeList(Node node);

    /**
     * 新增点位
     * 
     * @param node 点位
     * @return 结果
     */
    public int insertNode(Node node);

    /**
     * 修改点位
     * 
     * @param node 点位
     * @return 结果
     */
    public int updateNode(Node node);

    /**
     * 删除点位
     * 
     * @param id 点位主键
     * @return 结果
     */
    public int deleteNodeById(Long id);

    /**
     * 批量删除点位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNodeByIds(Long[] ids);

    /**
     * 查询点位管理列表
     * @param node
     * @return NodeVo集合
     */
    public List<NodeVo> selectNodeVoList(Node node);
}
