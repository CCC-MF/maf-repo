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

import de.ukw.ccc.mafrepo.testCsvRecord
import de.ukw.ccc.mafrepo.testMafSimpleVariant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultMafRecordMapperTest {

    private lateinit var mafRecordMapper: DefaultMafRecordMapper

    @BeforeEach
    fun setup() {
        this.mafRecordMapper = DefaultMafRecordMapper()
    }

    @Test
    fun testShouldMapRecord() {

        val record = testCsvRecord(
            "Tumor_Sample_Barcode" to "H/2023/1",
            "Hugo_Symbol" to "ABC1",
            "Chromosome" to "1",
            "Gene" to "ENSG00000123451",
            "Start_Position" to "3",
            "End_Position" to "4",
            "Reference_Allele" to "ABC",
            "Tumor_Seq_Allele2" to "DEF",
            "HGVSc" to "X.Chr76.a",
            "HGVSp_Short" to "Y.Chr17.a",
            "Exon_Number" to "42",
            "t_depth" to "25",
            "dbSNP_RS" to "novel",
            "Panel" to "OnkoTestPanel",
            "AF_alt_tum" to "3.141",
            "RefSeq" to "NM_00456.1"
        )

        val actual = this.mafRecordMapper.apply(record)

        assertThat(actual).isEqualTo(testMafSimpleVariant(1))
    }

}