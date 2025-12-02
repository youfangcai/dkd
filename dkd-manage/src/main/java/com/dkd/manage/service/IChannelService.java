package com.dkd.manage.service;

import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.dto.ChannelConfigDto;
import com.dkd.manage.domain.vo.ChannelVo;

import java.util.List;

/**
 * 售货机货道Service接口
 *
 * @author youkk
 * @date 2025-07-13
 */
public interface IChannelService {
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
     * 批量删除售货机货道
     *
     * @param ids 需要删除的售货机货道主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);

    /**
     * 删除售货机货道信息
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);


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
     * @return
     */
    int countChannelSelectBySkuIds(Long[] skuIds);

    /*
     * @param innerCode 内部代码，用于查询渠道信息的关键词
     * @return 返回一个渠道信息列表，包含所有匹配内部代码的渠道信息
     */
    public List<ChannelVo> selectChannelSelectByInnerCode(String innerCode);

    /*
     * @param innerCode 渠道内部代码
     * @param channelCode 渠道货道编号
     * @return 渠道信息
     */
    public int setChannels(ChannelConfigDto channelConfigDto);
}
