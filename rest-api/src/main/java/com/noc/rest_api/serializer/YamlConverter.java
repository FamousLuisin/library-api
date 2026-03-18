package com.noc.rest_api.serializer;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public final class YamlConverter extends AbstractJackson2HttpMessageConverter{
    
    protected YamlConverter(){
        super(new YAMLMapper()
            .setSerializationInclusion(JsonInclude.Include.ALWAYS), MediaType.parseMediaType(MediaType.APPLICATION_YAML_VALUE));
    }
}
