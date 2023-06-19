/*
 * MIT License
 *
 *  Copyright (c) 2023 Comprehensive Cancer Center Mainfranken
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

package de.ukw.ccc.mafrepo.model

import org.apache.commons.csv.CSVFormat
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import java.io.InputStream
import java.io.InputStreamReader

typealias MafSampleId = Long

@Table("sample")
data class MafSample(
    @Id val id: MafSampleId? = null,
    val tumorSampleBarcode: String,
    val upload: AggregateReference<MafUpload, MafUploadId>,
    @MappedCollection
    val simpleVariants: Set<MafSimpleVariant>
) {
    companion object {
        fun map(uploadId: MafUploadId, inputStream: InputStream): Set<MafSample> {
            val parser = CSVFormat.RFC4180.builder()
                .setHeader()
                .setDelimiter("\t")
                .setSkipHeaderRecord(true)
                .build()
                .parse(InputStreamReader(inputStream))

            return parser.map {
                MafSimpleVariant(
                    tumorSampleBarcode = it["Tumor_Sample_Barcode"],
                    hugoSymbol = it["Hugo_Symbol"],
                    chromosome = "chr${it["Chromosome"]}",
                    gene = it["Gene"],
                    startPosition = it["Start_Position"].toLong(),
                    endPosition = it["End_Position"].toLong(),
                    referenceAllele = it["Reference_Allele"],
                    tumorSeqAllele2 = it["Tumor_Seq_Allele2"],
                    hgvsc = it["HGVSc"],
                    hgvsp = it["HGVSp_Short"],
                    tDepth = it["t_depth"].toLong(),
                    dbSnpRs = it["dbSNP_RS"]
                )
            }.groupBy { it.tumorSampleBarcode }.map {
                MafSample(null, it.key, AggregateReference.to(uploadId), it.value.toSet())
            }.toSet()
        }
    }
}

interface MafSampleRepository : CrudRepository<MafSample, MafSampleId> {
    fun findAllByUpload(uploadId: AggregateReference<MafUpload, MafUploadId>): Iterable<MafSample>
}