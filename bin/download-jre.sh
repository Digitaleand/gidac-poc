# 
# The MIT License (MIT)
#
# Copyright (c) 2022 Mehdi Lefebvre
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

ZULU_BASE_NAME="zulu19.30.11-ca-jdk19.0.1-linux_x64"
ZULU_NAME="${ZULU_BASE_NAME}.tar.gz"
ZULU_URI="https://cdn.azul.com/zulu/bin/${ZULU_NAME}"

echo "Downloading jre..."

pushd .
cd /tmp
wget "${ZULU_URI}"
tar -zxf "${ZULU_NAME}"
mv "${ZULU_BASE_NAME}" jre
rm "${ZULU_NAME}"
popd

mv /tmp/jre .
