# docker-yafraepg

## EPG grabber as a docker
Dockerfile - Yafra TV EPG grabber. A docker which hosts a XMLTV EPG grabber, can be used to create/update an xmltv file
on a regular basis. It uses http://www.webgrabplus.com/

## XMLTV java plugin for SageTV V9+
A plugin for SageTV to read this xmltv file genrated from Webgrabplus is available
on github https://github.com/yafraorg/xmltvplugin.

## Run the docker
Mount with "-v /host/dir:/container/dir" a local filesystem to the container so it is easy to access the guide data

```bash
docker pull yafraorg/docker-yafraepg
docker run -t -i --rm -v /tmp/epg:/opt/epg --name yafraepg yafraorg/docker-yafraepg
```



