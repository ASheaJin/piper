package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.DeviceInfo;
import com.syswin.pipeline.db.model.DeviceInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceInfoRepository {
    long countByExample(DeviceInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    List<DeviceInfo> selectByExample(DeviceInfoExample example);

    DeviceInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DeviceInfo record, @Param("example") DeviceInfoExample example);

    int updateByExample(@Param("record") DeviceInfo record, @Param("example") DeviceInfoExample example);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);
}