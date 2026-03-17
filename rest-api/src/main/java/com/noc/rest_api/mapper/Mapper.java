package com.noc.rest_api.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final ModelMapper mapper = new ModelMapper(); 

    public <O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin, destination);
    }

    public <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination){
        return origin.stream().map(object -> mapper.map(object, destination)).toList();
    }
}
