#!/bin/bash
set -e
if [ ! -d "bin/server/models/ssd_inception_v2_coco_2017_11_17/" ]; then
  mkdir -p bin/server/models/
  curl -L http://download.tensorflow.org/models/object_detection/ssd_inception_v2_coco_2017_11_17.tar.gz | tar -xz -C bin/server/models/
fi
if [ ! -f "bin/server/labels/mscoco_label_map.pbtxt" ]; then
  mkdir -p bin/server/labels/
  curl -L -o bin/server/labels/mscoco_label_map.pbtxt "https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/mscoco_label_map.pbtxt"
  curl -L -o bin/server/labels/oid_bbox_trainable_label_map.pbtxt "https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/oid_bbox_trainable_label_map.pbtxt"
fi