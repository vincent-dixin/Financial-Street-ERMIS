/**
 * @author zhengjunxiang
 * simulating the server ajax by the client.
 * 实现所有的后台处理函数变成前台处理函数
 */
Ext.define('Ext.ux.data.ServerToClientProxy', {
    requires: ['Ext.util.MixedCollection', 'Ext.Ajax'],
    extend: 'Ext.data.proxy.Server',
    alias: 'proxy.ajax',
    alternateClassName: ['Ext.data.HttpProxy', 'Ext.data.AjaxProxy'],
    
    /**
     * @property {Object} actionMethods
     * Mapping of action name to HTTP request method. In the basic AjaxProxy these are set to 'GET' for 'read' actions
     * and 'POST' for 'create', 'update' and 'destroy' actions. The {@link Ext.data.proxy.Rest} maps these to the
     * correct RESTful methods.
     */
    actionMethods: {
        create : 'POST',
        read   : 'GET',
        update : 'POST',
        destroy: 'POST'
    },
    
    /**
     * @cfg {Object} headers
     * Any headers to add to the Ajax request. Defaults to undefined.
     */
    
    doRequest: function(operation, callback, scope) {
        var writer  = this.getWriter(),
            request = this.buildRequest(operation, callback, scope);
            
        if (operation.allowWrite()) {
            request = writer.write(request);
        }
        
        Ext.apply(request, {
            headers       : this.headers,
            timeout       : this.timeout,
            scope         : this,
            callback      : this.createRequestCallback(request, operation, callback, scope),
            method        : this.getMethod(request),
            disableCaching: false // explicitly set it to false, ServerProxy handles caching
        });
        
        Ext.Ajax.request(request);
        
        return request;
    },
    /**
     * override parent class and to do some customer actions
     */
    processResponse: function(success, operation, request, response, callback, scope) {
        var me = this,
            reader,
            result;

        /**
         * invoke callback function and return the data
         */
        var index = request.url.indexOf("?");
        if(index!=-1){
        	var str = request.url.substr(index+1);
            strs = str.split("&");
            for(var i = 0; i < strs.length; i ++) {
            	request.params[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
            }
        }
		response.responseText = me.myparent[me.mycallback](request.params);
		/**
		 * end
		 */
		
        if (success === true) {
            reader = me.getReader();

            // Apply defaults to incoming data only for read operations.
            // For create and update, there will already be a client-side record
            // to match with which will contain any defaulted in values.
            reader.applyDefaults = operation.action === 'read';

            result = reader.read(me.extractResponseData(response));

            if (result.success !== false) {
                //see comment in buildRequest for why we include the response object here
                Ext.apply(operation, {
                    response: response,
                    resultSet: result
                });

                operation.commitRecords(result.records);
                operation.setCompleted();
                operation.setSuccessful();
            } else {
                operation.setException(result.message);
                me.fireEvent('exception', this, response, operation);
            }
        } else {
            me.setException(operation, response);
            me.fireEvent('exception', this, response, operation);
        }

        //this callback is the one that was passed to the 'read' or 'write' function above
        if (typeof callback == 'function') {
            callback.call(scope || me, operation);
        }

        me.afterRequest(request, success);
    },
    /**
     * Returns the HTTP method name for a given request. By default this returns based on a lookup on
     * {@link #actionMethods}.
     * @param {Ext.data.Request} request The request object
     * @return {String} The HTTP method to use (should be one of 'GET', 'POST', 'PUT' or 'DELETE')
     */
    getMethod: function(request) {
        return this.actionMethods[request.action];
    },
    
    /**
     * @private
     * TODO: This is currently identical to the JsonPProxy version except for the return function's signature. There is a lot
     * of code duplication inside the returned function so we need to find a way to DRY this up.
     * @param {Ext.data.Request} request The Request object
     * @param {Ext.data.Operation} operation The Operation being executed
     * @param {Function} callback The callback function to be called when the request completes. This is usually the callback
     * passed to doRequest
     * @param {Object} scope The scope in which to execute the callback function
     * @return {Function} The callback function
     */
    createRequestCallback: function(request, operation, callback, scope) {
        var me = this;
        
        return function(options, success, response) {
            me.processResponse(success, operation, request, response, callback, scope);
        };
    }
}, function() {
    //backwards compatibility, remove in Ext JS 5.0
    Ext.data.HttpProxy = this;
});
