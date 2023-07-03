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

package de.ukw.ccc.mafrepo

import de.ukw.ccc.mafrepo.model.MafSimpleVariant
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

fun testCsvRecord(vararg content: Pair<String, String>): CSVRecord {
    val headline = content.joinToString("\t") { it.first }
    val values = content.joinToString("\t") { it.second }

    val parser = CSVFormat.RFC4180.builder()
        .setHeader()
        .setDelimiter("\t")
        .setSkipHeaderRecord(true)
        .build()

    return parser.parse("$headline\n$values".byteInputStream().reader()).records.first()
}

fun testMafSimpleVariant(testId: Int): MafSimpleVariant {
    return MafSimpleVariant(
        tumorSampleBarcode = "H/2023/$testId",
        hugoSymbol = "ABC$testId",
        chromosome = "chr$testId",
        gene = "ENSG0000012345$testId",
        startPosition = 3,
        endPosition = 4,
        referenceAllele = "ABC",
        tumorSeqAllele2 = "DEF",
        hgvsc = "X.Chr76.a",
        hgvsp = "Y.Chr17.a",
        tDepth = 25,
        dbSnpRs = "novel",
        allelicFrequency = 3.141,
        nmNumber = "NM_00456.$testId",
        panel = "OnkoTestPanel",
        active = false
    )
}