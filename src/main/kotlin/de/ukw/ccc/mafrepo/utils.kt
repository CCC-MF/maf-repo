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

import java.util.*

fun String.normalizedTumorSampleBarcode(): Optional<String> {
    val normalizedRegex = "[EHNRZ]/20\\d{2}/\\d+".toRegex()
    val otherRegex = "([EHNRZ])(\\d+)-(\\d{2})_?.*".toRegex()

    if (this matches normalizedRegex) {
        return Optional.of(this)
    } else if (this matches otherRegex) {
        val matches = otherRegex.find(this) ?: return Optional.empty()
        val (letter, number, shortYear) = matches.destructured
        return Optional.of("$letter/20$shortYear/$number")
    }

    return Optional.empty()
}

fun <T> Ok(data: T): Result<T> {
    return Result.success(data)
}

fun <T> Result<T>.isOk(): Boolean = this.isSuccess

fun <T> Err(err: Throwable): Result<T> {
    return Result.failure(err)
}

fun <T> Result<T>.isErr(): Boolean = this.isFailure