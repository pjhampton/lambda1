package com.pjhampton

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory
import spark.Spark.get
import spark.Spark.initExceptionHandler

class LambdaHandler @Throws(ContainerInitializationException::class)
constructor() : RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private val handler = SparkLambdaContainerHandler.getAwsProxyHandler()
    private var initialized = false
    private val log = LoggerFactory.getLogger(LambdaHandler::class.java)

    override fun handleRequest(awsProxyRequest: AwsProxyRequest, context: Context?): AwsProxyResponse {
        if(!initialized){
            defineRoutes()
            initialized = true
        }
        return handler.proxy(awsProxyRequest, context)
    }

    private fun defineRoutes(){
        BasicConfigurator.configure()
        initExceptionHandler { e ->
            log.error("Spark init failuer", e)
            System.exit(100)
        }
        get("/hello"){_, _ -> "Hello World"}
    }
}