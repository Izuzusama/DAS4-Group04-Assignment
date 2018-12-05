<style>
  code{
    color: #f92672 !important;
    border: 1px solid hsla(0, 0%, 89%, 1);
    background-color: hsla(0, 0%, 98%, 1);
    font-size: .875rem;
    border-radius: 2px;
    display: inline-block;
    padding: 3px 7px;
  }
</style>

# Distributed Processing Program

## Prerequisite

1. java >1.8
2. Make sure that java and javac is in path
3. Tensor flow (if want to run with simulation off)

## Build

1. Run `build.bat`

## Install Tensor flow models

One of the service provided is to run Tensor Flow Object Detection. This will require the tensor flow pre-trained models and labels. To download them run `download_models.ps1`. To save time, you can also turn on simulation so that the it does not actually run the tensor flow command but return a hard coded value. See configuration below to find out how to turn it on.

## Configuration

If a configuration file is not found. First startup will create default config file.

### Tracker

File: tracker.config.properties
| Config            | Definition       | Default |
| ----------------- | ---------------- | ------- |
| rmi_registry_port | Tracker RMI Port | 1099    |

### Server

File: server.config.properties
| Config                    | Definition                                                                                                                   | Default                                                      |
| ------------------------- | ---------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------ |
| image_analytics_simulate  | Turn simulate on or off see [Install Tensor flow models](#install-Tensor-flow-models)                                        | 0                                                            |
| services                  | Services that this server will provide. Split by comma (,)                                                                   | VideoAnalytics,VideoSplit,ImageAnalytics,ImageAnalyticsGraph |
| image_analytics_label     | The label file for tensor flow. Not needed if simulation is 1. Or if the server is not providing ImageAnalyticsService.      | labels/mscoco_label_map.pbtxt                                |
| image_analytics_model_dir | The model directory for tensor flow. Not needed if simulation is 1. Or if the server is not providing ImageAnalyticsService. | models/ssd_inception_v2_coco_2017_11_17/saved_model          |
| tracker                   | The tracker server                                                                                                           | localhost\:1099                                              |
| rmi_registry_port         | The server rmi registry port                                                                                                 | 1000                                                         |
| rmi_registry_host         | The server rmi registry host/IP                                                                                              | localhost                                                    |

### Client

File: server.config.properties
| Config            | Definition                                           | Default        |
| ----------------- | ---------------------------------------------------- | -------------- |
| rmi_registry_port | Callback RMI Registry Port                           | 1088           |
| rmi_registry_host | Callback RMI Registry Host/IP                        | localhost      |
| trackers          | Tracker servers to find servers. Comma seperated (,) | localhost:1099 |

## Run

### Run Tracker

Go to `bin\tracker` and run `java -jar tracker.jar` in command line / terminal

Go to `bin\server` and run `java -cp server.jar;xchart-3.5.2.jar Server` for windows in command line. Run `java -cp server.jar:xchart-3.5.2.jar Server` for linux in terminal

Go to `bin\client` and run `java -jar client.jar` in command line / terminal
