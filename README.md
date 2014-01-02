Overview
===============

A collection of plugins for Eclipse and CDT. 
* Font Size changer via keyboard 
* UI menu cleanup
* Across project environment control. 

Font Size changer 
==

Using <kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>+</kbd> to increase and <kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>-</kbd> to decrease font size whilst <kbd>CTRL</kbd>+<kbd>0</kbd> is available to reset to 12pt a default font size quickly. The none standard latter key combination is chosen so as not to conflict with the default keys used by CDT for source navigation.

UI Utils
==

Most plugins seem to pepper the menus and popups making them unwieldy and confused looking. UI Utils uses the activities framework to hide them from menus that I think they serve no purpose letting me focus on the important tools.


Environment
==

Allows you to easily link and unlink C projects using dynamic references (those stored in the workspace rather than with the project) allowing the indexer to cross the project boundary.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.update/images/environment.png)
