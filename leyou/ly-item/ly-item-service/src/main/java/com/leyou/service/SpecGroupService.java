package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    SpecGroupMapper specGroupMapper;


    public void addSpecGroup(SpecGroup specGroup) {
        specGroupMapper.addSpecGroup(specGroup);
    }

    public List<SpecGroup> findAllSpecGroup(Long cid) {

        return  specGroupMapper.findAllSpecGroup(cid);
    }


    public void deleteSpecGroupById(Long id) {

         specGroupMapper.deleteByPrimaryKey(id);
    }

    public void updateSpecGroup(SpecGroup specGroup) {

        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }
}
