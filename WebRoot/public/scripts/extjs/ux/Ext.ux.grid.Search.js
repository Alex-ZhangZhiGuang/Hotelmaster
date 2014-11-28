Ext.ns('Ext.ux.grid');

/**
 * @class Ext.ux.grid.Search
 * @extends Ext.util.Observable
 * @param {Object} config configuration object
 * @constructor
 */
Ext.ux.grid.Search = function(config) {
    Ext.apply(this, config);
    Ext.ux.grid.Search.superclass.constructor.call(this);
}; // eo constructor

Ext.extend(Ext.ux.grid.Search, Ext.util.Observable, {
    /**
     * @cfg {String} searchText Text to display on menu button
     */
     searchText:'查找'

    /**
     * @cfg {String} searchTipText Text to display as input tooltip. Set to '' for no tooltip
     */ 
    ,searchTipText:'Type a text to search and press Enter'

    /**
     * @cfg {String} selectAllText Text to display on menu item that selects all fields
     */
    ,selectAllText:'选择所有'

    /**
     * @cfg {String} position Where to display the search controls. Valid values are top and bottom (defaults to bottom)
     * Corresponding toolbar has to exist at least with mimimum configuration tbar:[] for position:top or bbar:[]
     * for position bottom. Plugin does NOT create any toolbar.
     */
    ,position:'bottom'

    /**
     * @cfg {String} iconCls Icon class for menu button (defaults to icon-magnifier)
     */
    ,iconCls:'icon-magnifier'

    /**
     * @cfg {String/Array} checkIndexes Which indexes to check by default. Can be either 'all' for all indexes
     * or array of dataIndex names, e.g. ['persFirstName', 'persLastName']
     */
    ,checkIndexes:'all'

    /**
     * @cfg {Array} disableIndexes Array of index names to disable (not show in the menu), e.g. ['persTitle', 'persTitle2']
     */
    ,disableIndexes:[]

    /**
     * @cfg {String} dateFormat how to format date values. If undefined (the default) 
     * date is formatted as configured in colummn model
     */
    ,dateFormat:undefined

    /**
     * @cfg {Boolean} showSelectAll Select All item is shown in menu if true (defaults to true)
     */
    ,showSelectAll:true

    /**
     * @cfg {String} mode Use 'remote' for remote stores or 'local' for local stores. If mode is local
     * no data requests are sent to server the grid's store is filtered instead (defaults to 'remote')
     */
    ,mode:undefined//'remote'

    /**
     * @cfg {Number} width Width of input field in pixels (defaults to 100)
     */
    ,width:180

    /**
     * @cfg {String} xtype xtype is usually not used to instantiate this plugin but you have a chance to identify it
     */
    ,xtype:'gridsearch'

    /**
     * @cfg {Object} paramNames Params name map (defaults to {fields:'fields', query:'query'}
     */
    ,paramNames: {
         fields:'fields'
        ,query:'query'
    }

    /**
     * @cfg {String} shortcutKey Key to fucus the input field (defaults to r = Sea_r_ch). Empty string disables shortcut
     */
    ,shortcutKey:'r'

    /**
     * @cfg {String} shortcutModifier Modifier for shortcutKey. Valid values: alt, ctrl, shift (defaults to alt)
     */
    ,shortcutModifier:'alt'

    /**
     * @cfg {String} align 'left' or 'right' (defaults to 'left')
     */

    /**
     * @cfg {Number} minLength force user to type this many character before he can make a search
     */

    /**
     * @cfg {Ext.Panel/String} toolbarContainer Panel (or id of the panel) which contains toolbar we want to render
     * search controls to (defaults to this.grid, the grid this plugin is plugged-in into)
     */
    ,extend:false
    ,loaclStore:undefined
    ,searchStore:undefined
    ,localSearchText:'本地查找'
    // {{{
    /**
     * private
     * @param {Ext.grid.GridPanel/Ext.grid.EditorGrid} grid reference to grid this plugin is used for
     */
    ,init:function(grid) {
        this.grid = grid;

        // setup toolbar container if id was given
        if('string' === typeof this.toolbarContainer) {
            this.toolbarContainer = Ext.getCmp(this.toolbarContainer);
        }

        // do our processing after grid render and reconfigure
        grid.onRender = grid.onRender.createSequence(this.onRender, this);
        grid.reconfigure = grid.reconfigure.createSequence(this.reconfigure, this);
    } // eo function init
    // }}}
    // {{{
    /**
     * private add plugin controls to <b>existing</b> toolbar and calls reconfigure
     */
    ,onRender:function() {
        var panel = this.toolbarContainer || this.grid;
        var tb = 'bottom' === this.position ? panel.bottomToolbar : panel.topToolbar;

        // add menu
        this.menu = new Ext.menu.Menu();

        // handle position
        if('right' === this.align) {
            tb.addFill();
        }
        else {
            tb.addSeparator();
        }

        // add menu button
        tb.add({
             text:this.searchText
            ,menu:this.menu
            ,iconCls:this.iconCls
        });

        // add input field (TwinTriggerField in fact)
        this.field = new Ext.form.TwinTriggerField({
             width:this.width
            ,selectOnFocus:undefined === this.selectOnFocus ? true : this.selectOnFocus
            ,trigger1Class:'x-form-clear-trigger'
            ,trigger2Class:'x-form-search-trigger'
            ,onTrigger1Click:this.onTriggerClear.createDelegate(this)
            ,onTrigger2Click:this.onTriggerSearch.createDelegate(this)
            ,minLength:this.minLength
        });

        // install event handlers on input field
        this.field.on('render', function() {
            this.field.el.dom.qtip = this.searchTipText;

            // install key map
            var map = new Ext.KeyMap(this.field.el, [{
                 key:Ext.EventObject.ENTER
                ,scope:this
                ,fn:this.onTriggerSearch
            },{
                 key:Ext.EventObject.ESC
                ,scope:this
                ,fn:this.onTriggerClear
            }]);
            map.stopEvent = true;
        }, this, {single:true});

        tb.add(this.field);

        // reconfigure
        this.reconfigure();

        // keyMap
        if(this.shortcutKey && this.shortcutModifier) {
            var shortcutEl = this.grid.getEl();
            var shortcutCfg = [{
                 key:this.shortcutKey
                ,scope:this
                ,stopEvent:true
                ,fn:function() {
                    this.field.focus();
                }
            }];
            shortcutCfg[0][this.shortcutModifier] = true;
            this.keymap = new Ext.KeyMap(shortcutEl, shortcutCfg);
        }
    } // eo function onRender
    // }}}
    // {{{
    /**
     * private Clear Trigger click handler
     */
    ,onTriggerClear:function() {
        this.field.setValue('');
        this.field.focus();
        this.onTriggerSearch();
    } // eo function onTriggerClear
    // }}}
    // {{{
    /**
     * private Search Trigger click handler (executes the search, local or remote)
     */
    ,onTriggerSearch:function() {
        if(!this.field.isValid()) {
            return;
        }
        var val = this.field.getValue();
        //var store = this.grid.store;
        if(!this.extend){
        	var store = this.grid.store;
        }else{
        	var store = this.localStore;
        }
		if(store==undefined){
        	console.log('No store');
        	return;
        }
        // grid's store filter
		console.log(this.mode);
        if('local' === this.mode) {
        	this.grid.reconfigure(store,this.grid.colModel);
            store.clearFilter();
            if(val) {
                store.filterBy(function(r) {
                    var retval = false;
                    this.menu.items.each(function(item) {
                        if(!item.checked || retval) {
                            return;
                        }
                        var rv = r.get(item.dataIndex);
                        rv = rv instanceof Date ? rv.format(this.dateFormat || r.fields.get(item.dataIndex).dateFormat) : rv;
                        var re = new RegExp(val, 'gi');
                        retval = re.test(rv);
                    }, this);
                    if(retval) {
                        return true;
                    }
                    return retval;
                }, this);
            }
            else {
            }
            this.mode = 'remote';
        }
        // ask server to filter records
        else {
        	store = this.searchStore;
        	if(store==undefined){
        		console.log('No store');
        		return;
        	}
        	this.grid.reconfigure(store,this.grid.colModel);
            // clear start (necessary if we have paging)
            if(store.lastOptions && store.lastOptions.params) {
                store.lastOptions.params[store.paramNames.start] = 0;
                //store.lastOptions.params[store.paramNames.limit] = 4;
            }

            // get fields to search array
            var fields = [];
            this.menu.items.each(function(item) {
                if(item.checked) {
                    fields.push(item.dataIndex);
                }
            });

            // add fields and query to baseParams of store
            delete(store.baseParams[this.paramNames.fields]);
            delete(store.baseParams[this.paramNames.query]);
            if (store.lastOptions && store.lastOptions.params) {
                delete(store.lastOptions.params[this.paramNames.fields]);
                delete(store.lastOptions.params[this.paramNames.query]);
            }
            if(fields.length) {
            	store.baseParams[store.paramNames.start] = 0;
                //store.baseParams[store.paramNames.limit] = 4;
                store.baseParams[this.paramNames.fields] = Ext.encode(fields);
                store.baseParams[this.paramNames.query] = val;
            }
			this.grid.getBottomToolbar.store = store;
            // reload store
            store.reload();
            
        }

    } // eo function onTriggerSearch
    // }}}
    // {{{
    /**
     * @param {Boolean} true to disable search (TwinTriggerField), false to enable
     */
    ,setDisabled:function() {
        this.field.setDisabled.apply(this.field, arguments);
    } // eo function setDisabled
    // }}}
    // {{{
    /**
     * Enable search (TwinTriggerField)
     */
    ,enable:function() {
        this.setDisabled(false);
    } // eo function enable
    // }}}
    // {{{
    /**
     * Enable search (TwinTriggerField)
     */
    ,disable:function() {
        this.setDisabled(true);
    } // eo function disable
    // }}}
    // {{{
    /**
     * private (re)configures the plugin, creates menu items from column model
     */
    ,reconfigure:function() {

        // {{{
        // remove old items
        var menu = this.menu;
        menu.removeAll();

        // add Select All item plus separator
        if(this.showSelectAll) {
            menu.add(new Ext.menu.CheckItem({
                 text:this.selectAllText
                ,checked:!(this.checkIndexes instanceof Array)
                ,hideOnClick:false
                ,handler:function(item) {
                    var checked = ! item.checked;
                    item.parentMenu.items.each(function(i) {
                        if(item !== i && i.setChecked) {
                            i.setChecked(checked);
                        }
                    });
                }
            }),'-');
        }
        if(this.extend){
        	console.log('menu add item');
			menu.add(new Ext.menu.CheckItem({
				 text:this.localSearchText
				,id:'searchItem'
				,checked:false
				,hideOnClick:false
				,handler:function(item){
					var checked = !item.checked;
					if(checked){
						this.mode = 'local';
						console.log(this.mode);
					}
					else{
						this.mode = 'remote';
						console.log(this.mode);
					}
				}.createDelegate(this)
			 }),'-');
        }//end of if(!this.exted)
        // }}}
        // {{{
        // add new items
        var cm = this.grid.colModel;
        Ext.each(cm.config, function(config) {
            var disable = false;
            if(config.header && config.dataIndex) {
                Ext.each(this.disableIndexes, function(item) {
                    disable = disable ? disable : item === config.dataIndex;
                });
                if(!disable) {
                    menu.add(new Ext.menu.CheckItem({
                         text:config.header
                        ,hideOnClick:false
                        ,checked:'all' === this.checkIndexes
                        ,dataIndex:config.dataIndex
                    }));
                }
            }
        }, this);
        // }}}
        // {{{
        // check items
        if(this.checkIndexes instanceof Array) {
            Ext.each(this.checkIndexes, function(di) {
                var item = menu.items.find(function(itm) {
                    return itm.dataIndex === di;
                });
                if(item) {
                    item.setChecked(true, true);
                }
            }, this);
        }
        // }}}

    } // eo function reconfigure
    // }}}

}); // eo extend

// eof