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

import de.ukw.ccc.mafrepo.model.MafSampleRepository
import de.ukw.ccc.mafrepo.model.MafUploadId
import de.ukw.ccc.mafrepo.model.MafUploadRepository
import de.ukw.ccc.mafrepo.service.FileHandlingService
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.multipart.MultipartFile

@Controller
class UploadController(
    private val mafUploadRepository: MafUploadRepository,
    private val mafSampleRepository: MafSampleRepository,
    private val fileHandlingService: FileHandlingService
) {

    @GetMapping(path = ["/uploads"])
    fun index(model: Model): String {
        model.addAttribute("uploads", mafUploadRepository.findAllByOrderByCreatedAtDesc())
        return "uploads"
    }

    @PostMapping(path = ["/uploads"])
    fun upload(file: MultipartFile): String {
        if (! fileHandlingService.handle(file.originalFilename.orEmpty(), file.inputStream)) {
            return "errors/400"
        }
        return "redirect:/uploads"
    }

    @GetMapping(path = ["/uploads/{id}"])
    fun uploads(@PathVariable id: MafUploadId, model: Model): String {
        val upload = mafUploadRepository.findByIdOrNull(id) ?: throw NotFoundException()
        model.addAttribute("upload", upload)
        model.addAttribute("samples", mafSampleRepository.findAllByUpload(AggregateReference.to(id)))
        return "upload"
    }

}