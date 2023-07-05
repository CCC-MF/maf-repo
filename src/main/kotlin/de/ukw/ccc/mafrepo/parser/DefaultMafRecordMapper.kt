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

package de.ukw.ccc.mafrepo.parser

import de.ukw.ccc.mafrepo.model.MafSimpleVariant
import de.ukw.ccc.mafrepo.normalizedTumorSampleBarcode
import org.apache.commons.csv.CSVRecord

class DefaultMafRecordMapper : MafRecordMapper {
    override fun apply(record: CSVRecord): MafSimpleVariant {
        return MafSimpleVariant(
            tumorSampleBarcode = record["Tumor_Sample_Barcode"].normalizedTumorSampleBarcode().orElse("INVALID"),
            hugoSymbol = record["Hugo_Symbol"],
            chromosome = "chr${record["Chromosome"]}",
            gene = record["Gene"],
            startPosition = record["Start_Position"].toLong(),
            endPosition = record["End_Position"].toLong(),
            referenceAllele = record["Reference_Allele"],
            tumorSeqAllele2 = record["Tumor_Seq_Allele2"],
            hgvsc = record["HGVSc"],
            hgvsp = record["HGVSp_Short"],
            tDepth = record["t_depth"].toLongOrNull() ?: 0,
            dbSnpRs = record["dbSNP_RS"],
            exon = exon(record),
            panel = record["Panel"],
            allelicFrequency = record["AF_alt_tum"].toDoubleOrNull() ?: .0,
            nmNumber = nmNumbers(record).joinToString(",")
        )
    }

    private fun nmNumbers(record: CSVRecord): List<String> {
        return record["RefSeq"].split(",").map { it.trim() }
    }


    companion object {
        fun exon(record: CSVRecord): String {
            val exonNumber = record["Exon_Number"]
            if (null != exonNumber) {
                return exonNumber.substringBefore('/')
            }
            return "";
        }
    }
}