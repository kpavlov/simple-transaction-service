package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.ws.model.ErrorCode
import com.github.kpavlov.txservice.ws.model.ErrorResponse
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class UnhandledExceptionMapper : ExceptionMapper<Throwable> {
    override fun toResponse(exception: Throwable): Response {
        val body = with(ErrorResponse()) {
            code = ErrorCode.INTERNAL_ERROR
            details = "Error: ${exception.message}"
            status = Response.Status.INTERNAL_SERVER_ERROR.statusCode
            this
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(body).build()
    }
}