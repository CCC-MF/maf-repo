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

import de.ukw.ccc.mafrepo.model.MafSimpleVariantId
import de.ukw.ccc.mafrepo.model.MafSimpleVariantRepository
import de.ukw.ccc.mafrepo.model.MafSimpleVariantRepositoryExtensions.updateActiveById
import de.ukw.ccc.mafrepo.model.MafSimpleVariantRepositoryExtensions.updateInterpretationById
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class SimpleVariantRestController(
    private val mafSimpleVariantRepository: MafSimpleVariantRepository
) {

    @PutMapping(path = ["simplevariant/{id}/active"])
    fun updateActiveTrue(@PathVariable id: MafSimpleVariantId) {
        mafSimpleVariantRepository.findById(id).ifPresent {
            mafSimpleVariantRepository.updateActiveById(id, true)
        }
    }

    @DeleteMapping(path = ["simplevariant/{id}/active"])
    fun updateActiveFalse(@PathVariable id: MafSimpleVariantId) {
        mafSimpleVariantRepository.findById(id).ifPresent {
            mafSimpleVariantRepository.updateActiveById(id, false)
        }
    }

    @PutMapping(path = ["simplevariant/{id}/interpretation"])
    fun updateInterpretation(@PathVariable id: MafSimpleVariantId, @RequestBody value: String) {
        mafSimpleVariantRepository.findById(id).ifPresent {
            mafSimpleVariantRepository.updateInterpretationById(id, value)
        }
    }

}