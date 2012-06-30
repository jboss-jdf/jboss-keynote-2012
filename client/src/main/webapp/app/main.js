require([
    "namespace",

    // Libs
    "jquery",
    "jquerym",
    "use!backbone",

    // Modules
    "modules/main",
    "modules/catalog",
    "modules/cart",
    "modules/orders",

    // Templates
    "text!templates/message.html"
],

function( namespace, $, $m, Backbone, Main, Catalog, Cart, Orders, messageTemplate ) {

    // Use Backbone's routing instead of jQuery Mobile's
    $m.hashListeningEnabled = false;
    $m.pushStateEnabled = false;
    $m.linkBindingEnabled = false;

    // Define all routes for the application
    var Router = Backbone.Router.extend({
        routes: {
            "logout": "logout",
            "": "index",
            ":buyer": "index",
            "buyer/role": "role",
            "buyer/catalog": "catalog",
            "buyer/catalog/:catID": "items",
            "buyer/item/:catID/:itemID": "itemDetail",
            "approver/item/:catID/:itemID": "itemDetail",
            "buyer/cart": "cart",
            "buyer/checkout/:cartID": "checkout",
            ":approver": "index",
            "approver/role": "role",
            ":approver/orders": "orders",
            ":vp": "index",
            "vp/role": "role",
            ":vp/orders": "orders"
        },

        index: function( hash ) {
            var localUser = localStorage.getItem( "user" );
            if ( !localUser ) {
                namespace.transitioner( new Main.Views.Registration( hash ? hash.toUpperCase() : "BUYER" ), $( "#main" ) );
            } else {
                this.role();
            }
        },

        role: function() {
            namespace.transitioner( new Main.Views.Role(), $( "#main" ) );
        },

        catalog: function() {
            namespace.transitioner( new Catalog.Views.Categories, $( "#main" ) );
        },

        items: function( catID ) {
            namespace.transitioner( new Catalog.Views.ItemList( catID ), $( "#main" ) );
        },

        itemDetail: function( catID, itemID ) {
            namespace.transitioner( new Catalog.Views.ItemPage( catID, itemID ), $( "#main" ) );
        },

        cart: function() {
            namespace.transitioner( new Cart.Views.CartPage, $( "#main" ) );
        },

        orders: function( hash ) {
            namespace.transitioner( new Orders.Views.OpenOrderView( hash ), $( "#main" ) );
        },

        logout: function() {
            namespace.clearStorage();
            window.location = "./";
        }
    });

    // Shorthand the application namespace
    var app = namespace.app;

    // Treat the jQuery ready function as the entry point to the application.
    $(function() {
        // Set jQuery's AJAX header globally since we will usually be transferring JSON
        $.ajaxSetup({
            contentType: "application/json",
            headers: {
                "Accept": "application/json"
            }
        });

        // Instantiate the router
        app.router = new Router();

        // Trigger the initial route
        Backbone.history.start();
    });

});
