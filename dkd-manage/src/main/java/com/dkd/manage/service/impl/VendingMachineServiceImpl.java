package com.dkd.manage.service.impl;

import com.dkd.common.constant.DkdContants;
import com.dkd.common.utils.DateUtils;
import com.dkd.common.utils.uuid.UUIDUtils;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.domain.VmType;
import com.dkd.manage.mapper.NodeMapper;
import com.dkd.manage.mapper.VendingMachineMapper;
import com.dkd.manage.mapper.VmTypeMapper;
import com.dkd.manage.service.IChannelService;
import com.dkd.manage.service.INodeService;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备管理Service业务层处理
 *
 * @author youkk
 * @date 2025-07-13
 */
@Service
public class VendingMachineServiceImpl implements IVendingMachineService {
    @Autowired
    private VendingMachineMapper vendingMachineMapper;
    @Autowired
    private VmTypeMapper vmTypeMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private IChannelService channelService;
    @Autowired
    private INodeService nodeService;

    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public VendingMachine selectVendingMachineById(Long id) {
        return vendingMachineMapper.selectVendingMachineById(id);
    }

    /**
     * 查询设备管理列表
     *
     * @param vendingMachine 设备管理
     * @return 设备管理
     */
    @Override
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine) {
        return vendingMachineMapper.selectVendingMachineList(vendingMachine);
    }

    /**
     * 新增设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertVendingMachine(VendingMachine vendingMachine) {
        // 新增设备
        vendingMachine.setCreateTime(DateUtils.getNowDate());
        // 同步其他表相关的信息
        /**
         * 1. 自动生成8位的设备编号
         */
        String innerCode = UUIDUtils.getUUID();
        vendingMachine.setInnerCode(innerCode);
        /**
         * 2. 查询售货机的类型表，获取售货机的容量
         */
        VmType vmType = vmTypeMapper.selectVmTypeById(vendingMachine.getVmTypeId());
        vendingMachine.setChannelMaxCapacity(vmType.getChannelMaxCapacity());
        /**
         * 3. 点位信息的获取
         */
        Node node = nodeMapper.selectNodeById(vendingMachine.getNodeId());
        BeanUtils.copyProperties(node, vendingMachine, "id", "createTime", "updateTime");
        vendingMachine.setAddr(node.getAddress());

        /**
         * 4. 添加设备的状态
         */
        vendingMachine.setVmStatus(DkdContants.VM_STATUS_NODEPLOY);
        vendingMachine.setCreateTime(DateUtils.getNowDate());
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        /**
         * 5. 保存设备
         */
        int result = vendingMachineMapper.insertVendingMachine(vendingMachine);
        /**
         * 6. 新增货道
         */
        List<Channel> channelList = new ArrayList<>();
        for (int i = 1; i <= vmType.getVmRow(); i++)
            for (int j = 1; j <= vmType.getVmCol(); j++){
                Channel channel = new Channel();
                channel.setVmId(vendingMachine.getId());
                channel.setInnerCode(innerCode);
                channel.setChannelCode(i+"-"+j);
                channel.setMaxCapacity(vmType.getChannelMaxCapacity());
                channel.setCreateTime(DateUtils.getNowDate());
                channel.setUpdateTime(DateUtils.getNowDate());
                channelList.add(channel);
            }
        channelService.batchInsertChannel(channelList); // 批量保存货道
        return result;
    }

    /**
     * 修改设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    public int updateVendingMachine(VendingMachine vendingMachine) {
        if (vendingMachine.getNodeId() != null){
            // 查询点位表，补充:区域、点位、合作商等信息
            Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
            BeanUtils.copyProperties(node, vendingMachine, "id", "createTime", "updateTime");
            vendingMachine.setAddr(node.getAddress());
        }
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        int result = vendingMachineMapper.updateVendingMachine(vendingMachine);
        return result;

    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineByIds(Long[] ids) {
        return vendingMachineMapper.deleteVendingMachineByIds(ids);
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineById(Long id) {
        return vendingMachineMapper.deleteVendingMachineById(id);
    }

    /**
     * 根据设备编号查询设备信息
     * @param innerCode
     * @return VendingMachine
     */
    @Override
    public VendingMachine selectVendingMachineByInnerCode(String innerCode) {
        return vendingMachineMapper.selectVendingMachineByInnerCode(innerCode);
    }
}
