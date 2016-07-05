#
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

#
# yafra.org XMLTV EGP grabber docker
#

# source is yafra os
FROM yafraorg/docker-yafrabase

MAINTAINER Martin Weber <info@yafra.org>

# Install mono packages
RUN echo "@testing http://dl-4.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories && \
  apk update && \
  apk upgrade && \
  apk add --update git mono@testing unrar unzip wget curl && \
  rm -rf /var/cache/apk/*

WORKDIR /work

# setup start script and create directory to store the egpdata file
COPY run-docker.sh /work/run-docker.sh
RUN chmod 755 /work/run-docker.sh
RUN mkdir -p /opt/epg

#  wget -q http://www.webgrabplus.com/sites/default/files/download/sw/V1.1.1/upgrade/patchexe_55.zip && \

# install web grabber
RUN cd /work && \
  git clone https://github.com/yafraorg/docker-yafraepg.git && \
  wget -q http://www.webgrabplus.com/sites/default/files/download/SW/V1.1.1/WebGrabPlusV1.1.1LINUX.rar && \
  wget -q http://www.webgrabplus.com/sites/default/files/patchexe_prebuild.zip && \
  unrar x WebGrabPlusV1.1.1LINUX.rar && \
  mv WebGrab+PlusV1.1.1LINUX/ wgplus && \
  mv patchexe_prebuild.zip wgplus/ && \
  cd wgplus && \
  unzip -D patchexe_prebuild.zip && \
  mkdir ../wg && \
  cp WebGrab+Plus.exe ../wg && \
  cd ../docker-yafraepg/epgconfig && \
  cp -r * /work/wg/. && \
  cd /work && \
  rm -rf wgplus/ && \
  rm -rf repos/ && \
  rm WebGrabPlusV1.1.1LINUX.rar

# expose files via http server
#EXPOSE 8085

#CMD ["/work/run-docker.sh"]
ENTRYPOINT ["/work/run-docker.sh"]