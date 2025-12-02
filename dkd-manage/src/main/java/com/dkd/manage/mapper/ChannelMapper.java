package com.dkd.manage.mapper;

import java.util.List;

import com.dkd.common.core.domain.AjaxResult;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.vo.ChannelVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.lang.model.element.NestingKind;

/**
 * 售货机货道Mapper接口
 * 
 * @author youkk
 * @date 2025-07-13
 */
public interface ChannelMapper {
    /**
     * 查询售货机货道
     *
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    public Channel selectChannelById(Long id);

    /**
     * 查询售货机货道列表
     *
     * @param channel 售货机货道
     * @return 售货机货道集合
     */
    public List<Channel> selectChannelList(Channel channel);

    /**
     * 新增售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 删除售货机货道
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);

    /**
     * 批量新增售货机货道
     *
     * @param channelList
     * @return 结果
     */
    public int batchInsertChannel(List<Channel> channelList);

    /**
     * 根据skuId查询售货机货道
     *
     * @param skuIds
     * @return int
     */
    int countChannelSelectBySkuIds(Long[] skuIds);

    /**
     * 根据skuId查询售货机货道
     *
     * @param innerCode
     * @return List<ChannelVo> 集合
     */
    List<ChannelVo> selectChannelSelectByInnerCode(String innerCode);

    /**
     * 根据innerCode和channelCode查询售货机货道
     * @param innerCode
     * @param channelCode
     * @return
     */
    @Select("select * from tb_channel where inner_code=#{innerCode} and channel_code=#{channelCode}")
    Channel getChannelList(@Param("innerCode") String innerCode, @Param("channelCode") String channelCode);


    /**
     * 批量修改售货机货道
     * @param channelList
     * @return
     */
    int batchUpdateChannel(List<Channel> channelList);
}