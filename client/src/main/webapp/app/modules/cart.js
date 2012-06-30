define([
    "namespace",

    // Libs
    "jquery",
    "jquerym",
    "use!backbone",

    // Modules

    // Plugins

    // Templates
    "text!templates/cart.html",
    "text!templates/message.html"
],

function( namespace, $, $m, Backbone, cartTemplate, messageTemplate ) {

    // Create a new module
    var Cart = namespace.module();

    // Model representing a shopping cart
    Cart.CartModel = Backbone.Model.extend({
        urlRoot: namespace.serviceURL + "/cart/",
        url: function() {
            return this.urlRoot + $.parseJSON( localStorage.getItem( "user" ) ).id;
        }
    });

    // Shopping cart page view
    Cart.Views.CartPage = Backbone.View.extend({
        cart: new Cart.CartModel,
        done: null,

        events: {
            "click a.remove-button": "removeItem",
            "click a.checkout-button": "checkout",
        },

        render: function( done ) {
            this.done = done;
            this.getCart( done );
        },

        removeItem: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var user = $.parseJSON( localStorage.getItem( "user" ) ).id,
                cart = $.parseJSON( localStorage.getItem( "cart" ) ),
                itemID = $( event.target ).closest( ".remove-button" ).data( "item-id" ),
                view = this;

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/cart/" + user + "/removeitem",
                type: "POST",
                data: '{"id":' + itemID + '}',
                success: function() {
                    cart.cartCount--;
                    localStorage.setItem( "cart", JSON.stringify( cart ) );
                    view.getCart();
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "INVALID_ITEM":
                            namespace.showMessageDialog( "#main", "#buyer/cart", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "STORE_CLOSED":
                            namespace.showMessageDialog( "#main", "#buyer/catalog", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        default:
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                    }
                    
                },
                done: function() {
                    $m.hidePageLoadingMsg();
                }
            });
        },

        checkout: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var user = $.parseJSON( localStorage.getItem( "user" ) ).id,
                cart = $.parseJSON( localStorage.getItem( "cart" ) ),
                cartID = $( event.target ).closest( ".checkout-button" ).data( "cart-id" ),
                message;

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/cart/" + user + "/checkout",
                type: "POST",
                data: '{"id":' + cartID + '}',
                success: function() {
                    namespace.showMessageDialog( "#main", "#buyer/role", messageTemplate, "Your Order has been Submitted", 2000, namespace.app.router );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "STORE_CLOSED":
                            namespace.showMessageDialog( "#main", "#buyer/catalog", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        default:
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, $.parseJSON( jqXHR.responseText ).message, 2000, namespace.app.router );
                            break;
                    }
                }
            }).done( function() {
                cart.cartCount = 0;
                localStorage.setItem( "cart", JSON.stringify( cart ) );
                $m.hidePageLoadingMsg();
            });
        },

        getCart: function() {
            var view = this;

            view.cart.fetch({
                success: function( model, response ) {
                    view.el.innerHTML = _.template( cartTemplate, { cart: view.cart } );
                    
                    // If a done function is passed, pass it the updated element for processing, display, etc.
                    if ( _.isFunction( view.done ) ) {
                        view.done( view.el );
                        view.delegateEvents( view.events );
                    }
                },
                error: function( model, response ) {
                    response = $.parseJSON( response.responseText );
                    switch ( response.type ) {
                        case "STORE_CLOSED":
                            namespace.showMessageDialog( "#main", "#buyer/catalog", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        default:
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                    }
                }
            });
        }
    });

    // Required, return the module for AMD compliance
    return Cart;

});
