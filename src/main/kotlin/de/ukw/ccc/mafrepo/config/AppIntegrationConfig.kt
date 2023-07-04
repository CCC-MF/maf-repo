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
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.file.dsl.Files
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


@Configuration
class AppIntegrationConfig {

    @Value("\${app.source.fs.dir}")
    private lateinit var sourceDirectory: File

    @Bean
    @ConditionalOnProperty(name = ["app.source.fs.active"], havingValue = "true")
    fun fileInputFlow(
        applicationEventPublisher: ApplicationEventPublisher
    ): IntegrationFlow {
        return IntegrationFlow
            .from(
                Files.inboundAdapter(sourceDirectory).patternFilter("*.maf")
            ) { e -> e.poller(Pollers.fixedDelay(10.seconds.toJavaDuration())) }
            .log()
            .handle { msg ->
                applicationEventPublisher.publishEvent(FileUploadEvent(File(msg.payload.toString())))
            }
            .get()
    }

}