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

import de.ukw.ccc.mafrepo.model.MafSimpleVariantRepository
import de.ukw.ccc.mafrepo.testMafSimpleVariant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@ExtendWith(MockitoExtension::class)
@WebMvcTest(controllers = [SimpleVariantRestController::class])
class SimpleVariantRestControllerTest {

    @MockBean
    private lateinit var mafSimpleVariantRepository: MafSimpleVariantRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun anyInstant() = any(Instant::class.java) ?: Instant.now()

    @BeforeEach
    fun setup() {
        doAnswer {
            Optional.of(testMafSimpleVariant(1))
        }.`when`(mafSimpleVariantRepository).findById(anyLong())
    }

    @Test
    fun shouldActivateSimpleVariants() {
        mockMvc.perform(
            put("/simplevariant/{id}/active", 1)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(mafSimpleVariantRepository, times(1)).updateActiveById(anyLong(), captor.capture(), anyInstant())
        assertThat(captor.value).isTrue()
    }

    @Test
    fun shouldDisableSimpleVariants() {
        mockMvc.perform(
            delete("/simplevariant/{id}/active", 1)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(mafSimpleVariantRepository, times(1)).updateActiveById(anyLong(), captor.capture(), anyInstant())
        assertThat(captor.value).isFalse
    }

    @Test
    fun shouldUpdateSimpleVariantsInterpretationText() {
        mockMvc.perform(
            put("/simplevariant/{id}/interpretation", 1)
                .content("Test1234")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)

        verify(mafSimpleVariantRepository, times(1)).updateInterpretationById(anyLong(), anyString(), anyInstant())
    }

}