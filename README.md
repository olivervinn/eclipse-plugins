Overview
===============

A collection of plugins to make working with Eclipse and the CDT friendly. Implemented by me in my time for me. Maybe useful to you.


Utils
==

**View Hopper**

Store your current open views <kbd>CTRL+SHIFT+F5</kbd> and restore them via the menu or keyboard <kbd>CTRL+F5</kbd>. For good measure you can also undo your restore too.

![](https://raw.github.com/olivervinn/eclipse-plugins/master/com.vinn.feature.utils/images/view_hopper.png)


**Font Size changer**

Yet another font size changer. Using <kbd>CTRL+SHIFT+=</kbd> to increase and <kbd>CTRL+SHIFT+-</kbd> to decrease font size. While <kbd>CTRL+0</kbd> is available to reset to a default font size quickly. The none standard key combination is chosen so not to conflict with the default keys used by CDT for source navigation.

![](https://raw.github.com/olivervinn/eclipse-plugins/master/com.vinn.feature.utils/images/tools.png)

*Preferences > General > Appearance > Font Size*

![](https://raw.github.com/olivervinn/eclipse-plugins/master/com.vinn.feature.utils/images/font_size.png)



**Remove Problem markers**

With one click remove all problem markers from all workspace projects.

**Hide Context Menu Items**

The Editor context menu is seriously over populated so activities are used to take back control. Three tier hiding: basic copy-paste... then the build commands that a non managed project doesn't use. Finally to tame plugins like Easy shell and PyDev. Each group can be disabled via the preferneces page.

*Preferences > General > Context Menu*

![](https://raw.github.com/olivervinn/eclipse-plugins/master/com.vinn.feature.utils/images/context_menu.png)


**Refresh all project resources**

With one click refresh all workspace project resources


**Other**

* Remove trailing whitespace (Select a container and a dialog will open to apply to multiple resources at once.)
* Toggle Whitespace (useful for Eclipse 4.x where this sometimes disappears from the toolbar)


CDT Specific
==

The CDT plugin surfaces some control over the indexer and across projects.

* Associate (cross reference) C projects
* Rebuild all project indexes
* Stop the indexer


![](https://raw.github.com/olivervinn/eclipse-plugins/master/com.vinn.feature.cdt/images/proj_workspace_menu.png)


**Associate (cross reference) C projects**

With <kbd>CTRL+ALT+L</kbd> or the project toolbar you can associate all the C projects in the workspace (adds a dynamic reference that is stored in the workspace and not the project file). Use <kbd>CTRL+ALT+U</kbd> to disassociate.

This association is done using dynamic references as such the relationship is stored in the workspace and doesn't change .project/.cproject file. Useful for when you are using git or similar and you don't want to continuously be told about a project file change.

It is possible to exclude projects by name endings via the preferences page

*Preferences > C/C++ > Workspace*


**Rebuild all project indexes**

Rebuild the index on all projects with one click.

**Stop the indexer**

Often the indexer starts when you realise you need to change something first. Now you can stop it!
