package com.github.kpavlov.txservice.ws

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

@Provider
internal class ObjectMapperProvider : ContextResolver<ObjectMapper> {

    private val objectMapper = ObjectMapper()
            .findAndRegisterModules()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)

    override fun getContext(type: Class<*>): ObjectMapper {
        return objectMapper
    }

}