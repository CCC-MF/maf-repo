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

package de.ukw.ccc.mafrepo.service

import de.ukw.ccc.mafrepo.Genenames
import de.ukw.ccc.mafrepo.model.MafSampleRepository
import de.ukw.ccc.mafrepo.model.MafUpload
import de.ukw.ccc.mafrepo.model.MafUploadRepository
import de.ukw.ccc.mafrepo.parser.MafFileParser
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class FileHandlingService(
    private val mafUploadRepository: MafUploadRepository,
    private val mafSampleRepository: MafSampleRepository,
    private val genenames: Genenames,
    private val mafFileParser: MafFileParser
) {

    private val logger = LoggerFactory.getLogger(FileHandlingService::class.java)

    fun handle(filename: String, content: InputStream): Boolean {
        val contentString = content.readAllBytes().decodeToString()
        val upload = MafUpload(filename = filename, content = contentString)

        // Do not save the same file twice
        if (mafUploadRepository.findByHash(upload.hash).isPresent) return false

        val savedUpload = mafUploadRepository.save(upload)
        if (null != savedUpload.id) {
            try {
                val prepared = mafFileParser.apply(savedUpload.id, contentString.byteInputStream()).onEach { sample ->
                    sample.simpleVariants.onEach { simpleVariant ->
                        genenames.findGeneByEnsemblGeneId(simpleVariant.gene).ifPresent { gene ->
                            simpleVariant.geneName = gene.name
                            simpleVariant.hgncId = gene.hgncId
                        }
                    }
                }
                mafSampleRepository.saveAll(prepared)
            } catch (e: Exception) {
                // cleanup
                mafUploadRepository.delete(savedUpload)
                logger.error("Cannot handle file - cleaned up:", e)
                return false
            }
        }
        return true
    }

    @EventListener
    fun handleFileUploadEvent(event: FileUploadEvent) {
        handle(event.file.name, event.file.inputStream())
    }

}