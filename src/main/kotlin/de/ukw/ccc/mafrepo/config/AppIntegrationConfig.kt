/*
 * MIT License
 *
 * Copyright (c) 2023 Comprehensive Cancer Center Mainfranken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.ukw.ccc.mafrepo.config

import de.ukw.ccc.mafrepo.service.FileUploadEvent
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.file.dsl.Files
import org.springframework.util.Assert
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@ConfigurationProperties(AppSourceFsProperties.NAME)
data class AppSourceFsProperties(
    val active: Boolean = false,
    var dir: File?
) {
    companion object {
        const val NAME = "app.source.fs"
    }
}

@Configuration
@EnableConfigurationProperties(AppSourceFsProperties::class)
@ConditionalOnProperty(name = ["app.source.fs.active"], havingValue = "true")
class AppIntegrationConfig {

    @Bean
    @ConditionalOnProperty(name = ["app.source.fs.dir"])
    fun fileInputFlow(
        applicationEventPublisher: ApplicationEventPublisher,
        appSourceFsProperties: AppSourceFsProperties
    ): IntegrationFlow {
        val sourceDir = appSourceFsProperties.dir
        Assert.state(null != sourceDir && sourceDir.isDirectory) {
            "Property 'app.source.fs.active' is 'true' but source directory is not available"
        }
        logger.info("Watching directory '{}' for MAF files", appSourceFsProperties.dir!!.canonicalPath)
        return IntegrationFlow
            .from(
                Files.inboundAdapter(sourceDir!!).patternFilter("*.maf")
            ) { e -> e.poller(Pollers.fixedDelay(10.seconds.toJavaDuration())) }
            .log()
            .handle { msg ->
                applicationEventPublisher.publishEvent(FileUploadEvent(File(msg.payload.toString())))
            }
            .get()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AppIntegrationConfig::class.java)
    }

}