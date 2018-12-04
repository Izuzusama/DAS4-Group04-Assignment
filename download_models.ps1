function Expand-Tar($tarFile, $dest) {

    if (-not (Get-Command Expand-7Zip -ErrorAction Ignore)) {
        Install-Package -Scope CurrentUser -Force 7Zip4PowerShell > $null
    }

    Expand-7Zip $tarFile $dest
}

new-item -Name bin\server\models -ItemType directory

$client = new-object System.Net.WebClient
$client.DownloadFile("http://download.tensorflow.org/models/object_detection/ssd_inception_v2_coco_2017_11_17.tar.gz","ssd_inception_v2_coco_2017_11_17.tar.gz")
Expand-Tar ssd_inception_v2_coco_2017_11_17.tar.gz bin\server\models
Remove-Item -Path ssd_inception_v2_coco_2017_11_17.tar.gz
new-item -Name bin\server\labels -ItemType directory
$client.DownloadFile("https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/mscoco_label_map.pbtxt", "bin\server\labels\mscoco_label_map.pbtxt")
$client.DownloadFile("https://raw.githubusercontent.com/tensorflow/models/865c14c1209cb9ae188b2a1b5f0883c72e050d4c/research/object_detection/data/oid_bbox_trainable_label_map.pbtxt", "bin\server\labels\oid_bbox_trainable_label_map.pbtxt") 