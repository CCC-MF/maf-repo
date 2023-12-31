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
import de.ukw.ccc.mafrepo.model.MafSampleId
import de.ukw.ccc.mafrepo.model.MafSampleRepository
import de.ukw.ccc.mafrepo.model.MafSimpleVariant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriUtils
import java.util.*

@RestController
class SampleRestController(
    private val mafSampleRepository: MafSampleRepository
) {

    @GetMapping(path = ["samples/{id}"])
    fun getSample(@PathVariable id: MafSampleId): ResponseEntity<MafSample> {
        return ResponseEntity.of(mafSampleRepository.findById(id))
    }

    @DeleteMapping(path = ["samples/{id}"])
    fun deleteSample(@PathVariable id: MafSampleId) {
        mafSampleRepository.deleteById(id)
    }

    @GetMapping(path = ["samples/{id}/simplevariants"])
    fun findSamplesSimpleVariants(
        @PathVariable id: String,
        @RequestParam(required = false) panel: Optional<String>,
        @RequestParam(defaultValue = "false") all: Boolean
    ): ResponseEntity<List<MafSimpleVariant>> {
        val sampleId = UriUtils.decode(id, Charsets.UTF_8)
        val result = mafSampleRepository.findAllByTumorSampleBarcodeLikeIgnoreCase(sampleId).flatMap {
            it.simpleVariants
        }.filter {
            all || it.active
        }.filter {
            panel.isEmpty || panel.get() == it.panel
        }
        return ResponseEntity.ok(result)
    }

}