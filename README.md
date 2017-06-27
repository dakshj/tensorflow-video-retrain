# TensorFlow Video Retrain

A Kotlin script that
1. Converts MP4 videos into JPEG images using [`ffmpeg`](https://ffmpeg.org/)
1. Retrains Google's Inception Model
1. Optimizes the model for Android
1. Quantizes the model
1. Zips the model into a GZIP



## Requirements
This script requires the following to be installed and usable
* Python
* TensorFlow

(Please ensure that the executables are accessible via command line.)



## Required Directory Structure
```
<Script Working Directory>
│
└───Training Data
│   │
│   └───<Label 1>
│   │   └───Video 1.mp4
│   │   └───...
│   │   └───Video N.mp4
│   │
...
│   │
│   └───<Label N>
│       └───Video 1.mp4
│       └───...
│       └───Video N.mp4
```



## Execution
```
java -jar [1] [2] [3] [4]
[1]: Path to the script's JAR file
[2]: Path to the script's working directory
[3]: Number of frames per second to be extracted from the MP4 video
[4]: `true` to delete the MP4 files after conversion to JPEG images, `false` otherwise
```
**Example**:

`java -jar "D:\Projects\tensorflow-video-retrain\build\libs\tensorflow-video-retrain-1.0.jar" "D:\TensorFlow\Kotlin Script Working Directory" 5 true`



## Output Model File
The file named `Quantized Graph.pb.gz` is the final retrained and optimized Inception model.



## License

    Copyright 2017 Daksh Jotwani

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
