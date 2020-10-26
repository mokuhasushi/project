## Project setup

### Virtual Machine Setup

To use the provided Virtual Machine, you should install VMWare Workstation 16 Player for your Operating System:

https://my.vmware.com/en/web/vmware/downloads/info/slug/desktop_end_user_computing/vmware_workstation_player/16_0

It is also advisable to install VMWare Tools to allow file transfer between your host machine and the guest machine (i.e. the Virtual Machine we provide to you) via a shared folder:

https://geek-university.com/vmware-player/enable-a-shared-folder-for-a-virtual-machine/

A shared folder may be within `/mnt/hgfs`, or a similar directory within the Virtual Machine, once you have setup this functionality by following the above instructions.

The Operating System is the `ubuntu-20.04.1-desktop-amd64.iso` Ubuntu iso from:

https://ubuntu.com/download/desktop/thank-you?version=20.04.1&architecture=amd64

However, you should not need to download the Operating System ISO - downloading the contents of the following link and opening it in VMWare Workstation Player 16 should work:

https://unsw-my.sharepoint.com/:f:/g/personal/z5075269_ad_unsw_edu_au/Ek7X2VjGp1VDvHKlG7cvZToBXQVI9Krh2flmKDxMG67ZsQ


The Virtual Machine we are providing has been tested and includes all necessary software to immediately run the project in a Ubuntu 20.04 Virtual Machine, including:

* VSCode
* VSCode `Java Extension Pack`, `Coderunner`, `VSCode Map Preview` and `Draw.io Integration` extensions
* Java OpenJDK 11 (`default-jre` and `default-jdk` on Ubuntu `apt`)
* ArcGIS Runtime SDK for Java
* JavaFX JDK for Java 11
* JUnit 5
* Gradle 5.4.1

Also note that the repository `lib` folder includes the JAR files for `GeoJson POJOs for Jackson`, `Jackson`, and `JSON-Java`. `GeoJson POJOs for Jackson` provides Java classes representing GeoJSON data, allowing easier extraction of GeoJSON data, whilst `JSON-Java` provides similar functionality for standard JSON.

`GeoJson POJOs for Jackson`: https://github.com/opendatalab-de/geojson-jackson

`JSON-Java`: https://www.baeldung.com/java-org-json

The project setup includes several symbolic links - to the ArcGIS, JavaFX, and JUnit libraries.

The login password of the provided virtual machine is the same as the username: `comp2511-student`

To play the starter game, clone the repository into a directory in the Virtual Machine, open the root directory of the repository in VSCode, and click the *"Run"* link above the *main* method of **GloriaRomanusLauncher.java**

### Setting up Libraries on your Machine (not using Virtual Machine)

You can setup the project yourself on your computer, however support from teaching staff for this may be more limited by a lack of familiarity with your computer's setup. To install this on your own machine, you will need to perform additional setup steps to install the following components (you should already have `VSCode`, `Java`, and the `Java Extension Pack` VSCode extension fully installed):

* VSCode
* `Java Extension Pack`, `Coderunner`, `VSCode Map Preview` and `Draw.io Integration` VSCode extensions
* Java OpenJDK 11 (`default-jre` and `default-jdk` on Ubuntu `apt`)
* ArcGIS Runtime SDK for Java
* JavaFX JDK for Java 11
* JUnit 5
* Gradle 5.4.1

We recommend installing the `VSCode Map Preview` extension in VSCode to view the contents of GeoJSON files. This is not necessary to complete the project, however. You may also wish to install the `Draw.io Integration` extension into VSCode to create UML diagrams in VSCode: https://github.com/hediet/vscode-drawio/

### Installing JavaFX

Delete the *symlink_javafx* symbolic link, then download and unzip the latest version of the JavaFX JDK for Java 11 for your Operating System (taking into account if you have a 64 or 32 bit machine), and transfer the contents of the *lib* folder inside the JDK download into the *lib* folder on your cloned repository. You will also need to change the *launch.json* file to refer to **"./lib"** instead of **./lib/symlink_javafx** in the *"vmArgs"* configuration (note these modifications were tested on Windows 10).

You may also need to copy the contents of the *bin* folder in the unzipped JavaFX JDK download into a *bin* folder under the root directory of your cloned repository (e.g. for Windows).

The following version of the JavaFX JDK is recommended if you choose to run it on your computer, since it is the same version as on the CSE machine and the Virtual Machine:

https://gluonhq.com/products/javafx/

Note that if you deviate from this precise directory structure, you may need to modify the VSCode configuration in *.vscode/launch.json* to be able to run the game in VSCode.

### Installing ArcGIS Runtime SDK for Java

Follow these instructions for your operating System:

https://developers.arcgis.com/java/latest/guide/get-the-sdk.htm

Downloads: https://developers.arcgis.com/downloads/apis-and-sdks/

Note you will need to make an ArcGIS account to download the SDK.

After downloading the zip file, you must transfer the `jniLibs` and `resources` folders into the root directory of your cloned repository (replacing the symbolic links with the same names). You should delete the symbolic link `lib/symlink_arcgis` and remove the text *./lib/symlink_arcgis* from the file *.vscode/launch.json*. You must also transfer the contents of the `lib` folder into the `lib` folder of your cloned repository. 

The documentation for ArcGIS is at: https://developers.arcgis.com/java/latest/api-reference/reference/index.html

Note the system requirements for ArcGIS (for if you want to install on your own computer): https://developers.arcgis.com/java/latest/guide/system-requirements.htm

Notably, ArcGIS only supports 64 bit machines.

### Installing JUnit 5

You will need to download the JUnit 5 Platform Console Standalone JAR, from the Maven repository (or another repository), and place it into the lib directory, and delete the symbolic link `symlink_junit5`. The link to the Maven repository for this is (download by clicking the link "jar (2.1 MB)" next to "Files"):

https://mvnrepository.com/artifact/org.junit.platform/junit-platform-console-standalone/1.7.0-M1

Note that by installing the `Java Extension Pack` extension in VSCode as required, you will have installed the `Java Test Runner` extension to run tests with clickable "Run Test" links in the files.

### Installing Gradle 5.4.1

Download the zip file from (download should start automatically): https://gradle.org/next-steps/?version=5.4.1&format=bin

You should follow the installation instructions provided:

https://gradle.org/install/#manually

For Linux users, note that you may have to edit the `~/.bashrc` file to permanently change the PATH variable by appending the line:

`export PATH=$PATH:/opt/gradle/gradle-5.4.1/bin`

Note that Gradle 5.4.1 is the same version as on CSE machines. It has been chosen so that common syntax can be used for the `test.gradle` file to Jacoco coverage testing. We will run Gradle 5.4.1 and the provided `test.gradle` script to perform coverage checking of your submission for milestone 2, which will contribute towards your mark for testing - so you should check the coverage checking command works on a CSE machine (command provided below under *Running coverage checking*). Note that Gradle and JUnit tests should be able to run on a CSE machine, whilst the starter code does not, since your JUnit tests shouldn't start an ArcGIS map window.

If the steps in the above instructions worked, you should be able to run the starter code.

**IMPORTANT**: Please do not push the contents of the *lib*, *bin*, *resources*, *jniLibs* or *build* folders to your Gitlab repository. This is very likely to push you over the memory limits for the milestone 2 and 3 submissions.
