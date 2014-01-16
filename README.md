Overview
===============

A collection of plugins to make working with Eclipse and the CDT me friendly.


Utils
==

**View Hopper**

Store your current open views <kbd>CTRL+SHIFT+F5</kbd> and restore them via the menu or keyboard <kbd>CTRL+F5</kbd>. For good measure you can also undo your restore too.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/view_hopper.png)


**Font Size changer**

Yet another font size changer. Using <kbd>CTRL+SHIFT+=</kbd> to increase and <kbd>CTRL+SHIFT+-</kbd> to decrease font size. While <kbd>CTRL+0</kbd> is available to reset to a default font size quickly. The none standard key combination is chosen so not to conflict with the default keys used by CDT for source navigation.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/tools.png)

*Preferences > General > Appearance > Font Size*

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/font_size.png)



**Remove all problem markers**

With one click remove all problem markers from all workspace projects.

**Hide Context Menu Items**

The Editor context menu is seriously over populated so using activities take back control. Three tier hiding: basic copy-paste... then the build commands that a non managed project dont use. Finally to tame plugins like Easy shell and PyDev. Each group can be disabled via the preferneces page.

*Preferences > General > Context Menu*

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/context_menu.png)


**Refresh all project resources**

With one click refresh all workspace project resources


**Other**

* Remove trailing whitespace (Select a container and a dialog will open to apply to multiple resources at once.)
* Format the selected resource(s)
* Toggle Whitespace (useful for Eclipse 4.x where this disappears from the toolbar)


CDT Additions
==

The CDT plugin surfaces some control over the indexer and across projects. 

* Associate (cross reference) C projects
* Cross project configuration selector
* Rebuild all project indexes
* Stop the indexer


![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/environment.png)


**Associate (cross reference) C projects**

With <kbd>CTRL+ALT+L</kbd> or the menu item under environment you can associate all the C projects in the workspace (adds a dynamic reference that is stored in the workspace and not the project file). Use <kbd>CTRL+ALT+U</kbd> to disassociate.

This association is done with dynamic references so stored in the workspace. So you wont see anything in the references property page. This stops CM systems picking up changes to the .cproject files that standard references will modify.

It is possible to exclude projects by name endings via the preferences page

*Preferences > C/C++ > Environment*

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/preferences.png)


**Cross project configuration selector**

If you have an output that captures the C defines and a way to identify the different configurations (be it 1) you may be able to use this to set the c defines in all projects.

Using the preferences page to define epressions to identify a configuration and select files with C define and folder information it is possible to set the project definitions and get an accurate index.

Select the configuration from the dialog selection. The files will be parsed and the definitions set. Hides other configurations using resource filters and then kicks off the indexer. 

If you have a file that lists additional folders not used by the configuration it shall also be parsed and those will be hidden too prior to rebuilding the index.

When active the project view will highlight with the green indicator seen below. Click the icon to remove all resource filters and definitions.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/environment_indicator.png)


**Rebuild all project indexes**

Rebuild the index on all projects with one click.

**Stop the indexer**

Often the indexer starts when you realise you need to change something first. Now you can stop it!

