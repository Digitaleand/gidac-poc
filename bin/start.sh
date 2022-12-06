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
if [ ! -z "GIDAC_HOME" ]; then
  export GIDAC_HOME=$(pwd)
fi

echo "GIDAC_HOME is set: ${GIDAC_HOME}"

if [! -d "jre" ]; then
  echo "Jre found!"
else
  echo "Jre not found!"
  source "${GIDAC_HOME}"/bin/download-jre.sh
fi

echo "Adjusting JAVA_HOME..."
export JAVA_HOME="$(pwd)/jre"
echo "Adjusting JAVA_HOME... Done!"
echo "Starting the application..."
java -jar gidac-presentation/target/gidac-presentation-0.0.1-SNAPSHOT.jar

unset GIDAC_HOME