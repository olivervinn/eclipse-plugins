Overview
===============

A collection of plugins for Eclipse and CDT. 
* Font Size changer
* Remove all problem markers
* Remove infrequently used context menu items (CDT)
* Associate (cross reference) C projects (CDT)
* Rebuild all project indexes (CDT)
* Stop the indexer (CDT)
* Refresh all project resources
* Cross project configuration selector (CDT)


![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.utils/images/tools.png)

![](https://raw.github.com/ovinn/eclipse-plugins/master/com.vinn.feature.cdt/images/environment.png)




Font Size changer 
==
Using <kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>+</kbd> to increase and <kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>-</kbd> to decrease font size whilst <kbd>CTRL</kbd>+<kbd>0</kbd> is available to reset to 12pt a default font size quickly. The none standard key combination is chosen so not to conflict with the default keys used by CDT for source navigation.

Also available under the tools menu.

Remove all problem markers
==
Remove all problem markers from the workspace with one menu click.

Infrequently used context menu items (CDT)
==
Using activities this plugin will remove entries that grow the context menu 
unnecessarily (cut/paste), (debug/run) from file...


Associate (cross reference) C projects
==
With <kbd>CTRL</kbd>ALT<kbd>L</kbd> or the menu item under environment you can associate all the C projects in the workspace (adds a dynamic reference that is stored in the workspace and not the project file). Use <kbd>CTRL</kbd>ALT<kbd>L</kbd> to disassociate.

Rebuild all project indexes
==
Think the indexer could do with checking again. Do it everywhere with one click. This rebuilds the index of all C projects.

Stop the indexer
==
Often indexer starts when you realise you need to change something first. Now interrupt the indexer rather than waiting for it to complete.

Refresh all project resources
==
Newer versions of Eclipse have better change detection but for others and sometimes still you want to refresh. Now there is more no need to go to each project folder and hit F5. Here one click refresh those projects!


Cross project configuration selector
==
If you have an output that captures the C defines and a way to identify the 
different configurations (be it 1) you may be able to use this to set the c
defines in all projects.

See the preferences dialog for configuration (C/C++ > Environment).

Tested with a folder hierarchy indicating a configuration, under which files
can be found to parse out the C defines.

On selecting the configuration in addition to setting the defines the plugin
will use resource filters to remove the other unused configuration resources from view.

Further if you have a file that lists additional folders not used by the configuration
it can be parsed and those will be removed also.

Finally the C indexer is rebuilt on all projects.



