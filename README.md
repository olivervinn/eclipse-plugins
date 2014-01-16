Overview
===============

A collection of plugins to make working with Eclipse and the CDT me friendly.


Utils
==

**View Hopper**

Store your current open views <kbd>CTRL+SHIFT+F5</kbd> and restore them via the menu or keyboard <kbd>CTRL+F5</kbd>. For good measure you can also undo your restore too.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/view_hopper.png)


**Font Size changer**

Yet another font size changer. Using <kbd>CTRL+SHIFT++</kbd> to increase and <kbd>CTRL+SHIFT+-</kbd> to decrease font size. While <kbd>CTRL+0</kbd> is available to reset to a default font size quickly. The none standard key combination is chosen so not to conflict with the default keys used by CDT for source navigation.


*Preferences > General > Appearance > Font Size*


![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/tools.png)


**Remove all problem markers**

With one click remove all problem markers from all workspace projects.

**Hide Context Menu Items**

The Editor context menu is seriously over populated so using activities take back control. Three tier hiding: basic copy-paste... then the build commands that a non managed project dont use. Finally to tame plugins like Easy shell and PyDev.

Hiding can be disabled entirely via the preferneces page *Preferences > General > Context Menu*


**Refresh all project resources**

With one click refresh all workspace project resources


**Other**

* Remove trailing whitespace (Select a container and a dialog will open to apply to multiple resources at once.)
* Format the selected resource(s)


CDT Additions
==

The CDT plugin surfaces some control over the indexer and across projects. 

* Remove infrequently used context menu items
* Associate (cross reference) C projects
* Rebuild all project indexes
* Stop the indexer
* Cross project configuration selector


![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/environment.png)


**Associate (cross reference) C projects**

With <kbd>CTRL</kbd>ALT<kbd>L</kbd> or the menu item under environment you can associate all the C projects in the workspace (adds a dynamic reference that is stored in the workspace and not the project file). Use <kbd>CTRL</kbd>ALT<kbd>L</kbd> to disassociate.

This association is done with dynamic references so stored in the workspace. So you wont see anything in the references property page. This stops CM systems picking up changes to the .cproject files that standard references will modify.


**Cross project configuration selector**

If you have an output that captures the C defines and a way to identify the 
different configurations (be it 1) you may be able to use this to set the c
defines in all projects.

See the *Preferences > C/C++ > Environment*

Tested with a folder hierarchy indicating a configuration, under which files
can be found to parse out the C defines.

On selecting the configuration in addition to setting the defines the plugin
will use resource filters to remove the other unused configuration resources from view.

Further if you have a file that lists additional folders not used by the configuration
it can be parsed and those will be removed also.

Finally the C indexer is rebuilt on all projects.

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/environment_indicator.png)


**Rebuild all project indexes**

Think the indexer could do with checking again. Do it everywhere you like one click - this rebuilds indexes for all open projects.

**Stop the indexer**

Often indexer starts when you realise you need to change something first. Now interrupt the indexer rather than waiting for it to complete.

