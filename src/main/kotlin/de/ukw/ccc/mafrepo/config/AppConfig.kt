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

import de.ukw.ccc.mafrepo.parser.DefaultMafFileParser
import de.ukw.ccc.mafrepo.parser.DefaultMafRecordMapper
import de.ukw.ccc.mafrepo.parser.MafFileParser
import de.ukw.ccc.mafrepo.parser.MafRecordMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(AppProperties.NAME)
data class AppProperties(
    val parser: Parsers = Parsers.DEFAULT
) {
    companion object {
        const val NAME = "app"
    }
}

enum class Parsers {
    DEFAULT
}

@Configuration
@EnableConfigurationProperties(AppProperties::class)
class AppConfig {

    @Bean
    fun mafRecordMapper(appProperties: AppProperties): MafRecordMapper {
        return when (appProperties.parser) {
            Parsers.DEFAULT -> DefaultMafRecordMapper()
        }
    }

    @Bean
    fun mafFileParser(mafRecordMapper: MafRecordMapper, appProperties: AppProperties): MafFileParser {
        return when (appProperties.parser) {
            Parsers.DEFAULT -> DefaultMafFileParser(mafRecordMapper)
        }
    }

}