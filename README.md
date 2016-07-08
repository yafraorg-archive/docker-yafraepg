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

## Error
```
Error
[  Debug ]    skipped : show that happened before 'today' @ 06/07/2016 23:00
[  Info  ] 2.56 sec/update
[  Debug ] 
[  Debug ] 3784 shows in 35 channels
[  Debug ] 0 updated shows
[  Debug ] 3784 new shows added
[  Info  ] 
[  Info  ] 
[        ] Job finished at 07/07/2016 21:17:25 done in 2h 51m 9s
[  Debug ] statistics upload error: The remote server returned an error: (500) Internal Server Error.
bash-4.3# 
```


