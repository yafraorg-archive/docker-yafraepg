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

# source is yafra ubuntu
FROM yafraorg/docker-yafrabase

MAINTAINER Martin Weber <info@yafra.org>

COPY run-docker.sh /work

# Install mono packages
RUN apt-get update && \
  apt-get install -yq mono-complete unrar-free  && \
  apt-get install -yq libpq5 mysql-client-5.5 libmysql-cil-dev symlinks && \
  rm -rf /var/lib/apt/lists/*
  
RUN cd /work && \
  wget http://www.webgrabplus.com/sites/default/files/download/SW/V1.1.1/WebGrabPlusV1.1.1LINUX.rar && \
  unrar WebGrabPlusV1.1.1LINUX.rar

EXPOSE 80
CMD ["/work/run-docker.sh"]
