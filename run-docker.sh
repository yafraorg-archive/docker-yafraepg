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

cd /work/wg
mono WebGrab+Plus.exe "$(pwd)"

# do cron
# run egp grabber with mono
# mono WebGrab+Plus.exe "$(pwd)"
echo "done - running EPG grabber"
