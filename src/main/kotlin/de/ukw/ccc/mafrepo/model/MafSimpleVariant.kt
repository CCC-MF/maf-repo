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

import de.ukw.ccc.mafrepo.normalizedTumorSampleBarcode
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.Repository
import java.time.Instant
import java.util.*

typealias MafSimpleVariantId = Long

@Table("simple_variant")
data class MafSimpleVariant(
    @Id val id: MafSimpleVariantId? = null,
    val tumorSampleBarcode: String,
    val hugoSymbol: String,
    val chromosome: String,
    val gene: String,
    val startPosition: Long,
    val endPosition: Long,
    val referenceAllele: String,
    val tumorSeqAllele2: String,
    val hgvsc: String,
    val hgvsp: String,
    val tDepth: Long,
    val dbSnpRs: String,
    var allelicFrequency: Long = 0,
    var cosmicId: String = "",
    var interpretation: String = "",
    var active: Boolean = false,
    var hgncId: String? = null,
    var geneName: String? = null,
    @LastModifiedDate var modifiedAt: Instant? = null,
    @Version var version: Int = 0
) {

    fun hash(): String {
        return DigestUtils.sha256Hex(
            this.copy(modifiedAt = null).toString()
        )
    }

    fun isValid(): Boolean {
        return this.tumorSampleBarcode.normalizedTumorSampleBarcode().isPresent
    }

}

interface MafSimpleVariantRepository : Repository<MafSimpleVariant, MafSimpleVariantId> {

    fun findById(id: MafSimpleVariantId): Optional<MafSimpleVariant>

    fun findAllByTumorSampleBarcode(simpleBarcode: String): Iterable<MafSimpleVariant>

    @Modifying
    @Query("UPDATE simple_variant SET active=:value, modified_at=utc_timestamp(), version=version+1 WHERE id=:id")
    fun updateActiveById(id: MafSimpleVariantId, value: Boolean)

    @Modifying
    @Query("UPDATE simple_variant SET interpretation=:value, modified_at=utc_timestamp(), version=version+1 WHERE id=:id")
    fun updateInterpretationById(id: MafSimpleVariantId, value: String)

}