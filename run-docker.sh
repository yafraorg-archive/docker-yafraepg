#!/bin/sh
#-------------------------------------------------------------------------------
#  Copyright 2015 yafra.org
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#-------------------------------------------------------------------------------
#
# docker run script
#

echo "starting - run webgrab plus now"

cd /work/phpserver
php7 -S 127.0.0.1:80 &
cd /work

# run egp grabber with mono
# mono WebGrab+Plus.exe "$(pwd)"
cd /work/wg
cp /opt/epg/guideorig.xml guide.xml
./run.sh
#mono WebGrab+Plus.exe "$(pwd)"

# copy guide to ftp server
cp guide.xml /opt/epg/guideorig.xml
dos2unix guide.xml
cp guide.xml /work/epgdata.xml
cp guide.xml /opt/epg/epgdata.xml
cd /work
#curl -T epgdata.xml ftp://username:password@ftp.server.com/remotedir/

echo "done - running EPG grabber"
