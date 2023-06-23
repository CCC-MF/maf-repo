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

package de.ukw.ccc.mafrepo.web

import de.ukw.ccc.mafrepo.model.MafSample
import de.ukw.ccc.mafrepo.model.MafSampleRepository
import de.ukw.ccc.mafrepo.model.MafSimpleVariant
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.util.UriUtils

@ExtendWith(SpringExtension::class)
@ExtendWith(MockitoExtension::class)
@WebMvcTest(controllers = [SampleRestController::class])
class SampleRestControllerTest {

    @MockBean
    private lateinit var mafSampleRepository: MafSampleRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldReturnActiveSimpleVariants() {
        doAnswer {
            val barcode = it.getArgument<String>(0)
            dummyData(barcode)
        }.`when`(this.mafSampleRepository).findAllByTumorSampleBarcodeLikeIgnoreCase(anyString())

        mockMvc.perform(
            get("/samples/{id}/simplevariants", UriUtils.encode("H/2023/1", Charsets.UTF_8))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(
                jsonPath("$", Matchers.hasSize<MafSimpleVariant>(1))
            )
            .andExpect(
                jsonPath("$.[0].hugoSymbol", Matchers.equalTo("BRAF"))
            )
    }

    @Test
    fun shouldReturnAllSimpleVariants() {
        doAnswer {
            val barcode = it.getArgument<String>(0)
            dummyData(barcode)
        }.`when`(this.mafSampleRepository).findAllByTumorSampleBarcodeLikeIgnoreCase(anyString())

        mockMvc.perform(
            get("/samples/{id}/simplevariants", UriUtils.encode("H/2023/1", Charsets.UTF_8))
                .param("all", "1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(
                jsonPath("$", Matchers.hasSize<MafSimpleVariant>(2))
            )
            .andExpect(
                jsonPath("$.[0].hugoSymbol", Matchers.equalTo("BRAF"))
            )
    }

    companion object {
        fun dummyData(barcode: String): List<MafSample> {
            return listOf(
                MafSample(
                    tumorSampleBarcode = barcode,
                    upload = AggregateReference.to(0),
                    simpleVariants = setOf(
                        MafSimpleVariant(
                            tumorSampleBarcode = barcode,
                            hugoSymbol = "BRAF",
                            chromosome = "chr7",
                            gene = "ENSG00000157764",
                            startPosition = 1,
                            endPosition = 2,
                            referenceAllele = "ABC",
                            tumorSeqAllele2 = "DEF",
                            hgvsc = "X.Chr76.a",
                            hgvsp = "Y.Chr17.a",
                            tDepth = 25,
                            dbSnpRs = "novel",
                            allelicFrequency = 3.141,
                            nmNumber = "NM_00123.4",
                            panel = "OnkoTestPanel",
                            active = true
                        ),
                        MafSimpleVariant(
                            tumorSampleBarcode = barcode,
                            hugoSymbol = "ABC1",
                            chromosome = "chr1",
                            gene = "ENSG00000123456",
                            startPosition = 3,
                            endPosition = 4,
                            referenceAllele = "ABC",
                            tumorSeqAllele2 = "DEF",
                            hgvsc = "X.Chr76.a",
                            hgvsp = "Y.Chr17.a",
                            tDepth = 25,
                            dbSnpRs = "novel",
                            allelicFrequency = 3.141,
                            nmNumber = "NM_00456.7",
                            panel = "OnkoTestPanel",
                            active = false
                        )
                    )
                )
            )
        }
    }

}