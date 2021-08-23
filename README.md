# Blokkok Modules
Essential modules used for blokkok to function properly as an Android IDE, this is where you want to edit it's IDE features, like project editing, code editor, layout editor, etc.

## Modules Inside
### Essentials
This essentials module is used to easily do stuff without having to write them yourself, just call a single communication function. Note: Might migrate this into using a stub instead rather than the communication API because it sucks.

<!-- TODO: WRITE SOME MORE -->

### Project Manager
The default blokkok project manager that manages projects by saving them in a folder inside the `$dataDir/projects` directory, each projects / folders are identified by a randomly-generated 16-wide `[a-zA-Z1-0]` String ID.

But note that this project manager cannot edit the inside of a project, that will be done by a project editor module. To make a project editor module by yourself, you will need to include the flag `project-manager-impl` in your module's flag list and [here](https://github.com/Blokkok/blokkok-modules/blob/main/project-manager/module/src/main/java/com/blokkok/mod/project/manager/Utils.kt#L14-L34) you can check its required communications. (Note: might migrate this method into using a stub instead too)

Inside a project's folder, there will be a file called `project-metadata.json` that contains the information about the project name, project configuration, and the project editor implementer (so that it cannot clash with other editor implementations).

<!-- TODO: WRITE SOME MORE -->

### Android App Project Editor (AAPEM)
This module implements Blokkok Project Manager's Project Editor specification. This project editor basically edits an android app project. This module needs 3 modules to function properly: code editor, layout editor, and apk builder modules.

<!-- TODO: WRITE SOME MORE -->

### Simple Code Editor
A simple code editor module that implements AAPEM's Code Editor module interface defined in aapem's stub

<!-- TODO: WRITE SOME MORE -->

### Simple Layout Editor
A simple layout editor module that implements AAPEM's Layout Editor module interface defined in aapem's stub

<!-- TODO: WRITE SOME MORE -->

### Simple Apk Builder
A simple apk builder module that implements AAPEM's Apk Builder module interface defined in aapem's stub

<!-- TODO: WRITE SOME MORE -->
