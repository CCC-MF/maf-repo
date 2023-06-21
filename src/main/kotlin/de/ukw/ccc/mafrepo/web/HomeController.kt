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

import de.ukw.ccc.mafrepo.Genenames
import de.ukw.ccc.mafrepo.model.*
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class HomeController(
    private val mafSimpleVariantRepository: MafSimpleVariantRepository,
) {

    @GetMapping(path = ["/"])
    fun index(
        @RequestParam(required = false, defaultValue = "") l: String,
        @RequestParam(required = false, defaultValue = "0") y: Int,
        @RequestParam(required = false, defaultValue = "0") s: Int,
        model: Model
    ): String {
        if (l.isNotBlank() && y > 0 && s > 0) {
            val code = "$l/20$y/$s"
            model.addAttribute("simpleVariants", mafSimpleVariantRepository.findAllByTumorSampleBarcode(code))
        }
        return "index"
    }

}