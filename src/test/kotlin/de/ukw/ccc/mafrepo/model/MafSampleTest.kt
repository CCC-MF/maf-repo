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

package de.ukw.ccc.mafrepo.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MafSampleTest {

    @Test
    fun shouldFindSingleNmNumber() {
        val records = MafSample.parser.parse(singleNmNumberContent.reader())

        assertThat(MafSample.nmNumbers(records.first())).containsExactly("NM_001234.1")
    }

    @Test
    fun shouldFindMultipleNmNumbers() {
        val records = MafSample.parser.parse(multipleNmNumberContent.reader())

        assertThat(MafSample.nmNumbers(records.first())).containsExactly("NM_004321.1","NM_009876.7")
    }

    companion object {
        const val singleNmNumberContent = "Transcript_ID\tall_effects\tRefSeq\n" +
                "ENST00000123456\tGENE1,inframe_deletion,p.Xyz1234del,ENST00000123456,NM_001234.1;GENE1,inframe_deletion,p.Xyz1234del,ENST00000654321,NM_004321.1;\tNM_001234.1"

        const val multipleNmNumberContent = "Transcript_ID\tall_effects\tRefSeq\n" +
                "ENST00000654321\tGENE1,inframe_deletion,p.Xyz1234del,ENST00000123456,NM_001234.1;GENE1,inframe_deletion,p.Xyz1234del,ENST00000654321,NM_004321.1,NM_009876.7;\tNM_004321.1,NM_009876.7"
    }

}