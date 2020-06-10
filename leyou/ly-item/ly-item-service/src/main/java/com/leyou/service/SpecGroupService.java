package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    SpecGroupMapper specGroupMapper;


    @Autowired
    SpecParamMapper specParamMapper;


    public void addSpecGroup(SpecGroup specGroup) {
        specGroupMapper.addSpecGroup(specGroup);
    }

    public List<SpecGroup> findAllSpecGroup(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);

        //根据id查询参数组及组内的参数列表

        List<SpecGroup> GroupList = new ArrayList<>();

        specGroupMapper.select(specGroup);

        GroupList = specGroupMapper.select(specGroup);

        GroupList.forEach(group -> {
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(group.getId());
            List<SpecParam> params = specParamMapper.select(specParam);
            group.setParams(params);
        });

        //return  specGroupMapper.findAllSpecGroup(cid);
        return GroupList;
    }


    public void deleteSpecGroupById(Long id) {

        specGroupMapper.deleteByPrimaryKey(id);
    }

    public void updateSpecGroup(SpecGroup specGroup) {

        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }
}
