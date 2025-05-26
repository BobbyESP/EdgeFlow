package com.bobbyesp.imagingedge.data.remote.parser

import java.util.regex.Pattern

object SoapResponseParser {
    private val resultPattern = Pattern.compile("<Result>(.*?)</Result>", Pattern.DOTALL)

    fun extractInnerXml(soapResponse: String): String {
        val matcher = resultPattern.matcher(soapResponse)
        if (matcher.find()) return matcher.group(1)
        error("No se pudo extraer <Result> del XML SOAP")
    }

    fun unescapeXml(escaped: String): String =
        escaped
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
}