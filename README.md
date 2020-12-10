# Speech To Code Plugin

This repo contains an Eclipse plugin to convert Speech to Code using [Google Cloud Speech-to-text API](https://cloud.google.com/speech-to-text).


[![High-Level Design](https://img.shields.io/badge/design-docs-blue)](/Docs/High-Level-Design.pdf)

## Getting Started

1. Clone this repo onto your computer using git.
```
git clone https://github.com/somahota/Speech-Based-Code-Editor-Project.git
```
2. Launch Eclipse and go to `File` -> `Open Projects from File` to browse and load the `Voice2Code` folder from the repository you just cloned.
3. Now that the `Voice2Code` folder is in the eclipse-workspace make sure you have `Eclipse Plugin Development Tools` installed by following the steps below.
    1. Go to `Help` -> `Install New Software`.
    2. Select `The Eclipse Project Updates - http://download.eclipse.org/eclipse/updates/4.17` from the `Work with` dropdown.
    3. Select `Eclipse Plugin Development Tools` from under `Name`.
    4. Click Next. Once the installation is done, Eclipse will ask to restart, please do so.
4. Make sure you have `GOOGLE_APPLICATION_CREDENTIALS` environment variable set up in the `Run Configurations` under Run in Eclipse. (follow [this](#setting-up-gcloud-key) guide to get GCloud key setup)
5. To run the plugin, right-click the project (make sure that is `Voice2Code`) and click `Run As` -> `Eclipse Application`.
4. To view the plugin go to `Window`->`Show View`->`Other`->`Voice 2 Code`-> `Voice 2 Code`.
6. This will open up a new Eclipse window with the GUI with Press to Speak.
![GUI](/images/gui.png)
7. Click `Create a project` to create a standard project, when the GUI is pressed, speak out to view text being coded inside your project.
8. You will find results like below printed on the console. The confidence score is displayed for the final results (Final: true) after the sentence is completed. 
```
V2T Start
Mic and CloudStream initialized
Transcript: I'm speaking
Confidence: 0.95554876
Final: true
```

## Directory Structure
```bash
├── Voice2Code
│   └── src
│       ├── edu.brown.cs
│       │      └── plugin
│       │           ├── Voice2Code.java (Eclipse plugin's source)
│       │           └── editor (Helper methods for manipulating the editor)
|       ├── text2code (Converts text to code)
│       ├── voice2text (Interacts with Google Speech-to-text API)
│       └── test (Unit tests)
```




## Setting up GCloud Key

1. Sign in on the [Google Cloud](https://cloud.google.com/) with your Google account. 
2. Follow [these steps](https://cloud.google.com/speech-to-text/docs/quickstart-gcloud#before_you_begin) to set up a new project with Speech-to-Text API.
3. Lastly, follow [these steps](https://cloud.google.com/speech-to-text/docs/libraries#setting_up_authentication) to create and download the Service Account key. 
4. This key is downloaded as a JSON file. Please make sure Eclipse has an environment variable set up like below. (Note: `my-key` is a placeholder)
```
variable: GOOGLE_APPLICATION_CREDENTIALS
value: /home/user/Downloads/<my-key>.json
```

## Troubleshooting

May things can go wrong with Eclipse.

1. If you don't see `Voice 2 Code` in the View menu, make sure you have `.project` file inside Eclipse workspace.
2. If the microphone does not work, due to getting shared with other applications, launch Eclipse from the terminal using this command on MAC OS.
```
/Applications/Eclipse.app/Contents/MacOS/eclipse &!
```