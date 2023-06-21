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

const dateFormatOptions = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: 'numeric', second: 'numeric' };
const dateFormat = new Intl.DateTimeFormat('de-DE', dateFormatOptions);

window.onload = () => {
    Array.from(document.getElementsByTagName('time')).forEach((timeTag) => {
        let date = Date.parse(timeTag.getAttribute('datetime'));
        if (! isNaN(date)) {
            timeTag.innerText = dateFormat.format(date);
        }
    });
};

function deleteUpload(id) {
    fetch(`/uploads/${id}`, {
        method: 'DELETE'
    }).then(() => {
        document.getElementById(`upload_${id}`).remove();
        if (document.querySelector('#upload_table tbody').children.length === 0) {
            document.getElementById(`upload_table`).remove();
        }
    });
}

function deleteSample(id) {
    fetch(`/samples/${id}`, {
        method: 'DELETE'
    }).then(() => {
        document.getElementById(`sample_${id}`).remove();
    });
}

function onChangedActive(id, value) {
    fetch(`/simplevariant/${id}/active`, {
        method: value ? 'PUT' : 'DELETE',
        body: value
    }).then(() => {
        document.getElementById(`modified_${id}`).innerText = dateFormat.format(new Date());
    });
}

function onChangedInterpretation(id, value) {
    fetch(`/simplevariant/${id}/interpretation`, {
        method: 'PUT',
        body: value
    }).then(() => {
        document.getElementById(`modified_${id}`).innerText = dateFormat.format(new Date());
    });
}

function onCodeInputChange() {
    let letter = document.getElementById('code-letter').value;
    let year = document.getElementById('code-year').value;
    let number = document.getElementById('code-number').value;
    document.getElementById('code').value = `${letter}/20${year}/${number}`;
    console.log(`${letter}/20${year}/${number}`);
}