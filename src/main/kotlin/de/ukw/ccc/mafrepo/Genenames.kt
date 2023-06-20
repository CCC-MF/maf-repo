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

package de.ukw.ccc.mafrepo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class Genenames(
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper
) {

    private lateinit var genes: Set<Gene>

    @PostConstruct
    protected fun onOnit() {
        val resource = resourceLoader.getResource("classpath:hgnc_complete_set.json").inputStream
        val hgncSet = objectMapper.readValue(resource, HgncSet::class.java)
        this.genes = hgncSet.response.docs
    }

    fun findGeneByEnsemblGeneId(ensemblGeneId: String): Optional<Gene> {
        val gene = this.genes.firstOrNull { it.ensemblGeneId == ensemblGeneId.trim() } ?: return Optional.empty()
        return Optional.of(gene)
    }

}

private data class HgncSet(val response: Response)

private data class Response(val docs: Set<Gene>)

data class Gene(
    @JsonProperty("hgnc_id") val hgncId: String,
    @JsonProperty("ensembl_gene_id") val ensemblGeneId: String = "",
    val cosmic: String?,
    val symbol: String,
    val name: String
)