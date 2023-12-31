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
import de.ukw.ccc.mafrepo.normalizedTumorSampleBarcode
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class HomeController(
    private val mafSimpleVariantRepository: MafSimpleVariantRepository,
) {

    @GetMapping(path = ["/"])
    fun index(
        @RequestParam(required = false, defaultValue = "") l: String,
        @RequestParam(required = false, defaultValue = "") y: String,
        @RequestParam(required = false, defaultValue = "") s: String,
        @RequestParam(required = false, defaultValue = "") code: String,
        model: Model
    ): String {
        val c = "$l/20$y/$s"
        if (l.isNotBlank() && y.toIntOrNull() != null && s.toIntOrNull() != null && y.toIntOrNull()!! > 0 && s.toIntOrNull()!! > 0) {
            model.addAttribute("simpleVariants", mafSimpleVariantRepository.findAllByTumorSampleBarcodeOrderById(c))
        } else if (code.normalizedTumorSampleBarcode().isPresent) {
            val (l, year, s) = code.split('/')
            val y = year.subSequence(2, 4)
            return "redirect:/?l=$l&y=$y&s=$s"
        } else {
            model.addAttribute("invalidcode", c != "/20/" && !c.normalizedTumorSampleBarcode().isPresent)
        }
        return "index"
    }

}