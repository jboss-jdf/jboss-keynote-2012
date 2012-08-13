define([
    "namespace",

    // Libs
    "jquery",
    "jquerym",
    "use!backbone",

    // Modules

    // Plugins

    // Templates
    "text!templates/catalog.html",
    "text!templates/items.html",
    "text!templates/item.html",
    "text!templates/message.html"
],

function( namespace, $, $m, Backbone, catalogTemplate, itemsTemplate, itemTemplate, messageTemplate ) {

    // Create a new catalog module
    var Catalog = namespace.module();

    // Catalog model
    Catalog.FullCatalog = Backbone.Model.extend({});

    var localCatalog = localStorage.getItem( "catalog" );

    // Category view
    Catalog.Views.Categories = Backbone.View.extend({

        render: function( done ) {
            var timestamp = new Date().getTime(),
                catalogTimestamp = localStorage.getItem( "catalogTimestamp" ),
                view = this;
            //If there is no catalog in localstorage or the cached catalog is older than 1 hour, refresh
            if ( localCatalog && timestamp - catalogTimestamp <= 3600000 ) {
                Catalog.renderList( catalogTemplate, $.parseJSON( localCatalog ), view, done );
            } else {
                // Build the cache and then once complete, render the appropriate view
                Catalog.buildCache().done( function() {
                    Catalog.renderList( catalogTemplate, $.parseJSON( localCatalog ), view, done );
                });
            }
        }
    });

    // Item list view
    Catalog.Views.ItemList = Backbone.View.extend({
        events: {
            "click a.add-button": "addItem"
        },

        initialize: function( catID ) {
            // Store the current category ID on the view instance
            this.catID = catID;
        },

        render: function( done ) {
            var timestamp = new Date().getTime(),
                catalogTimestamp = localStorage.getItem( "catalogTimestamp" ),
                view = this;

            if ( localCatalog && timestamp - catalogTimestamp <= 3600000 ) {
                Catalog.renderList( itemsTemplate, $.parseJSON( localCatalog )[ view.catID ].items, view, done );
            } else {
                // Handle direct links to category before catalog is cached
                // Build the cache and then once complete, render the appropriate view
                Catalog.buildCache().done( function() {
                    Catalog.renderList( itemsTemplate, $.parseJSON( localCatalog )[ view.catID ].items, view, done );
                });
            }
        },

        addItem: function( event ) {
            Catalog.addItem( event );
        }
    });

    // Individual item view
    Catalog.Views.ItemPage = Backbone.View.extend({
        events: {
            "click a.add-button": "addItem"
        },

        initialize: function( catID, itemID ) {
            // Store the current category and item ID's on the view instance
            this.catID = catID;
            this.itemID = itemID;
        },

        render: function( done ) {
            var timestamp = new Date().getTime(),
                catalogTimestamp = localStorage.getItem( "catalogTimestamp" ),
                view = this;

            if ( localCatalog && timestamp - catalogTimestamp <= 3600000 ) {
                Catalog.renderList( itemTemplate, $.parseJSON( localCatalog )[ view.catID ].items[ view.itemID ], view, done );
            } else {
                // Handle direct links to item before catalog is cached
                // Build the cache and then once complete, render the appropriate view
                Catalog.buildCache().done( function() {
                    Catalog.renderList( itemTemplate, $.parseJSON( localCatalog )[ view.catID ].items[ view.itemID ], view, done );
                });
            }
        },

        addItem: function( event ) {
            Catalog.addItem( event );
        }
    });

    Catalog.addItem = function( event ) {
        // Don't add to browser history
        event.preventDefault();

        var user = $.parseJSON( localStorage.getItem( "user" ) ).id,
            cart = $.parseJSON( localStorage.getItem( "cart" ) ),
            itemID = $( event.target ).closest( ".add-button" ).data( "item-id" );

        $m.showPageLoadingMsg();
        $.ajax({
            url: namespace.serviceURL + "/cart/" + user + "/additem",
            type: "POST",
            data: '{"id":' + itemID + '}',
            success: function() {
                cart.cartCount++;
                localStorage.setItem( "cart", JSON.stringify( cart ) );
                namespace.app.router.navigate( "#buyer/cart", { trigger: true } );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                var response = $.parseJSON( jqXHR.responseText );
                switch ( response.type ) {
                    case "INVALID_ITEM":
                    case "STORE_CLOSED":
                        namespace.showMessageDialog( "#main", "#buyer/catalog", messageTemplate, response.message, 2000, namespace.app.router );
                        break;
                    default:
                        // INVALID_USER errors are also captured here
                        namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                        break;
                }
                
            },
            done: function() {
                $m.hidePageLoadingMsg();
            }
        });
    };

    // Shared view renderer for all catalog views
    Catalog.renderList = function( template, items, view, done ) {
        view.el.innerHTML = _.template( template, { "items": items } );
        
        // If a done function is passed, pass it the updated element for processing, display, etc.
        if ( _.isFunction( done ) ) {
            done( view.el );
        }
    };

    // Cache the catalog to prevent unnecessary REST calls and reload if it's more than one hour old
    Catalog.buildCache = function() {
        // Return the jqXHR which implements the Promise interface. We can use that interface to act
        // on the returned data in our render functions using jQuery's deferred.done()
        return $.ajax({
            url: namespace.serviceURL + "/category",
            // Our services expect a different Accept header in order to return the full catalog
            headers: {
                "Accept": "application/vnd.categorymap+json"
            },
            dataType: "json",
            success: function( data, response ) {
                var timestamp = new Date().getTime();
                localStorage.setItem( "catalogTimestamp", timestamp );
                localCatalog = JSON.stringify( data );
                localStorage.setItem( "catalog", localCatalog );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                // TODO: Add better offline handling in the future
                if ( !localCatalog ) {
                    namespace.showMessageDialog( "#main", "#buyer/role", messageTemplate, $.parseJSON( jqXHR.responseText ).message, 2000, namespace.app.router );
                }
            }
        });
    };

    // Required, return the module for AMD compliance
    return Catalog;

});
