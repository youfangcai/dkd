package com.dkd.manage.service.impl;

import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.dto.ChannelConfigDto;
import com.dkd.manage.domain.vo.ChannelVo;
import com.dkd.manage.mapper.ChannelMapper;
import com.dkd.manage.service.IChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 售货机货道Service业务层处理
 *
 * @author youkk
 * @date 2025-07-13
 */
@Service
public class ChannelServiceImpl implements IChannelService {
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 查询售货机货道
     *
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    @Override
    public Channel selectChannelById(Long id) {
        return channelMapper.selectChannelById(id);
    }

    /**
     * 查询售货机货道列表
     *
     * @param channel 售货机货道
     * @return 售货机货道
     */
    @Override
    public List<Channel> selectChannelList(Channel channel) {
        return channelMapper.selectChannelList(channel);
    }

    /**
     * 新增售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int insertChannel(Channel channel) {
        channel.setCreateTime(DateUtils.getNowDate());
        return channelMapper.insertChannel(channel);
    }

    /**
     * 修改售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int updateChannel(Channel channel) {
        channel.setUpdateTime(DateUtils.getNowDate());
        return channelMapper.updateChannel(channel);
    }

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelByIds(Long[] ids) {
        return channelMapper.deleteChannelByIds(ids);
    }

    /**
     * 删除售货机货道信息
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelById(Long id) {
        return channelMapper.deleteChannelById(id);
    }


    /**
     * 批量新增售货机货道
     *
     * @param channelList
     * @return 结果
     */
    @Override
    public int batchInsertChannel(List<Channel> channelList) {
        return channelMapper.batchInsertChannel(channelList);
    }

    /**
     * 批量查询售货机货道
     *
     * @param skuIds
     * @return
     */
    @Override
    public int countChannelSelectBySkuIds(Long[] skuIds) {
        return channelMapper.countChannelSelectBySkuIds(skuIds);
    }

    /**
     * 根据内部代码选择渠道信息
     * <p>
     * 此方法用于通过内部代码查询渠道信息，返回一个渠道信息的列表
     * 主要用于需要根据特定内部代码筛选渠道信息的场景
     *
     * @param innerCode 内部代码，用于查询渠道信息的关键词
     * @return 返回一个渠道信息列表，包含所有匹配内部代码的渠道信息
     */
    @Override
    public List<ChannelVo> selectChannelSelectByInnerCode(String innerCode) {
        return channelMapper.selectChannelSelectByInnerCode(innerCode);
    }

    /**
     * 设置渠道信息,并更新持久化渠道信息
     * @param channelConfigDto
     * @return
     */
    @Override
    public int setChannels(ChannelConfigDto channelConfigDto) {
        List<Channel> channelList = channelConfigDto.getChannelList().stream().map(dto -> {
            Channel channel = channelMapper.getChannelList(dto.getInnerCode(), dto.getChannelCode());
            if (channel != null) {
                channel.setSkuId(dto.getSkuId());
                channel.setUpdateTime(DateUtils.getNowDate());
            }
            return channel;
        }).collect(Collectors.toList());
        return channelMapper.batchUpdateChannel(channelList);
    }

}
