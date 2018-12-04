$ErrorActionPreference = "Stop"
Import-Module BitsTransfer
if(!(Test-Path -Path "bin\server\models\ssd_inception_v2_coco_2017_11_17\")){
  new-item -Name bin\server\models -Force -ItemType directory
  Start-BitsTransfer -Source "http://download.tensorflow.org/models/object_detection/ssd_inception_v2_coco_2017_11_17.tar.gz" -Destination "ssd_inception_v2_coco_2017_11_17.tar.gz"
  .\tools\7za.exe e "ssd_inception_v2_coco_2017_11_17.tar.gz"
  .\tools\7za.exe x "ssd_inception_v2_coco_2017_11_17.tar" -obin\server\models\ -r
  Remove-Item -Path ssd_inception_v2_coco_2017_11_17.tar
  Remove-Item -Path ssd_inception_v2_coco_2017_11_17.tar.gz
}
if(!(Test-Path "bin\server\labels\mscoco_label_map.pbtxt" -PathType Leaf)){
  new-item -Name bin\server\labels -Force -ItemType directory
  Start-BitsTransfer -Source "https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/mscoco_label_map.pbtxt" -Destination  "bin\server\labels\mscoco_label_map.pbtxt"
}
if(!(Test-Path "bin\server\labels\oid_bbox_trainable_label_map.pbtxt" -PathType Leaf)){
  new-item -Name bin\server\labels -Force -ItemType directory
  Start-BitsTransfer -Source "https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/oid_bbox_trainable_label_map.pbtxt" -Destination  "bin\server\labels\oid_bbox_trainable_label_map.pbtxt"
}